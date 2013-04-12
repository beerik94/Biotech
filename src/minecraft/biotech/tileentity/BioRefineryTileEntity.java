package biotech.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import biotech.Biotech;

import com.google.common.io.ByteArrayDataInput;

public class BioRefineryTileEntity extends BasicMachineTileEntity implements IPacketReceiver
{
	// Watts being used per action / idle action
	public static final double	WATTS_PER_TICK			= 500;
	public static final int		MAX_PROCESS_TIME		= 120;
	public int					PROCESS_TIME			= 0;
	
	// Amount of milliBuckets of internal storage
	//private ColorCode			color					= ColorCode.WHITE;
	private static final int	milkMaxStored			= 15 * LiquidContainerRegistry.BUCKET_VOLUME;
	private int					milkStored				= 0;
	private int					bucketVol				= LiquidContainerRegistry.BUCKET_VOLUME;
	public double				working					= 0;
	public static final int		PROCESS_TIME_REQUIRED	= 60;
	public int					processTicks			= 0;
	public boolean				fuelPressed				= false;
	public boolean				cheesePressed			= true;
	public String				buttonText				= "Fuel";
	
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
					if (this.processTicks == 0)
					{
						this.processTicks = this.PROCESS_TIME_REQUIRED;
					}
					else if (this.processTicks > 0)
					{
						this.processTicks--;
						
						/**
						 * Process the item when the process timer is done.
						 */
						if (this.processTicks < 1)
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
							this.processTicks = 0;
						}
					}
					else
					{
						this.processTicks = 0;
					}
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
		ItemStack biocheese = new ItemStack(Biotech.bioCheese, 1, 0);
		
		if (this.getMilkStored() >= bucketVol)
		{
			if (Biotech.mekanismEnabled && !fuelPressed && cheesePressed)
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
							this.inventory[1].stackSize += 3;
						}
						else
						{
							this.inventory[1].stackSize += 4;
						}
						this.setMilkStored(bucketVol, false);
						if (this.inventory[2].stackSize > 1)
						{
							this.inventory[2].stackSize -= 1;
						}
						else
						{
							this.inventory[2] = null;
						}
					}
					else if (this.inventory[2].getItem() == Item.wheat && this.inventory[1].stackSize <= 60 || this.inventory[2].getItem() == Item.wheat && this.inventory[1] == null)
					{
						if (this.inventory[1] == null)
						{
							this.inventory[1] = (Biotech.BioFuel);
							this.inventory[1].stackSize += 3;
						}
						else
						{
							this.inventory[1].stackSize += 4;
						}
						this.setMilkStored(bucketVol, false);
						if (this.inventory[2].stackSize > 1)
						{
							this.inventory[2].stackSize -= 1;
						}
						else
						{
							this.inventory[2] = null;
						}
					}
					else if (this.inventory[2].getItem() == Item.appleRed && this.inventory[1].stackSize <= 54 || this.inventory[2].getItem() == Item.appleRed && this.inventory[1] == null)
					{
						if (this.inventory[1] == null)
						{
							this.inventory[1] = (Biotech.BioFuel);
							this.inventory[1].stackSize += 9;
						}
						else
						{
							this.inventory[1].stackSize += 10;
						}
						this.setMilkStored(bucketVol, false);
						if (this.inventory[2].stackSize > 1)
						{
							this.inventory[2].stackSize -= 1;
						}
						else
						{
							this.inventory[2] = null;
						}
						
					}
				}
			}
			else if (fuelPressed && !cheesePressed)
			{
				if (this.inventory[1] != null && this.inventory[1].stackSize <= 62 || this.inventory[1] == null)
				{
					if (this.inventory[1] == null)
					{
						this.inventory[1] = (biocheese);
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
							this.inventory[1] = (biocheese);
							this.inventory[1].stackSize += 3;
						}
						else
						{
							this.inventory[1].stackSize += 4;
						}
						this.setMilkStored(bucketVol, false);
						if (this.inventory[2].stackSize > 1)
						{
							this.inventory[2].stackSize -= 1;
						}
						else
						{
							this.inventory[2] = null;
						}
					}
					else if (this.inventory[2].getItem() == Item.wheat && this.inventory[1].stackSize <= 60 || this.inventory[2].getItem() == Item.wheat && this.inventory[1] == null)
					{
						if (this.inventory[1] == null)
						{
							this.inventory[1] = (biocheese);
							this.inventory[1].stackSize += 3;
						}
						else
						{
							this.inventory[1].stackSize += 4;
						}
						this.setMilkStored(bucketVol, false);
						if (this.inventory[2].stackSize > 1)
						{
							this.inventory[2].stackSize -= 1;
						}
						else
						{
							this.inventory[2] = null;
						}
					}
					else if (this.inventory[2].getItem() == Item.appleRed && this.inventory[1].stackSize <= 54 || this.inventory[2].getItem() == Item.appleRed && this.inventory[1] == null)
					{
						if (this.inventory[1] == null)
						{
							this.inventory[1] = (biocheese);
							this.inventory[1].stackSize += 9;
						}
						else
						{
							this.inventory[1].stackSize += 10;
						}
						this.setMilkStored(bucketVol, false);
						if (this.inventory[2].stackSize > 1)
						{
							this.inventory[2].stackSize -= 1;
						}
						else
						{
							this.inventory[2] = null;
						}
						
					}
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
		this.processTicks = tagCompound.getInteger("processTicks");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setInteger("milkStored", this.milkStored);
		tagCompound.setInteger("processTicks", this.processTicks);
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
				this.processTicks = dataStream.readInt();
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
		return PacketManager.getPacket(Biotech.CHANNEL, this, this.milkStored, this.processTicks, this.facing);
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
	
	/*
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
	*/
}
