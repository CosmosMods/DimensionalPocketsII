package com.tcn.cosmoslibrary.math;

import java.util.stream.IntStream;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.MoreObjects;
import com.mojang.serialization.Codec;

import net.minecraft.dispenser.IPosition;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Immutable
public class Vector2i implements Comparable<Vector2i> {
   public static final Codec<Vector2i> CODEC = Codec.INT_STREAM.comapFlatMap((stream) -> {
      return Util.validateIntStreamSize(stream, 3).map((componentArraz) -> {
         return new Vector2i(componentArraz[0], componentArraz[1]);
      });
   }, (vector) -> {
      return IntStream.of(vector.getX(), vector.getZ());
   });
   /** An immutable vector with zero as all coordinates. */
   public static final Vector2i NULL_VECTOR = new Vector2i(0, 0);
   private int x;
   private int z;

   public Vector2i(int xIn, int zIn) {
      this.x = xIn;
      this.z = zIn;
   }

   public Vector2i(double xIn, double zIn) {
      this(MathHelper.floor(xIn), MathHelper.floor(zIn));
   }

   public boolean equals(Object p_equals_1_) {
      if (this == p_equals_1_) {
         return true;
      } else if (!(p_equals_1_ instanceof Vector2i)) {
         return false;
      } else {
         Vector2i vector3i = (Vector2i)p_equals_1_;
         if (this.getX() != vector3i.getX()) {
            return false;
         } else if (this.getZ() != vector3i.getZ()) {
            return false;
         } else if (this.getX() == vector3i.getX()) {
        	 return true;
         } else {
        	 return this.getZ() == vector3i.getZ();
         }
      }
   }

   public int hashCode() {
      return (this.getZ() * 31) * 31 + this.getX();
   }

   public int compareTo(Vector2i p_compareTo_1_) {
      if (this.getZ() == p_compareTo_1_.getZ()) {
         return this.getZ() == p_compareTo_1_.getZ() ? this.getX() - p_compareTo_1_.getX() : this.getZ() - p_compareTo_1_.getZ();
      } else {
         return this.getZ() - p_compareTo_1_.getZ();
      }
   }

   /**
    * Gets the X coordinate.
    */
   public int getX() {
      return this.x;
   }

   /**
    * Gets the Z coordinate.
    */
   public int getZ() {
      return this.z;
   }
   
   /**
    * Sets the X coordinate.
    */
   protected void setX(int xIn) {
      this.x = xIn;
   }

   protected void setZ(int zIn) {
      this.z = zIn;
   }
   
   /**
    * Offset this BlockPos 1 block up
    */
   public Vector2i up() {
      return this.up(1);
   }

   /**
    * Offset this BlockPos n blocks up
    */
   public Vector2i up(int n) {
      return this.offset(Direction.UP, n);
   }

   /**
    * Offset this BlockPos 1 block down
    */
   public Vector2i down() {
      return this.down(1);
   }

   /**
    * Offset this BlockPos n blocks down
    */
   public Vector2i down(int n) {
      return this.offset(Direction.DOWN, n);
   }

   /**
    * Offsets this BlockPos n blocks in the given direction
    */
   public Vector2i offset(Direction facing, int n) {
      return n == 0 ? this : new Vector2i(this.getX() + facing.getXOffset() * n, this.getZ() + facing.getZOffset() * n);
   }

   /**
    * Calculate the cross product of this and the given Vector
    */
   public Vector2i crossProduct(Vector2i vec) {
	   return null;
      //return new Vector2i(this.getZ() * vec.getZ() - this.getZ() * vec.getZ(), this.getZ() * vec.getX() - this.getX() * vec.getZ(), this.getX() * vec.getZ() - this.getZ() * vec.getX());
   }

   public boolean withinDistance(Vector2i vector, double distance) {
      return this.distanceSq((double)vector.getX(), (double)vector.getZ(), false) < distance * distance;
   }

   public boolean withinDistance(IPosition position, double distance) {
      return this.distanceSq(position.getX(), position.getZ(), true) < distance * distance;
   }

   /**
    * Calculate squared distance to the given Vector
    */
   public double distanceSq(Vector2i to) {
      return this.distanceSq((double)to.getX(), (double)to.getZ(), true);
   }

   public double distanceSq(IPosition position, boolean useCenter) {
      return this.distanceSq(position.getX(), position.getZ(), useCenter);
   }

   public double distanceSq(double x, double z, boolean useCenter) {
      double d0 = useCenter ? 0.5D : 0.0D;
      double d1 = (double)this.getX() + d0 - x;
      double d2 = (double)this.getZ() + d0 - z;
      return d1 * d1 + d2 * d2;
   }

   public int manhattanDistance(Vector2i vector) {
      float f = (float)Math.abs(vector.getX() - this.getX());
      float f1 = (float)Math.abs(vector.getZ() - this.getZ());
      return (int)(f + f1);
   }
   
   public String toString() {
      return MoreObjects.toStringHelper(this).add("x", this.getX()).add("z", this.getZ()).toString();
   }

   @OnlyIn(Dist.CLIENT)
   public String getCoordinatesAsString() {
      return "" + this.getX() + ", " + this.getZ();
   }
}
