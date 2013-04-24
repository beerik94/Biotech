package biotech.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class containerBasic extends Container
{
	protected int	slotCount	= this.inventorySlots.size();
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotID)
	{
		ItemStack var2 = null;
		Slot var3 = (Slot) this.inventorySlots.get(slotID);
		
		if (var3 != null && var3.getHasStack())
		{
			ItemStack itemStack = var3.getStack();
			var2 = itemStack.copy();
			
			// A slot ID greater than the slot count means it is inside the
			// TileEntity GUI.
			if (slotID >= this.slotCount)
			{
				// Player Inventory, Try to place into slot.
				boolean didTry = false;
				
				for (int i = 0; i < this.slotCount; i++)
				{
					if (this.getSlot(i).isItemValid(itemStack))
					{
						didTry = true;
						
						if (this.mergeItemStack(itemStack, i, i + 1, false))
						{
							break;
						}
					}
				}
				
				if (!didTry)
				{
					if (slotID < 27 + this.slotCount)
					{
						if (!this.mergeItemStack(itemStack, 27 + this.slotCount, 36 + this.slotCount, false))
						{
							return null;
						}
					}
					else if (slotID >= 27 + this.slotCount && slotID < 36 + this.slotCount && !this.mergeItemStack(itemStack, slotCount, 27 + slotCount, false))
					{
						return null;
					}
				}
			}
			else if (!this.mergeItemStack(itemStack, this.slotCount, 36 + this.slotCount, false))
			{
				return null;
			}
			
			if (itemStack.stackSize == 0)
			{
				var3.putStack((ItemStack) null);
			}
			else
			{
				var3.onSlotChanged();
			}
			if (itemStack.stackSize == var2.stackSize)
			{
				return null;
			}
			var3.onPickupFromSlot(par1EntityPlayer, itemStack);
		}
		return var2;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return false;
	}
}
