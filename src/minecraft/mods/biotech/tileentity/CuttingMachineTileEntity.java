package mods.biotech.tileentity;

import mods.biotech.helpers.Util;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.prefab.network.IPacketReceiver;

public class CuttingMachineTileEntity extends BasicMachineTileEntity implements IPacketReceiver
{
	// Watts being used per cut
	public static final double	WATTS_PER_CUT	= 700;
	private int					treeBlocks		= 2;
	private int					leavesCut		= 0;
	
	public CuttingMachineTileEntity()
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
				if (this.ticks % 10 == 0 && this.wattsReceived >= WATTS_PER_CUT)
				{
					GetTree();
				}
				if(this.leavesCut == 1 && this.ticks % 10 == 0)
				{
					Replant();
					RemoveLeaves();
					this.leavesCut = 0;
				}
			}
		}
	}
	
	// TODO Maybe add this feature in the future
	// For now only the tree that is 2 blocks(and all the other blocks till the
	// tree ends) above the woodcutter gets detected
	/**
	 * Check for Trees
	 */
	public void GetTree()
	{
		/*
		 * int XPos = this.xCoord; int ZPos = this.zCoord; switch
		 * (this.getFacing()) { case 2: // North for (int i = 1; i < GetRange();
		 * i++) { ZPos = this.zCoord + i; } break; case 3: // South for (int i =
		 * 1; i < GetRange(); i++) { ZPos = this.zCoord - i; } break; case 4: //
		 * West for (int i = 1; i < GetRange(); i++) { XPos = this.xCoord + i; }
		 * break; case 5: // East for (int i = 1; i < GetRange(); i++) { XPos =
		 * this.xCoord - i; } break; } int bottomBlock =
		 * worldObj.getBlockId(XPos, this.yCoord, ZPos); int YPos = this.yCoord
		 * + 1; while (bottomBlock == Block.wood.blockID) { int otherBlock =
		 * worldObj.getBlockId(XPos, YPos, ZPos); if (otherBlock ==
		 * Block.wood.blockID) { YPos += 1; } else { DoCut(XPos, YPos - 1,
		 * ZPos); return; } }
		 */
		
		if (worldObj.getBlockId(this.xCoord, this.yCoord + treeBlocks, this.zCoord) == Block.wood.blockID)
		{
			DoCut(this.xCoord, this.yCoord + treeBlocks, this.zCoord, true);
			InvAdd(true);
			treeBlocks++;
		}
		if (worldObj.getBlockId(this.xCoord, this.yCoord + treeBlocks, this.zCoord) != Block.wood.blockID && this.wattsReceived >= WATTS_PER_CUT)
		{
			leavesCut++;
			treeBlocks = 2;
		}
	}
	
	/**
	 * Adds stuff to inventory
	 * 
	 * @param add
	 *            if true adds stuff / if false removes stuff
	 */
	public void InvAdd(boolean add)
	{
		ItemStack woodStack = new ItemStack(Block.wood, 2, 0);
		
		for (int i = 1; i < 7; i++)
		{
			if (this.inventory[i] != null && this.inventory[i].stackSize == 64)
			{
				Util.addToRandomInventory(woodStack, worldObj, xCoord, yCoord, zCoord, ForgeDirection.UNKNOWN);
			}
			else if (this.inventory[i] == null)
			{
				this.inventory[i] = (woodStack);
				return;
			}
			else if (this.inventory[i].stackSize < 63)
			{
				this.inventory[i].stackSize += 2;
				return;
			}
		}
	}
	
	/**
	 * The cutting function
	 * 
	 * @param x
	 *            The X position of the block
	 * @param y
	 *            The Y position of the block
	 * @param z
	 *            The Z position of the block
	 * @param wood
	 *            True for wood / False for leaves
	 */
	public void DoCut(int x, int y, int z, boolean wood)
	{
		worldObj.setBlock(x, y, z, 0, 0, 2);
		if (wood)
		{
			this.wattsReceived = Math.max(this.wattsReceived - WATTS_PER_CUT / 4, 0);
		}
	}
	
	/**
	 * The replanting of saplings
	 */
	public void Replant()
	{
		if (worldObj.getBlockId(this.xCoord, this.yCoord + 2, this.zCoord) == 0)
		{
			worldObj.setBlock(this.xCoord, this.yCoord + 2, this.zCoord, Block.sapling.blockID, 0, 2);
		}
	}
	
	/**
	 * Removes leaves that are left behind.
	 */
	public void RemoveLeaves()
	{
		int xminrange = xCoord - 8;
		int xmaxrange = xCoord + 8;
		int yminrange = yCoord + 3;
		int ymaxrange = yCoord + 25;
		int zminrange = zCoord - 8;
		int zmaxrange = zCoord + 8;
		
		for (int xx = xminrange; xx <= xmaxrange; xx++)
		{
			for (int yy = yminrange; yy <= ymaxrange; yy++)
			{
				for (int zz = zminrange; zz <= zmaxrange; zz++)
				{
					DoCut(xx, yy, zz, false);
				}
			}
		}
	}
	
	// TODO This belongs to the future feature.
	/*
	 * /**
	 * Calculates the range
	 * 
	 * public int GetRange()
	 * {
	 * if (inventory[1] != null)
	 * {
	 * if (inventory[1].isItemEqual(Biotech.RangeUpgrade))
	 * {
	 * return (inventory[1].stackSize * 2 + 2);
	 * }
	 * }
	 * return 2;
	 * }
	 */
	@Override
	public String getInvName()
	{
		return "Wood Cutter";
	}
}