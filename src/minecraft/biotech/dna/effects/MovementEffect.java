package biotech.dna.effects;

import net.minecraft.entity.EntityLiving;

public abstract class MovementEffect extends DnaEffect
{

	@Override
	public String getEffectName()
	{
		return "movement";
	}

	@Override
	public boolean onEffectUpdate(String effectName, EntityLiving entity)
	{
		return effectName != null && effectName.startsWith("movement");
	}

	/** Gets the speed modifier for the given effect on the given Entity */
	public abstract float getSpeedModifier(String effectName, EntityLiving entity);

}
