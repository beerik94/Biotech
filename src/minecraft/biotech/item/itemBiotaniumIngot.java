package biotech.item;

import biotech.Biotech;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;

public class itemBiotaniumIngot extends Item
{

	public itemBiotaniumIngot(int id)
	{
		super(id);
		setCreativeTab(Biotech.tabBiotech);
		setUnlocalizedName("BiotaniumIngot");
		setMaxStackSize(64);
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon(Biotech.TEXTURE_NAME_PREFIX + "BiotaniumIngot");
	}
	
	@Override
	public Icon getIconFromDamage(int meta)
	{
		return this.itemIcon;
	}
	
	@Override
	public int getMetadata(int damageValue)
	{
		return damageValue;
	}
}
