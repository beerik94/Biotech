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
        this.cheese = new NMTModelRenderer(this);
        this.cheese.addModelOBJ(Biotech.MODEL_PATH + "KaasHighPoly");
    }
    
    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        this.cheese.render(par7);
    }
}