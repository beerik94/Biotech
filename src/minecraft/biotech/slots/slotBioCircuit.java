package biotech.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import biotech.Biotech;

public class slotBioCircuit extends Slot
{
	
	public slotBioCircuit(IInventory par1iInventory, int par2, int par3, int par4)
	{
		super(par1iInventory, par2, par3, par4);
		
	}
	
	@Override
	public boolean isItemValid(ItemStack itemstack)
	{
		if(itemstack.itemID == Biotech.bioCircuit.itemID)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}