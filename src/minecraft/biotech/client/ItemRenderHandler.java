package biotech.client;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import biotech.Biotech;
import biotech.client.model.ModelCheese;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemRenderHandler implements IItemRenderer
{
	public ModelCheese	cheese;
	
	public ItemRenderHandler()
	{
		cheese = new ModelCheese();
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return true;
	}
	
	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		switch (type) {
            case ENTITY: {
                renderCheese(-0.5F, 0.0F, 0.5F, 1.0F);
                return;
            }
            case EQUIPPED: {
                renderCheese(0.0F, 0.0F, 1.0F, 1.0F);
                return;
            }
            case INVENTORY: {
                renderCheese(0.0F, -0.1F, 1.0F, 1.0F);
                return;
            }
            default:
                return;
        }
	}
	
	private void renderCheese(float x, float y, float z, float scale)
	{
		GL11.glPushMatrix();
    	GL11.glDisable(GL11.GL_LIGHTING);
		
    	GL11.glScalef(scale, scale, scale);
    	GL11.glTranslatef(x, y, z);

		FMLClientHandler.instance().getClient().renderEngine.bindTexture(Biotech.BioCheeseTexture);

		cheese.render();
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
}