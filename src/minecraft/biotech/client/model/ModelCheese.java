package biotech.client.model;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

import net.minecraft.client.model.ModelBase;
import biotech.Biotech;
import biotech.helpers.render.WavefrontObject;
import biotech.item.bioCheeseItem;

public class ModelCheese extends ModelBase
{
	private WavefrontObject	modelCheeseOBJ;
	
	public ModelCheese()
	{
		
		modelCheeseOBJ = new WavefrontObject(Biotech.ModelBioCheese);
	}
	
	public void render()
	{
		modelCheeseOBJ.renderAll();
	}
	
	public void render(bioCheeseItem biocheese, double x, double y, double z)
	{
		
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		
		// Scale, Translate, Rotate
		GL11.glScalef(1.0F, 1.0F, 1.0F);
		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.0F, (float) z + 1.2F);
		GL11.glRotatef(45F, 0F, 1F, 0F);
		GL11.glRotatef(-90F, 1F, 0F, 0F);
		
		// Bind texture
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(Biotech.BioCheeseTexture);
		
		// Render
		this.render();
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
}
