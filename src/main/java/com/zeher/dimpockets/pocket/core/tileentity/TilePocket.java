package com.zeher.dimpockets.pocket.core.tileentity;

import com.zeher.dimpockets.core.manager.BusSubscriberMod;
import com.zeher.dimpockets.core.manager.ModDimensionManager;
import com.zeher.dimpockets.core.manager.SoundHandler;
import com.zeher.dimpockets.core.manager.TileEntityManager;
import com.zeher.dimpockets.core.util.DimUtils;
import com.zeher.dimpockets.pocket.core.Pocket;
import com.zeher.dimpockets.pocket.core.manager.PocketRegistryManager;
import com.zeher.zeherlib.api.client.util.TextHelper;
import com.zeher.zeherlib.api.compat.client.interfaces.IClientUpdatedTile;
import com.zeher.zeherlib.api.compat.core.interfaces.EnumSideState;
import com.zeher.zeherlib.api.compat.core.interfaces.ISidedTile;
import com.zeher.zeherlib.api.core.interfaces.block.IBlockInteract;
import com.zeher.zeherlib.api.core.interfaces.block.IBlockNotifier;
import com.zeher.zeherlib.mod.util.ModUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TilePocket extends TileBase implements IBlockNotifier, IBlockInteract, ITickableTileEntity, ISidedInventory, IInventory, ISidedTile, /**IEnergyReceiver, IEnergyProvider,*/ IClientUpdatedTile.Storage {
	
	public TilePocket() {
		super(TileEntityManager.POCKET);
	}

	@SuppressWarnings("unused")
	private static final String TAG_CUSTOM_DP_NAME = "customDPName";
	private String customName;
	
	public EnumSideState[] SIDE_STATE_ARRAY = EnumSideState.getStandardArray();

	private NonNullList<ItemStack> INVENTORY_STACKS = NonNullList.<ItemStack>withSize(17, ItemStack.EMPTY);
	
	@OnlyIn(Dist.CLIENT)
	private Pocket pocket;

	@Override
	public void tick() {
		/**
		for (int i = 0 ; i < 9; i++) {
			if (this.getStackInSlot(i) != this.getPocket().items.get(i)) {
				ItemStack pocket_stack = this.getPocket().items.get(i);
				ItemStack copy = pocket_stack.copy();
				if (!this.world.isRemote) {
					this.INVENTORY_STACKS.set(i, copy);
				}
			}
		}*/
	}

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
		Pocket pocket = getPocket();
		
		if (pocket != null && pocket.getCreator() != null) {
			creatorLore = TextHelper.LIGHT_BLUE + TextHelper.BOLD + I18n.format("pocket.desc.creator.name") + TextHelper.PURPLE + TextHelper.BOLD + " [" + pocket.getCreator() + "]";
		}
		
		//Saves the side data to NBT
		if (this instanceof ISidedTile) {
			CompoundNBT side_tag = new CompoundNBT();
			
			for (Direction c : Direction.values()) {
				side_tag.putInt("index_" + c.getIndex(), this.getSideArray()[c.getIndex()].getIndex());
			}
			
			compound.put("sides", side_tag);
		}
		
		/**
		if (this.getEnergyStored(Direction.DOWN) > 0) {
			compound.putInt("energy", this.getEnergyStored(Direction.DOWN));
		}
		*/
		
		itemStack.getTag().put("nbt_data", compound);
		
		BlockPos blockSet = new BlockPos(chunkSet.getX() << 4, chunkSet.getY() << 4, chunkSet.getZ() << 4);

		itemStack = DimUtils.generateItem(itemStack, customName, false, TextHelper.LIGHT_GRAY + TextHelper.BOLD
				+ "Pocket: [" + blockSet.getX() + " | " + blockSet.getY() + " | " + blockSet.getZ() + "]", creatorLore);
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

		item_stack = DimUtils.generateItem(item_stack, customName, false,
				"Pocket: [" + (x << 4) + "," + (y << 4) + "," + (z << 4) + "]", creatorLore);

		return item_stack;
	}

	@Override
	public Pocket getPocket() {
		return PocketRegistryManager.getOrCreatePocket(this.world, this.getPos());
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		/**
		if (this.SIDE_STATE_ARRAY != null) {
			CompoundNBT sides_tag = new CompoundNBT();
			
			for (Direction c : Direction.values()) {
				int i = this.getSide(c).getIndex();
				
				sides_tag.putInt(c.getName(), i);
			}
			compound.put("sides", sides_tag);
		}
		
		if (customName != null) {
			compound.putString(TAG_CUSTOM_DP_NAME, customName);
		}
		*/
		return compound;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		/*
		if (compound.contains("sides")) {
			CompoundNBT sides_tag = compound.getCompound("sides");
			
			for (Direction c : Direction.values()) {
				this.setSide(c, EnumSideState.getStateFromIndex(sides_tag.getInt(c.getName())));
			}
		}
		
		String tempString = compound.getString(TAG_CUSTOM_DP_NAME);
		if (!tempString.isEmpty()) {
			customName = tempString;
		}*/
	}

	private void shiftIntoPocket(PlayerEntity player, BlockPos pos_) {
		this.getPocket().addPosToBlockMap(this.getPos());
		this.getPocket().shiftTo(player);
	}

	/**
	 * Check whether the pocket is locked.
	 * @return boolean of the lock state
	 */
	public boolean getLockState() {
		return this.getPocket().getLockState();
	}
	
	/**
	 * Set the lock state of the pocket.
	 */
	public void setLockState(boolean change) {
		this.getPocket().setLockState(change);
		this.sendUpdates();
	}

	/**
	 * ISidedTile Begin--
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
	public void sendUpdates() {
		//this.world.markBlockRangeForRenderUpdate(this.getPos(), this.getPos());
		this.world.notifyBlockUpdate(this.getPos(), world.getBlockState(this.getPos()), world.getBlockState(this.getPos()), 3);
		//this.world.scheduleBlockUpdate(this.getPos(), this.getBlockType(), 0, 0);		
		this.markDirty();
	}
	
	/**
	 * Set the data once it has been received. [NBT > TE]
	 */
	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		System.out.println("READ TAG: index_1/up " + tag.getInt("index_1"));

		this.read(tag);
	}
	
	/**
	 * Retrieve the data to be stored. [TE > NBT]
	 */
	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT tag = new CompoundNBT();
		
		System.out.println("WRITE TAG: index_1/up " + this.getSide(Direction.UP).getIndex());
		
		this.write(tag);
		
		return tag;
	}
	
	/**
	 * Actually sends the data to the server. [NBT > SER]
	 */
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.getPos(), 3, this.getUpdateTag());
	}
	
	/**
	 * Method is called once packet has been received by the client. [SER > CLT]
	 */
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		super.onDataPacket(net, pkt);
		CompoundNBT tag_ = pkt.getNbtCompound();
		
		this.handleUpdateTag(tag_);
		this.sendUpdates();
	}
	
	@Override
	public int getSizeInventory() {
		/**return this.INVENTORY_STACKS.size() + */ return this.INVENTORY_STACKS.size();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.INVENTORY_STACKS.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		this.markDirty();
		return ItemStackHelper.getAndSplit(this.INVENTORY_STACKS, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		this.markDirty();
		return ItemStackHelper.getAndRemove(this.INVENTORY_STACKS, index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.INVENTORY_STACKS.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
		
		this.getPocket().items.set(index, stack);
		
		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return true;
	}

	@Override
	public void openInventory(PlayerEntity player) { }

	@Override
	public void closeInventory(PlayerEntity player) { }

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}
	
	@Override
	public void clear() { }

	@Override
	public int[] getSlotsForFace(Direction side) {
		return new int[] { 0 };
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
		return true;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
		return true;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.INVENTORY_STACKS) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		if (playerIn.isSneaking() && playerIn.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
			if (this.getPocket() != null) {
				Pocket pocket = this.getPocket();
				
				if (pocket.getCreator() == null) {
					pocket.setCreator(playerIn.getDisplayName().getString());
					//NetworkHandler.sendCreatorPacketToServer(playerIn.getName(), pos);
					this.markDirty();
				} else { 
					String creator = pocket.getCreator();
	
					String player_name = playerIn.getDisplayName().getString();
					
					this.markDirty();
					
					if (creator != null) {
						if (this.getLockState()) {
							if (player_name.equals(creator)) {
								playerIn.playSound(SoundHandler.GENERIC.PORTAL_IN, 1.0F, 1.0F);
								this.shiftIntoPocket(playerIn, pos);
							} else if (pocket.checkPlayerMap(player_name)) {
								playerIn.playSound(SoundHandler.GENERIC.PORTAL_IN, 1.0F, 1.0F);
								this.shiftIntoPocket(playerIn, pos);
							} else {
								StringTextComponent comp = new StringTextComponent(TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.is_locked.name") + "[" + creator + "]");
								playerIn.sendMessage(comp);
							}
						} else {
							playerIn.playSound(SoundHandler.GENERIC.PORTAL_IN, 1.0F, 1.0F);
							this.shiftIntoPocket(playerIn, pos);
						}
					}
				}
			}
		} else if (!playerIn.isSneaking() && playerIn.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
			String creator = this.getPocket().getCreator();
			String locked_comp;

			if (this.getLockState()) {
				locked_comp = TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.locked.name");
			} else {
				locked_comp = TextHelper.GREEN + TextHelper.BOLD + I18n.format("pocket.status.unlocked.name");
			}

			if (!worldIn.isRemote) {
				StringTextComponent comp = new StringTextComponent(TextHelper.LIGHT_GRAY + TextHelper.BOLD + I18n.format("pocket.status.owner.name") + TextHelper.PURPLE + TextHelper.BOLD + " {" + creator + "} " + TextHelper.LIGHT_GRAY + TextHelper.BOLD + I18n.format("pocket.status.and.name") + locked_comp + ".");
				playerIn.sendMessage(comp);
				
				return false;
			}
		} else if ((ModUtil.isHoldingHammer(playerIn)) && (playerIn.isSneaking())) {
			playerIn.swingArm(Hand.MAIN_HAND);
			if (!(worldIn.isRemote)) {

				String creator = this.getPocket().getCreator();

				if (creator != null) {
					if (playerIn.getDisplayName().getString().equals(creator)) {
						if (this.getLockState()) {
							StringTextComponent comp = new StringTextComponent(TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.remove_locked.name"));
							playerIn.sendMessage(comp);
						} else {
							//ChunkLoaderManagerRoom.removePocketFromChunkLoader(this.getPocket());
							//ChunkLoaderManagerBlock.removePocketBlockFromChunkLoader(this.getPos());
							DimUtils.spawnItemStack(this.generateItemStackOnRemoval(pos), world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0);
							
							world.setBlockState(pos, Blocks.AIR.getDefaultState());
						}
					} else {
						StringTextComponent comp = new StringTextComponent(TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.remove_not.name"));
						playerIn.sendMessage(comp);
					}
				}

				return true;
			}
		} else if ((ModUtil.isHoldingHammer(playerIn)) && !(playerIn.isSneaking())) {
			playerIn.swingArm(Hand.MAIN_HAND);
			
			String creator = this.getPocket().getCreator();

			if (creator != null) {
				if (playerIn.getDisplayName().getString().equals(creator)) {
					this.cycleSide(hit.getFace());
					this.markDirty();
					
					if (!(worldIn.isRemote)) {
						StringTextComponent comp = new StringTextComponent(TextHelper.LIGHT_GRAY + TextHelper.BOLD + I18n.format("pocket.status.cycle_side.name") + this.getSide(hit.getFace()).getTextColour() + TextHelper.BOLD + " [" + this.getSide(hit.getFace()).getDisplayName() + "]");
						playerIn.sendMessage(comp);
					}
					return false;
				} else {
					if (!(worldIn.isRemote)) {
						StringTextComponent comp = new StringTextComponent(TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.access_lock.name"));
						playerIn.sendMessage(comp);
					}
				}
			}
		}
		return false;
	}

	@Override
	public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn) {
		if (this.getPocket() != null) {
			if (ModUtil.isHoldingHammer(playerIn) && playerIn.isSneaking()) {
				if(this.getPocket().getCreator() != null) {
					String creator = this.getPocket().getCreator();
					
					if (creator != null) {
						if (playerIn.getDisplayName().getString().equals(creator)) {
							if (!(worldIn.isRemote)) {
								//FMLNetworkHandler.openGui(playerIn, DimensionalPockets.INSTANCE, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
							}
						} else {
							if (!(worldIn.isRemote)) {
								StringTextComponent comp = new StringTextComponent(TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.access_set.name"));
								playerIn.sendMessage(comp);
							}
						}
					}
				} else {
					StringTextComponent comp = new StringTextComponent(TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.creator_null.name"));
					playerIn.sendMessage(comp);
				}
			}
		} else {
			StringTextComponent comp = new StringTextComponent(TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.null.name"));
			playerIn.sendMessage(comp);
		}
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) { }

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (!worldIn.isRemote) {
			if (stack.hasTag() && placer.dimension != ModDimensionManager.POCKET_DIMENSION.getDimensionType()) {
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
	
					PocketRegistryManager.updatePocket(chunkSet, placer.dimension, getPos());
	
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
				pocket.generatePocket(placer.getDisplayName().getString());
				//NetworkHandler.sendPocketSetCreator(placer.getName(), pos);
				//ChunkLoaderManagerRoom.addPocketToChunkLoader(pocket);
				//ChunkLoaderManagerBlock.addPocketBlockToChunkLoader(this.getPos(), placer.dimension.getId());

			} else if (stack.hasTag() && placer.dimension == ModDimensionManager.POCKET_DIMENSION.getDimensionType()) {
				if (placer.dimension == ModDimensionManager.POCKET_DIMENSION.getDimensionType()) {
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
									DimUtils.spawnItemStack(this.generateItemStackWithNBT(pos, X, Y, Z), world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0);
									
									world.setBlockState(pos, Blocks.AIR.getDefaultState());
								}
							} else {
								PocketRegistryManager.updatePocket(chunk_set, placer.dimension, getPos());
	
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
								pocket.generatePocket(placer.getDisplayName().getString());
								//NetworkHandler.sendPocketSetCreator(placer.getName(), pos);
								//ChunkLoaderManagerRoom.addPocketToChunkLoader(pocket);
								//ChunkLoaderManagerBlock.addPocketBlockToChunkLoader(this.getPos(), placer.dimension.getId());
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) { }

	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) { }

	@Override
	public int getEnergyScaled(int scale) {
		return 0;
	}

	@Override
	public boolean hasEnergy() {
		return false;
	}
	
	public TileEntityType<?> getType() {
		return TileEntityManager.POCKET;
	}
}