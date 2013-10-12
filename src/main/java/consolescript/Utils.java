package consolescript;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Utils {

	public static String prefix = ChatColor.GOLD + "[C] " + ChatColor.AQUA;
	public static String prefixe = ChatColor.GOLD + "[C] " + ChatColor.DARK_RED;
	public static List<String> functionalCommands = Arrays.asList("delay", "run", "stopscript", "while", "wait", "setblock");
	
	public static boolean doesScriptExist(String scriptname){
		return ConsoleScript.plugin.getConfig().isSet("Scripts." + scriptname);
	}

	public static FileConfiguration getConfig(){
		return ConsoleScript.plugin.getConfig();
	}
	
	public static boolean isScriptRunning(String scriptname){
		return ConsoleScript.runningScripts.containsKey(scriptname);
	}
	
	public static void stopScript(String scriptname, CommandSender sender) {
		if(doesScriptExist(scriptname)){
			if(isScriptRunning(scriptname)){
				ConsoleScript.runningScripts.get(scriptname).stop();
				if(sender != null) sender.sendMessage(prefix + scriptname + " has been stopped!");
			}else{
				if(sender != null) sender.sendMessage(prefixe + scriptname + " is not running!");
			}
		}else{
			if(sender != null) sender.sendMessage(prefixe + "No such script: " + scriptname);
		}
	}

	public static void sendHelp(CommandSender sender) {
		sender.sendMessage(prefix + "Available commands:");
		sender.sendMessage(prefix + "/csript " + ChatColor.GREEN + "run" + ChatColor.AQUA + " <script> - Run a script");
		sender.sendMessage(prefix + "/csript " + ChatColor.GREEN + "stop" + ChatColor.AQUA + " <script> - Stop a script");
		sender.sendMessage(prefix + "/csript " + ChatColor.GREEN + "stopall" + ChatColor.AQUA + " - Stop all scripts");
		sender.sendMessage(prefix + "/csript " + ChatColor.GREEN + "reload" + ChatColor.AQUA + " - Reload the config");
		sender.sendMessage(prefix + "/csript " + ChatColor.GREEN + "list" + ChatColor.AQUA + " - Lists all scripts");
	}

	public static void stopAllScripts() {
		for(Script s : ConsoleScript.runningScripts.values()){
			s.stop();
		}
	}

	public static void info(String msg){
		ConsoleScript.plugin.getLogger().info(msg);
	}

	public static void warning(String msg){
		ConsoleScript.plugin.getLogger().warning(msg);
	}

	public static void severe(String msg){
		ConsoleScript.plugin.getLogger().severe(msg);
	}
	
	public static void delayMe(String seconds, final Object wake){
		Bukkit.broadcastMessage("wtf");
		Bukkit.getScheduler().scheduleSyncDelayedTask(ConsoleScript.plugin, new Runnable(){
			@Override
			public void run() {
				Bukkit.broadcastMessage("wtf2");
				wake.notify();
				Bukkit.broadcastMessage("wtf3");
			}
		}, new Long(seconds) * 20L);
	}
	
	public static void noPerms(CommandSender sender){
		sender.sendMessage(Utils.prefixe + "You don't have permission to use this command!");
	}
	
	public static boolean isEntity(String ent){
		for(EntityType e : EntityType.values()){
			if(e.name().toLowerCase().equalsIgnoreCase(ent)) return true;
		}
		return false;
	}
	
	public static void startScript(String scriptname, CommandSender sender) {
		if(doesScriptExist(scriptname)){
			if(!isScriptRunning(scriptname)){
				Script script = new Script(scriptname, ConsoleScript.plugin, false);
				if(sender != null){
					script.setSender(sender);
				}
				script.run();
			}else{
				if(sender != null){
					sender.sendMessage(prefixe + scriptname + " is already running!");
				}else{
					warning(scriptname + " is already running!");
				}
			}
		}else{
			if(sender != null){
				sender.sendMessage(prefixe + "No such script: " + scriptname);
			}else{
				warning("No such script: " + scriptname);
			}
		}
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}

	public static String populateCommand(String command, CCommand cc) {
		Player plr = null;
		
		if(cc.exec != null){
			if(cc.exec instanceof Player){
				plr = (Player) cc.exec;
			}
		}else if(cc.sender != null){
			if(cc.sender instanceof Player){
				plr = (Player) cc.sender;
			}
			command = command.replaceAll("%p", cc.sender.getName());
		}else{
			command = command.replaceAll("%w", Bukkit.getWorlds().get(0).getName());
		}
		
		if(plr != null){
			command = command.replaceAll("%p", plr.getName());
			command = command.replaceAll("%w", plr.getWorld().getName());
		}
		
		return command;
	}
	
	public static boolean listContains(String[] list, String match){
		for(String s : list){
			if(s.equals(match)){
				return true;
			}
		}
		return false;
	}
	
	public static List<Player> getRegionPlayers(ProtectedRegion reg, World wld) {
		List<Player> players = new ArrayList<Player>();
		for(Player plr : wld.getPlayers()){
			if(reg.contains(plr.getLocation().getBlockX(), plr.getLocation().getBlockY(), plr.getLocation().getBlockZ())){
				players.add(plr);
			}
		}
		return players;
	}
	
	public static List<Entity> getRegionEntities(ProtectedRegion reg, World wld) {
		List<Entity> entities = new ArrayList<Entity>();
		for(Entity ent : wld.getEntities()){
			if(reg.contains(ent.getLocation().getBlockX(), ent.getLocation().getBlockY(), ent.getLocation().getBlockZ())){
				entities.add(ent);
			}
		}
		return entities;
	}
	
}
