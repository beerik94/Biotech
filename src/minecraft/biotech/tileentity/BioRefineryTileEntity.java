package biotech.tileentity;

import java.util.EnumSet;

import liquidmechanics.api.IColorCoded;
import liquidmechanics.api.IReadOut;
import liquidmechanics.api.helpers.ColorCode;
import liquidmechanics.api.liquids.IPressure;
import liquidmechanics.api.liquids.LiquidData;
import liquidmechanics.api.liquids.LiquidHandler;

import biotech.Biotech;

import com.google.common.io.ByteArrayDataInput;

import net.minecraft.block.Block;
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
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidTank;
import net.minecraftforge.oredict.OreDictionary;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.implement.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;

public class BioRefineryTileEntity extends BasicMachineTileEntity implements IPacketReceiver, IColorCoded, IPressure, IReadOut
{
	// Watts being used per action / idle action
	public static final double WATTS_PER_TICK = 50;
	public static final double WATTS_PER_IDLE_TICK = 5.0;
	
	// Time idle after a tick
	public static final int IDLE_TIME_AFTER_ACTION = 60;
	public static final int IDLE_TIME_NO_ACTION = 30;
	
	//Is the machine currently powered, and did it change?
    public boolean prevIsPowered, isPowered = false;
	
	//How much power is stored?
    private double electricityStored  = 0;
    private double electricityMaxStored  = 5000;
    
    //Amount of milliBuckets of internal storage
    private ColorCode color = ColorCode.WHITE;
 	private static final int milkMaxStored = 15 * LiquidContainerRegistry.BUCKET_VOLUME;
 	private int milkStored = 0;
 	private ILiquidTank milkBioTank = new LiquidTank(Biotech.milkLiquid, milkMaxStored, this);
	private int bucketVol = LiquidContainerRegistry.BUCKET_VOLUME;
 	
	private int facing;
	private int playersUsing = 0;
	private int idleTicks;
	
	public BioRefineryTileEntity()
    {
        super();
    }
	
	@Override
	public void updateEntity()
	{
		if (!worldObj.isRemote)
		{
			if (this.ticks % 3 == 0 && this.playersUsing > 0)
			{
				PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
			}
		    this.fillFrom(ForgeDirection.DOWN);
	        this.chargeUp();
	        this.Refine();
	        if(this.getMilkStored() >= this.getMaxMilk())
	        {
	        	this.milkStored = this.getMaxMilk();
	        }
		}
		super.updateEntity();
	}
	
	public boolean isRedstoneSignal(){
		if(worldObj.isBlockGettingPowered(xCoord,yCoord, zCoord) || worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) { return true; }
		return false;
	}
	
	/**
	 * Refines milk into Mekanism bioFuel
	 */
	public void Refine()
	{
		ItemStack bedrockStack = new ItemStack(Block.bedrock, 1);
		this.inventory[1] = (bedrockStack);
		
		if(this.inventory[1] != null)
		{
			if (this.inventory[1].stackSize <= 62 && this.getMilkStored() >= bucketVol)
	        {
				this.inventory[1].stackSize += 2;
				this.setMilkStored(bucketVol, false);
	        }
			else if(this.inventory[2].getItem() == Item.seeds && this.inventory[1].stackSize <= 60 && this.milkStored >= 1000)
			{
				this.inventory[1].stackSize += 4;
				this.setMilkStored(bucketVol, false);
			}
		}
	}
	
	/**
     * Use this to fill from a pipe or tank connected to the right side
     */
    public void fillFrom(ForgeDirection dir)
    {
            TileEntity ent = worldObj.getBlockTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
            if(ent instanceof ITankContainer)
            {
                    ((ITankContainer) ent).drain(dir, bucketVol, true);
                    this.setMilkStored(bucketVol, true);
            }
    }
	
	@Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        
        this.facing = tagCompound.getShort("facing");
        this.isPowered = tagCompound.getBoolean("isPowered");
        this.electricityStored = tagCompound.getDouble("electricityStored");
        this.milkStored = tagCompound.getInteger("milkStored");
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

        tagCompound.setShort("facing", (short)this.facing);
        tagCompound.setBoolean("isPowered", this.isPowered);
        tagCompound.setDouble("electricityStored", this.electricityStored);
        tagCompound.setInteger("milkStored", this.milkStored);
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
        return "Bio Refinery";
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
				this.milkStored = dataStream.readInt();
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
		return PacketManager.getPacket(Biotech.CHANNEL, this, this.isPowered, this.facing, this.electricityStored, this.milkStored);
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
	
	public int getMilkStored()
    {
    	return this.milkStored;
    }
    
	/**
	 * Sets the current volume of milk stored
	 * 
	 * @param amount - volume sum
	 * @param add - if true it will add the amount to the current sum
	 */
	public void setMilkStored(int amount, boolean add)
	{
		if (add)
		{
			this.milkStored += amount;
		}
		else
		{
			this.milkStored -= amount;
		}
	}
    
    public int getMaxMilk()
    {
    	return this.milkStored;
    }

    @Override
	public String getMeterReading(EntityPlayer user, ForgeDirection side)
	{
		return "Milk:"+this.milkStored;
	}

    @Override
    public int presureOutput(LiquidData type, ForgeDirection dir)
    {
            if (type.getColor() == color){ return type.getPressure();}
           
            return 0;
    }

    @Override
    public boolean canPressureToo(LiquidData type, ForgeDirection dir)
    {
            if (type.getColor() == color && dir == ForgeDirection.DOWN.getOpposite()){ return true;}
           
            return false;
    }

    @Override
    public ColorCode getColor()
    {
            return ColorCode.WHITE;
    }

    @Override
    public void setColor(Object obj)
    {
            // leave this blank unless you plan on having it change or be changed
    }
	
}
