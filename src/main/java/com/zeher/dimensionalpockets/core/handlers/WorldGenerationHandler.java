package com.zeher.dimensionalpockets.core.handlers;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;
import com.zeher.dimensionalpockets.DimensionalPockets;
import com.google.common.base.Predicate;

public class WorldGenerationHandler implements IWorldGenerator {
	
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		switch (world.provider.getDimension()) {
		case -1:
			generateNether(world, random, chunkX * 16, chunkZ * 16);
		case 0:
			generateOverworld(world, random, chunkX * 16, chunkZ * 16);
		case 1:
			generateEnd(world, random, chunkX * 16, chunkZ * 16);
		}
	}

	private void generateNether(World world, Random random, int chunkX, int chunkZ) {
	}

	private void generateEnd(World world, Random random, int chunkX, int chunkZ) {
	}

	private void generateOverworld(World world, Random random, int blockXPos, int blockZPos) {
		/**for (int i = 0; i < 30; i++) {
			int xCoord = chunkX + random.nextInt(16);
			int yCoord = random.nextInt(60);
			int zCoord = chunkZ + random.nextInt(16);

			new WorldGenMinable(OreCraftCore.marble, 30).generate(world, random, xCoord, yCoord, zCoord);

			new WorldGenMinable(OreCraftCore.basalt, 30).generate(world, random, xCoord, yCoord, zCoord);
		}
		for (int i = 0; i < 15; i++) {
			int xCoord = chunkX + random.nextInt(16);
			int yCoord = random.nextInt(64);
			int zCoord = chunkZ + random.nextInt(16);
			new WorldGenMinable(OreCraftCore.oreSatsum, 12).generate(world, random, xCoord, yCoord, zCoord);
			new WorldGenMinable(OreCraftCore.oreCopper, 10).generate(world, random, xCoord, yCoord, zCoord);
			new WorldGenMinable(OreCraftCore.oreTin, 10).generate(world, random, xCoord, yCoord, zCoord);
			new WorldGenMinable(OreCraftCore.oreAzurite, 8).generate(world, random, xCoord, yCoord, zCoord);

			yCoord = random.nextInt(56);
			new WorldGenMinable(OreCraftCore.oreYellowTopaz, 6).generate(world, random, xCoord, yCoord, zCoord);
			new WorldGenMinable(OreCraftCore.oreSilver, 7).generate(world, random, xCoord, yCoord, zCoord);
			new WorldGenMinable(OreCraftCore.oreOnyx, 7).generate(world, random, xCoord, yCoord, zCoord);

		}
		for (int i = 0; i < 10; i++) {
			int xCoord = chunkX + random.nextInt(16);
			int yCoord = random.nextInt(30);
			int zCoord = chunkZ + random.nextInt(16);

			new WorldGenMinable(OreCraftCore.oreEutine, 7).generate(world, random, xCoord, yCoord, zCoord);
			new WorldGenMinable(OreCraftCore.oreSilicon, 6).generate(world, random, xCoord, yCoord, zCoord);
		}
		for (int i = 0; i < 7; i++) {
			int xCoord = chunkX + random.nextInt(16);
			int yCoord = random.nextInt(25);
			int zCoord = chunkZ + random.nextInt(16);

			new WorldGenMinable(OreCraftCore.oreMithril, 5).generate(world, random, xCoord, yCoord, zCoord);
			new WorldGenMinable(OreCraftCore.oreFireOpal, 5).generate(world, random, xCoord, yCoord, zCoord);

		}
		for (int i = 0; i < 6; i++) {
			int xCoord = chunkX + random.nextInt(16);
			int yCoord = random.nextInt(20);
			int zCoord = chunkZ + random.nextInt(16);

			new WorldGenMinable(OreCraftCore.oreAdamantite, 4).generate(world, random, xCoord, yCoord, zCoord);
			new WorldGenMinable(OreCraftCore.oreRuby, 5).generate(world, random, xCoord, yCoord, zCoord);
			new WorldGenMinable(OreCraftCore.oreAmythest, 5).generate(world, random, xCoord, yCoord, zCoord);
			new WorldGenMinable(OreCraftCore.oreTurquiose, 5).generate(world, random, xCoord, yCoord, zCoord);

		}
		for (int i = 0; i < 5; i++) {
			int xCoord = chunkX + random.nextInt(16);
			int yCoord = random.nextInt(15);
			int zCoord = chunkZ + random.nextInt(16);

			new WorldGenMinable(OreCraftCore.oreRune, 3).generate(world, random, xCoord, yCoord, zCoord);
			new WorldGenMinable(OreCraftCore.oreBlackDiamond, 4).generate(world, random, xCoord, yCoord, zCoord);

		}
		for (int i = 0; i < 4; i++) {
			int xCoord = chunkX + random.nextInt(16);
			int yCoord = random.nextInt(10);
			int zCoord = chunkZ + random.nextInt(16);

			new WorldGenMinable(OreCraftCore.oreFractum, 2).generate(world, random, xCoord, yCoord, zCoord);
		}
		*/
		//addOreSpawn(BlockHandlerOreCraft.block_ore_copper.getDefaultState(), world, random, blockXPos, blockZPos, 16, 16, 10, 3, 1, 1, BlockMatcher.forBlock(Blocks.STONE));
	}

	private void addOreSpawn(IBlockState block, World world, Random random, int blockXPos, int blockZPos, int maxX,
			int maxZ, int maxVeinSize, int chance, int minY, int maxY, Predicate<IBlockState> blockToSpawnIn) {
		int diffMinMaxY = maxY - minY;
		for (int x = 0; x < chance; x++) {
			int posX = blockXPos + random.nextInt(maxX);
			int posY = minY + random.nextInt(diffMinMaxY);
			int posZ = blockZPos + random.nextInt(maxZ);
			new WorldGenMinable(block, maxVeinSize, blockToSpawnIn).generate(world, random,
					new BlockPos(posX, posY, posZ));
		}
	}

	public static void add(IWorldGenerator generator) {
	}

}
