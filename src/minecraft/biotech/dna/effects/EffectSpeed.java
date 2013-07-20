package biotech.dna.effects;

import net.minecraft.entity.EntityLiving;

public class EffectSpeed extends MovementEffect
{

	@Override
	public String getEffectName()
	{
		return "Quickness";
	}

	@Override
	public float getChanceOfHarvest(String effectName, EntityLiving entity)
	{
		return 0.5f;
	}

	@Override
	public float getSpeedModifier(String effectName, EntityLiving entity)
	{
		return 2;
	}

}
