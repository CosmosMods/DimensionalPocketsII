package com.zeher.dimensionalpockets.pocket;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.dimshift.DimensionalShiftUtils;
import com.zeher.dimensionalpockets.core.dimshift.DimensionalShifter;
import com.zeher.dimensionalpockets.core.handler.BlockHandler;
import com.zeher.dimensionalpockets.core.handler.NetworkHandler;
import com.zeher.dimensionalpockets.core.util.DimLogger;
import com.zeher.dimensionalpockets.core.util.TeleportDirection;
import com.zeher.dimensionalpockets.pocket.block.BlockDimensionalPocket;
import com.zeher.dimensionalpockets.pocket.block.BlockDimensionalPocketWall;
import com.zeher.zeherlib.api.util.TextUtil;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Pocket {
	
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

	private transient NBTTagCompound nbtTagCompound;

	@SerializedName("sideStates")
	private Map<EnumFacing, PocketSideState> stateMap;

	@SerializedName("generated")
	private boolean isGenerated = false;

	@SerializedName("blockDim")
	private int blockDim;

	@SerializedName("chunkPos")
	private BlockPos chunkPos;
	
	//private Map<BlockPos, Integer> block_map = new HashMap<>();
	@SerializedName("side_map")
	private Map<Integer, BlockPos> side_map = new HashMap<>();
	
	@SerializedName("last_pos")
	private BlockPos last_pos;
	
	@SerializedName("spawnPos")
	private BlockPos spawnPos;
	
	@SerializedName("spawnPosYaw")
	private float spawnYaw;
	
	@SerializedName("spawnPosPitch")
	private float spawnPitch;

	@SerializedName("creator")
	private String creator;
	
	@SerializedName("allowed")
	private ArrayList<String> allowed = new ArrayList<String>();

	private Map<EnumFacing, PocketSideState> getSideStateMap() {
		if (stateMap == null) {
			stateMap = new EnumMap<>(EnumFacing.class);
		}
		return stateMap;
	}
	
	private Map<BlockPos, BlockPos> blockMap;

	private Pocket() {}

	public Pocket(BlockPos chunkPos, int blockDim, BlockPos blockPos) {
		this.setBlockDim(blockDim);
		this.addBlockPosToArray(blockPos);
		this.setSpawnInPocket(new BlockPos(7, 1, 7), 0f, 0f);
		this.spawnPos = new BlockPos(7,1,7);
		this.chunkPos = chunkPos;
	}

	public void generatePocket(String creatorName) {
		if (this.isGenerated) {
			return;
		}
		
		World world = PocketRegistry.getWorldForPockets();
		
		int worldX = chunkPos.getX() * 16;
		int worldY = chunkPos.getY() * 16;
		int worldZ = chunkPos.getZ() * 16;

		Chunk chunk = world.getChunkFromChunkCoords(chunkPos.getX(), chunkPos.getZ());
		
		int l = worldY >> 4;
		ExtendedBlockStorage storage = chunk.getBlockStorageArray()[l];
		
		if (storage == null) {
			storage = new ExtendedBlockStorage(worldY, !world.provider.hasSkyLight());
			chunk.getBlockStorageArray()[l] = storage;
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

					//Adds "edge-blocks" to the pocket.
					if (x == 1 || y == 1 || z == 1) {
						storage.set(x, y, z, BlockHandler.BLOCK_DIMENSIONAL_POCKET_WALL_EDGE.getDefaultState());
					} else if (x == 14 || y == 14 || z == 14) {
						storage.set(x, y, z, BlockHandler.BLOCK_DIMENSIONAL_POCKET_WALL_EDGE.getDefaultState());
					} else {
						storage.set(x, y, z, BlockHandler.BLOCK_DIMENSIONAL_POCKET_WALL.getDefaultState());
					}
					
					storage.setSkyLight(new NibbleArray());
					world.markBlockRangeForRenderUpdate(new BlockPos(x, y, z), new BlockPos(x, y, z));
					world.scheduleUpdate(new BlockPos(x, y, z), BlockHandler.BLOCK_DIMENSIONAL_POCKET_WALL, 0);
				}
			}
		}

		this.isGenerated = world.getBlockState(new BlockPos(worldX + 1, worldY, worldZ + 1)).getBlock() instanceof BlockDimensionalPocketWall;
		this.getNBT().setBoolean(NBT_GENERATED_KEY, isGenerated);

		if (!Strings.isNullOrEmpty(creatorName)) {
			//this.creator = creatorName;
			//this.getNBT().setString(NBT_GENERATED_KEY, creatorName);
			
			//this.addAllowedPlayer(creatorName);
			//NetworkHandler.sendPocketSetCreator(creatorName, last_pos);
			//NetworkHandler.sendCreatorPacketToServer(creatorName, last_pos);
			//NetworkHandler.sendCreatorPacketToClient(creatorName, last_pos);
		}
	}
	
	public void setCreator(String name) {
		if (this.isGenerated) {
			
		} else {
			this.creator = name;
			this.getNBT().setString(NBT_GENERATED_KEY, name);
			
			this.addAllowedPlayer(name);
		}
	}
	
	public void shiftTo(EntityPlayer entityPlayer) {
		if (entityPlayer.world.isRemote || !(entityPlayer instanceof EntityPlayerMP)) {
			return;
		}
		
		World world = this.getBlockWorld();
		TeleportDirection teleportSide = TeleportDirection.getValidTeleportLocation(world, this.getLastBlockPos());
		if (teleportSide == TeleportDirection.UNKNOWN) {
			TextComponentString comp = new TextComponentString(TextUtil.ITALIC + "Teleport Disabled. Pocket is blocked on all sides.");
			entityPlayer.sendMessage(comp);
			
			return;
		}

		EntityPlayerMP player = (EntityPlayerMP) entityPlayer;

		int dimID = player.dimension;
		BlockPos tempSet = new BlockPos(chunkPos.getX() * 16, this.chunkPos.getY() * 16, this.chunkPos.getZ() * 16).add(spawnPos);
		DimensionalShifter teleporter = DimensionalShiftUtils.createTeleporter(dimID, tempSet, spawnYaw, spawnPitch);

		this.generatePocket(entityPlayer.getName());

		TextComponentString comp = new TextComponentString(TextUtil.TEAL + "Entering pocket dimension...");
		if(!world.isRemote) {
			entityPlayer.sendMessage(comp);
		}
		
		if (dimID != DimensionalPockets.DIMENSION_ID) {
			DimensionalShiftUtils.shiftPlayerToDimension(player, DimensionalPockets.DIMENSION_ID, teleporter, tempSet);
		} else {
			DimensionalShiftUtils.shiftPlayerToDimension(player, 0, teleporter, getSpawnPos());
			DimensionalShiftUtils.shiftPlayerToDimension(player, DimensionalPockets.DIMENSION_ID, teleporter, tempSet);
			//teleporter.placeInPortal(player, 0);
			//DimensionalShiftUtils.shiftPlayerToDimension(player, DimensionalPockets.dimension_id, teleporter, player.getPosition());
		}
	}

	public void shiftFrom(EntityPlayer entityPlayer) {
		if (entityPlayer.world.isRemote || !(entityPlayer instanceof EntityPlayerMP)) {
			return;
		}

		EntityPlayerMP player = (EntityPlayerMP) entityPlayer;
		World world = getBlockWorld();

		if (this.isSourceBlockPlaced()) {
			TeleportDirection teleportSide = TeleportDirection.getValidTeleportLocation(world, this.getLastBlockPos());
			if (teleportSide != TeleportDirection.UNKNOWN) {
				BlockPos tempBlockSet = new BlockPos(this.getLastBlockPos()).add(teleportSide.toBlockPos());
				BlockPos spawnPos = new BlockPos(tempBlockSet);
				
				DimensionalShifter teleporter = DimensionalShiftUtils.createTeleporter(blockDim, spawnPos, player.rotationYaw, player.rotationPitch);

				if (blockDim != DimensionalPockets.DIMENSION_ID) {
					DimensionalShiftUtils.shiftPlayerToDimension(player, blockDim, teleporter, spawnPos);
				} else {
					teleporter.placeInPortal(player, 0);
				}
				
				TextComponentString comp = new TextComponentString(TextUtil.TEAL + "Leaving pocket dimension...");
				entityPlayer.sendMessage(comp);
				
			} else {
				if (!world.isRemote) {
					TextComponentString comp = new TextComponentString(TextUtil.ITALIC + "You're Trapped! Something is blocking your Pocket.");
					entityPlayer.sendMessage(comp);
				}
			}
		} else {
			if (!world.isRemote) {
				TextComponentString comp = new TextComponentString(TextUtil.ITALIC + "You're trapped! Someone broke your Pocket.");
				entityPlayer.sendMessage(comp);
			}
		}
	}

	public boolean isSourceBlockPlaced() {
		return this.getBlock() instanceof BlockDimensionalPocket;
	}

	public World getBlockWorld() {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getWorld(blockDim);
	}

	public Block getBlock() {
		World world = getBlockWorld();
		if (world == null) {
			DimLogger.warning("Dimension with ID " + blockDim + " does not exist... (Mystcraft or GalactiCraft world?) Returning null for Pocket");
			return null;
		}
		return world.getBlockState(this.getLastBlockPos()).getBlock();
	}

	public int getBlockDim() {
		return blockDim;
	}

	public void setBlockDim(int blockDim) {
		this.blockDim = blockDim;
		this.getNBT().setInteger(NBT_BLOCK_DIMENSION_KEY, blockDim);
	}
	
	public BlockPos getLastBlockPos() {
		//return this.side_map.get(this.side_map.size() - 1);
		return this.last_pos;
	}
	
	public void addBlockPosToArray(BlockPos pos) {
		if (side_map.containsValue(pos)) {
			DimLogger.info("Pocket: [" + this.chunkPos + "] Already contains that BlockPos. No update to the Map.");
		} else {
			side_map.put(side_map.size(), pos);
		}
		
		this.last_pos = pos;
	}
	
	public boolean doesArrayContain(BlockPos pos) {
		return side_map.containsValue(pos);
	}
	
	public Map<Integer, BlockPos> getMap() {
		return this.side_map;
	}
	
	private BlockPos getSpawnPos() {
		return spawnPos;
	}

	public void setSpawnInPocket(BlockPos spawnPos, float spawnYaw, float spawnPitch) {
		this.spawnPos = spawnPos;
		
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
			
			for (int i = 0; i < this.allowed.size(); i++) {
				String put = this.allowed.get(i);
				
				nbtTagCompound.setString("allowed_[" + i + "]", put);
			}
			
			nbtTagCompound.setInteger("allowed_size", this.allowed.size());
			
			nbtTagCompound.setTag(NBT_FLOW_STATE_MAP_KEY, stateMap);

			nbtTagCompound.setBoolean(NBT_GENERATED_KEY, isGenerated);
			nbtTagCompound.setInteger(NBT_BLOCK_DIMENSION_KEY, blockDim);

			if (chunkPos != null) {
				//chunkPos.writeToNBT(nbtTagCompound, NBT_CHUNK_COORDS_KEY);
				nbtTagCompound.setInteger("chunk_x", this.chunkPos.getX());
				nbtTagCompound.setInteger("chunk_y", this.chunkPos.getY());
				nbtTagCompound.setInteger("chunk_z", this.chunkPos.getZ());
			}

			if (this.getLastBlockPos() != null) {
				nbtTagCompound.setInteger("last_x", this.getLastBlockPos().getX());
				nbtTagCompound.setInteger("last_y", this.getLastBlockPos().getY());
				nbtTagCompound.setInteger("last_z", this.getLastBlockPos().getZ());
			}

			if (this.getSpawnPos() != null) {
				//spawnPos.writeToNBT(nbtTagCompound, NBT_SPAWN_COORDS_KEY);
			}
			
			if (this.side_map.size() > 0) {
				for (int i = 0; i < side_map.size(); i++) {
					BlockPos test = side_map.get(i);
					
					nbtTagCompound.setInteger(Integer.toString(i) + "_X", test.getX());
					nbtTagCompound.setInteger(Integer.toString(i) + "_Y", test.getY());
					nbtTagCompound.setInteger(Integer.toString(i) + "_Z", test.getZ());
				}
				
				nbtTagCompound.setInteger("length_", side_map.size());
			}
			
			this.getNBT().setFloat(NBT_SPAWN_COORDS_YAW_KEY, spawnYaw);
			this.getNBT().setFloat(NBT_SPAWN_COORDS_PITCH_KEY, spawnPitch);

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
		
		int size = pocketTag.getInteger("allowed_size");
		
		for (int i = 0; i < size; i++) {
			pocket.addAllowedPlayer(pocketTag.getString("allowed_[" + i + "]"));
		}
		
		int x_ = pocketTag.getInteger("chunk_x");
		int y_ = pocketTag.getInteger("chunk_y");
		int z_ = pocketTag.getInteger("chunk_z");
		pocket.chunkPos = new BlockPos(x_, y_, z_);
				
		int length = pocketTag.getInteger("length_");
		
		for (int j = 0; j < length; j++) {
			int new_x = pocketTag.getInteger(Integer.toString(j) + "_X");
			int new_y = pocketTag.getInteger(Integer.toString(j) + "_Y");
			int new_z = pocketTag.getInteger(Integer.toString(j) + "_Z");
			
			BlockPos test_pos = new BlockPos(new_x, new_y, new_z);
			
			pocket.side_map.put(j, test_pos);
			
			DimLogger.info("BLOCK_MAP LOADED FROM NBT");
		}
		
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
	
	public ArrayList<String> getAllowedPlayers() {
		return allowed;
	}
	
	public void addAllowedPlayer(String player_name) {
		if (checkPlayerInList(player_name)) {
			//DO NOTHING THEY ARE ALREADY THERE.
		} else {
			allowed.add(player_name);
		}
	}
	
	public void removeAllowedPlayer(String player_name) {
		for (int i = 0; i < allowed.size(); i++) {
			String test = allowed.get(i);
			
			if (test.equals(player_name)) {
				allowed.remove(i);
			}
		}
	}
	
	public boolean checkPlayerInList(String player_name) {
		for (int i = 0; i < allowed.size(); i++) {
			String test = allowed.get(i);
			
			if (test.equals(player_name)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isGenerated() {
		return this.isGenerated;
	}
}
