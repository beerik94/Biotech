package biotech.handlers;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import biotech.Biotech;

public class bioEventHandler {

    /* Drops */
    @ForgeSubscribe
    public void onLivingDrop (LivingDropsEvent event)
    {
    	if (event.entityLiving.getClass() == EntityBat.class)
    	{
        	ItemStack dropStack = new ItemStack(Biotech.bioDNA, 1, 0);
            EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, dropStack);
            entityitem.delayBeforeCanPickup = 10;
            event.drops.add(entityitem);
        }
    }

}
