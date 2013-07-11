package biotech.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import biotech.client.guiBioRefinery;
import biotech.client.guiCowMilker;
import biotech.client.guiCuttingMachine;
import biotech.client.guiFarmingMachine;
import biotech.client.guiFertilizer;
import biotech.client.guiSewer;
import biotech.container.containerBioRefinery;
import biotech.container.containerCowMilker;
import biotech.container.containerCuttingMachine;
import biotech.container.containerFarmingMachine;
import biotech.container.containerFertilizer;
import biotech.container.containerSewer;
import biotech.tileentity.tileEntityBioRefinery;
import biotech.tileentity.tileEntityCowMilker;
import biotech.tileentity.tileEntityCuttingMachine;
import biotech.tileentity.tileEntityFarmingMachine;
import biotech.tileentity.tileEntityFertilizer;
import biotech.tileentity.tileEntitySewer;
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
					return new containerFarmingMachine(player.inventory, (tileEntityFarmingMachine) tileEntity);
				case 1:
					return new containerCowMilker(player.inventory, (tileEntityCowMilker) tileEntity);
				case 2:
					return new containerBioRefinery(player.inventory, (tileEntityBioRefinery) tileEntity);
				case 3:
					return new containerCuttingMachine(player.inventory, (tileEntityCuttingMachine) tileEntity);
				case 4:
					return new containerFertilizer(player.inventory, (tileEntityFertilizer) tileEntity);
				case 5:
					return new containerSewer(player.inventory, (tileEntitySewer) tileEntity);
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
					return new guiFarmingMachine(player.inventory, (tileEntityFarmingMachine) tileEntity);
				case 1:
					return new guiCowMilker(player.inventory, (tileEntityCowMilker) tileEntity);
				case 2:
					return new guiBioRefinery(player.inventory, (tileEntityBioRefinery) tileEntity);
				case 3:
					return new guiCuttingMachine(player.inventory, (tileEntityCuttingMachine) tileEntity);
				case 4:
					return new guiFertilizer(player.inventory, (tileEntityFertilizer) tileEntity);
				case 5:
					return new guiSewer(player.inventory, (tileEntitySewer) tileEntity);
			}
		}
		return null;
	}
}
