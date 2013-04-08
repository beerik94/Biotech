package biotech.client;

import net.minecraftforge.client.MinecraftForgeClient;
import biotech.Biotech;
import biotech.common.CommonProxy;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRenderInformation()
	{
		//Register item handler
		MinecraftForgeClient.registerItemRenderer(Biotech.bioCheese.itemID, new ItemRenderHandler());

		System.out.println("[Biotech] Render registrations complete.");
	}
	
}
