package biotech.tileentity;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import universalelectricity.prefab.network.IPacketReceiver;

// Default machine TileEntity
// Has a power connection at the back
// Has a powered state
// Has an inventory

public class TillingMachineTileEntity extends BasicMachineTileEntity implements
		IInventory, ISidedInventory, IPacketReceiver {
	public static final double WATTS_PER_ACTION = 200;
	public static final double WATTS_PER_IDLE_ACTION = 25;

	// Time idle after a tick
	public static final int IDLE_TIME_AFTER_ACTION = 60;
	public static final int IDLE_TIME_NO_ACTION = 30;

	public int currentX = 0;
	public int currentZ = 0;
	public int currentY = 0;

	public int minX, maxX;
	public int minZ, maxZ;

	private Block tilledField = Block.tilledField;

	// TODO Add variables to indicate maximum workarea size. Should be based on
	// CommandItem usage?

	public static final ItemStack[] hoeToolStacks = new ItemStack[] {
			new ItemStack(Item.hoeWood, 1), new ItemStack(Item.hoeStone, 1),
			new ItemStack(Item.hoeSteel, 1), new ItemStack(Item.hoeGold, 1),
			new ItemStack(Item.hoeDiamond, 1) };

	private int idleTicks;

	public TillingMachineTileEntity() {
		super();
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		// Biotech.biotechLogger.info("UpdateEntity");

		if (this.worldObj.isRemote) {
			return;
		}

		if (this.idleTicks > 0) {
			if (this.ticks % 40 == 0)
				this.setElectricityStored(this.getElectricityStored()
						- this.WATTS_PER_IDLE_ACTION);

			--this.idleTicks;
			return;
		}

		while (canDoWork()) {
			this.setPowered(true);

			if (doWork()) {
				this.setElectricityStored(this.getElectricityStored()
						- this.WATTS_PER_ACTION);
				this.idleTicks = this.IDLE_TIME_AFTER_ACTION;
				advanceLocation();
				this.setPowered(false);
				break;
			} else {
				this.idleTicks = this.IDLE_TIME_NO_ACTION;
				advanceLocation();
				break;
			}
		}

		return;
	}

	public int getTopY() {/*
						 * for (int y = 0; y < 4; y++) { if
						 * (this.worldObj.getBlockMaterial(this.xCoord +
						 * this.currentX, this.yCoord + y, this.zCoord +
						 * this.currentZ) == Material.air) { return y - 1; } }
						 * 
						 * return -1;
						 */
		return this.yCoord - 1;
	}

	public boolean canTill() {
		int currentBlockBlockid = worldObj.getBlockId(xCoord + currentX,
				getTopY(), zCoord + currentZ);

		if ((currentBlockBlockid == Block.dirt.blockID || currentBlockBlockid == Block.grass.blockID)
				&& worldObj.isAirBlock(xCoord + currentX, getTopY() + 1, zCoord
						+ currentZ)
				&& worldObj.isAirBlock(xCoord + currentX, getTopY() + 1, zCoord
						+ currentZ)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean tillField() {
		int currentBlockBlockid = worldObj.getBlockId(xCoord + currentX,
				getTopY(), zCoord + currentZ);

		if (!worldObj.isRemote && canTill()) {
			worldObj.setBlock(xCoord + currentX, getTopY(), zCoord + currentZ,
					tilledField.blockID);

			damageTool(hoeToolStacks);

			return true;
		} else {
			return false;
		}
	}

	public boolean doWork() {
		if (canTill() && hasToolInSlot()) {
			return tillField();
		}

		return false;
	}

	public void damageTool(ItemStack... stacks) {
		for (int i = 0; i < stacks.length; i++) {
			ItemStack slot = getStackInSlot(1);

			if (slot == null) {
				continue;
			}

			if (slot.itemID == stacks[i].itemID) {
				if (slot.getItemDamage() >= slot.getMaxDamage()) {
					decrStackSize(1, 1);
				} else {
					slot.setItemDamage(slot.getItemDamage() + 10);
				}
			}
		}
	}

	public boolean canDoWork() {
		ItemStack circuit = this.inventory[2];
		ItemStack tools = this.inventory[1];

		if (this.getElectricityStored() >= this.WATTS_PER_ACTION) {
			if (hasToolInSlot()) {
				return true;
			}
		}

		return false;
	}

	private boolean hasToolInSlot() {
		ItemStack slot = this.inventory[1];

		if (slot == null) {
			return false;
		}

		for (int i = 0; i < hoeToolStacks.length; i++) {
			if (slot.itemID == hoeToolStacks[i].itemID) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void refreshConnectorsAndWorkArea() {
		super.refreshConnectorsAndWorkArea();

		ForgeDirection direction = ForgeDirection.getOrientation(getFacing());

		if (direction.offsetZ > 0) {
			this.minX = -2;
			this.maxX = 2;
			this.minZ = -5 * direction.offsetZ;
			this.maxZ = -1 * direction.offsetZ;
		} else if (direction.offsetZ < 0) {
			this.minX = -2;
			this.maxX = 2;
			this.minZ = -1 * direction.offsetZ;
			this.maxZ = -5 * direction.offsetZ;
		} else if (direction.offsetX > 0) {
			this.minZ = -2;
			this.maxZ = 2;
			this.minX = -5 * direction.offsetX;
			this.maxX = -1 * direction.offsetX;
		} else if (direction.offsetX < 0) {
			this.minZ = -2;
			this.maxZ = 2;
			this.minX = -1 * direction.offsetX;
			this.maxX = -5 * direction.offsetX;
		}

		if (this.currentX < this.minX || this.currentX > this.maxX) {
			this.currentX = this.minX;
		}

		if (this.currentZ < this.minZ || this.currentZ > this.maxZ) {
			this.currentZ = this.minZ;
		}
	}

	private void advanceLocation() {
		this.currentX++;

		if (this.currentX > this.maxX) {
			this.currentX = this.minX;
			this.currentZ++;

			if (this.currentZ > this.maxZ) {
				this.currentZ = this.minZ;
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		// this.progressTime = tagCompound.getShort("Progress");

	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		// tagCompound.setShort("Progress", (short)this.progressTime);

	}

	@Override
	public String getInvName() {
		return "TillingMachine";
	}

}
