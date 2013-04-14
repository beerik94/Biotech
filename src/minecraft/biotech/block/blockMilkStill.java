package biotech.block;

import net.minecraft.block.BlockStationary;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.world.World;
import net.minecraftforge.liquids.ILiquid;
import biotech.Biotech;

public class blockMilkStill extends BlockStationary implements ILiquid
{
	
	public blockMilkStill(int id)
	{
		super(id, Material.water);
		setHardness(100F);
		setLightOpacity(3);
		setUnlocalizedName("MilkStill");
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon(Biotech.TEXTURE_NAME_PREFIX + "MilkStill");
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