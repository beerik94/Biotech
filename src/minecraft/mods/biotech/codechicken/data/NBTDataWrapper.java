package biotech.codechicken.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.liquids.LiquidStack;
import biotech.codechicken.vec.BlockCoord;

public class NBTDataWrapper implements MCDataInput, MCDataOutput
{
    private NBTTagList readList;
    private int readTag = 0;
    private NBTTagList writeList;
    
    public NBTDataWrapper(NBTTagList input)
    {
        readList = input;
    }
    
    public NBTDataWrapper()
    {
        writeList = new NBTTagList();
    }
    
    public NBTTagList toTag()
    {
        return writeList;
    }
    
    @Override
    public void writeLong(long l)
    {
        writeList.appendTag(new NBTTagLong(null, l));
    }

    @Override
    public void writeInt(int i)
    {
        writeList.appendTag(new NBTTagInt(null, i));
    }

    @Override
    public void writeShort(int s)
    {
        writeList.appendTag(new NBTTagShort(null, (short) s));
    }

    @Override
    public void writeByte(int b)
    {
        writeList.appendTag(new NBTTagByte(null, (byte) b));
    }

    @Override
    public void writeDouble(double d)
    {
        writeList.appendTag(new NBTTagDouble(null, d));
    }

    @Override
    public void writeFloat(float f)
    {
        writeList.appendTag(new NBTTagFloat(null, f));
    }

    @Override
    public void writeBoolean(boolean b)
    {
        writeList.appendTag(new NBTTagByte(null, (byte) (b ? 1 : 0)));
    }

    @Override
    public void writeChar(char c)
    {
        writeList.appendTag(new NBTTagShort(null, (short)c));
    }

    @Override
    public void writeByteArray(byte[] array)
    {
        writeList.appendTag(new NBTTagByteArray(null, array));
    }

    @Override
    public void writeString(String s)
    {
        writeList.appendTag(new NBTTagString(null, s));
    }

    @Override
    public void writeCoord(int x, int y, int z)
    {
        writeInt(x);
        writeInt(y);
        writeInt(z);
    }

    @Override
    public void writeCoord(BlockCoord coord)
    {
        writeCoord(coord.x, coord.y, coord.z);
    }

    @Override
    public void writeNBTTagCompound(NBTTagCompound tag)
    {
        writeList.appendTag(tag);
    }

    @Override
    public void writeItemStack(ItemStack stack)
    {
        writeList.appendTag(stack.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void writeLiquidStack(LiquidStack liquid)
    {
        writeList.appendTag(liquid.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public long readLong()
    {
        return ((NBTTagLong)readTag()).data;
    }

    @Override
    public int readInt()
    {
        return ((NBTTagInt)readTag()).data;
    }

    @Override
    public short readShort()
    {
        return ((NBTTagShort)readTag()).data;
    }

    @Override
    public int readUnsignedShort()
    {
        return ((NBTTagShort)readTag()).data & 0xFFFF;
    }

    @Override
    public byte readByte()
    {
        return ((NBTTagByte)readTag()).data;
    }

    @Override
    public int readUnsignedByte()
    {
        return ((NBTTagByte)readTag()).data & 0xFF;
    }

    @Override
    public double readDouble()
    {
        return ((NBTTagDouble)readTag()).data;
    }

    @Override
    public float readFloat()
    {
        return ((NBTTagFloat)readTag()).data;
    }

    @Override
    public boolean readBoolean()
    {
        return ((NBTTagByte)readTag()).data != 0;
    }

    @Override
    public char readChar()
    {
        return (char)((NBTTagShort)readTag()).data;
    }

    @Override
    public byte[] readByteArray(int length)
    {
        return ((NBTTagByteArray)readTag()).byteArray;
    }

    @Override
    public String readString()
    {
        return ((NBTTagString)readTag()).data;
    }

    @Override
    public BlockCoord readCoord()
    {
        return new BlockCoord(readInt(), readInt(), readInt());
    }

    @Override
    public NBTTagCompound readNBTTagCompound()
    {
        return (NBTTagCompound)readTag();
    }

    @Override
    public ItemStack readItemStack()
    {
        return ItemStack.loadItemStackFromNBT(readNBTTagCompound());
    }

    @Override
    public LiquidStack readLiquidStack()
    {
        return LiquidStack.loadLiquidStackFromNBT(readNBTTagCompound());
    }
    
    private NBTBase readTag()
    {
        return readList.tagAt(readTag++);
    }
}
