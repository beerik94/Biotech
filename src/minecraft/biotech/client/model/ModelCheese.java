package biotech.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import biotech.Biotech;
import biotech.helpers.models.NMTModelRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelCheese extends ModelBase
{
    /** The Cheese*/
    NMTModelRenderer cheese;

    public ModelCheese()
    {
    	this.textureHeight = 512;
    	this.textureWidth = 512;
    	
        cheese = new NMTModelRenderer(this);
        cheese.setTextureSize(512, 512);
        cheese.mirror = true;
        cheese.addModelOBJ(Biotech.ModelBioCheese);
    }
    
    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
    	super.render(Entity, par2, par3, par4, par5, par6, par7);
        cheese.render(par7);
    }
    
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity)
    {
    	super.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
    }
}