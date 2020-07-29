package com.zeher.dimpockets.pocket.dimension.chunk;

import com.zeher.dimpockets.core.manager.ModBiomeManager;
import com.zeher.dimpockets.pocket.dimension.PocketDimensionGenSettings;
import com.zeher.dimpockets.pocket.dimension.biome.PocketBiomeProvider;
import com.zeher.dimpockets.pocket.dimension.biome.PocketBiomeProviderSettings;

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap.Type;


public class PocketChunkGenerator extends ChunkGenerator<GenerationSettings> {

	private IWorld world;

	public PocketChunkGenerator(IWorld worldIn) {
		super(worldIn, new PocketBiomeProvider(new PocketBiomeProviderSettings()), new PocketDimensionGenSettings());
		this.setWorld(worldIn);
	}
	
	@Override
	public void generateSurface(IChunk chunkIn) {
		Biome[] array = chunkIn.getBiomes();
		
		for (int i = 0; i < array.length; i++) {
			array[i] = (Biome) ModBiomeManager.POCKET_BIOME;
		}
		
		chunkIn.setBiomes(array);
	}

	@Override
	public int getGroundHeight() {
		return 0;
	}

	@Override
	public void makeBase(IWorld worldIn, IChunk chunkIn) { }

	@Override
	public int func_222529_a(int p_222529_1_, int p_222529_2_, Type p_222529_3_) {
		return 0;
	}

	public IWorld getWorld() {
		return world;
	}

	public void setWorld(IWorld world) {
		this.world = world;
	}

}