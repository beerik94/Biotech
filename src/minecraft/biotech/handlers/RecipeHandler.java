package biotech.handlers;

import biotech.Biotech;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeHandler 
{
	public static void BiotechRecipes()
	{
		/**
		 * Handle the items that will be used in recipes. Just use the string in
		 * the recipe like the milk manager recipe
		 */
		ItemStack itemBiotanium = new ItemStack(Biotech.BiotaniumIngot, 1, 0);
		ItemStack FarmMachine = new ItemStack(Biotech.biotechBlockMachine, 1, 0);
		ItemStack WoodMachine = new ItemStack(Biotech.biotechBlockMachine, 1, 1);
		ItemStack FertMachine = new ItemStack(Biotech.biotechBlockMachine, 1, 2);
		//ItemStack MineMachine = new ItemStack(Biotech.biotechBlockMachine, 1, 3);
		ItemStack CowMilker = new ItemStack(Biotech.biotechBlockMachine, 1, 4);
		ItemStack BioRefinery = new ItemStack(Biotech.biotechBlockMachine, 1, 5);
		ItemStack UnProgrammed = new ItemStack(Biotech.bioCircuit, 1, 0);
		ItemStack RangeUpgrade = new ItemStack(Biotech.bioCircuit, 1, 1);
		
		//Machines
		GameRegistry.addRecipe(new ShapedOreRecipe(FarmMachine, new Object[] { "#%#", "@@@", "###", '@', Item.hoeStone, '#', itemBiotanium, '%', "basicCircuit" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(WoodMachine, new Object[] { "#%#", "@@@", "###", '@', Item.axeStone, '#', itemBiotanium, '%', "basicCircuit" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(CowMilker, new Object[] { "@$@", "!@!", "@!@", '@', itemBiotanium, '!', Item.bucketMilk, '$', "basicCircuit" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(BioRefinery, new Object[] { "@$@", "%!%", "@!@", '@', itemBiotanium, '!', Item.bucketMilk, '$', "basicCircuit", '%', Item.bucketEmpty }));
		GameRegistry.addRecipe(new ShapedOreRecipe(FertMachine, new Object[] { "@!@", "@#@", "@!@", '@', itemBiotanium, '!', Item.bone, '#', "basicCircuit"}));
		
		//Items
		GameRegistry.addRecipe(new ShapelessOreRecipe(UnProgrammed, new Object[] { "basicCircuit", Item.redstone, "copperWire"}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(RangeUpgrade, new Object[] { UnProgrammed, Item.redstone, Item.compass}));
	
		FurnaceRecipes.smelting().addSmelting(Biotech.Biotanium.blockID, 0, new ItemStack(Biotech.BiotaniumIngot), 10F);
	}
	/*
	public static void MekanismRecipes()
	{
		/**
		 * Handle the items that will be used in recipes. Just use the string in
		 * the recipe like the milk manager recipe
		 *
		ItemStack FarmMachine = new ItemStack(Biotech.biotechBlockMachine, 1, 0);
		ItemStack WoodMachine = new ItemStack(Biotech.biotechBlockMachine, 1, 1);
		ItemStack FertMachine = new ItemStack(Biotech.biotechBlockMachine, 1, 2);
		//ItemStack MineMachine = new ItemStack(Biotech.biotechBlockMachine, 1, 3);
		ItemStack CowMilker = new ItemStack(Biotech.biotechBlockMachine, 1, 4);
		ItemStack BioRefinery = new ItemStack(Biotech.biotechBlockMachine, 1, 5);
		ItemStack UnProgrammed = new ItemStack(Biotech.bioCircuit, 1, 0);
		ItemStack RangeUpgrade = new ItemStack(Biotech.bioCircuit, 1, 1);
		
		//Machines
		GameRegistry.addRecipe(new ShapedOreRecipe(FarmMachine, new Object[] { "#%#", "@@@", "###", '@', Item.hoeStone, '#', "ingotSteel", '%', "basicCircuit" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(WoodMachine, new Object[] { "#%#", "@@@", "###", '@', Item.axeStone, '#', "ingotSteel", '%', "basicCircuit" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(CowMilker, new Object[] { "@$@", "!@!", "@!@", '@', "ingotSteel", '!', Item.bucketMilk, '$', "basicCircuit" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(BioRefinery, new Object[] { "@$@", "%!%", "@!@", '@', "ingotSteel", '!', Item.bucketMilk, '$', "basicCircuit", '%', Item.bucketEmpty }));
		GameRegistry.addRecipe(new ShapedOreRecipe(FertMachine, new Object[] { "@!@", "@#@", "@!@", '@', "ingotSteel", '!', Item.bone, '#', "basicCircuit"}));
		
		//Items
		GameRegistry.addRecipe(new ShapelessOreRecipe(UnProgrammed, new Object[] { "basicCircuit", Item.redstone, "copperWire"}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(RangeUpgrade, new Object[] { UnProgrammed, Item.redstone, Item.compass}));
	
		FurnaceRecipes.smelting().addSmelting(Biotech.Biotanium.blockID, 0, new ItemStack(Biotech.BiotaniumIngot), 10F);
	}
*/
}
