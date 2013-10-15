package consolescript;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("deprecation")
public class CustomItem {

	private String name;
	private FileConfiguration c;
	
	public CustomItem(String name){
		this.name = name;
		c = ConsoleScript.plugin.getConfig();
	}
	
	public ItemStack getItemStack(){
		if(c.isSet("Items." + name + ".Item")){
			if(Utils.isInteger(c.getString("Items." + name + ".Item"))){
				ItemStack i = new ItemStack(c.getInt("Items." + name + ".Item"));
				if(c.isSet("Items." + name + ".Enchantments")){
					for(String s : c.getConfigurationSection("Items." + name + ".Enchantments.").getKeys(false)){
						if(Utils.isInteger(s) && Utils.isInteger(c.getString("Items." + name + ".Enchantments." + s))){
							if(Enchantment.getById(c.getInt("Items." + name + ".Enchantments." + s)) != null){
								i.addEnchantment(Enchantment.getById(Integer.parseInt(s)), c.getInt("Items." + name + ".Enchantments." + s));
							}
						}
					}
				}
				
				ItemMeta meta = i.getItemMeta();
			    if (meta == null) {
					meta = Bukkit.getItemFactory().getItemMeta(i.getType());
			    }
				
			    if(meta != null){
					if(c.isSet("Items." + name + ".Name")){
						meta.setDisplayName(c.getString("Items." + name + ".Name"));
					}
					
					if(c.isSet("Items." + name + ".Lore")){
						List<String> transformed = new ArrayList<String>();
						for(String s : c.getStringList("Items." + name + ".Lore")){
							transformed.add(ChatColor.translateAlternateColorCodes('&', s));
						}
						meta.setLore(transformed);
					}
					
					i.setItemMeta(meta);
			    }
			    
				return i;
			}
		}
		return null;
	}
	
}
