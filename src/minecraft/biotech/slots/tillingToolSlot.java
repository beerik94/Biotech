package biotech.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import biotech.tileentity.TillingMachineTileEntity;

public class tillingToolSlot extends Slot {

	public tillingToolSlot(IInventory par1iInventory, int par2, int par3,
			int par4) {
		super(par1iInventory, par2, par3, par4);

	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		for (int i = 0; i < TillingMachineTileEntity.hoeToolStacks.length; i++) {
			if (itemstack.itemID == TillingMachineTileEntity.hoeToolStacks[i].itemID) {
				return true;
			}
		}
		return false;
	}
}