package biotech.client;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import biotech.client.model.ModelCheese;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemRenderHandler implements IItemRenderer
{
	public ModelCheese	cheese	= new ModelCheese();
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		switch (type)
		{
			case EQUIPPED:
				return true;
			default:
				return false;
		}
	}
	
	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return false;
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		switch (type)
		{
			case EQUIPPED:
			{
				
			}
			default:
				break;
		}
	}
	
}