package consolescript;

import org.bukkit.Bukkit;

public class Executor {

	public String command;
	public CCommand cc;
	public int waiter;
	
	public Executor(String s, CCommand cc) {
		command = s;
		this.cc = cc;
	}
	
	public void execute() {
		String com = cc.getCommand().toLowerCase();
		boolean complete = true;
		if(!Utils.functionalCommands.contains(com)){
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
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
								script.run();
								if(cc.getArgs().length == 2){
									if(cc.getArgs()[1].toLowerCase().equalsIgnoreCase("wait")){
										complete = false;
										waiter = Bukkit.getScheduler().scheduleSyncRepeatingTask(ConsoleScript.plugin, new Runnable(){
											@Override
											public void run() {
												if(script.isDone()){
													cc.setDone();
													Bukkit.getScheduler().cancelTask(waiter);
												}
											}
										}, 1L, 5L);
									}
								}
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
