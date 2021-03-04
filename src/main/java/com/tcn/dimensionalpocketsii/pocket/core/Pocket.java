package com.tcn.dimensionalpocketsii.pocket.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.tcn.cosmoslibrary.impl.colour.EnumMinecraftColour;
import com.tcn.cosmoslibrary.impl.colour.ChatColour;
import com.tcn.cosmoslibrary.impl.nbt.UtilNBT;
import com.tcn.cosmoslibrary.impl.registry.object.ObjectFluidTankCustom;
import com.tcn.cosmoslibrary.impl.registry.object.ObjectPlayerInformation;
import com.tcn.cosmoslibrary.impl.registry.object.ObjectTeleportPos;
import com.tcn.cosmoslibrary.math.ChunkPos;
import com.tcn.dimensionalpocketsii.DimReference;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.advancement.CoreTriggers;
import com.tcn.dimensionalpocketsii.core.management.CoreConfigurationManager;
import com.tcn.dimensionalpocketsii.core.management.CoreDimensionManager;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockPocket;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWall;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallEdge;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftLocation;
import com.tcn.dimensionalpocketsii.pocket.core.shift.Shifter;
import com.tcn.dimensionalpocketsii.pocket.core.shift.ShifterCore;
import com.tcn.dimensionalpocketsii.pocket.core.util.PocketUtil;

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
import net.minecraft.util.text.StringTextComponent;
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
	private static final String NBT_TRAP_KEY = "trap_players";
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
	private static final String NBT_ITEMS_KEY = "items_array";
	
	@SerializedName(NBT_OWNER_KEY)
	private ObjectPlayerInformation owner;

	@SerializedName(NBT_GENERATED_KEY)
	private boolean is_generated = false;

	@SerializedName(NBT_LOCKED_KEY)
	private boolean is_locked = false;
	
	@SerializedName(NBT_TRAP_KEY)
	private boolean trap_players = false;
	
	@SerializedName(NBT_COLOUR_KEY)
	private int display_colour = EnumMinecraftColour.POCKET_PURPLE.getDecimal();
	
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
	private ArrayList<ObjectPlayerInformation> allowed_players_array = new ArrayList<ObjectPlayerInformation>();
	
	@SerializedName(NBT_BLOCK_ARRAY_KEY)
	private LinkedHashMap<Integer, BlockPos> block_array = new LinkedHashMap<>();

	@SerializedName(NBT_ITEMS_KEY)
	public NonNullList<ItemStack> item_array = NonNullList.<ItemStack>withSize(DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE, ItemStack.EMPTY);

	public Pocket() {}

	public Pocket(ChunkPos chunk_pos, RegistryKey<World> block_dim_type, BlockPos blockPos) {
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
	
	public BlockState getSourceBlockState() {
		World world = this.getSourceBlockWorld();
		if (world == null) {
			DimensionalPockets.LOGGER.warn("[FAIL] getSourceBlock() { Dimension with ID: " + this.block_dimension + " does not exist! (Mystcraft or GalactiCraft world?) } Pocket will return null...", Pocket.class);
			return null;
		}
		return world.getBlockState(this.getLastBlockPos());
	}
	
	public BlockPos getSourceBlockPos() {
		
		return this.getLastBlockPos();
	}
	
	public void setSourceBlockDimension(RegistryKey<World> type) {
		this.block_dimension = type.getLocation();
	}
	
	public RegistryKey<World> getSourceBlockDimension() {
		return RegistryKey.getOrCreateKey(Registry.WORLD_KEY, this.block_dimension);
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
	
	public int getDisplayColour() {
		return this.display_colour;
	}
	
	public void setDisplayColour(int colour) {
		this.display_colour = colour;
	}
	
	public void setDisplayColour(EnumMinecraftColour colour) {
		this.display_colour = colour.getDecimal();
	}

	public int getInternalHeight() {
		return this.internal_height;
	}
	
	public void setLockState(boolean change) {
		this.is_locked = change;
	}
	
	public BlockPos getLastBlockPos() {
		return this.last_pos;
	}
	
	public BlockPos getLastBlockPosPlus(BlockPos pos) {
		return new BlockPos(this.last_pos.add(pos));
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

	public void setOwner(PlayerEntity playerIn) {
		if (!this.is_generated || this.owner == null) {
			this.owner = new ObjectPlayerInformation(playerIn);
			this.addAllowedPlayer(playerIn);
		}
	}
	
	public boolean checkIfOwner(PlayerEntity playerIn) {
		String player_name = playerIn.getDisplayName().getString();
		UUID player_uuid = playerIn.getUniqueID();
		
		if (this.owner.getPlayerName().equals(player_name)) {
			if (this.owner.getPlayerUUID().equals(player_uuid)) {
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
	
	public ArrayList<ObjectPlayerInformation> getAllowedPlayersArray() {
		return this.allowed_players_array;
	}
	
	public void addAllowedPlayerNBT(String player_name, UUID player_uuid) {
		if (!this.checkIfAllowedPlayerNBT(player_name, player_uuid)) {
			ObjectPlayerInformation player = new ObjectPlayerInformation(player_name, player_uuid);
			this.allowed_players_array.add(player);
		}
	}
	
	private boolean checkIfAllowedPlayerNBT(String player_name, UUID player_uuid) {
		for (int i = 0; i < this.allowed_players_array.size(); i++) {
			ObjectPlayerInformation test_player = this.allowed_players_array.get(i);
			
			if (test_player.getPlayerName().equals(player_name) && test_player.getPlayerUUID().equals(player_uuid)) {
				return true;
			}
		}
		return false;
	}
	
	public void addAllowedPlayer(PlayerEntity playerIn) {
		if (!this.checkIfAllowedPlayer(playerIn)) {
			ObjectPlayerInformation player = new ObjectPlayerInformation(playerIn);
			this.allowed_players_array.add(player);
		}
	}
	
	public void removeAllowedPlayer(PlayerEntity playerIn) {
		String player_name = playerIn.getDisplayName().getString();
		UUID player_uuid = playerIn.getUniqueID();
		
		for (int i = 0; i < this.allowed_players_array.size(); i++) {
			ObjectPlayerInformation test_player = this.allowed_players_array.get(i);
			
			if (test_player.getPlayerName().equals(player_name) && test_player.getPlayerUUID().equals(player_uuid)) {
				this.allowed_players_array.remove(i);
			}
		}
	}
	
	/** - Check Player Things - */
	
	public boolean checkIfAllowedPlayer(PlayerEntity playerIn) {
		String player_name = playerIn.getDisplayName().getString();
		UUID player_uuid = playerIn.getUniqueID();
		
		for (int i = 0; i < this.allowed_players_array.size(); i++) {
			ObjectPlayerInformation test_player = this.allowed_players_array.get(i);
			
			if (test_player.getPlayerName().equals(player_name) && test_player.getPlayerUUID().equals(player_uuid)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checkIfPlayerCanShift(PlayerEntity playerIn, EnumShiftDirection direction) {
		if (this.checkIfOwner(playerIn)) {
			return true;
		} else {
			if (direction.equals(EnumShiftDirection.ENTER)) {
				if (this.is_locked) {
					return this.checkIfAllowedPlayer(playerIn);
				} else {
					return true;
				}
			} else {
				return true;
			}
		}
	}
	
	/** - Energy Methods - */
	
	public int getStored() {
		return this.energy_stored;
	}
	
	public void setStored(int set) {
		this.energy_stored = set;
	}
	
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
		return this.energy_capacity;
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
		
		if (!this.isFluidTankEmpty()) {
			return this.fluid_tank.getFluidTank().getFluid().getFluid();
		}
		return null;
	}
	
	public String getCurrentStoredFluidName() {
		if (this.isFluidTankEmpty()) {
			return "Empty";
		}
		return this.fluid_tank.getFluidTank().getFluid().getDisplayName().getString();
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
	
	/** - Pocket General Methods - */
	
	public void generatePocket(PlayerEntity playerIn) {
		if (!this.is_generated || this.internal_height != CoreConfigurationManager.getInstance().getInternalHeight()) {
			World world = PocketRegistryManager.getWorldForPockets();
			Chunk chunk = world.getChunk(this.chunk_pos.getX(), this.chunk_pos.getZ());
			
			int worldX = this.chunk_pos.getX() * 16;
			int worldY = 1;
			int worldZ = this.chunk_pos.getZ() * 16;
	
			int size = PocketRegistryManager.pocket_size;
			int height = CoreConfigurationManager.getInstance().getInternalHeight();
			int y_offset = PocketRegistryManager.pocket_y_offset;
			
			for (int x = 0; x < size; x++) {
				for (int y = y_offset; y < height + y_offset; y++) {
					for (int z = 0; z < size; z++) {
						boolean flagX = x == 0 || x == (size - 1);
						boolean flagY = y == y_offset || y == (height);
						boolean flagZ = z == 0 || z == (size - 1);
						
						chunk.rescheduleTicks();
						
						BlockPos pos = new BlockPos(x, y, z);
						
						if (x == 0 || y == 1 || z == 0) {
							chunk.setBlockState(pos, Blocks.BEDROCK.getDefaultState(), false);
						}
						
						if (x == (size - 1) || y == (height - (1 - y_offset)) || z == (size - 1)) {
							chunk.setBlockState(pos, Blocks.BEDROCK.getDefaultState(), false);
						} 
						
						//Added those flags, so I could add these checks, almost halves the time.
						if (!(flagX || flagY || flagZ) || flagX && (flagY || flagZ) || flagY && flagZ) {
							continue;
						}
						
						//Creates the "edge" blocks first. Stylistic choice.
						if (x == 1 || y == (1 + y_offset) || z == 1) {
							chunk.setBlockState(pos, ModBusManager.BLOCK_WALL_EDGE.getDefaultState().updatePostPlacement(Direction.UP, world.getBlockState(pos), world, pos, pos.offset(Direction.UP)), false);
						} else if (x == (size - 2) || y == (height - (2 - y_offset)) || z == (size - 2)) {
							chunk.setBlockState(pos, ModBusManager.BLOCK_WALL_EDGE.getDefaultState().updatePostPlacement(Direction.UP, world.getBlockState(pos), world, pos, pos.offset(Direction.UP)), false);
						} else {
							if (!(chunk.getBlockState(pos).getBlock().equals(ModBusManager.BLOCK_WALL_CONNECTOR) && !(chunk.getBlockState(pos).getBlock().equals(ModBusManager.BLOCK_WALL_CHARGER))) 
									//&& !(world.getBlockState(pos).getBlock().equals(ModBusManager.BLOCK_ENERGY_DISPLAY))
										//&& !(worlde.getBlockState(pos).getBlock().equals(ModBusManager.BLOCK_ENERGY_DISPLAY))
											) {
								chunk.setBlockState(pos, ModBusManager.BLOCK_WALL.getDefaultState(), false);
							}
						}

						BlockPos world_pos = new BlockPos(worldX + x, y, worldZ + z);
						
						world.notifyBlockUpdate(world_pos, world.getBlockState(world_pos), world.getBlockState(world_pos).getBlock().getDefaultState(), 3);
						world.markAndNotifyBlock(world_pos, chunk, world.getBlockState(world_pos), world.getBlockState(world_pos).getBlock().getDefaultState(), 3, 0);
						world.getBlockState(world_pos).updateDiagonalNeighbors(world, world_pos, 3);
						world.getBlockState(world_pos).updateNeighbours(world, world_pos, 3);
					}
				}
			}
			
			if (height != this.internal_height) {
				int[] rep = new int[] { 1, 14 };
				
				for (int x = rep[0]; x < rep[1]; x++) {
					for (int z = rep[0]; z < rep[1]; z++) {
						BlockPos pos = new BlockPos(x, this.internal_height, z);
						
						chunk.setBlockState(pos, Blocks.AIR.getDefaultState(), false);
					}
				}
			
				if (!(this.internal_height > CoreConfigurationManager.getInstance().getInternalHeight())) {
					if (CoreConfigurationManager.getInstance().getInternalReplace()) {
						int[] edge = new int[] { 0, 14 };
					
						for (int x = edge[0]; x < edge[1]; x++) {
							for (int z = edge[0]; z < edge[1]; z++) {
								BlockPos pos = new BlockPos(x, this.internal_height, z);
								
								if (chunk.getBlockState(pos).getBlock().equals(ModBusManager.BLOCK_WALL)) {
									chunk.setBlockState(pos, ModBusManager.BLOCK_WALL_EDGE.getDefaultState(), false);
								}
							}
						}
					}  else {
						DimensionalPockets.LOGGER.warn("Pocket internal height is larger than the config. Pocket will not reduce in size.", Pocket.class);
					}
				}
				
				chunk.setLoaded(true);
			}
			
			Block check_block_one = world.getBlockState(new BlockPos((worldX + 1), worldY, (worldZ + 1))).getBlock();
			Block check_block_two = world.getBlockState(new BlockPos((worldX + 2), worldY, (worldZ + 2))).getBlock();
			Block check_block_three = world.getBlockState(new BlockPos(worldX, worldY, worldZ)).getBlock();
			Block check_block_four = world.getBlockState(new BlockPos(worldX, (worldY + internal_height) - 1, worldZ)).getBlock();
			
			this.setOwner(playerIn);

			this.is_generated = check_block_one instanceof BlockWallEdge && check_block_two instanceof BlockWall && check_block_three.equals(Blocks.BEDROCK) && check_block_four.equals(Blocks.BEDROCK);
			this.internal_height = height;
			
			PocketRegistryManager.saveData();
		}
	}
	
	public void shift(PlayerEntity playerIn, EnumShiftDirection direction, @Nullable BlockPos pocket_pos, @Nullable ItemStack stack) {
		World entity_world = playerIn.getEntityWorld();
		
		if (entity_world.isRemote || !(playerIn instanceof ServerPlayerEntity)) {
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
					Shifter shifter = Shifter.createTeleporter(CoreDimensionManager.POCKET_WORLD, direction, PocketUtil.scaleFromChunkPos(this.chunk_pos).add(this.getSpawnPos()).add(test), this.spawn_pos.getYaw(), this.spawn_pos.getPitch());
					
					this.generatePocket(server_player);
					ShifterCore.shiftPlayerToDimension(server_player, shifter);
					
					if (stack != null) {
						CoreTriggers.triggerUseShifter(server_player, stack);
					}
				} else {
					ShifterCore.sendPlayerToBedWithMessage(server_player, direction, "Teleport Blocked!");
				}
			} else if (direction.equals(EnumShiftDirection.LEAVE)) {
				if (this.isSourceBlockPlaced()) {
					EnumShiftLocation location = EnumShiftLocation.getValidTeleportLocation(source_world, this.getLastBlockPos());
					
					if (location != EnumShiftLocation.UNKNOWN) {
						Shifter shifter = Shifter.createTeleporter(this.getSourceBlockDimension(), direction, this.getLastBlockPosPlus(location.toBlockPos()), 0.0F, 0.0F); //player.rotationYaw, player.rotationPitch);
						
						if (stack != null) {
							CoreTriggers.triggerUseShifter(server_player, stack);
						}
						
						ShifterCore.shiftPlayerToDimension(server_player, shifter);
					} else {
						ShifterCore.sendPlayerToBedWithMessage(server_player, direction, "Something is blocking your Pocket!");
					}
				} else {
					ShifterCore.sendPlayerToBedWithMessage(server_player, direction, "Someone broke your Pocket!");
				}
			} else {
				playerIn.sendMessage(new StringTextComponent(ChatColour.RED + "Direction cannot be determined. Please report this issue to the Mod Author."), UUID.randomUUID());
			}
		} else {
			playerIn.sendMessage(new StringTextComponent(ChatColour.BOLD + ChatColour.RED + "This Pocket has been locked by: " + ChatColour.ORANGE + this.getOwnerName() + ChatColour.RED + "." + ChatColour.END), UUID.randomUUID());
		}
	}
	
	public CompoundNBT getNBT() {
		CompoundNBT compound_nbt = new CompoundNBT();
		
		//Base Info
		if (this.owner != null) {
			this.getOwner().writeToNBT(compound_nbt, NBT_OWNER_KEY);
		}
		
		compound_nbt.putBoolean(NBT_GENERATED_KEY, this.isGenerated());
		compound_nbt.putBoolean(NBT_LOCKED_KEY, this.getLockState());
		compound_nbt.putInt(NBT_COLOUR_KEY, this.getDisplayColour());
		UtilNBT.writeDimensionToNBT(getSourceBlockDimension(), compound_nbt);
		compound_nbt.putInt(NBT_ENERGY_STORED_KEY, this.getStored());
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
			ObjectPlayerInformation player = this.allowed_players_array.get(i);
			
			CompoundNBT player_tag = new CompoundNBT();
			
			player.writeToNBT(player_tag);
			players.put(Integer.toString(i), player_tag);
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
		if (pocketTag.contains(NBT_OWNER_KEY)) {
			pocket.owner = ObjectPlayerInformation.readFromNBT(compound_nbt, NBT_OWNER_KEY);
		}
		
		pocket.is_generated = pocketTag.getBoolean(NBT_GENERATED_KEY);
		pocket.setLockState(pocketTag.getBoolean(NBT_LOCKED_KEY));
		pocket.setDisplayColour(pocketTag.getInt(NBT_COLOUR_KEY));
		pocket.setSourceBlockDimension(RegistryKey.getOrCreateKey(Registry.WORLD_KEY, UtilNBT.readDimensionFromNBT(compound_nbt)));
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
			CompoundNBT player = playersTag.getCompound(Integer.toString(i));
			
			ObjectPlayerInformation player_info = ObjectPlayerInformation.readFromNBT(player);
			String player_name = player_info.getPlayerName();
			UUID player_uuid = player_info.getPlayerUUID();
			
			pocket.addAllowedPlayerNBT(player_name, player_uuid);
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
			
			pocket.item_array.set(i, ItemStack.read(item));
		}
		
		return pocket;
	}
}