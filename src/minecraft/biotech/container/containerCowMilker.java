package biotech.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import biotech.slots.slotElectricItem;
import biotech.slots.slotEmptyBucket;
import biotech.slots.slotMilkBucket;
import biotech.slots.slotRangeUpgrade;
import biotech.tileentity.tileEntityCowMilker;

public class containerCowMilker extends containerBasic
{
	private tileEntityCowMilker	tileEntity;
	
	public containerCowMilker(InventoryPlayer par1InventoryPlayer, tileEntityCowMilker te)
	{
		this.tileEntity = te;
		
		// Slot for Electricity
		this.addSlotToContainer(new slotElectricItem(tileEntity, 0, 5, 50));
		
		// Slot for Range Upgrade
		this.addSlotToContainer(new slotRangeUpgrade(tileEntity, 1, 5, 20));
		
		// Slot for empty bucket
		this.addSlotToContainer(new slotEmptyBucket(tileEntity, 2, 120, 20));
		
		// Slot for filled bucket
		this.addSlotToContainer(new slotMilkBucket(tileEntity, 3, 120, 55));
		
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
}