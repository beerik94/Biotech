package biotech.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricityDisplay;
import biotech.Biotech;
import biotech.container.BioRefineryContainer;
import biotech.tileentity.BioRefineryTileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BioRefineryGui extends GuiContainer
{
	private BioRefineryTileEntity	tileEntity;
	
	private int						containerWidth;
	private int						containerHeight;
	
	public BioRefineryGui(InventoryPlayer playerInventory, BioRefineryTileEntity tileEntity)
	{
		super(new BioRefineryContainer(playerInventory, tileEntity));
		
		this.tileEntity = tileEntity;
	}
	
	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), 57, 4, 4210752);
		
		String displayText = "";
		
		if (this.tileEntity.isDisabled())
		{
			displayText = "Disabled!";
		}
		else if (this.tileEntity.checkRedstone())
		{
			displayText = "Working";
		}
		else
		{
			displayText = "Idle";
		}
		
		this.fontRenderer.drawString("Status: " + displayText, 28, 22, 0x00CD00);
		this.fontRenderer.drawString("Milk: " + this.tileEntity.getMilkStored() + "/" + this.tileEntity.getMaxMilk(), 28, 32, 0x00CD00);
		if (this.tileEntity.checkRedstone())
		{
			this.fontRenderer.drawString("Refining: " + this.tileEntity.working + "%", 28, 42, 0x00CD00);
		}
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}
	
	/**
	 * Draw the background layer for the GuiContainer (everything behind the
	 * items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.renderEngine.func_98187_b(this.getTexture());
		
		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;
		
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, xSize, ySize);
		
		int scale = (int) (((double) this.tileEntity.getMilkStored() / this.tileEntity.getMaxMilk()) * 100);
		
		this.drawTexturedModalRect(containerWidth + 108, containerHeight + 71 - scale, 176, 50 - scale, 8, scale);
	}
	
	public static String getTexture()
	{
		return Biotech.GUI_PATH + "GUI_BioRefinery.png";
	}
	
}