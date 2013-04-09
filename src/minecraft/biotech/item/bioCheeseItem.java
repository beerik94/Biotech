package biotech.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer;
import biotech.Biotech;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class bioCheeseItem extends Item
{
	public bioCheeseItem(int par1)
	{
		super(par1);
		this.setMaxStackSize(64);
		this.setCreativeTab(Biotech.tabBiotech);
		this.setUnlocalizedName("bioCheese");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister iconRegister)
	{
		this.iconIndex = iconRegister.registerIcon(Biotech.TEXTURE_NAME_PREFIX + "KaasHighPoly");
	}
	
	@Override
	public Icon getIconFromDamage(int damage)
	{
		return this.iconIndex;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Returns True is the item is renderer in full 3D when hold.
	 */
	public boolean isFull3D() {
		return true;
	}
}
