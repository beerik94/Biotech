package biotech.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions({ "biotech.asm" })
public class AsmLoadingPlugin implements IFMLLoadingPlugin
{

	@Override
	public String[] getLibraryRequestClass()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[] {"biotech.asm.EntityAccessTransformer"};
	}

	@Override
	public String getModContainerClass()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSetupClass()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		// TODO Auto-generated method stub

	}

}
