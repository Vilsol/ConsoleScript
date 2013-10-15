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
				String item = s.split(" ")[0];
				String amount = s.split(" ")[1];
				if(Utils.isInteger(item)){
					if(Material.getMaterial(Integer.parseInt(item)) != null){
						ItemStack i = new ItemStack(Integer.parseInt(item), 1);
						if(amount != null && Utils.isInteger(amount)) i.setAmount(Integer.parseInt(amount));
						if(i != null) evt.getEntity().getWorld().dropItem(dropLocation, i);
					}
				}else if(Utils.isCustomItem(item)){
					CustomItem ci = new CustomItem(item);
					ItemStack cu = ci.getItemStack();
					if(amount != null && Utils.isInteger(amount)) cu.setAmount(Integer.parseInt(amount));
					evt.getEntity().getWorld().dropItem(dropLocation, cu);
				}
			}
			ConsoleScript.customEntities.remove(evt.getEntity().getEntityId());
		}
	}
	
}
