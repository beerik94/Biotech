package gigaherz.workercommand;

import java.util.List;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class CommandCircuit extends Item {
	
	private final static String[] subNames = {
		"unprogrammed", 
		
		// Tier 1
		"planter", "harvester", "woodcutter",
		
		// Tier 2
		"fertilizer", "tiller",
		
		// Tier 3
		"miner", "filler",
	};

    public CommandCircuit(int id)
    {
        super(id);
        
        // Constructor Configuration
        setMaxStackSize(64);
        setCreativeTab(CreativeTabs.tabMisc);
        setIconIndex(0);
        setItemName("commandCircuit");
        setHasSubtypes(true);
    }
    
    @Override
    public int getMetadata (int damageValue) {
    	return damageValue;
    }
    
    public String getTextureFile()
    {
        return CommonProxy.ITEMS_PNG;
    }
    
    @Override
    public String getItemNameIS(ItemStack stack) {
    	int sub = stack.getItemDamage();
    	
    	if(sub >= subNames.length)
    		sub = 0;
    	
    	return getItemName() + "." + subNames[sub];
    }
    
    public ItemStack getStack(int count, int damageValue)
    {
    	ItemStack stack = new ItemStack(this, count);
    	stack.setItemDamage(damageValue);
    	return stack;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(int unknown, CreativeTabs tab, List subItems) {
    	for (int meta = 1; meta < subNames.length; meta++) {
    		subItems.add(new ItemStack(this, 1, meta));
    	}
    }
}
