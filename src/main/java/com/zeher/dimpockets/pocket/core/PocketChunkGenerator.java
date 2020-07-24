package com.zeher.dimpockets.pocket.core;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.zeher.dimpockets.core.manager.ModBiomeManager;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;


public class PocketChunkGenerator implements IChunkGenerator {

	private World world;

	public PocketChunkGenerator(World world) {
		this.world = world;
	}
	
	@Override
	public Chunk generateChunk(int x, int z) {
		Chunk chunk = new Chunk(world, x, z);

		byte[] array = chunk.getBiomeArray();
		
		for (int i = 0; i < array.length; i++) {
			array[i] = (byte) ModBiomeManager.pocketBiome.getIdForBiome(ModBiomeManager.pocketBiome);
		}
		
		chunk.setBiomeArray(array);
		chunk.generateSkylightMap();
		return chunk;
	}

	@Override
	public void populate(int x, int z) { }

	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z) {
		return false;
	}

	@Override
	public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
		return Lists.newArrayList();
	}

	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z) { }

	@Override
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
		return null;
	}

	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
		return false;
	}
	
}
