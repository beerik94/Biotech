package biotech.client.renderer.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import biotech.client.model.ModelCheese;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class bioCheeseRenderer extends ItemRenderer
{
	private ModelCheese modelBioCheese = new ModelCheese();

	public bioCheeseRenderer(Minecraft par1Minecraft)
	{
		super(par1Minecraft);

	}
	
	@Override
	public void renderItem(EntityLiving par1EntityLiving, ItemStack par2ItemStack, int par3)
	{
		
	}
	
}
