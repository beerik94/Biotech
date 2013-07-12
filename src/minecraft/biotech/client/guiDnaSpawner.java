package biotech.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import biotech.Biotech;
import biotech.container.containerDnaSpawner;
import biotech.tileentity.tileEntityDnaSpawner;

public class guiDnaSpawner extends GuiContainer
{
	private tileEntityDnaSpawner	tileEntity;
	
	private int						containerWidth;
	private int						containerHeight;
	
	public guiDnaSpawner(InventoryPlayer playerInventory, tileEntityDnaSpawner tileEntity)
	{
		super(new containerDnaSpawner(playerInventory, tileEntity));
		
		this.tileEntity = tileEntity;
	}
	
	public void initGui()
	{
		super.initGui();
		
		int cornerX = (this.width - this.xSize) / 2;
		int cornerY = (this.height - this.ySize) / 2;
		buttonList.add(new GuiButton(0, cornerX + this.xSize, cornerY + 18, 10, 10, "+"));
		buttonList.add(new GuiButton(1, cornerX + this.xSize + 12, cornerY + 18, 10, 10, "-"));
		buttonList.add(new GuiButton(2, cornerX + this.xSize, cornerY + 30, 10, 10, "+"));
		buttonList.add(new GuiButton(3, cornerX + this.xSize + 12, cornerY + 30, 10, 10, "-"));
		buttonList.add(new GuiButton(4, cornerX + this.xSize, cornerY + 42, 10, 10, "+"));
		buttonList.add(new GuiButton(5, cornerX + this.xSize + 12, cornerY + 42, 10, 10, "-"));
		buttonList.add(new GuiButton(6, cornerX + this.xSize, cornerY + 54, 10, 10, "+"));
		buttonList.add(new GuiButton(7, cornerX + this.xSize + 12, cornerY + 54, 10, 10, "-"));
		buttonList.add(new GuiButton(8, cornerX + this.xSize, cornerY + 66, 10, 10, "+"));
		buttonList.add(new GuiButton(9, cornerX + this.xSize + 12, cornerY + 66, 10, 10, "-"));
		buttonList.add(new GuiButton(10, cornerX + 45, cornerY + 60, 40, 15, "Spawn Mob"));
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
		this.fontRenderer.drawString("Health: " + tileEntity.health, 108, 22, 4210752);
		this.fontRenderer.drawString("Width: " + tileEntity.width, 108, 34, 4210752);
		this.fontRenderer.drawString("Height: " + tileEntity.height, 108, 46, 4210752);
		this.fontRenderer.drawString("Drops: " + tileEntity.drops, 108, 58, 4210752);
		this.fontRenderer.drawString("Exp: " + tileEntity.EV, 108, 70, 4210752);
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
		this.mc.renderEngine.bindTexture(this.getTexture());
		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, xSize, ySize);
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		switch (button.id)
		{
			case 0:
				tileEntity.health += 1;
				break;
			case 1:
				tileEntity.health -= 1;
				break;
			case 2:
				tileEntity.width += 0.1;
				break;
			case 3:
				tileEntity.width -= 0.1;
				break;
			case 4:
				tileEntity.height += 0.1;
				break;
			case 5:
				tileEntity.height -= 0.1;
				break;
			case 6:
				tileEntity.drops += 1;
				break;
			case 7:
				tileEntity.drops -= 1;
				break;
			case 8:
				tileEntity.EV += 1;
				break;
			case 9:
				tileEntity.EV -= 1;
				break;
			case 10:
				tileEntity.buttonSpawn = true;
				System.out.println("ButtonPressed");
				break;
		}
	}
	
	public static String getTexture()
	{
		return Biotech.GUI_PATH + "GUI_DnaSpawner.png";
	}
}
