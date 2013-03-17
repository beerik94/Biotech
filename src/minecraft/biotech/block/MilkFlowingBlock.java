package biotech.block;

import net.minecraft.block.BlockFluid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.world.World;
import net.minecraftforge.liquids.ILiquid;
import biotech.Biotech;

public class MilkFlowingBlock extends BlockFluid implements ILiquid
{
	
	public MilkFlowingBlock(int id)
	{
		super(id, Material.water);
		setHardness(100F);
		setLightOpacity(3);
		setUnlocalizedName("MilkMoving");
	}
	
	@Override
	public void func_94332_a(IconRegister par1IconRegister)
	{
		this.field_94336_cN = par1IconRegister.func_94245_a(Biotech.TEXTURE_NAME_PREFIX + "MilkFlow");
	}
	
	@Override
	public int stillLiquidId()
	{
		return this.blockID;
	}
	
	@Override
	public boolean isMetaSensitive()
	{
		return false;
	}
	
	@Override
	public int stillLiquidMeta()
	{
		return 0;
	}
	
	@Override
	public boolean isBlockReplaceable(World world, int i, int j, int k)
	{
		return true;
	}
	
}