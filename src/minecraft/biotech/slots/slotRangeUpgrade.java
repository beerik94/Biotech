package biotech.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import biotech.Biotech;

public class slotRangeUpgrade extends Slot
{
	
	public slotRangeUpgrade(IInventory par1iInventory, int par2, int par3, int par4)
	{
		super(par1iInventory, par2, par3, par4);
	}
	
	@Override
	public boolean isItemValid(ItemStack itemstack)
	{
		if(itemstack.getItem() == Biotech.bioCircuit && itemstack.getItemDamage() == 1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
