package biotech.container;

import ic2.api.IElectricItem;
import universalelectricity.core.item.IItemElectric;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import biotech.slots.slotElectricItem;
import biotech.slots.slotRangeUpgrade;
import biotech.tileentity.tileEntityFertilizer;

public class containerFertilizer extends Container
{
	private tileEntityFertilizer	tileEntity;
	
	public containerFertilizer(InventoryPlayer par1InventoryPlayer, tileEntityFertilizer te)
	{
		this.tileEntity = te;
		
		// Slot for Electricity
		this.addSlotToContainer(new slotElectricItem(tileEntity, 0, 5, 50));
		
		// Slot for Range Upgrade
		this.addSlotToContainer(new slotRangeUpgrade(tileEntity, 1, 5, 20));
		
		int var3;
		
		for (var3 = 0; var3 < 3; ++var3)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 85 + var3 * 18));
			}
		}
		
		for (var3 = 0; var3 < 9; ++var3)
		{
			this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 142));
		}
		
		tileEntity.openChest();
	}
	
	@Override
	public void onCraftGuiClosed(EntityPlayer entityplayer)
	{
		super.onCraftGuiClosed(entityplayer);
		tileEntity.closeChest();
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return this.tileEntity.isUseableByPlayer(par1EntityPlayer);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotID)
	{
		ItemStack stack = null;
		Slot currentSlot = (Slot) this.inventorySlots.get(slotID);
		
		if (currentSlot != null && currentSlot.getHasStack())
		{
			ItemStack slotStack = currentSlot.getStack();
			stack = slotStack.copy();
			
			if (slotID > 4)
			{
				if (slotStack.getItem() instanceof IElectricItem || slotStack.getItem() instanceof IItemElectric)
				{
					if (((IElectricItem)slotStack.getItem()).canProvideEnergy(slotStack) || ((IItemElectric) slotStack.getItem()).getProvideRequest(stack).getWatts() > 0)
					{
						if (!this.mergeItemStack(slotStack, this.inventorySlots.size(), 0, false))
						{
							return null;
						}
					}
					else
					{
						if (!this.mergeItemStack(slotStack, 0, this.inventorySlots.size(), false))
						{
							return null;
						}
					}
				}

				else if (!this.mergeItemStack(slotStack, 2, 4, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(slotStack, 5, 38, false))
			{
				return null;
			}

			if (slotStack.stackSize == 0)
			{
				currentSlot.putStack((ItemStack) null);
			}
			else
			{
				currentSlot.onSlotChanged();
			}

			if (slotStack.stackSize == stack.stackSize)
			{
				return null;
			}

			currentSlot.onPickupFromSlot(par1EntityPlayer, slotStack);
		}

		return stack;
	}
}