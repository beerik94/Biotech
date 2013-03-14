package biotech.block;

import net.minecraft.block.BlockStationary;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.world.World;
import net.minecraftforge.liquids.ILiquid;
import biotech.Biotech;

public class MilkStillBlock extends BlockStationary implements ILiquid {

	public MilkStillBlock(int id, int textureIndex) {
		super(id, Material.water);
		setHardness(100F);
		setLightOpacity(3);
		setUnlocalizedName("Milk(Still)");
	}

	@Override
	public void func_94332_a(IconRegister par1IconRegister)
	{
		this.field_94336_cN = par1IconRegister.func_94245_a(Biotech.TEXTURE_NAME_PREFIX + "MilkStill");
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