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
	 * @todo this is buggy, wth?
	 * @return
	 */
	public static String getUsedDurabilityPercentage( ItemStack item ){

		short dura = item.getDurability();
		short max_dura = item.getType().getMaxDurability();
		if(dura > 0 && max_dura > 0 && dura != max_dura){
			double diff = ((dura / max_dura)*100);
			if(diff > 0){
				return Math.floor(diff) + "%";
			}
		}

		return "";
	}
	
	
	/**
	 * Returns the durability remaining
	 * @return
	 */
	public static String getDurabilityPercentage( ItemStack item ){

		short dura = item.getDurability();
		short max_dura = item.getType().getMaxDurability();
		if(dura > 0 && max_dura > 0 && dura != max_dura){
			double diff = max_dura - dura;
			diff = ((diff / max_dura)*100);
			if(diff > 0){
				return Math.floor(diff) + "%";
			}
			return "0%";
		}
		
		return "";
	}
	
	
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
		if(dataValueUsedForSubitems(item.getTypeId())){
			item_name += aliases.getAlias(item.getTypeId(), item.getDurability());
		} else {
			item_name += aliases.getAlias(item.getTypeId(), 0);
		}
		if(item_name.isEmpty()){
			item_name += item.getType().toString().toLowerCase().replace("_", " ");
		}
		
		// Anvils
		if( item.getTypeId() == 145 ){
			// @todo this needs work, because there's a range
			// but I can't find information on what it is
			if( item.getDurability() == 1 ){
				item_name = "slightly damaged anvil";
			}
			else if( item.getDurability() == 2 ){
				item_name = "very damaged anvil";
			}
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
	
	
	/**
     * Returns true if an item uses its damage value for something
     * other than durability.
     *
     * @param id
     * @return
     */
    public static boolean dataValueUsedForSubitems( int id ){
    	return id == 17 		// logs
        		|| id == 18 	// leaves
        		|| id == 24     // sandstone
        		|| id == 31 	// tallgrass
                || id == 35 	// wool
                || id == 43 	// double slab
                || id == 44 	// slab
                || id == 98 	// stonebrick
                || id == 139    // mossycobblewall
                || id == 263 	// charcoal
                || id == 351    // dye
                || id == 6		// saplings
                || id == 373    // potions
        		|| id == 383;   // creature eggs
    }
    
    
    /**
     * Determines if an itemstack can be stacked. Maz stack size, meta data,
     * and more taken into account.
     * @param item
     */
    public static boolean canSafelyStack( ItemStack item ){
    	// Can't stack
    	if( item.getMaxStackSize() == 1 ){
    		return false;
    	}
    	// Has meta
    	ItemMeta im = item.getItemMeta();
    	if(im != null){
    		return false;
    	}
    	return true;
    }
}