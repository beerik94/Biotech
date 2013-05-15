package biotech.entity;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIControlledByPlayer;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.world.World;

public class bioPig extends bioEntityAnimal
{
	/** AI task for player control. */
	private final EntityAIControlledByPlayer	aiControlledByPlayer;
	
	public bioPig(World par1World, int Health, float Width, float Height, int Drops)
	{
		super(par1World, Health, Width, Height, Drops);
		this.texture = "/mob/pig.png";
		this.setSize(Width, Height);
		this.getNavigator().setAvoidsWater(true);
		float f = 0.25F;
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIPanic(this, 0.38F));
		this.tasks.addTask(2, this.aiControlledByPlayer = new EntityAIControlledByPlayer(this, 0.34F));
		this.tasks.addTask(3, new EntityAIMate(this, f));
		this.tasks.addTask(4, new EntityAITempt(this, 0.3F, Item.carrotOnAStick.itemID, false));
		this.tasks.addTask(4, new EntityAITempt(this, 0.3F, Item.carrot.itemID, false));
		this.tasks.addTask(5, new EntityAIFollowParent(this, 0.28F));
		this.tasks.addTask(6, new EntityAIWander(this, f));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
	}
	
	@Override
	public int getMaxHealth()
	{
		return Health;
	}
	
	protected void updateAITasks()
	{
		super.updateAITasks();
	}
	
	/**
	 * returns true if all the conditions for steering the entity are met. For
	 * pigs, this is true if it is being ridden
	 * by a player and the player is holding a carrot-on-a-stick
	 */
	public boolean canBeSteered()
	{
		ItemStack itemstack = ((EntityPlayer) this.riddenByEntity).getHeldItem();
		return itemstack != null && itemstack.itemID == Item.carrotOnAStick.itemID;
	}
	
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
	}
	
	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("Saddle", this.getSaddled());
	}
	
	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setSaddled(par1NBTTagCompound.getBoolean("Saddle"));
	}
	
	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound()
	{
		return "mob.pig.say";
	}
	
	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound()
	{
		return "mob.pig.say";
	}
	
	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound()
	{
		return "mob.pig.death";
	}
	
	/**
	 * Plays step sound at given x, y, z for the entity
	 */
	protected void playStepSound(int par1, int par2, int par3, int par4)
	{
		this.playSound("mob.pig.step", 0.15F, 1.0F);
	}
	
	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer par1EntityPlayer)
	{
		if (super.interact(par1EntityPlayer))
		{
			return true;
		}
		else if (this.getSaddled() && !this.worldObj.isRemote && (this.riddenByEntity == null || this.riddenByEntity == par1EntityPlayer))
		{
			par1EntityPlayer.mountEntity(this);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	protected int getDropItemId()
	{
		return this.isBurning() ? Item.porkCooked.itemID : Item.porkRaw.itemID;
	}
	
	/**
	 * Drop 0-2 items of this living's type.
	 * 
	 * @param par1
	 *            - Whether this entity
	 *            has recently been hit by a player.
	 * @param par2
	 *            - Level of Looting used to kill this mob.
	 */
	protected void dropFewItems(boolean par1, int par2)
	{
		int j = this.rand.nextInt(Drops) + 1 + this.rand.nextInt(1 + par2);
		
		for (int k = 0; k < j; ++k)
		{
			if (this.isBurning())
			{
				this.dropItem(Item.porkCooked.itemID, 1);
			}
			else
			{
				this.dropItem(Item.porkRaw.itemID, 1);
			}
		}
		
		if (this.getSaddled())
		{
			this.dropItem(Item.saddle.itemID, 1);
		}
	}
	
	/**
	 * Returns true if the pig is saddled.
	 */
	public boolean getSaddled()
	{
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}
	
	/**
	 * Set or remove the saddle of the pig.
	 */
	public void setSaddled(boolean par1)
	{
		if (par1)
		{
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) 1));
		}
		else
		{
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) 0));
		}
	}
	
	/**
	 * Called when a lightning bolt hits the entity.
	 */
	public void onStruckByLightning(EntityLightningBolt par1EntityLightningBolt)
	{
		if (!this.worldObj.isRemote)
		{
			EntityPigZombie entitypigzombie = new EntityPigZombie(this.worldObj);
			entitypigzombie.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			this.worldObj.spawnEntityInWorld(entitypigzombie);
			this.setDead();
		}
	}
	
	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	protected void fall(float par1)
	{
		super.fall(par1);
		
		if (par1 > 5.0F && this.riddenByEntity instanceof EntityPlayer)
		{
			((EntityPlayer) this.riddenByEntity).triggerAchievement(AchievementList.flyPig);
		}
	}
	
	/**
	 * This function is used when two same-species animals in 'love mode' breed
	 * to generate the new baby animal.
	 */
	public bioPig spawnBabyAnimal(EntityAgeable par1EntityAgeable)
	{
		return new bioPig(this.worldObj, Health, Width, Height, Drops);
	}
	
	/**
	 * Checks if the parameter is an item which this animal can be fed to breed
	 * it (wheat, carrots or seeds depending on
	 * the animal type)
	 */
	public boolean isBreedingItem(ItemStack par1ItemStack)
	{
		return par1ItemStack != null && par1ItemStack.itemID == Item.carrot.itemID;
	}
	
	/**
	 * Return the AI task for player control.
	 */
	public EntityAIControlledByPlayer getAIControlledByPlayer()
	{
		return this.aiControlledByPlayer;
	}
	
	public EntityAgeable createChild(EntityAgeable par1EntityAgeable)
	{
		return this.spawnBabyAnimal(par1EntityAgeable);
	}
	
}
