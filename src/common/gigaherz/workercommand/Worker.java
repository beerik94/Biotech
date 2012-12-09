package gigaherz.workercommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import universalelectricity.prefab.BlockMachine;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Slot;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityFurnace;
import net.minecraft.src.World;

public class Worker extends BlockMachine
{
    public Worker(String name, int id, Material mat, CreativeTabs tab)
    {
        super(name, id, mat, tab);
    }

    @Override
    public String getTextureFile()
    {
        return CommonProxy.BLOCK_PNG;
    }

	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		return WorkerCommand.worker.blockID;
	}
	
    @Override
    public int getBlockTextureFromSideAndMetadata(int i, int meta)
    {
    	int sideTexture1 = (meta & 8) != 0 ? 17 : 1;
    	int sideTexture2 = (meta & 8) != 0 ? 48 : 32;
    	int sideTexture3 = 18;
    	
    	int meta2=0;
    	switch(meta & 7)
    	{
	    	case 2: meta2=3; break;
	    	case 3: meta2=2; break;
	    	case 5: meta2=4; break;
	    	case 4: meta2=5; break;
    	}
    	
        switch (i)
        {
            case 0: // bottom
                return 3;
            case 1: // top
                return 3;
            default: // sides
                if (meta2 == i)
                	return sideTexture3;
                return (meta & 7) == i ? sideTexture2 : sideTexture1;
        }
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        int metadata = world.getBlockMetadata(x, y, z);
        
        boolean wasPowered = (metadata & 8) != 0;
        
        if (wasPowered)
        {
        	metadata &= 7;
            float sx = (float)x + 0.5F;
            float sy = (float)y + 0.0F + random.nextFloat() * 6.0F / 16.0F;
            float sz = (float)z + 0.5F;
            float o1 = 0.52F;
            float o2 = random.nextFloat() * 0.6F - 0.3F;

            world.spawnParticle("reddust", (double)(sx - o1), (double)sy, (double)(sz + o2), 0.0D, 0.0D, 0.0D);
            world.spawnParticle("reddust", (double)(sx + o1), (double)sy, (double)(sz + o2), 0.0D, 0.0D, 0.0D);
            world.spawnParticle("reddust", (double)(sx + o2), (double)sy, (double)(sz - o1), 0.0D, 0.0D, 0.0D);
            world.spawnParticle("reddust", (double)(sx + o2), (double)sy, (double)(sz + o1), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity)
    {
        int angle = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        switch(angle)
        {
        case 0:
            world.setBlockMetadataWithNotify(x, y, z, 2);
            break;
        case 1:
            world.setBlockMetadataWithNotify(x, y, z, 5);
            break;
        case 2:
            world.setBlockMetadataWithNotify(x, y, z, 3);
            break;
        case 3:
            world.setBlockMetadataWithNotify(x, y, z, 4);
            break;
        }
    }

    @Override
	public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (tileEntity == null || player.isSneaking())
            return false;
        
        player.openGui(WorkerCommand.instance, 0, world, x, y, z);
        return true;
    }

    public static void updateBlockState(boolean powered, World world, int x, int y, int z)
    {
        int metadata = world.getBlockMetadata(x, y, z);

        if (powered)
        	metadata |= 8;
        else
            metadata &= 7;

        world.setBlockMetadataWithNotify(x, y, z, metadata);
    }

    @Override
	public boolean hasTileEntity(int metadata)
    {
    	return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new WorkerTile();
    }

    // Statics (recipe book)
    public static final List<Recipe> recipes = new ArrayList<Recipe>();
    
    public static ItemStack findRecipeResult(ItemStack input) {
    	for(Recipe r : recipes) {
    		if(r.input.itemID == input.itemID)
    		{
    			if(r.input.getItemDamage() < 0 || r.input.getItemDamage() == input.getItemDamage())
    				return r.result;
    		}
    	}
    	return null;
    }

    public static void addRecipe(Block block, ItemStack resultStack) {
    	recipes.add(new Recipe(new ItemStack(block.blockID, 1, -1), resultStack));
    }

    public static void addRecipe(Item item, ItemStack resultStack) {
    	recipes.add(new Recipe(new ItemStack(item.shiftedIndex, 1, -1), resultStack));
    }

    public static void addRecipe(ItemStack stack, ItemStack resultStack) {
    	recipes.add(new Recipe(stack, resultStack));
    }

    public static void addRecipe(int inputId, ItemStack resultStack) {
    	recipes.add(new Recipe(new ItemStack(inputId, 1, -1), resultStack));
    }

    private static class Recipe
    {
        private ItemStack result;
        private ItemStack input;

        private Recipe(ItemStack inputId, ItemStack resultStack) {
        	input = inputId;
        	result = resultStack;
        }

        public ItemStack getResult()
        {
            return result;
        }

        public ItemStack getInput()
        {
            return input;
        }
    }
}
