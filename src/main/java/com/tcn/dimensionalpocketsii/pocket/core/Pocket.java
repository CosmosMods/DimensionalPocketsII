package com.tcn.dimensionalpocketsii.pocket.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.tcn.cosmoslibrary.common.chat.CosmosChatUtil;
import com.tcn.cosmoslibrary.common.comp.CosmosColour;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.cosmoslibrary.common.enums.EnumGeneralAllowState;
import com.tcn.cosmoslibrary.common.enums.EnumGeneratedState;
import com.tcn.cosmoslibrary.common.enums.EnumLockState;
import com.tcn.cosmoslibrary.common.enums.EnumSideState;
import com.tcn.cosmoslibrary.common.enums.EnumTrapState;
import com.tcn.cosmoslibrary.common.math.ChunkPos;
import com.tcn.cosmoslibrary.common.nbt.UtilNBT;
import com.tcn.cosmoslibrary.registry.gson.object.ObjectFluidTankCustom;
import com.tcn.cosmoslibrary.registry.gson.object.ObjectPlayerInformation;
import com.tcn.cosmoslibrary.registry.gson.object.ObjectTeleportPos;
import com.tcn.dimensionalpocketsii.DimReference;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.advancement.CoreTriggers;
import com.tcn.dimensionalpocketsii.core.management.CoreConfigurationManager;
import com.tcn.dimensionalpocketsii.core.management.CoreDimensionManager;
import com.tcn.dimensionalpocketsii.core.management.CoreModBusManager;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockPocket;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWall;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallEdge;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftLocation;
import com.tcn.dimensionalpocketsii.pocket.core.shift.Shifter;
import com.tcn.dimensionalpocketsii.pocket.core.shift.ShifterCore;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityConnector;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class Pocket {
	private static final String NBT_DIMENSIONAL_POCKET_KEY = "pocket_data";

	private static final String NBT_OWNER_KEY = "owner";
	private static final String NBT_GENERATED_KEY = "is_generated";
	private static final String NBT_LOCKED_KEY = "is_locked";
	private static final String NBT_ALLOWED_PLAYER_SHIFT_KEY = "allowed_player_shift";
	private static final String NBT_TRAP_KEY = "trap_players";
	private static final String NBT_HOSTILE_SPAWNS_KEY = "allow_hostile_spawns";
	private static final String NBT_COLOUR_KEY = "display_colour";
	private static final String NBT_INTERNAL_HEIGHT_KEY = "internal_height";
	private static final String NBT_ENERGY_STORED_KEY = "energy_stored";
	private static final String NBT_ENERGY_CAPACITY_KEY = "energy_capacity";
	private static final String NBT_ENERGY_RECEIVE_KEY = "energy_max_receive";
	private static final String NBT_ENERGY_EXTRACT_KEY = "energy_max_extract";
	private static final String NBT_BLOCK_DIMENSION_KEY = "source_dimension";
	private static final String NBT_CHUNK_POS_KEY = "chunk_pos";
	private static final String NBT_LAST_POS_KEY = "last_pos";
	private static final String NBT_SPAWN_POS_KEY = "spawn_pos";
	private static final String NBT_FLUID_TANK_KEY = "fluid_tank";
	private static final String NBT_ALLOWED_PLAYERS_KEY = "allowed_players_array";
	private static final String NBT_BLOCK_ARRAY_KEY = "block_array";
	private static final String NBT_POCKET_SIDE_KEY = "pocket_side_array";
	private static final String NBT_ITEMS_KEY = "items_array";
	
	@SerializedName(NBT_OWNER_KEY)
	private ObjectPlayerInformation owner;

	@SerializedName(NBT_GENERATED_KEY)
	private EnumGeneratedState is_generated = EnumGeneratedState.UNGENERATED;

	@SerializedName(NBT_LOCKED_KEY)
	private EnumLockState is_locked = EnumLockState.UNLOCKED;

	@SerializedName(NBT_ALLOWED_PLAYER_SHIFT_KEY)
	private EnumGeneralAllowState allowed_players_shift = EnumGeneralAllowState.ALLOWED;
	
	@SerializedName(NBT_TRAP_KEY)
	private EnumTrapState trap_players = EnumTrapState.FREE;
	
	@SerializedName(NBT_HOSTILE_SPAWNS_KEY)
	private EnumGeneralAllowState allow_hostile_spawns = EnumGeneralAllowState.BLOCKED;
	
	@SerializedName(NBT_COLOUR_KEY)
	private int display_colour = CosmosColour.POCKET_PURPLE.dec();
	
	@SerializedName(NBT_INTERNAL_HEIGHT_KEY)
	private int internal_height = CoreConfigurationManager.getInstance().getInternalHeight();
	
	@SerializedName(NBT_ENERGY_STORED_KEY)
	private int energy_stored = 0;
	
	@SerializedName(NBT_ENERGY_CAPACITY_KEY)
	private int energy_capacity = DimReference.CONSTANT.POCKET_RF_CAP;
	
	@SerializedName(NBT_ENERGY_RECEIVE_KEY)
	private int energy_max_receive = 25000;
	
	@SerializedName(NBT_ENERGY_EXTRACT_KEY)
	private int energy_max_extract = 25000;
	
	@SerializedName(NBT_BLOCK_DIMENSION_KEY)
	private ResourceLocation block_dimension = new ResourceLocation("");

	@SerializedName(NBT_CHUNK_POS_KEY)
	private ChunkPos chunk_pos = ChunkPos.ZERO;

	@SerializedName(NBT_LAST_POS_KEY)
	private BlockPos last_pos = BlockPos.ZERO;
	
	@SerializedName(NBT_SPAWN_POS_KEY)
	private ObjectTeleportPos spawn_pos = new ObjectTeleportPos(BlockPos.ZERO, 0, 0);
	
	@SerializedName(NBT_FLUID_TANK_KEY)
	private ObjectFluidTankCustom fluid_tank = new ObjectFluidTankCustom(new FluidTank(256000), 0);

	@SerializedName(NBT_ALLOWED_PLAYERS_KEY)
	private ArrayList<String> allowed_players_array = new ArrayList<String>();
	
	@SerializedName(NBT_BLOCK_ARRAY_KEY)
	private LinkedHashMap<Integer, BlockPos> block_array = new LinkedHashMap<>();

	@SerializedName(NBT_POCKET_SIDE_KEY)
	public EnumSideState[] pocket_side_array = EnumSideState.STANDARD;
	
	@SerializedName(NBT_ITEMS_KEY)
	public NonNullList<ItemStack> item_array = NonNullList.<ItemStack>withSize(DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE, ItemStack.EMPTY);
	
	private Pocket() {}
	
	public Pocket(ChunkPos pos) {
		this.chunk_pos = pos;
	}

	public Pocket(ChunkPos chunk_pos, RegistryKey<World> block_dim_type, BlockPos blockPos) {
		this.setSourceBlockDimension(block_dim_type);
		this.addPosToBlockArray(blockPos);
		this.setSpawnInPocket(new BlockPos(7, 2, 7), 0f, 0f);
		this.chunk_pos = chunk_pos;
	}
	
	public boolean exists() {
		return this.chunk_pos != null;
	}
	
	public boolean isSourceBlockPlaced() {
		return this.getSourceBlock() instanceof BlockPocket;
	}

	public Block getSourceBlock() {
		World world = this.getSourceBlockWorld();
		if (world == null) {
			DimensionalPockets.LOGGER.warn("[FAIL] getSourceBlock() { Dimension with RegistryKey<World>: " + this.block_dimension + " does not exist! (Mystcraft or GalactiCraft world?) } Pocket will return null...", Pocket.class);
			return null;
		}
		return world.getBlockState(this.getLastBlockPos()).getBlock();
	}
	
	public BlockState getSourceBlockState() {
		World world = this.getSourceBlockWorld();
		if (world == null) {
			DimensionalPockets.LOGGER.warn("[FAIL] getSourceBlock() { Dimension with RegistryKey<World>: " + this.block_dimension + " does not exist! (Mystcraft or GalactiCraft world?) } Pocket will return null...", Pocket.class);
			return null;
		}
		return world.getBlockState(this.getLastBlockPos());
	}
	
	public BlockPos getSourceBlockPos() {
		
		return this.getLastBlockPos();
	}
	
	public void setSourceBlockDimension(RegistryKey<World> type) {
		this.block_dimension = type.location();
	}
	
	public RegistryKey<World> getSourceBlockDimension() {
		return RegistryKey.create(Registry.DIMENSION_REGISTRY, this.block_dimension);
	}
	
	public World getSourceBlockWorld() {
		return ServerLifecycleHooks.getCurrentServer().getLevel(RegistryKey.create(Registry.DIMENSION_REGISTRY, block_dimension));
	}
	
	public EnumGeneratedState getGeneratedState() {
		return this.is_generated;
	}
	
	public boolean getGeneratedStateValue() {
		return this.is_generated.getValue();
	}

	public void setGeneratedState(EnumGeneratedState state) {
		this.is_generated = state;
	}
	
	public void setGeneratedState(boolean change) {
		this.is_generated = EnumGeneratedState.getStateFromValue(change);
	}

	/** - LockState - */
	public EnumLockState getLockState() {
		return this.is_locked;
	}

	public boolean getLockStateValue() {
		return this.is_locked.getValue();
	}
	
	public void setLockState(EnumLockState state) {
		this.is_locked = state;
	}
	
	public void setLockState(boolean change) {
		this.is_locked = EnumLockState.getStateFromValue(change);
	}
	
	/** - AllowedPlayerShift - */
	public EnumGeneralAllowState getAllowedPlayerState() {
		return this.allowed_players_shift;
	}

	public boolean getAllowedPlayerStateValue() {
		return this.allowed_players_shift.getValue();
	}
	
	public void setAllowedPlayerState(EnumGeneralAllowState state) {
		this.allowed_players_shift = state;
	}
	
	public void setAllowedPlayerState(boolean change) {
		this.allowed_players_shift = EnumGeneralAllowState.getStateFromValue(change);
	}
	
	/** - Hostile Spawn State - */
	public EnumGeneralAllowState getHostileSpawnState() {
		return this.allow_hostile_spawns;
	}

	public boolean getHostileSpawnStateValue() {
		return this.allow_hostile_spawns.getValue();
	}
	
	public void setHostileSpawnState(EnumGeneralAllowState state) {
		this.allow_hostile_spawns = state;
	}
	
	public void setHostileSpawnState(boolean change) {
		this.allow_hostile_spawns = EnumGeneralAllowState.getStateFromValue(change);
	}
	
	public EnumTrapState getTrapState() {
		return this.trap_players;
	}
	
	public boolean getTrapStateValue() {
		return this.trap_players.getValue();
	}
	
	public void setTrapState(EnumTrapState state) {
		this.trap_players = state;
	}
	
	public void setTrapState(boolean change) {
		this.trap_players = EnumTrapState.getStateFromValue(change);
	}
	
	public int getDisplayColour() {
		return this.display_colour;
	}
	
	public void setDisplayColour(PlayerEntity playerIn, World worldIn, int colourIn) {
		if (!(this.display_colour == colourIn)) {
			this.display_colour = colourIn;
			this.updateWallBlocks(playerIn, worldIn);
		}
	}
	
	public void setDisplayColour(CosmosColour colour) {
		this.display_colour = colour.dec();
	}

	public int getInternalHeight() {
		return this.internal_height;
	}
	
	
	public BlockPos getLastBlockPos() {
		return this.last_pos;
	}
	
	public BlockPos getLastBlockPosPlus(BlockPos pos) {
		return new BlockPos(this.last_pos.getX() + pos.getX(), this.last_pos.getY() + pos.getY(), this.last_pos.getZ() + pos.getZ());
	}
	
	private BlockPos getSpawnPos() {
		return spawn_pos.getPos();
	}

	public void updatePocketSpawnPos(BlockPos pos) {
		this.spawn_pos.setPos(pos);

		PocketRegistryManager.saveData();
	}

	public void setSpawnInPocket(BlockPos posIn, float yawIn, float pitchIn) {
		this.spawn_pos = new ObjectTeleportPos(posIn, yawIn, pitchIn);
	}
	
	public ChunkPos getChunkPos() {
		return this.chunk_pos;
	}
	
	public boolean updateOwner(@Nullable ServerPlayerEntity oldPlayerIn, ServerPlayerEntity newPlayerIn) {
		if (oldPlayerIn != null) {
			ObjectPlayerInformation oldInfo = new ObjectPlayerInformation(oldPlayerIn);

			if (this.owner.equals(oldInfo)) {
				CosmosChatUtil.sendPlayerMessageServer(oldPlayerIn, CosmosCompHelper.locComp(CosmosColour.GREEN, false, "dimensionalpocketsii.command.transfer.success.old").append(CosmosCompHelper.locComp(CosmosColour.CYAN, false, newPlayerIn.getDisplayName().getString())));
				CosmosChatUtil.sendPlayerMessageServer(newPlayerIn, CosmosCompHelper.locComp(CosmosColour.CYAN, false, oldPlayerIn.getDisplayName().getString()).append(CosmosCompHelper.locComp(CosmosColour.GREEN, false, "dimensionalpocketsii.command.transfer.success.new")));
				
				this.setOwnerInternal(newPlayerIn);
				
				return true;
			} else {
				CosmosChatUtil.sendPlayerMessageServer(oldPlayerIn, CosmosCompHelper.locComp(CosmosColour.LIGHT_RED, false, "dimensionalpocketsii.command.transfer.error.not_owner"));
				return false;
			}
		} else {
			CosmosChatUtil.sendPlayerMessageServer(newPlayerIn, CosmosCompHelper.locComp(CosmosColour.CYAN, false, this.getOwnerName()).append(CosmosCompHelper.locComp(CosmosColour.GREEN, false, "dimensionalpocketsii.command.transfer.success.new")));
			
			this.setOwnerInternal(newPlayerIn);
			
			return true;
		}
	}
	
	private void setOwnerInternal(PlayerEntity playerIn) {
		this.owner = new ObjectPlayerInformation(playerIn);
		this.addAllowedPlayer(playerIn);
	}

	public void setOwner(PlayerEntity playerIn) {
		if (!this.getGeneratedStateValue() || this.owner == null) {
			this.owner = new ObjectPlayerInformation(playerIn);
			this.addAllowedPlayer(playerIn);
		}
	}
	
	public boolean checkIfOwner(PlayerEntity playerIn) {
		String player_name = playerIn.getDisplayName().getString();
		UUID player_uuid = playerIn.getUUID();
		
		if (this.owner != null) {
			if (this.owner.getPlayerName().equals(player_name)) {
				if (this.owner.getPlayerUUID().equals(player_uuid)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public ObjectPlayerInformation getOwner() {
		return this.owner;
	}
	
	public String getOwnerName() {
		return this.owner.getPlayerName();
	}
	
	@SuppressWarnings("unused")
	private UUID getOwnerUUID() {
		return this.owner.getPlayerUUID();
	}
	
	/** - Block Array Things - */
	
	public void addPosToBlockArray(BlockPos pos) {
		if (this.block_array.containsValue(pos)) {
			DimensionalPockets.LOGGER.info("Pocket: [" + this.chunk_pos + "] Already contains that BlockPos. No update to the Map.");
		} else {
			this.block_array.put(block_array.size(), pos);
		}
		
		this.last_pos = pos;
	}
	
	public boolean doesBlockArrayContain(BlockPos pos) {
		return this.block_array.containsValue(pos);
	}
	
	
	/** - Allowed Player Things - */
	public ArrayList<String> getAllowedPlayersArray() {
		return this.allowed_players_array;
	}

	public void addAllowedPlayer(PlayerEntity playerIn) {
		if (!this.checkIfAllowedPlayer(playerIn)) {
			String player = playerIn.getDisplayName().getString();
			this.allowed_players_array.add(player);
		}
	}
	
	public void addAllowedPlayerNBT(String player_name) {
		if (!this.checkIfAllowedPlayerNBT(player_name)) {
			this.allowed_players_array.add(player_name);
		}
	}
	
	private boolean checkIfAllowedPlayerNBT(String player_name) {
		for (int i = 0; i < this.allowed_players_array.size(); i++) {
			String test_player = this.allowed_players_array.get(i);
			
			if (test_player.equals(player_name)) {
				return true;
			}
		}
		return false;
	}
	
	public void removeAllowedPlayer(PlayerEntity playerIn) {
		String player_name = playerIn.getDisplayName().getString();
		
		for (int i = 0; i < this.allowed_players_array.size(); i++) {
			String test_player = this.allowed_players_array.get(i);
			
			if (!this.checkIfOwner(playerIn)) {
				if (test_player.equals(player_name)) {
					this.allowed_players_array.remove(i);
				}
			}
		}
	}
	
	public void removeAllowedPlayerNBT(String playerNameIn) {
		for (int i = 0; i < this.allowed_players_array.size(); i++) {
			String test_player = this.allowed_players_array.get(i);
			
			if (this.owner != null) {
				if (!this.owner.getPlayerName().equals(playerNameIn)) {
					if (test_player.equals(playerNameIn)) {
						this.allowed_players_array.remove(i);
					}
				}
			} else {
				if (test_player.equals(playerNameIn)) {
					this.allowed_players_array.remove(i);
				}
			}
		}
	}
	
	public boolean checkIfAllowedPlayer(PlayerEntity playerIn) {
		String player_name = playerIn.getDisplayName().getString();
		
		for (int i = 0; i < this.allowed_players_array.size(); i++) {
			String test_player = this.allowed_players_array.get(i);
			
			if (test_player.equals(player_name)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checkIfPlayerCanShift(PlayerEntity playerIn, EnumShiftDirection direction) {
		if (this.checkIfOwner(playerIn)) {
			return true;
		} else {
			if (this.getAllowedPlayerStateValue()) {
				if (this.getLockStateValue()) {
					return this.checkIfAllowedPlayer(playerIn);
				} else {
					return true;
				}
			} else {
				return false;
			}
		}
	}
	
	public boolean canPlayerLeave(PlayerEntity playerIn) {
		if (this.checkIfOwner(playerIn) || this.checkIfAllowedPlayer(playerIn)) {
			return true;
		} else if (!this.getTrapStateValue()){
			return true;
		} else {
			return false;
		}
	}
	
	/** 
	 * - Energy Methods 
	 */
	public void setMaxTransfer(int maxTransfer) {
		this.setMaxReceive(maxTransfer);
		this.setMaxExtract(maxTransfer);
	}

	public void setMaxReceive(int max_receive) {
		this.energy_max_receive = max_receive;
	}

	public void setMaxExtract(int max_extract) {
		this.energy_max_extract = max_extract;
	}

	public int getMaxReceive() {
		return this.energy_max_receive;
	}

	public int getMaxExtract() {
		return this.energy_max_extract;
	}
	
	public void setEnergyStored(int stored) {
		this.energy_stored = stored;

		if (this.energy_stored > energy_capacity) {
			this.energy_stored = energy_capacity;
		} else if (this.energy_stored < 0) {
			this.energy_stored = 0;
		}
	}
	
	public void modifyEnergyStored(int stored) {
		this.energy_stored += stored;

		if (this.energy_stored > this.energy_capacity) {
			this.energy_stored = energy_capacity;
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
		return this.energy_capacity;
	}
	
	public boolean hasStored() {
		return this.energy_stored > 0;
	}
	
	public int getStoredLevelScaled(int scale) {
		return ((this.getEnergyStored() / 100) * scale) / (this.getMaxEnergyStored() / 100);
	}

	/** - FluidHandler Start */
	
	public int getFluidLevelScaled(int one) {
		float scaled = this.getCurrentFluidAmount() * one / this.getFluidTankCapacity();
		
		if (scaled == 0 && this.getCurrentFluidAmount() > 0) {
			return 1;
		} else {
			return (int) scaled;
		}
	}

	public int getCurrentFluidAmount() {
		return this.fluid_tank.getFluidTank().getFluidAmount();
	}
	
	public Fluid getCurrentStoredFluid() {
		this.updateFluidFillLevel();
		
		if (!this.isFluidTankEmpty()) {
			return this.fluid_tank.getFluidTank().getFluid().getFluid();
		}
		return null;
	}
	
	public String getCurrentStoredFluidName() {
		if (this.isFluidTankEmpty()) {
			return "Empty";
		}
		return this.fluid_tank.getFluidTank().getFluid().getTranslationKey();
	}

	public boolean isFluidTankEmpty() {
		return this.fluid_tank.getFluidTank().getFluidAmount() == 0;
	}

	public int getFluidTankCapacity() {
		return this.fluid_tank.getFluidTank().getCapacity();
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
		return this.fluid_tank.getFluidTank().fill(resource, doFill);
	}

	public FluidStack drain(FluidStack resource, FluidAction doDrain) {
		this.updateFluidFillLevel();
		
		return this.fluid_tank.getFluidTank().drain(resource.getAmount(), doDrain);
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
	
	public void emptyFluidTank() {
		this.fluid_tank.getFluidTank().setFluid(FluidStack.EMPTY);
	}
	
	public EnumSideState getSide(Direction facing) {
		return this.pocket_side_array[facing.get3DDataValue()];
	}
	
	public void setSide(Direction facing, EnumSideState side_state, boolean update) {
		this.pocket_side_array[facing.get3DDataValue()] = side_state;
	}
	
	public EnumSideState[] getSideArray() {
		return this.pocket_side_array;
	}

	public void setSideArray(EnumSideState[] new_array, boolean update) {
		this.pocket_side_array = new_array;
	}

	public void cycleSide(Direction facing, boolean update) {
		EnumSideState state = this.pocket_side_array[facing.get3DDataValue()];
		
		this.setSide(facing, state.getNextState(), update);
	}

	public boolean canConnect(Direction direction) {
		EnumSideState state = this.pocket_side_array[direction.get3DDataValue()];
		
		if (state.equals(EnumSideState.DISABLED)) {
			return false;
		}
		return true;
	}
	
	public void updateWallBlocks(PlayerEntity playerIn, World worldIn) {
		if (!worldIn.isClientSide) {
			if (playerIn != null) {
				if (playerIn.level.dimension().equals(CoreDimensionManager.POCKET_WORLD)) {
					World world = PocketRegistryManager.getWorldForPockets();
					
					int worldX = ChunkPos.scaleFromChunkPos(this.chunk_pos).getX();
					int worldZ = ChunkPos.scaleFromChunkPos(this.chunk_pos).getZ();
			
					int y_offset = PocketRegistryManager.pocket_y_offset;
					
					for (int x = 0; x < 1; x++) {
						for (int z = 2; z < 3; z ++) {
							for (int y = y_offset + 2; y < this.internal_height; y += 8) {
								BlockPos pos = new BlockPos(worldX + x, y, worldZ + z);
								
								world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
								world.setBlockAndUpdate(pos, CoreModBusManager.BLOCK_WALL.defaultBlockState());
							}
						}
					}
					PocketRegistryManager.saveData();
				}
			}
		}
	}
	
	public void updateBaseConnector(World worldIn) {
		if (!worldIn.isClientSide) {
			World world = PocketRegistryManager.getWorldForPockets();
			BlockPos pos = ChunkPos.convertFrom(this.chunk_pos);
			BlockPos offset = pos.offset(Direction.UP.getNormal());
			
			BlockState state = world.getBlockState(offset);
			
			if (world.getBlockEntity(offset) instanceof TileEntityConnector) {
				TileEntityConnector connector = (TileEntityConnector) world.getBlockEntity(offset);
				
				connector.sendUpdates(false);
				world.sendBlockUpdated(offset, state, state, 3);	
			}
		}
	}
	
	public void generatePocket(PlayerEntity playerIn) {
		if (!this.getGeneratedStateValue() || this.internal_height != CoreConfigurationManager.getInstance().getInternalHeight()) {
			World world = PocketRegistryManager.getWorldForPockets();
			Chunk chunk = world.getChunk(this.chunk_pos.getX(), this.chunk_pos.getZ());

			int worldX = ChunkPos.scaleFromChunkPos(this.chunk_pos).getX();
			int worldY = 1;
			int worldZ = ChunkPos.scaleFromChunkPos(this.chunk_pos).getZ();
	
			int size = PocketRegistryManager.pocket_size;
			int height = CoreConfigurationManager.getInstance().getInternalHeight();
			int y_offset = PocketRegistryManager.pocket_y_offset;
			
			for (int x = 0; x < size; x++) {
				for (int y = y_offset; y < height + y_offset; y++) {
					for (int z = 0; z < size; z++) {
						boolean flagX = x == 0 || x == (size - 1);
						boolean flagY = y == y_offset || y == (height);
						boolean flagZ = z == 0 || z == (size - 1);
						
						BlockPos pos = new BlockPos(x, y, z);
						BlockPos world_pos = new BlockPos(worldX + x, y, worldZ + z);
						
						//Creates a Bedrock frame around the Pocket to prevent glitching outside
						if (chunk.getBlockState(pos).getBlock() != CoreModBusManager.BLOCK_WALL_CONNECTOR
								&& chunk.getBlockState(pos).getBlock() != CoreModBusManager.BLOCK_WALL_CHARGER
									&& chunk.getBlockState(pos).getBlock() != CoreModBusManager.BLOCK_WALL_CRAFTER
										&& chunk.getBlockState(pos).getBlock() != CoreModBusManager.BLOCK_WALL_ENERGY_DISPLAY) {
							
							if (x == 0 || y == 1 || z == 0) {
								chunk.setBlockState(pos, Blocks.BEDROCK.defaultBlockState(), false);
							} else if (x == (size - 1) || y == (height - (1 - y_offset)) || z == (size - 1)) {
								chunk.setBlockState(pos, Blocks.BEDROCK.defaultBlockState(), false);
							} 
						}
						
						if (x == 0 && y == 1 && z == 0) {
							world.setBlockAndUpdate(world_pos, CoreModBusManager.BLOCK_WALL_CONNECTOR.defaultBlockState());
						}
						
						//Added those flags, so I could add these checks, almost halves the time.
						if (!(flagX || flagY || flagZ) || flagX && (flagY || flagZ) || flagY && flagZ) {
							continue;
						}
						
						//Creates the "edge" blocks first. Stylistic choice.
						if (x == 1 || y == (1 + y_offset) || z == 1) {
							chunk.setBlockState(pos, CoreModBusManager.BLOCK_WALL_EDGE.defaultBlockState().updateShape(Direction.UP, chunk.getBlockState(pos), world, pos, pos.offset(Direction.UP.getNormal())), false);
						} else if (x == (size - 2) || y == (height - (2 - y_offset)) || z == (size - 2)) {
							chunk.setBlockState(pos, CoreModBusManager.BLOCK_WALL_EDGE.defaultBlockState().updateShape(Direction.UP, chunk.getBlockState(pos), world, pos, pos.offset(Direction.UP.getNormal())), false);
						} else {
							if (chunk.getBlockState(pos).getBlock() != CoreModBusManager.BLOCK_WALL_CONNECTOR
									&& chunk.getBlockState(pos).getBlock() != CoreModBusManager.BLOCK_WALL_CHARGER
										&& chunk.getBlockState(pos).getBlock() != CoreModBusManager.BLOCK_WALL_CHARGER
											&& chunk.getBlockState(pos).getBlock() != CoreModBusManager.BLOCK_WALL_ENERGY_DISPLAY) {
								chunk.setBlockState(pos, CoreModBusManager.BLOCK_WALL.defaultBlockState().updateShape(Direction.UP, chunk.getBlockState(pos), world, pos, pos.offset(Direction.UP.getNormal())), false);
							}
						}

						world.sendBlockUpdated(world_pos, world.getBlockState(world_pos), world.getBlockState(world_pos).getBlock().defaultBlockState(), 19);
						world.markAndNotifyBlock(world_pos, chunk, world.getBlockState(world_pos), world.getBlockState(world_pos).getBlock().defaultBlockState(), 19, 0);
						world.getBlockState(world_pos).updateNeighbourShapes(world, world_pos, 19);
						world.getBlockState(world_pos).updateIndirectNeighbourShapes(world, world_pos, 19);
					}
				}
			}
			
			if (height != this.internal_height) {
				int[] rep = new int[] { 1, 14 };
				int[] upd = new int[] { 0, 1, 13, 14 };
				
				for (int x = rep[0]; x < rep[1]; x++) {
					for (int z = rep[0]; z < rep[1]; z++) {
						BlockPos pos = new BlockPos(x, this.internal_height, z);
						
						chunk.setBlockState(pos, Blocks.AIR.defaultBlockState(), false);
					}
				}
				
				world.sendBlockUpdated(new BlockPos(upd[0], this.internal_height, upd[1]), world.getBlockState(new BlockPos(upd[0], this.internal_height, upd[1])), CoreModBusManager.BLOCK_WALL_EDGE.defaultBlockState(), 3);
				world.sendBlockUpdated(new BlockPos(upd[1], this.internal_height, upd[0]), world.getBlockState(new BlockPos(upd[0], this.internal_height, upd[1])), CoreModBusManager.BLOCK_WALL_EDGE.defaultBlockState(), 3);
				world.sendBlockUpdated(new BlockPos(upd[2], this.internal_height, upd[3]), world.getBlockState(new BlockPos(upd[0], this.internal_height, upd[1])), CoreModBusManager.BLOCK_WALL_EDGE.defaultBlockState(), 3);
				world.sendBlockUpdated(new BlockPos(upd[3], this.internal_height, upd[2]), world.getBlockState(new BlockPos(upd[0], this.internal_height, upd[1])), CoreModBusManager.BLOCK_WALL_EDGE.defaultBlockState(), 3);
				
				if (!(this.internal_height > CoreConfigurationManager.getInstance().getInternalHeight())) {
					if (CoreConfigurationManager.getInstance().getInternalReplace()) {
						int[] edge = new int[] { 0, 14 };
					
						for (int x = edge[0]; x < edge[1]; x++) {
							for (int z = edge[0]; z < edge[1]; z++) {
								BlockPos pos = new BlockPos(x, this.internal_height, z);
								
								if (chunk.getBlockState(pos).getBlock().equals(CoreModBusManager.BLOCK_WALL)) {
									chunk.setBlockState(pos, CoreModBusManager.BLOCK_WALL_EDGE.defaultBlockState(), false);
								}
							}
						}
					}  else {
						DimensionalPockets.LOGGER.warn("Pocket internal height is larger than the config. Pocket will not reduce in size.", Pocket.class);
					}
				}
				
				chunk.markUnsaved();
			}
			
			Block check_block_one = world.getBlockState(new BlockPos((worldX + 1), worldY, (worldZ + 1))).getBlock();
			Block check_block_two = world.getBlockState(new BlockPos((worldX + 2), worldY, (worldZ + 2))).getBlock();
			Block check_block_three = world.getBlockState(new BlockPos(worldX, worldY, worldZ)).getBlock();
			Block check_block_four = world.getBlockState(new BlockPos(worldX, (worldY + internal_height) - 1, worldZ)).getBlock();
			
			//System.out.println(check_block_one + " || " + check_block_two + " || " + check_block_three + " || " + check_block_four);
			
			if (playerIn != null) {
				this.setOwner(playerIn);
			}
			
			this.setGeneratedState(check_block_one instanceof BlockWallEdge && check_block_two instanceof BlockWall && check_block_three.equals(CoreModBusManager.BLOCK_WALL_CONNECTOR) && check_block_four.equals(Blocks.BEDROCK));
			this.internal_height = height;
			
			PocketRegistryManager.saveData();
		}
	}
	
	public void shift(PlayerEntity playerIn, EnumShiftDirection direction, @Nullable BlockPos pocket_pos, @Nullable ItemStack stack) {
		World entity_world = playerIn.level;

		if (entity_world.isClientSide || !(playerIn instanceof ServerPlayerEntity)) {
			return;
		}
		
		World source_world = this.getSourceBlockWorld();
		ServerPlayerEntity server_player = (ServerPlayerEntity) playerIn;
		
		if (this.checkIfPlayerCanShift(playerIn, direction)) {
			if (direction.equals(EnumShiftDirection.ENTER)) {
				if (pocket_pos != null) {
					this.addPosToBlockArray(pocket_pos);
				}
				
				EnumShiftLocation location = EnumShiftLocation.getValidTeleportLocation(PocketRegistryManager.getWorldForPockets(), this.getSpawnPos());
				
				if (location != EnumShiftLocation.UNKNOWN) {
					BlockPos test = location.toBlockPos();
					
					BlockPos chunk = ChunkPos.scaleFromChunkPos(this.chunk_pos);
					BlockPos spawn = this.getSpawnPos();
					BlockPos shift = new BlockPos(chunk.getX() + spawn.getX() + test.getX(), chunk.getY() + spawn.getY() + test.getY(), chunk.getZ() + spawn.getZ() + test.getZ());
					
					Shifter shifter = Shifter.createTeleporter(CoreDimensionManager.POCKET_WORLD, direction, shift, this.spawn_pos.getYaw(), this.spawn_pos.getPitch(), false, true);
					
					this.generatePocket(server_player);
					ShifterCore.shiftPlayerToDimension(server_player, shifter);
					
					if (stack != null) {
						CoreTriggers.triggerUseShifter(server_player, stack);
					}
				} else {
					ShifterCore.sendPlayerToBedWithMessage(server_player, direction, "dimensionalpocketsii.pocket.status.location_blocked");
				}
			} else if (direction.equals(EnumShiftDirection.LEAVE)) {
				if (this.isSourceBlockPlaced()) {
					if (this.canPlayerLeave(playerIn)) {
						EnumShiftLocation location = EnumShiftLocation.getValidTeleportLocation(source_world, this.getLastBlockPos());
						
						if (location != EnumShiftLocation.UNKNOWN) {
							Shifter shifter = Shifter.createTeleporter(this.getSourceBlockDimension(), direction, this.getLastBlockPosPlus(location.toBlockPos()), 0.0F, 0.0F, false, true); //player.rotationYaw, player.rotationPitch);
							
							if (stack != null) {
								CoreTriggers.triggerUseShifter(server_player, stack);
							}
							
							ShifterCore.shiftPlayerToDimension(server_player, shifter);
						} else {
							ShifterCore.sendPlayerToBedWithMessage(server_player, direction, "dimensionalpocketsii.pocket.status.blocked");
						}
					} else {
						CosmosChatUtil.sendServerPlayerMessage(server_player, CosmosCompHelper.locComp(CosmosColour.LIGHT_RED, false, "dimensionalpocketsii.pocket.status.trapped"));
					}
				} else {
					ShifterCore.sendPlayerToBedWithMessage(server_player, direction, "dimensionalpocketsii.pocket.status.broken");
				}
			} else {
				ShifterCore.sendPlayerToBedWithMessage(server_player, direction, "dimensionalpocketsii.pocket.status.direction_unknown");
			}
		} else {
			CosmosChatUtil.sendServerPlayerMessage(server_player, CosmosCompHelper.getErrorText("dimensionalpocketsii.pocket.status.locked"));
		}
	}
	
	public CompoundNBT getNBT() {
		CompoundNBT compound_nbt = new CompoundNBT();
		
		//Base Info
		if (this.getOwner() != null) {
			this.getOwner().writeToNBT(compound_nbt, NBT_OWNER_KEY);
		}
		
		compound_nbt.putBoolean(NBT_GENERATED_KEY, this.getGeneratedStateValue());
		compound_nbt.putBoolean(NBT_LOCKED_KEY, this.getLockStateValue());
		compound_nbt.putBoolean(NBT_TRAP_KEY, this.getTrapStateValue());
		compound_nbt.putBoolean(NBT_ALLOWED_PLAYER_SHIFT_KEY, this.getAllowedPlayerStateValue());
		compound_nbt.putBoolean(NBT_HOSTILE_SPAWNS_KEY, this.getHostileSpawnStateValue());
		compound_nbt.putInt(NBT_COLOUR_KEY, this.getDisplayColour());
		UtilNBT.writeDimensionToNBT(getSourceBlockDimension(), compound_nbt);
		compound_nbt.putInt(NBT_ENERGY_STORED_KEY, this.getEnergyStored());
		compound_nbt.putInt(NBT_ENERGY_CAPACITY_KEY, this.getMaxEnergyStored());
		compound_nbt.putInt(NBT_ENERGY_RECEIVE_KEY, this.getMaxReceive());
		compound_nbt.putInt(NBT_ENERGY_EXTRACT_KEY, this.getMaxExtract());
		
		//ChunkPos
		if (this.chunk_pos != null) {
			CompoundNBT chunk = new CompoundNBT();
			
			chunk.putInt(UtilNBT.Const.NBT_POS_X_KEY, this.getChunkPos().getX());
			chunk.putInt(UtilNBT.Const.NBT_POS_Z_KEY, this.getChunkPos().getZ());
			
			compound_nbt.put(NBT_CHUNK_POS_KEY, chunk);
		}

		//LastPos
		if (this.getLastBlockPos() != null) {
			CompoundNBT last = new CompoundNBT();
			
			last.putInt(UtilNBT.Const.NBT_POS_X_KEY, this.getLastBlockPos().getX());
			last.putInt(UtilNBT.Const.NBT_POS_Y_KEY, this.getLastBlockPos().getY());
			last.putInt(UtilNBT.Const.NBT_POS_Z_KEY, this.getLastBlockPos().getZ());
			
			compound_nbt.put(NBT_LAST_POS_KEY, last);
		}

		//SpawnPos
		if (this.getSpawnPos() != null) {
			CompoundNBT spawn = new CompoundNBT();
			
			this.spawn_pos.writeToNBT(spawn);
			
			compound_nbt.put(NBT_SPAWN_POS_KEY, spawn);
		}
		
		//FluidTank
		CompoundNBT tank = new CompoundNBT();
		this.fluid_tank.writeToNBT(tank);
		compound_nbt.put(NBT_FLUID_TANK_KEY, tank);
		
		//Player Map
		CompoundNBT players = new CompoundNBT();
		for (int i = 0; i < this.allowed_players_array.size(); i++){
			String player = this.allowed_players_array.get(i);
			
			players.putString(Integer.toString(i), player);
		}
		players.putInt("length", this.allowed_players_array.size());
		compound_nbt.put(NBT_ALLOWED_PLAYERS_KEY, players);
		
		//Side Map
		CompoundNBT sideMapTag = new CompoundNBT();
		if (this.block_array.size() > 0) {
			for (int i = 0; i < this.block_array.size(); i++) {
				BlockPos test = this.block_array.get(i);
				CompoundNBT sidePosTag = new CompoundNBT();
				
				sidePosTag.putInt(UtilNBT.Const.NBT_POS_X_KEY, test.getX());
				sidePosTag.putInt(UtilNBT.Const.NBT_POS_Y_KEY, test.getY());
				sidePosTag.putInt(UtilNBT.Const.NBT_POS_Z_KEY, test.getZ());
				sideMapTag.put(Integer.toString(i), sidePosTag);
			}
		}
		sideMapTag.putInt("length", this.block_array.size());
		compound_nbt.put(NBT_BLOCK_ARRAY_KEY, sideMapTag);
		
		//Items
		CompoundNBT item_list = new CompoundNBT();
		for (int i = 0; i < this.item_array.size(); i++) {
			ItemStack stack = this.item_array.get(i);
			
			item_list.put(Integer.toString(i), stack.serializeNBT());
		}
		
		//Pocket Side Array
		CompoundNBT side_array = new CompoundNBT();
		for (int i = 0; i < this.pocket_side_array.length; i++) {
			Integer value = this.pocket_side_array[i].getIndex();
			
			side_array.putInt(Integer.toString(i), value);
		}
		side_array.putInt("length", this.pocket_side_array.length);
		compound_nbt.put(NBT_POCKET_SIDE_KEY, side_array);
		
		
		compound_nbt.put(NBT_ITEMS_KEY, item_list);

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
		pocket.owner = ObjectPlayerInformation.readFromNBT(pocketTag, NBT_OWNER_KEY);
		
		pocket.setGeneratedState(pocketTag.getBoolean(NBT_GENERATED_KEY));
		pocket.setLockState(pocketTag.getBoolean(NBT_LOCKED_KEY));
		pocket.setAllowedPlayerState(pocketTag.getBoolean(NBT_ALLOWED_PLAYER_SHIFT_KEY));
		pocket.setTrapState(pocketTag.getBoolean(NBT_TRAP_KEY));
		pocket.setHostileSpawnState(pocketTag.getBoolean(NBT_HOSTILE_SPAWNS_KEY));
		pocket.display_colour = pocketTag.getInt(NBT_COLOUR_KEY);
		pocket.setSourceBlockDimension(RegistryKey.create(Registry.DIMENSION_REGISTRY, UtilNBT.readDimensionFromNBT(pocketTag)));
		pocket.setEnergyStored(pocketTag.getInt(NBT_ENERGY_STORED_KEY));
		pocket.energy_capacity = pocketTag.getInt(NBT_ENERGY_CAPACITY_KEY);
		pocket.energy_max_receive = pocketTag.getInt(NBT_ENERGY_RECEIVE_KEY);
		pocket.energy_max_extract = pocketTag.getInt(NBT_ENERGY_EXTRACT_KEY);
		
		//ChunkPos
		CompoundNBT chunkTag = pocketTag.getCompound(NBT_CHUNK_POS_KEY);
		pocket.chunk_pos = new ChunkPos(chunkTag.getInt(UtilNBT.Const.NBT_POS_X_KEY), chunkTag.getInt(UtilNBT.Const.NBT_POS_Z_KEY));
		
		//LastPos
		CompoundNBT lastTag = pocketTag.getCompound(NBT_LAST_POS_KEY);
		pocket.last_pos = new BlockPos(lastTag.getInt(UtilNBT.Const.NBT_POS_X_KEY), lastTag.getInt(UtilNBT.Const.NBT_POS_Y_KEY), lastTag.getInt(UtilNBT.Const.NBT_POS_Z_KEY));
		
		//SpawnPos
		CompoundNBT spawnTag = pocketTag.getCompound(NBT_SPAWN_POS_KEY);
		pocket.spawn_pos = ObjectTeleportPos.readFromNBT(spawnTag);
		
		//FluidTank
		CompoundNBT tankTag = pocketTag.getCompound(NBT_FLUID_TANK_KEY);
		pocket.fluid_tank = ObjectFluidTankCustom.readFromNBT(tankTag);
		
		//Player Map
		CompoundNBT playersTag = pocketTag.getCompound(NBT_ALLOWED_PLAYERS_KEY);
		for (int i = 0; i < playersTag.getInt("length"); i++) {
			String player_name = playersTag.getString(Integer.toString(i));
			
			pocket.addAllowedPlayerNBT(player_name);
		}
		
		//Side Map
		CompoundNBT sideMapTag = pocketTag.getCompound(NBT_BLOCK_ARRAY_KEY);
		for (int i = 0; i < sideMapTag.getInt("length"); i++) {
			CompoundNBT sideTag = sideMapTag.getCompound(Integer.toString(i));
			
			pocket.addPosToBlockArray(new BlockPos(sideTag.getInt(UtilNBT.Const.NBT_POS_X_KEY), sideTag.getInt(UtilNBT.Const.NBT_POS_Y_KEY), sideTag.getInt(UtilNBT.Const.NBT_POS_Z_KEY)));
		}
		
		//Items
		CompoundNBT itemsTag = pocketTag.getCompound(NBT_ITEMS_KEY);
		for (int i = 0; i < DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE; i++) {
			CompoundNBT item = itemsTag.getCompound(Integer.toString(i));
			
			pocket.item_array.set(i, ItemStack.of(item));
		}
		
		//Pocket Side Array
		CompoundNBT sideArrayTag = pocketTag.getCompound(NBT_POCKET_SIDE_KEY);
		for (int i = 0; i < sideArrayTag.getInt("length"); i++) {
			int side = sideArrayTag.getInt(Integer.toString(i));
			
			pocket.pocket_side_array[i] = EnumSideState.getStateFromIndex(side);
		}
		
		return pocket;
	}
	
	public ItemStack generateItemStackWithNBT() {
		ItemStack item_stack = new ItemStack(CoreModBusManager.BLOCK_POCKET);

		if (!item_stack.hasTag()) {
			item_stack.setTag(new CompoundNBT());
		}

		CompoundNBT compound = new CompoundNBT();
		CompoundNBT chunk_tag = new CompoundNBT();
		
		int x = this.chunk_pos.getX();
		int z = this.chunk_pos.getZ();
		
		chunk_tag.putInt("X", x);
		chunk_tag.putInt("Z", z);

		compound.put("chunk_set", chunk_tag);
		
		compound.putInt("colour", this.getDisplayColour());
		
		item_stack.getTag().put("nbt_data", compound);
		
		return item_stack;
	}

}