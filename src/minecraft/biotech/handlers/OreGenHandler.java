package biotech.handlers;

import java.util.Random;

import biotech.Biotech;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraft.world.gen.feature.WorldGenMinable;
import cpw.mods.fml.common.IWorldGenerator;

public class OreGenHandler implements IWorldGenerator
{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) 
	{		
		if(!(chunkGenerator instanceof ChunkProviderHell) && !(chunkGenerator instanceof ChunkProviderEnd))
		{
			for (int i = 0; i < Biotech.BiotaniumPerChunk; i++)
			{
				int x = (chunkX*16) + random.nextInt(16);
				int z = (chunkZ*16) + random.nextInt(16);
				int y = random.nextInt(60);
				new WorldGenMinable(new ItemStack(Biotech.Biotanium, 1, 0).itemID, (Biotech.BiotaniumPerChunk * Biotech.BiotaniumPerBranch)).generate(world, random, x, y, z);
			}
		}
	}	
}
