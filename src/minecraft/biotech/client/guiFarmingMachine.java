package biotech.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import biotech.Biotech;
import biotech.container.containerFarmingMachine;
import biotech.tileentity.tileEntityFarmingMachine;

public class guiFarmingMachine extends GuiContainer
{
	private tileEntityFarmingMachine	tileEntity;
	private int						containerWidth;
	private int						containerHeight;
	
	public guiFarmingMachine(InventoryPlayer playerInventory, tileEntityFarmingMachine machine)
	{
		super(new containerFarmingMachine(playerInventory, machine));
		
		this.tileEntity = machine;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), 70, 4, 4210752);
		
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
		return Biotech.GUI_PATH + "GUI_FarmingMachine.png";
	}
}