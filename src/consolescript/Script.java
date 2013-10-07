package consolescript;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Script {

	private String name;
	private List<String> commands;
	private CommandSender sender;
	private int line;
	private boolean run;
	
	Script(String name, ConsoleScript plugin){
		this.name = name;
		this.run = true;
		this.line = 0;
		ConsoleScript.runningScripts.put(name, this);
		this.commands = plugin.getConfig().getStringList("Scripts." + name + ".Commands");
	}

	public void setSender(CommandSender send){
		this.sender = send;
	}
	
	public void stop() {
		ConsoleScript.runningScripts.remove(name);
		run = false;
	}
	
	public void finished(){
		ConsoleScript.runningScripts.remove(name);
		run = false;
		if(sender != null) sender.sendMessage(Utils.prefix + "Script " + ChatColor.GREEN + name + ChatColor.AQUA + " finished!");
	}
	
	public void next(){
		if(!run) return;
		if(this.line < commands.size()){
			CCommand x = new CCommand(commands.get(this.line), this, sender);
			this.line += 1;
			x.execute();
		}else{
			finished();
		}
	}
	
	public void run() {
		if(sender != null) sender.sendMessage(Utils.prefix + "Script " + ChatColor.GREEN + name + ChatColor.AQUA + " started!");
		next();
	}

	public boolean isDone() {
		return !run;
	}
	
}