package biotech.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import biotech.slots.slotElectricItem;
import biotech.slots.slotRangeUpgrade;
import biotech.slots.slotSeeds;
import biotech.tileentity.tileEntityFarmingMachine;

public class containerFarmingMachine extends containerBasic
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
	public void onContainerClosed(EntityPlayer entityplayer)
	{
		super.onContainerClosed(entityplayer);
		tileEntity.closeChest();
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return this.tileEntity.isUseableByPlayer(par1EntityPlayer);
	}
}