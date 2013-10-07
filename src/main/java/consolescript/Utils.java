package consolescript;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class Utils {

	public static String prefix = ChatColor.GOLD + "[C] " + ChatColor.AQUA;
	public static String prefixe = ChatColor.GOLD + "[C] " + ChatColor.DARK_RED;
	public static List<String> functionalCommands = Arrays.asList("delay", "run", "stopscript");
	
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
	
	public static void startScript(String scriptname, CommandSender sender) {
		if(doesScriptExist(scriptname)){
			if(!isScriptRunning(scriptname)){
				Script script = new Script(scriptname, ConsoleScript.plugin);
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

	public static String replaceArgs(String command, CCommand cc) {
		if(cc.sender != null){
			command = command.replaceAll("%s", cc.sender.getName());
		}
		return command;
	}
	
}
