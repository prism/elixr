package me.botsko.elixr;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemUtils {
	
	
	/**
	 * Returns a proper full name for an item, which includes meta content as well.
	 * @return string
	 */
	public static String getItemFullNiceName( ItemStack item, MaterialAliases aliases ){
		
		String item_name = "";
		
		// Leather Coloring
		if(item.getType().name().contains("LEATHER_")){
			LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();
			if(lam.getColor() != null){
				item_name += "dyed ";
			}
		}
		
		// Skull Owner
		else if(item.getType().equals(Material.SKULL_ITEM)){
			SkullMeta skull = (SkullMeta) item.getItemMeta();
			if(skull.hasOwner()){
				item_name += skull.getOwner() + "'s ";
			}
		}
		
		// Set the base item name
		if(BlockUtils.hasSubitems(item.getTypeId())){
			item_name += aliases.getItemStackAliasById(item.getTypeId(), (byte)item.getDurability());
		} else {
			item_name += aliases.getItemStackAliasById(item.getTypeId(), (byte)0);
		}
		if(item_name.isEmpty()){
			item_name += item.getType().toString().toLowerCase().replace("_", " ");
		}
		
		// Written books
		if(item.getType().equals( Material.WRITTEN_BOOK )){
	        BookMeta meta = (BookMeta) item.getItemMeta();
			if(meta != null){
				item_name += " '" + meta.getTitle() + "' by " + meta.getAuthor();
			}
		}
		
		// Enchanted books
		else if(item.getType().equals( Material.ENCHANTED_BOOK )){
			EnchantmentStorageMeta bookEnchantments = (EnchantmentStorageMeta) item.getItemMeta();
			if(bookEnchantments.hasStoredEnchants()){
				int i = 1;
				Map<Enchantment,Integer> enchs = bookEnchantments.getStoredEnchants();
				if(enchs.size() > 0){
					item_name += " with";
					for (Map.Entry<Enchantment, Integer> ench : enchs.entrySet()){
						item_name += " " + EnchantmentUtils.getClientSideEnchantmentName( ench.getKey(), ench.getValue() );
						item_name += (i < enchs.size() ? ", " : "");
						i++;
					}
				}
			}
		}
		
		// Enchantments
		int i = 1;
		Map<Enchantment,Integer> enchs = item.getEnchantments();
		if(enchs.size() > 0){
			item_name += " with";
			for (Map.Entry<Enchantment, Integer> ench : enchs.entrySet()){
				item_name += " " + EnchantmentUtils.getClientSideEnchantmentName( ench.getKey(), ench.getValue() );
				item_name += (i < enchs.size() ? ", " : "");
				i++;
			}
		}
		
		// Custom item names
		ItemMeta im = item.getItemMeta();
		if(im != null){
			String displayName = im.getDisplayName();
			if(displayName != null){
				item_name += ", named \"" + displayName + "\"";
			}
		}
		
		return item_name;
		
	}
}