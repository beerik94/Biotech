package biotech.client;

import net.minecraftforge.client.MinecraftForgeClient;
import biotech.Biotech;
import biotech.common.CommonProxy;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerRenderers() {
		MinecraftForgeClient.preloadTexture(Biotech.ITEM_TEXTURE_FILE);
		MinecraftForgeClient.preloadTexture(Biotech.BLOCK_TEXTURE_FILE);
	}
}
