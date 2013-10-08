package consolescript;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class CCommand {

	private String cmd;
	private boolean isDone;
	public Script script;
	public CommandSender sender;
	public CommandSender exec;
	private int waiter;
	
	public CCommand(String command, Script script, CommandSender sender, CommandSender executor){
		this.script = script;
		this.sender = sender;
		this.exec = executor;
		this.cmd = Utils.populateCommand(command, this);
	}

	public String argString(){
		for(int i = 0; i < cmd.length(); i++){
			if(cmd.substring(i, i + 1).matches("^\\s*$")){
				return cmd.substring(i + 1);
			}
		}
		return null;
	}
	
	public String[] getArgs(){
		String clearCommand = argString();
		if(clearCommand == null) return null;
		String[] args;
		args = clearCommand.split(" ");
		return args;
	}
	
	public String getCommand(){
		return cmd.split(" ")[0];
	}
	
	public void execute(){
		if(!isCondition()){
			Executor x = new Executor(cmd, this, sender);
			if(this.exec != null){
				x.setExecutor(exec);
			}
			x.execute();
		}else{
			executeCondition();
		}
	}
	
	public void executeCondition() {
		ConditionChecker cc = new ConditionChecker(getIf());
		if(cc.isTrue()){
			executeThen();
		}else{
			if(isCondition(true)){
				executeElse();
			}
		}
	}

	public boolean isInteger(int arg){
		String[] args = getArgs();
		if(args.length >= arg){
			if(Utils.isInteger(args[arg])){
				return true;
			}
		}
		return false;
	}

	public String getIf(){
		if(isCondition()){
			return cmd.split("then")[0].split("if")[1].substring(1);
		}
		return null;
	}
	
	public String getThen(){
		if(isCondition()){
			if(isCondition(true)){
				return cmd.split("then")[1].split("else")[0].substring(1);
			}else{
				return cmd.split("then")[1].split("end")[0].substring(1);
			}
		}
		return null;
	}
	
	public String getElse(){
		if(isCondition(true)){
			return cmd.split("else")[1].split("end")[0].substring(1);
		}
		return null;
	}
	
	public void executeThen(){
		CCommand x = new CCommand(getThen(), null, sender, exec);
		x.execute();
		waitUntilDone(x);
	}

	public void executeElse(){
		CCommand x = new CCommand(getElse(), null, sender, exec);
		x.execute();
		waitUntilDone(x);
	}
	
	private void waitUntilDone(final CCommand x) {
		waiter = Bukkit.getScheduler().scheduleSyncRepeatingTask(ConsoleScript.plugin, new Runnable(){
			@Override
			public void run() {
				if(x.isDone){
					Bukkit.getScheduler().cancelTask(waiter);
					setDone();
				}
			}
		}, 5L, 5L);
	}
	
	public boolean isCondition(){
		return isCondition(false);
	}
	
	public boolean isCondition(boolean checkElse) {
		if(cmd.toLowerCase().contains("if") && cmd.toLowerCase().contains("then") && cmd.toLowerCase().contains("end")){
			if(checkElse) return cmd.toLowerCase().contains("else");
			return true;
		}
		return false;
	}

	public void setDone() {
		isDone = true;
		if(script != null){
			script.next();
		}
	}
	
	public boolean isDone() {
		return isDone;
	}
	
	public void reset(){
		isDone = false;
	}
	
}
