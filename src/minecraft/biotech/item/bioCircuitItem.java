package biotech.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import biotech.Biotech;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class bioCircuitItem extends Item {
	public final static String[] subNames = { "unprogrammed",

			// Tiller
			"wheatseeds", "melonseeds", "pumpkinseeds", "carrots", "potatoes",
			"rangeupgrade", };

	public bioCircuitItem(int id) {
		super(id);
		// Constructor Configuration
		setCreativeTab(Biotech.tabBiotech);
		setIconIndex(0);
		setUnlocalizedName("bioCircuit");
		setHasSubtypes(true);
	}

	@SideOnly(Side.CLIENT)
	public int getIconFromDamage(int par1) {
		switch (par1) {
		case 0:
			return 0; // unprogrammed
		case 1:
			return 1; // wheatseeds
		case 2:
			return 2; // melonseeds
		case 3:
			return 3; // pumpkinseeds
		case 4:
			return 4; // carrots
		case 5:
			return 5; // potatoes
		case 6:
			return 8; // Range Upgrade

		}
		return this.iconIndex;
	}

	@Override
	public int getMetadata(int damageValue) {
		return damageValue;
	}

	public String getTextureFile() {
		return Biotech.ITEM_TEXTURE_FILE;
	}

	@Override
	public String getItemNameIS(ItemStack stack) {
		int sub = stack.getItemDamage();

		if (sub >= subNames.length) {
			sub = 0;
		}

		return getItemName() + "." + subNames[sub];
	}

	public ItemStack getStack(int count, int damageValue) {
		ItemStack stack = new ItemStack(this, count);
		stack.setItemDamage(damageValue);
		if(damageValue == 6)
		{
			this.setMaxStackSize(4);
		}
		else
		{
			this.setMaxStackSize(1);
		}
		return stack;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(int unknown, CreativeTabs tab, List subItems) {
		for (int meta = 1; meta < subNames.length; meta++) {
			subItems.add(new ItemStack(this, 1, meta));
		}
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
