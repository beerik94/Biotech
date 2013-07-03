package biotech.handlers;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import biotech.Biotech;
import biotech.entity.passive.bioChicken;
import biotech.entity.passive.bioCow;
import biotech.entity.passive.bioPig;
import biotech.entity.passive.bioSheep;

public class bioEventHandler 
{
	public void dropItemStack(int par1, int par2, LivingDropsEvent event)
	{
		ItemStack dropStack = new ItemStack(Biotech.bioDNA, par1, par2);
        EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, dropStack);
        entityitem.delayBeforeCanPickup = 10;
        event.drops.add(entityitem);
	}
	
    /* Drops */
    @ForgeSubscribe
    public void onLivingDrop (LivingDropsEvent event)
    {
    	if (!event.entityLiving.isChild())
        {
    		/*
	    	if (event.entityLiving.getClass() == EntityBat.class || event.entityLiving.getClass() == bioBat.class)
	    	{
	        	ItemStack dropStack = new ItemStack(Biotech.bioDNA, 1, 1);
	            EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, dropStack);
	            entityitem.delayBeforeCanPickup = 10;
	            event.drops.add(entityitem);
	        }
	        
	    	else*/ if (event.entityLiving.getClass() == EntityChicken.class || event.entityLiving.getClass() == bioChicken.class)
	    	{
	        	ItemStack dropStack = new ItemStack(Biotech.bioDNA, 1, 2);
	            EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, dropStack);
	            entityitem.delayBeforeCanPickup = 10;
	            event.drops.add(entityitem);
	        }
	    	else if (event.entityLiving.getClass() == EntityCow.class || event.entityLiving.getClass() == bioCow.class)
	    	{
	        	ItemStack dropStack = new ItemStack(Biotech.bioDNA, 1, 3);
	            EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, dropStack);
	            entityitem.delayBeforeCanPickup = 10;
	            event.drops.add(entityitem);
	        }
	    	/*
	    	else if (event.entityLiving.getClass() == EntityOcelot.class || event.entityLiving.getClass() == bioOcelot.class)
	    	{
	        	ItemStack dropStack = new ItemStack(Biotech.bioDNA, 1, 5);
	            EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, dropStack);
	            entityitem.delayBeforeCanPickup = 10;
	            event.drops.add(entityitem);
	        }
	        */
	    	else if (event.entityLiving.getClass() == EntityPig.class || event.entityLiving.getClass() == bioPig.class)
	    	{
	        	ItemStack dropStack = new ItemStack(Biotech.bioDNA, 1, 6);
	            EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, dropStack);
	            entityitem.delayBeforeCanPickup = 10;
	            event.drops.add(entityitem);
	        }
	    	else if (event.entityLiving.getClass() == EntitySheep.class || event.entityLiving.getClass() == bioSheep.class)
	    	{
	        	ItemStack dropStack = new ItemStack(Biotech.bioDNA, 1, 7);
	            EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, dropStack);
	            entityitem.delayBeforeCanPickup = 10;
	            event.drops.add(entityitem);
	        }
	    	/*
	    	else if (event.entityLiving.getClass() == EntitySquid.class || event.entityLiving.getClass() == bioSquid.class)
	    	{
	        	ItemStack dropStack = new ItemStack(Biotech.bioDNA, 1, 8);
	            EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, dropStack);
	            entityitem.delayBeforeCanPickup = 10;
	            event.drops.add(entityitem);
	        }
	    	else if (event.entityLiving.getClass() == EntityWolf.class || event.entityLiving.getClass() == bioWolf.class)
	    	{
	        	ItemStack dropStack = new ItemStack(Biotech.bioDNA, 1, 9);
	            EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, dropStack);
	            entityitem.delayBeforeCanPickup = 10;
	            event.drops.add(entityitem);
	        }
	    	else if (event.entityLiving.getClass() == EntityBlaze.class || event.entityLiving.getClass() == bioBlaze.class)
	    	{
	        	ItemStack dropStack = new ItemStack(Biotech.bioDNA, 1, 10);
	            EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, dropStack);
	            entityitem.delayBeforeCanPickup = 10;
	            event.drops.add(entityitem);
	        }
	    	else if (event.entityLiving.getClass() == EntityCaveSpider.class || event.entityLiving.getClass() == bioCaveSpider.class)
	    	{
	        	ItemStack dropStack = new ItemStack(Biotech.bioDNA, 1, 11);
	            EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, dropStack);
	            entityitem.delayBeforeCanPickup = 10;
	            event.drops.add(entityitem);
	        }
	    	else if (event.entityLiving.getClass() == EntityCreeper.class || event.entityLiving.getClass() == bioCreeper.class)
	    	{
	        	ItemStack dropStack = new ItemStack(Biotech.bioDNA, 1, 12);
	            EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, dropStack);
	            entityitem.delayBeforeCanPickup = 10;
	            event.drops.add(entityitem);
	        }
	    	else if (event.entityLiving.getClass() == EntityEnderman.class || event.entityLiving.getClass() == bioEnderman.class)
	    	{
	        	ItemStack dropStack = new ItemStack(Biotech.bioDNA, 1, 13);
	            EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, dropStack);
	            entityitem.delayBeforeCanPickup = 10;
	            event.drops.add(entityitem);
	        }
	    	else if (event.entityLiving.getClass() == EntityGhast.class || event.entityLiving.getClass() == bioGhast.class)
	    	{
	        	ItemStack dropStack = new ItemStack(Biotech.bioDNA, 1, 14);
	            EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, dropStack);
	            entityitem.delayBeforeCanPickup = 10;
	            event.drops.add(entityitem);
	        }
	    	else if(event.entityLiving.getClass() == EntitySkeleton.class || event.entityLiving.getClass() == bioSkeleton.class)
	    	{
	    		ItemStack dropStack = new ItemStack(Biotech.bioDNA, 1, 15);
	    		EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, dropStack);
	    		entityitem.delayBeforeCanPickup = 10;
	    		event.drops.add(entityitem);
	    	}
	    	else if(event.entityLiving.getClass() == EntitySpider.class || event.entityLiving.getClass() == bioSpider.class)
	    	{
	    		ItemStack dropStack = new ItemStack(Biotech.bioDNA, 1, 16);
	    		EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, dropStack);
	    		entityitem.delayBeforeCanPickup = 10;
	    		event.drops.add(entityitem);
	    	}
	    	else if(event.entityLiving.getClass() == EntityZombie.class || event.entityLiving.getClass() == bioZombie.class)
	    	{
	    		ItemStack dropStack = new ItemStack(Biotech.bioDNA, 1, 17);
	    		EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, dropStack);
	    		entityitem.delayBeforeCanPickup = 10;
	    		event.drops.add(entityitem);
	    	}
	    	*/
        }
    }

}
