package consolescript;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

public class WorldCounter {

	public boolean alone = true;
	public boolean plus = false;
	public int count = 0;
	
	@SuppressWarnings("deprecation")
	public WorldCounter(World wld, String[]div){
		if(div[3].substring(div[3].length() - 1).equalsIgnoreCase("+")){
			alone = false;
			plus = true;
		}else if(div[3].substring(div[3].length() - 1).equalsIgnoreCase("-")){
			alone = false;
		}

		if(div[4].equalsIgnoreCase("player")){
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
				if(Utils.isInteger(div[5]) && Material.getMaterial(Integer.parseInt(div[5])) != null){
					for(Entity e : wld.getEntities()){
						if(e instanceof Item){
							Item i = (Item) e;
							if(i.getItemStack().getType().equals(Material.getMaterial(Integer.parseInt(div[5])))){
								count += i.getItemStack().getAmount();
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
		
	}
	
}
