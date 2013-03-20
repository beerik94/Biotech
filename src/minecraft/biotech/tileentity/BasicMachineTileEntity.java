package biotech.tileentity;

import java.util.EnumSet;

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
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.block.IConnector;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityRunnable;
import biotech.Biotech;

import com.google.common.io.ByteArrayDataInput;

// Default machine TileEntity
// Has a power connection at the back
// Has a powered state
// Has an inventory

public class BasicMachineTileEntity extends TileEntityElectricityRunnable implements IInventory, ISidedInventory, IPacketReceiver
{
	protected ItemStack[]		inventory;
	
	// this.wattsReceived = Math.max(this.wattsReceived - WATTS_PER_TICK / 4,
	// 0);
	
	// Watts being used per action
	public static final double	WATTS_PER_TICK	= 500;
	
	private int					playersUsing	= 0;
	
	// Is the machine currently powered, and did it change?
	public boolean				prevIsPowered, isPowered = false;
	
	public boolean				hasRedstone		= false;
	
	private int					facing;
	
	public BasicMachineTileEntity()
	{
		super();
		this.RefreshConnections();
		this.inventory = new ItemStack[24];
	}
	
	@Override
	public int getSizeInventory()
	{
		return this.inventory.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int par1)
	{
		return this.inventory[par1];
	}
	
	@Override
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (this.inventory[par1] != null)
		{
			ItemStack var3;
			
			if (this.inventory[par1].stackSize <= par2)
			{
				var3 = this.inventory[par1];
				this.inventory[par1] = null;
				return var3;
			}
			else
			{
				var3 = this.inventory[par1].splitStack(par2);
				
				if (this.inventory[par1].stackSize == 0)
				{
					this.inventory[par1] = null;
				}
				
				return var3;
			}
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (this.inventory[par1] != null)
		{
			ItemStack var2 = this.inventory[par1];
			this.inventory[par1] = null;
			return var2;
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		this.inventory[par1] = par2ItemStack;
		
		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
		{
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
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
		// this.progressTime = tagCompound.getShort("Progress");
		
		this.facing = tagCompound.getShort("facing");
		this.isPowered = tagCompound.getBoolean("isPowered");
		this.hasRedstone = tagCompound.getBoolean("hasRedstone");
		
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
		tagCompound.setBoolean("hasRedstone", this.hasRedstone);
		
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
		return "Basic Machine";
	}
	
	public void setPowered(boolean powered)
	{
		this.isPowered = powered;
		
		if (prevIsPowered != powered)
		{
			prevIsPowered = isPowered;
			PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj);
		}
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		this.chargeUp();
		if (!worldObj.isRemote)
		{
			if (this.ticks % 3 == 0 && this.playersUsing > 0)
			{
				PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
			}
		}
	}
	
	public boolean checkRedstone()
	{
		if (worldObj.isBlockIndirectlyProvidingPowerTo(xCoord, yCoord, zCoord, facing) != 0 || worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void RefreshConnections()
	{
		int front = 0;
		switch (this.getFacing())
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
	}
	
	/**
	 * Charges up the tileEntities energy storage
	 */
	public void chargeUp()
	{
		/**
		 * Attempts to charge using batteries.
		 */
		this.wattsReceived += ElectricItemHelper.dechargeItem(this.inventory[0], WATTS_PER_TICK, this.getVoltage());
		
	}
	
	@Override
	public ElectricityPack getRequest()
	{
		if (this.wattsReceived <= WATTS_PER_TICK)
		{
			return new ElectricityPack(WATTS_PER_TICK / this.getVoltage(), this.getVoltage());
		}
		else
		{
			return new ElectricityPack();
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
				int newSize = copy.stackSize; // Math.min(itemStack.stackSize,
												// slot.getMaxStackSize());
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
	
	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		switch(this.getFacing())
		{
			case 2:
				return direction == ForgeDirection.getOrientation(3);
			case 3:
				return direction == ForgeDirection.getOrientation(2);
			case 4:
				return direction == ForgeDirection.getOrientation(5);
			case 5:
				return direction == ForgeDirection.getOrientation(4);
			default:
				return direction == ForgeDirection.getOrientation(3);
		}
	}
	
	@Override
	public boolean func_94042_c()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean func_94041_b(int i, ItemStack itemstack)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
