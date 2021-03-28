package com.tcn.dimensionalpocketsii.pocket.core.tileentity;

import com.tcn.cosmoslibrary.common.chat.CosmosChatUtil;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.cosmoslibrary.common.interfaces.block.IBlockInteract;
import com.tcn.cosmoslibrary.common.math.ChunkPos;
import com.tcn.cosmoslibrary.common.util.CosmosUtil;
import com.tcn.dimensionalpocketsii.core.management.CoreModBusManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.util.PocketUtil;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class TileEntityCrafter extends TileEntity implements IBlockInteract, ITickableTileEntity {

	private Pocket pocket;
	
	public TileEntityCrafter() {
		super(CoreModBusManager.CRAFTER_TILE_TYPE);
	}
	
	public Pocket getPocket() {
		if (level.isClientSide) {
			return this.pocket;
		}
		
		return PocketRegistryManager.getPocketFromChunkPosition(ChunkPos.scaleToChunkPos(this.getBlockPos()));
	}

	public void updateRenderers() {
		this.level.sendBlockUpdated(this.getBlockPos(), level.getBlockState(this.getBlockPos()), level.getBlockState(this.getBlockPos()), 3);
		//this.level.markBlockRangeForRenderUpdate(this.getBlockPos(), this.world.getBlockState(this.getBlockPos()), this.level.getBlockState(this.getBlockPos()));
		this.setChanged();
	}
	
	@Override
	public void tick() { }
	
	public void sendUpdates(boolean update) {
		this.setChanged();
		
		if (update) {
			if (!level.isClientSide) {
				
				level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState());
			}
		}
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		
		if (this.getPocket().exists()) {
			this.getPocket().writeToNBT(compound);
		}
		
		return compound;
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);

		if (PocketUtil.hasPocketKey(compound)) {
			this.pocket = Pocket.readFromNBT(compound);
		}
	}
	
	/**
	 * Set the data once it has been received. [NBT > TE] (READ)
	 */
	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		this.load(state, tag);
		
		this.sendUpdates(true);
	}
	
	/**
	 * Retrieve the data to be stored. [TE > NBT] (WRITE)
	 */
	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT tag = new CompoundNBT();
		
		this.save(tag);
		
		return tag;
	}
	
	/**
	 * Actually sends the data to the server. [NBT > SER]
	 */
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.getBlockPos(), 0, this.getUpdateTag());
	}
	
	/**
	 * Method is called once packet has been received by the client. [SER > CLT]
	 */
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		super.onDataPacket(net, pkt);
		CompoundNBT tag_ = pkt.getTag();

		BlockState state = level.getBlockState(pkt.getPos());
		
		this.handleUpdateTag(state, tag_);
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		worldIn.sendBlockUpdated(pos, state, state, 3);
		this.setChanged();
		this.sendUpdates(true);
		
		if (CosmosUtil.getStackItem(playerIn) instanceof BlockItem) {
			return ActionResultType.FAIL;
		}
		
		if (!playerIn.isShiftKeyDown()) {
			if (worldIn.isClientSide) {
				return ActionResultType.SUCCESS;
			} else {
				if (playerIn instanceof ServerPlayerEntity) {
					NetworkHooks.openGui((ServerPlayerEntity)playerIn, state.getMenuProvider(worldIn, pos), (packetBuffer)->{ packetBuffer.writeBlockPos(pos); });
					return ActionResultType.SUCCESS;
				}
			}
		} else {
			if(!worldIn.isClientSide) {
				ChunkPos chunkPos = ChunkPos.scaleToChunkPos(pos);
				Pocket pocketIn = PocketRegistryManager.getPocketFromChunkPosition(chunkPos);
				
				if(pocketIn.exists()) {
					if (CosmosUtil.holdingWrench(playerIn)) {
						if (pocketIn.checkIfOwner(playerIn)) {
							worldIn.setBlockAndUpdate(pos, CoreModBusManager.BLOCK_WALL.defaultBlockState());
							
							if (!playerIn.isCreative()) {
								CosmosUtil.addItem(playerIn, CoreModBusManager.MODULE_CRAFTER, 1);
							}
							
							return ActionResultType.SUCCESS;
						} else {
							CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.pocket.status.no_access"));
							return ActionResultType.FAIL;
						}
					} 
					
					else if (CosmosUtil.handEmpty(playerIn)) {
						pocketIn.shift(playerIn, EnumShiftDirection.LEAVE, null, null);
						return ActionResultType.SUCCESS;
					}
				}
			}
		}
		
		return ActionResultType.SUCCESS;
	}

	@Override
	public void attack(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) { }
}