/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TFC.Blocks.Devices;

import TFC.Blocks.BlockTerraContainer;
import TFC.Entities.Mobs.EntityChickenTFC;
import TFC.Reference;
import TFC.TerraFirmaCraft;
import TFC.TileEntities.NetworkTileEntity;
import TFC.TileEntities.TileEntityChickenBreedingPen;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

/**
 *
 * @author jackd`23`
 */
public class BlockChickenBreedingPen extends BlockTerraContainer {
    @SideOnly(Side.CLIENT)
    private Icon _IconTopEmpty;
    @SideOnly(Side.CLIENT)
    private Icon _IconTopEgg;
    @SideOnly(Side.CLIENT)
    private Icon _IconBottom;
    @SideOnly(Side.CLIENT)
    private Icon _IconSidesEmpty;
    @SideOnly(Side.CLIENT)
    private Icon _IconSidesEgg;
    
    public BlockChickenBreedingPen(int itemID) {
        super(itemID, Material.wood);
        setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityChickenBreedingPen();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        if (side == 0)
            return _IconBottom;
        else if (side == 1)
            return meta == 0 ? _IconTopEmpty : _IconTopEgg;
        
        return meta == 0 ? _IconSidesEmpty : _IconSidesEgg;
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            ((NetworkTileEntity)world.getBlockTileEntity(x, y, z)).validate();
            return true;
        }
        
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity == null)
            return false;
        
        // Cannot open the breeding pen while a hen is breeding out an egg.
        //if (((TileEntityChickenBreedingPen)tileEntity).hasBreedingHen())
            //return false;
        
        player.openGui(TerraFirmaCraft.instance, 24, world, x, y, z);
        return true;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        _IconTopEmpty = register.registerIcon(Reference.ModID + ":blocks/plants/Straw");
        _IconTopEgg = register.registerIcon(Reference.ModID + ":devices/ChickenBreedingPenTopFertilized");
        _IconBottom = register.registerIcon(Reference.ModID + ":blocks/wood/Oak Plank");
        _IconSidesEmpty = register.registerIcon(Reference.ModID + ":devices/ChickenBreedingPenSideEmpty");
        _IconSidesEgg = register.registerIcon(Reference.ModID + ":devices/ChickenBreedingPenSideFertilized");
    }
}
