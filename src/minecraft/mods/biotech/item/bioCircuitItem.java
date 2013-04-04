package mods.biotech.item;

import java.util.List;

import mods.biotech.Biotech;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class bioCircuitItem extends Item
{
	private final static String[]	subNames	= { "UnProgrammed", /*"wheatseeds", "carrots", "potatoes",*/ "RangeUpgrade", /*"treesappling",*/ };
	
	private Icon[]					icons		= new Icon[subNames.length];
	
	private Icon					iconUnProgrammed;
//	private Icon					iconWheatSeeds;
//	private Icon					iconCarrots;
//	private Icon					iconPotatoes;
	private Icon					iconRangeUpgrade;
//	private Icon					iconTreeSappling;
	
	public bioCircuitItem(int id)
	{
		super(id);
		setCreativeTab(Biotech.tabBiotech);
		setHasSubtypes(true);
		setUnlocalizedName("bioCircuit");
		setMaxStackSize(4);
	}
	
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister iconRegister)
	{
		for (int i = 0; i < subNames.length; i++)
		{
			this.icons[i] = iconRegister.registerIcon(Biotech.TEXTURE_NAME_PREFIX + subNames[i]);
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
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(int i, CreativeTabs tab, List subItems)
	{
		for (int meta = 0; meta < subNames.length; meta++)
		{
			subItems.add(new ItemStack(this, 1, meta));
		}
	}
}
