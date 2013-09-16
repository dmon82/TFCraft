/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TFC.TileEntities;

import TFC.API.Entities.IAnimal;
import TFC.API.TFCOptions;
import TFC.Core.TFC_Time;
import TFC.Entities.Mobs.EntityChickenTFC;
import TFC.TFCItems;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.entity.item.EntityItem;

/**
 *
 * @author Iymayne
 */
public class TileEntityChickenBreedingPen extends NetworkTileEntity implements IInventory {
    public final int BreedingTime = TFCOptions.enableDebugMode ? 200 : TFC_Time.dayLength * 5;
    
    private EntityChickenTFC _BreedingHen;
    private int _BreedingStart;
    private int _BreedingTime;
    private ItemStack _Inventory;
    
    public TileEntityChickenBreedingPen() {
        
    }

    public EntityChickenTFC getBreedingHen() {
        List<EntityChickenTFC> list = worldObj.getEntitiesWithinAABB(EntityChickenTFC.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 2, this.zCoord + 1));
        _BreedingHen = (list.size() == 0) ? null : list.get(0);
        
        //FMLLog.info("I have looked for a hen and %s found one.", _BreedingHen == null ? "not" : "have");
        return _BreedingHen;
    }
    
    public boolean hasBreedingHen() {
        return getBreedingHen() != null;
    }
    
    public boolean hasFertilizedEgg() {
        if (_Inventory == null || _Inventory.itemID != TFCItems.EggFertilized.itemID)
            return false;
        
        return true;
    }
    
    @Override
    public void handleDataPacket(DataInputStream inStream) throws IOException {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void handleDataPacketServer(DataInputStream inStream) throws IOException {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createInitPacket(DataOutputStream outStream) throws IOException {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void handleInitPacket(DataInputStream inStream) throws IOException {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return _Inventory;
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (j <= 0)
            return null;
        
        ItemStack itemStack = _Inventory;
        _Inventory = null;
        
        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (_Inventory != null && _Inventory.itemID != TFCItems.EggFertilized.itemID) {
            EntityItem item = new EntityItem(worldObj, xCoord, yCoord, zCoord, _Inventory);
            _Inventory = null;
        }
        
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        _Inventory = itemstack;

        if (_Inventory == null)
            return;

        if (_Inventory.stackSize > getInventoryStackLimit())
            _Inventory.stackSize = getInventoryStackLimit();
    }

    @Override
    public String getInvName() {
        return "Chicken Breeding Pen";
    }

    @Override
    public boolean isInvNameLocalized() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true; // ??
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        //return false; ??
        return i == 0 
                && itemstack.itemID == TFCItems.EggFertilized.itemID 
                && itemstack.stackSize <= getInventoryStackLimit();
    }
        
    @Override
    public void updateEntity() {
        // If there's no egg in here, or no fertilized egg, we have nothing to update.
        if (_Inventory == null || _Inventory.itemID != TFCItems.EggFertilized.itemID) {
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 2);
            return;
        }
        
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 2);

        // Process server side only/
        if (this.worldObj.isRemote)
            return;

        // No hen is breeding this egg.
        if (!hasBreedingHen())
            return;
        
        if (++_BreedingTime >= this.BreedingTime) {
            // Spawn small chicken.
            EntityChickenTFC chicken = (EntityChickenTFC)_BreedingHen.createChildTFC(_BreedingHen);
            chicken.setLocationAndAngles(_BreedingHen.posX, _BreedingHen.posY, _BreedingHen.posZ, 0f, 0f);
            chicken.rotationYawHead = chicken.rotationYaw;
            chicken.renderYawOffset = chicken.rotationYaw;
            worldObj.spawnEntityInWorld(chicken);
            chicken.setAge((int)TFC_Time.getTotalDays());
            
            // Clear egg slot.
            _Inventory = null;
            _BreedingStart = 0;
            _BreedingTime = 0;
        }
        // Just an idea. Delete the egg, or make it an unfertilised one?
        /*else if (TFC_Time.getTotalTicks() - _BreedingStart >= FertilizedEggDeathTime) {
            KillFertilizedEgg();
        }*/
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("BreedingTime", _BreedingTime);
        nbt.setInteger("BreedingStart", _BreedingStart);
        
        nbt.setBoolean("HasItem", _Inventory != null);
        if (_Inventory != null)
            _Inventory.writeToNBT(nbt);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        _BreedingTime = nbt.getInteger("BreedingTime");
        _BreedingStart = nbt.getInteger("BreedingStart");
        
        if (nbt.getBoolean("HasItem"))
            _Inventory = ItemStack.loadItemStackFromNBT(nbt);
    }

    @Override
    public void openChest() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void closeChest() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
