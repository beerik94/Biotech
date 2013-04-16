package biotech;

import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
		return Biotech.BioTabIcon;
	}
}
