package com.zhr.dimensionalpocketsii.pocket.core.tileentity;

import java.util.UUID;

import com.zhr.cosmoslibrary.impl._client.util.TextHelper;
import com.zhr.cosmoslibrary.impl.enums.EnumSideState;
import com.zhr.cosmoslibrary.impl.interfaces.block.IBlockInteract;
import com.zhr.cosmoslibrary.impl.interfaces.block.IBlockNotifier;
import com.zhr.cosmoslibrary.impl.interfaces.tile.ISidedTile;
import com.zhr.cosmoslibrary.impl.util.CompatUtil;
import com.zhr.cosmoslibrary.impl.util.ModUtil;
import com.zhr.dimensionalpocketsii.core.management.SoundHandler;
import com.zhr.dimensionalpocketsii.core.management.bus.BusSubscriberMod;
import com.zhr.dimensionalpocketsii.pocket.core.Pocket;
import com.zhr.dimensionalpocketsii.pocket.core.management.PocketDimensionManager;
import com.zhr.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.zhr.dimensionalpocketsii.pocket.core.util.PocketUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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
		return BusSubscriberMod.POCKET_TYPE;
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
	private void shiftIntoPocket(PlayerEntity player, BlockPos pos_) {
		this.getPocket().addPosToBlockArray(this.getPos());
		this.getPocket().shiftTo(player);
	}
	
	public boolean getLockState() {
		return this.getPocket().getLockState();
	}
	
	public void setLockState(boolean change) {
		this.getPocket().setLockState(change);
		this.sendUpdates();
	}

	/*
	 * - ISidedTile Methods
	 */
	@Override 
	public EnumSideState getSide(Direction direction) {
		return SIDE_STATE_ARRAY[direction.getIndex()];
	}
	
	@Override
	public void setSide(Direction direction, EnumSideState side_state) {
		SIDE_STATE_ARRAY[direction.getIndex()] = side_state;
		
		this.sendUpdates();
	}
	
	@Override
	public EnumSideState[] getSideArray() {
		return this.SIDE_STATE_ARRAY;
	}

	@Override
	public void setSideArray(EnumSideState[] new_array) {
		SIDE_STATE_ARRAY = new_array;
		
		this.sendUpdates();
	}

	@Override
	public void cycleSide(Direction direction) {
		EnumSideState state = SIDE_STATE_ARRAY[direction.getIndex()];
		
		EnumSideState state2 = state.getNextState();
		
		SIDE_STATE_ARRAY[direction.getIndex()] = state2;
		
		this.sendUpdates();
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
		//super.write(compound);
		
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
				this.setSide(c, EnumSideState.getStateFromIndex(sides_tag.getInt(c.getName2())));
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
	public void sendUpdates() {
		//this.world.markBlockRangeForRenderUpdate(this.getPos(), this.getPos());
		this.world.notifyBlockUpdate(this.getPos(), world.getBlockState(this.getPos()), world.getBlockState(this.getPos()), 3);
		//this.world.scheduleBlockUpdate(this.getPos(), this.getBlockType(), 0, 0);		
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
		this.sendUpdates();
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		if (playerIn.isSneaking()) {
			if (playerIn.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
				Pocket pocket_ = this.getPocket();
				
				if (pocket_ != null) {
					if (pocket_.getCreator() == null) {
						pocket_.setCreator(playerIn);
						this.markDirty();
					} else {
						if (this.getLockState()) {
							if (pocket_.checkCreator(playerIn)) {
								playerIn.playSound(SoundHandler.GENERIC.PORTAL_IN, 1.0F, 1.0F);
								this.shiftIntoPocket(playerIn, pos);
							} else if (pocket_.checkIfAllowedPlayer(playerIn)) {
								playerIn.playSound(SoundHandler.GENERIC.PORTAL_IN, 1.0F, 1.0F);
								this.shiftIntoPocket(playerIn, pos);
							} else {
								if (world.isRemote) {
									playerIn.sendMessage(new StringTextComponent(TextHelper.RED + TextHelper.BOLD + new TranslationTextComponent("pocket.status.is_locked.name").getString() + "[" + pocket_.getCreatorName() + "]"), UUID.randomUUID());
									//ModUtil.sendPlayerMessage(worldIn, playerIn, TextHelper.RED + TextHelper.BOLD + new TranslationTextComponent("pocket.status.is_locked.name").getString() + "[" + pocket_.getCreatorName() + "]");
								}
							}
						} else {
							playerIn.playSound(SoundHandler.GENERIC.PORTAL_IN, 1.0F, 1.0F);
							this.shiftIntoPocket(playerIn, pos);
						}
					}
				}
			} 
			
			else if (ModUtil.isHoldingHammer(playerIn)) {
				System.out.println("GLOW");
				
				Pocket pocket_ = this.getPocket();
				
				if (pocket_ != null) {
					if (pocket_.checkCreator(playerIn)) {
						if (this.getLockState()) {
							if (world.isRemote) {
								playerIn.sendMessage(new StringTextComponent(TextHelper.RED + TextHelper.BOLD + new TranslationTextComponent("pocket.status.remove_locked.name").getString()), UUID.randomUUID());
							}
						} else {
							//ChunkLoaderManagerRoom.removePocketFromChunkLoader(this.getPocket());
							//ChunkLoaderManagerBlock.removePocketBlockFromChunkLoader(this.getPos());
							CompatUtil.spawnStack(this.generateItemStackOnRemoval(pos), world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0);
							
							world.setBlockState(pos, Blocks.AIR.getDefaultState());
						}
					} else {
						if (world.isRemote) {
							playerIn.sendMessage(new StringTextComponent(TextHelper.RED + TextHelper.BOLD + new TranslationTextComponent("pocket.status.remove_not.name").getString()), UUID.randomUUID());
						}
					}
				}
			}
		} else {
			if (playerIn.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
				Pocket pocket_ = this.getPocket();
				String creator = pocket_.getCreatorName();
				String locked_comp;

				if (this.getLockState()) {
					locked_comp = TextHelper.RED + TextHelper.BOLD + new TranslationTextComponent("pocket.status.locked.name").getString();
				} else {
					locked_comp = TextHelper.GREEN + TextHelper.BOLD + new TranslationTextComponent("pocket.status.unlocked.name").getString();
				}

				if (world.isRemote) {
					playerIn.sendMessage(new StringTextComponent(TextHelper.LIGHT_GRAY + TextHelper.BOLD + new TranslationTextComponent("pocket.status.owner.name").getString() + TextHelper.PURPLE + TextHelper.BOLD + " {" + creator + "} " + TextHelper.LIGHT_GRAY + TextHelper.BOLD + new TranslationTextComponent("pocket.status.and.name").getString() + locked_comp), UUID.randomUUID());
				}
				
				return ActionResultType.SUCCESS;
			} 
			
			else if (ModUtil.isHoldingHammer(playerIn)) {
				Pocket pocket_ = this.getPocket();
				
				if (pocket_ != null) {
					if (pocket_.checkCreator(playerIn)) {
						if (world.isRemote) {
							this.cycleSide(hit.getFace());
						}
						this.markDirty();
						
						if (world.isRemote) {
							playerIn.sendMessage(new StringTextComponent(TextHelper.LIGHT_GRAY + TextHelper.BOLD + new TranslationTextComponent("pocket.status.cycle_side.name").getString() + this.getSide(hit.getFace()).getTextColour() + TextHelper.BOLD + " [" + this.getSide(hit.getFace()).getDisplayName() + "]"), UUID.randomUUID());
						}
						
						return ActionResultType.SUCCESS;
					} else {
						if (world.isRemote) {
							playerIn.sendMessage(new StringTextComponent(TextHelper.RED + TextHelper.BOLD + new TranslationTextComponent("pocket.status.access_lock.name").getString()), UUID.randomUUID());
						}
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
			if (ModUtil.isHoldingHammer(playerIn) && playerIn.isSneaking()) {
				if (pocket_.checkCreator(playerIn)) {
					//if (!(worldIn.isRemote)) {
						//FMLNetworkHandler.openGui(playerIn, DimensionalPockets.INSTANCE, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
					//}
					
					
				} else {
					if (worldIn.isRemote) {
						playerIn.sendMessage(new StringTextComponent(TextHelper.RED + TextHelper.BOLD + new TranslationTextComponent("pocket.status.access_set.name").getString()), UUID.randomUUID());
					}
				} 		
			}
		} else {
			if (world.isRemote) {
				playerIn.sendMessage(new StringTextComponent(TextHelper.RED + TextHelper.BOLD + new TranslationTextComponent("pocket.status.null.name").getString()), UUID.randomUUID());
			}
		}
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) { }

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (!worldIn.isRemote) {
			if (stack.hasTag()) {
				if (!(placer.world.getDimensionKey().equals(PocketDimensionManager.POCKET_WORLD))) {
					CompoundNBT compound = stack.getTag();
					
					if (compound.contains("nbt_data")) {
						CompoundNBT nbt_tag = compound.getCompound("nbt_data");
						
						int X = nbt_tag.getCompound("chunk_set").getInt("X");
						int Y = nbt_tag.getCompound("chunk_set").getInt("Y");
						int Z = nbt_tag.getCompound("chunk_set").getInt("Z");
		
						BlockPos chunkSet = new BlockPos(X, Y, Z);
						boolean success = PocketRegistryManager.getPocket(chunkSet) != null;
		
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
							this.setSide(c, EnumSideState.getStateFromIndex(side_tag.getInt("index_" + c.getIndex())));
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
				} else if (placer.world.getDimensionKey().equals(PocketDimensionManager.POCKET_WORLD)) {
					CompoundNBT stack_tag = stack.getTag();

					if (stack_tag.contains("nbt_data")) {
						CompoundNBT nbt_tag = stack_tag.getCompound("nbt_data");
						
						int X = nbt_tag.getCompound("chunk_set").getInt("X");
						int Y = nbt_tag.getCompound("chunk_set").getInt("Y");
						int Z = nbt_tag.getCompound("chunk_set").getInt("Z");
						
						BlockPos chunk_set = new BlockPos(X, Y, Z);
	
						Pocket test_pocket = PocketRegistryManager.getPocket(chunk_set);
	
						if (test_pocket != null) {
							if (test_pocket.equals(PocketRegistryManager.getPocket(new BlockPos(pos.getX() >> 4, pos.getY() >> 4, pos.getZ() >> 4)))) {
								if (!world.isRemote) {
									CompatUtil.spawnStack(this.generateItemStackWithNBT(pos, X, Y, Z), world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0);
									
									world.setBlockState(pos, Blocks.AIR.getDefaultState());
								}
							} else {
								PocketRegistryManager.updatePocket(chunk_set, placer.world.getDimensionKey(), getPos());
	
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
		ItemStack itemStack = new ItemStack(BusSubscriberMod.BLOCK_POCKET);

		if (!itemStack.hasTag()) {
			itemStack.setTag(new CompoundNBT());
		}

		BlockPos chunkSet = this.getPocket().getChunkPos();
		
		CompoundNBT compound = new CompoundNBT();
		
		//Saves the chunk data to NBT
		CompoundNBT chunk_tag = new CompoundNBT();
		chunk_tag.putInt("X", chunkSet.getX());
		chunk_tag.putInt("Y", chunkSet.getY());
		chunk_tag.putInt("Z", chunkSet.getZ());

		compound.put("chunk_set", chunk_tag);

		String creatorLore = null;
		Pocket pocket = this.getPocket();
		
		if (pocket != null && pocket.getCreator() != null) {
			creatorLore = TextHelper.LIGHT_BLUE + TextHelper.BOLD + new TranslationTextComponent("pocket.desc.creator.name").getString() + TextHelper.PURPLE + TextHelper.BOLD + " [" + pocket.getCreator() + "]";
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
		
		BlockPos blockSet = new BlockPos(chunkSet.getX() << 4, chunkSet.getY() << 4, chunkSet.getZ() << 4);

		itemStack = CompatUtil.generateItem(itemStack, customName, false, TextHelper.LIGHT_GRAY + TextHelper.BOLD + "Pocket: [" + blockSet.getX() + " | " + blockSet.getY() + " | " + blockSet.getZ() + "]", creatorLore);
		return itemStack;
	}

	public ItemStack generateItemStackWithNBT(BlockPos pos, int x, int y, int z) {
		ItemStack item_stack = new ItemStack(BusSubscriberMod.BLOCK_POCKET);

		if (!item_stack.hasTag()) {
			item_stack.setTag(new CompoundNBT());
		}

		CompoundNBT compound = new CompoundNBT();
		
		CompoundNBT chunk_tag = new CompoundNBT();
		chunk_tag.putInt("X", x);
		chunk_tag.putInt("Y", y);
		chunk_tag.putInt("Z", z);

		compound.put("chunk_set", chunk_tag);
		
		item_stack.getTag().put("nbt_data", compound);

		String creatorLore = null;
		Pocket pocket = getPocket();
		if (pocket != null && pocket.getCreator() != null) {
			creatorLore = "Creator: [" + pocket.getCreator() + "]";
		}

		item_stack = CompatUtil.generateItem(item_stack, customName, false, "Pocket: [" + (x << 4) + "," + (y << 4) + "," + (z << 4) + "]", creatorLore);

		return item_stack;
	}
}