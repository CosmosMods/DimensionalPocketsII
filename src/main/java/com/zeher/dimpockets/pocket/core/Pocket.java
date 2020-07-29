package com.zeher.dimpockets.pocket.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import com.zeher.dimpockets.DimReference;
import com.zeher.dimpockets.DimensionalPockets;
import com.zeher.dimpockets.core.manager.BusSubscriberMod;
import com.zeher.dimpockets.core.manager.ModDimensionManager;
import com.zeher.dimpockets.core.util.TeleportDirection;
import com.zeher.dimpockets.pocket.core.block.BlockPocket;
import com.zeher.dimpockets.pocket.core.block.BlockWall;
import com.zeher.dimpockets.pocket.core.block.BlockWallEdge;
import com.zeher.dimpockets.pocket.core.dimshift.Shifter;
import com.zeher.dimpockets.pocket.core.dimshift.ShifterUtil;
import com.zeher.dimpockets.pocket.core.manager.PocketRegistryManager;
import com.zeher.zeherlib.api.client.util.TextHelper;
import com.zeher.zeherlib.api.compat.core.interfaces.EnumConnectionType;
import com.zeher.zeherlib.mod.util.ModUtil;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class Pocket {
	
	private static final String NBT_CONNECTOR_MAP_KEY = "connectorMap";
	private static final String NBT_SPAWN_COORDS_KEY = "spawnPos";
	private static final String NBT_SPAWN_COORDS_YAW_KEY = "spawnYaw";
	private static final String NBT_SPAWN_COORDS_PITCH_KEY = "spawnPitch";
	private static final String NBT_BLOCK_DIMENSION_KEY = "blockDim";
	private static final String NBT_BLOCK_DIMENSION_TYPE = "block_dim_type";
	private static final String NBT_CHUNK_COORDS_KEY = "chunkPos";
	private static final String NBT_BLOCK_MAP_KEY = "side_map";

	private static final String NBT_GENERATED_KEY = "generated";
	private static final String NBT_LAST_POS_KEY = "last_pos";
	private static final String NBT_CREATOR_KEY = "creator";
	private static final String NBT_ALLOWED_PLAYERS_KEY = "allowed_players";
	private static final String NBT_STORED_KEY = "stored";
	private static final String NBT_CAPACITY_KEY = "capacity";
	private static final String NBT_RECEIVE_KEY = "maxReceive";
	private static final String NBT_EXTRACT_KEY = "maxExtract";
	private static final String NBT_LOCKED_KEY = "locked";
	private static final String NBT_ITEMS_KEY = "Items";


	@SerializedName(NBT_CREATOR_KEY)
	private String creator;

	@SerializedName(NBT_GENERATED_KEY)
	private boolean isGenerated = false;

	@SerializedName(NBT_LOCKED_KEY)
	private boolean locked = false;
	
	@SerializedName(NBT_BLOCK_DIMENSION_KEY)
	private int blockDim = -99;
	
	@SerializedName(NBT_SPAWN_COORDS_YAW_KEY)
	private float spawnYaw;
	
	@SerializedName(NBT_SPAWN_COORDS_PITCH_KEY)
	private float spawnPitch;

	@SerializedName(NBT_STORED_KEY)
	private int stored = 0;
	
	@SerializedName(NBT_CAPACITY_KEY)
	public int capacity = DimReference.CONSTANT.POCKET_RF_CAP;
	
	@SerializedName(NBT_RECEIVE_KEY)
	private int max_receive = 25000;
	
	@SerializedName(NBT_EXTRACT_KEY)
	private int max_extract = 25000;

	@SerializedName("fill_level")
	private int fill_level = 0;

	@SerializedName(NBT_BLOCK_DIMENSION_TYPE)
	private DimensionType block_dim_type;

	@SerializedName(NBT_CHUNK_COORDS_KEY)
	private BlockPos chunkPos;

	@SerializedName(NBT_LAST_POS_KEY)
	private BlockPos last_pos;
	
	@SerializedName(NBT_SPAWN_COORDS_KEY)
	private BlockPos spawnPos;
	
	@SerializedName("fluid_tank")
	private FluidTank fluid_tank = new FluidTank(256000);

	@SerializedName(NBT_ALLOWED_PLAYERS_KEY)
	private ArrayList<String> playerMap = new ArrayList<String>();
	
	@SerializedName(NBT_CONNECTOR_MAP_KEY)
	private Map<BlockPos, EnumConnectionType> connectorMap = new HashMap<>();
	
	@SerializedName(NBT_BLOCK_MAP_KEY)
	private Map<Integer, BlockPos> blockMap = new HashMap<>();

	@SerializedName(NBT_ITEMS_KEY)
	public NonNullList<ItemStack> items = NonNullList.<ItemStack>withSize(DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE, ItemStack.EMPTY);

	public Pocket() {}

	public Pocket(BlockPos chunkPos, DimensionType block_dim_type, BlockPos blockPos) {
		this.setSourceBlockDimensionType(block_dim_type);
		this.addPosToBlockMap(blockPos);
		this.setSpawnInPocket(new BlockPos(7, 1, 7), 0f, 0f);
		this.chunkPos = chunkPos;
	}
	
	public boolean isSourceBlockPlaced() {
		return this.getSourceBlock() instanceof BlockPocket;
	}

	public Block getSourceBlock() {
		World world = this.getSourceBlockWorld();
		if (world == null) {
			DimensionalPockets.LOGGER.warn("[FAIL] getSourceBlock() { Dimension with ID: " + this.block_dim_type + " does not exist! (Mystcraft or GalactiCraft world?) } Pocket will return null...", Pocket.class);
			return null;
		}
		return world.getBlockState(this.getLastBlockPos()).getBlock();
	}

	public DimensionType getSourceBlockDimensionType() {
		return this.block_dim_type;
	}
	
	public int getBlockDim() {
		return this.blockDim;
	}

	public void setSourceBlockDimensionType(DimensionType type) {
		this.block_dim_type = type;
	}

	public void setCreator(String name) {
		if (!this.isGenerated) {
			this.creator = name;
			this.addToPlayerMap(name);
		}
	}
	
	public World getSourceBlockWorld() {
		return ServerLifecycleHooks.getCurrentServer().getWorld(block_dim_type);
	}
	
	public BlockPos getLastBlockPos() {
		return last_pos;
	}
	
	@SuppressWarnings("unused")
	private BlockPos getSpawnPos() {
		return spawnPos;
	}
	
	public BlockPos getChunkPos() {
		return chunkPos;
	}

	public String getCreator() {
		return creator;
	}
	
	public void addPosToBlockMap(BlockPos pos) {
		if (blockMap.containsValue(pos)) {
			DimensionalPockets.LOGGER.info("Pocket: [" + this.chunkPos + "] Already contains that BlockPos. No update to the Map.");
		} else {
			blockMap.put(blockMap.size(), pos);
		}
		
		this.last_pos = pos;
	}
	
	public boolean doesBlockMapContain(BlockPos pos) {
		return blockMap.containsValue(pos);
	}
	
	public void updatePocketSpawnPos(BlockPos pos) {
		this.spawnPos = pos;
		
		PocketRegistryManager.saveData();
	}
	
	public void setSpawnInPocket(BlockPos spawnPos, float spawnYaw, float spawnPitch) {
		this.spawnPos = spawnPos;
		
		this.spawnYaw = spawnYaw;
		this.spawnPitch = spawnPitch;
	}
	
	/** TODO - AllowedPlayer Methods*/
	
	public ArrayList<String> getPlayerMap() {
		return this.playerMap;
	}
	
	public void addToPlayerMap(String player_name) {
		if (!checkPlayerMap(player_name)) {
			if (!player_name.isEmpty()) {
				this.playerMap.add(player_name);
			}
		}
	}
	
	public void removeFromPlayerMap(String player_name) {
		for (int i = 0; i < this.playerMap.size(); i++) {
			String test = this.playerMap.get(i);
			
			if (test.equals(player_name)) {
				this.playerMap.remove(i);
			}
		}
	}
	
	public boolean checkPlayerMap(String player_name) {
		for (int i = 0; i < this.playerMap.size(); i++) {
			String test = this.playerMap.get(i);
			
			if (test.equals(player_name)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isGenerated() {
		return this.isGenerated;
	}
	

	public Map<BlockPos, EnumConnectionType> getConnectorMap() {
		return this.connectorMap;
	}
	
	/** TODO - Connector Stuff -  */
	public void updateInConnectorMap(BlockPos put_pos, EnumConnectionType enum_state) {
		if (connectorMap.containsKey(put_pos)) {
			DimensionalPockets.LOGGER.info("Connector already exists in Map. Updating...", Pocket.class);
			connectorMap.replace(put_pos, enum_state);
		} else {
			DimensionalPockets.LOGGER.info("Connector added to Map.", Pocket.class);
			connectorMap.put(put_pos, enum_state);
		}
		
		PocketRegistryManager.saveData();
	}
	
	public void removeFromConnectorMap(BlockPos pos) {
		DimensionalPockets.LOGGER.info("Connector removed from Map.", Pocket.class);
		connectorMap.remove(pos);
		
		PocketRegistryManager.saveData();
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
	
	public void setEnergyStored(int stored) {

		this.stored = stored;

		if (this.stored > 1000000) {
			this.stored = 1000000;
		} else if (this.stored < 0) {
			this.stored = 0;
		}
	}
	
	public void modifyEnergyStored(int stored) {

		this.stored += stored;

		if (this.stored > 1000000) {
			this.stored = 1000000;
		} else if (this.stored < 0) {
			this.stored = 0;
		}
	}
	
	public int receiveEnergy(int max_receive, boolean simulate) {
		int storedReceived = Math.min(this.getMaxEnergyStored() - stored, Math.min(this.max_receive, max_receive));

		if (!simulate) {
			this.stored += storedReceived;
		}
		
		return storedReceived;
	}

	public int extractEnergy(int max_extract, boolean simulate) {
		int storedExtracted = Math.min(stored, Math.min(this.max_extract, max_extract));

		if (!simulate) {
			this.stored -= storedExtracted;
		}
		
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
	
	public boolean getLockState() {
		return this.locked;
	}
	
	public void setLockState(boolean change) {
		this.locked = change;
	}
	
	/** - FluidHandler Start */
/** TODO - Fluid Methods - */
	
	public int getFluidLevelScaled(int one) {
		return this.fluid_tank.getFluidAmount() * one / this.fluid_tank.getCapacity();
	}

	public int getCurrentFluidAmount() {
		return this.fluid_tank.getFluidAmount();
	}
	
	public Fluid getCurrentStoredFluid() {
		this.updateFluidFillLevel();
		
		if (!isFluidTankEmpty()) {
			return this.fluid_tank.getFluid().getFluid();
		}
		return null;
	}
	
	public String getCurrentStoredFluidName() {
		if (isFluidTankEmpty()) {
			return "Empty";
		}
		return this.fluid_tank.getFluid().getDisplayName().toString();
	}

	public boolean isFluidTankEmpty() {
		return this.fluid_tank.getFluidAmount() == 0;
	}

	public int getFluidTankCapacity() {
		return 256000;
	}
	
	public int getFluidFillLevel() {
		return this.fill_level;
	}

	public void setFluidFillLevel(int set) {
		this.fill_level = set;
	}
	
	public void updateFluidFillLevel() {
		if (!this.isFluidTankEmpty()) {
			if (this.getFluidLevelScaled(16) == 0) {
				this.fill_level = 1;
			} else {
				this.fill_level = this.getFluidLevelScaled(16);
			}
		} else {
			this.fill_level = 0;
		}
	}
	
	public int fill(FluidStack resource, FluidAction doFill) {
		this.updateFluidFillLevel();
		return fluid_tank.fill(resource, doFill);
	}

	public FluidStack drain(FluidStack resource, FluidAction doDrain) {
		this.updateFluidFillLevel();
		
		return fluid_tank.drain(resource.getAmount(), doDrain);
	}

	public FluidStack drain(int maxDrain, FluidAction doDrain) {
		this.updateFluidFillLevel();
		return this.fluid_tank.drain(maxDrain, doDrain);
	}
	
	public boolean canFill(Direction from, Fluid fluid) {
		return true;
	}
	
	public boolean canDrain(Direction from, Fluid fluid) {
		return true;
	}
	
	public FluidTank getFluidTank() {
		return this.fluid_tank;
	}
	
	public void setFluidTank(FluidTank tank) {
		this.fluid_tank = tank;
	}
	
	public void generatePocket(String creatorName) {
		if (this.isGenerated) {
			return;
		}
		
		World world = PocketRegistryManager.getWorldForPockets();
		
		int worldX = chunkPos.getX() * 16;
		int worldY = chunkPos.getY() * 16;
		int worldZ = chunkPos.getZ() * 16;

		Chunk chunk = world.getChunk(chunkPos.getX(), chunkPos.getZ());
		
		int l = worldY >> 4;
		ChunkSection storage = chunk.getSections()[l]; //.getBlockStorageArray()[l];
		
		if (storage == null) {
			storage = new ChunkSection(worldY);
			chunk.getSections()[l] = storage;
		}
		
		//The interior size of the structure + 1. [Not a dynamic value. If different size pockets are required, extra dimensions will have to be added]
		int size = 15;
		
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				for (int z = 0; z < size; z++) {
					boolean flagX = x == 0 || x == (size - 1);
					boolean flagY = y == 0 || y == (size - 1);
					boolean flagZ = z == 0 || z == (size - 1);
					
					BlockPos pos = new BlockPos(x, y, z);

					//Added those flags, so I could add these checks, almost halves the time.
					if (!(flagX || flagY || flagZ) || flagX && (flagY || flagZ) || flagY && flagZ) {
						continue;
					}

					//Creates the "edge" blocks first. Stylistic choice.
					if (x == 1 || y == 1 || z == 1) {
						storage.setBlockState(x, y, z, BusSubscriberMod.BLOCK_DIMENSIONAL_POCKET_WALL_EDGE.getDefaultState());
					} else if (x == (size - 2) || y == (size - 2) || z == (size - 2)) {
						storage.setBlockState(x, y, z, BusSubscriberMod.BLOCK_DIMENSIONAL_POCKET_WALL_EDGE.getDefaultState());
					} else {
						storage.setBlockState(x, y, z, BusSubscriberMod.BLOCK_DIMENSIONAL_POCKET_WALL.getDefaultState());
					}
					
					//Update the structure so it displays.
					//NOTE: Skylight is a required component. Without this, the game will crash.
					world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos).getBlock().getDefaultState(), 3);
					world.markAndNotifyBlock(pos, chunk, world.getBlockState(pos), world.getBlockState(pos).getBlock().getDefaultState(), 3);
					
					chunk.markDirty();
				}
			}
		}
		
		//Added these checks to ensure correct pocket generation.
		Block check_block_one = world.getBlockState(new BlockPos(worldX + 1, worldY, worldZ + 1)).getBlock();
		Block check_block_two = world.getBlockState(new BlockPos(worldX + 2, worldY, worldZ + 2)).getBlock();
		
		this.isGenerated = check_block_one instanceof BlockWallEdge && check_block_two instanceof BlockWall;
		
		if (!Strings.isNullOrEmpty(creatorName)) {
			this.setCreator(creatorName);
		}
		
		PocketRegistryManager.saveData();
	}
	
	
	public void shiftTo(PlayerEntity entityPlayer) {
		if (entityPlayer.world.isRemote || !(entityPlayer instanceof ServerPlayerEntity)) {
			return;
		}
		
		World world = this.getSourceBlockWorld();
		TeleportDirection teleportSide = TeleportDirection.getValidTeleportLocation(world, this.getLastBlockPos());
		if (teleportSide == TeleportDirection.UNKNOWN) {
			StringTextComponent comp = new StringTextComponent(TextHelper.ITALIC + "Teleport Disabled. Pocket is blocked on all sides.");
			entityPlayer.sendMessage(comp);
			
			return;
		}

		ServerPlayerEntity player = (ServerPlayerEntity) entityPlayer;

		DimensionType dimID = player.dimension;
		BlockPos tempSet = new BlockPos(chunkPos.getX() * 16, this.chunkPos.getY() * 16, this.chunkPos.getZ() * 16).add(spawnPos);
		Shifter teleporter = ShifterUtil.createTeleporter(dimID, tempSet, spawnYaw, spawnPitch);

		this.generatePocket(entityPlayer.getDisplayName().getString());

		StringTextComponent comp = new StringTextComponent(TextHelper.TEAL + "Entering pocket dimension...");
		if(!world.isRemote) {
			entityPlayer.sendMessage(comp);
		}
		
		if (dimID != ModDimensionManager.POCKET_DIMENSION.getDimensionType()) {
			ShifterUtil.shiftPlayerToDimension(player, ModDimensionManager.POCKET_DIMENSION.getDimensionType(), teleporter);
		} else {
			ShifterUtil.shiftPlayerToDimension(player, ModDimensionManager.POCKET_DIMENSION.getDimensionType(), teleporter);
		}
	}

	public void shiftFrom(PlayerEntity entityPlayer) {
		ServerPlayerEntity player = (ServerPlayerEntity) entityPlayer;
		World world = getSourceBlockWorld();

		if (this.isSourceBlockPlaced()) {
			TeleportDirection teleportSide = TeleportDirection.getValidTeleportLocation(world, this.getLastBlockPos());
			if (teleportSide != TeleportDirection.UNKNOWN) {
				BlockPos tempBlockSet = new BlockPos(this.getLastBlockPos()).add(teleportSide.toBlockPos());
				BlockPos spawnPos = new BlockPos(tempBlockSet);
				
				Shifter teleporter = ShifterUtil.createTeleporter(block_dim_type, spawnPos, 0.0F, 0.0F); //player.rotationYaw, player.rotationPitch);

				if (block_dim_type != ModDimensionManager.POCKET_DIMENSION.getDimensionType()) {
					ShifterUtil.shiftPlayerToDimension(player, block_dim_type, teleporter);
				} else {
					ShifterUtil.shiftPlayerToDimension(player, block_dim_type, teleporter);
				}
				
				ModUtil.sendPlayerMessage(world, entityPlayer, TextHelper.TEAL + "Leaving pocket dimension...");
			} else {
				ShifterUtil.sendPlayerToBedWithMessage(entityPlayer, world, TextHelper.ITALIC + "Something is blocking your Pocket. Sending you to your Bed!");
			}
		} else {
			ShifterUtil.sendPlayerToBedWithMessage(entityPlayer, world, TextHelper.ITALIC + "Someone broke your Pocket. Sending you to your Bed!");
		}
	}
}