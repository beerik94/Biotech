package biotech;

import java.io.File;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import net.minecraftforge.liquids.LiquidContainerData;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.oredict.OreDictionary;
import biotech.block.blockBiotanium;
import biotech.block.blockBiotechMachine;
import biotech.block.blockMilkFlowing;
import biotech.block.blockMilkStill;
import biotech.common.CommonProxy;
import biotech.handlers.GuiHandler;
import biotech.handlers.OreGenHandler;
import biotech.handlers.PacketHandler;
import biotech.handlers.RecipeHandler;
import biotech.handlers.bioEventHandler;
import biotech.item.itemBioBlock;
import biotech.item.itemBioCheese;
import biotech.item.itemBioCircuit;
import biotech.item.itemBioDNA;
import biotech.item.itemBioTabIcon;
import biotech.item.itemBiotaniumIngot;
import biotech.tileentity.tileEntityBasicMachine;
import biotech.tileentity.tileEntityBioRefinery;
import biotech.tileentity.tileEntityCowMilker;
import biotech.tileentity.tileEntityCuttingMachine;
import biotech.tileentity.tileEntityFarmingMachine;
import biotech.tileentity.tileEntityFertilizer;
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
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = Biotech.MOD_ID, name = Biotech.NAME, version = Biotech.VERSION, dependencies = "after:BasicComponents;after:Fluid_Mechanics")
@NetworkMod(channels = Biotech.CHANNEL, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class Biotech
{
	// The instance of your mod that Forge uses.
	@Instance("Biotech")
	public static Biotech				instance;
	
	public static final String			MOD_ID				= "Biotech";
	public static final String			CHANNEL				= "Biotech";
	public static final String			NAME				= "Biotech";
	public static final String			FILE_PATH			= "/mods/biotech/";
	public static final String			LANGUAGE_PATH		= FILE_PATH + "language/";
	public static final String			TEXTURE_PATH		= FILE_PATH + "textures/";
	public static final String			MODEL_PATH			= FILE_PATH + "models/";
	public static final String			GUI_PATH			= TEXTURE_PATH + "gui/";
	public static final String			MODEL_TEXTURE_PATH	= TEXTURE_PATH + "items/model/";
	public static final String			TEXTURE_NAME_PREFIX	= "biotech:";
	private static final int			BLOCK_ID_PREFIX		= 2450;
	private static final int			ITEM_ID_PREFIX		= (24400 - 256);
	private static final String[]		LANGUAGES_SUPPORTED	= new String[] { "en_US" };
	public static final int				MAJOR_VERSION		= 0;
	public static final int				MINOR_VERSION		= 2;
	public static final int				REVISION_VERSION	= 1;
	public static final String			VERSION				= MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION;
	
	// Default config loader
	public static final Configuration	Config				= new Configuration(new File(Loader.instance().getConfigDir(), "UniversalElectricity/Biotech.cfg"));
	public static boolean				mekanismLoaded		= false;
	public static boolean				IC2Loaded			= false;
	public static boolean				BuildCraftLoaded	= false;
	
	// Ore gen per chunk
	public static int					BiotaniumPerChunk	= 2;
	public static int					BiotaniumPerBranch	= 3;
	
	// Item templates
	public static Item					biotechPotionItem;
	public static itemBioCircuit		bioCircuit;
	public static Item					bioCheese;
	public static Item					BiotaniumIngot;
	public static Item					bioDNA;
	public static Item					itemBioTab;
	public static ItemStack				BioTabIcon;
	
	// Mekanism bioFuel
	public static ItemStack				BioFuel;
	
	// Config Ratio Values
	public static double				TO_IC2;
	public static double				TO_BC;
	public static double				FROM_IC2;
	public static double				FROM_BC;
	
	// Block templates
	public static Block					biotechBlockMachine;
	public static Block					milkMoving;
	public static Block					milkStill;
	public static Block					Biotanium;
	
	// Models
	public static final String			ModelBioCheese		= MODEL_PATH + "Cheese.obj";
	
	// Model Textures
	public static final String			BioCheeseTexture	= MODEL_TEXTURE_PATH + "CheeseTexture.png";
	
	// Liquid Stack Milk
	public static LiquidStack			milkLiquid;
	
	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "biotech.client.ClientProxy", serverSide = "biotech.common.CommonProxy")
	public static CommonProxy			proxy;
	
	// Gui Handler
	private GuiHandler					guiHandler			= new GuiHandler();
	
	// Biotech's own CreativeTab
	public static BiotechCreativeTab	tabBiotech			= new BiotechCreativeTab();
	
	// The logger for Biotech
	public static Logger				biotechLogger		= Logger.getLogger("Biotech");
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		biotechLogger.setParent(FMLLog.getLogger());
		biotechLogger.info("Starting Biotech");
		biotechLogger.info("Loading config");
		Config.load();
		Property prop;
		
		/**
		 * Define the items.
		 */
		this.bioCircuit = new itemBioCircuit(Config.getItem("biotech.bioCircuit", ITEM_ID_PREFIX + 1).getInt());
		this.bioCheese = new itemBioCheese(Config.getItem("biotech.bioCheese", ITEM_ID_PREFIX + 2).getInt());
		this.BiotaniumIngot = new itemBiotaniumIngot(Config.getItem("biotech.BiotaniumIngot", ITEM_ID_PREFIX + 3).getInt());
		this.bioDNA = new itemBioDNA(Config.getItem("biotech.bioDNA", ITEM_ID_PREFIX + 4).getInt());
		
		/**
		 * Define the blocks.
		 */
		this.biotechBlockMachine = new blockBiotechMachine(Config.getBlock("biotech.BiotechBlock", BLOCK_ID_PREFIX).getInt(), 0);
		this.milkMoving = new blockMilkFlowing(Config.getBlock("biotech.MilkFlowing", BLOCK_ID_PREFIX + 1).getInt());
		this.milkStill = new blockMilkStill(Config.getBlock("biotech.MilkStill", BLOCK_ID_PREFIX + 2).getInt());
		this.Biotanium = new blockBiotanium(Config.getBlock("biotech.Biotanium", BLOCK_ID_PREFIX + 3).getInt());
		
		/**
		 * Ore gen per chunk
		 */
		this.BiotaniumPerChunk = Config.get(Config.CATEGORY_GENERAL, "biotech.BiotaniumPerChunk", BiotaniumPerChunk).getInt(BiotaniumPerChunk);
		this.BiotaniumPerBranch = Config.get(Config.CATEGORY_GENERAL, "biotech.BiotaniumPerBranch", BiotaniumPerBranch).getInt(BiotaniumPerBranch);
		
		Config.save();
		
		events = new bioEventHandler();
		MinecraftForge.EVENT_BUS.register(events);
		
		/**
		 * Registering Biotech Creative Tab Icon
		 */
		this.itemBioTab = new itemBioTabIcon(ITEM_ID_PREFIX); 
		this.BioTabIcon = new ItemStack(itemBioTab, 1);
		
		if (milkMoving.blockID + 1 != milkStill.blockID)
		{
			throw new RuntimeException("Milk Still id must be Milk Moving id + 1");
		}
		
		/**
		 * Register Milk as a Liquid
		 */
		milkLiquid = LiquidDictionary.getOrCreateLiquid("Milk", new LiquidStack(milkStill, 1));
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Milk", LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(Item.bucketMilk), new ItemStack(Item.bucketEmpty)));
		
		if (Loader.isModLoaded("IC2"))
			IC2Loaded = true;
		if (Loader.isModLoaded("BuildCraft|Energy"))
			BuildCraftLoaded = true;
		if (Loader.isModLoaded("MekanismGenerators"))
			mekanismLoaded = true;
		
		if (mekanismLoaded)
		{
			ItemStack BioFuel = new ItemStack(OreDictionary.getOreID("itemBioFuel"), 1, 0);
		}
		
		biotechLogger.info("Config loaded");
	}
	
	@Init
	public void load(FMLInitializationEvent event)
	{
		/**
		 * Register the TileEntity's
		 */
		GameRegistry.registerTileEntity(tileEntityBasicMachine.class, "BasicMachineTileEntity");
		GameRegistry.registerTileEntity(tileEntityFarmingMachine.class, "FarmingMachineTileEntity");
		GameRegistry.registerTileEntity(tileEntityCuttingMachine.class, "CuttingMachineTileEntity");
		GameRegistry.registerTileEntity(tileEntityFertilizer.class, "FertilizerTileEntity");
		GameRegistry.registerTileEntity(tileEntityCowMilker.class, "CowMilkerTileEntity");
		GameRegistry.registerTileEntity(tileEntityBioRefinery.class, "BioRefineryTileEntity");
		
		/**
		 * Handle the blocks
		 */
		GameRegistry.registerBlock(Biotech.biotechBlockMachine, itemBioBlock.class, "Basic Biotech Block");
		GameRegistry.registerBlock(Biotech.milkMoving, "Milk(Flowing)");
		GameRegistry.registerBlock(Biotech.milkStill, "Milk(Still)");
		GameRegistry.registerBlock(Biotech.Biotanium, "Biotanium");
		
		// Register the mod's ore handler
		GameRegistry.registerWorldGenerator(new OreGenHandler());
		
		/**
		 * Call the recipe registry
		 */
		//if(!mekanismLoaded)
		//{
			RecipeHandler.BiotechRecipes();
		//}
		//else
		//{
		//	RecipeHandler.MekanismRecipes();
		//}
		
		/**
		 * Load Proxy
		 */
		proxy.registerRenderInformation();
		
		/**
		 * Handle language support
		 */
		int languages = 0;
		
		for (String language : LANGUAGES_SUPPORTED)
		{
			LanguageRegistry.instance().loadLocalization(LANGUAGE_PATH + language + ".properties", language, false);
			
			if (LanguageRegistry.instance().getStringLocalization("children", language) != "")
			{
				try
				{
					String[] children = LanguageRegistry.instance().getStringLocalization("children", language).split(",");
					
					for (String child : children)
					{
						if (child != "" && child != null)
						{
							LanguageRegistry.instance().loadLocalization(LANGUAGE_PATH + language + ".properties", child, false);
							languages++;
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
		System.out.println(NAME + ": Loaded " + languages + " Official");
		
		NetworkRegistry.instance().registerGuiHandler(this, guiHandler);
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event)
	{
		biotechLogger.info("Biotech fully loaded");
	}
	
	public static bioEventHandler events;
}
