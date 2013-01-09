package gigaherz.biotech;

import gigaherz.biotech.block.BasicWorkerBlockMachine;
import gigaherz.biotech.block.BiotechBlockMachine;
import gigaherz.biotech.common.CommonProxy;
import gigaherz.biotech.item.BiotechItemBlock;
import gigaherz.biotech.tileentity.BasicMachineTileEntity;
import gigaherz.biotech.tileentity.BasicWorkerTileEntity;

import java.io.File;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import net.minecraftforge.oredict.OreDictionary;
import universalelectricity.prefab.network.ConnectionHandler;
import universalelectricity.prefab.network.PacketManager;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "Biotech", name = "Biotech", version = "0.1.2")
@NetworkMod(channels = Biotech.CHANNEL, clientSideRequired = true, serverSideRequired = false, connectionHandler = ConnectionHandler.class, packetHandler = PacketManager.class)

public class Biotech
{
	public static final String FILE_PATH = "/resources/biotech/textures/";
	public static final String BLOCK_TEXTURE_FILE = FILE_PATH + "block.png";
	public static final String ITEM_TEXTURE_FILE = FILE_PATH + "items.png";

	public static final String CHANNEL = "Biotech";
	
    private final static int firstItemId = 24400;
    private final static int firstBlockId = 2450;

    private final static int defaultWorkerBlockId = firstBlockId + 1;
    
    private final static int defaultBiotechBlockId = defaultWorkerBlockId + 1;
    
    private final static int defaultCommandCircuitId = firstItemId + 1;

    public static final Configuration Config = new Configuration(new File(Loader.instance().getConfigDir(), "Biotech/Biotech.cfg"));

    // Item templates
    private static CommandCircuit circuit;

    // Block templates
    public static Block basicWorker;
    

    public static Block biotechBlockMachine;
	//0 == Tiller
	//1 == Foresting
	//2 == Woodcutter
	//3 == Crop Harvester
	//4 == Fertilizer
	//5 == Miner
	//6 == Filler

    // Tier 1
    public static ItemStack planter;
    public static ItemStack harvester;
    public static ItemStack woodcutter;

    // Tier 2
    public static ItemStack fertilizer;
    public static ItemStack tiller;

    // Tier 3
    public static ItemStack miner;
    public static ItemStack filler;

    // The instance of your mod that Forge uses.
    @Instance("Biotech")
    public static Biotech instance;

    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide = "gigaherz.biotech.client.ClientProxy", serverSide = "gigaherz.biotech.common.CommonProxy")
    
    public static CommonProxy proxy;

    public static Property enableChatCommand;
    
    private GuiHandler guiHandler = new GuiHandler();

    public static BiotechCreativeTab tabBiotech = new BiotechCreativeTab();
    public static Logger biotechLogger = Logger.getLogger("Biotech");
    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
    	biotechLogger.setParent(FMLLog.getLogger());
    	biotechLogger.info("Starting Biotech");
    	biotechLogger.info("Loading config");
    	Config.load();
        Property prop;
        
        prop = Config.getItem("gigaherz.biotech.CommandCircuit", defaultCommandCircuitId);
        
        circuit = new CommandCircuit(prop.getInt());
        
        prop = Config.getBlock("gigaherz.biotech.Worker", defaultWorkerBlockId);
        
        basicWorker = new BasicWorkerBlockMachine(prop.getInt(), 1).setHardness(0.5F).setStepSound(Block.soundMetalFootstep);
        
        planter = circuit.getStack(1, 1);
        harvester = circuit.getStack(1, 2);
        woodcutter = circuit.getStack(1, 3);
        fertilizer = circuit.getStack(1, 4);
        tiller = circuit.getStack(1, 5);
        miner = circuit.getStack(1, 6);
        filler = circuit.getStack(1, 7);

        prop = Config.getBlock("gigaherz.biotech.BiotechBlock", defaultBiotechBlockId);
        
        biotechBlockMachine = new BiotechBlockMachine(prop.getInt(), 1).setHardness(0.5F).setStepSound(Block.soundMetalFootstep);
        
        Property enableChatCommand = Config.get("general", "enableChatCommand", true);
        
        Config.save();
        biotechLogger.info("Config loaded");
    }

    
    public static void initCommands(FMLServerStartingEvent event) 
    {
    if (enableChatCommand.value == "true") 
		{
			
			event.registerServerCommand(new CmdWorker());
			biotechLogger.info("Biotech Command Enabled");
		}
    }
    
    @Init
    public void load(FMLInitializationEvent event)
    {
    	ItemStack itemBasicCircuit =  new ItemStack(OreDictionary.getOreID("basicCircuit"), 1, 0);
    	
    	ItemStack itemMotor =  new ItemStack(OreDictionary.getOreID("motor"), 1 , 0);
    	
    	ItemStack itemBronzePlate =  new ItemStack(OreDictionary.getOreID("plateBronze"), 1 , 0);
    	
    	ItemStack itemChest = new ItemStack(Block.chest, 1);
    	
        proxy.registerRenderers();
        
        GameRegistry.registerTileEntity(BasicWorkerTileEntity.class, "basicWorkerTile");
        GameRegistry.registerTileEntity(BasicMachineTileEntity.class, "BasicMachineTileEntity");
        
        MinecraftForge.setBlockHarvestLevel(basicWorker, "pickaxe", 0);
        // Registration
        GameRegistry.registerBlock(basicWorker, "Basic Worker");
        
        
        LanguageRegistry.addName(basicWorker, "Basic Worker");
        
        GameRegistry.registerBlock(Biotech.biotechBlockMachine, BiotechItemBlock.class, "Basic Biotech Block");
        
        LanguageRegistry.addName(biotechBlockMachine, "Basic Biotech Block");
        
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
        ItemStack[] bcCircuits =
        {
        		itemBasicCircuit,
        		itemBasicCircuit,
        		itemBasicCircuit,
        };
        bcCircuits[0].setItemDamage(0); //Basic Circuit
        bcCircuits[1].setItemDamage(1); //Advanced Circuit
        bcCircuits[2].setItemDamage(2); //Elite Circuit
        biotechLogger.info("Loading recipes");
        GameRegistry.addShapelessRecipe(planter, bcCircuits[0], Item.seeds);
        GameRegistry.addShapelessRecipe(planter, bcCircuits[0], Item.melonSeeds);
        GameRegistry.addShapelessRecipe(planter, bcCircuits[0], Item.pumpkinSeeds);
        GameRegistry.addShapelessRecipe(planter, bcCircuits[0], Item.netherStalkSeeds);
        GameRegistry.addShapelessRecipe(planter, bcCircuits[0], Block.cactus);
        GameRegistry.addShapelessRecipe(planter, bcCircuits[0], Item.reed);
        GameRegistry.addShapelessRecipe(harvester, bcCircuits[0], Item.wheat);
        GameRegistry.addShapelessRecipe(woodcutter, bcCircuits[0], new ItemStack(Block.sapling, 1, -1));
        GameRegistry.addShapelessRecipe(fertilizer, bcCircuits[1], new ItemStack(Item.dyePowder, 1, 15));
        GameRegistry.addShapelessRecipe(tiller, bcCircuits[1], Block.dirt);
        GameRegistry.addShapelessRecipe(miner, bcCircuits[2], Block.stone);
        GameRegistry.addShapelessRecipe(filler, bcCircuits[2], Block.dirt);
        //1x Chest, 1x Circuit, 1x Motor, 1x Tier material, Plating?
        

        GameRegistry.addRecipe(new ItemStack(basicWorker, 1),
                "ddd",
                "bcb",
                "dad",
                'a', itemChest,
                'b', itemBasicCircuit,
                'c', itemMotor,
                'd', itemBronzePlate
        );
        
        NetworkRegistry.instance().registerGuiHandler(this, guiHandler);
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
    	biotechLogger.info("Biotech fully loaded");
    }
}
