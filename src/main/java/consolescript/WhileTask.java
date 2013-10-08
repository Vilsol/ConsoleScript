package consolescript;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class WhileTask {

	public boolean isDone = false;
	public String cond;
	public String act;
	private int checker;
	private CCommand comc;
	private boolean firstrun = true;
	private ConditionChecker cc;
	
	WhileTask(String condition, String action, CommandSender sender){
		cond = condition;
		act = action;
		if(action != null) comc = new CCommand(action, null, sender, null);
		cc = new ConditionChecker(cond);
		execute();
	}
	
	public void execute() {
		if(cc.isTrue()){
			checker = Bukkit.getScheduler().scheduleSyncRepeatingTask(ConsoleScript.plugin, new Runnable(){
				@Override
				public void run() {
					if(cc.checkCondition()){
						if(comc != null){
							if(comc.isDone() || firstrun){
								firstrun = false;
								comc.reset();
								comc.execute();
							}
						}
					}else{
						isDone = true;
						Bukkit.getScheduler().cancelTask(checker);
					}
				}
			}, 5L, 5L);
		}
	}
	
}
