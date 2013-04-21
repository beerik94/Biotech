package biotech.tileentity;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import biotech.helpers.Util;

public class tileEntityFarmingMachine extends tileEntityBasicMachine
{
	// Watts being used per action
	public static final double	ENERGY_PER_ACTION	= 75;
	
	Random						random;
	
	private Block				tilledField			= Block.tilledField;
	private Block				wheatseedsField		= Block.crops;
	private Block				melonStemField		= Block.melonStem;
	private Block				pumpkinStemField	= Block.pumpkinStem;
	private Block				carrotField			= Block.carrot;
	private Block				potatoField			= Block.potato;
	
	protected Item[]			resourceStacks		= new Item[] { Item.seeds, Item.carrot, Item.potato, };
	protected Block[]			cropStacks			= new Block[] { Block.crops, Block.carrot, Block.potato, };
	protected ItemStack[]		harvestStacks		= new ItemStack[] { new ItemStack(Item.wheat, 1), new ItemStack(Item.carrot, 1), new ItemStack(Item.potato, 1), };
	
	public tileEntityFarmingMachine()
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
				/* Per 40 Tick Processes */
				if (this.ticks % 40 == 0 && this.electricityStored >= ENERGY_PER_ACTION)
				{
					this.WorkArea();
					this.electricityStored -= ENERGY_PER_ACTION;
				}
			}
		}
	}
	
	public int AreaSize()
	{
		if (this.inventory[2] != null)
		{
			return (2 * this.inventory[2].stackSize);
		}
		return 2;
	}
	
	/**
	 * Calculates the work area base on the AreaSize
	 */
	public void WorkArea()
	{
		int xmin = 0;
		int xmax = 0;
		int zmin = 0;
		int zmax = 0;
		
		switch (this.getFacing())
		{
			case 2:
			{
				xmin = xCoord - AreaSize();
				xmax = xCoord + AreaSize();
				zmin = zCoord - 1 - AreaSize();
				zmax = zCoord - 1;
				break;
			}
			case 3:
			{
				xmin = xCoord - AreaSize();
				xmax = xCoord + AreaSize();
				zmin = zCoord + 1;
				zmax = zCoord + 1 + AreaSize();
				break;
			}
			case 4:
			{
				xmin = xCoord - 1 - AreaSize();
				xmax = xCoord - 1;
				zmin = zCoord - AreaSize();
				zmax = zCoord + AreaSize();
				break;
			}
			case 5:
			{
				xmin = xCoord + 1;
				xmax = xCoord + 1 + AreaSize();
				zmin = zCoord - AreaSize();
				zmax = zCoord + AreaSize();
				break;
			}
		}
		
		for (int i = 0; i < resourceStacks.length; i++)
		{
			if (this.inventory[1] != null && this.inventory[1].itemID == resourceStacks[i].itemID)
			{
				for (int xx = xmin; xx < xmax; xx++)
				{
					for (int zz = zmin; zz < zmax; zz++)
					{
						if (worldObj.getBlockId(xx, this.yCoord - 1, zz) != Block.tilledField.blockID)
						{
							TillLand(xx, this.yCoord - 1, zz);
						}
						if (worldObj.getBlockId(xx, this.yCoord - 1, zz) == Block.tilledField.blockID)
						{
							PlantSeed(xx, this.yCoord, zz, cropStacks[i].blockID);
						}
						else if (worldObj.getBlockId(xx, this.yCoord - 1, zz) == Block.tilledField.blockID && worldObj.getBlockId(xx, this.yCoord, zz) == cropStacks[i].blockID)
						{
							HarvestPlant(xx, this.yCoord, zz, harvestStacks[i]);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Makes dirt tilledDirt at given position
	 * 
	 * @param x
	 *            position
	 * @param y
	 *            position
	 * @param z
	 *            position
	 */
	public void TillLand(int x, int y, int z)
	{
		worldObj.setBlock(x, y, z, Block.tilledField.blockID, 0, 2);
	}
	
	/**
	 * Plants the given seed at the given position
	 * 
	 * @param x
	 *            position
	 * @param y
	 *            position
	 * @param z
	 *            position
	 * @param seed
	 *            that has to be planted
	 */
	public void PlantSeed(int x, int y, int z, int seed)
	{
		worldObj.setBlock(x, y, z, seed, 0, 3);
		this.inventory[1].stackSize -= 1;
	}
	
	/**
	 * Harvests the plant at the given position
	 * 
	 * @param x
	 *            position
	 * @param y
	 *            position
	 * @param z
	 *            position
	 * @param plant
	 *            that has to be harvested
	 */
	public void HarvestPlant(int x, int y, int z, ItemStack plant)
	{
		worldObj.setBlock(x, y, z, 0, 0, 2);
		
		for (int i = 3; i < 8; i++)
		{
			if (this.inventory[i] == null)
			{
				this.inventory[i] = (plant);
			}
			if (this.inventory[i] != null && this.inventory[i].stackSize <= 60)
			{
				int plantRandom = random.nextInt(3);
				this.inventory[i].stackSize += plantRandom;
			}
		}
		if (this.inventory[1] != null && this.inventory[1].stackSize <= 60)
		{
			int plantRandom = random.nextInt(3);
			this.inventory[1].stackSize += plantRandom;
		}
		else if (this.inventory[1].stackSize > 64)
		{
			ItemStack added = Util.addToRandomInventory(plant, worldObj, xCoord, yCoord, zCoord, ForgeDirection.UNKNOWN);
		}
		else
		{
			float f = worldObj.rand.nextFloat() * 0.8F + 0.1F;
			float f1 = worldObj.rand.nextFloat() * 0.8F + 0.1F;
			float f2 = worldObj.rand.nextFloat() * 0.8F + 0.1F;
			
			EntityItem entityitem = new EntityItem(worldObj, xCoord + f, yCoord + f1 + 0.5F, zCoord + f2, plant);
			
			entityitem.delayBeforeCanPickup = 10;
			
			float f3 = 0.05F;
			entityitem.motionX = (float) worldObj.rand.nextGaussian() * f3;
			entityitem.motionY = (float) worldObj.rand.nextGaussian() * f3 + 1.0F;
			entityitem.motionZ = (float) worldObj.rand.nextGaussian() * f3;
			worldObj.spawnEntityInWorld(entityitem);
		}
	}
	
	@Override
	public String getInvName()
	{
		return "Farmer";
	}
}
