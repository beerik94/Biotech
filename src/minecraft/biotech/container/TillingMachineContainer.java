package biotech.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import universalelectricity.core.implement.IItemElectric;
import universalelectricity.prefab.SlotElectricItem;
import biotech.slots.tillingToolSlot;
import biotech.tileentity.TillingMachineTileEntity;

public class TillingMachineContainer extends Container
{
	private TillingMachineTileEntity tileEntity;

	public TillingMachineContainer(InventoryPlayer par1InventoryPlayer, TillingMachineTileEntity te)
	{
		this.tileEntity = te;

		// Electric Input Slot
		this.addSlotToContainer(new SlotElectricItem(tileEntity, 0, 7, 27));

		// Slot for tools
		this.addSlotToContainer(new tillingToolSlot(tileEntity, 1, 144, 7));
		
		// Slot BioCircuit
		this.addSlotToContainer(new Slot(tileEntity, 2, 7, 7));
		
		// Slot for water bucket
		this.addSlotToContainer(new Slot(tileEntity, 3, 144, 27));
		
		int var3;

		for (var3 = 0; var3 < 3; ++var3)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
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
	 * Called to transfer a stack from one inventory to the other eg. when shift clicking.
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
				if (!this.mergeItemStack(var4, 3, 39, true)) { return null; }

				var3.onSlotChange(var4, var2);
			}
			else if (par1 != 1 && par1 != 0)
			{
				if (var4.getItem() instanceof IItemElectric)
				{
					if (!this.mergeItemStack(var4, 0, 1, false)) { return null; }
				}
				else if (var4.itemID == net.minecraft.item.Item.hoeWood.itemID) //not working
				{ 
					if (!this.mergeItemStack(var4, 1, 1, false)) { return null; }
				}
				else if (par1 >= 3 && par1 < 30)
				{
					if (!this.mergeItemStack(var4, 30, 39, false)) { return null; }
				}
				else if (par1 >= 30 && par1 < 39 && !this.mergeItemStack(var4, 3, 30, false)) { return null; }
			}
			else if (!this.mergeItemStack(var4, 3, 39, false)) { return null; }

			if (var4.stackSize == 0)
			{
				var3.putStack((ItemStack) null);
			}
			else
			{
				var3.onSlotChanged();
			}

			if (var4.stackSize == var2.stackSize) { return null; }

			var3.onPickupFromSlot(par1EntityPlayer, var4);
		}

		return var2;
	}
}