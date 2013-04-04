package mods.biotech;

import mods.biotech.client.BioRefineryGui;
import mods.biotech.client.CowMilkerGui;
import mods.biotech.client.CuttingMachineGui;
import mods.biotech.client.FarmMachineGui;
import mods.biotech.container.BioRefineryContainer;
import mods.biotech.container.CowMilkerContainer;
import mods.biotech.container.CuttingMachineContainer;
import mods.biotech.container.FarmMachineContainer;
import mods.biotech.tileentity.BioRefineryTileEntity;
import mods.biotech.tileentity.CowMilkerTileEntity;
import mods.biotech.tileentity.CuttingMachineTileEntity;
import mods.biotech.tileentity.FarmMachineTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
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
			}
			
		}
		return null;
	}
}
