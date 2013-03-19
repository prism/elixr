package me.botsko.elixr;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * 
 * @author botskonet
 *
 */
public class InventoryUtils {
	
	
	/**
	 * Returns the slot id of a specific item type, or -1 if none
	 * @param inv
	 * @param item_id
	 * @param sub_id
	 * @return
	 */
	public static int inventoryHasItem( Inventory inv, int item_id, byte sub_id ){
		int currentSlot = 0;
		for(ItemStack item : inv.getContents()){
			if( item != null && item.getTypeId() == item_id && item.getDurability() == sub_id ){
				return currentSlot;
			}
			currentSlot++;
		}
		return -1;
	}
	
	
	/**
	 * Moves a specific item to the player's hand, returns false if the item doesn't exist in the inventory
	 * @param inv
	 * @param item_id
	 * @param sub_id
	 * @return
	 */
	public static boolean moveItemToHand( PlayerInventory inv, int item_id, byte sub_id ){
		int slot = inventoryHasItem( inv, item_id, sub_id );
		if( slot > -1 ){
			ItemStack item = inv.getItem(slot);
			inv.clear(slot);
			// If the player has an item in-hand, switch to a vacant spot
			if( !playerHasEmptyHand(inv) ){
				inv.setItem(slot, inv.getItemInHand());
			}
			inv.setItemInHand(item);
			return true;
		}
		return false;
	}
	
	
	/**
	 * Whether or not the player has an empty hand
	 * @param inv
	 * @return
	 */
	public static boolean playerHasEmptyHand( PlayerInventory inv ){
		return (inv.getItemInHand().getTypeId() == 0);
	}
	
	
	/**
	 * Adds an item to the inventory, returns a hashmap of leftovers
	 * @param player
	 */
	public static HashMap<Integer,ItemStack> addItemToInventory( Inventory inv, ItemStack item ){
		return inv.addItem(item);
	}
	
	
	/**
	 * Hands the item to a player. If they have an item already, we move it to their inv. Returns
	 * false if no room in inv.
	 * @param player
	 */
	public static boolean handItemToPlayer( PlayerInventory inv, ItemStack item ){
		// Ensure there's at least one empty inv spot
		if( inv.firstEmpty() != -1 ){
			// If the player has an item in-hand, switch to a vacant spot
			if( !playerHasEmptyHand( inv ) ){
				addItemToInventory( inv, inv.getItemInHand() );
			}
			inv.setItemInHand(item);
			return true;
		}
		return false;
	}
	
	
	/**
	 * Subtract a specific quantity from an inventory slots item stack.
	 * @param inv
	 * @param slot
	 * @param quant
	 */
	public static void subtractAmountFromPlayerInvSlot( PlayerInventory inv, int slot, int quant ){
		ItemStack itemAtSlot = inv.getItem(slot);
		if( itemAtSlot != null && quant <= 64 ){
			itemAtSlot.setAmount( itemAtSlot.getAmount() - quant );
			if( itemAtSlot.getAmount() == 0 ){
				inv.clear(slot);
			}
		}
	}
	
	
	/**
	 * Drop items at player's location.
	 * @param leftovers
	 * @param player
	 */
	public static void dropItemsByPlayer( HashMap<Integer,ItemStack> leftovers, Player player ){
		if(!leftovers.isEmpty()){
			for (Entry<Integer, ItemStack> entry : leftovers.entrySet()){
			    player.getWorld().dropItemNaturally(player.getLocation(), entry.getValue());
			}
		}
	}
}