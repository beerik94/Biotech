package mods.biotech.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;

public interface ITransactor {

	ItemStack add(ItemStack stack, ForgeDirection orientation, boolean doAdd);

}