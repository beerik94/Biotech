package gigaherz.biotech.tileentity;

import gigaherz.biotech.Biotech;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import com.google.common.io.ByteArrayDataInput;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityCow;
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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityReceiver;

public class CowMilkerTileEntity extends BasicMachineTileEntity implements IInventory, ISidedInventory, IPacketReceiver
{
	private ItemStack Inventory[];
	
	private int tickCounter;
	private int scantickCounter;
	
	protected List<EntityLiving> CowList = new ArrayList<EntityLiving>();
	
	public static final double WATTS_PER_ACTION = 500;
	public static final double WATTS_PER_IDLE_ACTION = 25;
	
	// Time idle after a tick
	public static final int IDLE_TIME_AFTER_ACTION = 80;
	public static final int IDLE_TIME_NO_ACTION = 40;
	
	//How much power is stored?
    private double electricityStored  = 0;
    private double electricityMaxStored  = 5000;
    
    //How much milk is stored?
    public static int milkStored = 0;
    public static int milkMaxStored = 3000;
    private static int cowMilk = 10;
    private static int milkIntRandom;
    private static Random milkRandom = new Random();
    
    //Is the machine currently powered, and did it change?
    public boolean prevIsPowered, isPowered = false;

	/**
	 * The ItemStacks that hold the items currently being used in the cow milker
	 */
	private ItemStack[] lefttopSlot = new ItemStack[4];
	private Item[] leftbottomSlot = new Item[1];

	private int facing;
	private int playersUsing = 0;
	private int idleTicks;
	
	
	public CowMilkerTileEntity()
	{
		super();
		Inventory = new ItemStack[36];
		ElectricityConnections.registerConnector(this, EnumSet.noneOf(ForgeDirection.class));
	
		for(int idx = 1; idx <= 10; ++idx)
		{
			milkIntRandom = milkRandom.nextInt(30);
		}
	}
	
	@Override
    public void initiate()
    {
        refreshConnectorsAndWorkArea();
    }
	
	public void refreshConnectorsAndWorkArea()
	{
		int orientation = this.getBlockMetadata();	   
		
		ForgeDirection direction = ForgeDirection.getOrientation(orientation);
		
		ElectricityConnections.registerConnector(this, EnumSet.of(direction));
	}

	public void scanCows()
	{
		
		int xminrange = xCoord- getScanRange();
		int xmaxrange = xCoord+ getScanRange()+1;
		int yminrange = yCoord- getScanRange();
		int ymaxrange = yCoord+ getScanRange()+1;
		int zminrange = zCoord- getScanRange();
		int zmaxrange = zCoord+ getScanRange()+1;
		
		List<EntityCow> scannedCowslist = worldObj.getEntitiesWithinAABB(EntityCow.class, AxisAlignedBB.getBoundingBox(xminrange, yminrange, zminrange, xmaxrange, ymaxrange, zmaxrange));
	
		for(EntityCow Living : scannedCowslist)
		{
			if(!CowList.contains(Living))
			{
				CowList.add(Living);
			}
		}	
	}
	
	public void milkCows()
	{		
		
		//if(this.getElectricityStored() > WATTS_PER_ACTION)
		//{
			milkStored += cowMilk;
			if(CowList.size() != 0)
			{
				CowList.remove(1);
			}

		//}
		
		
	}
	
	public int getScanRange() {
		if (getStackInSlot(2) != null) {
			if (getStackInSlot(1).getItem() == Biotech.RangeUpgrade) {
				return (getStackInSlot(1).stackSize+5);
			}
		}
		return 5;
	}
	
	@Override
    public void updateEntity()
    {
        if (!worldObj.isRemote)
        {
	        if(this.idleTicks > 0)
	        {
	            if (this.ticks % 40 == 0)
	            	this.setElectricityStored(this.getElectricityStored() - this.WATTS_PER_IDLE_ACTION);
	        	
	        	--this.idleTicks;
	        	return;
	        }
	        
	        if(this.isRedstoneSignal())
	        {
	        	if(scantickCounter == 50)
	        	{
	        		scanCows();
	        		scantickCounter = 0;
	        	}
	        	if(CowList.size() == 0)
	        	{
	        		return;
	        	}
	        	else if(tickCounter == 100)
	        	{
	        		milkCows();
	        		tickCounter = 0;
	        	}
	            tickCounter++;
	            scantickCounter++;
	        	this.setElectricityStored(this.getElectricityStored() - this.WATTS_PER_ACTION);
	        }
	        cowMilk += milkIntRandom;
	        if(milkStored >= milkMaxStored)
	        {
	        	milkStored = milkMaxStored;
	        }
	        if(tickCounter >= 150)
	        {
	        	tickCounter = 0;
	        }
	        if(scantickCounter >= 100)
	        {
	        	tickCounter = 0;
	        }
	        
        }
        System.out.println(CowList);
        System.out.println(tickCounter);
        super.updateEntity();
    }
	
	public boolean isRedstoneSignal(){
		if(worldObj.isBlockGettingPowered(xCoord,yCoord, zCoord) ||
		    worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord))
			return true;
		return false;
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
	public void openChest()
	{
		if (!this.worldObj.isRemote)
		{
			PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 15);
		}
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
        return "Cow Milker";
    }
	
	public void setPowered(boolean powered, World world, int x, int y, int z)
    {
    	this.isPowered = powered;
    	
    	if(prevIsPowered != powered)
    	{
    		prevIsPowered = isPowered;
    		PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj);
    	}
    }
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
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
		return PacketManager.getPacket(Biotech.CHANNEL, this, this.isPowered, this.facing);
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

	public void setElectricityStored(double joules, Object... data)
	{
		electricityStored = Math.max(Math.min(joules, getMaxJoules()), 0);
	}	

	public double getMaxJoules(Object... data) 
	{
		return electricityMaxStored;
	}
	
}