package biotech.dna.effects;

import net.minecraft.entity.EntityLiving;

public class EffectSpeed extends DnaEffect
{

	@Override
	public String getEffectName()
	{
		return "Quickness";
	}

	@Override
	public boolean onEffectUpdate(String effectName, EntityLiving entity)
	{
		if (entity != null && !entity.isDead)
		{
			EntityLiving entityDefault = new EntityLiving(entity.worldObj);
		}
		return false;
	}

	@Override
	public float getChanceOfHarvest(String effectName, EntityLiving entity)
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
