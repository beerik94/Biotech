package gigaherz.biotech.client;

import gigaherz.biotech.Biotech;
import gigaherz.biotech.tileentity.CowMilkerTileEntity;
import gigaherz.biotech.container.CowMilkerContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CowMilkerGui extends GuiContainer
{
	private CowMilkerTileEntity tileEntity;

	public static String COWMILKER_GUI = Biotech.FILE_PATH + "cowmilker.png";
	
	private int containerWidth;
	private int containerHeight;

	public CowMilkerGui(InventoryPlayer inventory, CowMilkerTileEntity tileEntity)
	{
		super(new CowMilkerContainer(inventory, tileEntity));
		
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
		else if (this.tileEntity.isPowered)
		{
			displayText = "Working";
		}
		else
		{
			displayText = "Idle";
		}

		this.fontRenderer.drawString("Status: " + displayText, 32, 18, 0x00CD00);
		this.fontRenderer.drawString("Voltage: " + ElectricInfo.getDisplayShort(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE), 32, 28, 0x00CD00);
		this.fontRenderer.drawString("Storage: " + ElectricInfo.getDisplayShort(this.tileEntity.getElectricityStored(), ElectricUnit.JOULES), 32, 38, 0x00CD00);

		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	@Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        int picture = mc.renderEngine.getTexture(this.COWMILKER_GUI);
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
		int scale = (int) ((this.tileEntity.milkStored / this.tileEntity.milkMaxStored) * 51);
		
		//Biotech.biotechLogger.info(Double.toString(this.tileEntity.getElectricityStored()));
		
		this.drawTexturedModalRect(containerWidth + 125, containerHeight + 15 + 51 - scale, 176, 51 - scale, 8, scale);
    }
}