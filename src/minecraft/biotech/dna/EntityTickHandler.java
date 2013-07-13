package biotech.dna;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.entity.EntityLiving;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class EntityTickHandler implements ITickHandler
{
	private static List<EntityLiving> DNAEntities = new ArrayList<EntityLiving>();
	private static HashMap<EntityLiving, Integer> harvestDelay = new HashMap<EntityLiving, Integer>();
	private long tick = 0;
	public static int harvestDelayMin = 900;

	/**
	 * Registers a entity to the DNA Entity list to be tracked and modified
	 */
	public static void registerEntity(EntityLiving entity)
	{
		if (!DNAEntities.contains(entity))
		{
			DNAEntities.add(entity);
		}
	}

	/**
	 * Registers an entity to be track on delay that its dna can be extracted again
	 */
	public static void onHarvestDna(EntityLiving entity, int delay)
	{
		if (delay < 0)
		{
			delay = harvestDelayMin + new Random().nextInt(((int) DNARegistry.getRarity(entity) * harvestDelayMin) + 1);
		}
		harvestDelay.put(entity, delay);
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		tick++;
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{

		if (tick % 100 == 0)
		{
			Iterator<EntityLiving> it = DNAEntities.iterator();
			while (it.hasNext())
			{
				EntityLiving entity = it.next();
				if (entity == null)
				{
					it.remove();
				}
				if (entity.isDead)
				{
					it.remove();
				}
			}
		}

		for (Entry<EntityLiving, Integer> entry : harvestDelay.entrySet())
		{
			int number = entry.getValue() - 1;
			if (number <= 0 || entry.getKey() == null || entry.getKey().isDead)
			{
				harvestDelay.remove(entry.getKey());
			}
			else
			{
				harvestDelay.put(entry.getKey(), number);
				entry.getKey().getEntityData().setInteger("dnaDelay", number);
			}
		}

	}

	@Override
	public EnumSet<TickType> ticks()
	{
		// TODO Auto-generated method stub
		return EnumSet.of(TickType.SERVER);
	}

	@Override
	public String getLabel()
	{
		return "Biotech-EntityTickHandler";
	}

}
