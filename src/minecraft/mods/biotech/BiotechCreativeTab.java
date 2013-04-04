package mods.biotech;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class BiotechCreativeTab extends CreativeTabs
{
	public BiotechCreativeTab()
	{
		super("tabBiotech");
		LanguageRegistry.instance().addStringLocalization("itemGroup.tabBiotech", "Biotech");
	}
	
	@Override
	public ItemStack getIconItemStack()
	{
		return new ItemStack(Item.wheat);
	}
}
