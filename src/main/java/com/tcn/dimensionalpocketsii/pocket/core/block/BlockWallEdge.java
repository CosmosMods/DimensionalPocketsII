package com.tcn.dimensionalpocketsii.pocket.core.block;

import javax.annotation.Nonnull;

import com.tcn.cosmoslibrary.impl.block.CosmosBlockConnectedUnbreakable;
import com.tcn.cosmoslibrary.impl.colour.ChatColour;
import com.tcn.cosmoslibrary.impl.util.CosmosChatUtil;
import com.tcn.dimensionalpocketsii.core.management.CoreConfigurationManager;
import com.tcn.dimensionalpocketsii.core.management.CoreDimensionManager;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.util.PocketUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.network.DebugPacketSender;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockWallEdge extends CosmosBlockConnectedUnbreakable {

	public BlockWallEdge(Block.Properties prop) {
		super(prop);
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		if (PocketUtil.isDimensionEqual(worldIn, CoreDimensionManager.POCKET_WORLD)) {
			Pocket pocket = PocketRegistryManager.getPocketFromChunk(PocketUtil.scaleToChunkPos(pos));
			
			if (!worldIn.isRemote) {
				if (pocket != null) {
					if (playerIn.isSneaking()) {
						if (playerIn.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
							if (!worldIn.isRemote) {
								pocket.shift(playerIn, EnumShiftDirection.LEAVE, null, null);
								return ActionResultType.SUCCESS;
							}
						} 
					}
				} else {
					CosmosChatUtil.sendPlayerMessage(playerIn, false, ChatColour.RED + "Unable to shift to complete action. Pocket is null.");
					return ActionResultType.FAIL;
				}
			}
		}
		return ActionResultType.FAIL;
	}
	
	@Override
	public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
		return false;
    }
	
	@Override
	protected boolean canConnect(@Nonnull BlockState orig, @Nonnull BlockState conn) {
		if (CoreConfigurationManager.getInstance().getConnectedTexturesInsidePocket()) {
			if (conn.getBlock().equals(Blocks.AIR)) {
				return false;
			} else if (orig.getBlock().equals(conn.getBlock())) {
				return true;
			} else if (conn.getBlock().equals(ModBusManager.BLOCK_WALL)) {
				return true;
			} else if (conn.getBlock().equals(ModBusManager.BLOCK_WALL_CONNECTOR)) {
				return true;
			} else if (conn.getBlock().equals(ModBusManager.BLOCK_WALL_CHARGER)) {
				return true;
			} 
			
			
			else {
				return false;
			}
			
			/*else if (conn.getBlock().equals(BlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_ENERGY_DISPLAY)) {
				return true;
			} else if (conn.getBlock().equals(BlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_FLUID_DISPLAY)) {
				return true;
			}*/
		}
		return false;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
		return true;
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		DebugPacketSender.func_218806_a(worldIn, pos);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		if (CoreConfigurationManager.getInstance().getCanDestroyWalls()) {
			return this.getDefaultState();
		}
		return Blocks.AIR.getDefaultState();
	}
}