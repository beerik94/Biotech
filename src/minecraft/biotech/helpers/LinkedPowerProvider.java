package biotech.helpers;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import biotech.Biotech;
import biotech.tileentity.tileEntityBasic;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerProvider;

public class LinkedPowerProvider extends PowerProvider
{
	public TileEntity tileEntity;
	
	public LinkedPowerProvider(TileEntity tile)
	{
		tileEntity = tile;
	}

	@Override
	public boolean update(IPowerReceptor receptor)
	{
		tileEntityBasic basicBlock = (tileEntityBasic)tileEntity;
		maxEnergyStored = (int)(basicBlock.getMaxJoules()*Biotech.TO_BC);
		energyStored = (int)(basicBlock.electricityStored);
		return true;
	}
	
	@Override
	public void receiveEnergy(float quantity, ForgeDirection from) 
	{
		tileEntityBasic electricBlock = (tileEntityBasic)tileEntity;
		electricBlock.setJoules(electricBlock.electricityStored+(quantity*Biotech.FROM_BC));
	}
}
