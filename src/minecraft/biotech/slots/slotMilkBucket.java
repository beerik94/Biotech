package biotech.slots;

import universalelectricity.core.item.IItemElectric;
import mekanism.api.IEnergizedItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class slotMilkBucket extends Slot
{
	
	public slotMilkBucket(IInventory par1iInventory, int par2, int par3, int par4)
	{
		super(par1iInventory, par2, par3, par4);
		
	}
	
	@Override
	public boolean isItemValid(ItemStack itemstack)
	{
		if(itemstack.itemID == Item.bucketMilk.itemID)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}