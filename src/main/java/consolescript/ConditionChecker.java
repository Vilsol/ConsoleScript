package consolescript;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ConditionChecker {

	private String ifcond;
	private boolean condition = false;
	private String[] div;
	
	public ConditionChecker(String ifcondition) {
		this.ifcond = ifcondition;
		this.div = this.ifcond.split(" ");
		if(div.length > 0){
			condition = checkCondition();	
		}
	}

	private boolean checkCondition() {
		if(div[0].equalsIgnoreCase("player")){
			if(div.length > 1){
				OfflinePlayer oplr = Bukkit.getOfflinePlayer(div[1]);
				Player plr = Bukkit.getPlayer(div[1]);
				if(oplr != null){
					if(div.length == 3){
						if(div[2].equalsIgnoreCase("isop")){
							return oplr.isOp();
						}else if(div[2].equalsIgnoreCase("isalive")){
							if(plr != null) return !plr.isDead();
						}else if(div[2].equalsIgnoreCase("isonline")){
							return oplr.isOnline();
						}else if(div[2].equalsIgnoreCase("issneaking")){
							if(plr != null) return plr.isSneaking();
						}else if(div[2].equalsIgnoreCase("issleeping")){
							if(plr != null) return plr.isSleeping();
						}else if(div[2].equalsIgnoreCase("issprinting")){
							if(plr != null) return plr.isSprinting();
						}else if(div[2].equalsIgnoreCase("isinvehicle")){
							if(plr != null) return plr.isInsideVehicle();
						}
					}else if(div.length == 4){
						if(div[2].equalsIgnoreCase("isinworld")){
							if(plr != null){
								if(plr.getWorld().equals(div[3])) return true;
							}
						}else if(div[2].equalsIgnoreCase("isinregion")){
							
						}
					}
				}
			}
		}else if(div[0].equalsIgnoreCase("world")){
			if(div.length > 1){
				World wld = Bukkit.getWorld(div[1]);
				if(wld != null){
					if(div.length == 3){
						if(div[2].equalsIgnoreCase("israining")){
							return wld.hasStorm();
						}else if(div[2].equalsIgnoreCase("isday")){
							return (wld.getTime() < 12000);
						}else if(div[2].equalsIgnoreCase("isnight")){
							return (wld.getTime() >= 12000);
						}
					}
				}
			}
		}
		return false;
	}

	public boolean isTrue() {
		return condition;
	}

}
