package biotech.dna.effects;

import net.minecraft.entity.EntityLiving;

public class EffectDrop extends DnaEffect
{

	@Override
	public String getEffectName()
	{
		return "Regrow";
	}

	@Override
	public boolean onEffectUpdate(String effectName, EntityLiving entity)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float getChanceOfHarvest(String effectName, EntityLiving entity)
	{
		return 0.1f;
	}

}
