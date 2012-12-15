package gigaherz.workercommand;

import java.util.List;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.StatList;
import net.minecraft.src.StringUtils;
import net.minecraft.src.World;

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
		case 4: // fertilizer	
			// TODO: implement
			break;
		case 5: // tiller	
			// TODO: implement
			break;
		case 6: // miner	
			// TODO: implement
			break;
		case 7: // filler	
			return workerTile.hasAnyBlockInInputArea();
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
			System.out.println("Runnig dummy planter.");
			return true;
		case 2: // harvester
			// TODO: implement
			break;//return true;
		case 3: // woodcutter			
			// TODO: implement
			break;//return true;			
		case 4: // fertilizer	
			// TODO: implement
			break;
		case 5: // tiller	
			// TODO: implement
			break;
		case 6: // miner	
			// TODO: implement
			break;
		case 7: // filler
			for(int i=0;i<9; i++)
			{
				ItemStack stack = workerTile.getStackInSlot(i);
				if(stack == null)
					continue;

				Item item = stack.getItem();
				
				if(!(item instanceof ItemBlock))
					continue;
				
				int topY = workerTile.getTopY();
				
				if(topY < 0)
					continue;
				
				EntityPlayer fakePlayer = new FakePlayer(workerTile.worldObj);

	            ItemBlock block = (ItemBlock)item;
	            
	            int cx = workerTile.xCoord + workerTile.currentX;
	            int cy = workerTile.yCoord + topY;
	            int cz = workerTile.zCoord + workerTile.currentZ;
	            for(int s=0;s<6;s++)
	            {
	            	int tx=cx, ty=cy, tz=cz;
	            	switch(s)
	            	{
	            	case 0: ty++; break;
	            	case 1: ty--; break;
	            	case 2: tz++; break;
	            	case 3: tz--; break;
	            	case 4: tx++; break;
	            	case 5: tx--; break;
	            	}
	            	
		            if (!block.canPlaceItemBlockOnSide(workerTile.worldObj, tx, ty, tz, s, fakePlayer, stack))
		            {
		            	continue;
		            }
		            				
					if(block.onItemUse(stack, fakePlayer, workerTile.worldObj, tx, ty, tz, s, (float)0, (float)0, (float)0))
					{
						System.out.println("Worked!");
						stack.stackSize++;
						return true;
					}
	            }
			}
			break;
		}
		return false;
	}
}

class FakePlayer extends EntityPlayer
{
    public FakePlayer(World par1World)
    {
        super(par1World);
    }

    /**
     * sets the players height back to normal after doing things like sleeping and dieing
     */
    protected void resetHeight()
    {
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        return false;
    }

    /**
     * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
     * posY, posZ, yaw, pitch
     */
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
        this.posX = par1;
        this.posY = par3;
        this.posZ = par5;
    }

    public void updateCloak()
    {
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
    }

    public float getShadowSize()
    {
        return 0.0F;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
    }

    /**
     * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
     */
    public void setCurrentItemOrArmor(int par1, ItemStack par2ItemStack)
    {
    }

    public float getEyeHeight()
    {
        return 0.5f;
    }

    public void sendChatToPlayer(String par1Str)
    {
    }

    /**
     * Returns true if the command sender is allowed to use the given command.
     */
    public boolean canCommandSenderUseCommand(int par1, String par2Str)
    {
        return false;
    }

    /**
     * Return the coordinates for this player as ChunkCoordinates.
     */
    public ChunkCoordinates getPlayerCoordinates()
    {
        return new ChunkCoordinates(MathHelper.floor_double(this.posX + 0.5D), MathHelper.floor_double(this.posY + 0.5D), MathHelper.floor_double(this.posZ + 0.5D));
    }
}
