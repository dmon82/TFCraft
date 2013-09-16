/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TFC.Containers;

import TFC.TFCItems;
import TFC.TileEntities.TileEntityChickenBreedingPen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 *
 * @author jackd`23`
 */
public class ContainerChickenBreedingPen extends ContainerTFC {
    private TileEntityChickenBreedingPen _TileEntity;
    private boolean _HasEgg;
    
    public ContainerChickenBreedingPen(InventoryPlayer inventory, TileEntityChickenBreedingPen tileEntity, World world, int x, int y, int z) {
        _TileEntity = tileEntity;
        addSlotToContainer(new Slot(_TileEntity, 0, 80, 29));
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventory, i * 9 + j + 9, j * 18 + 8, i * 18 + 84));
            }
        }
        
        for (int i = 0; i < 9; i++)
            addSlotToContainer(new Slot(inventory, i, i * 18 + i, 142));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        Slot clickedSlot = (Slot)this.inventorySlots.get(index);
        
        // Clicked slot is empty, doing nothing.
        if (clickedSlot == null || !clickedSlot.getHasStack())
            return null;
        
        ItemStack clickedStack = clickedSlot.getStack();
        
        // If clicked slot is the one slot in the chicken breeding pen, try
        // transfering it into any slot with index greater than 1, which is
        // the player's inventory.
        if (index == 0) {
            if (!this.mergeItemStack(clickedStack, 1, inventorySlots.size(), true))
                return null;

            if (clickedSlot.getStack().stackSize <= 0)
                clickedSlot.putStack(null);
            else
                clickedSlot.onSlotChanged();
        }
        // If clicked slot is one in the player's inventory, which is any with
        // an index greater than 1, since this container only has 1 slot.
        else {
            Slot penSlot = (Slot)this.inventorySlots.get(0);

            // Items in the pen are not stackable, if there is already an
            // item in it, we do nothing.
            if (penSlot.getHasStack() || clickedStack.itemID != TFCItems.EggFertilized.itemID)
                return null;
            
            // We put 1 item into the pen's slot, and decrease the clicked
            // slot's stack size. If that is 0 or less, we empty the clicked
            // slot. We send an onChanged() notice otherwise.
            ItemStack penStack = clickedStack.copy();
            penStack.stackSize = 1;
            penSlot.putStack(penStack);
            clickedStack.stackSize--;
            
            if (clickedStack.stackSize <= 0)
                clickedSlot.putStack(null);
            else
                clickedSlot.onSlotChanged();
        }
      
        return null;
    }
}
