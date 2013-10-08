package consolescript;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConsoleScript extends JavaPlugin {

	public static Map<String, Script> runningScripts = new HashMap<String, Script>();
	public static ConsoleScript plugin;
	
	public void onEnable(){
		loadConfig();
		plugin = this;
	}
	
	private void loadConfig() {
		InputStream defConfigStream = getResource("config.yml");
    	YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		getConfig().setDefaults(defConfig);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	public void onDisable(){
		Utils.stopAllScripts();
	}
	
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		if(args.length > 0){
			String com = args[0];
			if(com.equalsIgnoreCase("run")){
				if(args.length >= 2){
					if(sender.hasPermission("cscript.run") || sender.hasPermission("cscript.run." + args[1])){
						Utils.startScript(args[1], sender);
					}else{
						sender.sendMessage(Utils.prefixe + "You don't have permission to use this command!");
					}
				}else{
					sender.sendMessage(Utils.prefixe + "Please specify the script name!");
				}
			}else if(com.equalsIgnoreCase("stop")){
				if(args.length >= 2){
					if(sender.hasPermission("cscript.stop") || sender.hasPermission("cscript.stop." + args[1])){
						Utils.stopScript(args[1], sender);
					}else{
						sender.sendMessage(Utils.prefixe + "You don't have permission to use this command!");
					}
				}else{
					sender.sendMessage(Utils.prefixe + "Please specify the script name!");
				}
			}else if(com.equalsIgnoreCase("stopall")){
				if(sender.hasPermission("cscript.stopall")){
					Utils.stopAllScripts();
					sender.sendMessage(Utils.prefix + "All scripts stopped!");
				}else{
					sender.sendMessage(Utils.prefixe + "You don't have permission to use this command!");
				}
			}else if(com.equalsIgnoreCase("reload")){
				if(sender.hasPermission("cscript.reload")){
					reloadConfig();
					sender.sendMessage(Utils.prefix + "Configuration reloaded!");
				}else{
					sender.sendMessage(Utils.prefixe + "You don't have permission to use this command!");
				}
			}else{
				Utils.sendHelp(sender);
			}
		}else{
			Utils.sendHelp(sender);
		}
		return true;
	}
	
}
