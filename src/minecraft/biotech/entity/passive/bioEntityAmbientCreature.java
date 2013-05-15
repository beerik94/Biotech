package biotech.entity.passive;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.world.World;

public abstract class bioEntityAmbientCreature extends EntityLiving implements IAnimals
{
	/**
	 * Variables for editing base things.(health, etc...)
	 */
	public int		Health		= 10;
	public float	Width		= 0.9F;
	public float	Height		= 0.9F;
	public int		Drops		= 3;
	
	public bioEntityAmbientCreature(World par1World, int Health, float Width, float Height, int Drops)
	{
		super(par1World);
	}
}
