package biotech.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.implement.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import biotech.Biotech;

import com.google.common.io.ByteArrayDataInput;

public class MilkingManagerTileEntity extends BasicMachineTileEntity implements IInventory, ISidedInventory, IPacketReceiver, ITankContainer
{
	private int tickCounter;
	private int scantickCounter;
	
	private List<MilkingMachineTileEntity> MachineList = new ArrayList<MilkingMachineTileEntity>();
	private int MachineListSize = 0;
	
	// Watts being used per action / idle action
	public static final double WATTS_PER_TICK = 250;
	public static final double WATTS_PER_IDLE_TICK = 25;
	
	// Time idle after a tick
	public static final int IDLE_TIME_AFTER_ACTION = 60;
	public static final int IDLE_TIME_NO_ACTION = 30;
	
	// Watts being used per pump action
	public static final double WATTS_PER_PUMP_ACTION = 50;
	
	//How much power is stored?
    private double electricityStored  = 0;
    private double electricityMaxStored  = 5000;
	
    //How much milk is stored?
    private int milkStored = 0;
    private int milkMaxStored = 3000;
    private int cowMilk = 10;
    
    private boolean isMilking = false;
    public boolean bucketIn = false;
	public int bucketTimeMax = 100;
	public int bucketTime = 0;
	
	// Amount of milliBuckets of internal storage
	private static final int MILK_CAPACITY_MILLIBUCKET = 3000;
	private int milkContentsMilliBuckets = 0;
	private ILiquidTank internalLiquidTank;
    
    //Is the machine currently powered, and did it change?
    public boolean prevIsPowered, isPowered = false;

	private int facing;
	private int playersUsing = 0;
	private int idleTicks;
	
    public int currentX = 0;
    public int currentZ = 0;
    public int currentY = 0;
	
    public int minX, maxX;
    public int minZ, maxZ;
	
    
    /**
	 * note this set once at construction and never changed because a the manager can only accept the kind of liquid it is designed for
	 */
	private final LiquidStack requiredLiquid;
	
	public MilkingManagerTileEntity()
	{
		super();
		this.requiredLiquid = LiquidDictionary.getLiquid("milk", 1);
		internalLiquidTank = new LiquidTank(requiredLiquid, MILK_CAPACITY_MILLIBUCKET, this);
		
	}

	public void scanForMachines()
	{
		int xminrange = xCoord- getScanRange();
		int xmaxrange = xCoord+ getScanRange()+1;
		int yminrange = yCoord- getScanRange();
		int ymaxrange = yCoord+ getScanRange()+1;
		int zminrange = zCoord- getScanRange();
		int zmaxrange = zCoord+ getScanRange()+1;

		for(int xx = xminrange; xx <= xmaxrange; xx++)
		{
			for(int yy = yminrange; yy <= ymaxrange; yy++)
			{
				for(int zz = zminrange; zz <= zmaxrange; zz++)
				{
					TileEntity tileEntity = worldObj.getBlockTileEntity(xx, yy, zz);
					
					if(tileEntity instanceof MilkingMachineTileEntity)
					{
						if(MachineList.contains((MilkingMachineTileEntity) tileEntity))
						{
							return;
						}
						else
						{
							MachineList.add((MilkingMachineTileEntity) tileEntity);
						}
					}
				}
			}
		}
	}
	
	public int getScanRange() 
	{
		if (getStackInSlot(1) != null) 
		{
			if (inventory[1].isItemEqual(Biotech.bioCircuitRangeUpgrade))
			{
				return (getStackInSlot(1).stackSize + 5);
			}
		}
		return 3;
	}
	
	@Override
    public void updateEntity()
    {
        if (!worldObj.isRemote)
        {	        
	        if(this.isRedstoneSignal())
	        {
	        	setMachineSize();
	        	this.setPowered(true);
	        	if(scantickCounter >= 40)
	        	{
	        		scanForMachines();
	        		scantickCounter = 0;
	        	}
	        	int i = 0;
				while(i < MachineList.size())
				{
					MilkingMachineTileEntity machine = MachineList.get(i);
					machine.ReceivedRedstone = true;
					if(MachineList.get(i).isInvalid())
					{
						MachineList.remove(i);
					}
					i++;
				}
				if(MachineList.size() == 0)
				{
					i = 0;
				}			
	            scantickCounter++;
	        }
	        if(milkStored >= 30)
	        {
	        	if(inventory[2] != null && inventory[3] == null)
	        	{
	        		
	        		this.bucketIn = true;
	        		if(bucketTime >= bucketTimeMax)
	        		{
	        			if(inventory[2].stackSize >= 2)
	        			{
	        				inventory[2].stackSize -= 1;
	        			}
	        			else
	        			{
	        				inventory[2] = null;
	        			}
	        			ItemStack bMilk = new ItemStack(Item.bucketMilk);
	        			inventory[3] = (bMilk);
	        			milkStored -= 30;
	        			bucketTime = 0;
	        			this.bucketIn = false;
	        		}
	        	}
	        }
	        if(bucketTime < bucketTimeMax)
    		{
    			bucketTime++;
    		}
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
	        if (this.ticks % 3 == 0 && this.playersUsing > 0)
			{
				PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
			}
        
	        int front = 0; 
	    	
	    	switch(this.getFacing())
	    	{
	    	case 2:
	    		front = 3;
	    		break;
	    	case 3:
	    		front = 2;
	    		break;
	    	case 4:
	    		front = 5;
	    		break;
	    	case 5:
	    		front = 4;
	    		break;
	    	default:
	    		front = 3;
	   			break;
	    	}
	    	
	    	ForgeDirection direction = ForgeDirection.getOrientation(front);
	
	        TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), direction);
	        
	        ElectricityNetwork network = ElectricityNetwork.getNetworkFromTileEntity(inputTile, direction);
	        
	        if (inputTile != null)
	        {   
                if (this.electricityStored < this.electricityMaxStored)
                {
                	double electricityNeeded = this.electricityMaxStored - this.electricityStored; 
                	
                    network.startRequesting(this, electricityNeeded, electricityNeeded >= getVoltage() ? getVoltage() : electricityNeeded);

                    this.setElectricityStored(electricityStored + (network.consumeElectricity(this).getWatts()));
                    
                    if (UniversalElectricity.isVoltageSensitive)
    				{
                    	ElectricityPack electricityPack = network.consumeElectricity(this);
    					if (electricityPack.voltage > this.getVoltage())
    					{
    						this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, 2f, true);
    					}
    				}
                    
                }
				else if(electricityStored >= electricityMaxStored)
                {
                    network.stopRequesting(this);
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
        }
        super.updateEntity();
    }
	
	public boolean isRedstoneSignal(){
		if(worldObj.isBlockGettingPowered(xCoord,yCoord, zCoord) || worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord))
		{
			return true;
		}
		return false;
	}

	@Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        //this.progressTime = tagCompound.getShort("Progress");
        
        this.facing = tagCompound.getShort("facing");
        this.isPowered = tagCompound.getBoolean("isPowered");
        this.milkStored = tagCompound.getInteger("milkStored");  
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
        tagCompound.setInteger("milkStored", (int)this.milkStored);
        tagCompound.setDouble("electricityStored", this.electricityStored);
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
        return "Milking Manager";
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
				this.milkStored = dataStream.readInt();
				this.electricityStored = dataStream.readDouble();
				this.MachineListSize = dataStream.readInt();
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
		return PacketManager.getPacket(Biotech.CHANNEL, this, this.isPowered, this.facing, this.milkStored, this.electricityStored, this.MachineListSize);
	}
	
	public int getFacing() 
	{
		return facing;
	}

	public void setFacing(int facing) 
	{
		this.facing = facing;
	}
    
    public int getMilkStored()
    {
    	return this.milkContentsMilliBuckets;
    }
    
    public void setMilkStored(int amount)
	{
		this.milkContentsMilliBuckets = amount;
	}
    
    public int getMaxMilk()
    {
    	return this.MILK_CAPACITY_MILLIBUCKET;
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
	
	public void setMachineSize()
	{
		MachineListSize = MachineList.size();
	}
	
	public int getMachineSize()
	{
		return MachineListSize;
	}

	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
		boolean isValidResource = isValidLiquid(from, resource);

		int availibleCapactity = MILK_CAPACITY_MILLIBUCKET
				- milkContentsMilliBuckets;
		int inputAmount = resource.amount;
		if (inputAmount <= availibleCapactity) {
			if (doFill) {
				this.milkContentsMilliBuckets += inputAmount;
			}
			return inputAmount;
		} else {
			if (doFill) {
				this.milkContentsMilliBuckets = MILK_CAPACITY_MILLIBUCKET;
			}
			return availibleCapactity;
		}
	}
	
	/**
	 * @param from
	 * @param resource
	 */
	private boolean isValidLiquid(ForgeDirection from, LiquidStack resource) {
		if (resource == null
				|| (from != ForgeDirection.DOWN)) {
			return false;
		}

		return this.requiredLiquid.isLiquidEqual(resource);
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		// there is only 1 tank so ignore tank index and do a valid fill
		return fill(ForgeDirection.DOWN, resource, doFill);
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction) {
		return new ILiquidTank[] { getTank(direction, this.requiredLiquid) };
	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
		if ((direction == ForgeDirection.DOWN)
				&& type.isLiquidEqual(this.requiredLiquid)) {
			return this.internalLiquidTank;
		}
		return null;
	}
}