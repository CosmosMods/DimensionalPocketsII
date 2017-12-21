package com.zeher.dimensionalpockets.core.tileentity;

import com.zeher.dimensionalpockets.core.handlers.BlockHandler;
import com.zeher.dimensionalpockets.core.pocket.Pocket;
import com.zeher.dimensionalpockets.core.pocket.PocketRegistry;
import com.zeher.dimensionalpockets.core.pocket.PocketShiftPreparation;
import com.zeher.dimensionalpockets.core.pocket.PocketShiftPreparation.Direction;
import com.zeher.dimensionalpockets.core.pocket.handlers.PocketChunkLoaderHandler;
import com.zeher.dimensionalpockets.core.util.DimUtils;
import com.zeher.trzlib.api.interfaces.IBlockInteract;
import com.zeher.trzlib.api.interfaces.IBlockNotifier;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityDimensionalPocket extends TileEntityDim
implements IBlockNotifier, IBlockInteract, ITickable {
	
	private static final String TAG_CUSTOM_DP_NAME = "customDPName";
	private String customName;

	@SideOnly(Side.CLIENT)
	private Pocket pocket;
	private PocketShiftPreparation telePrep = null;

	@Override
	public void update() {
		if (world.isRemote) {
			return;
		}

		if (telePrep != null) {
			if (telePrep.doPrepareTick()) {
				telePrep = null;
			}
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		if (world.isRemote) {
			return;
		}
		DimUtils.spawnItemStack(generateItemStackOnRemoval(), world, (int) pos.getX() + 0.5, (int) pos.getY() + 0.5, (int )pos.getZ() + 0.5, 0);
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		Pocket p = getPocket();
		if (p != null) {
			for (EnumFacing side : EnumFacing.VALUES) {
				//p.markConnectorForUpdate(side);
			}
		}
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (worldIn.isRemote) {
			return;
		}

		if (stack.hasTagCompound()) {
			NBTTagCompound itemCompound = stack.getTagCompound();

			BlockPos chunkSet = new BlockPos(0,0,0); //CoordSet.readFromNBT(itemCompound);
			boolean success = PocketRegistry.getPocket(chunkSet) != null;

			if (!success) {
				throw new RuntimeException("YOU DESERVED THIS!");
			}

			PocketRegistry.updatePocket(chunkSet, placer.dimension, getPos());

			if (itemCompound.hasKey("display")) {
				String tempString = itemCompound.getCompoundTag("display").getString("Name");
				if (!tempString.isEmpty()) {
					customName = tempString;
				}
			}

			if (placer instanceof EntityPlayer) {
				EntityPlayerMP player = (EntityPlayerMP) placer;
				if (player.getHeldItemMainhand() == stack) {
					player.getHeldItemMainhand().setCount(0);
				}
			}
		}

		Pocket pocket = getPocket();
		pocket.generatePocketRoom(placer.getName());
		PocketChunkLoaderHandler.addPocketToChunkLoader(pocket);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn == null) {
			return true;
		}
		
		//prepareTeleportIntoPocket(playerIn);
		shiftIntoPocket(playerIn);
		return true;
	}

	public ItemStack generateItemStackOnRemoval() {
		ItemStack itemStack = new ItemStack(BlockHandler.block_dimensional_pocket);

		if (!itemStack.hasTagCompound()) {
			itemStack.setTagCompound(new NBTTagCompound());
		}

		BlockPos chunkSet = getPocket().getChunkPos();
		//chunkSet.writeToNBT(itemStack.getTagCompound());

		String creatorLore = null;
		Pocket pocket = getPocket();
		if (pocket != null && pocket.getCreator() != null) {
			creatorLore = "Creator: §3§o" + pocket.getCreator();
		}

		BlockPos blockSet = new BlockPos(chunkSet.getX() << 4, chunkSet.getY() << 4, chunkSet.getZ() << 4);

		itemStack = DimUtils.generateItem(itemStack, customName, false, "~ Pocket §e" + blockSet.getX() + "," + blockSet.getY() + "," + blockSet.getZ() + "§8 ~", creatorLore);
		return itemStack;
	}

	@Override
	public Pocket getPocket() {
		if (world.isRemote) {
			return pocket;
		}

		return PocketRegistry.getOrCreatePocket(world, getPos());
	}
	
	@Override
	public BlockPos getPos() {
        return this.pos;
    }
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		Pocket pocket = getPocket();
		if (pocket != null) {
			pocket.writeToNBT(compound);
		}
		if (customName != null) {
			compound.setString(TAG_CUSTOM_DP_NAME, customName);
		}
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);

		if (world != null && world.isRemote) {
			pocket = Pocket.readFromNBT(tag);
		}

		String tempString = tag.getString(TAG_CUSTOM_DP_NAME);
		if (!tempString.isEmpty()) {
			customName = tempString;
		}
	}

	private void prepareTeleportIntoPocket(EntityPlayer player) {
		int ticksToTake = 5;
		if (!world.isRemote) {
			telePrep = new PocketShiftPreparation(player, ticksToTake, getPocket(), Direction.INTO_POCKET);
		}
	}
	
	private void shiftIntoPocket(EntityPlayer player) {
		if(!world.isRemote) {
			getPocket().shiftTo(player);
		}
	}
	
}
