package com.zeher.zeherlib.mod.util;

import java.util.ArrayList;
import java.util.List;

import com.zeher.zeherlib.api.core.interfaces.IRotatableTile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModBlockPos {
	
	public int x;
	public int y;
	public int z;
	
	public BlockPos pos;
	public EnumFacing orientation;

	public ModBlockPos(BlockPos pos) {
		this.pos = pos;
		this.orientation = null;
	}

	public ModBlockPos(BlockPos pos, EnumFacing corientation) {
		this.pos = pos;
		this.orientation = corientation;
	}

	public ModBlockPos(ModBlockPos te) {
		this.pos = te.pos;
		this.orientation = te.orientation;
	}

	public ModBlockPos(NBTTagCompound nbttagcompound) {
		this.pos = new BlockPos(nbttagcompound.getShort("i"), nbttagcompound.getShort("j"), nbttagcompound.getShort("k"));
		this.orientation = null;
	}

	public ModBlockPos(TileEntity tile) {
		this.pos = tile.getPos();
		this.orientation = null;
	}

	public static ModBlockPos fromFactoryTile(IRotatableTile te) {
		ModBlockPos bp = new ModBlockPos((TileEntity) te);
		bp.orientation = te.getDirectionFacing();
		return bp;
	}

	public static EnumFacing getOpposite(EnumFacing side) {
		if (side.equals(EnumFacing.UP)) {
			return EnumFacing.DOWN;
		}
		if (side.equals(EnumFacing.NORTH)) {
			return EnumFacing.SOUTH;
		}
		if (side.equals(EnumFacing.WEST)) {
			return EnumFacing.EAST;
		}

		if (side.equals(EnumFacing.DOWN)) {
			return EnumFacing.UP;
		}
		if (side.equals(EnumFacing.SOUTH)) {
			return EnumFacing.NORTH;
		}
		if (side.equals(EnumFacing.EAST)) {
			return EnumFacing.WEST;
		} else {
			return null;
		}
	}

	public ModBlockPos copy() {
		return new ModBlockPos(this.pos, this.orientation);
	}

	public void moveRight(int step) {
		switch (this.orientation) {
		case SOUTH:
			this.pos = new BlockPos(pos.getX() - step, pos.getY(), pos.getZ());
			break;
		case NORTH:
			this.pos = new BlockPos(pos.getX() + step, pos.getY(), pos.getZ());
			break;
		case EAST:
			this.pos = new BlockPos(pos.getX(), pos.getY(), pos.getZ() + step);
			break;
		case WEST:
			this.pos = new BlockPos(pos.getX(), pos.getY(), pos.getZ() - step);
			break;
		default:
			break;
		}
	}

	public void moveLeft(int step) {
		moveRight(-step);
	}

	public void moveForwards(int step) {
		switch (this.orientation) {
		case UP:
			this.pos = new BlockPos(pos.getX(), pos.getY() + step, pos.getZ());
			break;
		case DOWN:
			this.pos = new BlockPos(pos.getX(), pos.getY() - step, pos.getZ());
			break;
		case SOUTH:
			this.pos = new BlockPos(pos.getX(), pos.getY(), pos.getZ() + step);
			break;
		case NORTH:
			this.pos = new BlockPos(pos.getX(), pos.getY(), pos.getZ() - step);
			break;
		case EAST:
			this.pos = new BlockPos(pos.getX() + step, pos.getY(), pos.getZ());
			break;
		case WEST:
			this.pos = new BlockPos(pos.getX() - step, pos.getY(), pos.getZ());
			break;
		default:
			break;
		}
	}

	public void moveBackwards(int step) {
		moveForwards(-step);
	}

	public void moveUp(int step) {
		switch (this.orientation) {
		case SOUTH:
		case NORTH:
		case EAST:
		case WEST:
			this.pos = new BlockPos(pos.getX(), pos.getY() + step, pos.getZ());
			break;
		default:
			break;
		}
	}

	public void moveDown(int step) {
		moveUp(-step);
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("i", this.pos.getX());
		nbttagcompound.setInteger("j", this.pos.getY());
		nbttagcompound.setInteger("k", this.pos.getZ());
	}

	public String toString() {
		if (this.orientation == null) {
			return "{" + this.x + ", " + this.y + ", " + this.z + "}";
		}
		return "{" + this.x + ", " + this.y + ", " + this.z + ";" + this.orientation.toString() + "}";
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof ModBlockPos)) {
			return false;
		}
		ModBlockPos bp = (ModBlockPos) obj;
		return (bp.x == this.x) && (bp.y == this.y) && (bp.z == this.z) && (bp.orientation == this.orientation);
	}

	public int hashCode() {
		return this.x & 0xFFF | this.y & 0xFF00 | this.z & 0xFFF000;
	}
	
	public List<ModBlockPos> getAdjacent(boolean includeVertical) {
		List<ModBlockPos> a = new ArrayList();
		a.add(new ModBlockPos(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ()), EnumFacing.EAST));
		a.add(new ModBlockPos(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ()), EnumFacing.WEST));
		a.add(new ModBlockPos(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1), EnumFacing.SOUTH));
		a.add(new ModBlockPos(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1), EnumFacing.NORTH));
		if (includeVertical) { 
			a.add(new ModBlockPos(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()), EnumFacing.UP));
			a.add(new ModBlockPos(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()), EnumFacing.DOWN));
		}
		return a;
	}

	public TileEntity getTileEntity(World world) {
		return world.getTileEntity(this.pos);
	}

	public static TileEntity getAdjacentTileEntity(TileEntity start, EnumFacing side) {
		ModBlockPos p = new ModBlockPos(start);
		p.orientation = side;
		p.moveForwards(1);
		BlockPos pos = new BlockPos(p.x, p.y, p.z);
		return start.getWorld().getTileEntity(pos);
	}

	public static TileEntity getAdjacentTileEntity(TileEntity start, EnumFacing side,
			Class<? extends TileEntity> targetClass) {
		TileEntity te = getAdjacentTileEntity(start, side);
		if (targetClass.isAssignableFrom(te.getClass())) {
			return te;
		}
		return null;
	}
}
