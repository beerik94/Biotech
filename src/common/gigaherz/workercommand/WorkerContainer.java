package gigaherz.workercommand;

import java.util.EnumSet;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.registry.TickRegistry;
import net.minecraft.src.*;

public class WorkerContainer extends Container
{
    protected WorkerTile worker;

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
                		92, 
                		17 + i * 18));
        }

        for (int i = 0; i < 3; i++)
        {
            addSlotToContainer(new Slot(tileEntity, 
                		i + 21,
                		68, 
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
        //crafter.sendProgressBarUpdate(this, 0, this.worker.progressTime);
    }
    
    public void updateCraftingResults()
    {
        super.updateCraftingResults();

        /*for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting crafter = (ICrafting)this.crafters.get(i);
	
	        if (this.lastProgress != this.worker.progressTime)
	        {
	        	crafter.sendProgressBarUpdate(this, 0, this.worker.progressTime);
	        }
	
	        if (this.lastPowerTicks != this.worker.powerTicks)
	        {
	        	crafter.sendProgressBarUpdate(this, 1, this.worker.powerTicks);
	        }
	        
	        if (this.lastPowerFlow != this.worker.powerFlow)
	        {
	            crafter.sendProgressBarUpdate(this, 2, this.worker.powerFlow);
	        }
        }
        
        this.lastProgress = this.worker.progressTime;
        this.lastPowerTicks = this.worker.powerTicks;
        this.lastPowerFlow = this.worker.powerFlow;
        */
    }

	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
		this.worker.updateProgressBar(par1, par2);
    }
}
