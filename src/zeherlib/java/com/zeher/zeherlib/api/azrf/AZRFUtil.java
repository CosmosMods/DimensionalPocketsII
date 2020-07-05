package com.zeher.zeherlib.api.azrf;

import cofh.redstoneflux.api.IEnergyHandler;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Collection of useful utilities connected to AZRF.
 */
public class AZRFUtil {
	
	/**
	 * Used to generate an {@link ItemStack} from a block to produce a {@link ItemBlock} with the required NBT data.
	 * @param {@link World} [given world the block is in]
	 * @param {@link BlockPos} [position of the given block]
	 */
	public static void generateStack(World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		Block block = world.getBlockState(pos).getBlock();
		
		ItemStack stack = new ItemStack(block);
		NBTTagCompound compound = new NBTTagCompound();
		stack.setTagCompound(new NBTTagCompound());
		NBTTagList list = new NBTTagList();
		
		if (tile != null) {
			if (tile instanceof IInventory) {
				if (!((IInventory) tile).isEmpty()) {
					int size = ((IInventory) tile).getSizeInventory();
					
					NonNullList<ItemStack> list_ = NonNullList.<ItemStack>withSize(size, ItemStack.EMPTY);
					
					for (int i = 0; i < size; i++) {
						list_.set(i, ((IInventory) tile).getStackInSlot(i));
					}
					
					ItemStackHelper.saveAllItems(compound, list_);
					compound.setInteger("size", size);
				}
			}
		
			if (tile instanceof ISidedTile) {
				NBTTagCompound compound_tag = new NBTTagCompound();
				compound.setTag("sides", compound_tag);
				
				for (EnumFacing c : EnumFacing.VALUES) {
					compound_tag.setInteger("index_" + c.getIndex(), ((ISidedTile) tile).getSideArray()[c.getIndex()].getIndex());
				}
			}
			
			if (tile instanceof IEnergyHandler) {
				compound.setInteger("energy", ((IEnergyHandler) tile).getEnergyStored(EnumFacing.DOWN));
			}
			stack.getTagCompound().setTag("nbt_data", compound);
		}
		
		block.spawnAsEntity(world, pos, stack);
		world.setBlockToAir(pos);
	}
	
	/**
	 * Generates a blank itemstack when NBT data is not required.
	 * @param block [block to generate from]
	 * @return {@link ItemStack} [itemstack containing the block]
	 */
	public static ItemStack generateItemStackFromTile(Block block) {
		return new ItemStack(block);
	}
}