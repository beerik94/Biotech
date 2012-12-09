package gigaherz.workercommand;

import gigaherz.workercommand.client.WorkerGui;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.src.*;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (tileEntity instanceof WorkerTile)
        {
            return new WorkerContainer((WorkerTile) tileEntity, player.inventory);
        }
        
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (tileEntity instanceof WorkerTile)
        {
            return new WorkerGui(player.inventory, (WorkerTile) tileEntity);
        }

        return null;
    }
}
