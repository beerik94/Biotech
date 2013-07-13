package biotech.dna;

import java.util.HashMap;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityChicken;
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

	static
	{
		registerDNA("Chicken", new DNAInfo("Chicken", EntityChicken.class,2,1));
	}
	/**
	 * registers a new dna type, usually only one dna type per entity class
	 * 
	 * @param ref - reference the item will call to load info from NBT. Should never change, and
	 * acts the same as Fluid info
	 * @param dna - Data to store on the dna reference
	 */
	public static void registerDNA(String ref, DNAInfo dna)
	{
		if (ref != null && !ref.isEmpty() && DNAInfo.isValid(dna))
		{
			DNAMap.put(ref, dna);
			MinecraftForge.EVENT_BUS.post(new DNARegisterEvent(ref, dna));
		}
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
		 * @param rarity - drop rarity 1 common 0 rare
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
