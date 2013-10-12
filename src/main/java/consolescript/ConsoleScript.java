package consolescript;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Joiner;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class ConsoleScript extends JavaPlugin {

	public static Map<String, Script> runningScripts = new HashMap<String, Script>();
	public static ConsoleScript plugin;
	public static Plugin WorldGuard;
	
	public void onEnable(){
		loadConfig();
		plugin = this;
		loadWorldGuard();
	}
	
	private void loadWorldGuard() {
		WorldGuard = getServer().getPluginManager().getPlugin("WorldGuard");
	    if (WorldGuard == null || !(WorldGuard instanceof WorldGuardPlugin)){
	    	getLogger().warning("WorldGuard not found!");
	    }
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
					Utils.noPerms(sender);
				}
			}else if(com.equalsIgnoreCase("stop")){
				if(args.length >= 2){
					if(sender.hasPermission("cscript.stop") || sender.hasPermission("cscript.stop." + args[1])){
						Utils.stopScript(args[1], sender);
					}else{
						sender.sendMessage(Utils.prefixe + "You don't have permission to use this command!");
					}
				}else{
					Utils.noPerms(sender);
				}
			}else if(com.equalsIgnoreCase("stopall")){
				if(sender.hasPermission("cscript.stopall")){
					Utils.stopAllScripts();
					sender.sendMessage(Utils.prefix + "All scripts stopped!");
				}else{
					Utils.noPerms(sender);
				}
			}else if(com.equalsIgnoreCase("reload")){
				if(sender.hasPermission("cscript.reload")){
					reloadConfig();
					sender.sendMessage(Utils.prefix + "Configuration reloaded!");
				}else{
					Utils.noPerms(sender);
				}
			}else if(com.equalsIgnoreCase("list")){
				if(sender.hasPermission("cscript.list")){
					Set<String> list = getConfig().getConfigurationSection("Scripts.").getKeys(false);
					if(list != null){
						sender.sendMessage(Utils.prefix + "Available scripts: " + ChatColor.GREEN + Joiner.on(ChatColor.AQUA + ", " + ChatColor.GREEN).join(list));
					}else{
						sender.sendMessage(Utils.prefixe + "No scripts defined!");
					}
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
