package com.zeher.zeherlib.mod.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public abstract class ModTileEntityAbstract extends TileEntity {

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new SPacketUpdateTileEntity(getPos(), 0, tag);
    }

    public void fireEvent(int id, int process) {
        world.addBlockEvent(getPos(), getBlockType(), id, process);
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