package gigaherz.workercommand;

import java.util.ArrayList;
import java.util.List;

import basiccomponents.BasicComponents;
import gigaherz.workercommand.client.ClientPacketHandler;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "WorkerCommand", name = "WorkerCommand", version = "0.1.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false,
        clientPacketHandlerSpec = @SidedPacketHandler(channels = {"WorkerCommand" }, packetHandler = ClientPacketHandler.class),
        serverPacketHandlerSpec = @SidedPacketHandler(channels = {"WorkerCommand" }, packetHandler = ServerPacketHandler.class))
public class WorkerCommand
{
    private final static int firstItemId = 24400;
    private final static int firstBlockId = 2450;

    // Item templates
    private final static CommandCircuit circuit = new CommandCircuit(firstItemId + 1);

    // Block templates
    public final static Block worker = new Worker("worker", firstBlockId + 1, Material.iron, CreativeTabs.tabRedstone)
	    .setHardness(0.5F).setStepSound(Block.soundMetalFootstep);
		
	// Tier 1
    public final static ItemStack planter = circuit.getStack(1,1);
    public final static ItemStack harvester = circuit.getStack(1,2);
    public final static ItemStack woodcutter = circuit.getStack(1,3);
		
	// Tier 2
    public final static ItemStack fertilizer = circuit.getStack(1,4);
    public final static ItemStack tiller = circuit.getStack(1,5);
		
	// Tier 3
    public final static ItemStack miner = circuit.getStack(1,6);
    public final static ItemStack filler = circuit.getStack(1,7);
	
    // Future Blocks
    // * Generator
    // * Transducer
    // * Energy Extractor
    // * Induction Furnace
    // * Duplicator

    // The instance of your mod that Forge uses.
    @Instance("WorkerCommand")
    public static WorkerCommand instance;

    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide = "gigaherz.workercommand.client.ClientProxy", serverSide = "gigaherz.workercommand.CommonProxy")
    public static CommonProxy proxy;

    private GuiHandler guiHandler = new GuiHandler();

    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        // Stub Method
    }

    @Init
    public void load(FMLInitializationEvent event)
    {
        proxy.registerRenderers();
        
        GameRegistry.registerTileEntity(WorkerTile.class, "workerTile");
        
        MinecraftForge.setBlockHarvestLevel(worker, "pickaxe", 0);        
        
        // Registration
        GameRegistry.registerBlock(worker);
        LanguageRegistry.addName(worker, "Worker");
        
		// Tier 1
		LanguageRegistry.addName(planter, "Planter Command Circuit");
		LanguageRegistry.addName(harvester, "Harvester Command Circuit");
		LanguageRegistry.addName(woodcutter, "Woodcutter Command Circuit");
			
		// Tier 2
		LanguageRegistry.addName(fertilizer, "Fertilizer Command Circuit");
		LanguageRegistry.addName(tiller, "Tiller Command Circuit");
			
		// Tier 3
		LanguageRegistry.addName(miner, "Miner Command Circuit");
		LanguageRegistry.addName(filler, "Filler Command Circuit");
		        
        // Recipes
        ItemStack[] c = {
        		new ItemStack(BasicComponents.itemCircuit),
        		new ItemStack(BasicComponents.itemCircuit),
        		new ItemStack(BasicComponents.itemCircuit),
        };
        c[0].setItemDamage(0);
        c[1].setItemDamage(1);
        c[2].setItemDamage(2);

        GameRegistry.addShapelessRecipe(planter, c[0], Item.seeds);
        GameRegistry.addShapelessRecipe(planter, c[0], Item.melonSeeds);
        GameRegistry.addShapelessRecipe(planter, c[0], Item.pumpkinSeeds);
        GameRegistry.addShapelessRecipe(planter, c[0], Item.netherStalkSeeds);
        GameRegistry.addShapelessRecipe(harvester, c[0], Item.wheat);        
        GameRegistry.addShapelessRecipe(woodcutter, c[0], new ItemStack(Block.sapling, 1, -1));
        
        GameRegistry.addShapelessRecipe(fertilizer, c[1], new ItemStack(Item.dyePowder, 1, 15));
        GameRegistry.addShapelessRecipe(tiller, c[1], Block.dirt);

        GameRegistry.addShapelessRecipe(miner, c[2], Block.stone);
        GameRegistry.addShapelessRecipe(filler, c[2], Block.dirt);
        
        //1x Chest, 1x Circuit, 1x Motor, 1x Tier material, Plating?
        ItemStack chest = new ItemStack(Block.chest, 1);
        ItemStack circuit = new ItemStack(BasicComponents.itemCircuit, 1, 0);
        ItemStack motor = new ItemStack(BasicComponents.itemMotor, 1, -1);
        ItemStack plating = new ItemStack(BasicComponents.itemBronzePlate, 1);
        
        GameRegistry.addRecipe(new ItemStack(worker, 1),
        		"ddd",
        		"bcb",
        		"dad",
        		'a', chest,
        		'b', circuit,
        		'c', motor,
        		'd', plating
        		);
        
        NetworkRegistry.instance().registerGuiHandler(this, guiHandler);
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
        // Stub Method
    }
}
