package consolescript;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Executor {

	public String command;
	public CCommand cc;
	public int waiter;
	public Player exec;
	
	public Executor(String s, CCommand cc) {
		command = s;
		this.cc = cc;
	}
	
	public void setExecutor(Player plr){
		this.exec = plr;
	}
	
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
						if(cc.isInteger(0)){
							complete = false;
							Long delay = new Long(cc.getArgs()[0]) * 20L;
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
								final Script script = new Script(cc.getArgs()[0], ConsoleScript.plugin);
								if(cc.getArgs().length > 1){
									if(Utils.listContains(cc.getArgs(), "wait")){
										complete = false;
										waiter = Bukkit.getScheduler().scheduleSyncRepeatingTask(ConsoleScript.plugin, new Runnable(){
											@Override
											public void run() {
												if(script.isDone()){
													cc.setDone();
													Bukkit.getScheduler().cancelTask(waiter);
												}
											}
										}, 5L, 5L);
									}
									if(cc.getArgs().length == 4){
										if(cc.getArgs()[1].toLowerCase().equals("as")){
											if(Bukkit.getPlayer(cc.getArgs()[2]) != null){
												script.setExecutor(Bukkit.getPlayer(cc.getArgs()[2]));
											}
										}
									}else if(cc.getArgs().length == 3){
										if(cc.getArgs()[1].toLowerCase().equals("as")){
											if(Bukkit.getPlayer(cc.getArgs()[2]) != null){
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
			}
		}
		if(complete){
			cc.setDone();
		}
	}
	
}
