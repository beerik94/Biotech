package biotech.tileentity;

import universalelectricity.prefab.network.IPacketReceiver;
import biotech.Biotech;

public class FertilizerTileEntity extends BasicMachineTileEntity implements IPacketReceiver
{
	// Watts being used per cut
	public static final double	WATTS_PER_GROWTH	= 500;
	
	public FertilizerTileEntity()
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
				if (this.ticks % 10 == 0 && this.wattsReceived >= WATTS_PER_GROWTH)
				{
					CheckPlants();
				}
			}
		}
	}
	
	/**
	 * Checks for not fully grown plants
	 */
	public void CheckPlants()
	{
		int xminrange = xCoord - GetRange();
		int xmaxrange = xCoord + GetRange();
		int yminrange = yCoord - GetRange();
		int ymaxrange = yCoord + GetRange();
		int zminrange = zCoord - GetRange();
		int zmaxrange = zCoord + GetRange();
		
		for (int xx = xminrange; xx <= xmaxrange; xx++)
		{
			for (int yy = yminrange; yy <= ymaxrange; yy++)
			{
				for (int zz = zminrange; zz <= zmaxrange; zz++)
				{
					
				}
			}
		}
	}
	
	/**
	 * Plus the plants growth stage
	 */
	public void GrowPlants()
	{
		
	}
	
	/**
	 * Calculates the range
	 */
	public int GetRange()
	{
		if (inventory[1] != null)
		{
			if (inventory[1].isItemEqual(Biotech.RangeUpgrade))
			{
				return (inventory[1].stackSize * 5 + 5);
			}
		}
		return 5;
	}
	
	@Override
	public String getInvName()
	{
		return "Fertilizer";
	}
}