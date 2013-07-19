package biotech.dna.effects;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLiving;

/**
 * Method all DNA modifiers or effects should use to construct effects that can be applied by the
 * DNA system to entities
 * 
 * @author DarkGuardsman
 * 
 */
public abstract class DnaEffect
{
	/** Main name of the effect used as a prefix for subEffects */
	public abstract String getEffectName();

	/** Called when a dna effect is applied to an entity use this to modify the entity */
	public abstract boolean applyEffect(String effectName, EntityLiving entity);

	/** Called when new DNA data is created for a basic entity */
	public abstract float getChanceOfHarvest(String effectName, EntityLiving entity);

	/** Checks to see if the effect can be applied to the given entity */
	public boolean canApplyEffect(String effectName, EntityLiving entity)
	{
		return entity != null && !entity.isDead;
	}

	/** Sub effects of this effect use if you have several effects that are level ups of each other */
	public List<String> getSubEffects()
	{
		List<String> list = new ArrayList<String>();
		list.add("");
		return list;
	}
}
