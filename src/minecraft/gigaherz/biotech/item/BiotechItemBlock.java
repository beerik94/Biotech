package gigaherz.biotech.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class biotechItemBlock extends ItemBlock
{
	public biotechItemBlock(int id)
	{
		super(id);
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
		int metadata = itemstack.getItemDamage();

		return Block.blocksList[this.getBlockID()].getBlockName() + "." + metadata;
	}

	@Override
	public String getItemName()
	{
		return Block.blocksList[this.getBlockID()].getBlockName() + ".0";
	}
}