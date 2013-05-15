package biotech.entity.passive;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class bioCow extends bioEntityAnimal
{
	
	public bioCow(World par1World, int Health, float Width, float Height, int Drops)
	{
		super(par1World, Health, Width, Height, Drops);
		this.texture = "/mob/cow.png";
		this.setSize(Width, Height);
		this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIPanic(this, 0.38F));
		this.tasks.addTask(2, new EntityAIMate(this, 0.2F));
		this.tasks.addTask(3, new EntityAITempt(this, 0.25F, Item.wheat.itemID, false));
		this.tasks.addTask(4, new EntityAIFollowParent(this, 0.25F));
		this.tasks.addTask(5, new EntityAIWander(this, 0.2F));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
	}
	
	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	public boolean isAIEnabled()
	{
		return true;
	}
	
	public int getMaxHealth()
	{
		return Health;
	}
	
	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound()
	{
		return "mob.cow.say";
	}
	
	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound()
	{
		return "mob.cow.hurt";
	}
	
	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound()
	{
		return "mob.cow.hurt";
	}
	
	/**
	 * Plays step sound at given x, y, z for the entity
	 */
	protected void playStepSound(int par1, int par2, int par3, int par4)
	{
		this.playSound("mob.cow.step", 0.15F, 1.0F);
	}
	
	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	protected float getSoundVolume()
	{
		return 0.4F;
	}
	
	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	protected int getDropItemId()
	{
		return Item.leather.itemID;
	}
	
	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity
	 * has recently been hit by a player. @param
	 * par2 - Level of Looting used to kill this mob.
	 */
	protected void dropFewItems(boolean par1, int par2)
	{
		int j = this.rand.nextInt(Drops) + this.rand.nextInt(1 + par2);
		int k;
		
		for (k = 0; k < j; ++k)
		{
			this.dropItem(Item.leather.itemID, 1);
		}
		
		j = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + par2);
		
		for (k = 0; k < j; ++k)
		{
			if (this.isBurning())
			{
				this.dropItem(Item.beefCooked.itemID, 1);
			}
			else
			{
				this.dropItem(Item.beefRaw.itemID, 1);
			}
		}
	}
	
	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer par1EntityPlayer)
	{
		ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();
		
		if (itemstack != null && itemstack.itemID == Item.bucketEmpty.itemID)
		{
			if (--itemstack.stackSize <= 0)
			{
				par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, new ItemStack(Item.bucketMilk));
			}
			else if (!par1EntityPlayer.inventory.addItemStackToInventory(new ItemStack(Item.bucketMilk)))
			{
				par1EntityPlayer.dropPlayerItem(new ItemStack(Item.bucketMilk.itemID, 1, 0));
			}
			
			return true;
		}
		else
		{
			return super.interact(par1EntityPlayer);
		}
	}
	
	/**
	 * This function is used when two same-species animals in 'love mode' breed
	 * to generate the new baby animal.
	 */
	public bioCow spawnBabyAnimal(EntityAgeable par1EntityAgeable)
	{
		return new bioCow(this.worldObj, Health, Width, Height, Drops);
	}
	
	public EntityAgeable createChild(EntityAgeable par1EntityAgeable)
	{
		return this.spawnBabyAnimal(par1EntityAgeable);
	}
}
