package biotech.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.prefab.SlotSpecific;
import biotech.slots.rangeUpgradeSlot;
import biotech.tileentity.FarmMachineTileEntity;

public class FarmMachineContainer extends Container
{
	private FarmMachineTileEntity	tileEntity;
	
	public FarmMachineContainer(InventoryPlayer par1InventoryPlayer, FarmMachineTileEntity te)
	{
		this.tileEntity = te;
		
		// Electric Input Slot
		this.addSlotToContainer(new SlotSpecific(tileEntity, 0, 5, 49, IItemElectric.class));
		
		// Slot for seeds
		this.addSlotToContainer(new Slot(tileEntity, 1, 153, 20));
		
		// Slot BioCircuit
		this.addSlotToContainer(new rangeUpgradeSlot(tileEntity, 2, 5, 20));
		
		//Output Slots
		for (int var1 = 0; var1 < 3; ++var1)
		{
			for (int var2 = 0; var2 < 2; ++var2)
			{
				this.addSlotToContainer(new Slot(tileEntity, var2 + var1 * 3 + 3, 110 + var2 * 18, 20 + var1 * 18));
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
			
			if (par1 > 4)
			{
				if (var4.getItem() instanceof IItemElectric)
				{
					if (((IItemElectric) var4.getItem()).getProvideRequest(var2).getWatts() > 0)
					{
						if (!this.mergeItemStack(var4, 1, 2, false))
						{
							return null;
						}
					}
					else
					{
						if (!this.mergeItemStack(var4, 0, 1, false))
						{
							return null;
						}
					}
				}
				
				else if (!this.mergeItemStack(var4, 2, 4, false))
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
}