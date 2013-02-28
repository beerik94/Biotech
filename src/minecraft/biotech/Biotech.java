package biotech;

import java.io.File;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

@Mod(modid = "Biotech", name = "Biotech", version = "0.1.7", dependencies = "after:hydraulic")
@NetworkMod(channels = Biotech.CHANNEL, clientSideRequired = true, serverSideRequired = false, connectionHandler = ConnectionHandler.class, packetHandler = PacketManager.class)
public class Biotech {
	// The instance of your mod that Forge uses.
	@Instance("Biotech")
	public static Biotech instance;

	// Texture file paths
	public static final String FILE_PATH = "/resources/biotech/textures/";
	public static final String BLOCK_TEXTURE_FILE = FILE_PATH + "block.png";
	public static final String ITEM_TEXTURE_FILE = FILE_PATH + "items.png";

	// Public channel used in all communication
	public static final String CHANNEL = "Biotech";

	// First Item ID
	private final static int firstItemId = 24400;

	// First Block ID
	private final static int firstBlockId = 2450;

	// All Item ID's
	private final static int defaultBioItemId = firstItemId + 1;

	// All Block ID's
	private final static int defaultBiotechBlockId = firstBlockId + 1;

	// Default config loader
	public static final Configuration Config = new Configuration(new File(
			Loader.instance().getConfigDir(), "Biotech/Biotech.cfg"));

	// Item templates
	public static Item biotechPotionItem;
	public static bioCircuitItem bioCircuit;
	// Metadata for BioCircuit
	// 0 == unprogrammed
	// 1 == wheatseeds
	// 2 == melonseeds
	// 3 == pumpkinseeds
	// 4 == carrots
	// 5 == potatoes

	// Tilling machine Circuits
	public static ItemStack bioCircuitEmpty;
	public static ItemStack bioCircuitWheatSeeds;
	public static ItemStack bioCircuitMelonSeeds;
	public static ItemStack bioCircuitPumpkinSeeds;
	public static ItemStack bioCircuitCarrots;
	public static ItemStack bioCircuitPotatoes;
	public static ItemStack bioCircuitRangeUpgrade;

	// Mekanism bioFuel
	public static ItemStack BioFuel = new ItemStack(
			OreDictionary.getOreID("itemBioFuel"), 1, 0);

	// Block templates
	public static Block biotechBlockMachine;
	public static Block milkMoving;
	public static Block milkStill;

	// Metadata for biotechBlockMachine
	// 0 == Farm
	// 1 == Woodcutter
	// 2 == Fertilizer
	// 3 == Miner
	// 4 == Filler
	// 5 == Cow Milker
	// 6 == BioRefinery

	// Liquid Stack Milk
	public static LiquidStack milkLiquid;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "biotech.client.ClientProxy", serverSide = "biotech.common.CommonProxy")
	public static CommonProxy proxy;

	// Chat commands
	public static Property enableChatCommand;

	// Gui Handler
	private GuiHandler guiHandler = new GuiHandler();

	// Biotech's own CreativeTab
	public static BiotechCreativeTab tabBiotech = new BiotechCreativeTab();

	// The logger for Biotech
	public static Logger biotechLogger = Logger.getLogger("Biotech");

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		UniversalElectricity.register(this, UniversalElectricity.MAJOR_VERSION,
				UniversalElectricity.MINOR_VERSION,
				UniversalElectricity.REVISION_VERSION, true);

		biotechLogger.setParent(FMLLog.getLogger());
		biotechLogger.info("Starting Biotech");
		biotechLogger.info("Loading config");
		Config.load();
		Property prop;

		/**
		 * Define the items and blocks.
		 */
		this.bioCircuit = new bioCircuitItem(Config.getItem(
				"biotech.BioCircuit", defaultBioItemId).getInt());

		this.biotechBlockMachine = new BiotechBlockMachine(Config.getBlock(
				"biotech.BiotechBlock", defaultBiotechBlockId).getInt(), 1)
				.setHardness(0.5F).setStepSound(Block.soundMetalFootstep);
		this.milkMoving = new MilkFlowingBlock(Config.getBlock(
				"biotech.MilkFlowing", defaultBiotechBlockId + 1).getInt(), 4);
		this.milkStill = new MilkStillBlock(Config.getBlock(
				"biotech.MilkStill", defaultBiotechBlockId + 2).getInt(), 4);

		/**
		 * Define the subitems
		 */
		this.bioCircuitEmpty = bioCircuit.getStack(1, 0);
		this.bioCircuitWheatSeeds = bioCircuit.getStack(1, 1);
		this.bioCircuitMelonSeeds = bioCircuit.getStack(1, 2);
		this.bioCircuitPumpkinSeeds = bioCircuit.getStack(1, 3);
		this.bioCircuitCarrots = bioCircuit.getStack(1, 4);
		this.bioCircuitPotatoes = bioCircuit.getStack(1, 5);
		this.bioCircuitRangeUpgrade = bioCircuit.getStack(1, 6);

		/**
		 * Enable the chat commands
		 */
		Property enableChatCommand = Config.get("general", "enableChatCommand",
				true);

		// guiHandler.preInit();

		Config.save();

		biotechLogger.info("Config loaded");
	}

	public static void initCommands(FMLServerStartingEvent event) {
		if (enableChatCommand.value == "true") {
			event.registerServerCommand(new CmdWorker());
			biotechLogger.info("Biotech Command Enabled");
		}
	}

	@Init
	public void load(FMLInitializationEvent event) {

		proxy.registerRenderers();

		/**
		 * Handle the items that will be used in recipes. Just use the string in
		 * the recipe like the milk manager recipe
		 */
		ItemStack itemBioFuel = new ItemStack(
				OreDictionary.getOreID("bioFuel"), 1, 0);
		ItemStack itemStone = new ItemStack(Block.stone, 1);
		ItemStack FarmMachine = new ItemStack(Biotech.biotechBlockMachine, 1, 0);
		ItemStack WoodMachine = new ItemStack(Biotech.biotechBlockMachine, 1, 1);
		ItemStack FertMachine = new ItemStack(Biotech.biotechBlockMachine, 1, 2);
		ItemStack MineMachine = new ItemStack(Biotech.biotechBlockMachine, 1, 3);
		ItemStack FillMachine = new ItemStack(Biotech.biotechBlockMachine, 1, 4);
		ItemStack CowMilker = new ItemStack(Biotech.biotechBlockMachine, 1, 5);
		ItemStack BioRefinery = new ItemStack(Biotech.biotechBlockMachine, 1, 6);

		/**
		 * Register the TileEntity's
		 */
		GameRegistry.registerTileEntity(BasicMachineTileEntity.class,
				"BasicMachineTileEntity");
		GameRegistry.registerTileEntity(FarmMachineTileEntity.class,
				"FarmMachineTileEntity");
		GameRegistry.registerTileEntity(CuttingMachineTileEntity.class,
				"CuttingMachineTileEntity");
		GameRegistry.registerTileEntity(CowMilkerTileEntity.class,
				"CowMilkerTileEntity");
		GameRegistry.registerTileEntity(BioRefineryTileEntity.class,
				"BioRefineryTileEntity");

		/**
		 * Register Milk as a Liquid
		 */
		milkLiquid = LiquidDictionary.getOrCreateLiquid("Milk",
				new LiquidStack(milkStill, 1));
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(
				LiquidDictionary.getLiquid("Milk",
						LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(
						Item.bucketMilk), new ItemStack(Item.bucketEmpty)));

		/**
		 * Handle the blocks
		 */
		GameRegistry.registerBlock(Biotech.biotechBlockMachine,
				biotechItemBlock.class, "Basic Biotech Block");
		GameRegistry.registerBlock(Biotech.milkMoving, "Milk(Flowing)");
		GameRegistry.registerBlock(Biotech.milkStill, "Milk(Still)");

		// Registration

		/**
		 * Handle localization and add names for all items
		 */

		// Blocks
		LanguageRegistry.addName(milkMoving, "Milk(Flowing)");
		LanguageRegistry.addName(milkStill, "Milk(Still)");

		// Items
		LanguageRegistry.addName(bioCircuit, "Bio Circuit");

		// Subitems
		LanguageRegistry.addName(bioCircuitEmpty, "Bio Circuit - Empty");
		LanguageRegistry.addName(bioCircuitWheatSeeds,
				"Bio Circuit - Wheat Seeds");
		LanguageRegistry.addName(bioCircuitMelonSeeds,
				"Bio Circuit - Melon Seeds");
		LanguageRegistry.addName(bioCircuitPumpkinSeeds,
				"Bio Circuit - Pumpkin Seeds");
		LanguageRegistry.addName(bioCircuitCarrots, "Bio Circuit - Carrots");
		LanguageRegistry.addName(bioCircuitPotatoes, "Bio Circuit - Potaties");
		LanguageRegistry.addName(bioCircuitRangeUpgrade,
				"Bio Circuit - Range Upgrade");

		// Subblocks

		LanguageRegistry.instance().addStringLocalization(
				"tile.BiotechBlockMachine.0.name", "Farm");
		LanguageRegistry.instance().addStringLocalization(
				"tile.BiotechBlockMachine.1.name", "Woodcutter");
		LanguageRegistry.instance().addStringLocalization(
				"tile.BiotechBlockMachine.2.name", "Fertilizer");
		LanguageRegistry.instance().addStringLocalization(
				"tile.BiotechBlockMachine.3.name", "Miner");
		LanguageRegistry.instance().addStringLocalization(
				"tile.BiotechBlockMachine.4.name", "Filler");
		LanguageRegistry.instance().addStringLocalization(
				"tile.BiotechBlockMachine.5.name", "Cow Milker");
		LanguageRegistry.instance().addStringLocalization(
				"tile.BiotechBlockMachine.6.name", "Bio Refinery");

		// CreativeTab
		LanguageRegistry.instance().addStringLocalization(
				"itemGroup.tabBiotech", "Biotech");

		// Recipes
		// TODO Add Recipes for other machines
		/*
		GameRegistry.addRecipe(new ShapedOreRecipe(TillMachine, new Object[] {
				"#%#", "@!@", "#$#", '@', Item.hoeStone, '!', "motor", '#',
				itemStone, '$', "battery", '%', "basicCircuit" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(PlanMachine, new Object[] {
				"#%#", "@!@", "#$#", '@', Item.appleRed, '!', "motor", '#',
				itemStone, '$', "battery", '%', "basicCircuit" }));
				*/
		GameRegistry.addRecipe(new ShapedOreRecipe(WoodMachine, new Object[] {
				"#%#", "@!@", "#$#", '@', Item.axeStone, '!', "motor", '#',
				itemStone, '$', "battery", '%', "basicCircuit" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(CowMilker, new Object[] {
				"@$@", "@!@", "@#@", '@', Item.ingotIron, '!', "motor", '#',
				"battery", '$', "basicCircuit" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(BioRefinery, new Object[] {
				"@$@", "%!%", "@#@", '@', Item.ingotIron, '!', "motor", '#', 
				"battery", '$', "basicCircuit", '%', Item.bucketEmpty })); 

		NetworkRegistry.instance().registerGuiHandler(this, guiHandler);
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
		biotechLogger.info("Biotech fully loaded");
	}
}
