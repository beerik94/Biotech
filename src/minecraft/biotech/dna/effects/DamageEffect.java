package biotech.dna.effects;

import java.awt.geom.Arc2D.Float;

import net.minecraft.entity.EntityLiving;

public abstract class DamageEffect extends DnaEffect
{

	@Override
	public String getEffectName()
	{
		return "Damage";
	}

	@Override
	public boolean onEffectUpdate(String effectName, EntityLiving entity)
	{
		return effectName != null && effectName.startsWith(this.getEffectName());
	}

	@Override
	public float getChanceOfHarvest(String effectName, EntityLiving entity)
	{
		return 0.5f;
	}

	public abstract float getDamageIncrease(String effectName, EntityLiving entity);

}
