package biotech.client.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import biotech.Biotech;
import biotech.helpers.models.NMTModelRenderer;
import biotech.helpers.obj.WavefrontObject;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelCheese extends ModelBase
{
    /** The Cheese*/
    private WavefrontObject cheeseOBJ;

    public ModelCheese()
    {
    	cheeseOBJ = new WavefrontObject(Biotech.ModelBioCheese);
    }
    
    public void render()
    {
    	cheeseOBJ.renderAll();
    }
    
    public void render(Entity Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
    	GL11.glPushMatrix();
    	GL11.glDisable(GL11.GL_LIGHTING);
		
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(Biotech.BioCheeseTexture);
		
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		
		this.render();
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
    }
}