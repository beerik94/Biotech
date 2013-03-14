package biotech.item;

import java.util.ArrayList;
import java.util.List;

import universalelectricity.prefab.modifier.IModifier;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import biotech.Biotech;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class bioCircuitItem extends Item implements IModifier {
	public final static String[] subNames = { "unprogrammed",

			// Tiller
			"wheatseeds", "melonseeds", "pumpkinseeds", "carrots", "potatoes",
			"rangeupgrade", };
	
	private Icon iconUnProgrammed;
	private Icon iconWheatSeeds;
	private Icon iconMelonSeeds;
	private Icon iconPumpkinSeeds;
	private Icon iconCarrots;
	private Icon iconPotatoes;
	private Icon iconRangeUpgrade;
	private Icon iconTreeSappling;
	
	
	public bioCircuitItem(int id) {
		super(id);
		setCreativeTab(Biotech.tabBiotech);
		setUnlocalizedName("bioCircuit");
		setHasSubtypes(true);
	}
	
	@SideOnly(Side.CLIENT)
	public void func_94581_a(IconRegister iconRegister)
	{
		this.iconUnProgrammed = iconRegister.func_94245_a(Biotech.TEXTURE_NAME_PREFIX + "UnProgrammed");
		this.iconWheatSeeds = iconRegister.func_94245_a(Biotech.TEXTURE_NAME_PREFIX + "WheatSeeds");
		this.iconMelonSeeds = iconRegister.func_94245_a(Biotech.TEXTURE_NAME_PREFIX + "MelonSeeds");
		this.iconPumpkinSeeds = iconRegister.func_94245_a(Biotech.TEXTURE_NAME_PREFIX + "PumpkinSeeds");
		this.iconCarrots = iconRegister.func_94245_a(Biotech.TEXTURE_NAME_PREFIX + "Carrots");
		this.iconPotatoes = iconRegister.func_94245_a(Biotech.TEXTURE_NAME_PREFIX + "Potatoes");
		this.iconRangeUpgrade = iconRegister.func_94245_a(Biotech.TEXTURE_NAME_PREFIX + "RangeUpgrade");
		this.iconTreeSappling = iconRegister.func_94245_a(Biotech.TEXTURE_NAME_PREFIX + "TreeSappling");
	}
	
	@Override
	public Icon getIconFromDamage(int damage)
	{
		switch (damage) {
		case 0:
			return this.iconUnProgrammed; // unprogrammed
		case 1:
			return this.iconWheatSeeds; // wheatseeds
		case 2:
			return this.iconMelonSeeds; // melonseeds
		case 3:
			return this.iconPumpkinSeeds; // pumpkinseeds
		case 4:
			return this.iconCarrots; // carrots
		case 5:
			return this.iconPotatoes; // potatoes
		case 6:
			return this.iconRangeUpgrade; // Range Upgrade
		}

		return super.getIconFromDamage(damage);
	}

	@Override
	public int getMetadata(int damageValue) {
		return damageValue;
	}

	@Override
	public String getName(ItemStack itemstack)
	{
		switch (itemstack.getItemDamage())
		{
			case 1:	
				return "WheatSeeds";
			case 2:	
				return "MelonSeeds";
			case 3:	
				return "PumpkinSeeds";
			case 4:	
				return "Carrots";
			case 5:	
				return "Potatoes";
			case 6:	
				return "RangeUpgrade";
			default:
				return "UnProgrammed";
		}
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

	@Override
	public int getEffectiveness(ItemStack itemstack) {
		// TODO Auto-generated method stub
		return 0;
	}
}
