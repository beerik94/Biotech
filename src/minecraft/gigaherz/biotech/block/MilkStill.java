package gigaherz.biotech.block;

import gigaherz.biotech.Biotech;
import net.minecraft.block.BlockStationary;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;
import net.minecraftforge.liquids.ILiquid;

public class MilkStill extends BlockStationary implements ILiquid {

	public MilkStill(int id, int textureIndex) {
		super(id, Material.water);
		setCreativeTab(Biotech.tabBiotech);
		setHardness(100F);
		setLightOpacity(3);
		setBlockName("Milk(Still)");
		
		this.blockIndexInTexture = textureIndex;
	}

	@Override
	public String getTextureFile() {
		return Biotech.BLOCK_TEXTURE_FILE;
	}

	@Override
	public int stillLiquidId() {
		return Biotech.milkStill.blockID;
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