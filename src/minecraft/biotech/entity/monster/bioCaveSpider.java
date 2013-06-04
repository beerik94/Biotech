package biotech.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class bioCaveSpider extends bioSpider
{
    public bioCaveSpider(World par1World, int Health, float Width, float Height, int Drops, int EV, int AS, boolean Hostile)
    {
        super(par1World, Health, Width, Height, Drops, EV, AS, Hostile);
        this.texture = "/mob/cavespider.png";
        this.setSize(0.7F, 0.5F);
    }

    @SideOnly(Side.CLIENT)

    /**
     * How large the spider should be scaled.
     */
    public float spiderScaleAmount()
    {
        return 0.7F;
    }

    public boolean attackEntityAsMob(Entity par1Entity)
    {
        if (super.attackEntityAsMob(par1Entity))
        {
            if (par1Entity instanceof EntityLiving)
            {
                byte b0 = 0;

                if (this.worldObj.difficultySetting > 1)
                {
                    if (this.worldObj.difficultySetting == 2)
                    {
                        b0 = 7;
                    }
                    else if (this.worldObj.difficultySetting == 3)
                    {
                        b0 = 15;
                    }
                }

                if (b0 > 0)
                {
                    ((EntityLiving)par1Entity).addPotionEffect(new PotionEffect(Potion.poison.id, b0 * 20, 0));
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Initialize this creature.
     */
    public void initCreature() {}
}
