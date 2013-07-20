package biotech.dna.effects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import biotech.dna.DNARegistry;
import biotech.dna.DNARegistry.DNAData;

public class DNAEffectHandler
{
	/** Map of all effects and there class handlers */
	static HashMap<String, DnaEffect> effectMap = new HashMap<String, DnaEffect>();
	/** Valid neutral creature effect spawning list */
	public static List<String> validNeutralEffects = new ArrayList<String>();
	/** Valid Monster creature effect spawning list */
	public static List<String> validMonsterEffects = new ArrayList<String>();
	/** Valid Human creature effect spawning list */
	public static List<String> validHumanEffects = new ArrayList<String>();
	/** Array of all lists allowing for easy effect adding to all branches of creatures */
	public static final String[] allLists = new String[] { "Neutrial", "Monster", "Human" };
	// TODO config maxSpeedBoost to prevent OP effect stacking, as well account for potion effects
	public static float maxSpeedBoost = 10;

	static
	{
		registerEffect(new EffectSpeed(), allLists);
	}

	/**
	 * Registers an effect to handler so it will be called per update
	 *
	 * @param effect - new effect instance
	 * @param validSets - valid sets to add the effect to. Used mainly for getting entities to spawn
	 * with the effect ("Neutral, Monster, Human")
	 */
	public static void registerEffect(DnaEffect effect, String... validSets)
	{
		if (effect != null)
		{
			String prefix = effect.getEffectName();
			List<String> effects = effect.getSubEffects();
			for (String e : effects)
			{
				String name = prefix + e;
				effectMap.put(name, effect);
				for (int i = 0; validSets != null && i < validSets.length; i++)
				{
					String na = validSets[i];
					// TODO replace with hashmap of lists to allow for custom lists to be added
					if (na.equalsIgnoreCase("Neutrial") && !validNeutralEffects.contains(name))
					{
						validNeutralEffects.add(name);
					}
					else if (na.equalsIgnoreCase("Monster") && !validMonsterEffects.contains(name))
					{
						validMonsterEffects.add(name);
					}
					else if (na.equalsIgnoreCase("Human") && !validHumanEffects.contains(name))
					{
						validHumanEffects.add(name);
					}
				}
			}
		}
	}

	/** Returns the DnaEffect instance for the given effect name */
	public static DnaEffect getEffect(String name)
	{
		return effectMap.get(name);
	}

	/**
	 * Called by the event or tick handle to update an effect
	 *
	 * @param effect - effect name usually pulled from a list
	 * @param entity - entity this effect is for
	 */
	public static void onEffectUpdated(String effect, EntityLiving entity)
	{
		try
		{
			if (effectMap.containsKey(effect))
			{
				effectMap.get(effect).onEffectUpdate(effect, entity);
			}
		}
		catch (Exception e)
		{
			System.out.println("Biotech has received an error while processing an effect on an entity");
			if (entity != null)
			{
				System.out.println("Effect: " + effect + "   Entity: " + entity.toString());
			}
			e.printStackTrace();
		}
	}

	/** Collects the speed boost to apply to any entity. Called by a modified class in the entity */
	public static float getSpeedBoost(EntityLiving living)
	{
		float boost = 0;
		if (living != null && !living.isDead)
		{
			DNAData data = DNARegistry.getDNAFor(living);
			if (data != null && data.effects != null && data.effects.size() > 0)
			{
				for (String effectName : data.effects)
				{
					DnaEffect effect = getEffect(effectName);
					if (effect instanceof MovementEffect)
					{
						boost += ((MovementEffect) effect).getSpeedModifier(effectName, living);
					}
				}
			}
		}
		return Math.min(Math.max(boost, 0), maxSpeedBoost);
	}

	@ForgeSubscribe
	public void onEntitySpawn(LivingSpawnEvent event)
	{
		EntityLiving entity = event.entityLiving;
		if (entity != null && !entity.isDead)
		{
			NBTTagCompound tag = entity.getEntityData();
			if (!tag.hasKey("DNA"))
			{
				DNAData data = DNARegistry.getNewDataFor(entity);
				if (data != null && data.info != null)
				{
					data.write(entity.getEntityData().getCompoundTag("DNA"));
				}
			}
		}
	}

	@ForgeSubscribe
	public void onEntityUpdate(LivingUpdateEvent event)
	{
		EntityLiving entity = event.entityLiving;
		if (entity != null && !entity.isDead)
		{
			NBTTagCompound tag = entity.getEntityData();
			if (!tag.hasKey("DNA"))
			{
				DNAData data = DNAData.read(entity.getEntityData().getCompoundTag("DNA"));
				if (data != null)
				{
					if (data.effects != null && data.effects.size() > 0)
					{
						for (String effectName : data.effects)
						{
							onEffectUpdated(effectName, entity);
						}
					}
				}
			}

		}
	}
}
