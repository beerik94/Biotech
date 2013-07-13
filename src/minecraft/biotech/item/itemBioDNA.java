package biotech.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import biotech.Biotech;
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
		setHasSubtypes(true);
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
		if (itemStack == null || itemStack.getItemDamage() >= this.subNames.length)
		{
			return this.getUnlocalizedName();
		}
		return this.getUnlocalizedName() + "." + this.subNames[itemStack.getItemDamage()];
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		if (itemStack != null)
		{
			NBTTagCompound tag = this.getItemDataStored(itemStack);

			if (tag != null)
			{
				par3List.add("==DNA Changes==");
				par3List.add("1: " + tag.getString("upgradeOne"));
				par3List.add("2: " + tag.getString("upgradeTwo"));
				par3List.add("3: " + tag.getString("upgradeThree"));
				par3List.add("4: " + tag.getString("upgradeFour"));
				par3List.add("5: " + tag.getString("upgradeFive"));
			}
			else
			{
				par3List.add("Empty");
			}
		}
	}
	
	public NBTTagCompound getItemDataStored(ItemStack stack)
	{
		NBTTagCompound tag = stack.getTagCompound();
		
		if(tag == null)
		{
			tag = new NBTTagCompound();
		}
		return tag;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(int i, CreativeTabs tab, List subItems)
	{
		for (int meta = 0; meta < subNames.length; meta++)
		{
			subItems.add(new ItemStack(this, 1, meta));
		}
	}
}
