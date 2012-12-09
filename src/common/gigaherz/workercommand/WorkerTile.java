package gigaherz.workercommand;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashSet;

import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.tile.TileEntityElectricityReceiver;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.src.BlockFurnace;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.IInventory;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet132TileEntityData;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class WorkerTile extends TileEntityElectricityReceiver implements IInventory, ISidedInventory
{	// The amount of watts required by the
	// electric furnace per tick
	public static final double WATTS_PER_TICK = 1000;
	public static final double WATTS_PER_TIME = 50;
	
    private ItemStack[] inventory;

    public WorkerTile()
    {
        this.inventory = new ItemStack[24];
    }

    @Override
    public int getSizeInventory()
    {
        return this.inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex)
    {
    	if(slotIndex >= this.inventory.length)
    	{
    		System.out.println("Tried to access slot " + slotIndex);
    		return null;
    	}
        return this.inventory[slotIndex];
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        this.inventory[slot] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit())
        {
            stack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int amount)
    {
        ItemStack stack = getStackInSlot(slotIndex);

        if (stack != null)
        {
            if (stack.stackSize <= amount)
            {
                setInventorySlotContents(slotIndex, null);
            }
            else
            {
                stack = stack.splitStack(amount);

                if (stack.stackSize == 0)
                {
                    setInventorySlotContents(slotIndex, null);
                }
            }
        }

        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotIndex)
    {
        ItemStack stack = getStackInSlot(slotIndex);

        if (stack != null)
        {
            setInventorySlotContents(slotIndex, null);
        }

        return stack;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest() {}

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        
        //this.progressTime = tagCompound.getShort("Progress");
        //this.powerTicks = tagCompound.getShort("PowerTicks");
        
        NBTTagList tagList = tagCompound.getTagList("Inventory");

        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
            byte slot = tag.getByte("Slot");

            if (slot >= 0 && slot < inventory.length)
            {
                inventory[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        
        //tagCompound.setShort("Progress", (short)this.progressTime);
        //tagCompound.setShort("PowerTicks", (short)this.powerTicks);
        
        NBTTagList itemList = new NBTTagList();

        for (int i = 0; i < inventory.length; i++)
        {
            ItemStack stack = inventory[i];

            if (stack != null)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                stack.writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }

        tagCompound.setTag("Inventory", itemList);
    }

    @Override
    public String getInvName()
    {
        return "GrinderInventory";
    }

    @Override
    public void updateEntity()
    {
        if (this.worldObj.isRemote)
        	return;
        
        boolean stateChanged = false;

        if (stateChanged)
        {
            this.onInventoryChanged();
        }
    }

	/**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canGrind()
    {
        if (this.inventory[0] == null)
        {
            return false;
        }
        else
        {
            ItemStack var1 = Worker.findRecipeResult(this.inventory[0]);

            if (var1 == null)
            {
                return false;
            }

            if (this.inventory[1] == null)
            {
                return true;
            }

            if (!this.inventory[1].isItemEqual(var1))
            {
                return false;
            }

            int result = inventory[1].stackSize + var1.stackSize;
            return (result <= getInventoryStackLimit() && result <= var1.getMaxStackSize());
        }
    }

    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    public void grindItem()
    {
        if (this.canGrind())
        {
            ItemStack var1 = Worker.findRecipeResult(this.inventory[0]);
            
            if (this.inventory[1] == null)
            {
                this.inventory[1] = var1.copy();
            }
            else if (this.inventory[1].isItemEqual(var1))
            {
            	this.inventory[1].stackSize += var1.stackSize;
            }

            --this.inventory[0].stackSize;

            if (this.inventory[0].stackSize <= 0)
            {
                this.inventory[0] = null;
            }
        }
    }

    @Override
    public int getStartInventorySide(ForgeDirection side)
    {
        if (side == ForgeDirection.UP)
        {
            return 0;
        }

        return 1;
    }

    @Override
    public int getSizeInventorySide(ForgeDirection side)
    {
        return 1;
    }

    /*
    private void sendFlowUpdate() {
    	ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
    	DataOutputStream outputStream = new DataOutputStream(bos);
    	try {
    		outputStream.writeInt(this.worldObj.getWorldInfo().getDimension());
    		outputStream.writeInt(this.xCoord);
    		outputStream.writeInt(this.yCoord);
    		outputStream.writeInt(this.zCoord);
    		outputStream.writeInt(2);
    		outputStream.writeInt(this.powerFlow);
		} catch (Exception ex) {
    		ex.printStackTrace();
    	}

    	Packet250CustomPayload packet = new Packet250CustomPayload();
    	packet.channel = "Plasma";
    	packet.data = bos.toByteArray();
    	packet.length = bos.size();
    	
    	PacketDispatcher.sendPacketToAllAround(this.xCoord, this.yCoord, this.zCoord, 20, this.worldObj.getWorldInfo().getDimension(), packet);
	}	*/
    
	public void updateProgressBar(int par1, int par2) {

		/*
        if (par1 == 0)
        {
            this.progressTime = par2;
        }

        if (par1 == 1)
        {
            this.powerTicks = par2;
        }
        
        if (par1 == 2)
        {
            this.powerFlow = par2;

            Worker.updateBlockState(this.isPowered(), this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        }
        */
	}
}
