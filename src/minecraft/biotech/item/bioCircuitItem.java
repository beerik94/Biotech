package biotech.item;

import java.util.ArrayList;
import java.util.List;

import universalelectricity.prefab.modifier.IModifier;

import net.minecraft.block.Block;
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

public class bioCircuitItem extends Item
{
	private final static String[]	subNames	= { "unprogrammed", "wheatseeds", "melonseeds", "pumpkinseeds", "carrots", "potatoes", "rangeupgrade", "treesappling", "pickaxecircuit", "shovelcircuit", "hoecircuit", };
	
	private Icon[]					icons		= new Icon[subNames.length];
	
	private Icon					iconUnProgrammed;
	private Icon					iconWheatSeeds;
	private Icon					iconMelonSeeds;
	private Icon					iconPumpkinSeeds;
	private Icon					iconCarrots;
	private Icon					iconPotatoes;
	private Icon					iconRangeUpgrade;
	private Icon					iconTreeSappling;
	private Icon					iconPickAxeCircuit;
	private Icon					iconShovelCircuit;
	private Icon					iconHoeCircuit;
	
	public bioCircuitItem(int id, int damage)
	{
		super(id);
		setCreativeTab(Biotech.tabBiotech);
		setHasSubtypes(true);
		setUnlocalizedName("bioCircuit");
	}
	
	@SideOnly(Side.CLIENT)
	public void func_94581_a(IconRegister iconRegister)
	{
		for (int i = 0; i < subNames.length; i++)
		{
			
			this.icons[i] = iconRegister.func_94245_a(Biotech.TEXTURE_NAME_PREFIX + subNames[i]);
		}
	}
	
	@Override
	public Icon getIconFromDamage(int damage)
	{
		return this.icons[damage];
	}
	
	@Override
	public int getMetadata(int damageValue)
	{
		return damageValue;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack i)
	{
		int meta = i.getItemDamage();
		
		return this.getUnlocalizedName() + "." + meta;
	}
	
	public ItemStack getStack(int count, int damageValue, String name)
	{
		ItemStack stack = new ItemStack(this, count);
		stack.setItemDamage(damageValue);
		if (damageValue == 6)
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
	public void getSubItems(int i, CreativeTabs tab, List subItems)
	{
		for (int meta = 1; meta < subNames.length; meta++)
		{
			subItems.add(new ItemStack(this, 1, meta));
		}
	}
}
