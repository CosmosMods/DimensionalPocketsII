package com.zeher.zeherlib.mod.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

public abstract class ModTileEntityAbstract extends TileEntity {

    public ModTileEntityAbstract(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	@Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        read(pkt.getNbtCompound());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tag = new CompoundNBT();
        write(tag);
        return new SUpdateTileEntityPacket(getPos(), 0, tag);
    }

    public void fireEvent(int id, int process) {
        world.addBlockEvent(getPos(), this.getBlockState().getBlock(), id, process);
    }

    @Override
    public boolean receiveClientEvent(int id, int process) {
        return false;
    }

    public BlockPos getCoordSet() {
        return this.pos;
    }

    public void markForUpdate() {
        //world.update.markBlockForUpdate(pos);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + getCoordSet();
    }
}