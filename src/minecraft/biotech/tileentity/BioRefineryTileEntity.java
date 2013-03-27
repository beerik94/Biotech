package biotech.tileentity;

import hydraulic.core.implement.ColorCode;
import hydraulic.core.implement.IColorCoded;
import hydraulic.core.implement.IPsiReciever;
import hydraulic.core.implement.IReadOut;

import java.util.EnumSet;

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
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;

public class BioRefineryTileEntity extends BasicMachineTileEntity implements IPacketReceiver, IColorCoded, IPsiReciever, IReadOut
{
	// Watts being used per action / idle action
	public static final double	WATTS_PER_TICK		= 500;
	public static final int		MAX_PROCESS_TIME	= 120;
	public int					PROCESS_TIME		= 0;
	
	// Amount of milliBuckets of internal storage
	private ColorCode			color				= ColorCode.WHITE;
	private static final int	milkMaxStored		= 15 * LiquidContainerRegistry.BUCKET_VOLUME;
	private int					milkStored			= 0;
	private int					bucketVol			= LiquidContainerRegistry.BUCKET_VOLUME;
	public double				working				= 0;
	public boolean				bucketIn			= false;
	public int					bucketTimeMax		= 100;
	public int					bucketTime			= 0;
	
	public BioRefineryTileEntity()
	{
		super();
	}
	
	@Override
	public void updateEntity()
	{
		if (!worldObj.isRemote)
		{
			if (this.checkRedstone())
			{
				this.fillFrom(ForgeDirection.DOWN);
				if (this.getMilkStored() >= this.getMaxMilk())
				{
					this.milkStored = this.getMaxMilk();
				}
				if (this.PROCESS_TIME >= this.MAX_PROCESS_TIME)
				{
					this.Refine();
					this.PROCESS_TIME = 0;
				}
				working = (((MAX_PROCESS_TIME - (MAX_PROCESS_TIME - PROCESS_TIME)) / MAX_PROCESS_TIME) * 100);
				PROCESS_TIME++;
				
				if (milkStored <= (milkMaxStored - 30) && inventory[3] != null && inventory[4] == null || milkStored <= (milkMaxStored - 30) && inventory[3] != null && inventory[4].stackSize < 16)
				{
					this.bucketIn = true;
					if (bucketTime >= bucketTimeMax)
					{
						if (inventory[4] == null)
						{
							inventory[4] = (new ItemStack(Item.bucketEmpty, 1, 0));
						}
						else
						{
							inventory[4].stackSize += 1;
						}
						inventory[3] = null;
						milkStored += this.MilkPerBucket;
						bucketTime = 0;
						this.bucketIn = false;
					}
				}
				if (bucketIn && bucketTime < bucketTimeMax)
				{
					bucketTime++;
				}
			}
		}
		super.updateEntity();
	}
	
	/**
	 * Refines milk into Mekanism bioFuel
	 */
	public void Refine()
	{
		if (this.getMilkStored() >= bucketVol)
		{
			if (this.inventory[1] != null && this.inventory[1].stackSize <= 62 || this.inventory[1] == null)
			{
				if (this.inventory[1] == null)
				{
					this.inventory[1] = (Biotech.BioFuel);
					this.inventory[1].stackSize += 1;
				}
				else
				{
					this.inventory[1].stackSize += 2;
				}
				this.setMilkStored(bucketVol, false);
			}
			
			if (this.inventory[2] != null)
			{
				if (this.inventory[2].getItem() == Item.seeds && this.inventory[1].stackSize <= 60 || this.inventory[2].getItem() == Item.seeds && this.inventory[1] == null)
				{
					if (this.inventory[1] == null)
					{
						this.inventory[1] = (Biotech.BioFuel);
						this.inventory[1].stackSize += 1;
					}
					else
					{
						this.inventory[1].stackSize += 4;
					}
					this.setMilkStored(bucketVol, false);
				}
				else if (this.inventory[2].getItem() == Item.wheat && this.inventory[1].stackSize <= 60 || this.inventory[2].getItem() == Item.wheat && this.inventory[1] == null)
				{
					this.inventory[1].stackSize += 4;
					this.setMilkStored(bucketVol, false);
				}
				else if (this.inventory[2].getItem() == Item.appleRed && this.inventory[1].stackSize <= 54 || this.inventory[2].getItem() == Item.appleRed && this.inventory[1] == null)
				{
					this.inventory[1].stackSize += 10;
					this.setMilkStored(bucketVol, false);
					
				}
			}
		}
		
	}
	
	/**
	 * Use this to fill from a pipe or tank connected to the right side
	 */
	public void fillFrom(ForgeDirection dir)
	{
		TileEntity ent = worldObj.getBlockTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
		if (ent instanceof ITankContainer)
		{
			((ITankContainer) ent).drain(dir, bucketVol, true);
			this.setMilkStored(bucketVol, true);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		this.milkStored = tagCompound.getInteger("milkStored");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setInteger("milkStored", this.milkStored);
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
		return PacketManager.getPacket(Biotech.CHANNEL, this, this.milkStored);
	}
	
	public int getMilkStored()
	{
		return this.milkStored;
	}
	
	/**
	 * Sets the current volume of milk stored
	 * 
	 * @param amount
	 *            - volume sum
	 * @param add
	 *            - if true it will add the amount to the current sum
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
		return this.milkMaxStored;
	}
	
	@Override
	public String getMeterReading(EntityPlayer user, ForgeDirection side)
	{
		return "Milk:" + this.milkStored;
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
	
	@Override
	public double getMaxPressure(ForgeDirection side)
	{
		return color.getLiquidData().getPressure();
	}
	
	@Override
	public void onReceivePressure(double pressure)
	{
		// TODO Auto-generated method stub
	}
}
