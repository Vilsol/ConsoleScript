package consolescript;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Executor {

	public String command;
	public CCommand cc;
	public int waiter;
	public CommandSender exec;
	public CommandSender sender;
	
	public Executor(String s, CCommand cc, CommandSender sender) {
		command = s;
		this.cc = cc;
		this.sender = sender;
	}
	
	public void setExecutor(CommandSender plr){
		this.exec = plr;
	}
	
	@SuppressWarnings("deprecation")
	public void execute() {
		String com = cc.getCommand().toLowerCase();
		boolean complete = true;
		if(!Utils.functionalCommands.contains(com)){
			if(this.exec != null){
				Bukkit.dispatchCommand(exec, command);
			}else{
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
			}
			cc.setDone();
			return;
		}else{
			if(com.equalsIgnoreCase("delay")){
				if(cc.getArgs() != null && cc.getArgs().length > 0){
					if(cc.getArgs().length == 1){
						if(cc.isInteger(0) || Utils.isInteger(cc.getArgs()[0].substring(0, cc.getArgs()[0].length() - 1))){
							complete = false;
							Long delay = 20L;
							if(cc.isInteger(0)){
								delay = new Long(cc.getArgs()[0]) * 20L;
							}else{
								delay = new Long(Integer.parseInt(cc.getArgs()[0].substring(0, cc.getArgs()[0].length() - 1)));
							}
							Bukkit.getScheduler().scheduleSyncDelayedTask(ConsoleScript.plugin, new Runnable(){
								@Override
								public void run() {
									cc.setDone();
								}

							}, delay);
						}
					}
				}
			}else if(com.equalsIgnoreCase("run")){
				if(cc.getArgs().length > 0){
					if(Utils.doesScriptExist(cc.getArgs()[0])){
						if(Utils.doesScriptExist(cc.getArgs()[0])){
							if(!Utils.isScriptRunning(cc.getArgs()[0])){
								final Script script = new Script(cc.getArgs()[0], ConsoleScript.plugin, true);
								if(sender != null){
									script.setSender(sender);
								}
								if(cc.getArgs().length > 1){
									if(Utils.listContains(cc.getArgs(), "wait")){
										complete = false;
										waiter = Bukkit.getScheduler().scheduleSyncRepeatingTask(ConsoleScript.plugin, new Runnable(){
											@Override
											public void run() {
												if(script.isDone()){
													Bukkit.getScheduler().cancelTask(waiter);
													cc.setDone();
												}
											}
										}, 5L, 5L);
									}
									if(cc.getArgs().length >= 3){
										if(cc.getArgs()[1].toLowerCase().equals("as")){
											if(cc.getArgs()[2] == "console"){
												script.setExecutor(Bukkit.getConsoleSender());
											}else if(Bukkit.getPlayer(cc.getArgs()[2]) != null){
												script.setExecutor(Bukkit.getPlayer(cc.getArgs()[2]));
											}
										}
									}
								}
								script.run();
							}
						}
					}
				}
			}else if(com.equalsIgnoreCase("stopscript")){
				Utils.stopScript(cc.script.name, null, false);
			}else if(com.equalsIgnoreCase("while")){
				if(cc.getArgs().length > 3){
					if(cc.argString().contains("do") && cc.argString().contains("end")){
						final WhileTask wt = new WhileTask(cc.argString().split("do")[0], cc.argString().split("do")[1].substring(1), sender);
						waiter = Bukkit.getScheduler().scheduleSyncRepeatingTask(ConsoleScript.plugin, new Runnable(){
							@Override
							public void run() {
								if(cc.run){
									if(wt.isDone){
										Bukkit.getScheduler().cancelTask(waiter);
										cc.setDone();
									}
								}else{
									wt.stop();
								}
							}
						}, 5L, 5L);
						complete = false;
					}
				}
			}else if(com.equalsIgnoreCase("wait")){
				if(cc.getArgs().length > 3){
					if(cc.argString().contains("until") && cc.argString().contains("end")){
						final ConditionChecker cond = new ConditionChecker(cc.argString().split("end")[0].split("until")[1].substring(1));
						waiter = Bukkit.getScheduler().scheduleSyncRepeatingTask(ConsoleScript.plugin, new Runnable(){
							@Override
							public void run() {
								if(cond.checkCondition() || ! cc.run){
									Bukkit.getScheduler().cancelTask(waiter);
									cc.setDone();
								}
							}
						}, 5L, 5L);
						complete = false;
					}
				}
			}else if(com.equalsIgnoreCase("setblock")){
				if(cc.getArgs().length == 5){
					if(cc.isInteger(1) && cc.isInteger(2) && cc.isInteger(3) && cc.isInteger(4)){
						World wld = Bukkit.getWorld(cc.getArgs()[0]);
						if(wld != null){
							if(Material.getMaterial(Integer.parseInt(cc.getArgs()[4])) != null){
								wld.getBlockAt(Integer.parseInt(cc.getArgs()[1]), Integer.parseInt(cc.getArgs()[2]), Integer.parseInt(cc.getArgs()[3])).setTypeId(Integer.parseInt(cc.getArgs()[4]));
							}
						}
					}
				}
			}else if(com.equalsIgnoreCase("set") || com.equalsIgnoreCase("setl")){
				boolean global = true;
				String setting = null;
				if(com.equalsIgnoreCase("setl")) global = false;
				if(cc.getArgs().length >= 4){
					if(cc.getArgs()[1].equalsIgnoreCase("world")){
						World wld = Bukkit.getWorld(cc.getArgs()[2]);
						if(wld != null){
							WorldCounter WC = new WorldCounter(wld, cc.getArgs(), -1, -1);
							setting = WC.count + "";
						}
					}else if(cc.getArgs()[1].equalsIgnoreCase("region")){
						if(cc.getArgs()[2].contains("-")){
							World wld = Bukkit.getWorld(cc.getArgs()[2].split("-")[0]);
							if(wld != null){
								ProtectedRegion reg = WGBukkit.getRegionManager(wld).getRegion(cc.getArgs()[2].split("-")[1]);
								if(reg != null){
									RegionCounter RC = new RegionCounter(wld, reg, cc.getArgs(), -1, -1);
									setting = RC.count + "";
								}
							}
						}
					}else if(cc.getArgs()[1].equalsIgnoreCase("random")){
						if(Utils.isInteger(cc.getArgs()[2]) && Utils.isInteger(cc.getArgs()[3])){
							int Min = Integer.parseInt(cc.getArgs()[2]);
							int Max = Integer.parseInt(cc.getArgs()[3]);
							setting = (Min + (int)(Math.random() * ((Max - Min) + 1))) + "";
						}
					}
				}
				
				if(setting != null){
					if(global){
						Utils.setGlobalVariable(cc.getArgs()[0], setting);
					}else{
						Utils.setLocalVariable(cc.script.name, cc.getArgs()[0], setting);
					}
				}
			}else if(com.equalsIgnoreCase("region")){
				if(cc.getArgs().length >= 2){
					if(cc.getArgs()[0].contains("-")){
						World wld = Bukkit.getWorld(cc.getArgs()[0].split("-")[0]);
						if(wld != null){
							ProtectedRegion reg = WGBukkit.getRegionManager(wld).getRegion(cc.getArgs()[0].split("-")[1]);
							if(reg != null){
								if(cc.getArgs()[1].equalsIgnoreCase("killallmobs")){
									for(Entity e : Utils.getRegionEntities(reg, wld)){
										if(e instanceof Monster){
											if(e.isValid()) e.remove();
										}
									}
								}else if(cc.getArgs()[1].equalsIgnoreCase("killallanimals")){
									for(Entity e : Utils.getRegionEntities(reg, wld)){
										if(e instanceof Animals){
											if(e.isValid()) e.remove();
										}
									}
								}else if(cc.getArgs()[1].equalsIgnoreCase("removeallitems")){
									for(Entity e : Utils.getRegionEntities(reg, wld)){
										if(e instanceof Item){
											if(e.isValid()) e.remove();
										}
									}
								}else if(cc.getArgs()[1].equalsIgnoreCase("removeeverything")){
									for(Entity e : Utils.getRegionEntities(reg, wld)){
										if(e.isValid()) e.remove();
									}
								}
							}
						}
					}
				}
			}else if(com.equalsIgnoreCase("dropitem")){
				if(cc.getArgs().length >= 6){
					World wld = Bukkit.getWorld(cc.getArgs()[0]);
					if(wld != null){
						if(cc.isInteger(1) && cc.isInteger(2) && cc.isInteger(3) && cc.isInteger(4) && cc.isInteger(5)){
							Location dropLocation = new Location(wld, Integer.parseInt(cc.getArgs()[1]), Integer.parseInt(cc.getArgs()[2]), Integer.parseInt(cc.getArgs()[3]));
							ItemStack it = new ItemStack(Integer.parseInt(cc.getArgs()[4]), Integer.parseInt(cc.getArgs()[5]));
							wld.dropItem(dropLocation, it);
						}
					}
				}
			}
		}
		if(complete){
			cc.setDone();
		}
	}
	
}
