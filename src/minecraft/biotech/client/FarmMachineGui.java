package biotech.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import biotech.Biotech;
import biotech.container.FarmMachineContainer;
import biotech.container.PlantingMachineContainer;
import biotech.tileentity.FarmMachineTileEntity;
import biotech.tileentity.PlantingMachineTileEntity;

public class FarmMachineGui extends GuiContainer
{
	private FarmMachineTileEntity	tileEntity;
	
	public static String			GUI	= Biotech.FILE_PATH + "farmmachine.png";
	
	private int						containerWidth;
	private int						containerHeight;
	
	public FarmMachineGui(InventoryPlayer playerInventory, FarmMachineTileEntity machine)
	{
		super(new FarmMachineContainer(playerInventory, machine));
		
		this.tileEntity = machine;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), 65, 4, 4210752);
		
		String displayText = "";
		
		if (this.tileEntity.isDisabled())
		{
			displayText = "Disabled!";
		}
		else if (this.tileEntity.hasRedstone)
		{
			displayText = "Working";
		}
		else
		{
			displayText = "Idle";
		}
		
		this.fontRenderer.drawString("Status: " + displayText, 32, 18, 0x00CD00);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		int picture = mc.renderEngine.getTexture(this.GUI);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.renderEngine.func_98187_b(this.getTexture());
		
		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;
		
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, xSize, ySize);
	}
	
	public static String getTexture()
	{
		return Biotech.GUI_PATH + "GUI_FarmingMachine.png";
	}
}