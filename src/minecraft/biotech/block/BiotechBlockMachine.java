package biotech.block;

import java.util.List;
import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.BlockMachine;
import biotech.Biotech;
import biotech.tileentity.BasicMachineTileEntity;
import biotech.tileentity.BioRefineryTileEntity;
import biotech.tileentity.MilkingMachineTileEntity;
import biotech.tileentity.MilkingManagerTileEntity;
import biotech.tileentity.PlantingMachineTileEntity;
import biotech.tileentity.TillingMachineTileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BiotechBlockMachine extends BlockMachine
{
	//0 == Tiller
	//1 == Planter
	//2 == Woodcutter
	//3 == Crop Harvester
	//4 == Fertilizer
	//5 == Miner
	//6 == Filler
	//7 == Milk Manager
	//8 == Milking Machine
	//9 == BioFuel Maker
	
	public static final int TILLER_METADATA = 0;
	public static final int PLANTER_METADATA = 1;
	public static final int WOODCUTTER_METADATA = 2;
	public static final int CROPPER_METADATA = 3;
	public static final int FERTILIZER_METADATA = 4;
	public static final int MINER_METADATA = 5;
	public static final int FILLER_METADATA = 6;
	public static final int MILK_MANAGER_METADATA = 7;
	public static final int MILK_MACHINE_METADATA = 8;
	public static final int BIO_REFINERY_METADATA = 9;
	
	
    public BiotechBlockMachine(int id, int textureIndex)
    {
        super("BiotechBlockMachine", id, UniversalElectricity.machine, Biotech.tabBiotech);
        
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
      	if(meta == 0)
    	{
      		switch(side)
      		{
      		case 0:
      			return 3;
      		case 1:
      			return 18;
      		case 2:
      			return 32;
      		case 3:
      			return 0;
      		default:
      			return 3;
      		}
    	}
    	else if(meta == 1)
    	{
      		switch(side)
      		{
      		case 0:
      			return 3;
      		case 1:
      			return 18;
      		case 2:
      			return 32;
      		case 3:
      			return 0;
      		default:
      			return 3;
      		}
    	}
    	else if(meta == 2)
    	{
      		switch(side)
      		{
      		case 0:
      			return 3;
      		case 1:
      			return 18;
      		case 2:
      			return 32;
      		case 3:
      			return 0;
      		default:
      			return 3;
      		}
    	}
    	else if(meta == 3)
    	{
      		switch(side)
      		{
      		case 0:
      			return 3;
      		case 1:
      			return 18;
      		case 2:
      			return 32;
      		case 3:
      			return 0;
      		default:
      			return 3;
      		}
    	}
    	else if(meta == 4)
    	{
      		switch(side)
      		{
      		case 0:
      			return 3;
      		case 1:
      			return 18;
      		case 2:
      			return 32;
      		case 3:
      			return 0;
      		default:
      			return 3;
      		}
    	}
    	else if(meta == 5)
    	{
      		switch(side)
      		{
      		case 0:
      			return 3;
      		case 1:
      			return 18;
      		case 2:
      			return 32;
      		case 3:
      			return 0;
      		default:
      			return 3;
      		}
    	}
    	else if(meta == 6)
    	{
      		switch(side)
      		{
      		case 0:
      			return 3;
      		case 1:
      			return 18;
      		case 2:
      			return 32;
      		case 3:
      			return 0;
      		default:
      			return 3;
      		}
    	}
    	else if(meta == 7)
    	{
    		switch(side)
    		{
    		case 0:
    			return 3;
    		case 1:
    			return 18;
    		case 2:
    			return 32;
    		case 3:
    			return 3;
    		default:
    			return 3;
    		}
    	}
    	else if(meta == 8)
    	{
    		switch(side)
    		{
    		case 0:
    			return 3;
    		case 1:
    			return 18;
    		case 2:
    			return 32;
    		case 3:
    			return 3;
    		default:
    			return 3;
    		}
    	}
    	else if(meta == 9)
    	{
    		switch(side)
      		{
      		case 0:
      			return 3;
      		case 1:
      			return 18;
      		case 2:
      			return 32;
      		case 3:
      			return 0;
      		default:
      			return 3;
      		}
    	}
    	else 
    	{
    		return 3;
    	}
    }

	@Override
    @SideOnly(Side.CLIENT)
    public int getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
    {
    	int metadata = world.getBlockMetadata(x, y, z);
    	
    	BasicMachineTileEntity tileEntity = (BasicMachineTileEntity)world.getBlockTileEntity(x, y, z);

    	//System.out.println("getBlockTexture: " + tileEntity.getFacing());
    	
		int front = 3;
		int back = 2;
		int left = 5;
		int right = 4;
		int top = 1;
		int bottom = 0;    	
		
    	switch(tileEntity.getFacing())
    	{
    	case 2:
    		front = 3;
    		back = 2;
    		left = 5;
    		right = 4;
    		break;
    	case 3:
    		front = 2;
    		back = 3;
    		left = 4;
    		right = 5;
    		break;
    	case 4:
    		front = 5;
    		back = 4;
    		left = 2;
    		right = 3;
    		break;
    	case 5:
    		front = 4;
    		back = 5;
    		left = 3;
    		right = 2;
    		break;
    		
    	default:

   			break;
    	}
    	
    	if(metadata == 0)
    	{
	        if(side == front)
	        {
	        	return tileEntity.isPowered ? 48 : 32;
	        }
	        else if(side == back)
	        {		
	      			return 0;
	        }
	        else if(side == bottom)
	        {
	      			return 3;
	        }
	        else if(side == top)
	        {		
	      			return 18;
	        }
	        else
	        {
	        	return 3;
	        }
    	}
    	else if(metadata == 1)
    	{
	        if(side == front)
	        {
	        	return tileEntity.isPowered ? 48 : 32;
	        }
	        else if(side == back)
	        {		
	      			return 0;
	        }
	        else if(side == bottom)
	        {
	      			return 3;
	        }
	        else if(side == top)
	        {		
	      			return 18;
	        }
	        else
	        {
	        	return 3;
	        }
    	}
    	else if(metadata == 2)
    	{
	        if(side == front)
	        {
	        	return tileEntity.isPowered ? 48 : 32;
	        }
	        else if(side == back)
	        {		
	      			return 0;
	        }
	        else if(side == bottom)
	        {
	      			return 3;
	        }
	        else if(side == top)
	        {		
	      			return 18;
	        }
	        else
	        {
	        	return 3;
	        }
    	}
    	else if(metadata == 3)
    	{
	        if(side == front)
	        {
	        	return tileEntity.isPowered ? 48 : 32;
	        }
	        else if(side == back)
	        {		
	      			return 0;
	        }
	        else if(side == bottom)
	        {
	      			return 3;
	        }
	        else if(side == top)
	        {		
	      			return 18;
	        }
	        else
	        {
	        	return 3;
	        }
    	}
    	else if(metadata == 4)
    	{
	        if(side == front)
	        {
	        	return tileEntity.isPowered ? 48 : 32;
	        }
	        else if(side == back)
	        {		
	      			return 0;
	        }
	        else if(side == bottom)
	        {
	      			return 3;
	        }
	        else if(side == top)
	        {		
	      			return 18;
	        }
	        else
	        {
	        	return 3;
	        }
    	}
    	else if(metadata == 5)
    	{
	        if(side == front)
	        {
	        	return tileEntity.isPowered ? 48 : 32;
	        }
	        else if(side == back)
	        {		
	      			return 0;
	        }
	        else if(side == bottom)
	        {
	      			return 3;
	        }
	        else if(side == top)
	        {		
	      			return 18;
	        }
	        else
	        {
	        	return 3;
	        }
    	}
    	else if(metadata == 6)
    	{
	        if(side == front)
	        {
	        	return tileEntity.isPowered ? 48 : 32;
	        }
	        else if(side == back)
	        {		
	      			return 0;
	        }
	        else if(side == bottom)
	        {
	      			return 3;
	        }
	        else if(side == top)
	        {		
	      			return 18;
	        }
	        else
	        {
	        	return 3;
	        }
    	}
    	else if(metadata == 7)
    	{
    		if(side == front)
    		{
    			return tileEntity.isPowered ? 48 : 32;
    		}
    		else if(side == back)
    		{
    			return 3;
    		}
    		else if(side == bottom)
    		{
    			return 19;
    		}
    		else if(side == top)
    		{
    			return 18;
    		}
    		else
    		{
    			return 3;
    		}
    	}
    	else if(metadata == 8)
    	{
    		if(side == front)
    		{
    			return tileEntity.isPowered ? 48 : 32;
    		}
    		else if(side == back)
    		{
    			return 3;
    		}
    		else if(side == bottom)
    		{
    			return 19;
    		}
    		else if(side == top)
    		{
    			return 18;
    		}
    		else
    		{
    			return 3;
    		}
    	}
    	else if(metadata == 9)
    	{
    		if(side == front)
    		{
    			return tileEntity.isPowered ? 48 : 32;
    		}
    		else if(side == back)
    		{
    			return 3;
    		}
    		else if(side == bottom)
    		{
    			return 19;
    		}
    		else if(side == top)
    		{
    			return 18;
    		}
    		else
    		{
    			return 3;
    		}
    	}
    	
     	else 
     	{
    		return 3;
    	}
    }
    
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

		if (tile instanceof BasicMachineTileEntity)
		{
			BasicMachineTileEntity tileEntity = (BasicMachineTileEntity) tile;
			
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
        TileEntity tile = world.getBlockTileEntity(x, y, z);

			if (tile instanceof BasicMachineTileEntity)
			{
				BasicMachineTileEntity tileEntity = (BasicMachineTileEntity) tile;
					
		        int side = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		        int change = 3;
		        
		        switch(side)
		        {
		        	case 0: change = 3; break;
		        	case 1: change = 4; break;
		        	case 2: change = 2; break;
		        	case 3: change = 5; break;
		        }
		        //System.out.println("onBlockPlacedBy: " + change);
		        
		        tileEntity.setFacing((short)change);
		}
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int i, CreativeTabs creativetabs, List list)
	{
		list.add(new ItemStack(i, 1, 0));
		list.add(new ItemStack(i, 1, 1));
		list.add(new ItemStack(i, 1, 2));
		list.add(new ItemStack(i, 1, 3));
		list.add(new ItemStack(i, 1, 4));
		list.add(new ItemStack(i, 1, 5));
		list.add(new ItemStack(i, 1, 6));
		list.add(new ItemStack(i, 1, 7));
		list.add(new ItemStack(i, 1, 8));
		list.add(new ItemStack(i, 1, 9));
	}
    
    
    @Override
    public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        int meta = world.getBlockMetadata(x, y, z);

        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (tileEntity == null || !(tileEntity instanceof BasicMachineTileEntity))
        {
            return false;
        }

        BasicMachineTileEntity basicEntity = (BasicMachineTileEntity)tileEntity;

        // Re-orient the block
        switch (basicEntity.getFacing())
        {
            case 2:
            	basicEntity.setFacing((short)5);
                break;

            case 5:
            	basicEntity.setFacing((short)3);
                break;

            case 3:
            	basicEntity.setFacing((short)4);
                break;

            case 4:
            	basicEntity.setFacing((short)2);
                break;
        }

        world.setBlockMetadataWithNotify(x, y, z, meta);
        
        basicEntity.refreshConnectorsAndWorkArea();
        
        return true;
    }

    @Override
    public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
    	int metadata = world.getBlockMetadata(x, y, z);
    	
    	TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
    	if(!world.isRemote)
    	{
	    	switch(metadata)
	    	{
	    	case 0:
	    		if(!player.isSneaking())
	    		{
	    			player.openGui(Biotech.instance, 0, world, x, y, z);
	    			return true;
	    		}
	    	case 1:
	    		if(!player.isSneaking())
	    		{
	    			player.openGui(Biotech.instance, 2, world, x, y, z);
	    			return true;
	    		}
	    	case 2:
	    	case 3:
	    	case 4:
	    	case 5:
	    	case 6:
	    	case 7:
	    		if(!player.isSneaking())
				{
					player.openGui(Biotech.instance, 1, world, x, y, z);
					return true;
				}
	    	case 8:
	    		if(!player.isSneaking())
	    		{
	    			player.openGui(Biotech.instance, 3, world, x, y, z);
	    			return true;
	    		}
	    	case 9:
	    		if(!player.isSneaking())
	    		{
	    			player.openGui(Biotech.instance, 4, world, x, y, z);
	    			return true;	    			
	    		}
	    	default:
	    		if(!player.isSneaking())
	    		{
	    			player.openGui(Biotech.instance, 0, world, x, y, z);
	    			return true;
	    		}
	    	}
    	}
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
    	switch(metadata)
    	{
    	case 0:
    		return new TillingMachineTileEntity();
    	case 1:
    		return new PlantingMachineTileEntity();
    	case 2:
    	case 3:
    	case 4:
    	case 5:
    	case 6:
    	case 7:
    		return new MilkingManagerTileEntity();
    	case 8:
    		return new MilkingMachineTileEntity();
    	case 9:
    		return new BioRefineryTileEntity();
    	default:
    		return new BasicMachineTileEntity();
    	}
    }
}
