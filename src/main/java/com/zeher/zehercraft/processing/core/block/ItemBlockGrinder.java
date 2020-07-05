package com.zeher.zehercraft.processing.core.block;

import com.zeher.zehercraft.processing.core.tile.TileEntityGrinder;
import com.zeher.zeherlib.api.azrf.AZRFItemBlock;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockGrinder extends AZRFItemBlock {

	public ItemBlockGrinder(Block block, String description, String shift_desc_one, String shift_desc_two) {
		super(block, description, shift_desc_one, shift_desc_two);
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hit_x, float hit_y, float hit_z, IBlockState newState) {
		if (!world.setBlockState(pos, newState, 11)) {
			return false;
		}
		
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock().equals(this.block)) {
			if (stack.hasTagCompound()) {
				NBTTagCompound tag = stack.getTagCompound();
				NBTTagCompound compound_tag = tag.getCompoundTag("nbt_data");

				TileEntityGrinder tile_kiln = (TileEntityGrinder) world.getTileEntity(pos);

				int energy = compound_tag.getInteger("energy");
				int size = compound_tag.getInteger("size");
				
				NonNullList list_ = NonNullList.<ItemStack>withSize(size, ItemStack.EMPTY);
				ItemStackHelper.loadAllItems(compound_tag, list_);
				
				tile_kiln.storage.setEnergyStored(energy);
				
				for (int i = 0; i < list_.size(); i++) {
					tile_kiln.setInventorySlotContents(i, (ItemStack) list_.get(i));
				}
			}	
		}
		return true;
	}
}