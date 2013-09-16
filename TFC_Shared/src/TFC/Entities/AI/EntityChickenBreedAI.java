/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TFC.Entities.AI;

import TFC.Entities.Mobs.EntityChickenTFC;
import TFC.TFCBlocks;
import TFC.TFCItems;
import TFC.TileEntities.TileEntityChickenBreedingPen;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.entity.ai.EntityAIBase;

/**
 *
 * @author Iymayne
 */
public class EntityChickenBreedAI extends EntityAIBase {
    private TileEntityChickenBreedingPen _Pen;
    
    private int _PauseForTicks = 100; // ~5 seconds
    private int _PauseTicks = _PauseForTicks;

    // Remaining ticks until next egg to seek out a pen.
    protected int _SeekPenForEggTicks = 2000;
    protected EntityChickenTFC _Chicken;
    
    public EntityChickenBreedAI(EntityChickenTFC chicken) {
        this.setMutexBits(0x01 | 0x02 | 0x010000);
        _Chicken = chicken;
    }
    
    @Override
    public boolean shouldExecute() {
        if (_PauseTicks-- > 0)
            return false;

        _PauseTicks = _PauseForTicks;
        
        if (_Chicken == null || _Chicken.isDead)
            return false;
        
        if (wantsToLayEgg() && (_Pen = findEmptyBreedingPen()) != null) {
           FMLLog.info("I will execute the chicken pen AI.", (Object) null);
           return true;
        }
        
        if ((_Pen = findBreedingPenWithEgg()) != null) {
            FMLLog.info("I will execute the chicken pen AI.", (Object) null);
            return true;
        }

        return false;
    }
    
    @Override
    public boolean continueExecuting() {
        if (wantsToLayEgg() && !_Pen.hasBreedingHen())
            return true;
        
        if (!_Pen.hasBreedingHen() && _Pen.hasFertilizedEgg())
            return true;
        
        return false;
    }
    
    @Override
    public void startExecuting() {
        
    }
    
    @Override
    public void resetTask() {
        FMLLog.fine("I will reset the chicken pen AI", (Object)null);
        _PauseTicks = _PauseForTicks;
        _Pen = null;
    }
    
    @Override
    public void updateTask() {
        _Chicken.getLookHelper().setLookPosition(_Pen.xCoord, _Pen.yCoord, _Pen.zCoord, 30f, _Chicken.getVerticalFaceSpeed());
        
        double distanceSq = _Chicken.getDistanceSq(_Pen.xCoord, _Pen.yCoord, _Pen.zCoord);

        if (distanceSq >= 6.25d)
            _Chicken.getNavigator().clearPathEntity();
        else
            _Chicken.getNavigator().tryMoveToXYZ(_Pen.xCoord, _Pen.yCoord + 1, _Pen.zCoord, 1d);
    }
    
    public boolean wantsToLayEgg() {
        FMLLog.info("I do %s want to lay an egg soon", _Chicken.timeUntilNextEgg > _SeekPenForEggTicks ? "NOT" : "really");
        
        return _Chicken.timeUntilNextEgg <= _SeekPenForEggTicks;
    }
    
    public TileEntityChickenBreedingPen findEmptyBreedingPen() {
        int posX = (int)_Chicken.posX,
                posY = (int)_Chicken.posY,
                posZ = (int)_Chicken.posZ;
        
        for (int x = posX - 5; x <= posX + 5; x++) {
            for (int z = posZ - 5; z <= posZ + 5; z++) {
                for (int y = posY - 5; y <= posY + 5; y++) {
                    if (_Chicken.worldObj.getBlockId(x, y, z) != TFCBlocks.ChickenBreedingPen.blockID)
                        continue;
                    
                    TileEntityChickenBreedingPen pen = 
                            (TileEntityChickenBreedingPen)_Chicken.worldObj.getBlockTileEntity(x, y, z);

                    FMLLog.info("I can see %s breeding pen.", pen == null ? "NO" : "a");
                    
                    if (/*pen != null &&*/ !pen.hasBreedingHen())
                        return pen;
                }
            }
        }
        
        return null;
    }
    
    public TileEntityChickenBreedingPen findBreedingPenWithEgg() {
        int posX = (int)_Chicken.posX,
                posY = (int)_Chicken.posY,
                posZ = (int)_Chicken.posZ;
        
        for (int x = posX - 5; x <= posX + 5; x++) {
            for (int z = posZ - 5; z <= posZ + 5; z++) {
                for (int y = posY - 5; y <= posY + 5; y++) {
                    if (_Chicken.worldObj.getBlockId(x, y, z) != TFCBlocks.ChickenBreedingPen.blockID)
                        continue;
                    
                    TileEntityChickenBreedingPen pen = 
                            (TileEntityChickenBreedingPen)_Chicken.worldObj.getBlockTileEntity(x, y, z);

                    FMLLog.info("I can see %s breeding pen.", pen == null ? "NO" : "a");
                    
                    if (/*pen != null
                            &&*/ !pen.hasBreedingHen() 
                            && pen.getStackInSlot(0) != null
                            && pen.getStackInSlot(0).itemID == TFCItems.EggFertilized.itemID)
                        return pen;
                }
            }
        }
        
        return null;
    }
}
