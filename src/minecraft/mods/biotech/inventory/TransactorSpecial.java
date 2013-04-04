package mods.biotech.inventory;

import mods.biotech.helpers.ISpecialInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;

public class TransactorSpecial extends Transactor {

	protected ISpecialInventory inventory;

	public TransactorSpecial(ISpecialInventory inventory) {
		this.inventory = inventory;
	}

	@Override
	public int inject(ItemStack stack, ForgeDirection orientation, boolean doAdd) {
		return inventory.addItem(stack, doAdd, orientation);
	}

}