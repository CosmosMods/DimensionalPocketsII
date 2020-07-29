package com.zeher.zeherlib.mod.util;

import java.util.ArrayList;
import java.util.List;

import com.zeher.zeherlib.api.core.interfaces.IRotatableTile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModBlockPos {
	
	public int x;
	public int y;
	public int z;
	
	public BlockPos pos;
	public Direction orientation;

	public ModBlockPos(BlockPos pos) {
		this.pos = pos;
		this.orientation = null;
	}

	public ModBlockPos(BlockPos pos, Direction corientation) {
		this.pos = pos;
		this.orientation = corientation;
	}

	public ModBlockPos(ModBlockPos te) {
		this.pos = te.pos;
		this.orientation = te.orientation;
	}

	public ModBlockPos(CompoundNBT nbttagcompound) {
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

	public static Direction getOpposite(Direction side) {
		if (side.equals(Direction.UP)) {
			return Direction.DOWN;
		}
		if (side.equals(Direction.NORTH)) {
			return Direction.SOUTH;
		}
		if (side.equals(Direction.WEST)) {
			return Direction.EAST;
		}

		if (side.equals(Direction.DOWN)) {
			return Direction.UP;
		}
		if (side.equals(Direction.SOUTH)) {
			return Direction.NORTH;
		}
		if (side.equals(Direction.EAST)) {
			return Direction.WEST;
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

	public void writeToNBT(CompoundNBT nbttagcompound) {
		nbttagcompound.putInt("i", this.pos.getX());
		nbttagcompound.putInt("j", this.pos.getY());
		nbttagcompound.putInt("k", this.pos.getZ());
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
		List<ModBlockPos> a = new ArrayList<ModBlockPos>();
		a.add(new ModBlockPos(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ()), Direction.EAST));
		a.add(new ModBlockPos(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ()), Direction.WEST));
		a.add(new ModBlockPos(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1), Direction.SOUTH));
		a.add(new ModBlockPos(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1), Direction.NORTH));
		if (includeVertical) { 
			a.add(new ModBlockPos(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()), Direction.UP));
			a.add(new ModBlockPos(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()), Direction.DOWN));
		}
		return a;
	}

	public TileEntity getTileEntity(World world) {
		return world.getTileEntity(this.pos);
	}

	public static TileEntity getAdjacentTileEntity(TileEntity start, Direction side) {
		ModBlockPos p = new ModBlockPos(start);
		p.orientation = side;
		p.moveForwards(1);
		BlockPos pos = new BlockPos(p.x, p.y, p.z);
		return start.getWorld().getTileEntity(pos);
	}

	public static TileEntity getAdjacentTileEntity(TileEntity start, Direction side,
			Class<? extends TileEntity> targetClass) {
		TileEntity te = getAdjacentTileEntity(start, side);
		if (targetClass.isAssignableFrom(te.getClass())) {
			return te;
		}
		return null;
	}
}
