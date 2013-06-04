package biotech.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class bioEntityFlying extends EntityFlying
{
	public int	drops;

	public bioEntityFlying(World par1World, int Health, float Width, float Height, int Drops, int EV)
	{
		super(par1World);
		this.health = Health;
		this.width = Width;
		this.height = Height;
		this.drops = Drops;
		this.experienceValue = EV;
	}

	@Override
	public int getMaxHealth()
	{
		return health;
	}
}
