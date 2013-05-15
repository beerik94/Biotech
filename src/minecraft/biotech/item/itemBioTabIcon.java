package biotech.item;

import biotech.Biotech;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;

public class itemBioTabIcon extends Item
{
	public itemBioTabIcon(int id)
	{
		super(id);
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon(Biotech.TEXTURE_NAME_PREFIX + "BioTabIcon");
	}
	
	@Override
	public Icon getIconFromDamage(int damage)
	{
		return this.itemIcon;
	}
}
