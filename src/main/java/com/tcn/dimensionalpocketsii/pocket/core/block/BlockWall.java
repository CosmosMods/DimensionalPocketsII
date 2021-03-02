package com.tcn.dimensionalpocketsii.pocket.core.block;

import com.tcn.cosmoslibrary.client.impl.util.TextHelper;
import com.tcn.cosmoslibrary.impl.block.CosmosBlockUnbreakable;
import com.tcn.cosmoslibrary.impl.enums.EnumConnectionType;
import com.tcn.cosmoslibrary.impl.util.CosmosChatUtil;
import com.tcn.cosmoslibrary.impl.util.CosmosUtil;
import com.tcn.dimensionalpocketsii.core.management.CoreConfigurationManager;
import com.tcn.dimensionalpocketsii.core.management.CoreDimensionManager;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityConnector;
import com.tcn.dimensionalpocketsii.pocket.core.util.PocketUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.network.DebugPacketSender;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockWall extends CosmosBlockUnbreakable {
	
	public BlockWall(Block.Properties prop) {
		super(prop);
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		if (PocketUtil.isDimensionEqual(worldIn, CoreDimensionManager.POCKET_WORLD)) {
			Pocket pocket = PocketRegistryManager.getPocketFromChunk(PocketUtil.scaleToChunkPos(pos));
			
			if (pocket != null) {
				if (!playerIn.isSneaking()) {
					if (CosmosUtil.isHoldingHammer(playerIn)) {
						if (pos.getY() != 0 && pos.getY() != 15) {
							if (CosmosUtil.isHoldingHammer(playerIn)) {
								worldIn.removeBlock(pos, false);
								worldIn.setBlockState(pos, ModBusManager.BLOCK_WALL_CHARGER.getDefaultState());
								
								return ActionResultType.SUCCESS;
							}
						}
					}
				} else {
					if (CosmosUtil.isHoldingHammer(playerIn)) {
						worldIn.setBlockState(pos, ModBusManager.BLOCK_WALL_CONNECTOR.getDefaultState());
						worldIn.setTileEntity(pos, new TileEntityConnector());
						pocket.updateConnectorInArray(pos, EnumConnectionType.getStandardValue());
						
						return ActionResultType.SUCCESS;
					}
					
					else if (playerIn.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
						if (!worldIn.isRemote) {
							pocket.shift(playerIn, EnumShiftDirection.LEAVE, null, null);
							return ActionResultType.SUCCESS;
						}
					}
				}
			} else {
				CosmosChatUtil.sendPlayerMessage(playerIn, false, TextHelper.RED + "Unable to complete action. Pocket is null.");
				return ActionResultType.FAIL;
			}
		}
		return ActionResultType.FAIL;
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