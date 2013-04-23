package biotech.slots;

import universalelectricity.core.item.IItemElectric;
import mekanism.api.IEnergizedItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import biotech.Biotech;

public class slotElectricItem extends Slot
{
	
	public slotElectricItem(IInventory par1iInventory, int par2, int par3, int par4)
	{
		super(par1iInventory, par2, par3, par4);
		
	}
	
	@Override
	public boolean isItemValid(ItemStack itemstack)
	{
		if(itemstack.getItem() instanceof IItemElectric)
		{
			return true;
		}
		else if(itemstack.getItem() instanceof IEnergizedItem)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}