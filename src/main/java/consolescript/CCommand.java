package consolescript;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CCommand {

	private String cmd;
	private boolean isDone;
	public Script script;
	public CommandSender sender;
	public Player exec;
	
	public CCommand(String command, Script script, CommandSender sender, Player executor){
		this.script = script;
		this.sender = sender;
		this.exec = executor;
		this.cmd = Utils.populateCommand(command, this);
	}

	private String argString(){
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
			Executor x = new Executor(cmd, this);
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
			return cmd.split("then")[0].split("if")[1];
		}
		return null;
	}
	
	public String getThen(){
		if(isCondition()){
			if(isCondition(true)){
				return cmd.split("then")[1].split("else")[0];
			}else{
				return cmd.split("then")[1].split("end")[0];
			}
		}
		return null;
	}
	
	public String getElse(){
		if(isCondition(true)){
			return cmd.split("else")[1].split("end")[0];
		}
		return null;
	}
	
	public void executeThen(){
		Executor x = new Executor(getThen(), this);
		x.execute();
	}
	
	public void executeElse(){
		Executor x = new Executor(getElse(), this);
		x.execute();
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
	
}
