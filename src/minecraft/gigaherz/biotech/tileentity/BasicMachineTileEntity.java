package gigaherz.biotech.tileentity;

import gigaherz.biotech.Biotech;

import gigaherz.biotech.item.CommandCircuit;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.EnumSet;

import com.google.common.io.ByteArrayDataInput;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.implement.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityReceiver;
import cpw.mods.fml.common.network.PacketDispatcher;

// Default machine TileEntity
// Has a power connection at the back
// Has a powered state
// Has an inventory

public class BasicMachineTileEntity extends TileEntityElectricityReceiver implements IInventory, ISidedInventory, IPacketReceiver
{
    protected ItemStack[] inventory;

    //How much power is stored?
    private double electricityStored  = 0;
    private double electricityMaxStored  = 5000;
    
    private int playersUsing = 0;
    
    //Is the macine currently powered, and did it change?
    public boolean prevIsPowered, isPowered = false;

    private int facing;
    
    public BasicMachineTileEntity()
    {
        super();
        
        this.inventory = new ItemStack[24];
        
        ElectricityConnections.registerConnector(this, EnumSet.allOf(ForgeDirection.class));
    }
    
    @Override
    public void initiate()
    {
        refreshConnectorsAndWorkArea();
    }

    public void refreshConnectorsAndWorkArea()
    {
        int front = 0; 
    	
    	switch(this.getFacing())
    	{
    	case 2:
    		front = 2;
    		break;
    	case 3:
    		front = 3;
    		break;
    	case 4:
    		front = 4;
    		break;
    	case 5:
    		front = 5;
    		break;
    	default:
    		front = 2;
   			break;
    	}

    	ForgeDirection direction = ForgeDirection.getOrientation(front);
    	
        ElectricityConnections.registerConnector(this, EnumSet.of(direction));
    }

    @Override
    public int getSizeInventory()
    {
        return this.inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex)
    {
        if (slotIndex >= this.inventory.length)
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
	public void openChest()
	{
		this.playersUsing++;
	}

	@Override
	public void closeChest()
	{
		this.playersUsing--;
	}


    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        //this.progressTime = tagCompound.getShort("Progress");
        
        this.facing = tagCompound.getShort("facing");
        this.isPowered = tagCompound.getBoolean("isPowered");
        this.electricityStored = tagCompound.getDouble("electricityStored");
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

        tagCompound.setShort("facing", (short)this.facing);
        tagCompound.setBoolean("isPowered", this.isPowered);
        tagCompound.setDouble("electricityStored", (double)this.electricityStored);
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
        return "BasicMachineInventory";
    }

    public void setPowered(boolean powered)
    {
    	this.isPowered = powered;
    	
    	if(prevIsPowered != powered)
    	{
    		prevIsPowered = isPowered;
    		PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj);
    	}
     }
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
        
        //System.out.println("Remote: " + worldObj.isRemote + " playersUsing: " + this.playersUsing);
        
		if(!worldObj.isRemote)
		{
	        int front = 0; 
	    	
	    	switch(this.getFacing())
	    	{
	    	case 2:
	    		front = 2;
	    		break;
	    	case 3:
	    		front = 3;
	    		break;
	    	case 4:
	    		front = 4;
	    		break;
	    	case 5:
	    		front = 5;
	    		break;
	    	default:
	    		front = 2;
	   			break;
	    	}

	    	ForgeDirection direction = ForgeDirection.getOrientation(front);

	        TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), direction);
	        
		        if (inputTile != null)
		        {
		            if (inputTile instanceof IConductor)
		            {
		                IConductor conductor = (IConductor) inputTile;
		                ElectricityNetwork network = conductor.getNetwork();
		
		                if (this.electricityStored < this.electricityMaxStored)
		                {
		                	double electricityNeeded = this.electricityMaxStored - this.electricityStored; 
		                	
		                    network.startRequesting(this, electricityNeeded, electricityNeeded >= getVoltage() ? getVoltage() : electricityNeeded);
		
		                    this.setElectricityStored(electricityStored + (network.consumeElectricity(this).getWatts()));
		                    
		                }
						else if(electricityStored >= electricityMaxStored)
		                {
		                    network.stopRequesting(this);
		                }
		            }
		        }
		        
				if (this.inventory[0] != null && this.electricityStored < this.electricityMaxStored)
				{
					if (this.inventory[0].getItem() instanceof IItemElectric)
					{
						IItemElectric electricItem = (IItemElectric) this.inventory[0].getItem();

						if (electricItem.canProduceElectricity())
						{
							double joulesReceived = electricItem.onUse(electricItem.getMaxJoules(this.inventory[0]) * 0.005, this.inventory[0]);
							this.setElectricityStored(this.electricityStored + joulesReceived);
						}
					}
				}
		        
				if (this.ticks % 3 == 0 && this.playersUsing > 0)
				{
					PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
				}
		}
		
    }



    @Override
    public int getStartInventorySide(ForgeDirection side)
    {
        ForgeDirection left, right;

        switch (this.facing)
        {
            case 2: // North
                left = ForgeDirection.WEST;
                right = ForgeDirection.EAST;
                break;

            case 3: // South
                left = ForgeDirection.EAST;
                right = ForgeDirection.WEST;
                break;

            case 4: // West
                left = ForgeDirection.NORTH;
                right = ForgeDirection.SOUTH;
                break;

            case 5: // East
                left = ForgeDirection.SOUTH;
                right = ForgeDirection.NORTH;
                break;

            default:
                left = ForgeDirection.WEST;
                right = ForgeDirection.EAST;
                break;
        }

        if (side == left)
        {
            return 0;
        }

        if (side == right)
        {
            return 9;
        }

        if (side == ForgeDirection.UP)
        {
            return 18;
        }

        return 0;
    }

    @Override
    public int getSizeInventorySide(ForgeDirection side)
    {
        ForgeDirection left, right;

        switch (this.facing)
        {
            case 2: // North
                left = ForgeDirection.WEST;
                right = ForgeDirection.EAST;
                break;

            case 3: // South
                left = ForgeDirection.EAST;
                right = ForgeDirection.WEST;
                break;

            case 4: // West
                left = ForgeDirection.NORTH;
                right = ForgeDirection.SOUTH;
                break;

            case 5: // East
                left = ForgeDirection.SOUTH;
                right = ForgeDirection.NORTH;
                break;

            default:
                left = ForgeDirection.WEST;
                right = ForgeDirection.EAST;
                break;
        }

        if (side == left)
        {
            return 9;
        }

        if (side == right)
        {
            return 9;
        }

        if (side == ForgeDirection.UP)
        {
            return 3;
        }

        return 0;
    }

    public boolean hasItemInInputArea(ItemStack itemStack)
    {
        for (int i = 0; i < 9; i++)
        {
            ItemStack slot = inventory[i];

            if (slot == null)
            {
                continue;
            }

            if (slot.itemID != itemStack.itemID)
            {
                continue;
            }

            int damage = itemStack.getItemDamage();

            if (damage < 0 || slot.getItemDamage() == damage)
            {
                return true;
            }
        }

        return false;
    }

    public boolean hasAnyBlockInInputArea()
    {
        for (int i = 0; i < 9; i++)
        {
            ItemStack slot = inventory[i];

            if (slot == null)
            {
                continue;
            }

            Item item = slot.getItem();

            if (item instanceof ItemBlock)
            {
                return true;
            }
        }

        return false;
    }

    public boolean hasSpaceInOutputAreaForItem(ItemStack itemStack)
    {
        for (int i = 9; i < 18; i++)
        {
            ItemStack slot = inventory[i];

            if (slot == null)
            {
                return true;
            }

            if (slot.itemID != itemStack.itemID)
            {
                continue;
            }

            int damage = itemStack.getItemDamage();

            if (damage >= 0 && slot.getItemDamage() != damage)
            {
                continue;
            }

            if (slot.stackSize + itemStack.stackSize <= slot.getMaxStackSize())
            {
                return true;
            }
        }

        return false;
    }

    public boolean hasToolInToolArea(ItemStack itemStack)
    {
        for (int i = 18; i < 21; i++)
        {
            ItemStack slot = inventory[i];

            if (slot == null)
            {
                continue;
            }

            if (slot.itemID == itemStack.itemID)
            {
                return true;
            }
        }

        return false;
    }

    public void addStackToOutputArea(ItemStack itemStack)
    {
        for (int i = 9; i < 18; i++)
        {
            ItemStack slot = inventory[i];

            if (slot == null)
            {
                continue;
            }

            if (slot.itemID != itemStack.itemID)
            {
                continue;
            }

            int damage = itemStack.getItemDamage();

            if (damage >= 0 && slot.getItemDamage() != damage)
            {
                continue;
            }

            if (slot.stackSize <= slot.getMaxStackSize())
            {
                int newSize = Math.min(slot.stackSize + itemStack.stackSize, slot.getMaxStackSize());
                int howMany = newSize - slot.stackSize;
                slot.stackSize = newSize;
                itemStack.stackSize -= howMany;

                if (itemStack.stackSize == 0)
                {
                    return;
                }
            }
        }

        // partial stack not found, or not enough space, search for empty slots
        for (int i = 9; i < 18; i++)
        {
            ItemStack slot = inventory[i];

            if (slot == null)
            {
                ItemStack copy = itemStack.copy();
                int newSize = copy.stackSize; //Math.min(itemStack.stackSize, slot.getMaxStackSize());
                int howMany = newSize;
                copy.stackSize = newSize;
                itemStack.stackSize -= howMany;
                setInventorySlotContents(i, copy);

                if (itemStack.stackSize == 0)
                {
                    return;
                }
            }
        }
    }

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
	{
		try
		{
			if (this.worldObj.isRemote)
			{
				this.isPowered = dataStream.readBoolean();
				this.facing = dataStream.readInt();
				this.electricityStored = dataStream.readDouble();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket(Biotech.CHANNEL, this, this.isPowered, this.facing, this.electricityStored);
	}

	public int getFacing() 
	{
		return facing;
	}

	public void setFacing(int facing) 
	{
		this.facing = facing;
	}

	public double getElectricityStored() 
	{
		return electricityStored;
	}

	public void setElectricityStored(double joules)
	{
		electricityStored = Math.max(Math.min(joules, getMaxElectricity()), 0);
	}	
	
	public double getMaxElectricity() 
	{
		return electricityMaxStored;
	}
}
