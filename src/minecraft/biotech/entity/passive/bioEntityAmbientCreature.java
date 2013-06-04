package biotech.entity.passive;

import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.world.World;

public abstract class bioEntityAmbientCreature extends EntityLiving implements IAnimals
{
	
	private EntityLookHelper	lookHelper;
	private EntityMoveHelper	moveHelper;
	private EntityJumpHelper	jumpHelper;
	private EntityBodyHelper	bodyHelper;
	private PathNavigate		navigator;
	private EntitySenses		senses;
	public final EntityAITasks	tasks;
	public final EntityAITasks	targetTasks;
	
	public bioEntityAmbientCreature(World par1World, int Health, float Width, float Height, int Drops, int ExperienceValue)
	{
		super(par1World);
		this.preventEntitySpawning = true;
		this.tasks = new EntityAITasks(par1World != null && par1World.theProfiler != null ? par1World.theProfiler : null);
		this.targetTasks = new EntityAITasks(par1World != null && par1World.theProfiler != null ? par1World.theProfiler : null);
		this.lookHelper = new EntityLookHelper(this);
		this.moveHelper = new EntityMoveHelper(this);
		this.jumpHelper = new EntityJumpHelper(this);
		this.bodyHelper = new EntityBodyHelper(this);
		this.navigator = new PathNavigate(this, par1World, (float) this.func_96121_ay());
		this.senses = new EntitySenses(this);
		this.field_70770_ap = (float) (Math.random() + 1.0D) * 0.01F;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.field_70769_ao = (float) Math.random() * 12398.0F;
		this.rotationYaw = (float) (Math.random() * Math.PI * 2.0D);
		this.rotationYawHead = this.rotationYaw;
		this.health = Health;
		this.width = Width;
		this.height = Height;
		this.experienceValue = ExperienceValue;
		
		for (int i = 0; i < this.equipmentDropChances.length; ++i)
		{
			this.equipmentDropChances[i] = 0.085F;
		}
		this.stepHeight = 0.5F;
	}
}
