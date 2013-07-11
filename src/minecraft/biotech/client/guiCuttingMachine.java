package biotech.client;

import org.lwjgl.opengl.GL11;
import biotech.Biotech;
import biotech.container.containerCuttingMachine;
import biotech.tileentity.tileEntityCuttingMachine;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class guiCuttingMachine extends GuiContainer
{
	private tileEntityCuttingMachine	tileEntity;
	
	private int							containerWidth;
	private int							containerHeight;
	
	public guiCuttingMachine(InventoryPlayer playerInventory, tileEntityCuttingMachine tileEntity)
	{
		super(new containerCuttingMachine(playerInventory, tileEntity));
		
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
		
		if (this.tileEntity.checkRedstone())
		{
			displayText = "Working";
		}
		else
		{
			displayText = "Idle";
		}
		
		this.fontRenderer.drawString("Status: " + displayText, 28, 22, 0x00CD00);
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
		ResourceLocation rLoc = new ResourceLocation(Biotech.MOD_ID, this.getTexture());
		this.mc.renderEngine.func_110577_a(rLoc);
		
		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;
		
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, xSize, ySize);
	}
	
	public static String getTexture()
	{
		return Biotech.GUI_PATH + "GUI_CuttingMachine.png";
	}
}