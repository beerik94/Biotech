package biotech.block;

import net.minecraft.block.BlockStationary;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.liquids.ILiquid;
import biotech.Biotech;

public class MilkStillBlock extends BlockStationary implements ILiquid {

	public MilkStillBlock(int id, int textureIndex) {
		super(id, Material.water);
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