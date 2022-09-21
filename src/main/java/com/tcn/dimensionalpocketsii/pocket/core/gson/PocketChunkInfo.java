package com.tcn.dimensionalpocketsii.pocket.core.gson;

import java.util.ArrayList;

import com.tcn.cosmoslibrary.common.lib.CosmosChunkPos;

import net.minecraft.nbt.CompoundTag;

public class PocketChunkInfo {
	
	public static final PocketChunkInfo EMPTY = new PocketChunkInfo(new CosmosChunkPos(0, 0), true);
	private ArrayList<CosmosChunkPos> chunks = new ArrayList<CosmosChunkPos>();
	
	private int chunkSize;
	private boolean isSingleChunk;
	
	public PocketChunkInfo(CosmosChunkPos dominantChunkIn, boolean isSingleChunkIn) {
		this.chunks.add(dominantChunkIn);
		
		if (isSingleChunkIn) {
			this.chunkSize = 1;
			this.isSingleChunk = true;
		} else {
			this.chunks.add(new CosmosChunkPos(dominantChunkIn.add(1, 0)));
			this.chunks.add(new CosmosChunkPos(dominantChunkIn.add(0, 1)));
			this.chunks.add(new CosmosChunkPos(dominantChunkIn.add(1, 1)));
			
			this.chunkSize = 2;
			this.isSingleChunk = false;
		}
	}
	
	public boolean isChunkContained(CosmosChunkPos chunkIn) {
		return this.chunks.contains(chunkIn);
	}
	
	public int getChunkSize() {
		return this.chunkSize;
	}
	
	public CosmosChunkPos getDominantChunk() {
		return this.chunks.get(0);
	}
	
	public boolean isSingleChunk() {
		return this.isSingleChunk;
	}
	
	public void save(CompoundTag tag) {
		tag.putInt("chunk_size", this.chunkSize);
		tag.putBoolean("isSingleChunk", this.isSingleChunk);
		
		this.chunks.get(0).saveRaw(tag);
	}
	
	public static PocketChunkInfo load(CompoundTag tag) {
		boolean singleChunk = tag.getBoolean("isSingleChunk");
		PocketChunkInfo info = new PocketChunkInfo(CosmosChunkPos.loadRaw(tag), singleChunk);
		
		return info;
	}

	@Override
	public String toString() {
		return "{ " + this.chunks.get(0).toString() + " | " + this.isSingleChunk + " }";
	}
}
