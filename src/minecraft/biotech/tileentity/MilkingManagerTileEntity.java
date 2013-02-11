package biotech.tileentity;

import java.util.ArrayList;
import java.util.List;

import liquidmechanics.api.IColorCoded;
import liquidmechanics.api.helpers.ColorCode;
import liquidmechanics.api.liquids.LiquidHandler;
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

public class MilkingManagerTileEntity extends BasicMachineTileEntity implements IInventory, ISidedInventory, IPacketReceiver, IColorCoded
{
	private int tickCounter;
	private int scantickCounter;

	// Watts being used per action / idle action
	public static final double WATTS_PER_TICK = 250;
	public static final double WATTS_PER_IDLE_TICK = 25;

	// Time idle after a tick
	public static final int IDLE_TIME_AFTER_ACTION = 60;
	public static final int IDLE_TIME_NO_ACTION = 30;

	// Watts being used per pump action
	public static final double WATTS_PER_PUMP_ACTION = 50;

	// How much power is stored?
	private double electricityStored = 0;
	private double electricityMaxStored = 5000;

	// How much milk is stored?
	private int milkStored = 0;
	private int milkMaxStored = 7 * LiquidContainerRegistry.BUCKET_VOLUME;
	private int cowMilk = 10;

	private boolean isMilking = false;
	public boolean bucketIn = false;
	public int bucketTimeMax = 100;
	public int bucketTime = 0;

	// Amount of milliBuckets of internal storage
	private ColorCode color = ColorCode.WHITE;

	// Is the machine currently powered, and did it change?
	public boolean prevIsPowered, isPowered = false;

	private int facing;
	private int playersUsing = 0;
	private int idleTicks;

	public int currentX = 0;
	public int currentZ = 0;
	public int currentY = 0;

	public int minX, maxX;
	public int minZ, maxZ;

	public MilkingManagerTileEntity()
	{
		super();
	}

	@Override
	public void updateEntity()
	{
		if (!worldObj.isRemote)
		{
			if (this.isRedstoneSignal())
			{
				this.setPowered(true);
				if (this.getMilkStored() < this.getMaxMilk())
				{
					this.fillFrom();
				}
				scantickCounter++;
			}
			if (milkStored >= 30 && inventory[2] != null && inventory[3] == null)
			{
				this.bucketIn = true;
				if (bucketTime >= bucketTimeMax)
				{
					if (inventory[2].stackSize >= 1)
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
			if (bucketTime < bucketTimeMax)
			{
				bucketTime++;
			}
			if (milkStored >= milkMaxStored)
			{
				milkStored = milkMaxStored;
			}
			if (tickCounter >= 150)
			{
				tickCounter = 0;
			}
			if (scantickCounter >= 100)
			{
				tickCounter = 0;
			}
			if (this.ticks % 3 == 0 && this.playersUsing > 0)
			{
				PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
			}
			this.chargeUp();
		}
		super.updateEntity();
	}
	
	/**
     * Use this to fill from a pipe or tank connected to the bottom side
     */
    public void fillFrom()
    {
            TileEntity ent = worldObj.getBlockTileEntity(xCoord, yCoord-1, xCoord);
            if(ent != null && ent instanceof ITankContainer)
            {
                    ITankContainer tank = (ITankContainer) ent;
                    LiquidStack milk = tank.drain(ForgeDirection.DOWN, (LiquidContainerRegistry.BUCKET_VOLUME / 4), true);
                    if(milk != null)
                    {
                    	this.milkStored += (LiquidContainerRegistry.BUCKET_VOLUME / 4);
                    }
            }
    }

	public boolean isRedstoneSignal()
	{
		if (worldObj.isBlockGettingPowered(xCoord, yCoord, zCoord) || worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) { return true; }
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		// this.progressTime = tagCompound.getShort("Progress");

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
		// tagCompound.setShort("Progress", (short)this.progressTime);

		tagCompound.setShort("facing", (short) this.facing);
		tagCompound.setBoolean("isPowered", this.isPowered);
		tagCompound.setInteger("milkStored", (int) this.milkStored);
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
		return PacketManager.getPacket(Biotech.CHANNEL, this, this.isPowered, this.facing, this.milkStored, this.electricityStored);
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
		return this.milkStored;
	}

	public void setMilkStored(int amount)
	{
		this.milkStored = amount;
	}

	public int getMaxMilk()
	{
		return this.milkMaxStored;
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

	@Override
	public ColorCode getColor()
	{
		return ColorCode.WHITE;
	}

	@Override
	public void setColor(Object obj)
	{
		this.color = ColorCode.WHITE;
	}

}