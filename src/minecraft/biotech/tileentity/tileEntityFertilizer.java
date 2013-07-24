package biotech.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockSapling;

public class tileEntityFertilizer extends tileEntityBasicMachine
{
	// Watts being used per cut
	public static final double	ENERGY_PER_ACTION	= 500;
	
	public Block[]				GrowablePlants		= new Block[] {};
	
	public tileEntityFertilizer()
	{
		super();
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (!worldObj.isRemote)
		{
			/* Per 10 Tick Process */
			if (this.ticks % 10 == 0 && this.energyStored >= ENERGY_PER_ACTION)
			{
				GrowPlants();
			}
		}
	}
	
	/**
	 * Checks for not fully grown plants
	 */
	public void GrowPlants()
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
					int bID = worldObj.getBlockId(xx, yy, zz);
					if (bID == Block.sapling.blockID)
					{
						if ((double) worldObj.rand.nextFloat() < 0.45D)
						{
							((BlockSapling) Block.sapling).markOrGrowMarked(worldObj, xx, yy, zz, worldObj.rand);
						}
					}
					else if (bID > 0 && Block.blocksList[bID] instanceof BlockCrops)
					{
						if (worldObj.getBlockMetadata(xx, yy, zz) != 7)
						{
							((BlockCrops) Block.blocksList[bID]).fertilize(worldObj, xx, yy, zz);
						}
					}
					this.energyStored -= ENERGY_PER_ACTION;
				}
			}
		}
	}
	
	/**
	 * Calculates the range
	 */
	public int GetRange()
	{
		if (inventory[1] != null)
		{
			return (inventory[1].stackSize * 5 + 5);
		}
		return 5;
	}
	
	@Override
	public String getInvName()
	{
		return "Fertilizer";
	}
}