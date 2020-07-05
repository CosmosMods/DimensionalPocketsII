package com.zeher.zeherlib.mod.util;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ModFluidUtil {
	
	public static ItemStack useItemSafely(ItemStack stack) {
		if (stack.getCount() == 1) {
			if (stack.getItem().hasContainerItem(stack))
				return stack.getItem().getContainerItem(stack);
			else
				return null;
		} else {
			stack.splitStack(1);
			return stack;
		}
	}

	public static void dropStackInWorld(World world, BlockPos pos, ItemStack stack) {
		if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops")) {
			float f = 0.7F;
			double d0 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
			double d1 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
			double d2 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
			EntityItem entityitem = new EntityItem(world, (double) pos.getX() + d0, (double) pos.getY() + d1, (double) pos.getZ() + d2, stack);
			entityitem.setPickupDelay(10); 
			world.spawnEntity(entityitem);
		}
	}
	
	public static ItemStack getTankStackFromData(Block block) {
		ItemStack stack = new ItemStack(Item.getItemFromBlock(block));
		NBTTagCompound tag = new NBTTagCompound();
		stack.setTagCompound(tag);
		return stack;
	}
}