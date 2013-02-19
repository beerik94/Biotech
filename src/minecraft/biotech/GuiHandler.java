package biotech;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import biotech.client.BioRefineryGui;
import biotech.client.CowMilkerGui;
import biotech.client.CuttingMachineGui;
import biotech.client.PlantingMachineGui;
import biotech.client.TillingMachineGui;
import biotech.container.BioRefineryContainer;
import biotech.container.CowMilkerContainer;
import biotech.container.CuttingMachineContainer;
import biotech.container.PlantingMachineContainer;
import biotech.container.TillingMachineContainer;
import biotech.tileentity.BioRefineryTileEntity;
import biotech.tileentity.CowMilkerTileEntity;
import biotech.tileentity.CuttingMachineTileEntity;
import biotech.tileentity.PlantingMachineTileEntity;
import biotech.tileentity.TillingMachineTileEntity;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	public void preInit() {
		// Preload textures
		MinecraftForgeClient.preloadTexture(Biotech.BLOCK_TEXTURE_FILE);
		MinecraftForgeClient.preloadTexture(Biotech.ITEM_TEXTURE_FILE);

		MinecraftForgeClient.preloadTexture(Biotech.FILE_PATH
				+ "tillingmachine.png");
		MinecraftForgeClient.preloadTexture(Biotech.FILE_PATH
				+ "plantingmachine.png");
		MinecraftForgeClient
				.preloadTexture(Biotech.FILE_PATH + "cowmilker.png");

	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity != null) {
			switch (id) {
			case 0:
				return new TillingMachineContainer(player.inventory,
						(TillingMachineTileEntity) tileEntity);
			case 1:
				return new CowMilkerContainer(player.inventory,
						(CowMilkerTileEntity) tileEntity);
			case 2:
				return new PlantingMachineContainer(player.inventory,
						(PlantingMachineTileEntity) tileEntity);
			case 3:
				return new BioRefineryContainer(player.inventory,
						(BioRefineryTileEntity) tileEntity);
			case 4:
				return new CuttingMachineContainer(player.inventory,
						(CuttingMachineTileEntity) tileEntity);
			}

		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity != null) {
			switch (id) {
			case 0:
				return new TillingMachineGui(player.inventory,
						(TillingMachineTileEntity) tileEntity);
			case 1:
				return new CowMilkerGui(player.inventory,
						(CowMilkerTileEntity) tileEntity);
			case 2:
				return new PlantingMachineGui(player.inventory,
						(PlantingMachineTileEntity) tileEntity);
			case 3:
				return new BioRefineryGui(player.inventory,
						(BioRefineryTileEntity) tileEntity);
			case 4:
				return new CuttingMachineGui(player.inventory,
						(CuttingMachineTileEntity) tileEntity);
			}

		}
		return null;
	}
}
