package com.tcn.dimensionalpocketsii.core.external.blockentity;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.common.enums.EnumChannelSideState;
import com.tcn.cosmoslibrary.common.interfaces.IEnergyEntity;
import com.tcn.cosmoslibrary.common.interfaces.block.IBlockInteract;
import com.tcn.cosmoslibrary.common.interfaces.blockentity.IBlockEntityChannel;
import com.tcn.cosmoslibrary.common.interfaces.blockentity.IBlockEntityChannelSided;
import com.tcn.cosmoslibrary.common.interfaces.blockentity.IBlockEntityChannelType.IChannelEnergy;
import com.tcn.cosmoslibrary.common.util.CosmosUtil;
import com.tcn.dimensionalpocketsii.core.external.block.BlockConduitEnergy;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class BlockEntityEnergyConduit extends BlockEntity implements IBlockInteract, IBlockEntityChannel.Energy, /*ITickableTileEntity,*/ IChannelEnergy, IEnergyEntity, IBlockEntityChannelSided {

	private EnumChannelSideState[] SIDE_STATE_ARRAY = EnumChannelSideState.getStandardArray();
	
	private int energy_stored = 0;
	private int energy_capacity = 4000;
	private int energy_max_receive = 2000;
	private int energy_max_extract = 2000;
	
	public Direction last_facing;
	public int last_rf_rate;

	public BlockEntityEnergyConduit(BlockPos posIn, BlockState stateIn) {
		super(ModBusManager.CONDUIT_ENERGY_TILE_TYPE, posIn, stateIn);
	}
	
	@Override 
	public EnumChannelSideState getSide(Direction facing) {
		return this.SIDE_STATE_ARRAY[facing.get3DDataValue()];
	}
	
	@Override
	public void setSide(Direction facing, EnumChannelSideState side_state) {
		this.SIDE_STATE_ARRAY[facing.get3DDataValue()] = side_state;
		
		this.updateRenders();
	}
	
	@Override
	public EnumChannelSideState[] getSideArray() {
		return this.SIDE_STATE_ARRAY;
	}

	@Override
	public void setSideArray(EnumChannelSideState[] new_array) {
		this.SIDE_STATE_ARRAY = new_array;
		
		this.updateRenders();
	}

	@Override
	public void cycleSide(Direction facing) {
		EnumChannelSideState next = this.SIDE_STATE_ARRAY[facing.get3DDataValue()].getNextState();
		
		this.setSide(facing, next);
		
		this.updateRenders();
	}

	@Override
	public boolean canConnect(Direction facing) {
		EnumChannelSideState state = SIDE_STATE_ARRAY[facing.get3DDataValue()];
		
		if (state.equals(EnumChannelSideState.DISABLED)) {
			return false;
		}
		return true;
	}

	@Override
	public void updateRenders() {
		if (level != null) {
			this.setChanged();
			BlockState state = this.getBlockState();
			BlockConduitEnergy block = (BlockConduitEnergy) state.getBlock();
			
			level.sendBlockUpdated(this.getBlockPos(), state, state, 3);
			
			if (!level.isClientSide) {
				level.setBlockAndUpdate(this.getBlockPos(), block.defaultBlockState());
			}
		}
	}
	
	@Override
	public Direction getLastFacing() {
		return this.last_facing;
	}
	
	@Override
	public void setLastFacing(Direction facing) {
		this.last_facing = facing;
	}

	@Override
	public int getLastRFRate() {
		return this.last_rf_rate;
	}

	@Override
	public void setLastRFRate(int value) { 
		this.last_rf_rate = value;
	}

	@SuppressWarnings("unused")
	public static void tick(Level levelIn, BlockPos posIn, BlockState stateIn, BlockEntityEnergyConduit entityIn) {
		for (Direction c : Direction.values()) {
			BlockEntity tile_other = entityIn.getLevel().getBlockEntity(entityIn.getBlockPos().offset(c.getNormal()));
			
			/*
			if (tile_other instanceof IEnergyReceiver) {
				if (this.hasEnergy() && ((IEnergyReceiver) tile_other).canConnectEnergy(c.getOpposite())) {
					if (tile_other instanceof IChannelType.IChannelEnergy && tile_other instanceof ISidedChannelTile) {
						if (!((ISidedChannelTile) tile_other).getSide(c.getOpposite()).equals(EnumChannelSideState.DISABLED) && !((ISidedChannelTile) tile_other).getSide(c.getOpposite()).equals(EnumChannelSideState.INTERFACE_OUTPUT)) {
							if (this.last_facing != null) {
								if (!c.equals(last_facing.getOpposite())) {
									this.extractEnergy(c, ((IEnergyReceiver) tile_other).receiveEnergy(c.getOpposite(), this.getLastRFRate(), false), false);
								}
							}	
						}
					} else if (tile_other instanceof ISidedTile) {
						if (!((ISidedTile) tile_other).getSide(c.getOpposite()).equals(EnumSideState.DISABLED) && !((ISidedTile) tile_other).getSide(c.getOpposite()).equals(EnumSideState.INTERFACE_OUTPUT)) {
							
							this.extractEnergy(c, ((IEnergyReceiver) tile_other).receiveEnergy(c.getOpposite(), this.getLastRFRate(), false), false);
						}
					} else {
						this.extractEnergy(c, ((IEnergyReceiver) tile_other).receiveEnergy(c.getOpposite(), this.getLastRFRate(), false), false);
					}
				}
			}*/
		}
	}
	
	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		//this.storage.save(compound);
		
		this.setLastFacing(Direction.from3DDataValue(compound.getInt("last_facing")));
		
		this.SIDE_STATE_ARRAY = new EnumChannelSideState[] { EnumChannelSideState.getStateFromIndex(compound.getInt("down")),
				EnumChannelSideState.getStateFromIndex(compound.getInt("up")),
				EnumChannelSideState.getStateFromIndex(compound.getInt("north")),
				EnumChannelSideState.getStateFromIndex(compound.getInt("south")),
				EnumChannelSideState.getStateFromIndex(compound.getInt("west")),
				EnumChannelSideState.getStateFromIndex(compound.getInt("east"))};
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
		//this.storage.writeToNBT(compound);
		
		if (this.last_facing != null) {
			compound.putInt("last_facing", this.getLastFacing().get3DDataValue());
		}
		
		compound.putInt("down", this.SIDE_STATE_ARRAY[0].getIndex());
		compound.putInt("up", this.SIDE_STATE_ARRAY[1].getIndex());
		compound.putInt("north", this.SIDE_STATE_ARRAY[2].getIndex());
		compound.putInt("south", this.SIDE_STATE_ARRAY[3].getIndex());
		compound.putInt("west", this.SIDE_STATE_ARRAY[4].getIndex());
		compound.putInt("east", this.SIDE_STATE_ARRAY[5].getIndex());
		
		return compound;
	}

	@Override
	public EnumChannelSideState getStateForConnection(Direction facing) {
		return this.getStateForConnection(facing, this.getBlockPos(), this.getLevel(), this);
	}
	
	public EnumChannelSideState getStateForConnection(Direction facing, BlockPos pos, Level world, IBlockEntityChannelSided tile) {
		BlockEntity tile_offset = world.getBlockEntity(pos.offset(facing.getNormal()));
		
		if (tile.getSide(facing).equals(EnumChannelSideState.DISABLED)) {
			return EnumChannelSideState.DISABLED;
		} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_NORMAL)) { 
			return EnumChannelSideState.INTERFACE_NORMAL;
		} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_INPUT)) { 
			return EnumChannelSideState.INTERFACE_INPUT;
		} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_OUTPUT)) { 
			return EnumChannelSideState.INTERFACE_OUTPUT;
		} 
		
		else if (tile_offset != null) {
			if (tile_offset instanceof BlockEntityEnergyConduit) {
				if (((BlockEntityEnergyConduit) tile_offset).getSide(facing.getOpposite()).equals(EnumChannelSideState.DISABLED)) {
					return EnumChannelSideState.NO_CONN;
				} else {
					return EnumChannelSideState.CABLE;
				}
			} 
			
			
			if (tile_offset.getCapability(CapabilityEnergy.ENERGY, facing).resolve().isPresent()) {
				LazyOptional<?> consumer = tile_offset.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
	
				if (consumer.resolve().get() instanceof IEnergyStorage) {
					IEnergyStorage storage = (IEnergyStorage) consumer.resolve().get();
	
					if (storage.canReceive() || storage.canExtract()) {
						if (tile.getSide(facing).equals(EnumChannelSideState.NO_CONN)) {
							return EnumChannelSideState.INTERFACE_NORMAL;
						} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_NORMAL)) {
							return EnumChannelSideState.INTERFACE_NORMAL;
						} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_OUTPUT)) {
							return EnumChannelSideState.INTERFACE_OUTPUT;
						} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_INPUT)) {
							return EnumChannelSideState.INTERFACE_INPUT;
						} else {
							return EnumChannelSideState.NO_CONN;
						}
					}
				}
			}
		}
		return EnumChannelSideState.NO_CONN;
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand handIn, BlockHitResult hit) {
		if (CosmosUtil.holdingWrench(playerIn) && !playerIn.isShiftKeyDown()) {
			this.cycleSide(hit.getDirection());
		}
		return InteractionResult.PASS;
	}

	@Override
	public void attack(BlockState state, Level worldIn, BlockPos pos, Player player) { }

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return this.createEnergyProxy(facing).cast();
		}
		
		return super.getCapability(capability, facing);
	}
	
	private LazyOptional<IEnergyStorage> createEnergyProxy(@Nullable Direction directionIn) {
        return LazyOptional.of(() -> new IEnergyStorage() {
        	
            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                return BlockEntityEnergyConduit.this.extractEnergy(directionIn, maxExtract, simulate);
            }

            @Override
            public int getEnergyStored() {
                return BlockEntityEnergyConduit.this.getEnergyStored();
            }

            @Override
            public int getMaxEnergyStored() {
                return BlockEntityEnergyConduit.this.getMaxEnergyStored();
            }

            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                return BlockEntityEnergyConduit.this.receiveEnergy(directionIn, maxReceive, simulate);
            }

            @Override
            public boolean canReceive() {
                return BlockEntityEnergyConduit.this.canReceive(directionIn);
            }

            @Override
            public boolean canExtract() {
                return BlockEntityEnergyConduit.this.canExtract(directionIn);
            }
        });
    }
	
	@Override
	public void setMaxTransfer(int maxTransfer) {
		this.setMaxReceive(maxTransfer);
		this.setMaxExtract(maxTransfer);
	}

	@Override
	public void setMaxReceive(int maxReceive) {
		this.energy_max_receive = maxReceive;
	}
	@Override

	public void setMaxExtract(int maxExtract) {
		this.energy_max_extract = maxExtract;
	}

	@Override
	public int getMaxReceive() {
		return this.energy_max_receive;
	}

	@Override
	public int getMaxExtract() {
		return this.energy_max_extract;
	}

	@Override
	public void setEnergyStored(int stored) {
		this.energy_stored = stored;

		if (this.energy_stored > energy_capacity) {
			this.energy_stored = energy_capacity;
		} else if (this.energy_stored < 0) {
			this.energy_stored = 0;
		}
	}

	@Override
	public void modifyEnergyStored(int stored) {
		this.energy_stored += stored;

		if (this.energy_stored > this.energy_capacity) {
			this.energy_stored = energy_capacity;
		} else if (this.energy_stored < 0) {
			this.energy_stored = 0;
		}
	}

	@Override
	public int receiveEnergy(Direction directionIn, int max_receive, boolean simulate) {
		int storedReceived = Math.min(this.getMaxEnergyStored() - energy_stored, Math.min(this.energy_max_receive, max_receive));

		if (!simulate) {
			this.energy_stored += storedReceived;
		}
		
		return storedReceived;
	}

	@Override
	public int extractEnergy(Direction directionIn, int max_extract, boolean simulate) {
		int storedExtracted = Math.min(energy_stored, Math.min(this.energy_max_extract, max_extract));

		if (!simulate) {
			this.energy_stored -= storedExtracted;
		}
		
		return storedExtracted;
	}

	@Override
	public int getEnergyStored() {
		return this.energy_stored;
	}

	@Override
	public int getMaxEnergyStored() {
		return this.energy_capacity;
	}

	@Override
	public boolean hasEnergy() {
		return this.energy_stored > 0;
	}

	@Override
	public boolean canExtract(Direction directionIn) {
		return true;
	}

	@Override
	public boolean canReceive(Direction directionIn) {
		return true;
	}
}