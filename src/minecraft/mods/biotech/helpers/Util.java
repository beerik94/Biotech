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
}
