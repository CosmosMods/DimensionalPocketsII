package com.tcn.dimensionalpocketsii.pocket.core.block;

import java.util.Random;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.common.interfaces.IBlankCreativeTab;
import com.tcn.dimensionalpocketsii.core.management.ConfigurationManager;
import com.tcn.dimensionalpocketsii.core.management.ObjectManager;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleFurnace;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleFurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockWallFurnace extends BlockWallModule implements IBlankCreativeTab, EntityBlock {
	private static final Component CONTAINER_NAME = Component.translatable("dimensionalpocketsii.gui.furnace");
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	
	public BlockWallFurnace(Block.Properties prop) {
		super(prop);
		
		this.registerDefaultState(this.defaultBlockState().setValue(LIT, false));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos posIn, BlockState stateIn) {
		return new BlockEntityModuleFurnace(posIn, stateIn);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level levelIn, BlockState stateIn, BlockEntityType<T> entityTypeIn) {
		return createTicker(levelIn, entityTypeIn, ObjectManager.tile_entity_furnace);
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createTicker(Level levelIn, BlockEntityType<T> entityTypeIn, BlockEntityType<? extends BlockEntityModuleFurnace> entityIn) {
		return createTickerHelper(entityTypeIn, entityIn, BlockEntityModuleFurnace::tick);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand handIn, BlockHitResult hit) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		
		if (tileEntity instanceof BlockEntityModuleFurnace) {
			return ((BlockEntityModuleFurnace) tileEntity).use(state, worldIn, pos, playerIn, handIn, hit);
		}
		return InteractionResult.PASS;
	}
	
	@Override
	public void attack(BlockState state, Level world, BlockPos pos, Player player) {
		BlockEntity tileEntity = world.getBlockEntity(pos);
		
		if (tileEntity instanceof BlockEntityModuleFurnace) {
			((BlockEntityModuleFurnace) tileEntity).attack(state, world, pos, player);
		}
	}

	
	@Override
	public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		
		if (tileEntity instanceof BlockEntityModuleFurnace) {
			BlockEntityModuleFurnace tileFurnace = (BlockEntityModuleFurnace) tileEntity;
			return new SimpleMenuProvider((id, inventory, player) -> { return new ContainerModuleFurnace(id, inventory, tileFurnace, tileFurnace.dataAccess, pos); }, CONTAINER_NAME);
		}
		return null;
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateContainer) {
		stateContainer.add(LIT);
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level worldIn, BlockPos posIn, Random randomIn) {
		if (stateIn.getValue(LIT)) {
			double d0 = (double) posIn.getX() + 0.5D;
			double d1 = (double) posIn.getY();
			double d2 = (double) posIn.getZ() + 0.5D;
			
			if (randomIn.nextDouble() < 0.1D) {
				worldIn.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
			}

			for (Direction direction : Direction.values()) {
				if (direction != Direction.UP && direction != Direction.DOWN) {
					BlockState state = worldIn.getBlockState(posIn.offset(direction.getNormal()));
					
					if (state.isAir()) {
						Direction.Axis direction$axis = direction.getAxis();
						double d4 = randomIn.nextDouble() * 0.6D - 0.3D;
						double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52D : d4;
						double d6 = randomIn.nextDouble() * 6.0D / 16.0D;
						double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52D : d4;
						worldIn.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
						worldIn.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
					}
				}
			}
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		if (ConfigurationManager.getInstance().getCanDestroyWalls()) {
			return this.defaultBlockState();
		}
		return Blocks.AIR.defaultBlockState();
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter blockReader, BlockPos posIn, BlockState stateIn) {
		return new ItemStack(ObjectManager.module_furnace);
    }
}