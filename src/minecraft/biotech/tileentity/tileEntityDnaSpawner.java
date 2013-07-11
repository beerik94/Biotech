package biotech.tileentity;

import net.minecraft.entity.Entity;
import biotech.entity.passive.bioChicken;
import biotech.entity.passive.bioCow;
import biotech.entity.passive.bioPig;
import biotech.entity.passive.bioSheep;

public class tileEntityDnaSpawner extends tileEntityBasicMachine
{
	// Watts being used per cut
	public static final double	ENERGY_PER_ACTION	= 100;
	
	// Stats
	public int					health;
	public float				width;
	public float				height;
	public int					drops;
	public int					EV;
	
	public boolean				buttonSpawn			= false;
	private int					proccessTicks		= 0;
	
	public tileEntityDnaSpawner()
	{
		super();
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (!worldObj.isRemote)
		{
			System.out.println("ButtonState: " + buttonSpawn);
			CheckLowestValue();
			if (this.inventory[1] != null)
			{
				int dnaInt = inventory[1].getItemDamage();
				if (buttonSpawn)
				{
					proccessTicks++;
				}
				if(proccessTicks == 40)
				{
					buttonSpawn = false;
					proccessTicks = 0;
					SpawnMob(dnaInt);
				}
			}
		}
	}
	
	public void SpawnMob(int dnaDamage)
	{
		Entity par1Entity;
		switch (dnaDamage)
		{
			case 0:
				par1Entity = new bioChicken(worldObj, this.xCoord, this.yCoord + 1, this.zCoord, health, width, height, drops, EV);
				worldObj.spawnEntityInWorld(par1Entity);
				break;
			case 1:
				par1Entity = new bioCow(worldObj, this.xCoord, this.yCoord + 1, this.zCoord, health, width, height, drops, EV);
				worldObj.spawnEntityInWorld(par1Entity);
				break;
			case 2:
				par1Entity = new bioPig(worldObj, this.xCoord, this.yCoord + 1, this.zCoord, health, width, height, drops, EV);
				worldObj.spawnEntityInWorld(par1Entity);
				break;
			case 3:
				par1Entity = new bioSheep(worldObj, this.xCoord, this.yCoord + 1, this.zCoord, health, width, height, drops, EV);
				worldObj.spawnEntityInWorld(par1Entity);
				break;
		}
	}
	
	public void CheckLowestValue()
	{
		if (health < 1)
		{
			health = 1;
		}
		if (width < 0.1f)
		{
			width = 0.1f;
		}
		if (height < 0.1f)
		{
			height = 0.1f;
		}
		if (drops < 1)
		{
			drops = 1;
		}
		if (EV < 1)
		{
			EV = 1;
		}
	}
	
	@Override
	public String getInvName()
	{
		return "DNA Spawner";
	}
}
