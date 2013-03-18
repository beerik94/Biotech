package biotech.helpers;

import java.util.LinkedList;

import biotech.inventory.ITransactor;
import biotech.inventory.Transactor;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class Util
{
	public static ItemStack addToRandomInventory(ItemStack stack, World world, int x, int y, int z, ForgeDirection from) {
		LinkedList<ITransactor> possibleInventories = new LinkedList<ITransactor>();

		// Determine inventories which can accept (at least part of) this stack.
		for (ForgeDirection orientation : ForgeDirection.values()) {
			if (from.getOpposite() == orientation) {
				continue;
			}

			Position pos = new Position(x, y, z, orientation);
			pos.moveForwards(1.0);

			TileEntity tileInventory = world.getBlockTileEntity((int) pos.x, (int) pos.y, (int) pos.z);
			ITransactor transactor = Transactor.getTransactorFor(tileInventory);
			if (transactor != null && transactor.add(stack, from, false).stackSize > 0) {
				possibleInventories.add(transactor);
			}
		}

		if (possibleInventories.size() > 0) {
			int choice = world.rand.nextInt(possibleInventories.size());
			return possibleInventories.get(choice).add(stack, from, true);
		}

		ItemStack added = stack.copy();
		added.stackSize = 0;
		return added;

	}
	
	public static ForgeDirection get2dOrientation(Position pos1, Position pos2) {
		double Dx = pos1.x - pos2.x;
		double Dz = pos1.z - pos2.z;
		double angle = Math.atan2(Dz, Dx) / Math.PI * 180 + 180;

		if (angle < 45 || angle > 315)
			return ForgeDirection.EAST;
		else if (angle < 135)
			return ForgeDirection.SOUTH;
		else if (angle < 225)
			return ForgeDirection.WEST;
		else
			return ForgeDirection.NORTH;
	}

	public static ForgeDirection get3dOrientation(Position pos1, Position pos2) {
		double Dx = pos1.x - pos2.x;
		double Dy = pos1.y - pos2.y;
		double angle = Math.atan2(Dy, Dx) / Math.PI * 180 + 180;

		if (angle > 45 && angle < 135)
			return ForgeDirection.UP;
		else if (angle > 225 && angle < 315)
			return ForgeDirection.DOWN;
		else
			return get2dOrientation(pos1, pos2);
	}
	
	public static void dropItems(World world, ItemStack stack, int i, int j, int k) {
		if (stack.stackSize <= 0)
			return;

		float f1 = 0.7F;
		double d = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
		double d1 = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
		double d2 = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
		EntityItem entityitem = new EntityItem(world, i + d, j + d1, k + d2, stack);
		entityitem.delayBeforeCanPickup = 10;

		world.spawnEntityInWorld(entityitem);
	}

	public static void dropItems(World world, IInventory inventory, int i, int j, int k) {
		for (int l = 0; l < inventory.getSizeInventory(); ++l) {
			ItemStack items = inventory.getStackInSlot(l);

			if (items != null && items.stackSize > 0) {
				dropItems(world, inventory.getStackInSlot(l).copy(), i, j, k);
			}
		}
	}

	public static TileEntity getTile(World world, Position pos, ForgeDirection step) {
		Position tmp = new Position(pos);
		tmp.orientation = step;
		tmp.moveForwards(1.0);

		return world.getBlockTileEntity((int) tmp.x, (int) tmp.y, (int) tmp.z);
	}

	/**
	 * Ensures that the given inventory is the full inventory, i.e. takes double chests into account.
	 *
	 * @param inv
	 * @return Modified inventory if double chest, unmodified otherwise.
	 */
	public static IInventory getInventory(IInventory inv) {
		if (inv instanceof TileEntityChest) {
			TileEntityChest chest = (TileEntityChest) inv;
			Position pos = new Position(chest.xCoord, chest.yCoord, chest.zCoord);
			TileEntity tile;
			IInventory chest2 = null;
			tile = Util.getTile(chest.worldObj, pos, ForgeDirection.WEST);
			if (tile instanceof TileEntityChest) {
				chest2 = (IInventory) tile;
			}
			tile = Util.getTile(chest.worldObj, pos, ForgeDirection.EAST);
			if (tile instanceof TileEntityChest) {
				chest2 = (IInventory) tile;
			}
			tile = Util.getTile(chest.worldObj, pos, ForgeDirection.NORTH);
			if (tile instanceof TileEntityChest) {
				chest2 = (IInventory) tile;
			}
			tile = Util.getTile(chest.worldObj, pos, ForgeDirection.SOUTH);
			if (tile instanceof TileEntityChest) {
				chest2 = (IInventory) tile;
			}
			if (chest2 != null)
				return new InventoryLargeChest("", inv, chest2);
		}
		return inv;
	}
}
