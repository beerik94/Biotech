package biotech.dna;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
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
	public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityLiving par2EntityLiving)
	{
		DNAData data = DNARegistry.getDNAFor(par2EntityLiving);
		if (data != null)
		{
			int delay = par2EntityLiving.getEntityData().getInteger("dnaDelay");
			if (delay > 0)
			{
				EntityTickHandler.onHarvestDna(par2EntityLiving, delay);
				return true;
			}
			System.out.println("[BioTech]Debug: DNA extracted");
			System.out.println("[BioTech]DNAData:" + data.toString());
			EntityTickHandler.onHarvestDna(par2EntityLiving, -1);
			return true;
		}

		return false;
	}

}
