package biotech;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import biotech.client.MilkingMachineGui;
import biotech.client.MilkingManagerGui;
import biotech.client.PlantingMachineGui;
import biotech.client.TillingMachineGui;
import biotech.container.MilkingMachineContainer;
import biotech.container.MilkingManagerContainer;
import biotech.container.PlantingMachineContainer;
import biotech.container.TillingMachineContainer;
import biotech.tileentity.MilkingMachineTileEntity;
import biotech.tileentity.MilkingManagerTileEntity;
import biotech.tileentity.PlantingMachineTileEntity;
import biotech.tileentity.TillingMachineTileEntity;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	public void preInit()
	{
		// Preload textures
		MinecraftForgeClient.preloadTexture(Biotech.BLOCK_TEXTURE_FILE);
		MinecraftForgeClient.preloadTexture(Biotech.ITEM_TEXTURE_FILE);
		
		MinecraftForgeClient.preloadTexture(Biotech.FILE_PATH + "tillingmachine.png");
		MinecraftForgeClient.preloadTexture(Biotech.FILE_PATH + "plantingmachine.png");
		MinecraftForgeClient.preloadTexture(Biotech.FILE_PATH + "cowmilker.png");
		
	}
	
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if(tileEntity != null)
	    {
	    	switch(id)
	    	{
	    	case 0:
	    		return new TillingMachineContainer(player.inventory, (TillingMachineTileEntity) tileEntity);
	    	case 1:
	    		return new MilkingManagerContainer(player.inventory, (MilkingManagerTileEntity) tileEntity);
	    	case 2:
	    		return new PlantingMachineContainer(player.inventory, (PlantingMachineTileEntity) tileEntity);
	    	case 3:
	    		return new MilkingMachineContainer(player.inventory, (MilkingMachineTileEntity) tileEntity);
	    	}
	    	
	    }
        return null;
    }
    
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
	    TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
	    
	    if(tileEntity != null)
	    {
	    	switch(id)
	    	{
	    	case 0:
	    		return new TillingMachineGui(player.inventory, (TillingMachineTileEntity) tileEntity);
	    	case 1:
	    		return new MilkingManagerGui(player.inventory, (MilkingManagerTileEntity) tileEntity);
	    	case 2:
	    		return new PlantingMachineGui(player.inventory, (PlantingMachineTileEntity) tileEntity);
	    	case 3:
	    		return new MilkingMachineGui(player.inventory, (MilkingMachineTileEntity) tileEntity);
	    	}
	    	
	    }
        return null;
    }
}
