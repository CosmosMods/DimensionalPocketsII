package com.tcn.dimensionalpocketsii.pocket.core.blockentity;

import com.tcn.cosmoslibrary.common.blockentity.CosmosBlockEntityUpdateable;
import com.tcn.cosmoslibrary.common.chat.CosmosChatUtil;
import com.tcn.cosmoslibrary.common.interfaces.block.IBlockInteract;
import com.tcn.cosmoslibrary.common.lib.ComponentHelper;
import com.tcn.cosmoslibrary.common.lib.CosmosChunkPos;
import com.tcn.cosmoslibrary.common.util.CosmosUtil;
import com.tcn.dimensionalpocketsii.core.management.ObjectManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.util.PocketUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class BlockEntityZModuleCreativeFluid extends CosmosBlockEntityUpdateable implements IBlockInteract {
	
	private Pocket pocket;
	private int update = 0;
	
	public BlockEntityZModuleCreativeFluid(BlockPos posIn, BlockState stateIn) {
		super(ObjectManager.tile_entity_creative_fluid, posIn, stateIn);
	}
	
	public Pocket getPocket() {
		if (level.isClientSide) {
			return this.pocket;
		}
		
		return PocketRegistryManager.getPocketFromChunkPosition(CosmosChunkPos.scaleToChunkPos(this.getBlockPos()));
	}

	public void sendUpdates(boolean update) {
		super.sendUpdates(update);
	}

	@Override
	public void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		
		if (this.getPocket() != null) {
			this.getPocket().writeToNBT(compound);
		}
	}
	
	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		
		if (PocketUtil.hasPocketKey(compound)) {
			this.pocket = Pocket.readFromNBT(compound);
		}
	}
	
	/**
	 * Set the data once it has been received. [NBT > TE] (READ)
	 */
	@Override
	public void handleUpdateTag(CompoundTag tag) {
		this.load(tag);
	}
	
	/**
	 * Retrieve the data to be stored. [TE > NBT] (WRITE)
	 */
	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = new CompoundTag();
		this.saveAdditional(tag);
		return tag;
	}
	
	/**
	 * Actually sends the data to the server. [NBT > SER]
	 */
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	/**
	 * Method is called once packet has been received by the client. [SER > CLT]
	 */
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		super.onDataPacket(net, pkt);
		CompoundTag tag_ = pkt.getTag();
		this.handleUpdateTag(tag_);
		this.sendUpdates(true);
	}
	
	@Override
	public void onLoad() { }

	public static void tick(Level levelIn, BlockPos posIn, BlockState stateIn, BlockEntityZModuleCreativeFluid entityIn) {
		Pocket pocket = entityIn.getPocket();
		
		if (pocket != null) {
			FluidTank tank = pocket.getFluidTank();
			if (!tank.isEmpty()) {
				pocket.fill(new FluidStack(tank.getFluid().getFluid(), tank.getCapacity() - tank.getFluidAmount()), FluidAction.EXECUTE);
			}
		}
		
		boolean flag = entityIn.update > 0;

		if (flag) {
			entityIn.update--;
		} else {
			entityIn.update = 100;

			entityIn.sendUpdates(true);
		}
	}

	@Override
	public void attack(BlockState state, Level worldIn, BlockPos pos, Player player) { }
	
	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand handIn, BlockHitResult hit) {
		if (playerIn.isShiftKeyDown()) {
			CosmosChunkPos chunkPos = CosmosChunkPos.scaleToChunkPos(pos);
			Pocket pocketIn = PocketRegistryManager.getPocketFromChunkPosition(chunkPos);
			
			if(pocketIn.exists()) {
				if (CosmosUtil.holdingWrench(playerIn)) {
					if (pocketIn.checkIfOwner(playerIn)) {
						ItemStack stack = new ItemStack(ObjectManager.module_creative_fluid);
						
						worldIn.setBlockAndUpdate(pos, ObjectManager.block_wall.defaultBlockState());
						worldIn.removeBlockEntity(pos);
						
						CosmosUtil.addStack(worldIn, playerIn, stack);
						
						return InteractionResult.SUCCESS;
					} else {
						CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.no_access"));
						return InteractionResult.FAIL;
					}
				} else if (CosmosUtil.handEmpty(playerIn)) {
					pocketIn.shift(playerIn, EnumShiftDirection.LEAVE, null, null, null);
					return InteractionResult.SUCCESS;
				}
			} else {
				CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.null"));
				return InteractionResult.FAIL;
			}
		}
		return InteractionResult.FAIL;
	}
}