package com.tcn.dimensionalpocketsii.pocket.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.tcn.cosmoslibrary.common.chat.CosmosChatUtil;
import com.tcn.cosmoslibrary.common.enums.EnumGeneralAllowState;
import com.tcn.cosmoslibrary.common.enums.EnumGeneratedState;
import com.tcn.cosmoslibrary.common.enums.EnumLockState;
import com.tcn.cosmoslibrary.common.enums.EnumSideState;
import com.tcn.cosmoslibrary.common.enums.EnumTrapState;
import com.tcn.cosmoslibrary.common.interfaces.blockentity.IEnergyHolder;
import com.tcn.cosmoslibrary.common.lib.ComponentColour;
import com.tcn.cosmoslibrary.common.lib.ComponentHelper;
import com.tcn.cosmoslibrary.common.lib.CosmosChunkPos;
import com.tcn.cosmoslibrary.common.lib.MathHelper;
import com.tcn.cosmoslibrary.common.nbt.CosmosNBTHelper;
import com.tcn.cosmoslibrary.common.nbt.CosmosNBTHelper.Const;
import com.tcn.cosmoslibrary.core.teleport.EnumSafeTeleport;
import com.tcn.cosmoslibrary.registry.gson.object.ObjectBlockPosDimension;
import com.tcn.cosmoslibrary.registry.gson.object.ObjectDestinationInfo;
import com.tcn.cosmoslibrary.registry.gson.object.ObjectFluidTankCustom;
import com.tcn.cosmoslibrary.registry.gson.object.ObjectPlayerInformation;
import com.tcn.dimensionalpocketsii.DimReference;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.advancement.CoreTriggers;
import com.tcn.dimensionalpocketsii.core.management.ConfigurationManager;
import com.tcn.dimensionalpocketsii.core.management.DimensionManager;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockPocket;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallBase;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallEdge;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallModule;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleConnector;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.shift.Shifter;
import com.tcn.dimensionalpocketsii.pocket.core.shift.ShifterCore;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;
import net.minecraftforge.items.ItemHandlerHelper;

@SuppressWarnings("unused")
public class Pocket implements IEnergyHolder, Container {
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
	private int display_colour = ComponentColour.POCKET_PURPLE.dec();
	
	@SerializedName(NBT_INTERNAL_HEIGHT_KEY)
	private int internal_height = ConfigurationManager.getInstance().getInternalHeight();
	
	@SerializedName(NBT_ENERGY_STORED_KEY)
	private int energy_stored = 0;
	
	@SerializedName(NBT_ENERGY_CAPACITY_KEY)
	public int energy_capacity = DimReference.CONSTANT.POCKET_FE_CAP;
	
	@SerializedName(NBT_ENERGY_RECEIVE_KEY)
	public int energy_max_receive = DimReference.CONSTANT.POCKET_FE_REC;
	
	@SerializedName(NBT_ENERGY_EXTRACT_KEY)
	public int energy_max_extract = DimReference.CONSTANT.POCKET_FE_EXT;
	
	@SerializedName(NBT_BLOCK_DIMENSION_KEY)
	private ResourceLocation block_dimension = new ResourceLocation("");

	@SerializedName(NBT_CHUNK_POS_KEY)
	private CosmosChunkPos chunk_pos = CosmosChunkPos.ZERO;

	@SerializedName(NBT_LAST_POS_KEY)
	private ObjectBlockPosDimension last_pos = new ObjectBlockPosDimension(BlockPos.ZERO, new ResourceLocation(""));

	@SerializedName(NBT_SPAWN_POS_KEY)
	private ObjectDestinationInfo spawn_pos = new ObjectDestinationInfo(BlockPos.ZERO, 0, 0);
	
	@SerializedName(NBT_FLUID_TANK_KEY)
	public ObjectFluidTankCustom fluid_tank = new ObjectFluidTankCustom(new FluidTank(DimReference.CONSTANT.POCKET_FLUID_CAP), 0);

	@SerializedName(NBT_ALLOWED_PLAYERS_KEY)
	private ArrayList<String> allowed_players_array = new ArrayList<String>();
	
	@SerializedName(NBT_BLOCK_ARRAY_KEY)
	private LinkedHashMap<Integer, ObjectBlockPosDimension> block_array = new LinkedHashMap<>();

	@SerializedName(NBT_POCKET_SIDE_KEY)
	public EnumSideState[] pocket_side_array = EnumSideState.STANDARD.clone();
	
	@SerializedName(NBT_ITEMS_KEY)
	public NonNullList<ItemStack> item_array = NonNullList.<ItemStack>withSize(DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE, ItemStack.EMPTY);
	
	private Pocket() { }
	
	public Pocket(CosmosChunkPos pos) {
		this.chunk_pos = pos;
	}

	public Pocket(CosmosChunkPos chunkPosIn, ResourceKey<Level> blockDimensionIn, BlockPos blockPos) {
		this.setSourceBlockDimension(blockDimensionIn);
		this.addPosToBlockArray(blockPos, blockDimensionIn.location());
		this.setSpawnInPocket(new BlockPos(7, 2, 7), 0f, 0f);
		this.chunk_pos = chunkPosIn;
	}
	
	public boolean exists() {
		return this.chunk_pos != null;
	}
	
	public boolean isSourceBlockPlaced() {
		return this.getSourceBlock() instanceof BlockPocket;
	}
	
	public Block getSourceBlock() {
		Level world = this.getSourceBlockLevel();
		if (world == null) {
			DimensionalPockets.CONSOLE.warning("[FAIL] getSourceBlock() { Dimension with RegistryKey<Level>: " + this.block_dimension + " does not exist! (Mystcraft or GalactiCraft world?) } Pocket will return null...");
			return null;
		}
		return world.getBlockState(this.getLastBlockPos()).getBlock();
	
	}
	
	public BlockState getSourceBlockState() {
		Level world = this.getSourceBlockLevel();
		if (world == null) {
			DimensionalPockets.CONSOLE.warning("[FAIL] getSourceBlock() { Dimension with RegistryKey<Level>: " + this.block_dimension + " does not exist! (Mystcraft or GalactiCraft world?) } Pocket will return null...");
			return null;
		}
		return world.getBlockState(this.getLastBlockPos());
	}
	
	public BlockPos getSourceBlockPos() {
		return this.getLastBlockPos();
	}
	
	public void setSourceBlockDimension(ResourceKey<Level> type) {
		this.block_dimension = type.location();
	}
	
	public ResourceKey<Level> getSourceBlockDimension() {
		return ResourceKey.create(Registry.DIMENSION_REGISTRY, this.block_dimension);
	}
	
	public Level getSourceBlockLevel() {
		return ServerLifecycleHooks.getCurrentServer().getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, block_dimension));
	}

	public Level getLevelFromResource(ResourceLocation location) {
		return ServerLifecycleHooks.getCurrentServer().getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, location));
	}
	
	public void resetSourceBlock() {
		for (int i = 0; i < this.block_array.size(); i++) {
			ObjectBlockPosDimension object = this.block_array.get(i);
			
			Level level = this.getLevelFromResource(object.getDimension());
			BlockPos pos = object.getPos();
			
			if (level.getBlockState(pos).getBlock() instanceof BlockPocket) {
				this.last_pos = object;
				this.setSourceBlockDimension(ResourceKey.create(Registry.DIMENSION_REGISTRY, object.getDimension()));
				
				DimensionalPockets.CONSOLE.info("Pocket updated: [" + pos + "]");
				break;
			}
		}
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
	
	public void setDisplayColour(Player playerIn, Level worldIn, int colourIn) {
		if (!(this.display_colour == colourIn)) {
			this.display_colour = colourIn;
			this.updateWallBlocks(playerIn, worldIn);
		}
	}
	
	public void setDisplayColour(ComponentColour colour) {
		this.display_colour = colour.dec();
	}

	public int getInternalHeight() {
		return this.internal_height;
	}
	
	public void setInternalHeight(int height) {
		this.internal_height = height;
	}

	public BlockPos getLastBlockPos() {
		return this.last_pos.getPos();
	}
	
	public ObjectBlockPosDimension getLastBlock() {
		return this.last_pos;
	}
	
	public BlockPos getLastBlockPosPlus(BlockPos pos) {
		return new BlockPos(this.last_pos.getPos().getX() + pos.getX(), this.last_pos.getPos().getY() + pos.getY(), this.last_pos.getPos().getZ() + pos.getZ());
	}
	
	private BlockPos getSpawnPos() {
		return spawn_pos.getPos();
	}

	public void updatePocketSpawnPos(BlockPos pos) {
		this.spawn_pos.setPos(pos);

		PocketRegistryManager.saveData();
	}

	public void setSpawnInPocket(BlockPos posIn, float yawIn, float pitchIn) {
		this.spawn_pos = new ObjectDestinationInfo(posIn, yawIn, pitchIn);
	}
	
	public CosmosChunkPos getChunkPos() {
		return this.chunk_pos;
	}
	
	public boolean updateOwner(@Nullable ServerPlayer oldPlayerIn, ServerPlayer newPlayerIn) {
		if (oldPlayerIn != null) {
			ObjectPlayerInformation oldInfo = new ObjectPlayerInformation(oldPlayerIn);

			if (this.owner.equals(oldInfo)) {
				CosmosChatUtil.sendPlayerMessageServer(oldPlayerIn, ComponentHelper.locComp(ComponentColour.GREEN, false, "dimensionalpocketsii.command.transfer.success.old").append(ComponentHelper.locComp(ComponentColour.CYAN, false, newPlayerIn.getDisplayName().getString())));
				CosmosChatUtil.sendPlayerMessageServer(newPlayerIn, ComponentHelper.locComp(ComponentColour.CYAN, false, oldPlayerIn.getDisplayName().getString()).append(ComponentHelper.locComp(ComponentColour.GREEN, false, "dimensionalpocketsii.command.transfer.success.new")));
				
				this.setOwnerInternal(newPlayerIn);
				
				return true;
			} else {
				CosmosChatUtil.sendPlayerMessageServer(oldPlayerIn, ComponentHelper.locComp(ComponentColour.LIGHT_RED, false, "dimensionalpocketsii.command.transfer.error.not_owner"));
				return false;
			}
		} else {
			CosmosChatUtil.sendPlayerMessageServer(newPlayerIn, ComponentHelper.locComp(ComponentColour.CYAN, false, this.getOwnerName()).append(ComponentHelper.locComp(ComponentColour.GREEN, false, "dimensionalpocketsii.command.transfer.success.new")));
			
			this.setOwnerInternal(newPlayerIn);
			
			return true;
		}
	}
	
	private void setOwnerInternal(Player playerIn) {
		this.owner = new ObjectPlayerInformation(playerIn);
		this.addAllowedPlayer(playerIn);
	}

	public void setOwner(Player playerIn) {
		if (playerIn != null) {
			if (!this.getGeneratedStateValue() || this.owner == null) {
				this.owner = new ObjectPlayerInformation(playerIn);
				this.addAllowedPlayer(playerIn);
			}
		}
	}
	
	public boolean checkIfOwner(Player playerIn) {
		String player_name = playerIn.getDisplayName().getString();
		UUID player_uuid = playerIn.getUUID();
		
		if (this.owner != null) {
			if (this.owner.getPlayerUUID().equals(player_uuid)) {
				if (!this.owner.getPlayerName().equals(player_name)) {
					this.owner.setPlayerName(player_name);
				}
				return true;
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
	
	private UUID getOwnerUUID() {
		return this.owner.getPlayerUUID();
	}
	
	/** - Block Array Things - */
	
	public void addPosToBlockArray(BlockPos posIn, ResourceLocation dimensionIn) {
		if (this.doesBlockArrayContain(posIn, dimensionIn)) {
			DimensionalPockets.CONSOLE.debug("Pocket: [" + this.chunk_pos + "] Already contains that BlockPos. No update to the Map.");
		} else {
			this.block_array.put(block_array.size(), new ObjectBlockPosDimension(posIn, dimensionIn));
		}
		
		this.last_pos = new ObjectBlockPosDimension(posIn, dimensionIn);
	}

	public void addPosToBlockArray(ObjectBlockPosDimension objectIn) {
		if (this.doesBlockArrayContain(objectIn.getPos(), objectIn.getDimension())) {
			DimensionalPockets.CONSOLE.debug("Pocket: [" + this.chunk_pos + "] Already contains that BlockPos. No update to the Map.");
		} else {
			this.block_array.put(block_array.size(), objectIn);
		}
		
		this.last_pos = objectIn;
	}
	
	public boolean doesBlockArrayContain(BlockPos pos, ResourceLocation dimensionIn) {
		for (int i = 0; i < this.block_array.size(); i++) {
			BlockPos checkPos = this.block_array.get(i).getPos();
			ResourceLocation checkLocation = this.block_array.get(i).getDimension();
			
			if (checkLocation.getNamespace().equals("") || checkLocation.getPath().equals("")) {
				if (checkPos.equals(pos)) {
					return true;
				}
			} else {
				if (checkPos.equals(pos) && checkLocation.equals(dimensionIn)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	/** - Allowed Player Things - */
	public ArrayList<String> getAllowedPlayersArray() {
		return this.allowed_players_array;
	}

	public void addAllowedPlayer(Player playerIn) {
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
	
	public void removeAllowedPlayer(Player playerIn) {
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
	
	public boolean checkIfAllowedPlayer(Player playerIn) {
		String player_name = playerIn.getDisplayName().getString();
		
		for (int i = 0; i < this.allowed_players_array.size(); i++) {
			String test_player = this.allowed_players_array.get(i);
			
			if (test_player.equals(player_name)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checkIfPlayerCanShift(Player playerIn, EnumShiftDirection direction) {
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
	
	public boolean canPlayerLeave(Player playerIn) {
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
	public void setMaxIO(int maxTransfer) {
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
	
	public boolean hasEnergyStored() {
		return this.energy_stored > 0;
	}
	
	public int getEnergyStoredScaled(int scale) {
		return ((this.getEnergyStored() / 100) * scale) / (this.getMaxEnergyStored() / 100);
	}
	
	public boolean canExtractEnergy() {
		return this.hasEnergyStored();
	}
	
	public boolean canReceiveEnergy() {
		return this.getEnergyStored() < this.getMaxEnergyStored();
	}

	/** - FluidHandler Start */
	
	public int getTanks() {
		return 1;
	}
	
	public FluidStack getFluidInTank() {
		return this.getFluidTank().getFluid();
	}
	
	public int getFluidLevelScaled(int one) {
		float scaled = this.getCurrentFluidAmount() * one / this.getFluidTankCapacity() + 1;
		
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
	
	/** - Side State Things - */
	
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
	
	@Nonnull
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		}

		ItemStack stackInSlot = this.getItem(slot);

		int m;
		if (!stackInSlot.isEmpty()) {
			if (stackInSlot.getCount() >= Math.min(stackInSlot.getMaxStackSize(), 64)) {
				return stack;
			}

			if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot)) {
				return stack;
			}
				
			if (!this.canPlaceItem(slot, stack)) {
				return stack;
			}

			m = Math.min(stack.getMaxStackSize(), 64) - stackInSlot.getCount();

			if (stack.getCount() <= m) {
				if (!simulate) {
					ItemStack copy = stack.copy();
					copy.grow(stackInSlot.getCount());
					this.setItem(slot, copy);
					//getInv().setChanged();
				}

				return ItemStack.EMPTY;
			} else {
				// copy the stack to not modify the original one
				stack = stack.copy();
				if (!simulate) {
					ItemStack copy = stack.split(m);
					copy.grow(stackInSlot.getCount());
					this.setItem(slot, copy);
					//getInv().setChanged();
					return stack;
				} else {
					stack.shrink(m);
					return stack;
				}
			}
		} else {
			if (!this.canPlaceItem(slot, stack))
				return stack;

			m = Math.min(stack.getMaxStackSize(), 64);
			if (m < stack.getCount()) {
				// copy the stack to not modify the original one
				
				stack = stack.copy();
				if (!simulate) {
					this.setItem(slot, stack.split(m));
					//getInv().setChanged();
					return stack;
				} else {
					stack.shrink(m);
					return stack;
				}
			} else {
				if (!simulate) {
					this.setItem(slot, stack);
					//getInv().setChanged();
				}
				return ItemStack.EMPTY;
			}
		}
	}

	@Nonnull
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (amount == 0) {
			return ItemStack.EMPTY;
		}

		ItemStack stackInSlot = this.getItem(slot);

		if (stackInSlot.isEmpty()) {
			return ItemStack.EMPTY;
		}

		if (simulate) {
			if (stackInSlot.getCount() < amount) {
				return stackInSlot.copy();
			} else {
				ItemStack copy = stackInSlot.copy();
				copy.setCount(amount);
				return copy;
			}
		} else {
			int m = Math.min(stackInSlot.getCount(), amount);

			ItemStack decrStackSize = this.removeItem(slot, m);
			//getInv().setChanged();
			return decrStackSize;
		}
	}
	
	public void updateWallBlocks(Player playerIn, Level worldIn) {
		if (!worldIn.isClientSide) {
			if (playerIn != null) {
				if (playerIn.level.dimension().equals(DimensionManager.POCKET_WORLD)) {
					Level world = PocketRegistryManager.getLevelForPockets();
					
					int worldX = CosmosChunkPos.scaleFromChunkPos(this.chunk_pos).getX();
					int worldZ = CosmosChunkPos.scaleFromChunkPos(this.chunk_pos).getZ();
			
					int y_offset = PocketRegistryManager.getPocketYOffset();
					
					for (int x = 0; x < 1; x++) {
						for (int z = 2; z < 3; z ++) {
							for (int y = y_offset + 2; y < this.internal_height; y += 8) {
								BlockPos pos = new BlockPos(worldX + x, y, worldZ + z);
								
								world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
								world.setBlockAndUpdate(pos, ModBusManager.BLOCK_WALL.defaultBlockState());
							}
						}
					}
					PocketRegistryManager.saveData();
				}
			}
		}
	}
	
	public void updateBaseConnector(Level worldIn) {
		if (!worldIn.isClientSide) {
			Level world = PocketRegistryManager.getLevelForPockets();
			BlockPos pos = CosmosChunkPos.convertFrom(this.chunk_pos);
			BlockPos offset = pos.offset(Direction.UP.getNormal());
			
			BlockState state = world.getBlockState(offset);
			
			if (world.getBlockEntity(offset) instanceof BlockEntityModuleConnector) {
				BlockEntityModuleConnector connector = (BlockEntityModuleConnector) world.getBlockEntity(offset);
				
				connector.sendUpdates(false);
				world.sendBlockUpdated(offset, state, state, 3);	
			}
		}
	}
	
	public void generatePocket(Player playerIn) {
		if (!this.getGeneratedStateValue() || this.getInternalHeight() != ConfigurationManager.getInstance().getInternalHeight()) {
			Level world = PocketRegistryManager.getLevelForPockets();
			LevelChunk chunk = world.getChunk(this.getChunkPos().getX(), this.getChunkPos().getZ());
			
			BlockPos worldPos = new BlockPos(CosmosChunkPos.scaleFromChunkPos(this.getChunkPos()).getX(), 1, CosmosChunkPos.scaleFromChunkPos(this.getChunkPos()).getZ());
			
			int height = ConfigurationManager.getInstance().getInternalHeight();
			
			if (height != this.getInternalHeight()) {
				
				if (this.getInternalHeight() > height) {
					if (ConfigurationManager.getInstance().getInternalReplace()) {
						int[] edge = new int[] { 0, 14 };
						
						for (int x = edge[0]; x < edge[1]; x++) {
							for (int z = edge[0]; z < edge[1]; z++) {
								BlockPos pos = new BlockPos(x, this.internal_height, z);
								
								if (chunk.getBlockState(pos).getBlock().equals(ModBusManager.BLOCK_WALL)) {
									chunk.setBlockState(pos, ModBusManager.BLOCK_WALL_EDGE.defaultBlockState(), false);
								}
							}
						}
						
						this.generateStructureAndUpdate(world, worldPos, chunk);
						this.setInternalHeight(height);
						
						for (int x = 0; x < PocketRegistryManager.getPocketSize(); x++) {
							for (int z = 0; z < PocketRegistryManager.getPocketSize(); z++) {
								BlockPos updatePos = new BlockPos(x, height - 1, z);
								
								if (world.getBlockState(updatePos).getBlock() instanceof BlockWallEdge) {
									world.setBlockAndUpdate(updatePos, world.getBlockState(updatePos).updateShape(Direction.UP, world.getBlockState(updatePos), world, updatePos, updatePos.offset(Direction.UP.getNormal())));
								}
							}
							
						}
					} else {
						DimensionalPockets.CONSOLE.info("Pocket internal height is larger than the Config Value. Pocket will not reduce in size.");
					}
				} else {
					int[] upd = new int[] { 0, 1, 13, 14 };
					int[] rep = new int[] { 1, 14 };
					
					for (int x = rep[0]; x < rep[1]; x++) {
						for (int z = rep[0]; z < rep[1]; z++) {
							BlockPos pos = new BlockPos(x, this.getInternalHeight(), z);
							
							chunk.setBlockState(pos, Blocks.AIR.defaultBlockState(), false);
						}
					}
					
					this.generateStructureAndUpdate(world, worldPos, chunk);
					
					BlockPos upd1 = MathHelper.addBlockPos(worldPos, new BlockPos(upd[0], this.getInternalHeight() - 1, upd[1]));
					BlockPos upd2 = MathHelper.addBlockPos(worldPos, new BlockPos(upd[1], this.getInternalHeight() - 1, upd[0]));
					BlockPos upd3 = MathHelper.addBlockPos(worldPos, new BlockPos(upd[2], this.getInternalHeight() - 1, upd[3]));
					BlockPos upd4 = MathHelper.addBlockPos(worldPos, new BlockPos(upd[3], this.getInternalHeight() - 1, upd[2]));

					BlockPos upd5 = MathHelper.addBlockPos(worldPos, new BlockPos(upd[0], this.getInternalHeight() - 1, upd[2]));
					BlockPos upd6 = MathHelper.addBlockPos(worldPos, new BlockPos(upd[1], this.getInternalHeight() - 1, upd[3]));
					BlockPos upd7 = MathHelper.addBlockPos(worldPos, new BlockPos(upd[2], this.getInternalHeight() - 1, upd[0]));
					BlockPos upd8 = MathHelper.addBlockPos(worldPos, new BlockPos(upd[3], this.getInternalHeight() - 1, upd[1]));
					
					world.setBlockAndUpdate(upd1, ModBusManager.BLOCK_WALL_EDGE.defaultBlockState().updateShape(Direction.UP, world.getBlockState(upd1), world, upd1, upd1.offset(Direction.UP.getNormal())));
					world.setBlockAndUpdate(upd2, ModBusManager.BLOCK_WALL_EDGE.defaultBlockState().updateShape(Direction.UP, world.getBlockState(upd2), world, upd2, upd2.offset(Direction.UP.getNormal())));
					world.setBlockAndUpdate(upd3, ModBusManager.BLOCK_WALL_EDGE.defaultBlockState().updateShape(Direction.UP, world.getBlockState(upd3), world, upd3, upd3.offset(Direction.UP.getNormal())));
					world.setBlockAndUpdate(upd4, ModBusManager.BLOCK_WALL_EDGE.defaultBlockState().updateShape(Direction.UP, world.getBlockState(upd4), world, upd4, upd4.offset(Direction.UP.getNormal())));

					world.setBlockAndUpdate(upd5, ModBusManager.BLOCK_WALL_EDGE.defaultBlockState().updateShape(Direction.UP, world.getBlockState(upd5), world, upd5, upd5.offset(Direction.UP.getNormal())));
					world.setBlockAndUpdate(upd6, ModBusManager.BLOCK_WALL_EDGE.defaultBlockState().updateShape(Direction.UP, world.getBlockState(upd6), world, upd6, upd6.offset(Direction.UP.getNormal())));
					world.setBlockAndUpdate(upd7, ModBusManager.BLOCK_WALL_EDGE.defaultBlockState().updateShape(Direction.UP, world.getBlockState(upd7), world, upd7, upd7.offset(Direction.UP.getNormal())));
					world.setBlockAndUpdate(upd8, ModBusManager.BLOCK_WALL_EDGE.defaultBlockState().updateShape(Direction.UP, world.getBlockState(upd8), world, upd8, upd8.offset(Direction.UP.getNormal())));
					
					this.setInternalHeight(height);
				}
				
				chunk.markUnsaved();
			} else {
				this.generateStructureAndUpdate(world, worldPos, chunk);
			}

			Block check_block_one = world.getBlockState(MathHelper.addBlockPos(worldPos, new BlockPos(1, 0, 1))).getBlock();
			Block check_block_two = world.getBlockState(MathHelper.addBlockPos(worldPos, new BlockPos(2, 0, 2))).getBlock();
			Block check_block_three = world.getBlockState(worldPos).getBlock();
			Block check_block_four = world.getBlockState(MathHelper.addBlockPos(worldPos, new BlockPos(0, this.getInternalHeight() - 1, 0))).getBlock();
			
			//DimensionalPockets.CONSOLE.message(LEVEL.DEBUG, check_block_one + " || " + check_block_two + " || " + check_block_three + " || " + check_block_four);
			
			this.setOwner(playerIn);
			this.setGeneratedState(check_block_one instanceof BlockWallEdge && check_block_two instanceof BlockWallBase && check_block_three.equals(ModBusManager.BLOCK_WALL_CONNECTOR) && check_block_four.equals(Blocks.BEDROCK));
			
			PocketRegistryManager.saveData();
		}
	}
	
	//Code that actually generates the structure.
	public void generateStructureAndUpdate(Level worldIn, BlockPos worldPos, LevelChunk chunkIn) {
		int height = ConfigurationManager.getInstance().getInternalHeight();
		int size = PocketRegistryManager.getPocketSize();
		int y_offset = PocketRegistryManager.getPocketYOffset();
		
		for (int x = 0; x < size; x++) {
			for (int y = y_offset; y < height + y_offset; y++) {
				for (int z = 0; z < size; z++) {
					boolean flagX = x == 0 || x == (size - 1);
					boolean flagY = y == y_offset || y == (height);
					boolean flagZ = z == 0 || z == (size - 1);
					
					BlockPos pos = new BlockPos(x, y, z);
					BlockPos world_pos = new BlockPos(worldPos.getX() + x, y, worldPos.getZ() + z);
					
					//Creates a Bedrock frame around the Pocket to prevent glitching outside
					if (!(chunkIn.getBlockState(pos).getBlock() instanceof BlockWallModule)) {
						if (x == 0 || y == 1 || z == 0) {
							chunkIn.setBlockState(pos, Blocks.BEDROCK.defaultBlockState(), false);
						} else if (x == (size - 1) || y == (height - (1 - y_offset)) || z == (size - 1)) {
							chunkIn.setBlockState(pos, Blocks.BEDROCK.defaultBlockState(), false);
						} 
					}
					
					if (x == 0 && y == 1 && z == 0) {
						worldIn.setBlockAndUpdate(world_pos, ModBusManager.BLOCK_WALL_CONNECTOR.defaultBlockState());
					}
					
					//Added those flags, so I could add these checks, almost halves the time.
					if (!(flagX || flagY || flagZ) || flagX && (flagY || flagZ) || flagY && flagZ) {
						continue;
					}
					
					//Creates the "edge" blocks first. Stylistic choice.
					if (x == 1 || y == (1 + y_offset) || z == 1) {
						chunkIn.setBlockState(pos, ModBusManager.BLOCK_WALL_EDGE.defaultBlockState().updateShape(Direction.UP, chunkIn.getBlockState(pos), worldIn, pos, pos.offset(Direction.UP.getNormal())), false);
					} else if (x == (size - 2) || y == (height - (2 - y_offset)) || z == (size - 2)) {
						chunkIn.setBlockState(pos, ModBusManager.BLOCK_WALL_EDGE.defaultBlockState().updateShape(Direction.UP, chunkIn.getBlockState(pos), worldIn, pos, pos.offset(Direction.UP.getNormal())), false);
					} else {
						if (!(chunkIn.getBlockState(pos).getBlock() instanceof BlockWallModule)) {
							chunkIn.setBlockState(pos, ModBusManager.BLOCK_WALL.defaultBlockState().updateShape(Direction.UP, chunkIn.getBlockState(pos), worldIn, pos, pos.offset(Direction.UP.getNormal())), false);
						}
					}

					BlockState worldState = worldIn.getBlockState(world_pos);
					
					worldIn.sendBlockUpdated(world_pos, worldState, worldState.getBlock().defaultBlockState(), 19);
					worldIn.markAndNotifyBlock(world_pos, chunkIn, worldState, worldState.getBlock().defaultBlockState(), 19, 0);
					worldState.updateNeighbourShapes(worldIn, world_pos, 19);
					worldState.updateIndirectNeighbourShapes(worldIn, world_pos, 19);
				}
			}
		}
	}
	
	public void shift(Player playerIn, EnumShiftDirection direction, @Nullable BlockPos pocket_pos, @Nullable ResourceKey<Level> dimensionIn, @Nullable ItemStack stack) {
		Level entity_world = playerIn.level;

		if (entity_world.isClientSide || !(playerIn instanceof ServerPlayer)) {
			return;
		}
		
		Level source_world = this.getSourceBlockLevel();
		ServerPlayer server_player = (ServerPlayer) playerIn;
		
		if (this.checkIfPlayerCanShift(playerIn, direction)) {
			if (direction.equals(EnumShiftDirection.ENTER)) {
				
				if (pocket_pos != null && dimensionIn != null) {
					this.addPosToBlockArray(pocket_pos, dimensionIn.location());
					//this.setSourceBlockDimension(entity_world.dimension());
				}

				BlockPos chunk = CosmosChunkPos.scaleFromChunkPos(this.chunk_pos);
				EnumSafeTeleport location = EnumSafeTeleport.getValidTeleportLocation(PocketRegistryManager.getLevelForPockets(), MathHelper.addBlockPos(chunk, this.getSpawnPos()));
				
				if (location != EnumSafeTeleport.UNKNOWN) {
					BlockPos test = location.toBlockPos();
					BlockPos spawn = this.getSpawnPos();
					BlockPos shiftPos = MathHelper.addBlockPos(chunk, spawn, test);
					
					Shifter shifter = Shifter.createTeleporter(DimensionManager.POCKET_WORLD, direction, shiftPos, this.spawn_pos.getYaw(), this.spawn_pos.getPitch(), false, true, false);
					
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
						EnumSafeTeleport location = EnumSafeTeleport.getValidTeleportLocation(source_world, this.getLastBlockPos());
						
						if (location != EnumSafeTeleport.UNKNOWN) {
							Shifter shifter = Shifter.createTeleporter(this.getSourceBlockDimension(), direction, MathHelper.addBlockPos(this.getLastBlockPos(), location.toBlockPos()), 0.0F, 0.0F, false, true, false); //player.rotationYaw, player.rotationPitch);
							
							if (stack != null) {
								CoreTriggers.triggerUseShifter(server_player, stack);
							}
							
							ShifterCore.shiftPlayerToDimension(server_player, shifter);
						} else {
							ShifterCore.sendPlayerToBedWithMessage(server_player, direction, "dimensionalpocketsii.pocket.status.blocked");
						}
					} else {
						CosmosChatUtil.sendServerPlayerMessage(server_player, ComponentHelper.locComp(ComponentColour.LIGHT_RED, false, "dimensionalpocketsii.pocket.status.trapped"));
					}
				} else {
					ShifterCore.sendPlayerToBedWithMessage(server_player, direction, "dimensionalpocketsii.pocket.status.broken");
				}
			} else {
				ShifterCore.sendPlayerToBedWithMessage(server_player, direction, "dimensionalpocketsii.pocket.status.direction_unknown");
			}
		} else {
			CosmosChatUtil.sendServerPlayerMessage(server_player, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.locked"));
		}
	}
	
	private CompoundTag getNBT() {
		CompoundTag compound_nbt = new CompoundTag();
		
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
		compound_nbt.putInt(NBT_INTERNAL_HEIGHT_KEY, this.getInternalHeight());
		
		//CosmosNBTHelper.writeDimensionToNBT(this.getSourceBlockDimension(), compound_nbt);
		compound_nbt.putInt(NBT_ENERGY_STORED_KEY, this.getEnergyStored());
		compound_nbt.putInt(NBT_ENERGY_CAPACITY_KEY, this.getMaxEnergyStored());
		compound_nbt.putInt(NBT_ENERGY_RECEIVE_KEY, this.getMaxReceive());
		compound_nbt.putInt(NBT_ENERGY_EXTRACT_KEY, this.getMaxExtract());
		
		//CosmosChunkPos
		if (this.chunk_pos != null) {
			CompoundTag chunk = new CompoundTag();
			
			chunk.putInt(CosmosNBTHelper.Const.NBT_POS_X_KEY, this.getChunkPos().getX());
			chunk.putInt(CosmosNBTHelper.Const.NBT_POS_Z_KEY, this.getChunkPos().getZ());
			
			compound_nbt.put(NBT_CHUNK_POS_KEY, chunk);
		}
		
		//Block Dimension
		CompoundTag blockDimension = new CompoundTag();
		blockDimension.putString(Const.NBT_NAMESPACE_KEY, this.block_dimension.getNamespace());
		blockDimension.putString(Const.NBT_PATH_KEY, this.block_dimension.getPath());
		compound_nbt.put(NBT_BLOCK_DIMENSION_KEY, blockDimension);

		//LastPos
		CompoundTag last = new CompoundTag();
		this.last_pos.save(last);
		compound_nbt.put(NBT_LAST_POS_KEY, last);
		
		//SpawnPos
		if (this.getSpawnPos() != null) {
			CompoundTag spawn = new CompoundTag();
			
			this.spawn_pos.writeToNBT(spawn);
			
			compound_nbt.put(NBT_SPAWN_POS_KEY, spawn);
		}
		
		//FluidTank
		CompoundTag tank = new CompoundTag();
		this.fluid_tank.writeToNBT(tank);
		compound_nbt.put(NBT_FLUID_TANK_KEY, tank);
		
		//Player Map
		CompoundTag players = new CompoundTag();
		for (int i = 0; i < this.allowed_players_array.size(); i++){
			String player = this.allowed_players_array.get(i);
			
			players.putString(Integer.toString(i), player);
		}
		players.putInt("length", this.allowed_players_array.size());
		compound_nbt.put(NBT_ALLOWED_PLAYERS_KEY, players);
		
		//Side Map
		CompoundTag sideMapTag = new CompoundTag();
		if (this.block_array.size() > 0) {
			for (int i = 0; i < this.block_array.size(); i++) {
				ObjectBlockPosDimension test = this.block_array.get(i);
				
				test.save(sideMapTag);
			}
		}
		sideMapTag.putInt("length", this.block_array.size());
		compound_nbt.put(NBT_BLOCK_ARRAY_KEY, sideMapTag);
		
		//Items
		CompoundTag item_list = new CompoundTag();
		for (int i = 0; i < this.item_array.size(); i++) {
			ItemStack stack = this.item_array.get(i);
			
			item_list.put(Integer.toString(i), stack.serializeNBT());
		}
		compound_nbt.put(NBT_ITEMS_KEY, item_list);
		
		//Pocket Side Array
		CompoundTag side_array = new CompoundTag();
		for (int i = 0; i < this.pocket_side_array.length; i++) {
			Integer value = this.pocket_side_array[i].getIndex();
			
			side_array.putInt(Integer.toString(i), value);
		}
		side_array.putInt("length", this.pocket_side_array.length);
		compound_nbt.put(NBT_POCKET_SIDE_KEY, side_array);
		
		return compound_nbt;
	}

	public void writeToNBT(CompoundTag tag) {
		tag.put(NBT_DIMENSIONAL_POCKET_KEY, this.getNBT());
	}
	
	public static Pocket readFromNBT(CompoundTag compound) {
		return readFromNBT(compound, NBT_DIMENSIONAL_POCKET_KEY);
	}

	public static Pocket readFromNBT(CompoundTag compound_nbt, String key) {
		CompoundTag pocketTag = compound_nbt.getCompound(key);
		Pocket pocket = new Pocket(new CosmosChunkPos(0, 0));
		
		//BaseInfo
		pocket.owner = ObjectPlayerInformation.readFromNBT(pocketTag, NBT_OWNER_KEY);
		
		pocket.setGeneratedState(pocketTag.getBoolean(NBT_GENERATED_KEY));
		pocket.setLockState(pocketTag.getBoolean(NBT_LOCKED_KEY));
		pocket.setAllowedPlayerState(pocketTag.getBoolean(NBT_ALLOWED_PLAYER_SHIFT_KEY));
		pocket.setTrapState(pocketTag.getBoolean(NBT_TRAP_KEY));
		pocket.setHostileSpawnState(pocketTag.getBoolean(NBT_HOSTILE_SPAWNS_KEY));
		pocket.display_colour = pocketTag.getInt(NBT_COLOUR_KEY);
		pocket.setInternalHeight(pocketTag.getInt(NBT_INTERNAL_HEIGHT_KEY));
		//pocket.setSourceBlockDimension(ResourceKey.create(Registry.DIMENSION_REGISTRY, CosmosNBTHelper.readDimensionFromNBT(pocketTag)));
		pocket.setEnergyStored(pocketTag.getInt(NBT_ENERGY_STORED_KEY));
		pocket.energy_capacity = pocketTag.getInt(NBT_ENERGY_CAPACITY_KEY);
		pocket.energy_max_receive = pocketTag.getInt(NBT_ENERGY_RECEIVE_KEY);
		pocket.energy_max_extract = pocketTag.getInt(NBT_ENERGY_EXTRACT_KEY);
		
		//CosmosChunkPos
		CompoundTag chunkTag = pocketTag.getCompound(NBT_CHUNK_POS_KEY);
		pocket.chunk_pos = new CosmosChunkPos(chunkTag.getInt(CosmosNBTHelper.Const.NBT_POS_X_KEY), chunkTag.getInt(CosmosNBTHelper.Const.NBT_POS_Z_KEY));
		
		//BlockDimension
		CompoundTag blockDimension = pocketTag.getCompound(NBT_BLOCK_DIMENSION_KEY);
		pocket.block_dimension = new ResourceLocation(blockDimension.getString(Const.NBT_NAMESPACE_KEY), blockDimension.getString(Const.NBT_PATH_KEY));
		
		//LastPos
		CompoundTag lastTag = pocketTag.getCompound(NBT_LAST_POS_KEY);
		pocket.last_pos = ObjectBlockPosDimension.load(lastTag);
		
		//SpawnPos
		CompoundTag spawnTag = pocketTag.getCompound(NBT_SPAWN_POS_KEY);
		pocket.spawn_pos = ObjectDestinationInfo.readFromNBT(spawnTag);
		
		//FluidTank
		CompoundTag tankTag = pocketTag.getCompound(NBT_FLUID_TANK_KEY);
		pocket.fluid_tank = ObjectFluidTankCustom.readFromNBT(tankTag);
		
		//Player Map
		CompoundTag playersTag = pocketTag.getCompound(NBT_ALLOWED_PLAYERS_KEY);
		for (int i = 0; i < playersTag.getInt("length"); i++) {
			String player_name = playersTag.getString(Integer.toString(i));
			
			pocket.addAllowedPlayerNBT(player_name);
		}
		
		//Side Map
		CompoundTag sideMapTag = pocketTag.getCompound(NBT_BLOCK_ARRAY_KEY);
		for (int i = 0; i < sideMapTag.getInt("length"); i++) {
			CompoundTag sideTag = sideMapTag.getCompound(Integer.toString(i));
			
			pocket.addPosToBlockArray(ObjectBlockPosDimension.load(sideTag));
		}
		
		//Items
		CompoundTag itemsTag = pocketTag.getCompound(NBT_ITEMS_KEY);
		for (int i = 0; i < DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE; i++) {
			CompoundTag item = itemsTag.getCompound(Integer.toString(i));
			
			pocket.item_array.set(i, ItemStack.of(item));
		}
		
		//Pocket Side Array
		CompoundTag sideArrayTag = pocketTag.getCompound(NBT_POCKET_SIDE_KEY);
		for (int i = 0; i < sideArrayTag.getInt("length"); i++) {
			int side = sideArrayTag.getInt(Integer.toString(i));
			
			pocket.pocket_side_array[i] = EnumSideState.getStateFromIndex(side);
		}
		
		return pocket;
	}
	
	public ItemStack generateItemStackWithNBT() {
		ItemStack item_stack = new ItemStack(ModBusManager.BLOCK_POCKET);

		if (!item_stack.hasTag()) {
			item_stack.setTag(new CompoundTag());
		}

		CompoundTag compound = new CompoundTag();
		CompoundTag chunk_tag = new CompoundTag();
		
		int x = this.chunk_pos.getX();
		int z = this.chunk_pos.getZ();
		
		chunk_tag.putInt("X", x);
		chunk_tag.putInt("Z", z);

		compound.put("chunk_set", chunk_tag);
		
		compound.putInt("colour", this.getDisplayColour());
		
		item_stack.getTag().put("nbt_data", compound);
		
		return item_stack;
	}

	@Override
	public boolean canPlaceItem(int p_94041_1_, ItemStack p_94041_2_) {
		return true;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.item_array) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getContainerSize() {
		return this.item_array.size();
	}

	@Override
	public ItemStack getItem(int index) {
		return this.item_array.get(index);
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		return ContainerHelper.removeItem(this.item_array, index, count);
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		return ContainerHelper.takeItem(this.item_array, index);
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		this.item_array.set(index, stack);
		
		if (stack.getCount() > this.getContainerSize()) {
			stack.setCount(this.getContainerSize());
		}
	}

	@Override
	public void setChanged() {
		//PocketRegistryManager.saveData();
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return true;
	}

	@Override
	public void clearContent() {
		
	}
}