package biotech.client;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;
import biotech.Biotech;
import biotech.container.CuttingMachineContainer;
import biotech.tileentity.CuttingMachineTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CuttingMachineGui extends GuiContainer {
	private CuttingMachineTileEntity tileEntity;

	public static String CUTTINGMACHINE_GUI = Biotech.FILE_PATH
			+ "GUI_CuttingMachine.png";

	private int containerWidth;
	private int containerHeight;

	public CuttingMachineGui(InventoryPlayer playerInventory,
			CuttingMachineTileEntity tileEntity) {
		super(new CuttingMachineContainer(playerInventory, tileEntity));

		this.tileEntity = tileEntity;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		this.fontRenderer.drawString(this.tileEntity.getInvName(), 60, 6,
				4210752);

		String displayText = "";

		if (this.tileEntity.isDisabled()) {
			displayText = "Disabled!";
		} else if (this.tileEntity.hasRedstone) {
			displayText = "Working";
		} else {
			displayText = "Idle";
		}

		this.fontRenderer
				.drawString("Status: " + displayText, 32, 17, 0x00CD00);
		this.fontRenderer.drawString(
				StatCollector.translateToLocal("container.inventory"), 8,
				this.ySize - 96 + 2, 4210752);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the
	 * items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int picture = mc.renderEngine.getTexture(this.CUTTINGMACHINE_GUI);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.renderEngine.bindTexture(picture);

		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0,
				xSize, ySize);
	}
}