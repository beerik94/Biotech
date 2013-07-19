package biotech.dna;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import biotech.Biotech;
import biotech.dna.DNARegistry.DNAInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class itemBioDNA extends Item
{

	private final static String[] subNames = { "Chicken", "Cow", "Pig", "Sheep" };
	// Blank-Bat-Ocelot-Squid-Wolf-Blaze-CaveSpider-Creeper-Enderman-Ghast-Skeleton-Spider-Zombie

	private Icon[] icons = new Icon[subNames.length];

	public itemBioDNA(int id)
	{
		super(id);
		setCreativeTab(Biotech.tabBiotech);
		setUnlocalizedName("DNA");
		setMaxStackSize(1);
	}

	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		for (int i = 0; i < subNames.length; i++)
		{
			this.icons[i] = iconRegister.registerIcon(Biotech.TEXTURE_NAME_PREFIX + subNames[i]);
		}
	}

	@Override
	public Icon getIconFromDamage(int damage)
	{
		return this.icons[damage];
	}

	@Override
	public int getMetadata(int damageValue)
	{
		return damageValue;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		return this.getUnlocalizedName();
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		if (itemStack != null)
		{
			List<String> effects = this.getDNAEffects(itemStack);

			if (effects != null && effects.size() > 0)
			{
				// TODO add local translation using the entity.class
				par3List.add("\u00a75==" + this.getItemDataStored(itemStack).getCompoundTag("DNA").getString("creatureName") + " DNA==");
				for (int i = 0; i < 4 && i < effects.size(); i++)
				{
					// TODO sort effects for neg and pos as well show best effects if can
					par3List.add((i + 1) + ": " + effects.get(i));
				}
				if (effects.size() > 4)
				{
					par3List.add("\u00a7c!Long List!");
				}

			}
			else
			{
				par3List.add("Basic DNA");
			}
		}
	}

	/**
	 * Used this to create a new DNA item with NBTTagCompound data for the DNAInfo
	 * 
	 * @param stack - itemStack that contains this item
	 * @param info - DNAInfo to be encoded to the DNA Item
	 * @param effects - effects to start the DNA strand off with
	 * @return new ItemStack with DNA data encoded. That is if stack was not null
	 */
	public static ItemStack createNewDNA(ItemStack stack, DNAInfo info, String... effects)
	{
		if (stack != null && info != null)
		{
			ItemStack dnaStack = stack.copy();
			NBTTagCompound tag = getItemDataStored(stack);
			NBTTagCompound dna = tag.getCompoundTag("DNA");
			dna.setString("creatureName", info.name);
			if (effects != null)
			{
				int e = 0;
				for (int i = 0; i < effects.length; i++)
				{
					if (isValidDnaEffect(effects[i]))
					{
						dna.setString("upgrade" + e, effects[i]);
						e++;
					}
				}
				dna.setInteger("count", e);
			}
			tag.setCompoundTag("DNA", dna);
			dnaStack.setTagCompound(tag);
			return dnaStack;
		}
		return stack;
	}

	/**
	 * Gets the list of DNA effect/attributes from the Item's NBT
	 */
	public static List<String> getDNAEffects(ItemStack stack)
	{
		NBTTagCompound tag = getItemDataStored(stack);
		List<String> effects = new ArrayList<String>();
		if (tag.hasKey("DNA"))
		{
			NBTTagCompound dna = tag.getCompoundTag("DNA");
			int count = dna.getInteger("count");
			for (int i = 0; i < count; i++)
			{
				String string = dna.getString("upgrade" + i);
				if (string != null && isValidDnaEffect(string))
				{
					effects.add(string);
				}
			}
		}
		return effects;
	}

	/**
	 * Sets the DNA tag effect list of the itemStack. Doesn't check for valid tags
	 */
	public static void setEffects(ItemStack stack, List<String> effects)
	{
		if (stack != null && effects != null)
		{
			NBTTagCompound tag = getItemDataStored(stack);
			NBTTagCompound dna = tag.getCompoundTag("DNA");
			dna.setInteger("count", effects.size());
			for (int i = 0; i < effects.size(); i++)
			{
				dna.setString("upgrade" + i, effects.get(i));
			}
			stack.stackTagCompound.setCompoundTag("DNA", dna);
		}
	}

	/**
	 * used to check if an effect is valid. Right now it always returns true but later this will be
	 * check against a list of valid effects
	 */
	public static boolean isValidDnaEffect(String effect)
	{
		return true;
	}

	public static NBTTagCompound getItemDataStored(ItemStack stack)
	{
		NBTTagCompound tag = stack.getTagCompound();

		if (tag == null)
		{
			tag = new NBTTagCompound();
			NBTTagCompound dna = new NBTTagCompound();
			dna.setInteger("count", 0);
			dna.setString("creatureName", "Unkown");
			tag.setCompoundTag("DNA", dna);

		}
		return tag;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(int i, CreativeTabs tab, List subItems)
	{
		ItemStack stack = new ItemStack(this, 1, 0);
		subItems.add(stack);
		for (Entry<String, DNAInfo> entry : DNARegistry.DNAMap.entrySet())
		{
			subItems.add(createNewDNA(stack, entry.getValue(), "Creative"));
		}
	}
}
