package biotech.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class bioEntityMob extends EntityMob
{
	public int	drops;
	public int	AttackStrength;
	public boolean IsHostile = true;
	
	public bioEntityMob(World par1World, int Health, float Width, float Height, int Drops, int EV, int AS, boolean Hostile)
	{
		super(par1World);
		this.health = Health;
		this.width = Width;
		this.height = Height;
		this.drops = Drops;
		this.AttackStrength = AS;
		this.IsHostile = Hostile;
		this.experienceValue = EV;
	}
	
	@Override
	public int getMaxHealth()
	{
		return health;
	}
	
	/**
	 * Returns the amount of damage a mob should deal.
	 */
	@Override
	public int getAttackStrength(Entity par1Entity)
	{
		return AttackStrength;
	}
	
	/**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
	@Override
    protected Entity findPlayerToAttack()
    {
        EntityPlayer entityplayer = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
        if(IsHostile)
        {
        	return entityplayer != null && this.canEntityBeSeen(entityplayer) ? entityplayer : null;
        }
        else
        {
        	return null;
        }
    }
}
