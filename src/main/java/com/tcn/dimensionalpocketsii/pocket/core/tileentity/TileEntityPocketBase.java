package com.tcn.dimensionalpocketsii.pocket.core.tileentity;

import com.tcn.cosmoslibrary.client.impl.util.TextHelper;
import com.tcn.cosmoslibrary.impl.enums.EnumSideState;
import com.tcn.cosmoslibrary.impl.interfaces.block.IBlockInteract;
import com.tcn.cosmoslibrary.impl.interfaces.block.IBlockNotifier;
import com.tcn.cosmoslibrary.impl.interfaces.tile.ISidedTile;
import com.tcn.cosmoslibrary.impl.util.CompatUtil;
import com.tcn.cosmoslibrary.impl.util.CosmosChatUtil;
import com.tcn.cosmoslibrary.impl.util.CosmosUtil;
import com.tcn.cosmoslibrary.math.ChunkPos;
import com.tcn.dimensionalpocketsii.core.management.CoreDimensionManager;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.util.PocketUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public abstract class TileEntityPocketBase extends TileEntity implements IBlockNotifier, IBlockInteract, ITickableTileEntity, ISidedTile { //, IClientUpdatedTile.Storage {
	
	private static final String TAG_CUSTOM_DP_NAME = "customDPName";
	private String customName;
	private EnumSideState[] SIDE_STATE_ARRAY = EnumSideState.getStandardArray();
	
	@SuppressWarnings("unused")
	private Pocket pocket;
	
	public TileEntityPocketBase(TileEntityType<?> type) {
		super(type);
	}

	@Override
	public TileEntityType<?> getType() {
		return ModBusManager.POCKET_TYPE;
	}
	
	@Override
	public void tick() { }

	public Pocket getPocket() {
		//if (world.isRemote) {
		//	return this.pocket;
		//}
		
		return PocketRegistryManager.getOrCreatePocket(this.world, this.getPos());
	}
	
	/*
	 * - Lock Methods -
	 */
	
	public boolean getLockState() {
		return this.getPocket().getLockState();
	}
	
	public void setLockState(boolean change, boolean update) {
		this.getPocket().setLockState(change);
		this.sendUpdates(update);
	}

	/*
	 * - ISidedTile Methods
	 */
	@Override 
	public EnumSideState getSide(Direction facing) {
		return SIDE_STATE_ARRAY[facing.getIndex()];
	}
	
	@Override
	public void setSide(Direction facing, EnumSideState side_state, boolean update) {
		for (int i = 0; i < this.SIDE_STATE_ARRAY.length; i++) {
			SIDE_STATE_ARRAY[i] = side_state;
		}
		this.sendUpdates(update);
	}
	
	@Override
	public EnumSideState[] getSideArray() {
		return this.SIDE_STATE_ARRAY;
	}

	@Override
	public void setSideArray(EnumSideState[] new_array, boolean update) {
		SIDE_STATE_ARRAY = new_array;
		
		this.sendUpdates(update);
	}

	@Override
	public void cycleSide(Direction facing, boolean update) {
		EnumSideState state = SIDE_STATE_ARRAY[facing.getIndex()];
		
		EnumSideState state2 = state.getNextState();
		
		for (int i = 0; i < this.SIDE_STATE_ARRAY.length; i++) {
			SIDE_STATE_ARRAY[i] = state2;
		}
		
		this.sendUpdates(update);
	}

	@Override
	public boolean canConnect(Direction direction) {
		EnumSideState state = SIDE_STATE_ARRAY[direction.getIndex()];
		
		if (state.equals(EnumSideState.DISABLED)) {
			return false;
		}
		return true;
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		
		if (this.SIDE_STATE_ARRAY != null) {
			CompoundNBT sides_tag = new CompoundNBT();
			
			for (Direction c : Direction.values()) {
				int i = this.getSide(c).getIndex();
				
				sides_tag.putInt(c.getName2(), i);
			}
			compound.put("sides", sides_tag);
		}
		
		if (this.getPocket() != null) {
			this.getPocket().writeToNBT(compound);
			
			if (!this.world.isRemote) {
				this.pocket = this.getPocket();
			}
		}
		
		if (customName != null) {
			compound.putString(TAG_CUSTOM_DP_NAME, customName);
		}
		
		return compound;
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		
		if (compound.contains("sides")) {
			CompoundNBT sides_tag = compound.getCompound("sides");
			
			for (Direction c : Direction.values()) {
				this.setSide(c, EnumSideState.getStateFromIndex(sides_tag.getInt(c.getName2())), false);
			}
		}
		
		if (PocketUtil.hasPocketKey(compound)) {
			this.pocket = Pocket.readFromNBT(compound);
		}
		
		String tempString = compound.getString(TAG_CUSTOM_DP_NAME);
		if (!tempString.isEmpty()) {
			customName = tempString;
		}
	}
	
	@Override
	public void sendUpdates(boolean update) {
		if (update) {
			//this.world.markBlockRangeForRenderUpdate(this.getPos(), this.getPos());
			this.world.notifyBlockUpdate(this.getPos(), world.getBlockState(this.getPos()), world.getBlockState(this.getPos()), 3);
			//this.world.scheduleBlockUpdate(this.getPos(), this.getBlockType(), 0, 0);
		}
		this.markDirty();
	}
	
	//Set the data once it has been received. [NBT > TE]
	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		if (PocketUtil.hasPocketKey(tag)) {
			this.pocket = Pocket.readFromNBT(tag);
		}
	}
	
	//Retrieve the data to be stored. [TE > NBT]
	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT tag = new CompoundNBT();
		
		if (this.getPocket() != null) {
			this.getPocket().writeToNBT(tag);
		}
		
		return tag;
	}
	

	//Actually sends the data to the server. [NBT > SER]
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.getPos(), 3, this.getUpdateTag());
	}
	
	//Method is called once packet has been received by the client. [SER > CLT]
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		super.onDataPacket(net, pkt);
		CompoundNBT tag_ = pkt.getNbtCompound();
		
		BlockState state = world.getBlockState(pkt.getPos());
		
		this.handleUpdateTag(state, tag_);
		this.sendUpdates(true);
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		if (playerIn.isSneaking()) {
			if (playerIn.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
				Pocket pocket = this.getPocket();
				
				pocket.shift(playerIn, EnumShiftDirection.ENTER, pos, null);
				/*
				if (pocket_ != null) {
					if (pocket_.getCreator() == null) {
						pocket_.setCreator(playerIn);
						this.markDirty();
					}
				}
				*/
			} 
			
			else if (CosmosUtil.isHoldingHammer(playerIn)) {
				Pocket pocket_ = this.getPocket();
				
				if (pocket_ != null) {
					if (pocket_.checkIfOwner(playerIn)) {
						if (this.getLockState()) {
							CosmosChatUtil.sendPlayerMessage(playerIn, true, TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.remove_locked.name"));
						} else {
							//ChunkLoaderManagerRoom.removePocketFromChunkLoader(this.getPocket());
							//ChunkLoaderManagerBlock.removePocketBlockFromChunkLoader(this.getPos());
							CompatUtil.spawnStack(this.generateItemStackOnRemoval(pos), world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0);
							
							world.setBlockState(pos, Blocks.AIR.getDefaultState());
						}
					} else {
						CosmosChatUtil.sendPlayerMessage(playerIn, true, TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.remove_not.name"));
					}
				}
			}
		} else {
			if (playerIn.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
				Pocket pocket_ = this.getPocket();
				String creator = pocket_.getOwnerName();
				String locked_comp;

				if (this.getLockState()) {
					locked_comp = TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.locked.name");
				} else {
					locked_comp = TextHelper.GREEN + TextHelper.BOLD + I18n.format("pocket.status.unlocked.name");
				}

				CosmosChatUtil.sendPlayerMessage(playerIn, true, TextHelper.LIGHT_GRAY + TextHelper.BOLD + I18n.format("pocket.status.owner.name") + TextHelper.PURPLE + TextHelper.BOLD + " {" + creator + "} " + TextHelper.LIGHT_GRAY + TextHelper.BOLD + I18n.format("pocket.status.and.name") + locked_comp);
				
				return ActionResultType.SUCCESS;
			} 
			
			else if (CosmosUtil.isHoldingHammer(playerIn)) {
				Pocket pocket_ = this.getPocket();
				
				if (pocket_ != null) {
					if (pocket_.checkIfOwner(playerIn)) {
						this.cycleSide(hit.getFace(), true);
						
						this.markDirty();
						CosmosChatUtil.sendPlayerMessage(playerIn, true, TextHelper.LIGHT_GRAY + TextHelper.BOLD + I18n.format("pocket.status.cycle_side.name") + this.getSide(hit.getFace()).getTextColour() + TextHelper.BOLD + " [" + this.getSide(hit.getFace()).getDisplayName() + "]");
						
						return ActionResultType.SUCCESS;
					} else {
						CosmosChatUtil.sendPlayerMessage(playerIn, true, TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.access_lock.name"));
					}
				}
			}
		}
		
		return ActionResultType.FAIL;
	}

	@Override
	public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn) {
		Pocket pocket_ = this.getPocket();
		
		if (pocket_ != null) {
			if (CosmosUtil.isHoldingHammer(playerIn) && playerIn.isSneaking()) {
				if (pocket_.checkIfOwner(playerIn)) {
					//if (!(worldIn.isRemote)) {
						//FMLNetworkHandler.openGui(playerIn, DimensionalPockets.INSTANCE, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
					//}
					
					
				} else {
					CosmosChatUtil.sendPlayerMessage(playerIn, true, TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.access_set.name"));
				} 		
			}
		} else {
			CosmosChatUtil.sendPlayerMessage(playerIn, true, TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.null.name"));
		}
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) { }

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (!worldIn.isRemote) {
			if (stack.hasTag()) {
				if (!(PocketUtil.isDimensionEqual(worldIn, CoreDimensionManager.POCKET_WORLD))) {
					CompoundNBT compound = stack.getTag();
					
					if (compound.contains("nbt_data")) {
						CompoundNBT nbt_tag = compound.getCompound("nbt_data");
						
						int X = nbt_tag.getCompound("chunk_set").getInt("X");
						int Z = nbt_tag.getCompound("chunk_set").getInt("Z");
		
						ChunkPos chunkSet = new ChunkPos(X, Z);
						boolean success = PocketRegistryManager.getPocketFromChunk(chunkSet) != null;
		
						if (!success) {
							throw new RuntimeException("YOU DESERVED THIS!");
						}
		
						PocketRegistryManager.updatePocket(chunkSet, placer.world.getDimensionKey(), getPos());
		
						if (nbt_tag.contains("display")) {
							String tempString = nbt_tag.getCompound("display").getString("Name");
							if (!tempString.isEmpty()) {
								customName = tempString;
							}
						}
						
						CompoundNBT side_tag = nbt_tag.getCompound("sides");
						
						for(Direction c : Direction.values()) {
							this.setSide(c, EnumSideState.getStateFromIndex(side_tag.getInt("index_" + c.getIndex())), true);
						}
					}
	
					if (placer instanceof PlayerEntity) {
						ServerPlayerEntity player = (ServerPlayerEntity) placer;
						if (player.getHeldItemMainhand() == stack) {
							player.getHeldItemMainhand().setCount(0);
						}
					}
	
					Pocket pocket = this.getPocket();
					pocket.generatePocket(((PlayerEntity) placer));
					//NetworkHandler.sendPocketSetCreator(placer.getName(), pos);
					//ChunkLoaderManagerRoom.addPocketToChunkLoader(pocket);
					//ChunkLoaderManagerBlock.addPocketBlockToChunkLoader(this.getPos(), placer.dimension.getId());
				} else if (PocketUtil.isDimensionEqual(worldIn, CoreDimensionManager.POCKET_WORLD)) {
					CompoundNBT stack_tag = stack.getTag();

					if (stack_tag.contains("nbt_data")) {
						CompoundNBT nbt_tag = stack_tag.getCompound("nbt_data");
						
						int X = nbt_tag.getCompound("chunk_set").getInt("X");
						int Z = nbt_tag.getCompound("chunk_set").getInt("Z");
						
						ChunkPos chunk_pos = new ChunkPos(X, Z);
						Pocket test_pocket = PocketRegistryManager.getPocketFromChunk(chunk_pos);
	
						if (test_pocket != null) {
							if (test_pocket.equals(PocketRegistryManager.getPocketFromChunk(PocketUtil.scaleToChunkPos(pos)))) {
								if (!world.isRemote) {
									CompatUtil.spawnStack(this.generateItemStackWithNBT(pos, chunk_pos), world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0);
									
									world.setBlockState(pos, Blocks.AIR.getDefaultState());
								}
							} else {
								PocketRegistryManager.updatePocket(chunk_pos, placer.world.getDimensionKey(), getPos());
	
								if (nbt_tag.contains("display")) {
									CompoundNBT nbt = nbt_tag.getCompound("display");
									
									String tempString = nbt.getString("name");
									if (!tempString.isEmpty()) {
										customName = tempString;
									}
								}
	
								if (placer instanceof PlayerEntity) {
									ServerPlayerEntity player = (ServerPlayerEntity) placer;
									if (player.getHeldItemMainhand() == stack) {
										player.getHeldItemMainhand().setCount(0);
									}
								}
	
								Pocket pocket = this.getPocket();
								pocket.generatePocket(((PlayerEntity) placer));
								//NetworkHandler.sendPocketSetCreator(placer.getName(), pos);
								//ChunkLoaderManagerRoom.addPocketToChunkLoader(pocket);
								//ChunkLoaderManagerBlock.addPocketBlockToChunkLoader(this.getPos(), placer.dimension.getId());
							}
						}
					}
				} 
			} else {
				Pocket pocket = this.getPocket();
				pocket.generatePocket((PlayerEntity) placer);
				//ChunkLoaderManagerRoom.addPocketToChunkLoader(pocket);
				//ChunkLoaderManagerBlock.addPocketBlockToChunkLoader(this.getPos(), placer.dimension);
			}
		}
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) { }

	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) { }

	public ItemStack generateItemStackOnRemoval(BlockPos pos) {
		ItemStack itemStack = new ItemStack(ModBusManager.BLOCK_POCKET);

		if (!itemStack.hasTag()) {
			itemStack.setTag(new CompoundNBT());
		}

		ChunkPos chunkSet = this.getPocket().getChunkPos();
		
		CompoundNBT compound = new CompoundNBT();
		
		//Saves the chunk data to NBT
		CompoundNBT chunk_tag = new CompoundNBT();
		chunk_tag.putInt("X", chunkSet.getX());
		chunk_tag.putInt("Z", chunkSet.getZ());

		compound.put("chunk_set", chunk_tag);

		String creatorLore = null;
		Pocket pocket = this.getPocket();
		
		if (pocket != null && pocket.getOwnerName() != null) {
			creatorLore = TextHelper.LIGHT_BLUE + TextHelper.BOLD + I18n.format("pocket.desc.creator.name") + TextHelper.PURPLE + TextHelper.BOLD + " [" + pocket.getOwnerName() + "]";
		}
		
		//Saves the side data to NBT
		if (this instanceof ISidedTile) {
			CompoundNBT side_tag = new CompoundNBT();
			
			for (Direction c : Direction.values()) {
				side_tag.putInt("index_" + c.getIndex(), this.getSideArray()[c.getIndex()].getIndex());
			}
			
			compound.put("sides", side_tag);
		}
		
		itemStack.getTag().put("nbt_data", compound);
		
		BlockPos blockSet = PocketUtil.scaleFromChunkPos(chunkSet); 

		itemStack = CompatUtil.generateItem(itemStack, customName, false, TextHelper.LIGHT_GRAY + TextHelper.BOLD + "Pocket: [" + blockSet.getX() + " | " + blockSet.getY() + " | " + blockSet.getZ() + "]", creatorLore);
		return itemStack;
	}

	public ItemStack generateItemStackWithNBT(BlockPos posIn, ChunkPos chunkPosIn) {
		ItemStack item_stack = new ItemStack(ModBusManager.BLOCK_POCKET);

		if (!item_stack.hasTag()) {
			item_stack.setTag(new CompoundNBT());
		}

		CompoundNBT compound = new CompoundNBT();
		CompoundNBT chunk_tag = new CompoundNBT();
		
		int x = chunkPosIn.getX();
		int z = chunkPosIn.getZ();
		
		chunk_tag.putInt("X", x);
		chunk_tag.putInt("Z", z);

		compound.put("chunk_set", chunk_tag);
		
		item_stack.getTag().put("nbt_data", compound);

		String creatorLore = null;
		Pocket pocket = getPocket();
		if (pocket != null && pocket.getOwnerName() != null) {
			creatorLore = "Creator: [" + pocket.getOwnerName() + "]";
		}

		item_stack = CompatUtil.generateItem(item_stack, customName, false, "Pocket: [" + (x << 4) + "," + (z << 4) + "]", creatorLore);

		return item_stack;
	}
}