package gigaherz.workercommand;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ServerPacketHandler implements IPacketHandler
{
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload payload, Player player)
    {
        DataInputStream data = new DataInputStream(new ByteArrayInputStream(payload.data));
        EntityPlayer sender = (EntityPlayer) player;
    }
}
