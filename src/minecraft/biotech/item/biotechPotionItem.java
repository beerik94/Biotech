package biotech.item;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import biotech.Biotech;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StringUtils;

public class biotechPotionItem extends Potion
{
	
	public static final biotechPotionItem[]	potionTypes		= new biotechPotionItem[32];
	public static final biotechPotionItem	removeEffects	= (new biotechPotionItem(1, false, 8171462)).setPotionName("potion.removeEffects");
	
	/** The Id of a Potion object. */
	public final int						id;
	
	/** The name of the Potion. */
	private String							name			= "";
	
	/** The index for the icon displayed when the potion effect is active. */
	private int								statusIconIndex	= -1;
	
	/**
	 * This field indicated if the effect is 'bad' - negative - for the entity.
	 */
	private final boolean					isBadEffect;
	private double							effectiveness;
	private boolean							usable;
	
	/** Is the color of the liquid for this potion. */
	private final int						liquidColor;
	
	protected biotechPotionItem(int par1, boolean par2, int par3)
	{
		super(par1, par2, par3);
		this.id = par1;
		potionTypes[par1] = this;
		this.isBadEffect = par2;
		
		if (par2)
		{
			this.effectiveness = 0.5D;
		}
		else
		{
			this.effectiveness = 1.0D;
		}
		
		this.liquidColor = par3;
		
	}
	
	/**
	 * Sets the index for the icon displayed in the player's inventory when the
	 * status is active.
	 */
	protected biotechPotionItem setIconIndex(int par1, int par2)
	{
		this.statusIconIndex = par1 + par2 * 8;
		return this;
	}
	
	/**
	 * returns the ID of the potion
	 */
	public int getId()
	{
		return this.id;
	}
	
	public void performEffect(EntityLiving par1EntityLiving, int par2)
	{
		if (this.id == removeEffects.id)
		{
			if (par1EntityLiving.getHealth() < par1EntityLiving.getMaxHealth())
			{
				par1EntityLiving.heal(1);
			}
		}
	}
	
	/**
	 * Hits the provided entity with this potion's instant effect.
	 */
	public void affectEntity(EntityLiving par1EntityLiving, EntityLiving par2EntityLiving, int par3, double par4)
	{
		int var6;
		
		if ((this.id != heal.id || par2EntityLiving.isEntityUndead()) && (this.id != harm.id || !par2EntityLiving.isEntityUndead()))
		{
			if (this.id == harm.id && !par2EntityLiving.isEntityUndead() || this.id == heal.id && par2EntityLiving.isEntityUndead())
			{
				var6 = (int) (par4 * (double) (6 << par3) + 0.5D);
				
				if (par1EntityLiving == null)
				{
					par2EntityLiving.attackEntityFrom(DamageSource.magic, var6);
				}
				else
				{
					par2EntityLiving.attackEntityFrom(DamageSource.causeIndirectMagicDamage(par2EntityLiving, par1EntityLiving), var6);
				}
			}
		}
		else
		{
			var6 = (int) (par4 * (double) (6 << par3) + 0.5D);
			par2EntityLiving.heal(var6);
		}
	}
	
	/**
	 * Returns true if the potion has an instant effect instead of a continuous
	 * one (eg Harming)
	 */
	public boolean isInstant()
	{
		return false;
	}
	
	/**
	 * checks if Potion effect is ready to be applied this tick.
	 */
	public boolean isReady(int par1, int par2)
	{
		int var3;
		
		if (this.id != removeEffects.id)
		{
			var3 = 25 >> par2;
			return var3 > 0 ? par1 % var3 == 0 : true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Set the potion name.
	 */
	public biotechPotionItem setPotionName(String par1Str)
	{
		this.name = par1Str;
		return this;
	}
	
	/**
	 * returns the name of the potion
	 */
	public String getName()
	{
		return this.name;
	}
	
	protected biotechPotionItem setEffectiveness(double par1)
	{
		this.effectiveness = par1;
		return this;
	}
	
	@SideOnly(Side.CLIENT)
	/**
	 * Returns true if the potion has a associated status icon to display in then inventory when active.
	 */
	public boolean hasStatusIcon()
	{
		return this.statusIconIndex >= 0;
	}
	
	@SideOnly(Side.CLIENT)
	/**
	 * Returns the index for the icon to display when the potion is active.
	 */
	public int getStatusIconIndex()
	{
		return this.statusIconIndex;
	}
	
	@SideOnly(Side.CLIENT)
	/**
	 * This method returns true if the potion effect is bad - negative - for the entity.
	 */
	public boolean isBadEffect()
	{
		return this.isBadEffect;
	}
	
	@SideOnly(Side.CLIENT)
	public static String getDurationString(PotionEffect par0PotionEffect)
	{
		int var1 = par0PotionEffect.getDuration();
		return StringUtils.ticksToElapsedTime(var1);
	}
	
	public double getEffectiveness()
	{
		return this.effectiveness;
	}
	
	public boolean isUsable()
	{
		return this.usable;
	}
	
	/**
	 * Returns the color of the potion liquid.
	 */
	public int getLiquidColor()
	{
		return this.liquidColor;
	}
}
