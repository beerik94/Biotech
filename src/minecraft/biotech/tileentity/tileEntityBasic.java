package biotech.tileentity;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.tile.IWrenchable;
import mekanism.api.EnergizedItemManager;
import mekanism.api.IStrictEnergyAcceptor;
import mekanism.api.IStrictEnergyStorage;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.prefab.tile.TileEntityElectricityStorage;
import biotech.Biotech;
import biotech.handlers.PacketHandler;
import biotech.helpers.IPacketReceiver;
import biotech.helpers.LinkedPowerProvider;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;

import com.google.common.io.ByteArrayDataInput;

public class tileEntityBasic extends TileEntityElectricityStorage implements IPacketReceiver, IWrenchable, IPowerReceptor, IEnergyTile, IStrictEnergyStorage, IStrictEnergyAcceptor
{
	/** BuildCraft power provider. */
	public IPowerProvider	powerProvider;
	
	/** How much energy is stored in this block. */
	public double			electricityStored;
	
	/** Maximum amount of energy this machine can hold. */
	public double			MaxElectricity	= 2000;
	
	protected long			ticks			= 0;
	protected ItemStack[]	inventory;
	public boolean			prevIsPowered, isPowered = false;
	public short			facing;
	
	/**
	 * Whether or not this machine has initialized and registered with other
	 * mods.
	 */
	public boolean			initialized;
	
	public tileEntityBasic()
	{
		if (PowerFramework.currentFramework != null)
		{
			powerProvider = new LinkedPowerProvider(this);
			powerProvider.configure(0, 0, 100, 0, (int) (this.getMaxEnergy() * Biotech.TO_BC));
		}
	}
	
	@Override
	public void updateEntity()
	{
		if (this.ticks == 0)
		{
			this.initiate();
		}
		
		if (this.ticks >= Long.MAX_VALUE)
		{
			this.ticks = 1;
		}
		
		this.ticks++;
		
		if (!initialized && worldObj != null)
		{
			if (Biotech.IC2Loaded)
			{
				MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			}
			
			initialized = true;
		}
		
		if (!worldObj.isRemote)
		{
			if(getEnergy() < getMaxEnergy())
			{
				ElectricityPack electricityPack = ElectricityNetworkHelper.consumeFromMultipleSides(this, getRequest());
				setJoules(getJoules() + electricityPack.getWatts());
				setEnergy(electricityStored + EnergizedItemManager.discharge(this.inventory[0], getMaxEnergy()-getEnergy()));
			}
		}
	}
	
	/**
	 * Called on the TileEntity's first tick.
	 */
	public void initiate()
	{
	}
	
	public short getFacing()
	{
		return facing;
	}
	
	public void setFacing(short direction)
	{
		this.facing = direction;
	}
	
	@Override
	public int getBlockMetadata()
	{
		if (this.blockMetadata == -1)
		{
			this.blockMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
		}
		
		return this.blockMetadata;
	}
	
	@Override
	public Block getBlockType()
	{
		if (this.blockType == null)
		{
			this.blockType = Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
		}
		
		return this.blockType;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		// this.progressTime = tagCompound.getShort("Progress");
		
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
		// tagCompound.setShort("Progress", (short)this.progressTime);
		
		tagCompound.setShort("facing", this.facing);
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
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			if (this.worldObj.isRemote)
			{
				this.isPowered = dataStream.readBoolean();
				this.facing = dataStream.readShort();
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
		return PacketHandler.getPacket(Biotech.CHANNEL, this, this.isPowered, this.facing, this.electricityStored);
	}
	
	public boolean checkRedstone()
	{
		if (worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) != 0 || worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void setPowered(boolean powered)
	{
		this.isPowered = powered;
		
		if (prevIsPowered != powered)
		{
			prevIsPowered = isPowered;
			PacketHandler.sendPacketToClients(getDescriptionPacket(), this.worldObj);
		}
	}
	
	/**
	 * Mekanism
	 */
	
	@Override
	public double getEnergy()
	{
		return this.electricityStored;
	}
	
	@Override
	public void setEnergy(double energy)
	{
		electricityStored = Math.max(Math.min(energy, getMaxEnergy()), 0);
	}
	
	@Override
	public double getMaxEnergy()
	{
		return this.MaxElectricity;
	}
	
	@Override
	public double transferEnergyToAcceptor(double amount)
	{
		return 0;
	}
	
	@Override
	public boolean canReceiveEnergy(ForgeDirection side)
	{
		switch (this.getFacing())
		{
			case 2:
				return side == ForgeDirection.getOrientation(3);
			case 3:
				return side == ForgeDirection.getOrientation(2);
			case 4:
				return side == ForgeDirection.getOrientation(5);
			case 5:
				return side == ForgeDirection.getOrientation(4);
			default:
				return side == ForgeDirection.getOrientation(3);
		}
	}
	
	/**
	 * Universal Electricity
	 */
	
	public double getMaxJoules()
	{
		return getMaxEnergy();
	}
	
	public double getJoules()
	{
		return getEnergy();
	}
	
	public void setJoules(double joules)
	{
		setEnergy(joules);
	}
	
	@Override
	public double getVoltage()
	{
		return 120;
	}
	
	@Override
	public boolean canConnect(ForgeDirection side)
	{
		switch (this.getFacing())
		{
			case 2:
				return side == ForgeDirection.getOrientation(3);
			case 3:
				return side == ForgeDirection.getOrientation(2);
			case 4:
				return side == ForgeDirection.getOrientation(5);
			case 5:
				return side == ForgeDirection.getOrientation(4);
			default:
				return side == ForgeDirection.getOrientation(3);
		}
	}
	
	/**
	 * Returns the amount of energy being requested this tick. Return an empty
	 * ElectricityPack if no
	 * electricity is desired.
	 */
	public ElectricityPack getRequest()
	{
		if (this.getEnergy() < this.getMaxEnergy())
		{
			return new ElectricityPack(this.getMaxEnergy() / this.getVoltage(), this.getVoltage());
		}
		else
		{
			return new ElectricityPack();
		}
	}
	
	/**
	 * Called right after electricity is transmitted to the TileEntity. Override
	 * this if you wish to
	 * have another effect for a voltage overcharge.
	 * 
	 * @param electricityPack
	 */
	public void onReceive(ElectricityPack electricityPack)
	{
		/**
		 * Creates an explosion if the voltage is too high.
		 */
		if (UniversalElectricity.isVoltageSensitive)
		{
			if (electricityPack.voltage > this.getVoltage())
			{
				this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, 1.5f, true);
				return;
			}
		}
	}
	
	/**
	 * Buildcraft
	 */
	
	@Override
	public void setPowerProvider(IPowerProvider provider)
	{
	}
	
	@Override
	public IPowerProvider getPowerProvider()
	{
		return powerProvider;
	}
	
	@Override
	public int powerRequest(ForgeDirection from)
	{
		return (int) Math.min(((MaxElectricity - electricityStored) * Biotech.TO_BC), 100);
	}
	
	/**
	 * Industrial Craft 2
	 */
	
	@Override
	public void invalidate()
	{
		ElectricityNetworkHelper.invalidate(this);
		
		if (initialized)
		{
			if (Biotech.IC2Loaded)
			{
				MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			}
		}
		super.invalidate();
	}
	
	@Override
	public boolean isAddedToEnergyNet()
	{
		return initialized;
	}
	
	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
	{
		return true;
	}
	
	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer)
	{
		return false;
	}
	
	@Override
	public float getWrenchDropRate()
	{
		return 1;
	}
	
	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		return null;
	}

	@Override
	public void doWork()
	{
	}
	
}
