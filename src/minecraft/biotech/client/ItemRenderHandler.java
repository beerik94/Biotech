package biotech.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import biotech.Biotech;
import biotech.client.model.ModelCheese;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemRenderHandler implements IItemRenderer
{
	public ModelCheese cheese;
	
	public ItemRenderHandler()
	{
		cheese = new ModelCheese();
	}
	
	
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
				GL11.glPushMatrix();
				
				Minecraft.getMinecraft().renderEngine.bindTexture(Biotech.BioCheeseTexture);
				
				cheese.render((Entity)data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
				
				GL11.glPopMatrix();
			}
			default:
				break;
		}
	}
	
}