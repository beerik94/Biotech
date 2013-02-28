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
	public static final double WATTS_PER_TICK = 25;

	// How much power is stored?
	private double electricityStored = 0;
	private double electricityMaxStored = 5000;
	
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

	public CuttingMachineTileEntity() {
		super();
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (!worldObj.isRemote && this.HasRedstoneSignal()) {
			/* Per Tick Processes */
			this.setPowered(true);
			this.chargeUp();
			if (this.ticks % 40 == 0) {
				GetTree();
			}
			System.out.println("Facing: " + this.getFacing());
			/* Update Client */
			if (this.playersUsing > 0 && this.ticks % 3 == 0) {
				PacketManager.sendPacketToClients(getDescriptionPacket(),
						this.worldObj, new Vector3(this), 12);
			}
		}
	}

	/**
	 * Gets if this block is getting powered by redstone
	 */
	public boolean HasRedstoneSignal() {
		if (worldObj.isBlockGettingPowered(xCoord, yCoord, zCoord)
				|| worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord,
						zCoord)) {
			return true;
		}
		return false;
	}

	/**
	 * Check for Trees
	 */
	public void GetTree() {
		int XPos = this.xCoord;
		int ZPos = this.zCoord;
		switch (this.getFacing()) {
		case 2: // North
			for (int i = 1; i < GetRange(); i++) {
				ZPos = this.zCoord + i;
			}
			break;
		case 3: // South
			for (int i = 1; i < GetRange(); i++) {
				ZPos = this.zCoord - i;
			}
			break;
		case 4: // West
			for (int i = 1; i < GetRange(); i++) {
				XPos = this.xCoord + i;
			}
			break;
		case 5: // East
			for (int i = 1; i < GetRange(); i++) {
				XPos = this.xCoord - i;
			}
			break;
		}
		int bottomBlock = worldObj.getBlockId(XPos, this.yCoord, ZPos);
		int YPos = this.yCoord + 1;
		while (bottomBlock == Block.wood.blockID) {
			int otherBlock = worldObj.getBlockId(XPos, YPos, ZPos);
			if (otherBlock == Block.wood.blockID) {
				YPos += 1;
			} else {
				DoCut(XPos, YPos - 1, ZPos);
				return;
			}
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
	 */
	public void DoCut(int x, int y, int z) {
		worldObj.setBlockWithNotify(x, y, z, Block.wood.blockID);
	}

	/**
	 * The replanting of saplings
	 */
	public void Replant() {

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
		this.electricityStored = tagCompound.getDouble("electricityStored");
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
		tagCompound.setDouble("electricityStored", this.electricityStored);
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
				this.electricityStored = dataStream.readDouble();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		return PacketManager.getPacket(Biotech.CHANNEL, this, this.facing,
				this.electricityStored);
	}
}