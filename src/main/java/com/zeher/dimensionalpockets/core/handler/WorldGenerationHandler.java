package com.zeher.dimensionalpockets.core.handler;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;
import com.zeher.dimensionalpockets.DimensionalPockets;
import com.google.common.base.Predicate;

public class WorldGenerationHandler implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, net.minecraft.world.gen.IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		switch (world.provider.getDimension()) {
			case -1:
				this.generateNether(world, random, chunkX * 16, chunkZ * 16);
			case 0:
				this.generateOverworld(world, random, chunkX * 16, chunkZ * 16);
			case 1:
				this.generateEnd(world, random, chunkX * 16, chunkZ * 16);
		}
	}

	private void generateNether(World world, Random random, int chunkX, int chunkZ) { }

	private void generateEnd(World world, Random random, int chunkX, int chunkZ) { }

	private void generateOverworld(World world, Random random, int blockXPos, int blockZPos) {
		this.addOreSpawn(BlockHandler.BLOCK_DIMENSIONAL_ORE.getDefaultState(), world, random, blockXPos, blockZPos, 16, 16, 4, 3, 5, 50, BlockMatcher.forBlock(Blocks.STONE));
	}

	private void addOreSpawn(IBlockState block, World world, Random random, int blockXPos, int blockZPos, int maxX, int maxZ, int maxVeinSize, int chance, int minY, int maxY, Predicate<IBlockState> blockToSpawnIn) {
		int diffMinMaxY = maxY - minY;
		for (int x = 0; x < chance; x++) {
			int posX = blockXPos + random.nextInt(maxX);
			int posY = minY + random.nextInt(diffMinMaxY);
			int posZ = blockZPos + random.nextInt(maxZ);
			new WorldGenMinable(block, maxVeinSize, blockToSpawnIn).generate(world, random, new BlockPos(posX, posY, posZ));
		}
	}
}