package gigaherz.biotech.block;

import gigaherz.biotech.Biotech;
import gigaherz.biotech.common.CommonProxy;
import gigaherz.biotech.tileentity.BasicWorkerTileEntity;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.BlockMachine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BasicWorkerBlockMachine extends BlockMachine
{

	//METADATA: & 8 = powered
	
    public BasicWorkerBlockMachine(int id, int textureIndex)
    {
        super("btMachine", id, UniversalElectricity.machine, Biotech.tabBiotech);
        
        this.blockIndexInTexture = textureIndex;
    }

    @Override
    public String getTextureFile()
    {
        return Biotech.BLOCK_TEXTURE_FILE;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta)
    {
        int sideTexture1 = (meta & 8) != 0 ? 17 : 1;
        int sideTexture2 = (meta & 8) != 0 ? 48 : 32;
        int sideTexture3 = 18;
        int meta2 = 0;

        switch (meta)
        {
            case 2:
                meta2 = 3;
                break;

            case 3:
                meta2 = 2;
                break;

            case 5:
                meta2 = 4;
                break;

            case 4:
                meta2 = 5;
                break;
        }

        switch (side)
        {
            case 0: // bottom
                return 3;

            case 1: // top
                return 3;

            default: // sides
                if (meta2 == side)
                {
                    return sideTexture3;
                }

                return (meta & 7) == side ? sideTexture2 : sideTexture1;
        }
    }

	@Override
    @SideOnly(Side.CLIENT)
    public int getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
    {
    	int metadata = world.getBlockMetadata(x, y, z);
    	
    	BasicWorkerTileEntity tileEntity = (BasicWorkerTileEntity)world.getBlockTileEntity(x, y, z);
        
        int sideTexture1 = 0;
        int sideTexture2 = 0;
        int sideTexture3 = 0;
    	
    	if(tileEntity.isPowered)
    	{
    		sideTexture1 = 17;
    		sideTexture2 = 48;
    		sideTexture3 = 18;
    	}
    	else
    	{
    		sideTexture1 = 1;
    		sideTexture2 = 32;
    		sideTexture3 = 18;		
    		
    	}

    	int meta2 = 0;

        switch (metadata)
        {
            case 2:
                meta2 = 3;
                break;

            case 3:
                meta2 = 2;
                break;

            case 5:
                meta2 = 4;
                break;

            case 4:
                meta2 = 5;
                break;
        }

        switch (side)
        {
            case 0: // bottom
                return 3;

            case 1: // top
                return 3;

            default: // sides
                if (meta2 == side)
                {
                    return sideTexture3;
                }

                return (metadata) == side ? sideTexture2 : sideTexture1;
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

		if (tile instanceof BasicWorkerTileEntity)
		{
			BasicWorkerTileEntity tileEntity = (BasicWorkerTileEntity) tile;
			
	        if (tileEntity.isPowered)
	        {
	            float sx = (float)x + 0.5F;
	            float sy = (float)y + 0.0F + random.nextFloat() * 6.0F / 16.0F;
	            float sz = (float)z + 0.5F;
	            float o1 = 0.52F;
	            float o2 = random.nextFloat() * 0.6F - 0.3F;
	            world.spawnParticle("reddust", (double)(sx - o1), (double)sy, (double)(sz + o2), 0.0D, 0.0D, 0.0D);
	            world.spawnParticle("reddust", (double)(sx + o1), (double)sy, (double)(sz + o2), 0.0D, 0.0D, 0.0D);
	            world.spawnParticle("reddust", (double)(sx + o2), (double)sy, (double)(sz - o1), 0.0D, 0.0D, 0.0D);
	            world.spawnParticle("reddust", (double)(sx + o2), (double)sy, (double)(sz + o1), 0.0D, 0.0D, 0.0D);
	        }
		}
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity)
    {
        int angle = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        switch (angle)
        {
            case 0:
                world.setBlockMetadataWithNotify(x, y, z, 2);
                break;

            case 1:
                world.setBlockMetadataWithNotify(x, y, z, 5);
                break;

            case 2:
                world.setBlockMetadataWithNotify(x, y, z, 3);
                break;

            case 3:
                world.setBlockMetadataWithNotify(x, y, z, 4);
                break;
        }
    }

    @Override
    public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        int meta = world.getBlockMetadata(x, y, z);
        int orientation = meta & 7;
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (tileEntity == null || !(tileEntity instanceof BasicWorkerTileEntity))
        {
            return false;
        }

        BasicWorkerTileEntity worker = (BasicWorkerTileEntity)tileEntity;

        // Re-orient the block
        switch (orientation)
        {
            case 2:
                orientation = 4;
                break;

            case 5:
                orientation = 2;
                break;

            case 3:
                orientation = 5;
                break;

            case 4:
                orientation = 3;
                break;
        }

        meta = meta & 8 | orientation;
        world.setBlockMetadataWithNotify(x, y, z, meta);
        worker.refreshConnectorsAndWorkArea();
        return true;
    }

    @Override
    public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (tileEntity == null || player.isSneaking())
        {
            return false;
        }

        player.openGui(Biotech.instance, 0, world, x, y, z);
        
        return true;
    }



    @Override
    public boolean hasTileEntity(int metadata)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new BasicWorkerTileEntity();
    }
}
