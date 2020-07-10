package com.zeher.dimensionalpockets.pocket.tileentity;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.handler.BlockHandler;
import com.zeher.dimensionalpockets.core.handler.ItemHandler;
import com.zeher.dimensionalpockets.core.handler.SoundHandler;
import com.zeher.dimensionalpockets.core.util.DimUtils;
import com.zeher.dimensionalpockets.pocket.Pocket;
import com.zeher.dimensionalpockets.pocket.PocketRegistry;
import com.zeher.dimensionalpockets.pocket.handler.PocketChunkLoaderHandler;
import com.zeher.zeherlib.api.client.util.TextHelper;
import com.zeher.zeherlib.api.compat.client.interfaces.IClientUpdatedTile;
import com.zeher.zeherlib.api.compat.core.interfaces.EnumSideState;
import com.zeher.zeherlib.api.compat.core.interfaces.ISidedTile;
import com.zeher.zeherlib.api.core.interfaces.block.IBlockInteract;
import com.zeher.zeherlib.api.core.interfaces.block.IBlockNotifier;
import com.zeher.zeherlib.mod.util.ModUtil;

import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityDimensionalPocket extends TileEntityDim implements IBlockNotifier, IBlockInteract, ITickable, ISidedInventory, IInventory, ISidedTile, IEnergyReceiver, IEnergyProvider, IClientUpdatedTile.Storage {
	
	private static final String TAG_CUSTOM_DP_NAME = "customDPName";
	private String customName;
	
	public EnumSideState[] SIDE_STATE_ARRAY = EnumSideState.getStandardArray();

	private NonNullList<ItemStack> INVENTORY_STACKS = NonNullList.<ItemStack>withSize(15, ItemStack.EMPTY);
	
	@SideOnly(Side.CLIENT)
	private Pocket pocket;
	
	@Override
	public void update() { 
		for (int i = 0 ; i < 9; i++) {
			if (this.getStackInSlot(i) != this.getPocket().items.get(i)) {
				ItemStack pocket_stack = this.getPocket().items.get(i);
				ItemStack copy = pocket_stack.copy();
				if (!this.world.isRemote) {
					this.INVENTORY_STACKS.set(i, copy);
				}
			}
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		if (world.isRemote) {
			return;
		}
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		Pocket p = getPocket();
		if (p != null) {
			for (EnumFacing side : EnumFacing.VALUES) {
				// p.markConnectorForUpdate(side);
			}
		}
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) { }

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (!worldIn.isRemote) {
			if (stack.hasTagCompound() && placer.dimension != DimensionalPockets.DIMENSION_ID) {
				NBTTagCompound compound = stack.getTagCompound();
				
				if (compound.hasKey("nbt_data")) {
					NBTTagCompound nbt_tag = compound.getCompoundTag("nbt_data");
					
					int X = nbt_tag.getCompoundTag("chunk_set").getInteger("X");
					int Y = nbt_tag.getCompoundTag("chunk_set").getInteger("Y");
					int Z = nbt_tag.getCompoundTag("chunk_set").getInteger("Z");
	
					BlockPos chunkSet = new BlockPos(X, Y, Z);
					boolean success = PocketRegistry.getPocket(chunkSet) != null;
	
					if (!success) {
						throw new RuntimeException("YOU DESERVED THIS!");
					}
	
					PocketRegistry.updatePocket(chunkSet, placer.dimension, getPos());
	
					if (nbt_tag.hasKey("display")) {
						String tempString = nbt_tag.getCompoundTag("display").getString("Name");
						if (!tempString.isEmpty()) {
							customName = tempString;
						}
					}
					
					NBTTagCompound side_tag = nbt_tag.getCompoundTag("sides");
					
					for(EnumFacing c : EnumFacing.VALUES) {
						this.setSide(c, EnumSideState.getStateFromIndex(side_tag.getInteger("index_" + c.getIndex())));
					}
				}

				if (placer instanceof EntityPlayer) {
					EntityPlayerMP player = (EntityPlayerMP) placer;
					if (player.getHeldItemMainhand() == stack) {
						player.getHeldItemMainhand().setCount(0);
					}
				}

				Pocket pocket = this.getPocket();
				pocket.generatePocket(placer.getName());
				//NetworkHandler.sendPocketSetCreator(placer.getName(), pos);
				PocketChunkLoaderHandler.addPocketToChunkLoader(pocket);

			} else if (stack.hasTagCompound() && placer.dimension == DimensionalPockets.DIMENSION_ID) {
				if (placer.dimension == DimensionalPockets.DIMENSION_ID) {
					NBTTagCompound stack_tag = stack.getTagCompound();

					if (stack_tag.hasKey("nbt_data")) {
						NBTTagCompound nbt_tag = stack_tag.getCompoundTag("nbt_data");
						
						int X = nbt_tag.getCompoundTag("chunk_set").getInteger("X");
						int Y = nbt_tag.getCompoundTag("chunk_set").getInteger("Y");
						int Z = nbt_tag.getCompoundTag("chunk_set").getInteger("Z");
						
						BlockPos chunk_set = new BlockPos(X, Y, Z);
	
						Pocket test_pocket = PocketRegistry.getPocket(chunk_set);
	
						if (test_pocket != null) {
							if (test_pocket.equals(PocketRegistry
									.getPocket(new BlockPos(pos.getX() >> 4, pos.getY() >> 4, pos.getZ() >> 4)))) {
								if (!world.isRemote) {
									DimUtils.spawnItemStack(this.generateItemStackWithNBT(pos, X, Y, Z), world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0);
									world.setBlockToAir(pos);
								}
	
							} else {
								PocketRegistry.updatePocket(chunk_set, placer.dimension, getPos());
	
								if (nbt_tag.hasKey("display")) {
									String tempString = nbt_tag.getCompoundTag("display").getString("Name");
									if (!tempString.isEmpty()) {
										customName = tempString;
									}
								}
	
								if (placer instanceof EntityPlayer) {
									EntityPlayerMP player = (EntityPlayerMP) placer;
									if (player.getHeldItemMainhand() == stack) {
										player.getHeldItemMainhand().setCount(0);
									}
								}
	
								Pocket pocket = this.getPocket();
								pocket.generatePocket(placer.getName());
								//NetworkHandler.sendPocketSetCreator(placer.getName(), pos);
								PocketChunkLoaderHandler.addPocketToChunkLoader(pocket);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		if (this.getPocket() != null) {
			if (ModUtil.isHoldingHammer(playerIn) && playerIn.isSneaking() && !ModUtil.isHoldingItem(playerIn, ItemHandler.DIMENSIONAL_KEY)) {
				if(this.getPocket().getCreator() != null) {
					String creator = this.getPocket().getCreator();
					
					if (creator != null) {
						if (playerIn.getName().equals(creator)) {
							if (!(worldIn.isRemote)) {
								FMLNetworkHandler.openGui(playerIn, DimensionalPockets.INSTANCE, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
							}
						} else {
							if (!(worldIn.isRemote)) {
								TextComponentString comp = new TextComponentString(TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.access_set.name"));
								playerIn.sendMessage(comp);
							}
						}
					}
				} else {
					TextComponentString comp = new TextComponentString(TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.creator_null.name"));
					playerIn.sendMessage(comp);
				}
			} else if ((ModUtil.isHoldingItem(playerIn, ItemHandler.DIMENSIONAL_KEY) && playerIn.isSneaking())) {
				String creator = this.getPocket().getCreator();
				if (playerIn.getName().equals(creator)) {
					
					this.setLockState(!this.getLockState());
					this.sendUpdates();
					
					if (!world.isRemote) {
						if (this.getLockState()) {
							TextComponentString comp = new TextComponentString(TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.locked_.name"));
							playerIn.sendMessage(comp);	
						} else {
							TextComponentString comp = new TextComponentString(TextHelper.GREEN + TextHelper.BOLD + I18n.format("pocket.status.unlocked_.name"));
							playerIn.sendMessage(comp);	
						}
					}
					
					this.markDirty();
				} else {
					if (!(worldIn.isRemote)) {
						TextComponentString comp = new TextComponentString(TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.access_lock.name"));
						playerIn.sendMessage(comp);
					}
				}
			}
		} else {
			TextComponentString comp = new TextComponentString(TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.null.name"));
			playerIn.sendMessage(comp);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.isSneaking() && playerIn.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
			if (this.getPocket() != null) {
				Pocket pocket = this.getPocket();
				
				if (pocket.getCreator() == null) {
					pocket.setCreator(playerIn.getName());
					//NetworkHandler.sendCreatorPacketToServer(playerIn.getName(), pos);
					this.markDirty();
				} else { 
					String creator = pocket.getCreator();
	
					String player_name = playerIn.getName();
					
					this.markDirty();
					
					if (creator != null) {
						if (this.getLockState()) {
							if (player_name.equals(creator)) {
								playerIn.playSound(SoundHandler.GENERIC.PORTAL_IN, 1.0F, 1.0F);
								this.shiftIntoPocket(playerIn, pos);
							} else if (pocket.checkPlayerInList(player_name)) {
								playerIn.playSound(SoundHandler.GENERIC.PORTAL_IN, 1.0F, 1.0F);
								this.shiftIntoPocket(playerIn, pos);
							} else {
								TextComponentString comp = new TextComponentString(TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.is_locked.name") + "[" + creator + "]");
								playerIn.sendMessage(comp);
							}
						} else {
							playerIn.playSound(SoundHandler.GENERIC.PORTAL_IN, 1.0F, 1.0F);
							this.shiftIntoPocket(playerIn, pos);
						}
					}
				}
			}
		} else if (!playerIn.isSneaking() && playerIn.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
			String creator = this.getPocket().getCreator();
			String locked_comp;

			if (this.getLockState()) {
				locked_comp = TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.locked.name");
			} else {
				locked_comp = TextHelper.GREEN + TextHelper.BOLD + I18n.format("pocket.status.unlocked.name");
			}

			if (!worldIn.isRemote) {
				TextComponentString comp = new TextComponentString(TextHelper.LIGHT_GRAY + TextHelper.BOLD + I18n.format("pocket.status.owner.name") + TextHelper.PURPLE + TextHelper.BOLD + " {" + creator + "} " + TextHelper.LIGHT_GRAY + TextHelper.BOLD + I18n.format("pocket.status.and.name") + locked_comp + ".");
				playerIn.sendMessage(comp);
				
				return false;
			}
		} else if ((ModUtil.isHoldingHammer(playerIn)) && (playerIn.isSneaking())) {
			playerIn.swingArm(EnumHand.MAIN_HAND);
			if (!(worldIn.isRemote)) {

				String creator = this.getPocket().getCreator();

				if (creator != null) {
					if (playerIn.getName().equals(creator)) {
						if (this.getLockState()) {
							TextComponentString comp = new TextComponentString(TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.remove_locked.name"));
							playerIn.sendMessage(comp);
						} else {
							DimUtils.spawnItemStack(this.generateItemStackOnRemoval(pos), world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0);
							world.setBlockToAir(pos);
						}
					} else {
						TextComponentString comp = new TextComponentString(TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.remove_not.name"));
						playerIn.sendMessage(comp);
					}
				}

				return true;
			}
		} else if ((ModUtil.isHoldingHammer(playerIn)) && !(playerIn.isSneaking())) {
			playerIn.swingArm(EnumHand.MAIN_HAND);
			
			String creator = this.getPocket().getCreator();

			if (creator != null) {
				if (playerIn.getName().equals(creator)) {
					this.cycleSide(facing);
					this.markDirty();
					
					if (!(worldIn.isRemote)) {
						TextComponentString comp = new TextComponentString(TextHelper.LIGHT_GRAY + TextHelper.BOLD + I18n.format("pocket.status.cycle_side.name") + this.getSide(facing).getTextColour() + TextHelper.BOLD + " [" + this.getSide(facing).getDisplayName() + "]");
						playerIn.sendMessage(comp);
					}
					return false;
				} else {
					if (!(worldIn.isRemote)) {
						TextComponentString comp = new TextComponentString(TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.access_lock.name"));
						playerIn.sendMessage(comp);
					}
				}
			}
		}
		return false;
	}

	public ItemStack generateItemStackOnRemoval(BlockPos pos) {
		ItemStack itemStack = new ItemStack(BlockHandler.BLOCK_DIMENSIONAL_POCKET);

		if (!itemStack.hasTagCompound()) {
			itemStack.setTagCompound(new NBTTagCompound());
		}

		BlockPos chunkSet = this.getPocket().getChunkPos();
		
		NBTTagCompound compound = new NBTTagCompound();
		
		//Saves the chunk data to NBT
		NBTTagCompound chunk_tag = new NBTTagCompound();
		chunk_tag.setInteger("X", chunkSet.getX());
		chunk_tag.setInteger("Y", chunkSet.getY());
		chunk_tag.setInteger("Z", chunkSet.getZ());

		compound.setTag("chunk_set", chunk_tag);

		String creatorLore = null;
		Pocket pocket = getPocket();
		
		if (pocket != null && pocket.getCreator() != null) {
			creatorLore = TextHelper.LIGHT_BLUE + TextHelper.BOLD + I18n.format("pocket.desc.creator.name") + TextHelper.PURPLE + TextHelper.BOLD + " [" + pocket.getCreator() + "]";
		}
		
		//Saves the side data to NBT
		if (this instanceof ISidedTile) {
			NBTTagCompound side_tag = new NBTTagCompound();
			
			for (EnumFacing c : EnumFacing.VALUES) {
				side_tag.setInteger("index_" + c.getIndex(), this.getSideArray()[c.getIndex()].getIndex());
			}
			
			compound.setTag("sides", side_tag);
		}
		
		if (this.getEnergyStored(EnumFacing.DOWN) > 0) {
			compound.setInteger("energy", this.getEnergyStored(EnumFacing.DOWN));
		}
		
		itemStack.getTagCompound().setTag("nbt_data", compound);
		
		BlockPos blockSet = new BlockPos(chunkSet.getX() << 4, chunkSet.getY() << 4, chunkSet.getZ() << 4);

		itemStack = DimUtils.generateItem(itemStack, customName, false, TextHelper.LIGHT_GRAY + TextHelper.BOLD
				+ "Pocket: [" + blockSet.getX() + " | " + blockSet.getY() + " | " + blockSet.getZ() + "]", creatorLore);
		return itemStack;
	}

	public ItemStack generateItemStackWithNBT(BlockPos pos, int x, int y, int z) {
		ItemStack item_stack = new ItemStack(BlockHandler.BLOCK_DIMENSIONAL_POCKET);

		if (!item_stack.hasTagCompound()) {
			item_stack.setTagCompound(new NBTTagCompound());
		}

		NBTTagCompound compound = new NBTTagCompound();
		
		NBTTagCompound chunk_tag = new NBTTagCompound();
		chunk_tag.setInteger("X", x);
		chunk_tag.setInteger("Y", y);
		chunk_tag.setInteger("Z", z);

		compound.setTag("chunk_set", chunk_tag);
		
		item_stack.getTagCompound().setTag("nbt_data", compound);

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
		return PocketRegistry.getOrCreatePocket(this.world, this.getPos());
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		Pocket pocket = this.getPocket();
		
		if (this.SIDE_STATE_ARRAY != null) {
			NBTTagCompound sides_tag = new NBTTagCompound();
			
			for (EnumFacing c : EnumFacing.VALUES) {
				int i = this.getSide(c).getIndex();
				
				sides_tag.setInteger(c.getName(), i);
			}
			compound.setTag("sides", sides_tag);
		}
		
		if (pocket != null) {
			this.getPocket().writeToNBT(compound);
		}
		
		if (customName != null) {
			compound.setString(TAG_CUSTOM_DP_NAME, customName);
		}
		
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		if (compound.hasKey("sides")) {
			NBTTagCompound sides_tag = compound.getCompoundTag("sides");
			
			for (EnumFacing c : EnumFacing.VALUES) {
				this.setSide(c, EnumSideState.getStateFromIndex(sides_tag.getInteger(c.getName())));
			}
		}
		
		
		if (this.getWorld() != null && this.getWorld().isRemote) {
			pocket = Pocket.readFromNBT(compound);
		}
		
		
		String tempString = compound.getString(TAG_CUSTOM_DP_NAME);
		if (!tempString.isEmpty()) {
			customName = tempString;
		}
	}

	private void shiftIntoPocket(EntityPlayer player, BlockPos pos_) {
		this.getPocket().addBlockPosToArray(this.getPos());
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
	public EnumSideState getSide(EnumFacing facing) {
		return SIDE_STATE_ARRAY[facing.getIndex()];
	}
	
	@Override
	public void setSide(EnumFacing facing, EnumSideState side_state) {
		SIDE_STATE_ARRAY[facing.getIndex()] = side_state;
		
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
	public void cycleSide(EnumFacing facing) {
		EnumSideState state = SIDE_STATE_ARRAY[facing.getIndex()];
		
		EnumSideState state2 = state.getNextState();
		
		SIDE_STATE_ARRAY[facing.getIndex()] = state2;
		
		this.sendUpdates();
	}

	@Override
	public boolean canConnect(EnumFacing facing) {
		EnumSideState state = SIDE_STATE_ARRAY[facing.getIndex()];
		
		if (state.equals(EnumSideState.DISABLED)) {
			return false;
		}
		return true;
	}

	@Override
	public void sendUpdates() {
		this.world.markBlockRangeForRenderUpdate(this.getPos(), this.getPos());
		this.world.notifyBlockUpdate(this.getPos(), world.getBlockState(this.getPos()), world.getBlockState(this.getPos()), 3);
		this.world.scheduleBlockUpdate(this.getPos(), this.getBlockType(), 0, 0);		
		this.markDirty();
	}
	
	/**
	 * Set the data once it has been received. [NBT > TE]
	 */
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		System.out.println("READ TAG: index_1/up " + tag.getInteger("index_1"));

		this.readFromNBT(tag);
	}
	
	/**
	 * Retrieve the data to be stored. [TE > NBT]
	 */
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = new NBTTagCompound();
		
		System.out.println("WRITE TAG: index_1/up " + this.getSide(EnumFacing.UP).getIndex());
		
		this.writeToNBT(tag);
		
		return tag;
	}
	
	/**
	 * Actually sends the data to the server. [NBT > SER]
	 */
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.getPos(), 3, this.getUpdateTag());
	}
	
	/**
	 * Method is called once packet has been received by the client. [SER > CLT]
	 */
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		NBTTagCompound tag_ = pkt.getNbtCompound();
		
		this.handleUpdateTag(tag_);
		this.sendUpdates();
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getField(int id) {
		switch (id) {
		case 1:
			return this.getEnergyStored(EnumFacing.DOWN);
		default:
			return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		/**
		switch (id) {
		case 1:
			this.setEnergy(value);
			break;
		}
		*/
	}

	@Override
	public int getFieldCount() {
		return 1;
	}

	@Override
	public int getEnergyStored(EnumFacing from) {
		return this.getPocket().getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return this.getPocket().getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		if (this.getSide(from) == EnumSideState.DISABLED) {
			return false;
		}
		return true;
	}
	
	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		if (this.getSide(from).equals(EnumSideState.INTERFACE_INPUT)) {

			int receive = this.getPocket().receiveEnergy(maxReceive, simulate);
			
			this.markDirty();
			return receive;
		}
		return 0;
	}
	
	public void setEnergy(int set) {
		this.markDirty();
		this.getPocket().setEnergyStored(set);
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		if (this.getSide(from).equals(EnumSideState.INTERFACE_OUTPUT)) {
			this.markDirty();
			return this.getPocket().extractEnergy(maxExtract, simulate);
		}
		return 0;
	}

	@Override
	public int getEnergyScaled(int scale) {
		return this.getEnergyStored(EnumFacing.DOWN) * scale / this.getMaxEnergyStored(EnumFacing.DOWN);
	}

	@Override
	public boolean hasEnergy() {
		return this.getEnergyStored(EnumFacing.DOWN) > 0;
	}
	
	@Override
	public int getSizeInventory() {
		/**return this.INVENTORY_STACKS.size() + */ return this.INVENTORY_STACKS.size();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		/**
		if (index > 8) {
			return this.INVENTORY_STACKS.get(index);
		} else {*/
		return this.INVENTORY_STACKS.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		/**
		if (index > 8) {
			return ItemStackHelper.getAndSplit(this.INVENTORY_STACKS, index, count);
		} else {*/

		this.markDirty();
		//ItemStackHelper.getAndSplit(this.getPocket().items, index, count);
		return ItemStackHelper.getAndSplit(this.INVENTORY_STACKS, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		/**
		if (index > 8) {
			return ItemStackHelper.getAndRemove(this.INVENTORY_STACKS, index);
		} else {*/
			//this.sendUpdates();
		this.markDirty();
		//ItemStackHelper.getAndRemove(this.getPocket().items, index);
		return ItemStackHelper.getAndRemove(this.INVENTORY_STACKS, index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		/**
		if (index > 8) {
			ItemStack itemstack = (ItemStack) this.INVENTORY_STACKS.get(index);
			this.INVENTORY_STACKS.set(index, stack);
			if (stack.getCount() > this.getInventoryStackLimit()) {
				stack.setCount(this.getInventoryStackLimit());
			}
		} else { */
		
		//this.sendUpdates();
		ItemStack itemstack = (ItemStack) this.INVENTORY_STACKS.get(index);
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
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) { }

	@Override
	public void closeInventory(EntityPlayer player) { }

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}
	
	@Override
	public void clear() { }

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] { 0 };
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
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
}