package com.zhr.dimensionalpocketsii.pocket.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import com.google.gson.annotations.SerializedName;
import com.zhr.cosmoslibrary.impl._client.util.TextHelper;
import com.zhr.cosmoslibrary.impl.enums.EnumConnectionType;
import com.zhr.cosmoslibrary.impl.nbt.FluidTankNBT;
import com.zhr.cosmoslibrary.impl.nbt.PlayerNBT;
import com.zhr.cosmoslibrary.impl.nbt.UtilNBT;
import com.zhr.cosmoslibrary.impl.util.HashMapUtils;
import com.zhr.cosmoslibrary.impl.util.ModUtil;
import com.zhr.dimensionalpocketsii.DimReference;
import com.zhr.dimensionalpocketsii.DimensionalPockets;
import com.zhr.dimensionalpocketsii.core.management.bus.BusSubscriberMod;
import com.zhr.dimensionalpocketsii.pocket.core.block.BlockPocket;
import com.zhr.dimensionalpocketsii.pocket.core.block.BlockWall;
import com.zhr.dimensionalpocketsii.pocket.core.block.BlockWallEdge;
import com.zhr.dimensionalpocketsii.pocket.core.management.PocketDimensionManager;
import com.zhr.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.zhr.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.zhr.dimensionalpocketsii.pocket.core.shift.Shifter;
import com.zhr.dimensionalpocketsii.pocket.core.shift.ShifterCore;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class Pocket {
	private static final String NBT_DIMENSIONAL_POCKET_KEY = "pocket_data";
	private static final String NBT_CONNECTOR_MAP_KEY = "connector_array";
	private static final String NBT_SPAWN_POS_KEY = "spawn_pos";
	private static final String NBT_SPAWN_YAW_KEY = "spawn_yaw";
	private static final String NBT_SPAWN_PITCH_KEY = "spawn_pitch";
	private static final String NBT_BLOCK_DIMENSION_KEY = "block_dimension";
	private static final String NBT_CHUNK_POS_KEY = "chunk_pos";
	private static final String NBT_BLOCK_ARRAY_KEY = "block_array";

	private static final String NBT_GENERATED_KEY = "is_generated";
	private static final String NBT_LAST_POS_KEY = "last_pos";
	private static final String NBT_CREATOR_KEY = "creator";
	private static final String NBT_ALLOWED_PLAYERS_KEY = "allowed_players_array";
	private static final String NBT_ENERGY_STORED_KEY = "energy_stored";
	private static final String NBT_ENERGY_CAPACITY_KEY = "energy_capacity";
	private static final String NBT_ENERGY_RECEIVE_KEY = "energy_max_receive";
	private static final String NBT_ENERGY_EXTRACT_KEY = "energy_max_extract";
	private static final String NBT_LOCKED_KEY = "is_locked";
	private static final String NBT_ITEMS_KEY = "items_array";
	private static final String NBT_FLUID_TANK_FILL_LEVEL_KEY = "fluid_tank_fill_level";
	private static final String NBT_FLUID_TANK_KEY = "fluid_tank";
	
	private static final String NBT_POS_X_KEY = "x";
	private static final String NBT_POS_Y_KEY = "y";
	private static final String NBT_POS_Z_KEY = "z";

	private transient CompoundNBT compound_nbt;
	
	@SerializedName(NBT_CREATOR_KEY)
	private PlayerNBT creator;

	@SerializedName(NBT_GENERATED_KEY)
	private boolean is_generated = false;

	@SerializedName(NBT_LOCKED_KEY)
	private boolean is_locked = false;
	
	@SerializedName(NBT_SPAWN_YAW_KEY)
	private float spawn_yaw;
	
	@SerializedName(NBT_SPAWN_PITCH_KEY)
	private float spawn_pitch;

	@SerializedName(NBT_ENERGY_STORED_KEY)
	private int energy_stored = 0;
	
	@SerializedName(NBT_ENERGY_CAPACITY_KEY)
	private int energy_capacity = DimReference.CONSTANT.POCKET_RF_CAP;
	
	@SerializedName(NBT_ENERGY_RECEIVE_KEY)
	private int energy_max_receive = 25000;
	
	@SerializedName(NBT_ENERGY_EXTRACT_KEY)
	private int energy_max_extract = 25000;
	
	@SerializedName(NBT_BLOCK_DIMENSION_KEY)
	private ResourceLocation block_dimension;

	@SerializedName(NBT_CHUNK_POS_KEY)
	private BlockPos chunk_pos;

	@SerializedName(NBT_LAST_POS_KEY)
	private BlockPos last_pos;
	
	@SerializedName(NBT_SPAWN_POS_KEY)
	private BlockPos spawn_pos;
	
	@SerializedName(NBT_FLUID_TANK_KEY)
	private FluidTankNBT fluid_tank = new FluidTankNBT(new FluidTank(256000), 0);

	@SerializedName(NBT_ALLOWED_PLAYERS_KEY)
	private ArrayList<PlayerNBT> allowed_players_array = new ArrayList<PlayerNBT>();
	
	@SerializedName(NBT_CONNECTOR_MAP_KEY)
	private LinkedHashMap<BlockPos, EnumConnectionType> connector_array = new LinkedHashMap<>();
	
	@SerializedName(NBT_BLOCK_ARRAY_KEY)
	private LinkedHashMap<Integer, BlockPos> block_array = new LinkedHashMap<>();

	@SerializedName(NBT_ITEMS_KEY)
	public NonNullList<ItemStack> item_array = NonNullList.<ItemStack>withSize(DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE, ItemStack.EMPTY);

	public Pocket() {}

	public Pocket(BlockPos chunk_pos, RegistryKey<World> block_dim_type, BlockPos blockPos) {
		this.setSourceBlockDimension(block_dim_type);
		this.addPosToBlockArray(blockPos);
		this.setSpawnInPocket(new BlockPos(7, 2, 7), 0f, 0f);
		this.chunk_pos = chunk_pos;
	}
	
	public boolean isSourceBlockPlaced() {
		return this.getSourceBlock() instanceof BlockPocket;
	}

	public Block getSourceBlock() {
		World world = this.getSourceBlockWorld();
		if (world == null) {
			DimensionalPockets.LOGGER.warn("[FAIL] getSourceBlock() { Dimension with ID: " + this.block_dimension + " does not exist! (Mystcraft or GalactiCraft world?) } Pocket will return null...", Pocket.class);
			return null;
		}
		return world.getBlockState(this.getLastBlockPos()).getBlock();
	}

	public RegistryKey<World> getSourceBlockDimension() {
		return RegistryKey.getOrCreateKey(Registry.WORLD_KEY, this.block_dimension);
	}
	
	public void setSourceBlockDimension(RegistryKey<World> type) {
		this.block_dimension = type.getLocation();
	}

	public World getSourceBlockWorld() {
		return ServerLifecycleHooks.getCurrentServer().getWorld(RegistryKey.getOrCreateKey(Registry.WORLD_KEY, block_dimension));
	}
	
	public boolean isGenerated() {
		return this.is_generated;
	}

	public boolean getLockState() {
		return this.is_locked;
	}
	
	public void setLockState(boolean change) {
		this.is_locked = change;
	}
	
	public BlockPos getLastBlockPos() {
		return last_pos;
	}
	
	private BlockPos getSpawnPos() {
		return spawn_pos;
	}

	public void updatePocketSpawnPos(BlockPos pos) {
		this.spawn_pos = pos;
		
		PocketRegistryManager.saveData();
	}

	public void setSpawnInPocket(BlockPos spawnPos, float spawnYaw, float spawnPitch) {
		this.spawn_pos = spawnPos;
		
		this.spawn_yaw = spawnYaw;
		this.spawn_pitch = spawnPitch;
	}
	
	public BlockPos getChunkPos() {
		return chunk_pos;
	}

	public void setCreator(PlayerEntity playerIn) {
		if (this.creator == null) {
			PlayerNBT player = new PlayerNBT(playerIn);
			
			this.creator = player;
			this.addAllowedPlayer(playerIn);
		}
		/**
		if (!this.is_generated) {
			NBTPlayerInformation player = new NBTPlayerInformation(playerIn);
			
			this.creator = player;
			this.addAllowedPlayer(playerIn);
		}*/
	}
	
	public boolean checkCreator(PlayerEntity player) {
		String player_name = player.getDisplayName().getString();
		UUID player_uuid = player.getUniqueID();
		
		if (this.creator.getPlayerName().equals(player_name)) {
			if (this.creator.getPlayerUUID().equals(player_uuid)) {
				return true;
			}
		}
		
		return false;
	}
	
	public PlayerNBT getCreator() {
		return creator;
	}
	
	public String getCreatorName() {
		return this.creator.getPlayerName();
	}
	
	public UUID getCreatorUUID() {
		return this.creator.getPlayerUUID();
	}
	
	
	/** - Block Array Things - */
	
	public void addPosToBlockArray(BlockPos pos) {
		if (block_array.containsValue(pos)) {
			DimensionalPockets.LOGGER.info("Pocket: [" + this.chunk_pos + "] Already contains that BlockPos. No update to the Map.");
		} else {
			block_array.put(block_array.size(), pos);
		}
		
		this.last_pos = pos;
	}
	
	public boolean doesBlockArrayContain(BlockPos pos) {
		return block_array.containsValue(pos);
	}
	
	
	/** - Allowed Player Things - */
	
	public ArrayList<PlayerNBT> getAllowedPlayersArray() {
		return this.allowed_players_array;
	}
	
	public void addAllowedPlayerNBT(String player_name, UUID player_uuid) {
		if (!checkIfAllowedPlayerNBT(player_name, player_uuid)) {
			PlayerNBT player = new PlayerNBT(player_name, player_uuid);
			this.allowed_players_array.add(player);
		}
	}
	
	public void addAllowedPlayer(PlayerEntity playerIn) {
		if (!checkIfAllowedPlayer(playerIn)) {
			PlayerNBT player = new PlayerNBT(playerIn);
			this.allowed_players_array.add(player);
		}
	}
	
	public void removeAllowedPlayer(PlayerEntity playerIn) {
		String player_name = playerIn.getDisplayName().getString();
		UUID player_uuid = playerIn.getUniqueID();
		
		for (int i = 0; i < this.allowed_players_array.size(); i++) {
			PlayerNBT test_player = this.allowed_players_array.get(i);
			
			if (test_player.getPlayerName().equals(player_name) && test_player.getPlayerUUID().equals(player_uuid)) {
				this.allowed_players_array.remove(i);
			}
		}
	}
	
	public boolean checkIfAllowedPlayerNBT(String player_name, UUID player_uuid) {
		for (int i = 0; i < this.allowed_players_array.size(); i++) {
			PlayerNBT test_player = this.allowed_players_array.get(i);
			
			if (test_player.getPlayerName().equals(player_name) && test_player.getPlayerUUID().equals(player_uuid)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checkIfAllowedPlayer(PlayerEntity playerIn) {
		String player_name = playerIn.getDisplayName().getString();
		UUID player_uuid = playerIn.getUniqueID();
		
		for (int i = 0; i < this.allowed_players_array.size(); i++) {
			PlayerNBT test_player = this.allowed_players_array.get(i);
			
			if (test_player.getPlayerName().equals(player_name) && test_player.getPlayerUUID().equals(player_uuid)) {
				return true;
			}
		}
		return false;
	}
	
	/** TODO - Connector Stuff -  */
	public LinkedHashMap<BlockPos, EnumConnectionType> getConnectorArray() {
		return this.connector_array;
	}
	
	public void updateConnectorInArray(BlockPos put_pos, EnumConnectionType enum_state) {
		if (connector_array.containsKey(put_pos)) {
			DimensionalPockets.LOGGER.info("Connector already exists in Map. Updating...", Pocket.class);
			connector_array.replace(put_pos, enum_state);
		} else {
			DimensionalPockets.LOGGER.info("Connector added to Map.", Pocket.class);
			connector_array.put(put_pos, enum_state);
		}
		
		PocketRegistryManager.saveData();
	}
	
	public void removeConnector(BlockPos pos) {
		DimensionalPockets.LOGGER.info("Connector removed from Map.", Pocket.class);
		connector_array.remove(pos);
		
		PocketRegistryManager.saveData();
	}
	
	
	/** - Energy Methods - */
	
	public int getStored() {
		return this.energy_stored;
	}
	
	public void setStored(int set) {
		this.energy_stored = set;
	}
	
	public void setMaxTransfer(int maxTransfer) {
		setMaxReceive(maxTransfer);
		setMaxExtract(maxTransfer);
	}

	public void setMaxReceive(int max_receive) {
		this.energy_max_receive = max_receive;
	}

	public void setMaxExtract(int max_extract) {
		this.energy_max_extract = max_extract;
	}

	public int getMaxReceive() {

		return energy_max_receive;
	}

	public int getMaxExtract() {

		return energy_max_extract;
	}
	
	public void setEnergyStored(int stored) {

		this.energy_stored = stored;

		if (this.energy_stored > 1000000) {
			this.energy_stored = 1000000;
		} else if (this.energy_stored < 0) {
			this.energy_stored = 0;
		}
	}
	
	public void modifyEnergyStored(int stored) {

		this.energy_stored += stored;

		if (this.energy_stored > 1000000) {
			this.energy_stored = 1000000;
		} else if (this.energy_stored < 0) {
			this.energy_stored = 0;
		}
	}
	
	public int receiveEnergy(int max_receive, boolean simulate) {
		int storedReceived = Math.min(this.getMaxEnergyStored() - energy_stored, Math.min(this.energy_max_receive, max_receive));

		if (!simulate) {
			this.energy_stored += storedReceived;
		}
		
		return storedReceived;
	}

	public int extractEnergy(int max_extract, boolean simulate) {
		int storedExtracted = Math.min(energy_stored, Math.min(this.energy_max_extract, max_extract));

		if (!simulate) {
			this.energy_stored -= storedExtracted;
		}
		
		return storedExtracted;
	}

	public int getEnergyStored() {
		return this.energy_stored;
	}

	public int getMaxEnergyStored() {
		return 1000000;
	}
	
	public boolean hasStored() {
		return this.energy_stored > 0;
	}
	
	
	/** - FluidHandler Start */
	
	public int getFluidLevelScaled(int one) {
		return this.fluid_tank.getFluidTank().getFluidAmount() * one / this.fluid_tank.getFluidTank().getCapacity();
	}

	public int getCurrentFluidAmount() {
		return this.fluid_tank.getFluidTank().getFluidAmount();
	}
	
	public Fluid getCurrentStoredFluid() {
		this.updateFluidFillLevel();
		
		if (!isFluidTankEmpty()) {
			return this.fluid_tank.getFluidTank().getFluid().getFluid();
		}
		return null;
	}
	
	public String getCurrentStoredFluidName() {
		if (isFluidTankEmpty()) {
			return "Empty";
		}
		return this.fluid_tank.getFluidTank().getFluid().getDisplayName().toString();
	}

	public boolean isFluidTankEmpty() {
		return this.fluid_tank.getFluidTank().getFluidAmount() == 0;
	}

	public int getFluidTankCapacity() {
		return 256000;
	}
	
	public int getFluidFillLevel() {
		return this.fluid_tank.getFillLevel();
	}

	public void setFluidFillLevel(int set) {
		this.fluid_tank.setFillLevel(set);
	}
	
	public void updateFluidFillLevel() {
		if (!this.isFluidTankEmpty()) {
			if (this.getFluidLevelScaled(16) == 0) {
				this.fluid_tank.setFillLevel(1);
			} else {
				this.fluid_tank.setFillLevel(this.getFluidLevelScaled(16));
			}
		} else {
			this.fluid_tank.setFillLevel(0);
		}
	}
	
	public int fill(FluidStack resource, FluidAction doFill) {
		this.updateFluidFillLevel();
		return fluid_tank.getFluidTank().fill(resource, doFill);
	}

	public FluidStack drain(FluidStack resource, FluidAction doDrain) {
		this.updateFluidFillLevel();
		
		return fluid_tank.getFluidTank().drain(resource.getAmount(), doDrain);
	}

	public FluidStack drain(int maxDrain, FluidAction doDrain) {
		this.updateFluidFillLevel();
		return this.fluid_tank.getFluidTank().drain(maxDrain, doDrain);
	}
	
	public boolean canFill(Direction from, Fluid fluid) {
		return true;
	}
	
	public boolean canDrain(Direction from, Fluid fluid) {
		return true;
	}
	
	public FluidTank getFluidTank() {
		return this.fluid_tank.getFluidTank();
	}
	
	public void setFluidTank(FluidTank tank) {
		this.fluid_tank.setFluidTank(tank);
	}
	
	public void generatePocket(PlayerEntity playerIn) {
		if (this.is_generated) {
			return;
		}
		
		World world = PocketRegistryManager.getWorldForPockets();
		
		int worldX = chunk_pos.getX() * 16;
		int worldY = (chunk_pos.getY() * 16 ) + 1;
		int worldZ = chunk_pos.getZ() * 16;

		Chunk chunk = world.getChunk(chunk_pos.getX(), chunk_pos.getZ());
		
		int l = worldY >> 4;
		ChunkSection storage = chunk.getSections()[l]; //.getBlockStorageArray()[l];
		
		if (storage == null) {
			storage = new ChunkSection(worldY);
			chunk.getSections()[l] = storage;
		}
		
		int size = PocketRegistryManager.pocket_size;
		int y_offset = PocketRegistryManager.pocket_y_offset;
		
		for (int x = 0; x < size; x++) {
			for (int y = y_offset; y < size + y_offset; y++) {
				for (int z = 0; z < size; z++) {
					boolean flagX = x == 0 || x == (size - 1);
					boolean flagY = y == y_offset || y == (size);
					boolean flagZ = z == 0 || z == (size - 1);
					
					BlockPos pos = new BlockPos(x, y, z);
					
					//Added those flags, so I could add these checks, almost halves the time.
					if (!(flagX || flagY || flagZ) || flagX && (flagY || flagZ) || flagY && flagZ) {
						continue;
					}
					
					//Creates the "edge" blocks first. Stylistic choice.
					if (x == 1 || y == (1 + y_offset) || z == 1) {
						storage.setBlockState(x, y, z, BusSubscriberMod.BLOCK_DIMENSIONAL_POCKET_WALL_EDGE.getDefaultState().updatePostPlacement(Direction.UP, world.getBlockState(pos), world, pos, pos.offset(Direction.UP)));
						world.setBlockState(pos, BusSubscriberMod.BLOCK_DIMENSIONAL_POCKET_WALL_EDGE.getDefaultState().updatePostPlacement(Direction.UP, world.getBlockState(pos), world, pos, pos.offset(Direction.UP)));
					} else if (x == (size - 2) || y == (size - (2 - y_offset)) || z == (size - 2)) {
						storage.setBlockState(x, y, z, BusSubscriberMod.BLOCK_DIMENSIONAL_POCKET_WALL_EDGE.getDefaultState().updatePostPlacement(Direction.UP, world.getBlockState(pos), world, pos, pos.offset(Direction.UP)));
					} else {
						storage.setBlockState(x, y, z, BusSubscriberMod.BLOCK_DIMENSIONAL_POCKET_WALL.getDefaultState());
					}
					//Update the structure so it displays.
					world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos).getBlock().getDefaultState(), 3);
					world.markAndNotifyBlock(pos, chunk, world.getBlockState(pos), world.getBlockState(pos).getBlock().getDefaultState(), 3, z);
					
					storage.getBlockState(x, y, z).updateDiagonalNeighbors(world, pos, 3);
					storage.getBlockState(x, y, z).updateNeighbours(world, pos, 3);
				}
			}
		}
		
		chunk.setLoaded(true);
		
		//Added these checks to ensure correct pocket generation.
		Block check_block_one = world.getBlockState(new BlockPos((worldX + 1), (worldY + 1), (worldZ + 1))).getBlock();
		Block check_block_two = world.getBlockState(new BlockPos((worldX + 2), (worldY + 1), (worldZ + 2))).getBlock();
		
		System.out.println("XX [" + (worldX + 1) + "] YY [" + (worldY + 1) + "] ZZ [" + (worldZ + 1) + "]");
		System.out.println("C_X [" + chunk_pos.getX() + "]" + " C_Y [" + chunk_pos.getY() + "]" + " C_Z [" + chunk_pos.getZ() + "]");
		
		this.is_generated = check_block_one instanceof BlockWallEdge && check_block_two instanceof BlockWall;
		
		if (playerIn != null) {
			this.setCreator(playerIn);
		}
		
		PocketRegistryManager.saveData();
	}
	
	
	public void shiftTo(PlayerEntity entityPlayer) {
		if (entityPlayer.world.isRemote || !(entityPlayer instanceof ServerPlayerEntity)) {
			return;
		}
		
		World world = this.getSourceBlockWorld();
		EnumShiftDirection teleportSide = EnumShiftDirection.getValidTeleportLocation(world, this.getLastBlockPos());
		if (teleportSide == EnumShiftDirection.UNKNOWN) {
			ShifterCore.sendPlayerToBedWithMessage((ServerPlayerEntity)entityPlayer, world, TextHelper.ITALIC + "Teleport Blocked!");
		}

		ServerPlayerEntity player = (ServerPlayerEntity) entityPlayer;

		RegistryKey<World> dimID = player.world.getDimensionKey();
		BlockPos tempSet = new BlockPos(chunk_pos.getX() * 16, this.chunk_pos.getY(), this.chunk_pos.getZ() * 16).add(spawn_pos);
		Shifter teleporter = ShifterCore.createTeleporter(dimID, tempSet, spawn_yaw, spawn_pitch);

		this.generatePocket(entityPlayer);

		StringTextComponent comp = new StringTextComponent(TextHelper.TEAL + "Entering pocket dimension...");
		if(!world.isRemote) {
			entityPlayer.sendMessage(comp, null);
		}
		
		ShifterCore.shiftPlayerToDimension(player, PocketDimensionManager.POCKET_WORLD, teleporter);
	}

	public void shiftFrom(PlayerEntity entityPlayer) {
		ServerPlayerEntity player = (ServerPlayerEntity) entityPlayer;
		World world = this.getSourceBlockWorld();
		
		if (this.isSourceBlockPlaced()) {
			EnumShiftDirection teleportSide = EnumShiftDirection.getValidTeleportLocation(world, this.getLastBlockPos());
			if (teleportSide != EnumShiftDirection.UNKNOWN) {
				BlockPos tempBlockSet = new BlockPos(this.getLastBlockPos()).add(teleportSide.toBlockPos());
				BlockPos spawnPos = new BlockPos(tempBlockSet);
				
				Shifter teleporter = ShifterCore.createTeleporter(this.getSourceBlockDimension(), spawnPos, 0.0F, 0.0F); //player.rotationYaw, player.rotationPitch);
				ShifterCore.shiftPlayerToDimension(player, this.getSourceBlockDimension(), teleporter);
				
				ModUtil.sendPlayerMessage(world, entityPlayer, TextHelper.TEAL + "Leaving pocket dimension...");
			} else {
				ShifterCore.sendPlayerToBedWithMessage((ServerPlayerEntity) entityPlayer, world, TextHelper.ITALIC + "Something is blocking your Pocket!");
			}
		} else {
			ShifterCore.sendPlayerToBedWithMessage((ServerPlayerEntity) entityPlayer, world, TextHelper.ITALIC + "Someone broke your Pocket!");
		}
	}
	
	public CompoundNBT getNBT() {
		//if (compound_nbt == null) {
			// generate first compound
			compound_nbt = new CompoundNBT();
			
			//Base Info
			this.getCreator().writeToNBT(compound_nbt, NBT_CREATOR_KEY);
			compound_nbt.putBoolean(NBT_GENERATED_KEY, this.isGenerated());
			compound_nbt.putBoolean(NBT_LOCKED_KEY, this.getLockState());
			UtilNBT.writeDimensionToNBT(getSourceBlockDimension(), compound_nbt);
			compound_nbt.putFloat(NBT_SPAWN_YAW_KEY, this.spawn_yaw);
			compound_nbt.putFloat(NBT_SPAWN_PITCH_KEY, this.spawn_pitch);
			compound_nbt.putInt(NBT_ENERGY_STORED_KEY, this.getStored());
			compound_nbt.putInt(NBT_ENERGY_CAPACITY_KEY, this.getMaxEnergyStored());
			compound_nbt.putInt(NBT_ENERGY_RECEIVE_KEY, this.getMaxReceive());
			compound_nbt.putInt(NBT_ENERGY_EXTRACT_KEY, this.getMaxExtract());
			compound_nbt.putInt(NBT_FLUID_TANK_FILL_LEVEL_KEY, this.getFluidFillLevel());
			
			//ChunkPos
			if (chunk_pos != null) {
				CompoundNBT chunk = new CompoundNBT();
				
				chunk.putInt(NBT_POS_X_KEY, this.getChunkPos().getX());
				chunk.putInt(NBT_POS_Y_KEY, this.getChunkPos().getY());
				chunk.putInt(NBT_POS_Z_KEY, this.getChunkPos().getZ());
				
				compound_nbt.put(NBT_CHUNK_POS_KEY, chunk);
			}

			//LastPos
			if (this.getLastBlockPos() != null) {
				CompoundNBT last = new CompoundNBT();
				
				last.putInt(NBT_POS_X_KEY, this.getLastBlockPos().getX());
				last.putInt(NBT_POS_Y_KEY, this.getLastBlockPos().getY());
				last.putInt(NBT_POS_Z_KEY, this.getLastBlockPos().getZ());
				
				compound_nbt.put(NBT_LAST_POS_KEY, last);
			}

			//SpawnPos
			if (this.getSpawnPos() != null) {
				CompoundNBT spawn = new CompoundNBT();
				
				spawn.putInt(NBT_POS_X_KEY, this.getSpawnPos().getX());
				spawn.putInt(NBT_POS_Y_KEY, this.getSpawnPos().getY());
				spawn.putInt(NBT_POS_Z_KEY, this.getSpawnPos().getZ());

				compound_nbt.put(NBT_SPAWN_POS_KEY, spawn);
			}
			
			//FluidTank
			CompoundNBT tank = new CompoundNBT();
			this.fluid_tank.writeToNBT(tank);
			compound_nbt.put(NBT_FLUID_TANK_KEY, tank);
			
			//Player Map
			CompoundNBT players = new CompoundNBT();
			for (int i = 0; i < this.allowed_players_array.size(); i++){
				PlayerNBT player = this.allowed_players_array.get(i);
				
				CompoundNBT player_tag = new CompoundNBT();
				
				player.writeToNBT(player_tag);
				players.put(Integer.toString(i), player_tag);
			}
			players.putInt("length", allowed_players_array.size());
			compound_nbt.put(NBT_ALLOWED_PLAYERS_KEY, players);
			
			//Connector Map
			CompoundNBT connectors = new CompoundNBT();
			for (int i = 0; i < this.getConnectorArray().size(); i++) {
				CompoundNBT connector = new CompoundNBT();
				CompoundNBT connector_pos = new CompoundNBT();
				
				BlockPos pos = (BlockPos) HashMapUtils.Linked.getKeyByIndex(this.getConnectorArray(), i);
				EnumConnectionType state = (EnumConnectionType) HashMapUtils.Linked.getValueByIndex(this.getConnectorArray(), i);
				
				connector_pos.putInt(NBT_POS_X_KEY, pos.getX());
				connector_pos.putInt(NBT_POS_Y_KEY, pos.getY());
				connector_pos.putInt(NBT_POS_Z_KEY, pos.getZ());
				
				connector.put("pos", connector_pos);
				connector.putInt("type", state.getIndex());
				
				connectors.put(Integer.toString(i), connector);
			}
			connectors.putInt("length", this.getConnectorArray().size());
			compound_nbt.put(NBT_CONNECTOR_MAP_KEY, connectors);
			
			//Side Map
			CompoundNBT sideMapTag = new CompoundNBT();
			if (this.block_array.size() > 0) {
				for (int i = 0; i < this.block_array.size(); i++) {
					BlockPos test = this.block_array.get(i);
					CompoundNBT sidePosTag = new CompoundNBT();
					
					sidePosTag.putInt(NBT_POS_X_KEY, test.getX());
					sidePosTag.putInt(NBT_POS_Y_KEY, test.getY());
					sidePosTag.putInt(NBT_POS_Z_KEY, test.getZ());
					sideMapTag.put(Integer.toString(i), sidePosTag);
				}
			}
			sideMapTag.putInt("length", this.block_array.size());
			compound_nbt.put(NBT_BLOCK_ARRAY_KEY, sideMapTag);
			
			//Items
			CompoundNBT item_list = new CompoundNBT();
			for (int i = 0; i < item_array.size(); i++) {
				ItemStack stack = item_array.get(i);
				
				item_list.put(Integer.toString(i), stack.serializeNBT());
			}
			
			compound_nbt.put(NBT_ITEMS_KEY, item_list);
		//}

		return compound_nbt;
	}

	public void writeToNBT(CompoundNBT tag) {
		tag.put(NBT_DIMENSIONAL_POCKET_KEY, this.getNBT());
	}
	
	public static Pocket readFromNBT(CompoundNBT compound) {
		return readFromNBT(compound, NBT_DIMENSIONAL_POCKET_KEY);
	}

	public static Pocket readFromNBT(CompoundNBT compound_nbt, String key) {
		CompoundNBT pocketTag = compound_nbt.getCompound(key);
		Pocket pocket = new Pocket();
		
		//BaseInfo
		pocket.creator = PlayerNBT.readFromNBT(compound_nbt, NBT_CREATOR_KEY);
		pocket.is_generated = pocketTag.getBoolean(NBT_GENERATED_KEY);
		pocket.setLockState(pocketTag.getBoolean(NBT_LOCKED_KEY));
		pocket.setSourceBlockDimension(RegistryKey.getOrCreateKey(Registry.WORLD_KEY, UtilNBT.readDimensionFromNBT(compound_nbt)));
		pocket.spawn_yaw = pocketTag.getInt(NBT_SPAWN_YAW_KEY);
		pocket.spawn_pitch = pocketTag.getInt(NBT_SPAWN_PITCH_KEY);
		pocket.setEnergyStored(pocketTag.getInt(NBT_ENERGY_STORED_KEY));
		pocket.energy_capacity = pocketTag.getInt(NBT_ENERGY_CAPACITY_KEY);
		pocket.energy_max_receive = pocketTag.getInt(NBT_ENERGY_RECEIVE_KEY);
		pocket.energy_max_extract = pocketTag.getInt(NBT_ENERGY_EXTRACT_KEY);
		pocket.setFluidFillLevel(pocketTag.getInt(NBT_FLUID_TANK_FILL_LEVEL_KEY));
		
		//ChunkPos
		CompoundNBT chunkTag = pocketTag.getCompound(NBT_CHUNK_POS_KEY);
		pocket.chunk_pos = new BlockPos(chunkTag.getInt(NBT_POS_X_KEY), chunkTag.getInt(NBT_POS_Y_KEY), chunkTag.getInt(NBT_POS_Z_KEY));
		
		//LastPos
		CompoundNBT lastTag = pocketTag.getCompound(NBT_LAST_POS_KEY);
		pocket.last_pos = new BlockPos(lastTag.getInt(NBT_POS_X_KEY), lastTag.getInt(NBT_POS_Y_KEY), lastTag.getInt(NBT_POS_Z_KEY));
		
		//SpawnPos
		CompoundNBT spawnTag = pocketTag.getCompound(NBT_SPAWN_POS_KEY);
		pocket.spawn_pos = new BlockPos(spawnTag.getInt(NBT_POS_X_KEY), spawnTag.getInt(NBT_POS_Y_KEY), spawnTag.getInt(NBT_POS_Z_KEY));
		
		//FluidTank
		CompoundNBT tankTag = pocketTag.getCompound(NBT_FLUID_TANK_KEY);
		pocket.fluid_tank = FluidTankNBT.readFromNBT(tankTag);
		
		//Player Map
		CompoundNBT playersTag = pocketTag.getCompound(NBT_ALLOWED_PLAYERS_KEY);
		for (int i = 0; i < playersTag.getInt("length"); i++) {
			CompoundNBT player = playersTag.getCompound(Integer.toString(i));
			
			PlayerNBT player_info = PlayerNBT.readFromNBT(player);
			String player_name = player_info.getPlayerName();
			UUID player_uuid = player_info.getPlayerUUID();
			
			pocket.addAllowedPlayerNBT(player_name, player_uuid);
		}
		
		//Connector Map
		CompoundNBT connectorsTag = pocketTag.getCompound(NBT_CONNECTOR_MAP_KEY);
		for (int i = 0; i < connectorsTag.getInt("length"); i++) {
			CompoundNBT connectorTag = connectorsTag.getCompound(Integer.toString(i));
			CompoundNBT connectorPosTag = connectorTag.getCompound("pos");
			
			pocket.updateConnectorInArray(new BlockPos(connectorPosTag.getInt(NBT_POS_X_KEY), connectorPosTag.getInt(NBT_POS_Y_KEY), connectorPosTag.getInt(NBT_POS_Z_KEY)), EnumConnectionType.getStateFromIndex(connectorTag.getInt("type")));
		}
		
		//Side Map
		CompoundNBT sideMapTag = pocketTag.getCompound(NBT_BLOCK_ARRAY_KEY);
		for (int i = 0; i < sideMapTag.getInt("length"); i++) {
			CompoundNBT sideTag = sideMapTag.getCompound(Integer.toString(i));
			
			pocket.addPosToBlockArray(new BlockPos(sideTag.getInt(NBT_POS_X_KEY), sideTag.getInt(NBT_POS_Y_KEY), sideTag.getInt(NBT_POS_Z_KEY)));
		}
		
		//Items
		CompoundNBT itemsTag = pocketTag.getCompound(NBT_ITEMS_KEY);
		for (int i = 0; i < DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE; i++) {
			CompoundNBT item = itemsTag.getCompound(Integer.toString(i));
			
			pocket.item_array.set(i, ItemStack.read(item));
		}
		
		return pocket;
	}
}