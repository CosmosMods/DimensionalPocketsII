package com.zeher.dimensionalpockets.pocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.dimshift.DimensionalShiftUtils;
import com.zeher.dimensionalpockets.core.dimshift.DimensionalShifter;
import com.zeher.dimensionalpockets.core.handler.BlockHandler;
import com.zeher.dimensionalpockets.core.util.DimLogger;
import com.zeher.dimensionalpockets.core.util.EnumPocketSideState;
import com.zeher.dimensionalpockets.core.util.TeleportDirection;
import com.zeher.dimensionalpockets.pocket.block.BlockDimensionalPocket;
import com.zeher.dimensionalpockets.pocket.block.BlockDimensionalPocketWall;
import com.zeher.dimensionalpockets.pocket.block.BlockDimensionalPocketWallEdge;
import com.zeher.zeherlib.api.client.util.TextHelper;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Pocket {
	
	private static final String NBT_DIMENSIONAL_POCKET_KEY = "pocketData";
	private static final String NBT_CONNECTOR_MAP_KEY = "connectorMap";
	private static final String NBT_SPAWN_COORDS_KEY = "spawnPos";
	private static final String NBT_SPAWN_COORDS_YAW_KEY = "spawnYaw";
	private static final String NBT_SPAWN_COORDS_PITCH_KEY = "spawnPitch";
	private static final String NBT_BLOCK_DIMENSION_KEY = "blockDim";
	private static final String NBT_CHUNK_COORDS_KEY = "chunkPos";
	private static final String NBT_BLOCK_MAP_KEY = "side_map";

	private static final String NBT_GENERATED_KEY = "generated";
	private static final String NBT_LAST_POS_KEY = "last_pos";
	private static final String NBT_CREATOR_KEY = "creator";
	private static final String NBT_ALLOWED_PLAYERS_KEY = "allowed_players";
	private static final String NBT_ALLOWED_PLAYERS_SIZE_KEY = "size";
	private static final String NBT_ALLOWED_PLAYERS_LIST_KEY = "list";
	private static final String NBT_STORED_KEY = "stored";
	private static final String NBT_CAPACITY_KEY = "capacity";
	private static final String NBT_RECEIVE_KEY = "maxReceive";
	private static final String NBT_EXTRACT_KEY = "maxExtract";
	private static final String NBT_LOCKED_KEY = "locked";
	private static final String NBT_ITEMS_KEY = "Items";

	private transient NBTTagCompound nbtTagCompound;

	@SerializedName(NBT_CONNECTOR_MAP_KEY)
	private Map<BlockPos, EnumPocketSideState> connectorMap = new HashMap<>();
	
	@SerializedName(NBT_GENERATED_KEY)
	private boolean isGenerated = false;

	@SerializedName(NBT_BLOCK_DIMENSION_KEY)
	private int blockDim;

	@SerializedName(NBT_CHUNK_COORDS_KEY)
	private BlockPos chunkPos;
	
	//private Map<BlockPos, Integer> block_map = new HashMap<>();
	@SerializedName(NBT_BLOCK_MAP_KEY)
	private Map<Integer, BlockPos> block_map = new HashMap<>();
	
	@SerializedName(NBT_LAST_POS_KEY)
	private BlockPos last_pos = new BlockPos(0, 0, 0);
	
	@SerializedName(NBT_SPAWN_COORDS_KEY)
	private BlockPos spawnPos;
	
	@SerializedName(NBT_SPAWN_COORDS_YAW_KEY)
	private float spawnYaw;
	
	@SerializedName(NBT_SPAWN_COORDS_PITCH_KEY)
	private float spawnPitch;

	@SerializedName(NBT_CREATOR_KEY)
	private String creator;
	
	@SerializedName(NBT_ALLOWED_PLAYERS_KEY)
	private ArrayList<String> allowed_players = new ArrayList<String>();
	
	@SerializedName(NBT_STORED_KEY)
	private int stored = 0;
	
	@SerializedName(NBT_CAPACITY_KEY)
	private int capacity = 1000000;
	
	@SerializedName(NBT_RECEIVE_KEY)
	protected int max_receive = 25000;
	
	@SerializedName(NBT_EXTRACT_KEY)
	protected int max_extract = 25000;
	
	@SerializedName(NBT_LOCKED_KEY)
	private boolean locked = false;
	
	@SerializedName(NBT_ITEMS_KEY)
	public NonNullList<ItemStack> items = NonNullList.<ItemStack>withSize(15, ItemStack.EMPTY);
	
	@SerializedName("fluid_tank")
	public FluidTank fluid_tank = new FluidTank(40000);
	
	@SerializedName("fill_level")
	public int fill_level;
	
	@SerializedName("fluid_capacity")
	public int fluid_capacity = 40000;
	
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
		
		//The interior size of the structure + 1. [Not a dynamic value. If different size pockets are required, extra dimensions will have to be added]
		int size = 16;
		
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				for (int z = 0; z < size; z++) {
					boolean flagX = x == 0 || x == (size - 1);
					boolean flagY = y == 0 || y == (size - 1);
					boolean flagZ = z == 0 || z == (size - 1);

					//Added those flags, so I could add these checks, almost halves the time.
					if (!(flagX || flagY || flagZ) || flagX && (flagY || flagZ) || flagY && flagZ) {
						continue;
					}

					//Creates the "edge" blocks first. Stylistic choice.
					if (x == 1 || y == 1 || z == 1) {
						storage.set(x, y, z, BlockHandler.BLOCK_DIMENSIONAL_POCKET_WALL_EDGE.getDefaultState());
					} else if (x == (size - 2) || y == (size - 2) || z == (size - 2)) {
						storage.set(x, y, z, BlockHandler.BLOCK_DIMENSIONAL_POCKET_WALL_EDGE.getDefaultState());
					} else {
						storage.set(x, y, z, BlockHandler.BLOCK_DIMENSIONAL_POCKET_WALL.getDefaultState());
					}
					
					//Update the structure so it displays.
					//NOTE: Skylight is a required component. Without this, the game will crash.
					storage.setSkyLight(new NibbleArray());
					world.markBlockRangeForRenderUpdate(new BlockPos(x, y, z), new BlockPos(x, y, z));
					world.scheduleUpdate(new BlockPos(x, y, z), BlockHandler.BLOCK_DIMENSIONAL_POCKET_WALL, 0);
				}
			}
		}
		
		//Added these checks to ensure correct pocket generation.
		Block check_block_one = world.getBlockState(new BlockPos(worldX + 1, worldY, worldZ + 1)).getBlock();
		Block check_block_two = world.getBlockState(new BlockPos(worldX + 2, worldY, worldZ + 2)).getBlock();

		this.isGenerated = check_block_one instanceof BlockDimensionalPocketWallEdge && check_block_two instanceof BlockDimensionalPocketWall;
		//this.isGenerated = world.getBlockState(new BlockPos(worldX + 1, worldY, worldZ + 1)).getBlock() instanceof BlockDimensionalPocketWallEdge;
		
		this.getNBT().setBoolean(NBT_GENERATED_KEY, isGenerated);

		if (!Strings.isNullOrEmpty(creatorName)) {
			this.setCreator(creatorName);
			//this.creator = creatorName;
			//this.getNBT().setString(NBT_GENERATED_KEY, creatorName);
			
			//this.addAllowedPlayer(creatorName);
			//NetworkHandler.sendPocketSetCreator(creatorName, last_pos);
			//NetworkHandler.sendCreatorPacketToServer(creatorName, last_pos);
			//NetworkHandler.sendCreatorPacketToClient(creatorName, last_pos);
		}
		
		//generateDefaultConnectors();
	}
	
	public void setCreator(String name) {
		if (!this.isGenerated) {
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
			TextComponentString comp = new TextComponentString(TextHelper.ITALIC + "Teleport Disabled. Pocket is blocked on all sides.");
			entityPlayer.sendMessage(comp);
			
			return;
		}

		EntityPlayerMP player = (EntityPlayerMP) entityPlayer;

		int dimID = player.dimension;
		BlockPos tempSet = new BlockPos(chunkPos.getX() * 16, this.chunkPos.getY() * 16, this.chunkPos.getZ() * 16).add(spawnPos);
		DimensionalShifter teleporter = DimensionalShiftUtils.createTeleporter(dimID, tempSet, spawnYaw, spawnPitch);

		this.generatePocket(entityPlayer.getName());

		TextComponentString comp = new TextComponentString(TextHelper.TEAL + "Entering pocket dimension...");
		if(!world.isRemote) {
			entityPlayer.sendMessage(comp);
		}
		
		if (dimID != DimensionalPockets.DIMENSION_ID) {
			DimensionalShiftUtils.shiftPlayerToDimension(player, DimensionalPockets.DIMENSION_ID, teleporter);//, tempSet, true);
		} else {
			//DimensionalShiftUtils.shiftPlayerToDimension(player, 0, teleporter, getSpawnPos(), false);
			DimensionalShiftUtils.shiftPlayerToDimension(player, DimensionalPockets.DIMENSION_ID, teleporter); //, tempSet, true);
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
					DimensionalShiftUtils.shiftPlayerToDimension(player, blockDim, teleporter); //, spawnPos, false);
				} else {
					teleporter.placeInPortal(player, 0);
				}
				
				TextComponentString comp = new TextComponentString(TextHelper.TEAL + "Leaving pocket dimension...");
				entityPlayer.sendMessage(comp);
				
			} else {
				if (!world.isRemote) {
					TextComponentString comp = new TextComponentString(TextHelper.ITALIC + "You're Trapped! Something is blocking your Pocket.");
					entityPlayer.sendMessage(comp);
				}
			}
		} else {
			if (!world.isRemote) {
				TextComponentString comp = new TextComponentString(TextHelper.ITALIC + "You're trapped! Someone broke your Pocket.");
				entityPlayer.sendMessage(comp);
			}
		}
	}

	

	public NBTTagCompound getNBT() {
		if (nbtTagCompound == null) {
			// generate first compound
			nbtTagCompound = new NBTTagCompound();
			
			//ItemStackHelper.saveAllItems(nbtTagCompound, this.items);
			
			NBTTagList list = new NBTTagList();
			
			for (int i = 0; i < this.items.size(); i++) {
				NBTTagCompound tag = new NBTTagCompound();
				items.get(i).writeToNBT(tag);
				
				list.appendTag(tag);
			}
			
			nbtTagCompound.setTag(NBT_ITEMS_KEY, list);
			nbtTagCompound.setInteger(NBT_STORED_KEY, this.stored);
			nbtTagCompound.setBoolean(NBT_LOCKED_KEY, this.locked);
			
			NBTTagCompound player_tag = new NBTTagCompound();
			
			for (int i = 0; i < this.allowed_players.size(); i++) {
				String put = this.allowed_players.get(i);
				
				player_tag.setString("" + i, put);
			}

			player_tag.setInteger(NBT_ALLOWED_PLAYERS_SIZE_KEY, this.allowed_players.size());
			
			nbtTagCompound.setTag(NBT_ALLOWED_PLAYERS_KEY, player_tag);
			
			nbtTagCompound.setBoolean(NBT_GENERATED_KEY, isGenerated);
			nbtTagCompound.setInteger(NBT_BLOCK_DIMENSION_KEY, blockDim);

			this.fluid_tank.writeToNBT(nbtTagCompound);
			nbtTagCompound.setInteger("fill_level", this.fill_level);
			
			// chunk_pos
			NBTTagCompound chunk_tag = new NBTTagCompound();

			if (chunkPos != null) {
				chunk_tag.setInteger("x", this.chunkPos.getX());
				chunk_tag.setInteger("y", this.chunkPos.getY());
				chunk_tag.setInteger("z", this.chunkPos.getZ());

				nbtTagCompound.setTag(NBT_CHUNK_COORDS_KEY, chunk_tag);
			}

			// last_pos
			NBTTagCompound last_pos_tag = new NBTTagCompound();

			if (chunkPos != null) {
				last_pos_tag.setInteger("x", this.last_pos.getX());
				last_pos_tag.setInteger("y", this.last_pos.getY());
				last_pos_tag.setInteger("z", this.last_pos.getZ());

				nbtTagCompound.setTag(NBT_CHUNK_COORDS_KEY, last_pos_tag);
			}
			
			// blockMap
			NBTTagCompound blockMapTag = new NBTTagCompound();
			
			if (this.block_map.size() > 0) {
				for (int i = 0; i < block_map.size(); i++) {
					BlockPos test = block_map.get(i);
					
					blockMapTag.setInteger("[" + Integer.toString(i) + "_x" + "]", test.getX());
					blockMapTag.setInteger("[" + Integer.toString(i) + "_y" + "]", test.getY());
					blockMapTag.setInteger("[" + Integer.toString(i) + "_z" + "]", test.getZ());
				}
				
				blockMapTag.setInteger("array_length", block_map.size());
			}
			
			nbtTagCompound.setTag(NBT_BLOCK_MAP_KEY, blockMapTag);
			
			nbtTagCompound.setFloat(NBT_SPAWN_COORDS_YAW_KEY, spawnYaw);
			nbtTagCompound.setFloat(NBT_SPAWN_COORDS_PITCH_KEY, spawnPitch);

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

		pocket.blockDim = pocketTag.getInteger(NBT_BLOCK_DIMENSION_KEY);
		
		pocket.fluid_tank.readFromNBT(pocketTag);
		pocket.fill_level = pocketTag.getInteger("fill_level");
		
		NBTTagList list = pocketTag.getTagList(NBT_ITEMS_KEY, 10);
		
		NonNullList<ItemStack> items = NonNullList.<ItemStack>withSize(15, ItemStack.EMPTY);
		
		for (int i = 0; i < list.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
			items.set(i, new ItemStack(nbttagcompound));
		}
		
		pocket.items = items;
		pocket.stored = pocketTag.getInteger(NBT_STORED_KEY);
		pocket.locked = pocketTag.getBoolean(NBT_LOCKED_KEY);
		
		if (pocketTag.hasKey(NBT_ALLOWED_PLAYERS_KEY)) {
			NBTTagCompound allowed_players = pocketTag.getCompoundTag(NBT_ALLOWED_PLAYERS_KEY);
			int size = allowed_players.getInteger(NBT_ALLOWED_PLAYERS_SIZE_KEY);
			
			for (int i = 0; i < size; i++) {
				pocket.addAllowedPlayer(allowed_players.getString("" + i));
			}
		}
		
		// Chunk
		if (pocketTag.hasKey(NBT_CHUNK_COORDS_KEY)) {
			NBTTagCompound chunk_tag = pocketTag.getCompoundTag(NBT_CHUNK_COORDS_KEY);
			
			int x_ = chunk_tag.getInteger("x");
			int y_ = chunk_tag.getInteger("y");
			int z_ = chunk_tag.getInteger("z");
			pocket.chunkPos = new BlockPos(x_, y_, z_);
		}
		
		
		// last_pos_tag
		if (pocketTag.hasKey(NBT_LAST_POS_KEY)) {
			NBTTagCompound last_pos_tag = pocketTag.getCompoundTag(NBT_LAST_POS_KEY);
			
			int x_ = last_pos_tag.getInteger("x");
			int y_ = last_pos_tag.getInteger("y");
			int z_ = last_pos_tag.getInteger("z");
			pocket.last_pos = new BlockPos(x_, y_, z_);
		}
		
		
		// last_pos_tag
		if (pocketTag.hasKey(NBT_BLOCK_MAP_KEY)) {
			NBTTagCompound blockMapTag = pocketTag.getCompoundTag(NBT_BLOCK_MAP_KEY);
			int length = blockMapTag.getInteger("array_length");
			
			for (int j = 0; j < length; j++) {
				int new_x = pocketTag.getInteger("[" + Integer.toString(j) + "_x" + "]");
				int new_y = pocketTag.getInteger("[" + Integer.toString(j) + "_y" + "]");
				int new_z = pocketTag.getInteger("[" + Integer.toString(j) + "_z" + "]");
				
				BlockPos test_pos = new BlockPos(new_x, new_y, new_z);
				
				pocket.block_map.put(j, test_pos);
				
				DimLogger.info("BLOCK_MAP LOADED FROM NBT");
			}
		}
		
		pocket.isGenerated = pocketTag.getBoolean(NBT_GENERATED_KEY);
		pocket.spawnYaw = pocketTag.getFloat(NBT_SPAWN_COORDS_YAW_KEY);
		pocket.spawnPitch = pocketTag.getFloat(NBT_SPAWN_COORDS_PITCH_KEY);

		pocket.creator = pocketTag.getString(NBT_CREATOR_KEY);
		
		if (pocket.creator.isEmpty()) {
			pocket.creator = null;
		}
		
		return pocket;
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
		return last_pos;
	}
	
	private BlockPos getSpawnPos() {
		return spawnPos;
	}
	
	public BlockPos getChunkPos() {
		return chunkPos;
	}

	public String getCreator() {
		return creator;
	}
	
	public void addBlockPosToArray(BlockPos pos) {
		if (block_map.containsValue(pos)) {
			DimLogger.info("Pocket: [" + this.chunkPos + "] Already contains that BlockPos. No update to the Map.");
		} else {
			block_map.put(block_map.size(), pos);
		}
		
		this.last_pos = pos;
	}
	
	public boolean doesArrayContain(BlockPos pos) {
		return block_map.containsValue(pos);
	}
	
	public Map<Integer, BlockPos> getMap() {
		return this.block_map;
	}
	
	public void setSpawnInPocket(BlockPos spawnPos, float spawnYaw, float spawnPitch) {
		this.spawnPos = spawnPos;
		
		this.spawnYaw = spawnYaw;
		getNBT().setFloat(NBT_SPAWN_COORDS_YAW_KEY, this.spawnYaw);
		this.spawnPitch = spawnPitch;
		getNBT().setFloat(NBT_SPAWN_COORDS_PITCH_KEY, this.spawnPitch);
	}
	
	public ArrayList<String> getAllowedPlayers() {
		return this.allowed_players;
	}
	
	public void addAllowedPlayer(String player_name) {
		if (!checkPlayerInList(player_name)) {
			if (!player_name.isEmpty()) {
				this.allowed_players.add(player_name);
			}
		}
	}
	
	public void removeAllowedPlayer(String player_name) {
		for (int i = 0; i < this.allowed_players.size(); i++) {
			String test = this.allowed_players.get(i);
			
			if (test.equals(player_name)) {
				this.allowed_players.remove(i);
			}
		}
	}
	
	public boolean checkPlayerInList(String player_name) {
		for (int i = 0; i < this.allowed_players.size(); i++) {
			String test = this.allowed_players.get(i);
			
			if (test.equals(player_name)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isGenerated() {
		return this.isGenerated;
	}
	
	public Map<BlockPos, EnumPocketSideState> getConnectorMap() {
		return this.connectorMap;
	}
	
	/**
	public void addConnectorToMap(BlockPos pos, EnumPocketSideState enum_state) {
		if (connectorMap.containsKey(pos)) {
			DimLogger.info("Pocket: [" + this.chunkPos + "] Already contains that Connector. No update to the Map.");
		} else {
			connectorMap.put(pos, enum_state);
		}
	} */
	
	public void updateConnector(BlockPos put_pos, EnumPocketSideState enum_state) {
		if (connectorMap.containsKey(put_pos)) {
			connectorMap.replace(put_pos, enum_state);
		} else {
			connectorMap.put(put_pos, enum_state);
		}
	}
	
	public void removeConnector(BlockPos pos) {
		connectorMap.remove(pos);
	}
	
	public int getStored() {
		return this.stored;
	}
	
	public void setStored(int set) {
		this.stored = set;
	}
	
	public void setMaxTransfer(int maxTransfer) {
		setMaxReceive(maxTransfer);
		setMaxExtract(maxTransfer);
	}

	public void setMaxReceive(int max_receive) {
		this.max_receive = max_receive;
	}

	public void setMaxExtract(int max_extract) {
		this.max_extract = max_extract;
	}

	public int getMaxReceive() {

		return max_receive;
	}

	public int getMaxExtract() {

		return max_extract;
	}

	/**
	 * This function is included to allow for server to client sync. Do not call this externally to the containing Tile Entity, as not all IEnergyHandlers are guaranteed to have it.
	 */
	public void setEnergyStored(int stored) {

		this.stored = stored;

		if (this.stored > 1000000) {
			this.stored = 1000000;
		} else if (this.stored < 0) {
			this.stored = 0;
		}
	}

	/**
	 * This function is included to allow the containing tile to directly and efficiently modify the stored contained in the EnergyStorage. Do not rely on this externally, as not all IEnergyHandlers are guaranteed to have it.
	 */
	public void modifyEnergyStored(int stored) {

		this.stored += stored;

		if (this.stored > 1000000) {
			this.stored = 1000000;
		} else if (this.stored < 0) {
			this.stored = 0;
		}
	}

	/* IEnergyStorage */
	
	public int receiveEnergy(int max_receive, boolean simulate) {
		int storedReceived = Math.min(this.getMaxEnergyStored() - stored, Math.min(this.max_receive, max_receive));

		if (!simulate) {
			this.stored += storedReceived;
		}
		
		//System.out.println("R: " + storedReceived);
		
		return storedReceived;
	}

	
	public int extractEnergy(int max_extract, boolean simulate) {
		int storedExtracted = Math.min(stored, Math.min(this.max_extract, max_extract));

		if (!simulate) {
			this.stored -= storedExtracted;
		}
		
		//System.out.println("E: " + storedExtracted);
		
		return storedExtracted;
	}

	public int getEnergyStored() {
		return this.stored;
	}

	public int getMaxEnergyStored() {
		return 1000000;
	}
	
	public boolean hasStored() {
		return this.stored > 0;
	}
	
	/**
	 * Check whether the pocket is locked.
	 * @return boolean of the lock state
	 */
	public boolean getLockState() {
		return this.locked;
	}
	
	/**
	 * Set the lock state of the pocket.
	 */
	public void setLockState(boolean change) {
		this.locked = change;
	}
}