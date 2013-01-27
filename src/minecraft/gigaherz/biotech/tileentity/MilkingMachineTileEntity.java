package gigaherz.biotech.tileentity;

import gigaherz.biotech.Biotech;

import java.util.ArrayList;
import java.util.List;

import com.google.common.io.ByteArrayDataInput;

import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.implement.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
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


public class MilkingMachineTileEntity extends BasicMachineTileEntity implements IPacketReceiver
{
	private int tickCounter;
	private int scantickCounter;
	
	protected List<EntityLiving> CowList = new ArrayList<EntityLiving>();
	
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
    
    private boolean isMilking = false;
    
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
	
	
	public MilkingMachineTileEntity()
	{
		super();
	}
	
	@Override
    public void initiate()
    {
        refreshConnectorsAndWorkArea();
    }
	
	@Override
    public void refreshConnectorsAndWorkArea()
    {
    	super.refreshConnectorsAndWorkArea();
    	
    	ForgeDirection direction = ForgeDirection.getOrientation(getFacing());
    	
        if (direction.offsetZ > 0)
        {
            this.minX = -2;
            this.maxX =  2;
            this.minZ = -5 * direction.offsetZ;
            this.maxZ = -1 * direction.offsetZ;
        }
        else if (direction.offsetZ < 0)
        {
            this.minX = -2;
            this.maxX =  2;
            this.minZ = -1 * direction.offsetZ;
            this.maxZ = -5 * direction.offsetZ;
        }
        else if (direction.offsetX > 0)
        {
            this.minZ = -2;
            this.maxZ =  2;
            this.minX = -5 * direction.offsetX;
            this.maxX = -1 * direction.offsetX;
        }
        else if (direction.offsetX < 0)
        {
            this.minZ = -2;
            this.maxZ =  2;
            this.minX = -1 * direction.offsetX;
            this.maxX = -5 * direction.offsetX;
        }

        if (this.currentX < this.minX || this.currentX > this.maxX)
        {
            this.currentX = this.minX;
        }

        if (this.currentZ < this.minZ || this.currentZ > this.maxZ)
        {
            this.currentZ = this.minZ;
        }
    }

	public void scanForMilkers()
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
		if(CowList.size() != 0)
		{
			
			
		}
	}

	public int getScanRange() {
		return 3;
	}
	
	@Override
    public void updateEntity()
    {
		
        if (!worldObj.isRemote)
        {	        
	        if(this.CheckRedstonePowered())
	        {
	        	this.setPowered(true);
	        	if(scantickCounter >= 40)
	        	{
	        		scanForMilkers();
	        		scantickCounter = 0;
	        	}
	        	/*
	        	if(CowList.size() != 0 && tickCounter >= 100)
	        	{
	        		milkCows();
	    			isMilking = true;
	        		tickCounter = 0;
	        		this.setPowered(false);
	        	}
	        	*/
	            //tickCounter++;
	            scantickCounter++;
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
	
	public boolean CheckRedstonePowered()
	{		
		if(MilkingManagerTileEntity.isRedstonePowered)
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
        return "Milking Machine";
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