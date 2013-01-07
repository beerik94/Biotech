package gigaherz.biotech.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BiotechItemBlock extends ItemBlock
{
	public BiotechItemBlock(int id)
	{
		super(id);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	public String getItemNameIS(ItemStack itemstack)
	{
		int metadata = 0;

		return Block.blocksList[this.getBlockID()].getBlockName() + "." + metadata;
	}

	@Override
	public String getItemName()
	{
		return Block.blocksList[this.getBlockID()].getBlockName() + ".0";
	}
}