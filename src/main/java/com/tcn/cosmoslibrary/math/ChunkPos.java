package com.tcn.cosmoslibrary.math;

import java.util.Random;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.AbstractIterator;

import net.minecraft.dispenser.IPosition;
import net.minecraft.util.AxisRotation;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.MathHelper;

@Immutable
public class ChunkPos extends Vector2i {
	
	public static final ChunkPos ZERO = new ChunkPos(0, 0);
	private static final int NUM_X_BITS = 1 + MathHelper.log2(MathHelper.smallestEncompassingPowerOfTwo(30000000));
	private static final int NUM_Z_BITS = NUM_X_BITS;
	private static final int NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
	private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
	private static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;
	private static final int INVERSE_START_BITS_Z = NUM_Y_BITS;
	private static final int INVERSE_START_BITS_X = NUM_Y_BITS + NUM_Z_BITS;

	public ChunkPos(int x, int y) {
		super(x, y);
	}

	public ChunkPos(double x, double y) {
		super(x, y);
	}

	public ChunkPos(IPosition position) {
		this(position.getX(), position.getZ());
	}

	public ChunkPos(Vector2i source) {
		this(source.getX(), source.getZ());
	}

	public static long offset(long pos, Direction direction) {
		return offset(pos, direction.getXOffset(), direction.getZOffset());
	}

	public static long offset(long pos, int dx, int dz) {
		return pack(unpackX(pos) + dx, unpackZ(pos) + dz);
	}

	public static int unpackX(long packedPos) {
		return (int) (packedPos << 64 - INVERSE_START_BITS_X - NUM_X_BITS >> 64 - NUM_X_BITS);
	}

	public static int unpackZ(long packedPos) {
		return (int) (packedPos << 64 - INVERSE_START_BITS_Z - NUM_Z_BITS >> 64 - NUM_Z_BITS);
	}

	public static ChunkPos fromLong(long packedPos) {
		return new ChunkPos(unpackX(packedPos), unpackZ(packedPos));
	}

	public long toLong() {
		return pack(this.getX(), this.getZ());
	}

	public static long pack(int x, int z) {
		long i = 0L;
		i = i | ((long) x & X_MASK) << INVERSE_START_BITS_X;
		return i | ((long) z & Z_MASK) << INVERSE_START_BITS_Z;
	}

	public static long atSectionBottomY(long packedPos) {
		return packedPos & -16L;
	}
	
	public ChunkPos add(double x, double z) {
		return x == 0.0D && z == 0.0D ? this : new ChunkPos((double) this.getX() + x, (double) this.getZ() + z);
	}
	
	public ChunkPos add(int x, int y, int z) {
		return x == 0 && y == 0 && z == 0 ? this : new ChunkPos(this.getX() + x, this.getZ() + z);
	}
	
	public ChunkPos add(Vector2i vec) {
		return this.add(vec.getX(), vec.getZ());
	}
	
	public ChunkPos subtract(Vector2i vec) {
		return this.add(-vec.getX(), -vec.getZ());
	}
	
	public ChunkPos up() {
		return this.offset(Direction.UP);
	}
	
	public ChunkPos up(int n) {
		return this.offset(Direction.UP, n);
	}
	
	public ChunkPos down() {
		return this.offset(Direction.DOWN);
	}
	
	public ChunkPos down(int n) {
		return this.offset(Direction.DOWN, n);
	}
	
	public ChunkPos north() {
		return this.offset(Direction.NORTH);
	}
	
	public ChunkPos north(int n) {
		return this.offset(Direction.NORTH, n);
	}
	
	public ChunkPos south() {
		return this.offset(Direction.SOUTH);
	}
	
	public ChunkPos south(int n) {
		return this.offset(Direction.SOUTH, n);
	}
	
	public ChunkPos west() {
		return this.offset(Direction.WEST);
	}
	
	public ChunkPos west(int n) {
		return this.offset(Direction.WEST, n);
	}
	
	public ChunkPos east() {
		return this.offset(Direction.EAST);
	}
	
	public ChunkPos east(int n) {
		return this.offset(Direction.EAST, n);
	}
	
	public ChunkPos offset(Direction facing) {
		return new ChunkPos(this.getX() + facing.getXOffset(), this.getZ() + facing.getZOffset());
	}
	
	public ChunkPos offset(Direction facing, int n) {
		return n == 0 ? this
				: new ChunkPos(this.getX() + facing.getXOffset() * n, this.getZ() + facing.getZOffset() * n);
	}

	public ChunkPos rotate(Rotation rotationIn) {
		switch (rotationIn) {
		case NONE:
		default:
			return this;
		case CLOCKWISE_90:
			return new ChunkPos(-this.getZ(), this.getX());
		case CLOCKWISE_180:
			return new ChunkPos(-this.getX(), -this.getZ());
		case COUNTERCLOCKWISE_90:
			return new ChunkPos(this.getZ(), -this.getX());
		}
	}
	
	public ChunkPos toImmutable() {
		return this;
	}

	public ChunkPos.Mutable toMutable() {
		return new ChunkPos.Mutable(this.getX(), this.getZ());
	}

	public static Iterable<ChunkPos> getRandomPositions(Random rand, int amount, int minX, int minZ, int maxX,
			int maxZ) {
		int i = maxX - minX + 1;
		int k = maxZ - minZ + 1;
		return () -> {
			return new AbstractIterator<ChunkPos>() {
				final ChunkPos.Mutable pos = new ChunkPos.Mutable();
				int remainingAmount = amount;

				protected ChunkPos computeNext() {
					if (this.remainingAmount <= 0) {
						return this.endOfData();
					} else {
						ChunkPos blockpos = this.pos.setPos(minX + rand.nextInt(i), minZ + rand.nextInt(k));
						--this.remainingAmount;
						return blockpos;
					}
				}
			};
		};
	}

	public static class Mutable extends ChunkPos {
		public Mutable() {
			this(0, 0);
		}

		public Mutable(int x_, int z_) {
			super(x_, z_);
		}

		public Mutable(double x, double z) {
			this(MathHelper.floor(x), MathHelper.floor(z));
		}
		
		public ChunkPos add(double x, double z) {
			return super.add(x, z).toImmutable();
		}
		
		public ChunkPos add(int x, int z) {
			return super.add(x, z).toImmutable();
		}
		
		public ChunkPos offset(Direction facing, int n) {
			return super.offset(facing, n).toImmutable();
		}
		
		public ChunkPos rotate(Rotation rotationIn) {
			return super.rotate(rotationIn).toImmutable();
		}
		
		public ChunkPos.Mutable setPos(int xIn, int zIn) {
			this.setX(xIn);
			this.setZ(zIn);
			return this;
		}
		
		public ChunkPos.Mutable setPos(double xIn, double zIn) {
			return this.setPos(MathHelper.floor(xIn), MathHelper.floor(zIn));
		}

		public ChunkPos.Mutable setPos(Vector2i vec) {
			return this.setPos(vec.getX(), vec.getZ());
		}

		public ChunkPos.Mutable setPos(long packedPos) {
			return this.setPos(unpackX(packedPos), unpackZ(packedPos));
		}

		public ChunkPos.Mutable setPos(AxisRotation rotation, int x, int y, int z) {
			return this.setPos(rotation.getCoordinate(x, y, z, Direction.Axis.X),
					rotation.getCoordinate(x, y, z, Direction.Axis.Z));
		}

		public ChunkPos.Mutable setAndMove(Vector2i pos, Direction direction) {
			return this.setPos(pos.getX() + direction.getXOffset(), pos.getZ() + direction.getZOffset());
		}

		public ChunkPos.Mutable setAndOffset(Vector2i pos, int offsetX, int offsetY, int offsetZ) {
			return this.setPos(pos.getX() + offsetX, pos.getZ() + offsetZ);
		}

		public ChunkPos.Mutable move(Direction facing) {
			return this.move(facing, 1);
		}

		public ChunkPos.Mutable move(Direction facing, int n) {
			return this.setPos(this.getX() + facing.getXOffset() * n, this.getZ() + facing.getZOffset() * n);
		}

		public ChunkPos.Mutable move(int xIn, int yIn, int zIn) {
			return this.setPos(this.getX() + xIn, this.getZ() + zIn);
		}

		public ChunkPos.Mutable func_243531_h(Vector2i p_243531_1_) {
			return this.setPos(this.getX() + p_243531_1_.getX(), this.getZ() + p_243531_1_.getZ());
		}

		public ChunkPos.Mutable clampAxisCoordinate(Direction.Axis axis, int min, int max) {
			switch (axis) {
			case X:
				return this.setPos(MathHelper.clamp(this.getX(), min, max), this.getZ());
			case Z:
				return this.setPos(this.getX(), MathHelper.clamp(this.getZ(), min, max));
			default:
				throw new IllegalStateException("Unable to clamp axis " + axis);
			}
		}

		public void setX(int xIn) {
			super.setX(xIn);
		}

		public void setZ(int zIn) {
			super.setZ(zIn);
		}

		public ChunkPos toImmutable() {
			return new ChunkPos(this);
		}
	}
}