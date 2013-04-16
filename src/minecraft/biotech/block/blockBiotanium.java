package biotech.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import biotech.Biotech;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class blockBiotanium extends Block
{
	
	public blockBiotanium(int id)
	{
		super(id, Material.rock);
		setHardness(4F);
		setResistance(6F);
		setCreativeTab(Biotech.tabBiotech);
		setUnlocalizedName("Biotanium");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		this.blockIcon = register.registerIcon(Biotech.TEXTURE_NAME_PREFIX + "BiotaniumOre");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta)
	{
		return this.blockIcon;
	}
}
