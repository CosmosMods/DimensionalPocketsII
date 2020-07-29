package com.zeher.dimpockets.core.manager;

import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.block.BlockState;
import net.minecraft.world.World;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenerationHandler implements IWorldGenerator {

	@SuppressWarnings("rawtypes")
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, ChunkGenerator chunkGenerator, AbstractChunkProvider chunkProvider) {
		if (world.getDimension().getType().equals(DimensionType.THE_NETHER)) {
			this.generateOverworld(world, random, chunkX * 16, chunkZ * 16);
		}
		
		if (world.getDimension().getType().equals(DimensionType.OVERWORLD)) {
			this.generateNether(world, random, chunkX * 16, chunkZ * 16);
		}
		
		if (world.getDimension().getType().equals(DimensionType.THE_END)) {
			this.generateEnd(world, random, chunkX * 16, chunkZ * 16);
		}
	}

	private void generateNether(World world, Random random, int chunkX, int chunkZ) { }

	private void generateEnd(World world, Random random, int chunkX, int chunkZ) { }

	private void generateOverworld(World world, Random random, int blockXPos, int blockZPos) {
		//this.addOreSpawn(BlockHandler.BLOCK_DIMENSIONAL_ORE.getDefaultState(), world, random, blockXPos, blockZPos, 16, 16, 4, 3, 5, 50, BlockMatcher.forBlock(Blocks.STONE));
	}

	@SuppressWarnings("unused")
	private void addOreSpawn(BlockState block, World world, Random random, int blockXPos, int blockZPos, int maxX, int maxZ, int maxVeinSize, int chance, int minY, int maxY, Predicate<BlockState> blockToSpawnIn) {
		int diffMinMaxY = maxY - minY;
		for (int x = 0; x < chance; x++) {
			int posX = blockXPos + random.nextInt(maxX);
			int posY = minY + random.nextInt(diffMinMaxY);
			int posZ = blockZPos + random.nextInt(maxZ);
			//new WorldGenRegion(block, maxVeinSize, blockToSpawnIn)..generate(world, random, new BlockPos(posX, posY, posZ));
		}
	}
	
}