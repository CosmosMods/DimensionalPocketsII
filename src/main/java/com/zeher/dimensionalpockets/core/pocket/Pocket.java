package com.zeher.dimensionalpockets.core.pocket;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.block.BlockDimensionalPocket;
import com.zeher.dimensionalpockets.core.block.BlockDimensionalPocketWall;
import com.zeher.dimensionalpockets.core.dimshift.DimensionalShifter;
import com.zeher.dimensionalpockets.core.dimshift.DimensionalShiftUtils;
import com.zeher.dimensionalpockets.core.handlers.BlockHandler;
import com.zeher.dimensionalpockets.core.util.DimLogger;
import com.zeher.dimensionalpockets.core.util.DimUtils;
import com.zeher.dimensionalpockets.core.util.TeleportDirection;
import com.zeher.trzcore.api.TRZTextUtil;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Pocket {

	// NBT CONSTANTS START //
	private static final String NBT_DIMENSIONAL_POCKET_KEY = "pocketData";
	private static final String NBT_FLOW_STATE_MAP_KEY = "stateMap";

	private static final String NBT_GENERATED_KEY = "generated";
	private static final String NBT_BLOCK_DIMENSION_KEY = "blockDim";
	private static final String NBT_CHUNK_COORDS_KEY = "chunkPos";
	private static final String NBT_BLOCK_COORDS_KEY = "blockPos";
	private static final String NBT_SPAWN_COORDS_KEY = "spawnPos";
	private static final String NBT_SPAWN_COORDS_YAW_KEY = "spawnPosYaw";
	private static final String NBT_SPAWN_COORDS_PITCH_KEY = "spawnPosPitch";
	private static final String NBT_CREATOR_KEY = "creator";
	// NBT CONSTANTS END //

	private transient NBTTagCompound nbtTagCompound;

	@SerializedName("sideStates")
	private Map<EnumFacing, PocketSideState> stateMap;

	@SerializedName("generated")
	private boolean isGenerated = false;

	@SerializedName("blockDim")
	private int blockDim;

	@SerializedName("chunkPos")
	private BlockPos chunkPos;

	@SerializedName("blockPos")
	private BlockPos blockPos;

	@SerializedName("spawnPos")
	private BlockPos spawnPos;
	
	@SerializedName("spawnPosYaw")
	private float spawnYaw;
	
	@SerializedName("spawnPosPitch")
	private float spawnPitch;

	@SerializedName("creator")
	private String creator;

	@Deprecated
	private BlockPos spawnSet; // renamed to spawnPos. Needs to stay for compatibility with old saves.

	private Map<EnumFacing, PocketSideState> getSideStateMap() {
		if (stateMap == null) {
			stateMap = new EnumMap<>(EnumFacing.class);
		}
		return stateMap;
	}

	private Pocket() {}

	public Pocket(BlockPos chunkPos, int blockDim, BlockPos blockPos) {
		setBlockDim(blockDim);
		setBlockPos(blockPos);
		setSpawnInPocket(new BlockPos(7, 1, 7), 0f, 0f);
		spawnPos = new BlockPos(7,1,7);
		this.chunkPos = chunkPos;
	}

	public void generatePocketRoom(String creatorName) {
		if (isGenerated) {
			return;
		}
		
		World world = PocketRegistry.getWorldForPockets();
		
		int worldX = chunkPos.getX() * 16;
		int worldY = chunkPos.getY() * 16;
		int worldZ = chunkPos.getZ() * 16;

		Chunk chunk = world.getChunkFromChunkCoords(chunkPos.getX(), chunkPos.getZ());
		
		System.out.println("Pocket.class" + " - Generated pocket.");
		System.out.println("Pocket.class" + " - World Pos: " + worldX + " __ " + worldY + " __ " + worldZ + " Chunk data:" + chunk);
		System.out.println("Pocket.class" + " - Chunk Pos: " + this.chunkPos);
		
		int l = worldY >> 4;
		ExtendedBlockStorage extendedBlockStorage = chunk.getBlockStorageArray()[l];  

		if (extendedBlockStorage == null) {
			extendedBlockStorage = new ExtendedBlockStorage(worldY, !world.provider.hasNoSky());
			chunk.getBlockStorageArray()[l] = extendedBlockStorage;
		}

		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				for (int z = 0; z < 16; z++) {
					boolean flagX = x == 0 || x == 15;
					boolean flagY = y == 0 || y == 15;
					boolean flagZ = z == 0 || z == 15;

					// Added those flags, so I could add these checks, almost halves the time.
					if (!(flagX || flagY || flagZ) || flagX && (flagY || flagZ) || flagY && flagZ) {
						continue;
					}

					extendedBlockStorage.set(x, y, z, BlockHandler.block_dimensional_pocket_wall.getDefaultState());
					world.markBlockRangeForRenderUpdate(worldX + x, worldY + y, worldZ + z, worldX + x, worldY + y, worldZ + z);
				}
			}
		}

		isGenerated = world.getBlockState(new BlockPos(worldX + 1, worldY, worldZ + 1)).getBlock() instanceof BlockDimensionalPocketWall;
		getNBT().setBoolean(NBT_GENERATED_KEY, isGenerated);

		if (!Strings.isNullOrEmpty(creatorName)) {
			creator = creatorName;
			getNBT().setString(NBT_GENERATED_KEY, creatorName);
		}
	}
	
	public void teleportTo(EntityPlayer entityPlayer) {
		if (entityPlayer.world.isRemote || !(entityPlayer instanceof EntityPlayerMP)) {
			return;
		}

		World world = getBlockWorld();
		TeleportDirection teleportSide = TeleportDirection.getValidTeleportLocation(world, blockPos);
		if (teleportSide == TeleportDirection.UNKNOWN) {
			TextComponentString comp = new TextComponentString(TRZTextUtil.ITALIC + "Teleport Disabled. Pocket is blocked on all sides.");
			entityPlayer.sendMessage(comp);
			
			return;
		}

		EntityPlayerMP player = (EntityPlayerMP) entityPlayer;

		int dimID = player.dimension;
		
		BlockPos tempSet = getChunkPos();
		BlockPos spawnSet = tempSet.add(this.spawnPos);
		
		DimensionalShifter teleporter = DimensionalShiftUtils.createTeleporter(dimID, tempSet, spawnYaw, spawnPitch);

		generatePocketRoom(entityPlayer.getName());

		if (dimID != DimensionalPockets.dimension_id) {
			DimensionalShiftUtils.shiftPlayerToDimension(player, DimensionalPockets.dimension_id, teleporter, spawnSet);
		} else {
			teleporter.placeInPortal(player, 0);
		}
		
		TextComponentString comp = new TextComponentString(TRZTextUtil.TEAL + "Entering pocket dimension...");
		entityPlayer.sendMessage(comp);
	}

	public void teleportFrom(EntityPlayer entityPlayer) {
		if (entityPlayer.world.isRemote || !(entityPlayer instanceof EntityPlayerMP)) {
			return;
		}

		EntityPlayerMP player = (EntityPlayerMP) entityPlayer;
		World world = getBlockWorld();

		if (isSourceBlockPlaced()) {
			TeleportDirection teleportSide = TeleportDirection.getValidTeleportLocation(world, blockPos);
			if (teleportSide != TeleportDirection.UNKNOWN) {
				BlockPos tempBlockSet = blockPos.add(teleportSide.toCoordSet());
				BlockPos spawnPos = new BlockPos(tempBlockSet.getX(), tempBlockSet.getY() + 1, tempBlockSet.getZ());
				DimensionalShifter teleporter = DimensionalShiftUtils.createTeleporter(blockDim, blockPos, player.rotationYaw, player.rotationPitch);

				if (blockDim != DimensionalPockets.dimension_id) {
					DimensionalShiftUtils.shiftPlayerToDimension(player, blockDim, teleporter, spawnPos);
				} else {
					teleporter.placeInPortal(player, 0);
				}
				
				TextComponentString comp = new TextComponentString(TRZTextUtil.TEAL + "Leaving pocket dimension...");
				entityPlayer.sendMessage(comp);
				
			} else {
				TextComponentString comp = new TextComponentString(TRZTextUtil.ITALIC + "You're trapped! Someone broke your Pocket.");
				entityPlayer.sendMessage(comp);
			}
		} else {
			TextComponentString comp = new TextComponentString(TRZTextUtil.ITALIC + "You're Trapped! Something is blocking your Pocket.");
			entityPlayer.sendMessage(comp);
		}
	}

	public boolean isSourceBlockPlaced() {
		return getBlock() instanceof BlockDimensionalPocket;
	}

	public World getBlockWorld() {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getServer().worldServerForDimension(blockDim);
	}

	public Block getBlock() {
		World world = getBlockWorld();
		if (world == null) {
			DimLogger.warning("Dimension with ID " + blockDim + " does not exist... (Mystcraft or GalactiCraft world?) Returning null for Pocket");
			return null;
		}
		return world.getBlockState(blockPos).getBlock();
	}

	public int getBlockDim() {
		return blockDim;
	}

	public void setBlockDim(int blockDim) {
		this.blockDim = blockDim;
		getNBT().setInteger(NBT_BLOCK_DIMENSION_KEY, blockDim);
	}

	public BlockPos getBlockPos() {
		return blockPos;
	}

	public void setBlockPos(BlockPos blockPos) {
		this.blockPos = blockPos;
		//this.blockPos.writeToNBT(getNBT(), NBT_BLOCK_COORDS_KEY);
	}

	private BlockPos getSpawnPos() {
		if (spawnPos == null) {
			spawnPos = spawnSet;
			spawnSet = null;
		}
		return spawnPos;
	}

	public void setSpawnInPocket(BlockPos spawnPos, float spawnYaw, float spawnPitch) {
		this.spawnPos = spawnPos;
		//this.spawnPos.writeToNBT(getNBT(), NBT_SPAWN_COORDS_KEY);

		this.spawnYaw = spawnYaw;
		getNBT().setFloat(NBT_SPAWN_COORDS_YAW_KEY, this.spawnYaw);
		this.spawnPitch = spawnPitch;
		getNBT().setFloat(NBT_SPAWN_COORDS_PITCH_KEY, this.spawnPitch);
	}

	public BlockPos getChunkPos() {
		return chunkPos;
	}

	public String getCreator() {
		return creator;
	}

	private NBTTagCompound getNBT() {
		if (nbtTagCompound == null) {
			// generate first compound
			nbtTagCompound = new NBTTagCompound();

			NBTTagCompound stateMap = new NBTTagCompound();
			for (Entry<EnumFacing, PocketSideState> entry : getSideStateMap().entrySet()) {
				EnumFacing side = entry.getKey();
				PocketSideState state = entry.getValue();
				stateMap.setString(side.name(), state.name());
			}
			nbtTagCompound.setTag(NBT_FLOW_STATE_MAP_KEY, stateMap);

			nbtTagCompound.setBoolean(NBT_GENERATED_KEY, isGenerated);
			nbtTagCompound.setInteger(NBT_BLOCK_DIMENSION_KEY, blockDim);

			if (chunkPos != null) {
				//chunkPos.writeToNBT(nbtTagCompound, NBT_CHUNK_COORDS_KEY);
			}

			if (blockPos != null) {
				//blockPos.writeToNBT(nbtTagCompound, NBT_BLOCK_COORDS_KEY);
			}

			if (getSpawnPos() != null) {
				//spawnPos.writeToNBT(nbtTagCompound, NBT_SPAWN_COORDS_KEY);
			}

			getNBT().setFloat(NBT_SPAWN_COORDS_YAW_KEY, spawnYaw);
			getNBT().setFloat(NBT_SPAWN_COORDS_PITCH_KEY, spawnPitch);

			if (creator != null && !creator.isEmpty()) {
				nbtTagCompound.setString(NBT_CREATOR_KEY, creator);
			}
		}

		return nbtTagCompound;
	}

	public void writeToNBT(NBTTagCompound tag) {
		tag.setTag(NBT_DIMENSIONAL_POCKET_KEY, getNBT());
	}

	public static Pocket readFromNBT(NBTTagCompound tag) {
		NBTTagCompound pocketTag = tag.getCompoundTag(NBT_DIMENSIONAL_POCKET_KEY);

		Pocket pocket = new Pocket();

		//pocket.chunkPos = CoordSet.readFromNBT(pocketTag, NBT_CHUNK_COORDS_KEY);
		pocket.blockDim = pocketTag.getInteger(NBT_BLOCK_DIMENSION_KEY);
		//pocket.blockPos = CoordSet.readFromNBT(pocketTag, NBT_BLOCK_COORDS_KEY);

		pocket.isGenerated = pocketTag.getBoolean(NBT_GENERATED_KEY);
		//pocket.spawnPos = CoordSet.readFromNBT(pocketTag, NBT_SPAWN_COORDS_KEY);
		pocket.spawnYaw = pocketTag.getFloat(NBT_SPAWN_COORDS_YAW_KEY);
		pocket.spawnPitch = pocketTag.getFloat(NBT_SPAWN_COORDS_PITCH_KEY);

		pocket.creator = pocketTag.getString(NBT_CREATOR_KEY);
		if (pocket.creator.isEmpty()) {
			pocket.creator = null;
		}
		
		NBTTagCompound stateMap = pocketTag.getCompoundTag(NBT_FLOW_STATE_MAP_KEY);
		for (EnumFacing side : EnumFacing.VALUES) {
			if (stateMap.hasKey(side.name())) {
				PocketSideState state = PocketSideState.valueOf(stateMap.getString(side.name()));
				pocket.getSideStateMap().put(side, state);
			}
		}
		
		return pocket;
	}
}
