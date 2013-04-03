package hydraulic.core.implement;

import net.minecraft.tileentity.TileEntity;

/**
 * Applied to TileEntities.
 * 
 * @author Calclavia
 * 
 */
public interface ILiquidConnectionProvider extends ILiquidConnector
{

	/**
	 * Gets a list of all the connected TileEntities that this conductor is connected to. The
	 * array's length should be always the 6 adjacent wires.
	 * 
	 * @return
	 */
	public TileEntity[] getAdjacentConnections();

	/**
	 * Instantly refreshes all connected blocks around the conductor, recalculating the connected
	 * blocks.
	 */
	public void updateAdjacentConnections();
}
