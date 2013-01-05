package gigaherz.biotech.client;

import gigaherz.biotech.Biotech;
import gigaherz.biotech.common.CommonProxy;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderers()
    {
        MinecraftForgeClient.preloadTexture(Biotech.ITEM_TEXTURE_FILE);
        MinecraftForgeClient.preloadTexture(Biotech.BLOCK_TEXTURE_FILE);
    }
}
