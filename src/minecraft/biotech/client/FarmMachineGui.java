package biotech.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;
import biotech.Biotech;
import biotech.container.FarmMachineContainer;
import biotech.container.PlantingMachineContainer;
import biotech.tileentity.FarmMachineTileEntity;
import biotech.tileentity.PlantingMachineTileEntity;

public class FarmMachineGui extends GuiContainer {
	private FarmMachineTileEntity tileEntity;

	public static String GUI = Biotech.FILE_PATH + "farmmachine.png";

	private int containerWidth;
	private int containerHeight;

	public FarmMachineGui(InventoryPlayer playerInventory,
			FarmMachineTileEntity machine) {
		super(new FarmMachineContainer(playerInventory, machine));

		this.tileEntity = machine;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		this.fontRenderer.drawString(this.tileEntity.getInvName(), 60, 6,
				4210752);

		String displayText = "";

		if (this.tileEntity.isDisabled()) {
			displayText = "Disabled!";
		} else if (this.tileEntity.isPowered) {
			displayText = "Working";
		} else {
			displayText = "Idle";
		}

		this.fontRenderer
				.drawString("Status: " + displayText, 32, 18, 0x00CD00);
		this.fontRenderer.drawString(
				"Voltage: "
						+ ElectricInfo.getDisplayShort(
								this.tileEntity.getVoltage(),
								ElectricUnit.VOLTAGE), 32, 28, 0x00CD00);
		this.fontRenderer.drawString(
				"Storage: "
						+ ElectricInfo.getDisplayShort(
								this.tileEntity.getElectricityStored(),
								ElectricUnit.JOULES), 32, 38, 0x00CD00);

		this.fontRenderer.drawString(
				StatCollector.translateToLocal("container.inventory"), 8,
				this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int picture = mc.renderEngine.getTexture(this.GUI);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.renderEngine.bindTexture(picture);

		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0,
				xSize, ySize);

		int scale = (int) (((double) this.tileEntity.getElectricityStored() / (double) this.tileEntity
				.getMaxElectricity()) * 52);

		this.drawTexturedModalRect(containerWidth + 165, containerHeight + 17
				+ 52 - scale, 176, 52 - scale, 4, scale);
	}
}