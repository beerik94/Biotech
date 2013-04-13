package biotech;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import biotech.client.BioRefineryGui;
import biotech.client.CowMilkerGui;
import biotech.client.CuttingMachineGui;
import biotech.client.FarmMachineGui;
import biotech.client.FertilizerGui;
import biotech.container.BioRefineryContainer;
import biotech.container.CowMilkerContainer;
import biotech.container.CuttingMachineContainer;
import biotech.container.FarmMachineContainer;
import biotech.container.FertilizerContainer;
import biotech.tileentity.BioRefineryTileEntity;
import biotech.tileentity.CowMilkerTileEntity;
import biotech.tileentity.CuttingMachineTileEntity;
import biotech.tileentity.FarmMachineTileEntity;
import biotech.tileentity.FertilizerTileEntity;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		
		if (tileEntity != null)
		{
			switch (id)
			{
				case 0:
					return new FarmMachineContainer(player.inventory, (FarmMachineTileEntity) tileEntity);
				case 1:
					return new CowMilkerContainer(player.inventory, (CowMilkerTileEntity) tileEntity);
				case 2:
					return new BioRefineryContainer(player.inventory, (BioRefineryTileEntity) tileEntity);
				case 3:
					return new CuttingMachineContainer(player.inventory, (CuttingMachineTileEntity) tileEntity);
				case 4:
					return new FertilizerContainer(player.inventory, (FertilizerTileEntity) tileEntity);
			}
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		
		if (tileEntity != null)
		{
			switch (id)
			{
				case 0:
					return new FarmMachineGui(player.inventory, (FarmMachineTileEntity) tileEntity);
				case 1:
					return new CowMilkerGui(player.inventory, (CowMilkerTileEntity) tileEntity);
				case 2:
					return new BioRefineryGui(player.inventory, (BioRefineryTileEntity) tileEntity);
				case 3:
					return new CuttingMachineGui(player.inventory, (CuttingMachineTileEntity) tileEntity);
				case 4:
					return new FertilizerGui(player.inventory, (FertilizerTileEntity) tileEntity);
			}
		}
		return null;
	}
}
