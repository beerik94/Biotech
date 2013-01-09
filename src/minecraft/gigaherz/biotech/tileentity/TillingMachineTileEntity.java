package gigaherz.biotech.tileentity;

import gigaherz.biotech.Biotech;

import gigaherz.biotech.CommandCircuit;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.EnumSet;

import com.google.common.io.ByteArrayDataInput;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityReceiver;
import cpw.mods.fml.common.network.PacketDispatcher;

// Default machine TileEntity
// Has a power connection at the back
// Has a powered state
// Has an inventory

public class TillingMachineTileEntity extends BasicMachineTileEntity implements IInventory, ISidedInventory, IPacketReceiver
{
    public int currentX = 0;
    public int currentZ = 0;

    public int minX, maxX;
    public int minZ, maxZ;
	    
    public TillingMachineTileEntity()
    {
        super();
        
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        //this.progressTime = tagCompound.getShort("Progress");

    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        //tagCompound.setShort("Progress", (short)this.progressTime);

    }

    @Override
    public String getInvName()
    {
        return "TillingMachine";
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        
  
    }

}
