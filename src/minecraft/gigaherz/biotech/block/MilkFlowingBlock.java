package gigaherz.biotech.block;

import gigaherz.biotech.Biotech;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;
import net.minecraftforge.liquids.ILiquid;

public class MilkFlowingBlock extends BlockFluid implements ILiquid {

	int numAdjacentSources = 0;
	boolean isOptimalFlowDirection[] = new boolean[4];
	int flowCost[] = new int[4];

	public MilkFlowingBlock(int id, int textureIndex) {
		super(id, Material.water);
		setHardness(100F);
		setLightOpacity(3);
		setBlockName("Milk(Flowing)");
		
		this.blockIndexInTexture = textureIndex;
	}

	@Override
	public String getTextureFile() {
		return Biotech.BLOCK_TEXTURE_FILE;
	}

	@Override
	public int stillLiquidId() {
		return this.blockID;
	}

	@Override
	public boolean isMetaSensitive() {
		return false;
	}

	@Override
	public int stillLiquidMeta() {
		return 0;
	}

	@Override
	public boolean isBlockReplaceable(World world, int i, int j, int k) {
		return true;
	}

}