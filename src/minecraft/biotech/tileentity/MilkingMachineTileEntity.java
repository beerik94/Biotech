package biotech.tileentity;

import java.util.ArrayList;
import java.util.List;

import liquidmechanics.api.IColorCoded;
import liquidmechanics.api.IReadOut;
import liquidmechanics.api.helpers.ColorCode;
import liquidmechanics.api.liquids.IPressure;
import liquidmechanics.api.liquids.LiquidData;
import liquidmechanics.api.liquids.LiquidHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ITankContainer;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.implement.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import biotech.Biotech;

import com.google.common.io.ByteArrayDataInput;


public class MilkingMachineTileEntity extends BasicMachineTileEntity implements IPacketReceiver, IColorCoded, IPressure,IReadOut
{
        /*
         * Delete this after reading it LiQuid
         *
         * Ok first off you only want to use ITankContainer if you plan on accepting and storing liquids
         * In most cases you can get away with not using it if you know the liquid your going to use
         * and store the amount as an int.
         *
         * Second only pressure liquids you are using, unknown is not a liquid but will still try to act as one so don't try to pump it
         *
         * Third you don't need to drain your liquid just too my pipes, drain it too anything bellow that will accept it that way you can
         * work with railcraft, buildcraft, and other mod's pipes, tanks, and machiens
         *
         */
        private int tickCounter;
        private int scantickCounter;
 
        protected List<EntityLiving> CowList = new ArrayList<EntityLiving>();
 
        // Watts being used per action / idle action
        public static final double WATTS_PER_TICK = 250;
        public static final double WATTS_PER_IDLE_TICK = 25;
 
        // Time idle after a tick
        public static final int IDLE_TIME_AFTER_ACTION = 60;
        public static final int IDLE_TIME_NO_ACTION = 30;
 
        // Watts being used per pump action
        public static final double WATTS_PER_PUMP_ACTION = 50;
 
        // How much power is stored?
        private double electricityStored = 0;
        private double electricityMaxStored = 5000;
 
        private boolean isMilking = false;
 
        // Is the machine currently powered, and did it change?
        public boolean prevIsPowered, isPowered = false;
        public boolean ReceivedRedstone = false;
 
        // Amount of milliBuckets of internal storage
        private ColorCode color = ColorCode.WHITE;
        private static final int milkMaxStored = 3;// use bucket number then times by the constant for bucket volume in case it changes
        private int milkStored = 0;
 
        private int facing;
        private int playersUsing = 0;
        private int idleTicks;
 
        public int currentX = 0;
        public int currentZ = 0;
        public int currentY = 0;
 
        public int minX, maxX;
        public int minZ, maxZ;
 
        public MilkingMachineTileEntity()
        {
                super();
        }
 
        public void scanForCows()
        {
                int xminrange = xCoord - getScanRange();
                int xmaxrange = xCoord + getScanRange() + 1;
                int yminrange = yCoord - getScanRange();
                int ymaxrange = yCoord + getScanRange() + 1;
                int zminrange = zCoord - getScanRange();
                int zmaxrange = zCoord + getScanRange() + 1;
 
                List<EntityCow> scannedCowslist = worldObj.getEntitiesWithinAABB(EntityCow.class, AxisAlignedBB.getBoundingBox(xminrange, yminrange, zminrange, xmaxrange, ymaxrange, zmaxrange));
 
                for (EntityCow Living : scannedCowslist)
                {
                        if (!CowList.contains(Living))
                        {
                                CowList.add(Living);
                        }
                }
 
        }
 
        public void milkCows()
        {
                if (CowList.size() != 0)
                {
                        CowList.remove(0);
                        this.milkStored += 30;
                }
        }
 
        public int getScanRange()
        {
                return 3;
        }
 
        @Override
        public void updateEntity()
        {
 
                if (!worldObj.isRemote)
                {
                        if (this.ReceivedRedstone)
                        {
                                this.setPowered(true);
                                if (scantickCounter >= 40)
                                {
                                        scanForCows();
                                        scantickCounter = 0;
                                }
                                if (CowList.size() != 0 && tickCounter >= 100)
                                {
                                        milkCows();
                                        isMilking = true;
                                        tickCounter = 0;
                                        this.setPowered(false);
                                }
                                System.out.println("Machine:" + milkStored);
                                tickCounter++;
                                scantickCounter++;
                        }
                        if (tickCounter >= 150)
                        {
                                tickCounter = 0;
                        }
                        if (scantickCounter >= 100)
                        {
                                tickCounter = 0;
                        }
                       
                        this.chargeUp();
                        this.drainTo();
                       
                        if (this.ticks % 3 == 0 && this.playersUsing > 0)
                        {
                                PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
                        }
                }
                super.updateEntity();
        }
        /**
         * Charges up the tileEntities energy storage
         */
        public void chargeUp()
        {
                //moved your UE power charging to a seperate method to make reading easier
                int front = 0;
                switch (this.getFacing())
                {
                        case 2:
                                front = 3;
                                break;
                        case 3:
                                front = 2;
                                break;
                        case 4:
                                front = 5;
                                break;
                        case 5:
                                front = 4;
                                break;
                        default:
                                front = 3;
                                break;
                }
                ForgeDirection direction = ForgeDirection.getOrientation(front);
 
                TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), direction);
 
                ElectricityNetwork network = ElectricityNetwork.getNetworkFromTileEntity(inputTile, direction);
 
                if (inputTile != null && network != null)
                {
                        if (this.electricityStored < this.electricityMaxStored)
                        {
                                double electricityNeeded = this.electricityMaxStored - this.electricityStored;
 
                                network.startRequesting(this, electricityNeeded, electricityNeeded >= getVoltage() ? getVoltage() : electricityNeeded);
 
                                this.setElectricityStored(electricityStored + (network.consumeElectricity(this).getWatts()));
 
                                if (UniversalElectricity.isVoltageSensitive)
                                {
                                        ElectricityPack electricityPack = network.consumeElectricity(this);
                                        if (electricityPack.voltage > this.getVoltage())
                                        {
                                                this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, 2f, true);
                                        }
                                }
 
                        }
                        else if (electricityStored >= electricityMaxStored)
                        {
                                network.stopRequesting(this);
                        }
                }
 
                if (this.inventory[0] != null && this.electricityStored < this.electricityMaxStored)
                {
                        if (this.inventory[0].getItem() instanceof IItemElectric)
                        {
                                IItemElectric electricItem = (IItemElectric) this.inventory[0].getItem();
 
                                if (electricItem.canProduceElectricity())
                                {
                                        double joulesReceived = electricItem.onUse(electricItem.getMaxJoules(this.inventory[0]) * 0.005, this.inventory[0]);
                                        this.setElectricityStored(this.electricityStored + joulesReceived);
                                }
                        }
                }
       
        }
        /**
         * Use this to just drain to the pipe or liquid tank bellow your milker
         */
        public void drainTo()
        {
                TileEntity ent = worldObj.getBlockTileEntity(xCoord, yCoord-1, xCoord);
                if(ent instanceof ITankContainer)
                {
                        ITankContainer tank = (ITankContainer) ent;
                        //the fill method returns the ammount of liquid that was added to the target block
                        int filled = tank.fill(ForgeDirection.DOWN.getOpposite(), LiquidHandler.getStack(color.getLiquidData(), this.milkStored), true);
                        this.milkStored -= filled;
                }
        }
        public boolean GetRedstoneSignal()
        {
                return ReceivedRedstone;
        }
 
        @Override
        public void readFromNBT(NBTTagCompound tagCompound)
        {
                super.readFromNBT(tagCompound);
                //vars
                this.facing = tagCompound.getShort("facing");
                this.isPowered = tagCompound.getBoolean("isPowered");
                this.electricityStored = tagCompound.getDouble("electricityStored");
                this.milkStored = tagCompound.getInteger("milkStored");
 
                //inventory
                NBTTagList tagList = tagCompound.getTagList("Inventory");
                for (int i = 0; i < tagList.tagCount(); i++)
                {
                        NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
                        byte slot = tag.getByte("Slot");
 
                        if (slot >= 0 && slot < inventory.length)
                        {
                                inventory[slot] = ItemStack.loadItemStackFromNBT(tag);
                        }
                }
        }
 
        @Override
        public void writeToNBT(NBTTagCompound tagCompound)
        {
                super.writeToNBT(tagCompound);
                //vars
                tagCompound.setShort("facing", (short) this.facing);
                tagCompound.setBoolean("isPowered", this.isPowered);
                tagCompound.setDouble("electricityStored", this.electricityStored);
                tagCompound.setInteger("milkStored", this.milkStored);
 
                //inventory
                NBTTagList itemList = new NBTTagList();
                for (int i = 0; i < inventory.length; i++)
                {
                        ItemStack stack = inventory[i];
 
                        if (stack != null)
                        {
                                NBTTagCompound tag = new NBTTagCompound();
                                tag.setByte("Slot", (byte) i);
                                stack.writeToNBT(tag);
                                itemList.appendTag(tag);
                        }
                }
                tagCompound.setTag("Inventory", itemList);
        }
 
        @Override
        public String getInvName()
        {
                return "Milking Machine";
        }
 
        @Override
        public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
        {
                try
                {
                        if (this.worldObj.isRemote)
                        {
                                this.isPowered = dataStream.readBoolean();
                                this.facing = dataStream.readInt();
                                this.electricityStored = dataStream.readDouble();
                                this.ReceivedRedstone = dataStream.readBoolean();
                                this.milkStored = dataStream.readInt();
                        }
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }
        }
 
        @Override
        public Packet getDescriptionPacket()
        {
                return PacketManager.getPacket(Biotech.CHANNEL, this, this.isPowered, this.facing, this.electricityStored, this.ReceivedRedstone, this.milkStored);
        }
 
        public int getFacing()
        {
                return facing;
        }
 
        public void setFacing(int facing)
        {
                this.facing = facing;
        }
 
        public double getElectricityStored()
        {
                return electricityStored;
        }
 
        public void setElectricityStored(double joules)
        {
                electricityStored = Math.max(Math.min(joules, getMaxElectricity()), 0);
        }
 
        public double getMaxElectricity()
        {
                return electricityMaxStored;
        }
 
        public int getMilkStored()
        {
                return this.milkStored;
        }
 
        @Override
        public ColorCode getColor()
        {
                return ColorCode.WHITE;
        }
 
        @Override
        public void setColor(Object obj)
        {
                // leave this blank unless you plan on having it change or be changed
        }
 
        @Override
        public int presureOutput(LiquidData type, ForgeDirection dir)
        {
                // you can only output pressure for milk
                if (type.getColor() == color){ return type.getPressure();}
               
                return 0;
        }
 
        @Override
        public boolean canPressureToo(LiquidData type, ForgeDirection dir)
        {
                //Have to use the opposite dirrection that you want
                if (type.getColor() == color && dir == ForgeDirection.DOWN.getOpposite()){ return true;}
               
                return false;
        }

		@Override
		public String getMeterReading(EntityPlayer user, ForgeDirection side)
		{
			return "Milk:"+this.milkStored;
		}
}