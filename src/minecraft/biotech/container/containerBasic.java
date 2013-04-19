package biotech.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import biotech.slots.slotEmptyBucket;
import biotech.slots.slotMilkBucket;
import biotech.slots.slotSeeds;
import biotech.tileentity.tileEntityBioRefinery;

public class containerBasic extends Container
{

	public containerBasic(InventoryPlayer par1InventoryPlayer, tileEntityBioRefinery tileEntity)
	{
		
	}

	public void onCraftGuiClosed(EntityPlayer entityplayer)
	{
		super.onCraftGuiClosed(entityplayer);
	}
	
	/**
	 * Called to transfer a stack from one inventory to the other eg. when shift
	 * clicking.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
	{
		ItemStack var2 = null;
		Slot var3 = (Slot) this.inventorySlots.get(par1);
		
		if (var3 != null && var3.getHasStack())
		{
			ItemStack var4 = var3.getStack();
			var2 = var4.copy();
			
			if (par1 > 4)
			{
				if (!this.mergeItemStack(var4, 2, 4, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(var4, 5, 38, false))
			{
				return null;
			}
			
			if (var4.stackSize == 0)
			{
				var3.putStack((ItemStack) null);
			}
			else
			{
				var3.onSlotChanged();
			}
			
			if (var4.stackSize == var2.stackSize)
			{
				return null;
			}
			
			var3.onPickupFromSlot(par1EntityPlayer, var4);
		}
		
		return var2;
	}


	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return false;
	}
}