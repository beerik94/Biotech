package biotech.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;

import biotech.Biotech;

import com.google.common.io.ByteArrayDataInput;

public class CuttingMachineTileEntity extends BasicMachineTileEntity implements
		IPacketReceiver {

	// Watts being used per action / idle action
	public static final double WATTS_PER_SEARCH = 200;
	public static final double WATTS_PER_CUT = 700;

	private int facing;
	private int playersUsing = 0;
	private int idleTicks;

	public int currentX = 0;
	public int currentZ = 0;
	public int currentY = 0;

	public int minX, maxX;
	public int minZ, maxZ;

	public CuttingMachineTileEntity() {
		super();
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (!worldObj.isRemote && this.hasRedstone) {
			/* Per Tick Processes */
			
			if (this.ticks % 40 == 0
					&& this.wattsReceived >= WATTS_PER_SEARCH) {
				GetTree();
				this.wattsReceived = Math.max(this.wattsReceived - WATTS_PER_SEARCH / 4, 0);
				RemoveLeaves();
			}
			/* Update Client */
			if (this.playersUsing > 0 && this.ticks % 3 == 0) {
				PacketManager.sendPacketToClients(getDescriptionPacket(),
						this.worldObj, new Vector3(this), 12);
			}
		}
		this.chargeUp();
	}

	// TODO Maybe add this feature in the future
	// For now only the tree that is 2 blocks(and all the other blocks till the
	// tree ends) above the woodcutter gets detected
	/**
	 * Check for Trees
	 */
	public void GetTree() {
		/*
		 * int XPos = this.xCoord; int ZPos = this.zCoord; switch
		 * (this.getFacing()) { case 2: // North for (int i = 1; i < GetRange();
		 * i++) { ZPos = this.zCoord + i; } break; case 3: // South for (int i =
		 * 1; i < GetRange(); i++) { ZPos = this.zCoord - i; } break; case 4: //
		 * West for (int i = 1; i < GetRange(); i++) { XPos = this.xCoord + i; }
		 * break; case 5: // East for (int i = 1; i < GetRange(); i++) { XPos =
		 * this.xCoord - i; } break; } int bottomBlock =
		 * worldObj.getBlockId(XPos, this.yCoord, ZPos); int YPos = this.yCoord
		 * + 1; while (bottomBlock == Block.wood.blockID) { int otherBlock =
		 * worldObj.getBlockId(XPos, YPos, ZPos); if (otherBlock ==
		 * Block.wood.blockID) { YPos += 1; } else { DoCut(XPos, YPos - 1,
		 * ZPos); return; } }
		 */
		int i = 2;
		while (worldObj.getBlockId(this.xCoord, this.yCoord + i, this.zCoord) == Block.wood.blockID) {
			i++;
		}
		if (worldObj.getBlockId(this.xCoord, this.yCoord + i, this.zCoord) != Block.wood.blockID
				&& this.wattsReceived >= WATTS_PER_CUT) {
			for (int x = 2; x < i; x++) {
				DoCut(this.xCoord, this.yCoord + x, this.zCoord, true);
			}
			Replant();
		}
	}

	/**
	 * The cutting function
	 * 
	 * @param x
	 *            The X position of the block
	 * @param y
	 *            The Y position of the block
	 * @param z
	 *            The Z position of the block
	 * @param wood 
	 *            True for wood / False for leaves
	 */
	public void DoCut(int x, int y, int z, boolean wood) {
		worldObj.setBlockAndMetadataWithNotify(x, y, z, 0, 0, 2);
		this.wattsReceived = Math.max(this.wattsReceived - WATTS_PER_CUT / 4, 0);
	}

	/**
	 * The replanting of saplings
	 */
	public void Replant() {
		if(worldObj.getBlockId(this.xCoord, this.yCoord + 2, this.zCoord) == 0)
		{
			worldObj.setBlockAndMetadataWithNotify(this.xCoord, this.yCoord + 2, this.zCoord, Block.sapling.blockID, 0, 2);
		}
	}

	/**
	 * Removes leaves that are left behind.
	 */
	public void RemoveLeaves() {
		int xminrange = xCoord - 5;
		int xmaxrange = xCoord + 5;
		int yminrange = yCoord + 2;
		int ymaxrange = yCoord + 20;
		int zminrange = zCoord - 5;
		int zmaxrange = zCoord + 5;

		for (int xx = xminrange; xx <= xmaxrange; xx++)
		{
			for (int yy = yminrange; yy <= ymaxrange; yy++)
			{
				for (int zz = zminrange; zz <= zmaxrange; zz++)
				{
					if(worldObj.getBlockId(xx, yy, zz) == Block.leaves.blockID)
					{
						DoCut(xx, yy, zz, false);
					}									
				}
			}
		}
	}

	/**
	 * Calculates the range
	 */
	public int GetRange() {
		if (getStackInSlot(1) != null) {
			if (inventory[1].isItemEqual(Biotech.bioCircuitRangeUpgrade)) {
				return (getStackInSlot(1).stackSize * 2 + 2);
			}
		}
		return 2;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		// this.progressTime = tagCompound.getShort("Progress");

		this.facing = tagCompound.getShort("facing");
		this.hasRedstone = tagCompound.getBoolean("hasRedstone");
		NBTTagList tagList = tagCompound.getTagList("Inventory");

		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
			byte slot = tag.getByte("Slot");

			if (slot >= 0 && slot < inventory.length) {
				inventory[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		// tagCompound.setShort("Progress", (short)this.progressTime);

		tagCompound.setShort("facing", (short) this.facing);
		tagCompound.setBoolean("hasRedstone", this.hasRedstone);
		NBTTagList itemList = new NBTTagList();

		for (int i = 0; i < inventory.length; i++) {
			ItemStack stack = inventory[i];

			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		tagCompound.setTag("Inventory", itemList);
	}

	@Override
	public String getInvName() {
		return "Wood Cutter";
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType,
			Packet250CustomPayload packet, EntityPlayer player,
			ByteArrayDataInput dataStream) {
		try {
			if (this.worldObj.isRemote) {
				this.facing = dataStream.readInt();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		return PacketManager.getPacket(Biotech.CHANNEL, this, this.facing);
	}

	public int getFacing() {
		return facing;
	}

	public void setFacing(int facing) {
		this.facing = facing;
	}
}