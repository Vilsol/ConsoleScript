package consolescript;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RegionCounter {

	public boolean alone = true;
	public boolean plus = false;
	public int count = 0;
	
	@SuppressWarnings("deprecation")
	public RegionCounter(World wld, ProtectedRegion reg, String[] div){
		if(div[3].substring(div[3].length() - 1).equalsIgnoreCase("+")){
			alone = false;
			plus = true;
		}else if(div[3].substring(div[3].length() - 1).equalsIgnoreCase("-")){
			alone = false;
		}

		if(div[4].equalsIgnoreCase("player")){
			if(Utils.getRegionPlayers(reg, wld).size() > 0){
				if(div.length == 6){
					for(Player plr : Utils.getRegionPlayers(reg, wld)){
						if(plr.getName().equalsIgnoreCase(div[5])){
							count++;
						}
					}
				}else{
					count = Utils.getRegionPlayers(reg, wld).size();
				}
			}
		}else if(div[4].equalsIgnoreCase("mob")){
			if(div.length == 6){
				if(Utils.isEntity(div[5])){
					for(Entity e : Utils.getRegionEntities(reg, wld)){
						if(e.getType().name().equalsIgnoreCase(div[5])){
							count++;
						}
					}
				}
			}else{
				for(Entity e : Utils.getRegionEntities(reg, wld)){
					if(e instanceof Monster){
						count++;
					}
				}
			}
		}else if(div[4].equalsIgnoreCase("animal")){
			if(div.length == 6){
				if(Utils.isEntity(div[5])){
					for(Entity e : Utils.getRegionEntities(reg, wld)){
						if(e.getType().name().equalsIgnoreCase(div[5])){
							count++;
						}
					}
				}
			}else{
				for(Entity e : Utils.getRegionEntities(reg, wld)){
					if(e instanceof Animals){
						count++;
					}
				}
			}
		}else if(div[4].equalsIgnoreCase("item")){
			if(div.length == 6){
				if(Utils.isInteger(div[5]) && Material.getMaterial(Integer.parseInt(div[5])) != null){
					for(Entity e : Utils.getRegionEntities(reg, wld)){
						if(e instanceof Item){
							Item i = (Item) e;
							if(i.getItemStack().getType().equals(Material.getMaterial(Integer.parseInt(div[5])))){
								count += i.getItemStack().getAmount();
							}
						}
					}
				}
			}else{
				for(Entity e : Utils.getRegionEntities(reg, wld)){
					if(e instanceof Item){
						count++;
					}
				}
			}
		}
	}
	
}
