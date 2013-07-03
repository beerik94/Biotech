package biotech.tileentity;

public class tileEntityDnaSpawner extends tileEntityBasicMachine
{
	// Watts being used per cut
	public static final double	ENERGY_PER_ACTION	= 100;
	
	// Stats
	public int health = 0;
	public float width = 0;
	public float height = 0;
	public int drops = 0;
	public int EV = 0;
	
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
			if (this.checkRedstone())
			{
				/* Per 40 Tick Process */
				if (this.ticks % 15 == 0 && this.electricityStored >= ENERGY_PER_ACTION)
				{
					
				}
			}
		}
	}
	
	@Override
	public String getInvName()
	{
		return "DNA Spawner";
	}
}
