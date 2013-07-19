package biotech.dna;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import biotech.Biotech;
import biotech.dna.DNARegistry.DNAData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemToolSyringe extends Item
{

	public ItemToolSyringe(int id)
	{
		super(id);
		setCreativeTab(Biotech.tabBiotech);
		setUnlocalizedName("Syringe");
		setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.itemIcon = par1IconRegister.registerIcon(Biotech.NAME + ":seringe");
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityLiving entity)
	{
		if (entity != null && !entity.worldObj.isRemote)
		{
			DNAData data = DNARegistry.getDNAFor(entity);
			if (data != null)
			{
				int delay = EntityTickHandler.getDelay(entity);
				if (delay > 0)
				{
					return true;
				}
				EntityTickHandler.onHarvestDna(entity, -1);
				ItemStack stack = itemBioDNA.createNewDNA(new ItemStack(Biotech.bioDNA, 1, 0), data.info, data.effects);

				EntityItem entityitem = new EntityItem(entity.worldObj, entity.posX, entity.posY + 1, entity.posZ, stack);
				entityitem.delayBeforeCanPickup = 10;

				entity.worldObj.spawnEntityInWorld(entityitem);
				return true;
			}
		}

		return false;
	}
}
