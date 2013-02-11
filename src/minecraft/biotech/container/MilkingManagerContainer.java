package biotech.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import universalelectricity.core.implement.IItemElectric;
import universalelectricity.prefab.SlotElectricItem;
import biotech.slots.emptyBucketSlot;
import biotech.slots.milkBucketSlot;
import biotech.tileentity.MilkingManagerTileEntity;

public class MilkingManagerContainer extends Container
{
	private MilkingManagerTileEntity tileEntity;

	public MilkingManagerContainer(InventoryPlayer par1InventoryPlayer, MilkingManagerTileEntity te)
	{
		this.tileEntity = te;

		// Electric Input Slot
		this.addSlotToContainer(new SlotElectricItem(tileEntity, 0, 7, 16));

		// Slot for empty bucket
		this.addSlotToContainer(new emptyBucketSlot(tileEntity, 2, 152, 16));
			
		// Slot for filled bucket
		this.addSlotToContainer(new milkBucketSlot(tileEntity, 3, 152, 51));

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