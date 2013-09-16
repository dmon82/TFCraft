/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TFC.Items;

import TFC.Reference;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemEgg;

/**
 *
 * @author Iymayne
 */
public class ItemFertilizedEgg extends ItemTerra {
    public ItemFertilizedEgg(int itemID) {
        super(itemID);
        this.setUnlocalizedName("Fertilized Egg");
    }
    
    @Override
    public void registerIcons(IconRegister register) {
        this.itemIcon = register.registerIcon(Reference.ModID + ":items/food/" + this.getUnlocalizedName());
    }
    
}
