package biotech.codechicken.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.liquids.LiquidStack;
import biotech.codechicken.vec.BlockCoord;

public interface MCDataOutput
{
    public void writeLong(long l);
    public void writeInt(int i);
    public void writeShort(int s);
    public void writeByte(int b);
    public void writeDouble(double d);
    public void writeFloat(float f);
    public void writeBoolean(boolean b);
    public void writeChar(char c);
    public void writeByteArray(byte[] array);
    public void writeString(String s);
    public void writeCoord(int x, int y, int z);
    public void writeCoord(BlockCoord coord);
    public void writeNBTTagCompound(NBTTagCompound tag);
    public void writeItemStack(ItemStack stack);
    public void writeLiquidStack(LiquidStack liquid);
}
