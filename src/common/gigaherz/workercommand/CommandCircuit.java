package gigaherz.workercommand;

import java.util.List;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.src.Block;
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

	public boolean canDoWork(WorkerTile workerTile, int damage) 
	{
		switch(damage)
		{
		case 1: // planter
			return hasOneOf(workerTile,
					new ItemStack(Item.seeds, 1),
					new ItemStack(Item.melonSeeds, 1), 
					new ItemStack(Item.pumpkinSeeds, 1), 
					new ItemStack(Item.netherStalkSeeds, 1),
					new ItemStack(Block.cactus, 1), 
					new ItemStack(Item.reed, 1),
	        		new ItemStack(Block.sapling, 1, -1));
		case 2: // harvester
			return hasSlotWithSpaceFor(workerTile,
					new ItemStack(Item.seeds, 1),
					new ItemStack(Item.melonSeeds, 1), 
					new ItemStack(Item.pumpkinSeeds, 1), 
					new ItemStack(Item.netherStalkSeeds, 1),
					new ItemStack(Item.reed, 1));
		case 3: // woodcutter
			return hasSlotWithSpaceFor(workerTile,
					new ItemStack(Block.cactus, 1), 
	        		new ItemStack(Block.wood, 1, -1))
	        			&& hasToolOf(workerTile,
								new ItemStack(Item.axeWood, 1),
								new ItemStack(Item.axeStone, 1),
								new ItemStack(Item.axeSteel, 1),
								new ItemStack(Item.axeGold, 1),
								new ItemStack(Item.axeDiamond, 1)
									   );
		// TODO: other tiers
		}
		return false;
	}

	private boolean hasToolOf(WorkerTile workerTile, ItemStack... stacks) 
	{
		for(int i=0; i<stacks.length; i++)
		{
			if(workerTile.hasToolInToolArea(stacks[i]))
				return true;
		}
		return false;
	}

	private boolean hasSlotWithSpaceFor(WorkerTile workerTile,
			ItemStack... stacks) {
		for(int i=0; i<stacks.length; i++)
		{
			if(workerTile.hasSpaceInOutputAreaForItem(stacks[i]))
				return true;
		}
		return false;
	}

	private boolean hasOneOf(WorkerTile workerTile, ItemStack... stacks) {
		for(int i=0; i<stacks.length; i++)
		{
			if(workerTile.hasItemInInputArea(stacks[i]))
				return true;
		}
		return false;
	}

	public boolean doWork(WorkerTile workerTile, int damage) {
		switch(damage)
		{
		case 1: // planter
			// TODO: implement
			break;//return true;
		case 2: // harvester
			// TODO: implement
			break;//return true;
		case 3: // woodcutter			
			// TODO: implement
			break;//return true;
		// TODO: other tiers
		}
		return false;
	}
}
