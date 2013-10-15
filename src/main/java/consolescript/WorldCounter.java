package consolescript;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

public class WorldCounter {

	public int count = 0;
	
	@SuppressWarnings("deprecation")
	public WorldCounter(World wld, String[]div, int j, int l){
		if(div[4 + j].equalsIgnoreCase("player")){
			if(wld.getPlayers() != null){
				if(div.length == 6 + l){
					for(Player p : wld.getPlayers()){
						if(p.getName().equalsIgnoreCase(div[5 + j])){
							count++;
						}
					}
				}else{
					count = wld.getPlayers().size();
				}
			}
		}else if(div[4 + j].equalsIgnoreCase("mob")){
			if(div.length == 6 + l){
				if(Utils.isEntity(div[5 + j])){
					for(Entity e : wld.getEntities()){
						if(e.getType().name().toLowerCase().equalsIgnoreCase(div[5 + j])){
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
		}else if(div[4 + j].equalsIgnoreCase("animal")){
			if(div.length == 6 + l){
				if(Utils.isEntity(div[5 + j])){
					for(Entity e : wld.getEntities()){
						if(e.getType().name().toLowerCase().equalsIgnoreCase(div[5 + j])){
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
		}else if(div[4 + j].equalsIgnoreCase("item")){
			if(div.length == 6 + l){
				if(Utils.isInteger(div[5 + j]) && Material.getMaterial(Integer.parseInt(div[5 + j])) != null){
					for(Entity e : wld.getEntities()){
						if(e instanceof Item){
							Item i = (Item) e;
							if(i.getItemStack().getType().equals(Material.getMaterial(Integer.parseInt(div[5 + j])))){
								count += i.getItemStack().getAmount();
							}
						}
					}
				}
			}else{
				for(Entity e : wld.getEntities()){
					if(e instanceof Item){
						Item i = (Item) e;
						count += i.getItemStack().getAmount();
					}
				}
			}
		}else if(div[4 + j].equalsIgnoreCase("custommob")){
			if(div.length == 6 + l){
				if(Utils.isCustomEntity(div[5 + j])){
					for(Entity e : wld.getEntities()){
						if(e.hasMetadata("IAmFromConsoleScript")){
							if(e.getMetadata("IAmFromConsoleScript").get(0).asString().equalsIgnoreCase(div[5 + j])){
								count++;
							}
						}
					}
				}
			}
		}else if(div[4 + j].equalsIgnoreCase("customitem")){
			if(div.length == 6 + l){
				for(Entity e : wld.getEntities()){
					if(e instanceof Item){
						if(e.hasMetadata("IAmFromConsoleScript")){
							if(e.getMetadata("IAmFromConsoleScript").get(0).asString().equalsIgnoreCase(div[5 + j])){
								count++;
							}
						}
					}
				}
			}
		}
		
	}
	
}
