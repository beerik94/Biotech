package biotech.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import biotech.Biotech;

public class blockMilkFluid extends BlockFluidFinite
{
	public blockMilkFluid(int id, Fluid fluid, Material material)
	{
		super(id, fluid, material);
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon(Biotech.TEXTURE_NAME_PREFIX + "MilkFlow");
	}
	
	@Override
	public boolean isBlockReplaceable(World world, int i, int j, int k)
	{
		return true;
	}

	@Override
	public Fluid getFluid()
	{
		return null;
	}

	@Override
	public FluidStack drain(World world, int x, int y, int z, boolean doDrain)
	{
		return null;
	}

	@Override
	public boolean canDrain(World world, int x, int y, int z)
	{
		return true;
	}
	
}