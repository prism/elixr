package me.botsko.elixr;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
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
	
	
	/**
	 * Is an inventory fully empty
	 * @param in
	 * @return
	 */
	public static boolean isEmpty(Inventory in) {
		boolean ret = false;
		if (in == null) {
			return true;
		}
		for (ItemStack item : in.getContents()) {
			ret |= (item != null);
		}
		return !ret;
	}
	
	
	/**
	 * 
	 * @param player
	 * @param target
	 * @return
	 * @throws Exception 
	 */
	public static void movePlayerInventoryToContainer( PlayerInventory inv, Block target, int filter ) throws Exception{
		
		InventoryHolder container = (InventoryHolder) target.getState();
		if( !InventoryUtils.isEmpty( inv ) ){
			throw new Exception("Target container is not empty");
		}
		
		if( !moveInventoryToInventory( inv, container.getInventory(), false, filter ) ){
			throw new Exception("Target container is full.");
		}
	}
	
	
	/**
	 * 
	 * @param player
	 * @param target
	 * @return
	 * @throws Exception 
	 */
	public static void moveContainerInventoryToPlayer( PlayerInventory inv, Block target, int filter ) throws Exception{
		
		InventoryHolder container = (InventoryHolder) target.getState();
		if( !InventoryUtils.isEmpty( inv ) ){
			throw new Exception("Target container is not empty");
		}
		
		moveInventoryToInventory( container.getInventory(), inv, false, filter );

	}
	
	
	/**
	 * 
	 * @param player
	 * @param chest
	 * @param fullFlag
	 * @return
	 */
	public static boolean moveInventoryToInventory( Inventory from, Inventory to, boolean fullFlag, int filter ) {

		HashMap<Integer, ItemStack> leftovers;

		if (to.firstEmpty() != -1 && !fullFlag){
			for (ItemStack item : from.getContents()) {
				if(to.firstEmpty() == -1){
					return false;
				}
				if (item != null && to.firstEmpty() != -1) {
					
					boolean shouldTransfer = false;
					if(filter > 0){
						if(item.getTypeId() == filter){
							shouldTransfer = true;
						}
					} else {
						shouldTransfer = true;
					}
					
					if(shouldTransfer){
						leftovers = to.addItem(item);
						if (leftovers.size() == 0) {
							from.removeItem(item);
						} else {
							from.removeItem(item);
							from.addItem(leftovers.get(0));
						}
					}
				}
			}
			return true;
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param stack
	 * @param player
	 * @return
	 */
    public ItemStack[] sortItemStack(ItemStack[] stack, Player player) {
        return sortItemStack(stack, 0, stack.length, player);
    }

    
    /**
     * 
     * @param stack
     * @param start
     * @param end
     * @param player
     * @return
     */
    public ItemStack[] sortItemStack(ItemStack[] stack, int start, int end, Player player) {
        stack = stackItems(stack, start, end);
        recQuickSort(stack, start, end - 1);
        return stack;
    }

    
    /**
     * 
     * @param items
     * @param start
     * @param end
     * @return
     */
    private ItemStack[] stackItems(ItemStack[] items, int start, int end) {
        for (int i = start; i < end; i++) {
            ItemStack item = items[i];

            // Avoid infinite stacks and stacks with durability
            if (item == null || item.getAmount() <= 0 || !ItemUtils.canSafelyStack( item )) {
                continue;
            }

            int max_stack = item.getMaxStackSize();
            if (item.getAmount() < max_stack){
                int needed = max_stack - item.getAmount(); // Number of needed items until max_stack

                // Find another stack of the same type
                for (int j = i + 1; j < end; j++) {
                    ItemStack item2 = items[j];

                    // Avoid infinite stacks and stacks with durability
                    if (item2 == null || item2.getAmount() <= 0 || !ItemUtils.canSafelyStack( item )) {
                        continue;
                    }

                    // Same type?
                    // Blocks store their color in the damage value
                    if (item2.getTypeId() == item.getTypeId() && (!ItemUtils.dataValueUsedForSubitems(item.getTypeId()) || item.getDurability() == item2.getDurability())) {
                        // This stack won't fit in the parent stack
                        if (item2.getAmount() > needed) {
                            item.setAmount(max_stack);
                            item2.setAmount(item2.getAmount() - needed);
                            break;
                        } else {
                            item.setAmount(item.getAmount() + item2.getAmount());
                            needed = max_stack - item.getAmount();
                            items[j].setTypeId(0);
                        }
                    }
                }
            }
        }
        return items;
    }

    
    /**
     * 
     * @param list
     * @param first
     * @param second
     */
    private void swap(ItemStack[] list, int first, int second) {
        ItemStack temp;
        temp = list[first];
        list[first] = list[second];
        list[second] = temp;
    }

    
    /**
     * 
     * @author botskonet
     *
     */
    private class ComparableIS {

        private ItemStack item;

        public ComparableIS(ItemStack item) {
            this.item = item;
        }

        public int compareTo(ItemStack check) {
            // Type ID first
            if (item == null && check != null) {
                return -1;
            } else if (item != null && check == null) {
                return 1;
            } else if (item == null && check == null) {
                return 0;
            } else if (item.getTypeId() > check.getTypeId()) {
                return 1;
            } else if (item.getTypeId() < check.getTypeId()) {
                return -1;
            } else if (item.getTypeId() == check.getTypeId()) {
                if (ItemUtils.dataValueUsedForSubitems(item.getTypeId())) {
                    if (item.getDurability() < check.getDurability()) {
                        return 1;
                    } else if (item.getDurability() > check.getDurability()) {
                        return -1;
                    }
                }
                // Stack size
                if (item.getAmount() < check.getAmount()) {
                    return -1;
                } else if (item.getAmount() > check.getAmount()) {
                    return 1;
                }
            }
            return 0;
        }
    }

    
    /**
     * 
     * @param list
     * @param first
     * @param last
     * @return
     */
    private int partition(ItemStack[] list, int first, int last) {
        ItemStack pivot;

        int smallIndex;

        swap(list, first, (first + last) / 2);

        pivot = list[first];
        smallIndex = first;

        for (int index = first + 1; index <= last; index++) {
            ComparableIS compElem = new ComparableIS(list[index]);

            if (compElem.compareTo(pivot) < 0) {
                smallIndex++;
                swap(list, smallIndex, index);
            }

        }

        swap(list, first, smallIndex);
        return smallIndex;

    }

    
    /**
     * 
     * @param list
     * @param first
     * @param last
     */
    private void recQuickSort(ItemStack[] list, int first, int last) {
        if (first < last) {
            int pivotLocation = partition(list, first, last);
            recQuickSort(list, first, pivotLocation - 1);
            recQuickSort(list, pivotLocation + 1, last);
        }
    }
}