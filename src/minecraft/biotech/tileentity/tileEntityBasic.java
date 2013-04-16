package biotech.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.block.IConnector;
import universalelectricity.core.block.IVoltage;
import universalelectricity.prefab.tile.TileEntityDisableable;
import biotech.Biotech;
import biotech.PacketHandler;
import biotech.helpers.IPacketReceiver;

import com.google.common.io.ByteArrayDataInput;

public class tileEntityBasic extends TileEntityDisableable implements IPacketReceiver, IConnector, IVoltage
{
	protected ItemStack[]	inventory;
	public boolean			prevIsPowered, isPowered = false;
	public int				facing;
	
	public int getFacing()
	{
		return facing;
	}
	
	public void setFacing(int direction)
	{
		this.facing = direction;
	}
	
	@Override
	public double getVoltage()
	{
		return 120;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		// this.progressTime = tagCompound.getShort("Progress");
		
		this.facing = tagCompound.getShort("facing");
		this.isPowered = tagCompound.getBoolean("isPowered");
		
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
	public boolean canConnect(ForgeDirection direction)
	{
		switch (this.getFacing())
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
		return PacketHandler.getPacket(Biotech.CHANNEL, this, this.isPowered, this.facing);
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
}
