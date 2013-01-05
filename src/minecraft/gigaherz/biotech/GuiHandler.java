package gigaherz.biotech;

import gigaherz.biotech.client.WorkerGui;
import gigaherz.biotech.container.BasicWorkerContainer;
import gigaherz.biotech.tileentity.BasicWorkerTileEntity;
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

        if (tileEntity instanceof BasicWorkerTileEntity)
        {
            return new BasicWorkerContainer((BasicWorkerTileEntity) tileEntity, player.inventory);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (tileEntity instanceof BasicWorkerTileEntity)
        {
            return new WorkerGui(player.inventory, (BasicWorkerTileEntity) tileEntity);
        }

        return null;
    }
}
