package biotech;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BiotechCreativeTab extends CreativeTabs {
	public BiotechCreativeTab() {
		super("tabBiotech");
	}

	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(Item.wheat);
	}
}
