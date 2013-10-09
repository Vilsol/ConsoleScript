package consolescript;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
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

	public boolean checkCondition() {
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
					}else if(div.length == 5 || div.length == 6){
						if(div[2].equalsIgnoreCase("contains")){
							if(Utils.isInteger(div[3]) || Utils.isInteger(div[3].substring(0, div[3].length() - 1))){
								boolean alone = true;
								boolean plus = false;
								int count = 0;
								if(div[3].substring(div[3].length() - 1).equalsIgnoreCase("+")){
									alone = false;
									plus = true;
								}else if(div[3].substring(div[3].length() - 1).equalsIgnoreCase("-")){
									alone = false;
								}

								if(div[4].equalsIgnoreCase("player")){
									Bukkit.broadcastMessage(div.length + " - " + div[2] + " - " + ifcond);
									if(wld.getPlayers() != null){
										if(div.length == 6){
											for(Player p : wld.getPlayers()){
												if(p.getName().equalsIgnoreCase(div[5])){
													count++;
												}
											}
										}else{
											count = wld.getPlayers().size();
										}
									}
								}else if(div[4].equalsIgnoreCase("mob")){
									if(div.length == 6){
										if(Utils.isEntity(div[5])){
											for(Entity e : wld.getEntities()){
												if(e.getType().name().toLowerCase().equalsIgnoreCase(div[5])){
													count++;
												}
											}
										}
									}else{
										for(Entity e : wld.getEntities()){
											if(e instanceof Monster){
												count++;
											}
										}
									}
								}else if(div[4].equalsIgnoreCase("animal")){
									if(div.length == 6){
										if(Utils.isEntity(div[5])){
											for(Entity e : wld.getEntities()){
												if(e.getType().name().toLowerCase().equalsIgnoreCase(div[5])){
													count++;
												}
											}
										}
									}else{
										for(Entity e : wld.getEntities()){
											if(e instanceof Animals){
												count++;
											}
										}
									}
								}else if(div[4].equalsIgnoreCase("item")){
									if(div.length == 6){
										if(Utils.isInteger(div[5]) && Material.getMaterial(div[5]) != null){
											for(Entity e : wld.getEntities()){
												if(e instanceof Item){
													Item i = (Item) e;
													if(i.getItemStack().getType().equals(Material.getMaterial(div[5]))){
														count++;
													}
												}
											}
										}
									}else{
										for(Entity e : wld.getEntities()){
											if(e instanceof Item){
												count++;
											}
										}
									}
								}
								Bukkit.broadcastMessage(count + " - " + div[3]);
								if(alone){
									return (Integer.parseInt(div[3]) == count);
								}else{
									if(plus){
										return (Integer.parseInt(div[3].substring(0, div[3].length() - 1)) <= count);
									}else{
										return (Integer.parseInt(div[3].substring(0, div[3].length() - 1)) >= count);
									}
								}
								
							}
						}
					}
				}
			}
		}else if(div[0].equalsIgnoreCase("region")){
			if(div.length == 5 || div.length == 6){
				if(div[2].equalsIgnoreCase("contains")){
					if(Utils.isInteger(div[3]) || Utils.isInteger(div[3].substring(0, div[3].length() - 1))){
						boolean alone = true;
						boolean plus = false;
						int count = 0;
						if(div[3].substring(div[3].length() - 1).equalsIgnoreCase("+")){
							alone = false;
							plus = true;
						}else if(div[3].substring(div[3].length() - 1).equalsIgnoreCase("-")){
							alone = false;
						}

						if(div[4].equalsIgnoreCase("player")){
							// TODO Placeholder for regions
						}else if(div[4].equalsIgnoreCase("mob")){
							if(div.length == 6){
								if(Utils.isEntity(div[5])){
									// TODO Placeholder for regions
								}
							}else{
								// TODO Placeholder for regions
							}
						}else if(div[4].equalsIgnoreCase("animal")){
							if(div.length == 6){
								if(Utils.isEntity(div[5])){
									// TODO Placeholder for regions
								}
							}else{
								// TODO Placeholder for regions
							}
						}else if(div[4].equalsIgnoreCase("item")){
							if(div.length == 6){
								if(Utils.isInteger(div[5]) && Material.getMaterial(div[5]) != null){
									// TODO Placeholder for regions
								}
							}else{
								// TODO Placeholder for regions
							}
						}
						
						if(alone){
							return (Integer.parseInt(div[3]) == count);
						}else{
							if(plus){
								return (Integer.parseInt(div[3]) >= count);
							}else{
								return (Integer.parseInt(div[3]) <= count);
							}
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
