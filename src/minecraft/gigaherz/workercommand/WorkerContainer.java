package gigaherz.workercommand;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class WorkerContainer extends Container
{
    protected WorkerTile worker;
    protected int lastPowerAcc=0;
    protected int lastX=0;
    protected int lastY=0;

    public WorkerContainer(WorkerTile tileEntity, InventoryPlayer playerInventory)
    {
        this.worker = tileEntity;
        
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                addSlotToContainer(new Slot(tileEntity, 
                		j + i * 3,
                		8 + j * 18, 
                		17 + i * 18));
            }
        }

        for (int i = 0; i < 3; i++)
        {
            addSlotToContainer(new Slot(tileEntity, 
                		i + 18,
                		68, 
                		17 + i * 18));
        }

        for (int i = 0; i < 3; i++)
        {
            addSlotToContainer(new Slot(tileEntity, 
                		i + 21,
                		92, 
                		17 + i * 18));
        }

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                addSlotToContainer(new SlotFurnace(playerInventory.player, tileEntity, 
                		j + i * 3 + 9,
                		116 + j * 18, 
                		17 + i * 18));
            }
        }

        bindPlayerInventory(playerInventory);
    }

    protected void bindPlayerInventory(InventoryPlayer playerInventory)
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(playerInventory, 
                		j + i * 9 + 9,
                		8 + j * 18, 
                		84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return worker.isUseableByPlayer(player);
    }

    public void addCraftingToCrafters(ICrafting crafter)
    {
        super.addCraftingToCrafters(crafter);
        crafter.sendProgressBarUpdate(this, 0, this.worker.powerAccum);
        crafter.sendProgressBarUpdate(this, 1, this.worker.currentX);
        crafter.sendProgressBarUpdate(this, 2, this.worker.currentZ);
    }
    
    public void updateCraftingResults()
    {
        super.updateCraftingResults();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting crafter = (ICrafting)this.crafters.get(i);
	
	        if (this.lastPowerAcc != this.worker.powerAccum)
	        {
	        	crafter.sendProgressBarUpdate(this, 0, this.worker.powerAccum);
	        }
	
	        if (this.lastX != this.worker.currentX)
	        {
	        	crafter.sendProgressBarUpdate(this, 1, this.worker.currentX);
	        }
	        
	        if (this.lastY != this.worker.currentZ)
	        {
	            crafter.sendProgressBarUpdate(this, 2, this.worker.currentZ);
	        }
        }
        
        this.lastPowerAcc = this.worker.powerAccum;
        this.lastX = this.worker.currentX;
        this.lastY = this.worker.currentZ;        
    }

	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int bar, int value)
    {
		this.worker.updateProgressBar(bar, value);
    }
}
