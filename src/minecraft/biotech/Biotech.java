package biotech;

import java.io.File;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import net.minecraftforge.liquids.LiquidContainerData;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.network.ConnectionHandler;
import universalelectricity.prefab.network.PacketManager;
import biotech.block.BiotechBlockMachine;
import biotech.block.MilkFlowingBlock;
import biotech.block.MilkStillBlock;
import biotech.common.CommonProxy;
import biotech.item.bioCircuitItem;
import biotech.item.biotechItemBlock;
import biotech.tileentity.BasicMachineTileEntity;
import biotech.tileentity.BioRefineryTileEntity;
import biotech.tileentity.CowMilkerTileEntity;
import biotech.tileentity.CuttingMachineTileEntity;
import biotech.tileentity.FarmMachineTileEntity;
import biotech.tileentity.PlantingMachineTileEntity;
import biotech.tileentity.TillingMachineTileEntity;
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

@Mod(modid = Biotech.MOD_ID, name = Biotech.NAME, version = Biotech.VERSION, dependencies = "after:BasicComponents;after:hydraulic")
@NetworkMod(channels = Biotech.CHANNEL, clientSideRequired = true, serverSideRequired = false, connectionHandler = ConnectionHandler.class, packetHandler = PacketManager.class)
public class Biotech
{
	// The instance of your mod that Forge uses.
	@Instance("Biotech")
	public static Biotech				instance;
	
	public static final String			MOD_ID				= "Biotech";
	
	public static final String			CHANNEL				= "Biotech";
	
	public static final String			NAME				= "Biotech";
	
	public static final String			LANGUAGE_PATH		= "/mods/biotech/language/";
	
	public static final String			FILE_PATH			= "/mods/biotech/textures/";
	public static final String			GUI_PATH			= FILE_PATH + "gui/";
	public static final String			BLOCK_FILE_PATH		= FILE_PATH + "block/";
	public static final String			ITEM_FILE_PATH		= FILE_PATH + "items/";
	
	public static final String			TEXTURE_NAME_PREFIX	= "biotech:";
	private static final int			BLOCK_ID_PREFIX		= 2450;
	private static final int			ITEM_ID_PREFIX		= 24400;
	
	private static final String[]		LANGUAGES_SUPPORTED	= new String[] { "en_US" };
	
	public static final int				MAJOR_VERSION		= 0;
	public static final int				MINOR_VERSION		= 1;
	public static final int				REVISION_VERSION	= 0;
	public static final String			VERSION				= MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION;
	
	// Default config loader
	public static final Configuration	Config				= new Configuration(new File(Loader.instance().getConfigDir(), "UniversalElectricity/Biotech.cfg"));
	
	// Item templates
	public static Item					biotechPotionItem;
	public static bioCircuitItem		bioCircuit;
	
	// Itemstacks for different biocircuits
	public static ItemStack				UnProgrammed;
	public static ItemStack				WheatSeeds;
	public static ItemStack				MelonSeeds;
	public static ItemStack				PumpkinSeeds;
	public static ItemStack				Carrots;
	public static ItemStack				Potatoes;
	public static ItemStack				RangeUpgrade;
	public static ItemStack				TreeSappling;
	public static ItemStack				Pickaxe;
	public static ItemStack				Shovel;
	public static ItemStack				Hoe;
	
	// Metadata for BioCircuit
	// 0 == unprogrammed
	// 1 == wheatseeds
	// 2 == melonseeds
	// 3 == pumpkinseeds
	// 4 == carrots
	// 5 == potatoes
	// 6 == rangeupgrade
	// 7 == treesappling
	// 8 == pickaxecircuit
	// 9 == shovelcircuit
	// 10 == hoecircuit
	
	// Mekanism bioFuel
	public static ItemStack				BioFuel				= new ItemStack(OreDictionary.getOreID("itemBioFuel"), 1, 0);
	
	// Block templates
	public static Block					biotechBlockMachine;
	public static Block					milkMoving;
	public static Block					milkStill;
	
	// Metadata for biotechBlockMachine
	// 0 == Farm
	// 1 == Woodcutter
	// 2 == Fertilizer
	// 3 == Miner
	// 4 == Cow Milker
	// 5 == BioRefinery
	
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
		 * Define the items and blocks.
		 */
		this.bioCircuit = new bioCircuitItem(Config.getItem("biotech.bioCircuit", ITEM_ID_PREFIX).getInt(), 0);
		this.biotechBlockMachine = new BiotechBlockMachine(Config.getBlock("biotech.BiotechBlock", BLOCK_ID_PREFIX).getInt(), 0);
		this.milkMoving = new MilkFlowingBlock(Config.getBlock("biotech.MilkFlowing", BLOCK_ID_PREFIX + 1).getInt());
		this.milkStill = new MilkStillBlock(Config.getBlock("biotech.MilkStill", BLOCK_ID_PREFIX + 2).getInt());
		
		Config.save();
		
		if (milkMoving.blockID + 1 != milkStill.blockID)
		{
			throw new RuntimeException("Milk Still id must be Milk Moving id + 1");
		}
		
		/**
		 * Register Milk as a Liquid
		 */
		milkLiquid = LiquidDictionary.getOrCreateLiquid("Milk", new LiquidStack(milkStill, 1));
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Milk", LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(Item.bucketMilk), new ItemStack(Item.bucketEmpty)));
		
		biotechLogger.info("Config loaded");
	}
	
	@Init
	public void load(FMLInitializationEvent event)
	{
		
		proxy.registerRenderers();
		
		/**
		 * Handle the items that will be used in recipes. Just use the string in
		 * the recipe like the milk manager recipe
		 */
		ItemStack itemBioFuel = new ItemStack(OreDictionary.getOreID("bioFuel"), 1, 0);
		ItemStack itemStone = new ItemStack(Block.stone, 1);
		ItemStack FarmMachine = new ItemStack(Biotech.biotechBlockMachine, 1, 0);
		ItemStack WoodMachine = new ItemStack(Biotech.biotechBlockMachine, 1, 1);
		ItemStack FertMachine = new ItemStack(Biotech.biotechBlockMachine, 1, 2);
		ItemStack MineMachine = new ItemStack(Biotech.biotechBlockMachine, 1, 3);
		ItemStack CowMilker = new ItemStack(Biotech.biotechBlockMachine, 1, 4);
		ItemStack BioRefinery = new ItemStack(Biotech.biotechBlockMachine, 1, 5);
		ItemStack UnProgrammed = new ItemStack(Biotech.bioCircuit, 1, 0);
		ItemStack WheatSeeds = new ItemStack(Biotech.bioCircuit, 1, 1);
		ItemStack MelonSeeds = new ItemStack(Biotech.bioCircuit, 1, 2);
		ItemStack PumpkinSeeds = new ItemStack(Biotech.bioCircuit, 1, 3);
		ItemStack Carrots = new ItemStack(Biotech.bioCircuit, 1, 4);
		ItemStack Potatoes = new ItemStack(Biotech.bioCircuit, 1, 5);
		ItemStack RangeUpgrade = new ItemStack(Biotech.bioCircuit, 1, 6);
		ItemStack TreeSappling = new ItemStack(Biotech.bioCircuit, 1, 7);
		ItemStack Pickaxe = new ItemStack(Biotech.bioCircuit, 1, 8);
		ItemStack Shovel = new ItemStack(Biotech.bioCircuit, 1, 9);
		ItemStack Hoe = new ItemStack(Biotech.bioCircuit, 1, 10);
		
		
		/**
		 * Register the TileEntity's
		 */
		GameRegistry.registerTileEntity(BasicMachineTileEntity.class, "BasicMachineTileEntity");
		GameRegistry.registerTileEntity(FarmMachineTileEntity.class, "FarmMachineTileEntity");
		GameRegistry.registerTileEntity(CuttingMachineTileEntity.class, "CuttingMachineTileEntity");
		GameRegistry.registerTileEntity(CowMilkerTileEntity.class, "CowMilkerTileEntity");
		GameRegistry.registerTileEntity(BioRefineryTileEntity.class, "BioRefineryTileEntity");
		
		/**
		 * Handle the blocks
		 */
		GameRegistry.registerBlock(Biotech.biotechBlockMachine, biotechItemBlock.class, "Basic Biotech Block");
		GameRegistry.registerBlock(Biotech.milkMoving, "Milk(Flowing)");
		GameRegistry.registerBlock(Biotech.milkStill, "Milk(Still)");
		
		// Registration
		
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
		int unofficialLanguages = 0;
		unofficialLanguages = langLoad();

		System.out.println(NAME + ": Loaded " + languages + " Official and " + unofficialLanguages + " unofficial languages");
		
		// Recipes
		// TODO Add Recipes for other machines and items
		/*
		 * GameRegistry.addRecipe(new ShapedOreRecipe(FarmMachine, new Object[]
		 * {
		 * "#%#", "@!@", "#$#", '@', Item.hoeStone, '!', "motor", '#',
		 * itemStone, '$', "battery", '%', "basicCircuit" }));
		 */
		GameRegistry.addRecipe(new ShapedOreRecipe(WoodMachine, new Object[] { "#%#", "@!@", "#$#", '@', Item.axeStone, '!', "motor", '#', itemStone, '$', "battery", '%', "basicCircuit" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(CowMilker, new Object[] { "@$@", "@!@", "@#@", '@', Item.ingotIron, '!', "motor", '#', "battery", '$', "basicCircuit" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(BioRefinery, new Object[] { "@$@", "%!%", "@#@", '@', Item.ingotIron, '!', "motor", '#', "battery", '$', "basicCircuit", '%', Item.bucketEmpty }));
		
		NetworkRegistry.instance().registerGuiHandler(this, guiHandler);
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event)
	{
		biotechLogger.info("Biotech fully loaded");
	}
	
	public static File[] ListLanguages()
	{
		String folderDir = "";
		if (MinecraftServer.getServer().isDedicatedServer())
		{
			folderDir = "mods/" + "BiotechLanguages";
		}
		else if (!MinecraftServer.getServer().isDedicatedServer())
		{
			folderDir = Minecraft.getMinecraftDir() + File.separator + "mods" + File.separator + "BiotechLanguages";
		}

		File folder = new File(folderDir);

		if (!folder.exists())
			folder.mkdirs();

		String files;
		File[] listOfFiles = folder.listFiles();

		return listOfFiles;
	}

	public static int langLoad()
	{
		int unofficialLanguages = 0;
		try
		{
			for (File langFile : ListLanguages())
			{
				if (langFile.exists())
				{
					String name = langFile.getName();
					if (name.endsWith(".lang"))
					{
						String lang = name.substring(0, name.length() - 4);
						LanguageRegistry.instance().loadLocalization(langFile.toString(), lang, false);
						unofficialLanguages++;
					}
				}
			}
		}
		catch (Exception e)
		{
		}
		return unofficialLanguages;
	}
}
