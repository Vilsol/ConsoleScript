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

	public int count = 0;
	
	@SuppressWarnings("deprecation")
	public RegionCounter(World wld, ProtectedRegion reg, String[] div, int j, int l){
		if(div[4 + j].equalsIgnoreCase("player")){
			if(Utils.getRegionPlayers(reg, wld).size() > 0){
				if(div.length == 6 + l){
					for(Player plr : Utils.getRegionPlayers(reg, wld)){
						if(plr.getName().equalsIgnoreCase(div[5 + j])){
							count++;
						}
					}
				}else{
					count = Utils.getRegionPlayers(reg, wld).size();
				}
			}
		}else if(div[4 + j].equalsIgnoreCase("mob")){
			if(div.length == 6 + l){
				if(Utils.isEntity(div[5 + j])){
					for(Entity e : Utils.getRegionEntities(reg, wld)){
						if(e.getType().name().equalsIgnoreCase(div[5 + j])){
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
		}else if(div[4 + j].equalsIgnoreCase("animal")){
			if(div.length == 6 + l){
				if(Utils.isEntity(div[5 + j])){
					for(Entity e : Utils.getRegionEntities(reg, wld)){
						if(e.getType().name().equalsIgnoreCase(div[5 + j])){
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
		}else if(div[4 + j].equalsIgnoreCase("item")){
			if(div.length == 6 + l){
				if(Utils.isInteger(div[5 + j]) && Material.getMaterial(Integer.parseInt(div[5 + j])) != null){
					for(Entity e : Utils.getRegionEntities(reg, wld)){
						if(e instanceof Item){
							Item i = (Item) e;
							if(i.getItemStack().getType().equals(Material.getMaterial(Integer.parseInt(div[5 + j])))){
								count += i.getItemStack().getAmount();
							}
						}
					}
				}
			}else{
				for(Entity e : Utils.getRegionEntities(reg, wld)){
					if(e instanceof Item){
						Item i = (Item) e;
						count += i.getItemStack().getAmount();
					}
				}
			}
		}
	}
	
}
