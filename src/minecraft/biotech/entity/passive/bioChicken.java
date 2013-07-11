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
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class bioChicken extends bioEntityAnimal
{
	public boolean	field_70885_d	= false;
	public float	field_70886_e	= 0.0F;
	public float	destPos			= 0.0F;
	public float	field_70884_g;
	public float	field_70888_h;
	public float	field_70889_i	= 1.0F;
	
	/** The time until the next egg is spawned. */
	public int		timeUntilNextEgg;
	
	public bioChicken(World par1World, double X, double Y, double Z, int Health, float Width, float Height, int Drops, int EV)
	{
		super(par1World, Health, Width, Height, Drops, EV);
		this.texture = "/mob/chicken.png";
		this.setSize(Width, Height);
		this.width = Width;
		this.height = Height;
		this.health = Health;
		this.drops = Drops;
		this.experienceValue = EV;
		this.posX = X;
		this.posY = Y;
		this.posZ = Z;
		this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
		float f = 0.25F;
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIPanic(this, 0.38F));
		this.tasks.addTask(2, new EntityAIMate(this, f));
		this.tasks.addTask(3, new EntityAITempt(this, 0.25F, Item.seeds.itemID, false));
		this.tasks.addTask(4, new EntityAIFollowParent(this, 0.28F));
		this.tasks.addTask(5, new EntityAIWander(this, f));
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
		return health;
	}
	
	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		this.field_70888_h = this.field_70886_e;
		this.field_70884_g = this.destPos;
		this.destPos = (float) ((double) this.destPos + (double) (this.onGround ? -1 : 4) * 0.3D);
		
		if (this.destPos < 0.0F)
		{
			this.destPos = 0.0F;
		}
		
		if (this.destPos > 1.0F)
		{
			this.destPos = 1.0F;
		}
		
		if (!this.onGround && this.field_70889_i < 1.0F)
		{
			this.field_70889_i = 1.0F;
		}
		
		this.field_70889_i = (float) ((double) this.field_70889_i * 0.9D);
		
		if (!this.onGround && this.motionY < 0.0D)
		{
			this.motionY *= 0.6D;
		}
		
		this.field_70886_e += this.field_70889_i * 2.0F;
		
		if (!this.isChild() && !this.worldObj.isRemote && --this.timeUntilNextEgg <= 0)
		{
			this.playSound("mob.chicken.plop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			this.dropItem(Item.egg.itemID, 1);
			this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
		}
	}
	
	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	protected void fall(float par1)
	{
	}
	
	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound()
	{
		return "mob.chicken.say";
	}
	
	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound()
	{
		return "mob.chicken.hurt";
	}
	
	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound()
	{
		return "mob.chicken.hurt";
	}
	
	/**
	 * Plays step sound at given x, y, z for the entity
	 */
	protected void playStepSound(int par1, int par2, int par3, int par4)
	{
		this.playSound("mob.chicken.step", 0.15F, 1.0F);
	}
	
	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	protected int getDropItemId()
	{
		return Item.feather.itemID;
	}
	
	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity
	 * has recently been hit by a player. @param
	 * par2 - Level of Looting used to kill this mob.
	 */
	protected void dropFewItems(boolean par1, int par2)
	{
		int j = this.rand.nextInt(drops) + this.rand.nextInt(1 + par2);
		
		for (int k = 0; k < j; ++k)
		{
			this.dropItem(Item.feather.itemID, 1);
		}
		
		if (this.isBurning())
		{
			this.dropItem(Item.chickenCooked.itemID, 1);
		}
		else
		{
			this.dropItem(Item.chickenRaw.itemID, 1);
		}
	}
	
	/**
	 * This function is used when two same-species animals in 'love mode' breed
	 * to generate the new baby animal.
	 */
	public bioChicken spawnBabyAnimal(EntityAgeable par1EntityAgeable)
	{
		return new bioChicken(this.worldObj, this.posX, this.posY, this.posZ, health, width, height, drops, experienceValue);
	}
	
	/**
	 * Checks if the parameter is an item which this animal can be fed to breed
	 * it (wheat, carrots or seeds depending on
	 * the animal type)
	 */
	public boolean isBreedingItem(ItemStack par1ItemStack)
	{
		return par1ItemStack != null && par1ItemStack.getItem() instanceof ItemSeeds;
	}
	
	public EntityAgeable createChild(EntityAgeable par1EntityAgeable)
	{
		return this.spawnBabyAnimal(par1EntityAgeable);
	}
}
