package biotech.container;

import universalelectricity.core.item.IItemElectric;
import universalelectricity.prefab.SlotSpecific;
import biotech.slots.rangeUpgradeSlot;
import biotech.tileentity.CuttingMachineTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class CuttingMachineContainer extends Container
{
	private CuttingMachineTileEntity	tileEntity;
	
	public CuttingMachineContainer(InventoryPlayer par1InventoryPlayer, CuttingMachineTileEntity te)
	{
		this.tileEntity = te;
		
		// Electric Input Slot
		this.addSlotToContainer(new SlotSpecific(tileEntity, 0, 5, 50, IItemElectric.class));
		
		// Slot for Range Upgrade
		// this.addSlotToContainer(new rangeUpgradeSlot(tileEntity, 1, 7, 16));
				
		int InvOutID = 1;
		
		// Output Inventory
		for (int var1 = 0; var1 < 3; ++var1)
		{
			for (int var2 = 0; var2 < 2; ++var2)
			{
				this.addSlotToContainer(new Slot(tileEntity, InvOutID, 116 + var2 * 18, 20 + var1 * 18));
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
			
			if (par1 == 2)
			{
				if (!this.mergeItemStack(var4, 3, 39, true))
				{
					return null;
				}
				
				var3.onSlotChange(var4, var2);
			}
			else if (par1 != 1 && par1 != 0)
			{
				if (var4.getItem() instanceof IItemElectric)
				{
					if (!this.mergeItemStack(var4, 0, 1, false))
					{
						return null;
					}
				}
				else if (par1 >= 3 && par1 < 30)
				{
					if (!this.mergeItemStack(var4, 30, 39, false))
					{
						return null;
					}
				}
				else if (par1 >= 30 && par1 < 39 && !this.mergeItemStack(var4, 3, 30, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(var4, 3, 39, false))
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
}