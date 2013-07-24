package biotech.tileentity;

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
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import universalelectricity.compatibility.TileEntityUniversalElectrical;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import biotech.Biotech;
import biotech.handlers.PacketHandler;

import com.google.common.io.ByteArrayDataInput;

public class tileEntityBasicMachine extends TileEntityUniversalElectrical implements IInventory, ISidedInventory, IPacketReceiver
{
	/** How much energy is stored in this block. */
	public float			energyStored;
	
	/** Maximum amount of energy this machine can hold. */
	public float			maxEnergyStored	= 2000;
	
	protected ItemStack[]	inventory;
	public boolean			prevIsPowered, isPowered = false;
	
	/** Is the machine currently powered, and did it change? */
	public static final int	MilkPerBucket	= 100;
	private int				playersUsing	= 0;
	public short			facing;
	
	public tileEntityBasicMachine()
	{
		super();
		this.inventory = new ItemStack[24];
	}
	
	@Override
	public void updateEntity()
	{
		if (!worldObj.isRemote)
		{
			if (this.ticks % 3 == 0 && this.playersUsing > 0)
			{
				PacketHandler.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this.xCoord, this.yCoord, this.zCoord), 12);
			}
		}
	}
	
	@Override
	public float getRequest(ForgeDirection direction)
	{
		return Math.max(this.getMaxEnergyStored() - this.getEnergyStored(), 0);
	}
	
	@Override
	public float getProvide(ForgeDirection direction)
	{
		return 0;
	}
	
	@Override
	public float getMaxEnergyStored()
	{
		return this.maxEnergyStored;
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
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.energyStored = nbt.getFloat("energyStored");
		this.facing = nbt.getShort("facing");
		
		NBTTagList tagList = nbt.getTagList("Inventory");
		
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
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setFloat("energyStored", this.energyStored);
		nbt.setShort("facing", this.facing);
		
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
		
		nbt.setTag("Inventory", itemList);
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
				this.energyStored = dataStream.readFloat();
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
		return PacketHandler.getPacket(Biotech.CHANNEL, this, this.isPowered, this.facing, this.energyStored);
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
	
	/** Inventory Stuff */
	
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
			PacketHandler.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this.xCoord, this.yCoord, this.zCoord), 15);
		}
		this.playersUsing++;
	}
	
	@Override
	public void closeChest()
	{
		this.playersUsing--;
	}

	@Override
	public String getInvName()
	{
		return "Basic Machine";
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return false;
	}
}
