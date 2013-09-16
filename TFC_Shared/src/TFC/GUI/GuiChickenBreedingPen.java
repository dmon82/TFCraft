/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TFC.GUI;

import TFC.Containers.ContainerChickenBreedingPen;
import TFC.Reference;
import TFC.TileEntities.TileEntityChickenBreedingPen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author jackd`23`
 */
public class GuiChickenBreedingPen extends GuiContainer {
    private TileEntityChickenBreedingPen _TileEntity;
    private EntityPlayer _Player;
    
    public GuiChickenBreedingPen(InventoryPlayer inventory, TileEntityChickenBreedingPen tileEntity, World world, int x, int y, int z) {
        super(new ContainerChickenBreedingPen(inventory, tileEntity, world, x, y, z));
        _TileEntity = tileEntity;
        _Player = inventory.player;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        this.mc.func_110434_K().func_110577_a(new ResourceLocation(Reference.ModID, Reference.AssetPathGui + "gui_chickenbreedingpen.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
    }
}
