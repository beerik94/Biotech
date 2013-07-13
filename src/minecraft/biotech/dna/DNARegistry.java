package biotech.dna;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import biotech.dna.DNARegistry.DNAInfo;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.EventBus;

/**
 * Collects info on DNA types so to better sort and use DNA items
 * 
 * @author DarkGuardsman
 * 
 */
public class DNARegistry
{
	private static HashMap<String, DNAInfo> DNAMap = new HashMap<String, DNAInfo>();

	public static final DNAInfo chicken = new DNAInfo("Chicken", EntityChicken.class, 2, 0);
	public static final DNAInfo cow = new DNAInfo("Cow", EntityCow.class, 3, 0);

	static
	{
		registerDNA(chicken);
		registerDNA(cow);
	}

	/**
	 * registers a new dna type, usually only one dna type per entity class
	 * 
	 * @param ref - reference the item will call to load info from NBT. Should never change, and
	 * acts the same as Fluid info
	 * @param dna - Data to store on the dna reference
	 */
	public static void registerDNA(DNAInfo dna)
	{
		if (DNAInfo.isValid(dna))
		{
			DNAMap.put(dna.name, dna);
			MinecraftForge.EVENT_BUS.post(new DNARegisterEvent(dna.name, dna));
		}
	}

	/**
	 * Gets DNA info for the given entity
	 */
	public static DNAData getDNAFor(EntityLiving entity)
	{
		if (entity != null)
		{
			NBTTagCompound tag = entity.getEntityData();
			if (tag.hasKey("DNA"))
			{
				DNAData data = DNAData.read(tag.getCompoundTag("DNA"));
				if (data != null)
				{
					return data;
				}
			}
			for (Entry<String, DNAInfo> entry : DNAMap.entrySet())
			{
				Class cla = entry.getValue().entityClass;
				if (entity.getClass().equals(cla))
				{
					return new DNAData(entry.getValue());
				}
			}
			System.out.println("[BioTech]DNA strand not found for " + entity.toString());
		}
		return null;
	}

	/**
	 * Gets the DNAInfo from the creature reference string
	 */
	public static DNAInfo get(String creatureRef)
	{
		return DNAMap.get(creatureRef);
	}

	/**
	 * Gets the rarity of the DNA drop of the entity
	 */
	public static float getRarity(EntityLiving entity)
	{
		DNAData data = getDNAFor(entity);
		if (data != null && data.info != null)
		{
			return data.info.rarity;
		}
		return 0.5f;
	}

	/**
	 * Used to store info on a DNA segment type and not the DNA item itself.
	 */
	public static class DNAInfo
	{
		public String name;
		public Class entityClass;
		public int maxChanges;
		public float rarity;

		/**
		 * 
		 * @param entityName - EntityName for display or look up
		 * @param entityClass - Entity class
		 * @param maxChanges - max dna changes allowed per dna item
		 * @param rarity - drop rarity 0 common 1 rare
		 */
		public DNAInfo(String entityName, Class entityClass, int maxChanges, float rarity)
		{
			this.name = entityName;
			// TODO check for extends EntityLiving to make sure all entities are creatures
			this.entityClass = entityClass;
			this.maxChanges = maxChanges;
			this.rarity = rarity;
		}

		public static boolean isValid(DNAInfo dna)
		{
			return dna != null && dna.name != null && !dna.name.isEmpty() && dna.entityClass != null;
		}
	}

	/**
	 * Used to store info from an Entity about its current DNA
	 */
	public static class DNAData
	{
		public DNAInfo info;
		public String[] effects;

		public DNAData(final DNAInfo info, String... effects)
		{
			this.info = info;
			this.effects = effects;
		}

		public static DNAData read(NBTTagCompound compoundTag)
		{
			DNAInfo info = DNARegistry.get(compoundTag.getString("creatureRef"));
			if (info != null)
			{
				int count = compoundTag.getInteger("count");
				String[] array = null;
				if (count > 0)
				{
					array = new String[count];
					for (int i = 0; i < count; i++)
					{
						array[i] = compoundTag.getString("upgrade" + i);
					}
				}
				return new DNAData(info, array);
			}
			return null;
		}

		public void write(NBTTagCompound tag)
		{
			tag.setString("creatureRef", info.name);
			if (effects != null)
			{
				int i;
				for (i = 0; i < effects.length; i++)
				{
					tag.setString("upgrade" + 1, effects[i]);
				}
				tag.setInteger("count", i);
			}
		}

		@Override
		public String toString()
		{
			return info.name + " DNA";
		}
	}

	public static class DNARegisterEvent extends Event
	{
		public final String refName;
		public final DNAInfo info;

		public DNARegisterEvent(String refName, DNAInfo info)
		{
			this.refName = refName;
			this.info = info;
		}
	}

}
