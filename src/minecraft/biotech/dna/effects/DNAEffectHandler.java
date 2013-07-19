package biotech.dna.effects;

import java.util.HashMap;

import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import biotech.dna.DNARegistry;
import biotech.dna.DNARegistry.DNAData;

public class DNAEffectHandler
{
	HashMap<String, DnaEffect> effectMap = new HashMap<String, DnaEffect>();
	
	
	public void registerEffect(DnaEffect effect)
	{
		
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
					String[] effects = data.effects;
					if (effects != null && effects.length > 0)
					{

					}
				}
			}

		}
	}
}
