package com.zeher.dimpockets.pocket.dimension;

import com.zeher.dimpockets.pocket.dimension.chunk.PocketChunkGenerator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class PocketDimension extends Dimension {

	public PocketDimension(World worldIn, DimensionType typeIn) {
		super(worldIn, typeIn);
		
		if (ServerLifecycleHooks.getCurrentServer().isSinglePlayer()) {
			setSkyRenderer(new IRenderHandler() {
				@Override
				public void render(int ticks, float partialTicks, ClientWorld world, Minecraft mc) { }
			});
		}
	}

	@Override
	public ChunkGenerator<GenerationSettings> createChunkGenerator() {		
		return new PocketChunkGenerator(world);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public Vec3d getFogColor(float par1, float par2) {
		return new Vec3d(0, 0, 0);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public float getStarBrightness(float par1) {
		return 1F;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public float getCloudHeight() {
		return 600000F;
	}

	@OnlyIn(Dist.CLIENT)
	public double getVoidFogYFactor() {
		return 0.0;
	}

	@Override
	public boolean canRespawnHere() {
		return true;
	}

	@Override
	public boolean canDoLightning(Chunk chunk) {
		return false;
	}

	@Override
	public boolean canDoRainSnowIce(Chunk chunk) {
		return false;
	}

	@Override
	public boolean shouldMapSpin(String entity, double x, double y, double z) {
		return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public float[] calcSunriseSunsetColors(float par1, float par2) {
		return new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
	}

	@Override
	public float calculateCelestialAngle(long par1, float par3) {
		return 0.75F;
	}

	@Override
	public BlockPos findSpawn(ChunkPos chunkPosIn, boolean checkValid) {
		return null;
	}

	@Override
	public BlockPos findSpawn(int posX, int posZ, boolean checkValid) {
		return null;
	}

	@Override
	public boolean isSurfaceWorld() {
		return false;
	}

	@Override
	public boolean doesXZShowFog(int x, int z) {
		return false;
	}
	
	@OnlyIn(Dist.CLIENT)
	public boolean isSkyColored() {
		return false;
	}

	public boolean hasSkyLight() {
		return true;
	}
	
	public boolean isNether() {
		return false;
	}
}