package biotech.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import biotech.Biotech;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class bioCheeseItem extends Item
{
	/** The amount this food item heals the player. */
    private final int healAmount;
    private final float saturationModifier;
	
	public bioCheeseItem(int par1)
	{
		super(par1);
		this.setMaxStackSize(64);
		this.healAmount = 3;
        this.saturationModifier = 0.3F;
		this.setCreativeTab(Biotech.tabBiotech);
		this.setUnlocalizedName("bioCheese");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon(Biotech.TEXTURE_NAME_PREFIX + "KaasHighPoly");
	}
	
	@Override
	public Icon getIconFromDamage(int damage)
	{
		return this.itemIcon;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Returns True is the item is renderer in full 3D when hold.
	 */
	public boolean isFull3D()
	{
		return true;
	}
	
	public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		--par1ItemStack.stackSize;
		par3EntityPlayer.getFoodStats().addStats(this.getHealAmount(), this.getSaturationModifier());
		par2World.playSoundAtEntity(par3EntityPlayer, "random.burp", 0.5F, par2World.rand.nextFloat() * 0.1F + 0.9F);
		return par1ItemStack;
	}
	
	/**
	 * How long it takes to use or consume an item
	 */
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		return 32;
	}
	
	/**
	 * returns the action that specifies what animation to play when the items
	 * is being used
	 */
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
	{
		return EnumAction.eat;
	}
	
	/**
	 * Called whenever this item is equipped and the right mouse button is
	 * pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
	}
	
	public int getHealAmount()
	{
		return this.healAmount;
	}
	
	/**
	 * gets the saturationModifier of the ItemFood
	 */
	public float getSaturationModifier()
	{
		return this.saturationModifier;
	}
}
