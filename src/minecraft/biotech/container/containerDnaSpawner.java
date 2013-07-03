package biotech.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import biotech.slots.slotDNA;
import biotech.slots.slotElectricItem;
import biotech.tileentity.tileEntityDnaSpawner;

public class containerDnaSpawner extends containerBasic
{
	private tileEntityDnaSpawner	tileEntity;
	
	public containerDnaSpawner(InventoryPlayer par1InventoryPlayer, tileEntityDnaSpawner te)
	{
		this.tileEntity = te;
		
		// Slot for Electricity
		this.addSlotToContainer(new slotElectricItem(tileEntity, 0, 5, 50));
		
		// Slot for DNA
		this.addSlotToContainer(new slotDNA(tileEntity, 1, 5, 20));
		
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
