package biotech.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import biotech.Biotech;

public class rangeUpgradeItem extends Item {

	public rangeUpgradeItem(int id, int textureIndex) {
		super(id);
		setMaxStackSize(4);
		setCreativeTab(Biotech.tabBiotech);
		setIconIndex(0);
		setItemName("rangeUpgrade");
		setHasSubtypes(false);
	}

	public String getTextureFile() {
		return Biotech.ITEM_TEXTURE_FILE;
	}

	/**
	 * Spawns EntityItem in the world for the given ItemStack if the world is
	 * not remote.
	 */
	protected void dropStackAsItem(World world, int x, int y, int z,
			ItemStack stack) {
		if (!world.isRemote
				&& world.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
			float range = 0.7F;
			double dx = (double) (world.rand.nextFloat() * range)
					+ (double) (1.0F - range) * 0.5D;
			double dy = (double) (world.rand.nextFloat() * range)
					+ (double) (1.0F - range) * 0.5D;
			double dz = (double) (world.rand.nextFloat() * range)
					+ (double) (1.0F - range) * 0.5D;
			EntityItem entity = new EntityItem(world, (double) x + dx,
					(double) y + dy, (double) z + dz, stack);
			entity.delayBeforeCanPickup = 10;
			world.spawnEntityInWorld(entity);
		}
	}

}
