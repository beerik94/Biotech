package gigaherz.biotech.client;

import gigaherz.biotech.Biotech;
import gigaherz.biotech.tileentity.MilkingMachineTileEntity;
import gigaherz.biotech.tileentity.MilkingManagerTileEntity;
import gigaherz.biotech.container.MilkingMachineContainer;
import gigaherz.biotech.container.MilkingManagerContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MilkingMachineGui extends GuiContainer
{
	private MilkingMachineTileEntity tileEntity;

	public static String MILKINGMACHINE_GUI = Biotech.FILE_PATH + "milkingmachine.png";
	
	private int containerWidth;
	private int containerHeight;

	public MilkingMachineGui(InventoryPlayer playerInventory, MilkingMachineTileEntity tileEntity)
	{
		super(new MilkingMachineContainer(playerInventory, tileEntity));
		
		this.tileEntity = tileEntity;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
    protected void drawGuiContainerForegroundLayer(int i, int j)
    {
		this.fontRenderer.drawString(this.tileEntity.getInvName(), 60, 6, 4210752);
		
		String displayText = "";

		if (this.tileEntity.isDisabled())
		{
			displayText = "Disabled!";
		}
		else if (this.tileEntity.GetRedstoneSignal())
		{
			displayText = "Working";
		}
		else
		{
			displayText = "Idle";
		}

		this.fontRenderer.drawString("Status: " + displayText, 32, 17, 0x00CD00);
		this.fontRenderer.drawString("Voltage: " + ElectricInfo.getDisplayShort(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE), 32, 27, 0x00CD00);
		this.fontRenderer.drawString("Storage: " + ElectricInfo.getDisplayShort(this.tileEntity.getElectricityStored(), ElectricUnit.JOULES), 32, 37, 0x00CD00);
		
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	@Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        int picture = mc.renderEngine.getTexture(this.MILKINGMACHINE_GUI);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(picture);
        
		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;
		
        this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, xSize, ySize);
        /*
        if (this.grinder.isPowered())
        {
            this.drawTexturedModalRect(x + 86, y + 54, 176, 0, 8, 8);
        }
         */
    }
}