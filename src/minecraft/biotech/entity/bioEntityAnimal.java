package biotech.entity;

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
	public int		inLove;
	
	/**
	 * This is representation of a counter for reproduction progress. (Note that
	 * this is different from the inLove which
	 * represent being in Love-Mode)
	 */
	private int		breeding	= 0;
	
	/**
	 * Variables for editing base things.(health, etc...)
	 */
	public int		Health		= 10;
	public float	Width		= 0.9F;
	public float	Height		= 0.9F;
	public int		Drops		= 3;
	
	
	public bioEntityAnimal(World par1World, int Health, float Width, float Height, int Drops)
	{
		super(par1World);
	}
	
	/**
	 * main AI tick function, replaces updateEntityActionState
	 */
	protected void updateAITick()
	{
		if (this.getGrowingAge() != 0)
		{
			this.inLove = 0;
		}
		
		super.updateAITick();
	}
	
	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (this.getGrowingAge() != 0)
		{
			this.inLove = 0;
		}
		
		if (this.inLove > 0)
		{
			--this.inLove;
			String s = "heart";
			
			if (this.inLove % 10 == 0)
			{
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				double d2 = this.rand.nextGaussian() * 0.02D;
				this.worldObj.spawnParticle(s, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
			}
		}
		else
		{
			this.breeding = 0;
		}
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
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
	{
		if (this.isEntityInvulnerable())
		{
			return false;
		}
		else
		{
			this.fleeingTick = 60;
			this.entityToAttack = null;
			this.inLove = 0;
			return super.attackEntityFrom(par1DamageSource, par2);
		}
	}
	
	/**
	 * Takes a coordinate in and returns a weight to determine how likely this
	 * creature will try to path to the block.
	 * Args: x, y, z
	 */
	public float getBlockPathWeight(int par1, int par2, int par3)
	{
		return this.worldObj.getBlockId(par1, par2 - 1, par3) == Block.grass.blockID ? 10.0F : this.worldObj.getLightBrightness(par1, par2, par3) - 0.5F;
	}
	
	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("InLove", this.inLove);
	}
	
	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readEntityFromNBT(par1NBTTagCompound);
		this.inLove = par1NBTTagCompound.getInteger("InLove");
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
	 * Checks if the entity's current position is a valid location to spawn this
	 * entity.
	 */
	public boolean getCanSpawnHere()
	{
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.boundingBox.minY);
		int k = MathHelper.floor_double(this.posZ);
		return this.worldObj.getBlockId(i, j - 1, k) == Block.grass.blockID && this.worldObj.getFullBlockLightValue(i, j, k) > 8 && super.getCanSpawnHere();
	}
	
	/**
	 * Get number of ticks, at least during which the living entity will be
	 * silent.
	 */
	public int getTalkInterval()
	{
		return 120;
	}
	
	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	protected boolean canDespawn()
	{
		return false;
	}
	
	/**
	 * Get the experience points the entity currently has.
	 */
	protected int getExperiencePoints(EntityPlayer par1EntityPlayer)
	{
		return 1 + this.worldObj.rand.nextInt(3);
	}
	
	/**
	 * Checks if the parameter is an item which this animal can be fed to breed
	 * it (wheat, carrots or seeds depending on
	 * the animal type)
	 */
	public boolean isBreedingItem(ItemStack par1ItemStack)
	{
		return par1ItemStack.itemID == Item.wheat.itemID;
	}
	
	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer par1EntityPlayer)
	{
		ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();
		
		if (itemstack != null && this.isBreedingItem(itemstack) && this.getGrowingAge() == 0 && this.inLove <= 0)
		{
			if (!par1EntityPlayer.capabilities.isCreativeMode)
			{
				--itemstack.stackSize;
				
				if (itemstack.stackSize <= 0)
				{
					par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack) null);
				}
			}
			
			this.inLove = 600;
			this.entityToAttack = null;
			
			for (int i = 0; i < 7; ++i)
			{
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				double d2 = this.rand.nextGaussian() * 0.02D;
				this.worldObj.spawnParticle("heart", this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
			}
			
			return true;
		}
		else
		{
			return super.interact(par1EntityPlayer);
		}
	}
	
	/**
	 * Returns if the entity is currently in 'love mode'.
	 */
	public boolean isInLove()
	{
		return this.inLove > 0;
	}
	
	public void resetInLove()
	{
		this.inLove = 0;
	}
	
	/**
	 * Returns true if the mob is currently able to mate with the specified mob.
	 */
	public boolean canMateWith(bioEntityAnimal par1bioEntityAnimal)
	{
		return par1bioEntityAnimal == this ? false : (par1bioEntityAnimal.getClass() != this.getClass() ? false : this.isInLove() && par1bioEntityAnimal.isInLove());
	}
}
