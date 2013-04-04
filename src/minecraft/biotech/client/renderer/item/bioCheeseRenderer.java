package biotech.client.renderer.item;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.IItemRenderer;
import biotech.client.model.ModelCheese;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class bioCheeseRenderer extends Render implements IItemRenderer
{
	private ModelCheese	modelBioCheese	= new ModelCheese();
	
	@Override
	public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1)
	{
		
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return true;
	}
	
	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return type == ItemRenderType.ENTITY;
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		switch (type)
		{
			case ENTITY:
				RenderBlocks renderEntity = (RenderBlocks) data[0];
				EntityItem entityEntity = (EntityItem) data[1];
				
				modelBioCheese.render();
				break;
			case INVENTORY:
				RenderBlocks renderInventory = (RenderBlocks) data[0];
				break;
			case EQUIPPED:
				RenderBlocks renderEquipped = (RenderBlocks) data[0];
				EntityLiving entityEquipped = (EntityLiving) data[1];
				
				modelBioCheese.render();
				break;
			case FIRST_PERSON_MAP:
				EntityPlayer playerFirstPerson = (EntityPlayer) data[0];
				RenderEngine engineFirstPerson = (RenderEngine) data[1];
				MapData mapDataFirstPerson = (MapData) data[2];
				
				modelBioCheese.render();
				break;
			default:
		}
		
	}
	
}
