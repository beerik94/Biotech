package biotech.container;

import ic2.api.IElectricItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.IItemElectric;
import biotech.Biotech;
import biotech.slots.slotElectricItem;
import biotech.slots.slotRangeUpgrade;
import biotech.slots.slotSeeds;
import biotech.tileentity.tileEntityFarmingMachine;

public class containerFarmingMachine extends Container
{
	private tileEntityFarmingMachine	tileEntity;
	
	public containerFarmingMachine(InventoryPlayer par1InventoryPlayer, tileEntityFarmingMachine te)
	{
		this.tileEntity = te;

		// Slot for Electricity
		this.addSlotToContainer(new slotElectricItem(tileEntity, 0, 5, 50));
		
		// Slot for seeds
		this.addSlotToContainer(new slotSeeds(tileEntity, 1, 153, 20));
		
		// Slot RangeUpgrade
		this.addSlotToContainer(new slotRangeUpgrade(tileEntity, 2, 5, 20));
		
		int InvOutID = 3;
		
		//Output Slots
		for (int var1 = 0; var1 < 3; ++var1)
		{
			for (int var2 = 0; var2 < 2; ++var2)
			{
				this.addSlotToContainer(new Slot(tileEntity, InvOutID, 110 + var2 * 18, 20 + var1 * 18));
				InvOutID++;
			}
		}
		
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
			
			if(isInputSlot(slotID))
			{
				if (slotStack.getItem() instanceof IElectricItem || slotStack.getItem() instanceof IItemElectric)
				{
					if (!this.mergeItemStack(slotStack, this.inventorySlots.size(), 0, false))
					{
						return null;
					}
				}
				else if(slotStack.getItem() == Item.seeds)
				{
					if (!this.mergeItemStack(slotStack, this.inventorySlots.size(), 1, false))
					{
						return null;
					}
				}
				else if(slotStack == new ItemStack(Biotech.bioCircuit, 1, 1))
				{
					if (!this.mergeItemStack(slotStack, this.inventorySlots.size(), 2, false))
					{
						return null;
					}
				}
			}
			else if(isOutputSlot(slotID))
			{
				if (!this.mergeItemStack(slotStack, this.inventorySlots.size(), 3, false))
				{
					return null;
				}
				else if (!this.mergeItemStack(slotStack, this.inventorySlots.size(), 4, false))
				{
					return null;
				}
				else if (!this.mergeItemStack(slotStack, this.inventorySlots.size(), 5, false))
				{
					return null;
				}
				else if (!this.mergeItemStack(slotStack, this.inventorySlots.size(), 6, false))
				{
					return null;
				}
				else if (!this.mergeItemStack(slotStack, this.inventorySlots.size(), 7, false))
				{
					return null;
				}
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
	
	public boolean isInputSlot(int slot)
	{
		if (slot >= 0 && slot <= 2)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean isOutputSlot(int slot)
	{
		if (slot >= 3 && slot <= 8)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}