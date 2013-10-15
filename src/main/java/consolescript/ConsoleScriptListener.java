package consolescript;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

@SuppressWarnings("deprecation")
public class ConsoleScriptListener implements Listener {

	@EventHandler
	public void onEntityDeath(EntityDeathEvent evt){
		if(ConsoleScript.customEntities.containsKey(evt.getEntity().getEntityId())){
			Location dropLocation = evt.getEntity().getLocation().add(new Vector(0, 1, 0));
			for(String s : ConsoleScript.customEntities.get(evt.getEntity().getEntityId())){
				if(Utils.isInteger(s)){
					if(Material.getMaterial(Integer.parseInt(s)) != null){
						ItemStack i = new ItemStack(Integer.parseInt(s), 1);
						if(i != null) evt.getEntity().getWorld().dropItem(dropLocation, i);
					}
				}else if(Utils.isCustomItem(s)){
					CustomItem ci = new CustomItem(s);
					evt.getEntity().getWorld().dropItem(dropLocation, ci.getItemStack());
				}
			}
			ConsoleScript.customEntities.remove(evt.getEntity().getEntityId());
		}
	}
	
}
