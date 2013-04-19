package biotech.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import biotech.Biotech;
import biotech.tileentity.tileEntityBasicMachine;
import biotech.tileentity.tileEntityBioRefinery;
import biotech.tileentity.tileEntityCowMilker;
import biotech.tileentity.tileEntityCuttingMachine;
import biotech.tileentity.tileEntityFarmingMachine;
import biotech.tileentity.tileEntityFertilizer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class blockBiotechMachine extends BlockContainer
{
	// 0 == Farm
	// 1 == Woodcutter
	// 2 == Fertilizer
	// 3 == Miner
	// 4 == Cow Milker
	// 5 == BioRefinery
	
	public static final int	FARM_METADATA			= 0;
	public static final int	WOODCUTTER_METADATA		= 1;
	public static final int	FERTILIZER_METADATA		= 2;
	//public static final int	MINER_METADATA			= 3;
	public static final int	COW_MILKER_METADATA		= 4;
	public static final int	BIO_REFINERY_METADATA	= 5;
	
	// Front Sides
	private Icon			iconFarmer;
	private Icon			iconWoodcutter;
	private Icon			iconFertilizer;
	//private Icon			iconMiner;
	private Icon			iconCowMilker;
	private Icon			iconBioRefinery;
	
	// Other Sides
	private Icon			iconEmptySide;
	private Icon			iconMilkSide;
	private Icon			iconInputOn;
	private Icon			iconInputOff;
	private Icon			iconOutputOn;
	private Icon			iconOutputOff;
	
	public blockBiotechMachine(int id, int meta)
	{
		super(id, Material.iron);
		this.setUnlocalizedName("BioBlock");
		this.setCreativeTab(Biotech.tabBiotech);
		this.setStepSound(soundMetalFootstep);
		this.setHardness(0.5F);
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon(Biotech.TEXTURE_NAME_PREFIX + "MachineEmptySide");
		this.iconFarmer = par1IconRegister.registerIcon(Biotech.TEXTURE_NAME_PREFIX + "MachineFarmer");
		this.iconWoodcutter = par1IconRegister.registerIcon(Biotech.TEXTURE_NAME_PREFIX + "MachineWoodCutter");
		this.iconFertilizer = par1IconRegister.registerIcon(Biotech.TEXTURE_NAME_PREFIX + "MachineFertilizer");
		//this.iconMiner = par1IconRegister.registerIcon(Biotech.TEXTURE_NAME_PREFIX + "MachineMiner");
		this.iconCowMilker = par1IconRegister.registerIcon(Biotech.TEXTURE_NAME_PREFIX + "MachineCowMilker");
		this.iconBioRefinery = par1IconRegister.registerIcon(Biotech.TEXTURE_NAME_PREFIX + "MachineRefinery");
		this.iconEmptySide = par1IconRegister.registerIcon(Biotech.TEXTURE_NAME_PREFIX + "MachineEmptySide");
		this.iconMilkSide = par1IconRegister.registerIcon(Biotech.TEXTURE_NAME_PREFIX + "MachineMilkSide");
		this.iconInputOn = par1IconRegister.registerIcon(Biotech.TEXTURE_NAME_PREFIX + "MachineRedOnSide");
		this.iconInputOff = par1IconRegister.registerIcon(Biotech.TEXTURE_NAME_PREFIX + "MachineRedOffSide");
		this.iconOutputOn = par1IconRegister.registerIcon(Biotech.TEXTURE_NAME_PREFIX + "MachineBlackOnSide");
		this.iconOutputOff = par1IconRegister.registerIcon(Biotech.TEXTURE_NAME_PREFIX + "MachineBlackOffSide");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta)
	{
		
		if (meta == 0)
		{
			switch (side)
			{
				case 3:
					return this.iconFarmer;
				case 2:
					return this.iconInputOff;
				default:
					return this.iconEmptySide;
			}
		}
		
		else if (meta == 1)
		{
			switch (side)
			{
				case 3:
					return this.iconWoodcutter;
				case 2:
					return this.iconInputOff;
				default:
					return this.iconEmptySide;
			}
		}
		else if (meta == 2)
		{
			switch (side)
			{
				case 3:
					return this.iconFertilizer;
				case 2:
					return this.iconInputOff;
				default:
					return this.iconEmptySide;
			}
		}
		/*
		else if (meta == 3)
		{
			switch (side)
			{
				case 3:
					return this.iconMiner;
				case 2:
					return this.iconInputOff;
				default:
					return this.iconEmptySide;
			}
		}*/
		else if (meta == 4)
		{
			switch (side)
			{
				case 3:
					return this.iconCowMilker;
				case 2:
					return this.iconInputOff;
				default:
					return this.iconEmptySide;
			}
		}
		else if (meta == 5)
		{
			switch (side)
			{
				case 3:
					return this.iconBioRefinery;
				case 2:
					return this.iconInputOff;
				default:
					return this.iconEmptySide;
			}
		}
		else
		{
			return this.iconEmptySide;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
	{
		int metadata = world.getBlockMetadata(x, y, z);
		
		tileEntityBasicMachine tileEntity = (tileEntityBasicMachine) world.getBlockTileEntity(x, y, z);
		
		int front = 3;
		int back = 2;
		int left = 5;
		int right = 4;
		int top = 1;
		int bottom = 0;
		
		switch (tileEntity.getFacing())
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
		}
		
		if (metadata == 0)
		{
			if (side == front)
			{
				return tileEntity.isPowered ? this.iconInputOn : this.iconInputOff;
			}
			else if (side == back)
			{
				return this.iconFarmer;
			}
		}
		else if (metadata == 1)
		{
			if (side == front)
			{
				return tileEntity.isPowered ? this.iconInputOn : this.iconInputOff;
			}
			else if (side == back)
			{
				return this.iconWoodcutter;
			}
		}
		else if (metadata == 2)
		{
			if (side == front)
			{
				return tileEntity.isPowered ? this.iconInputOn : this.iconInputOff;
			}
			else if (side == back)
			{
				return this.iconFertilizer;
			}
		}
		/*
		else if (metadata == 3)
		{
			if (side == front)
			{
				return tileEntity.isPowered ? this.iconInputOn : this.iconInputOff;
			}
			else if (side == back)
			{
				return this.iconMiner;
			}
		}*/
		else if (metadata == 4)
		{
			if (side == front)
			{
				return tileEntity.isPowered ? this.iconInputOn : this.iconInputOff;
			}
			else if (side == back)
			{
				return this.iconCowMilker;
			}
			else if (side == bottom)
			{
				return this.iconMilkSide;
			}
		}
		else if (metadata == 5)
		{
			if (side == front)
			{
				return tileEntity.isPowered ? this.iconInputOn : this.iconInputOff;
			}
			else if (side == back)
			{
				return this.iconBioRefinery;
			}
			else if (side == bottom)
			{
				return this.iconMilkSide;
			}
		}
		else
		{
			return this.iconEmptySide;
		}
		return this.iconEmptySide;
	}
	
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		
		if (tile instanceof tileEntityBasicMachine)
		{
			tileEntityBasicMachine tileEntity = (tileEntityBasicMachine) tile;
			
			if (tileEntity.isPowered)
			{
				float sx = (float) x + 0.5F;
				float sy = (float) y + 0.0F + random.nextFloat() * 6.0F / 16.0F;
				float sz = (float) z + 0.5F;
				float o1 = 0.52F;
				float o2 = random.nextFloat() * 0.6F - 0.3F;
				world.spawnParticle("reddust", (double) (sx - o1), (double) sy, (double) (sz + o2), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("reddust", (double) (sx + o1), (double) sy, (double) (sz + o2), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("reddust", (double) (sx + o2), (double) sy, (double) (sz - o1), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("reddust", (double) (sx + o2), (double) sy, (double) (sz + o1), 0.0D, 0.0D, 0.0D);
			}
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity, ItemStack itemStack)
	{
		tileEntityBasicMachine tileEntity = (tileEntityBasicMachine) world.getBlockTileEntity(x, y, z);
        int side = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int change = 3;
        
        switch(side)
        {
        	case 0: change = 2; break;
        	case 1: change = 5; break;
        	case 2: change = 3; break;
        	case 3: change = 4; break;
        }
        
        tileEntity.setFacing((short)change);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int i, CreativeTabs creativetabs, List list)
	{
		for (int var4 = 0; var4 < 6; var4++)
		{
			list.add(new ItemStack(i, 1, var4));
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = world.getBlockMetadata(x, y, z);
		
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (!world.isRemote && !player.isSneaking())
		{
			switch (metadata)
			{
				case 0:
					player.openGui(Biotech.instance, 0, world, x, y, z);
					return true;
				case 1:
					player.openGui(Biotech.instance, 3, world, x, y, z);
					return true;
				case 2:
					player.openGui(Biotech.instance, 4, world, x, y, z);
					return true;
				case 3:
				case 4:
					player.openGui(Biotech.instance, 1, world, x, y, z);
					return true;
				case 5:
					player.openGui(Biotech.instance, 2, world, x, y, z);
					return true;
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
		switch (metadata)
		{
			case 0:
				return new tileEntityFarmingMachine();
			case 1:
				return new tileEntityCuttingMachine();
			case 2:
				return new tileEntityFertilizer();
			case 3:
			case 4:
				return new tileEntityCowMilker();
			case 5:
				return new tileEntityBioRefinery();
			default:
				return new tileEntityBasicMachine();
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return null;
	}
}
