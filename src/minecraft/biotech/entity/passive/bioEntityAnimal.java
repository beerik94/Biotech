package biotech.entity.passive;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class bioEntityAnimal extends EntityAnimal
{	
	/**
	 * This is representation of a counter for reproduction progress. (Note that
	 * this is different from the inLove which
	 * represent being in Love-Mode)
	 */
	private int		breeding	= 0;
	
	public int drops;
	
	public bioEntityAnimal(World par1World, int Health, float Width, float Height, int Drops, int EV)
	{
		super(par1World);
		this.health = Health;
		this.width = Width;
		this.height = Height;
		this.setSize(Width, Height);
		this.drops = Drops;
		this.experienceValue = EV;
	}
	
	/**
	 * Basic mob attack. Default to touch of death in EntityCreature. Overridden
	 * by each mob to define their attack.
	 */
	protected void attackEntity(Entity par1Entity, float par2)
	{
		if (par1Entity instanceof EntityPlayer)
		{
			if (par2 < 3.0F)
			{
				double d0 = par1Entity.posX - this.posX;
				double d1 = par1Entity.posZ - this.posZ;
				this.rotationYaw = (float) (Math.atan2(d1, d0) * 180.0D / Math.PI) - 90.0F;
				this.hasAttacked = true;
			}
			
			EntityPlayer entityplayer = (EntityPlayer) par1Entity;
			
			if (entityplayer.getCurrentEquippedItem() == null || !this.isBreedingItem(entityplayer.getCurrentEquippedItem()))
			{
				this.entityToAttack = null;
			}
		}
		else if (par1Entity instanceof bioEntityAnimal)
		{
			bioEntityAnimal biobaseentity = (bioEntityAnimal) par1Entity;
			
			if (this.getGrowingAge() > 0 && biobaseentity.getGrowingAge() < 0)
			{
				if ((double) par2 < 2.5D)
				{
					this.hasAttacked = true;
				}
			}
			else if (this.inLove > 0 && biobaseentity.inLove > 0)
			{
				if (biobaseentity.entityToAttack == null)
				{
					biobaseentity.entityToAttack = this;
				}
				
				if (biobaseentity.entityToAttack == this && (double) par2 < 3.5D)
				{
					++biobaseentity.inLove;
					++this.inLove;
					++this.breeding;
					
					if (this.breeding % 4 == 0)
					{
						this.worldObj.spawnParticle("heart", this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, 0.0D, 0.0D, 0.0D);
					}
					
					if (this.breeding == 60)
					{
						this.procreate((bioEntityAnimal) par1Entity);
					}
				}
				else
				{
					this.breeding = 0;
				}
			}
			else
			{
				this.breeding = 0;
				this.entityToAttack = null;
			}
		}
	}
	
	/**
	 * Creates a baby animal according to the animal type of the target at the
	 * actual position and spawns 'love'
	 * particles.
	 */
	private void procreate(bioEntityAnimal par1bioEntityAnimal)
	{
		EntityAgeable entityageable = this.createChild(par1bioEntityAnimal);
		
		if (entityageable != null)
		{
			this.setGrowingAge(6000);
			par1bioEntityAnimal.setGrowingAge(6000);
			this.inLove = 0;
			this.breeding = 0;
			this.entityToAttack = null;
			par1bioEntityAnimal.entityToAttack = null;
			par1bioEntityAnimal.breeding = 0;
			par1bioEntityAnimal.inLove = 0;
			entityageable.setGrowingAge(-24000);
			entityageable.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			
			for (int i = 0; i < 7; ++i)
			{
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				double d2 = this.rand.nextGaussian() * 0.02D;
				this.worldObj.spawnParticle("heart", this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
			}
			
			this.worldObj.spawnEntityInWorld(entityageable);
		}
	}
	
	/**
	 * Finds the closest player within 16 blocks to attack, or null if this
	 * Entity isn't interested in attacking
	 * (Animals, Spiders at day, peaceful PigZombies).
	 */
	protected Entity findPlayerToAttack()
	{
		if (this.fleeingTick > 0)
		{
			return null;
		}
		else
		{
			float f = 8.0F;
			List list;
			int i;
			bioEntityAnimal bioentityanimal;
			
			if (this.inLove > 0)
			{
				list = this.worldObj.getEntitiesWithinAABB(this.getClass(), this.boundingBox.expand((double) f, (double) f, (double) f));
				
				for (i = 0; i < list.size(); ++i)
				{
					bioentityanimal = (bioEntityAnimal) list.get(i);
					
					if (bioentityanimal != this && bioentityanimal.inLove > 0)
					{
						return bioentityanimal;
					}
				}
			}
			else if (this.getGrowingAge() == 0)
			{
				list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.boundingBox.expand((double) f, (double) f, (double) f));
				
				for (i = 0; i < list.size(); ++i)
				{
					EntityPlayer entityplayer = (EntityPlayer) list.get(i);
					
					if (entityplayer.getCurrentEquippedItem() != null && this.isBreedingItem(entityplayer.getCurrentEquippedItem()))
					{
						return entityplayer;
					}
				}
			}
			else if (this.getGrowingAge() > 0)
			{
				list = this.worldObj.getEntitiesWithinAABB(this.getClass(), this.boundingBox.expand((double) f, (double) f, (double) f));
				
				for (i = 0; i < list.size(); ++i)
				{
					bioentityanimal = (bioEntityAnimal) list.get(i);
					
					if (bioentityanimal != this && bioentityanimal.getGrowingAge() < 0)
					{
						return bioentityanimal;
					}
				}
			}
			
			return null;
		}
	}
	
	/**
	 * Returns true if the mob is currently able to mate with the specified mob.
	 */
	public boolean canMateWith(bioEntityAnimal par1bioEntityAnimal)
	{
		return par1bioEntityAnimal == this ? false : (par1bioEntityAnimal.getClass() != this.getClass() ? false : this.isInLove() && par1bioEntityAnimal.isInLove());
	}
}
