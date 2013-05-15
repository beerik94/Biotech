package biotech.entity.ai;

import biotech.entity.passive.bioOcelot;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

public class bioEntityAIOcelotSit extends EntityAIBase
{
    private final bioOcelot theOcelot;
    private final float field_75404_b;

    /** Tracks for how long the task has been executing */
    private int currentTick = 0;
    private int field_75402_d = 0;

    /** For how long the Ocelot should be sitting */
    private int maxSittingTicks = 0;

    /** X Coordinate of a nearby sitable block */
    private int sitableBlockX = 0;

    /** Y Coordinate of a nearby sitable block */
    private int sitableBlockY = 0;

    /** Z Coordinate of a nearby sitable block */
    private int sitableBlockZ = 0;

    public bioEntityAIOcelotSit(bioOcelot par1EntityOcelot, float par2)
    {
        this.theOcelot = par1EntityOcelot;
        this.field_75404_b = par2;
        this.setMutexBits(5);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        return this.theOcelot.isTamed() && !this.theOcelot.isSitting() && this.theOcelot.getRNG().nextDouble() <= 0.006500000134110451D && this.getNearbySitableBlockDistance();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return this.currentTick <= this.maxSittingTicks && this.field_75402_d <= 60 && this.isSittableBlock(this.theOcelot.worldObj, this.sitableBlockX, this.sitableBlockY, this.sitableBlockZ);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.theOcelot.getNavigator().tryMoveToXYZ((double)((float)this.sitableBlockX) + 0.5D, (double)(this.sitableBlockY + 1), (double)((float)this.sitableBlockZ) + 0.5D, this.field_75404_b);
        this.currentTick = 0;
        this.field_75402_d = 0;
        this.maxSittingTicks = this.theOcelot.getRNG().nextInt(this.theOcelot.getRNG().nextInt(1200) + 1200) + 1200;
        this.theOcelot.func_70907_r().setSitting(false);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.theOcelot.setSitting(false);
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        ++this.currentTick;
        this.theOcelot.func_70907_r().setSitting(false);

        if (this.theOcelot.getDistanceSq((double)this.sitableBlockX, (double)(this.sitableBlockY + 1), (double)this.sitableBlockZ) > 1.0D)
        {
            this.theOcelot.setSitting(false);
            this.theOcelot.getNavigator().tryMoveToXYZ((double)((float)this.sitableBlockX) + 0.5D, (double)(this.sitableBlockY + 1), (double)((float)this.sitableBlockZ) + 0.5D, this.field_75404_b);
            ++this.field_75402_d;
        }
        else if (!this.theOcelot.isSitting())
        {
            this.theOcelot.setSitting(true);
        }
        else
        {
            --this.field_75402_d;
        }
    }

    /**
     * Searches for a block to sit on within a 8 block range, returns 0 if none found
     */
    protected boolean getNearbySitableBlockDistance()
    {
        int i = (int)this.theOcelot.posY;
        double d0 = 2.147483647E9D;

        for (int j = (int)this.theOcelot.posX - 8; (double)j < this.theOcelot.posX + 8.0D; ++j)
        {
            for (int k = (int)this.theOcelot.posZ - 8; (double)k < this.theOcelot.posZ + 8.0D; ++k)
            {
                if (this.isSittableBlock(this.theOcelot.worldObj, j, i, k) && this.theOcelot.worldObj.isAirBlock(j, i + 1, k))
                {
                    double d1 = this.theOcelot.getDistanceSq((double)j, (double)i, (double)k);

                    if (d1 < d0)
                    {
                        this.sitableBlockX = j;
                        this.sitableBlockY = i;
                        this.sitableBlockZ = k;
                        d0 = d1;
                    }
                }
            }
        }

        return d0 < 2.147483647E9D;
    }

    /**
     * Determines whether the Ocelot wants to sit on the block at given coordinate
     */
    protected boolean isSittableBlock(World par1World, int par2, int par3, int par4)
    {
        int l = par1World.getBlockId(par2, par3, par4);
        int i1 = par1World.getBlockMetadata(par2, par3, par4);

        if (l == Block.chest.blockID)
        {
            TileEntityChest tileentitychest = (TileEntityChest)par1World.getBlockTileEntity(par2, par3, par4);

            if (tileentitychest.numUsingPlayers < 1)
            {
                return true;
            }
        }
        else
        {
            if (l == Block.furnaceBurning.blockID)
            {
                return true;
            }

            if (l == Block.bed.blockID && !BlockBed.isBlockHeadOfBed(i1))
            {
                return true;
            }
        }

        return false;
    }
}
