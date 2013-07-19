package biotech.dna;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import biotech.dna.DNARegistry.DNAData;

import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class EntityTickHandler implements ITickHandler
{
	private static HashMap<EntityLiving, Integer> harvestDelay = new HashMap<EntityLiving, Integer>();
	private long tick = 0;
	public static int harvestDelayMin = 900;

	/**
	 * Registers an entity to be track on delay that its dna can be extracted again
	 */
	public static void onHarvestDna(EntityLiving entity, int delay)
	{
		if (entity != null)
		{
			// get new delay or load it if delay is under zero
			if (delay < 0)
			{
				delay = entity.getEntityData().getInteger("dnaDelay");
				// Generate delay if delay is zero or bellow
				if (delay <= 0)
				{
					delay = harvestDelayMin + new Random().nextInt(((int) DNARegistry.getRarity(entity) * harvestDelayMin) + 1);
				}
			}

			harvestDelay.put(entity, delay);
		}
	}

	/**
	 * returns the current delay before DNA can be extracted again
	 */
	public static int getDelay(EntityLiving entity)
	{
		if (harvestDelay.containsKey(entity))
		{
			return harvestDelay.get(entity);
		}
		return 0;
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		tick++;
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{

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
