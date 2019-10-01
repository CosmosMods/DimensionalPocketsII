package com.zeher.dimensionalpockets.pocket.client.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.handler.BlockHandler;
import com.zeher.dimensionalpockets.core.handler.NetworkHandler;
import com.zeher.dimensionalpockets.core.util.DimLogger;
import com.zeher.dimensionalpockets.core.util.DimUtils;
import com.zeher.dimensionalpockets.pocket.Pocket;
import com.zeher.dimensionalpockets.pocket.PocketRegistry;
import com.zeher.dimensionalpockets.pocket.handler.PocketChunkLoaderHandler;
import com.zeher.zeherlib.api.interfaces.IBlockInteract;
import com.zeher.zeherlib.api.interfaces.IBlockNotifier;
import com.zeher.zeherlib.api.util.TextUtil;
import com.zeher.zeherlib.api.util.ModUtil;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityDimensionalPocket extends TileEntityDim implements IBlockNotifier, IBlockInteract, ITickable, IInventory {

	private static final String TAG_CUSTOM_DP_NAME = "customDPName";
	private boolean locked;
	private String customName;

	@SideOnly(Side.CLIENT)
	private Pocket pocket;

	@Override
	public void update() {
		if (world.isRemote) {
			return;
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		if (world.isRemote) {
			return;
		}
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		Pocket p = getPocket();
		if (p != null) {
			for (EnumFacing side : EnumFacing.VALUES) {
				// p.markConnectorForUpdate(side);
			}
		}
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) { }

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (!worldIn.isRemote) {
			if (placer.dimension == 1) {
				TextComponentString comp = new TextComponentString(TextUtil.ITALIC + "Pocket has not been generated.");
				placer.sendMessage(comp);
			} else if (stack.hasTagCompound() && placer.dimension != DimensionalPockets.DIMENSION_ID) {
				NBTTagCompound itemCompound = stack.getTagCompound();

				int X = itemCompound.getCompoundTag("chunk_set").getInteger("X");
				int Y = itemCompound.getCompoundTag("chunk_set").getInteger("Y");
				int Z = itemCompound.getCompoundTag("chunk_set").getInteger("Z");

				BlockPos chunkSet = new BlockPos(X, Y, Z);
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

				Pocket pocket = this.getPocket();
				pocket.generatePocket(placer.getName());
				//NetworkHandler.sendPocketSetCreator(placer.getName(), pos);
				PocketChunkLoaderHandler.addPocketToChunkLoader(pocket);

			} else if (stack.hasTagCompound() && placer.dimension == DimensionalPockets.DIMENSION_ID) {
				if (placer.dimension == DimensionalPockets.DIMENSION_ID) {
					NBTTagCompound tag_compound = stack.getTagCompound();

					int X = tag_compound.getCompoundTag("chunk_set").getInteger("X");
					int Y = tag_compound.getCompoundTag("chunk_set").getInteger("Y");
					int Z = tag_compound.getCompoundTag("chunk_set").getInteger("Z");

					BlockPos chunk_set = new BlockPos(X, Y, Z);

					Pocket test_pocket = PocketRegistry.getPocket(chunk_set);

					if (test_pocket != null) {
						if (test_pocket.equals(PocketRegistry
								.getPocket(new BlockPos(pos.getX() >> 4, pos.getY() >> 4, pos.getZ() >> 4)))) {
							if (!world.isRemote) {
								DimUtils.spawnItemStack(this.generateItemStackWithNBT(pos, X, Y, Z), world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0);
								world.setBlockToAir(pos);
							}

						} else {
							PocketRegistry.updatePocket(chunk_set, placer.dimension, getPos());

							if (tag_compound.hasKey("display")) {
								String tempString = tag_compound.getCompoundTag("display").getString("Name");
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

							Pocket pocket = this.getPocket();
							pocket.generatePocket(placer.getName());
							//NetworkHandler.sendPocketSetCreator(placer.getName(), pos);
							PocketChunkLoaderHandler.addPocketToChunkLoader(pocket);
						}
					}
				}
			}
		}
	}

	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		//this.getPocket().setCreator(playerIn.getName());
		
		if (worldIn.isRemote) {
			System.out.println("ArrayList: R " + this.getPocket().getAllowedPlayers());
			System.out.println("Creator: R " + this.getPocket().getCreator());
		} else {
			System.out.println("ArrayList: NR " + this.getPocket().getAllowedPlayers());
			System.out.println("Creator: NR " + this.getPocket().getCreator());
		}
		
		if (!ModUtil.isHoldingHammer(playerIn) && playerIn.isSneaking()) {
			String creator = this.getPocket().getCreator();

			if (creator != null) {
				if (playerIn.getName().equals(creator)) {
					if (!(worldIn.isRemote)) {
						FMLNetworkHandler.openGui(playerIn, DimensionalPockets.INSTANCE, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
					}
				} else {
					TextComponentString comp = new TextComponentString(TextUtil.TEAL + TextUtil.BOLD + "You cannot access the settings of this pocket.");
					playerIn.sendMessage(comp);
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn == null) {
			return true;
		} 
		
		if (playerIn.isSneaking() && !ModUtil.isHoldingHammer(playerIn)) {
			if (this.getPocket() != null) {
				if (!(this.getPocket().isGenerated()) && world.isRemote) {
					this.getPocket().setCreator(playerIn.getName());
					//NetworkHandler.sendCreatorPacketToServer(playerIn.getName(), pos);
					this.markDirty();
				} else if (this.getPocket().getCreator() == null) {
					this.getPocket().setCreator(playerIn.getName());
					//NetworkHandler.sendCreatorPacketToServer(playerIn.getName(), pos);
					this.markDirty();
				} else { 
					String creator = this.getPocket().getCreator();
	
					if (creator != null) {
						if (this.locked) {
							if (playerIn.getName().equals(creator)) {
								this.shiftIntoPocket(playerIn, pos);
							} else {
								TextComponentString comp = new TextComponentString(TextUtil.RED + TextUtil.BOLD + "This pocket has been locked by: [" + creator + "]");
								playerIn.sendMessage(comp);
							}
						} else {
							this.shiftIntoPocket(playerIn, pos);
						}
					} else {
						this.shiftIntoPocket(playerIn, pos);
					}
					return true;
				}
			}
		} else if (!playerIn.isSneaking() && !ModUtil.isHoldingHammer(playerIn)) {
			if (!(worldIn.isRemote)) {
				String creator = this.getPocket().getCreator();
				String locked_comp;

				if (this.locked) {
					locked_comp = TextUtil.RED + TextUtil.BOLD + "[Locked]";
				} else {
					locked_comp = TextUtil.GREEN + TextUtil.BOLD + "[Unlocked]";
				}

				TextComponentString comp = new TextComponentString(TextUtil.TEAL + TextUtil.BOLD + "Pocket is owned by: [" + creator + "] " + locked_comp);
				playerIn.sendMessage(comp);
			}
			return true;

		} else if ((ModUtil.isHoldingHammer(playerIn)) && (playerIn.isSneaking())) {
			playerIn.swingArm(EnumHand.MAIN_HAND);
			if (!(worldIn.isRemote)) {

				String creator = this.getPocket().getCreator();

				if (creator != null) {
					if (playerIn.getName().equals(creator)) {
						if (this.locked) {
							TextComponentString comp = new TextComponentString(TextUtil.TEAL + TextUtil.BOLD + "To remove this pocket, you must unlock it.");
							playerIn.sendMessage(comp);
						} else {
							DimUtils.spawnItemStack(this.generateItemStackOnRemoval(pos), world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0);
							world.setBlockToAir(pos);
						}
					} else {
						TextComponentString comp = new TextComponentString(TextUtil.RED + TextUtil.BOLD + "Only the creator of this pocket can remove it.");
						playerIn.sendMessage(comp);
					}
				}

				return false;
			}
		} else if ((ModUtil.isHoldingHammer(playerIn) && !(playerIn.isSneaking()))) {
			String creator = this.getPocket().getCreator();
			if (playerIn.getName().equals(creator)) {
				playerIn.swingArm(hand);
				this.locked = !(this.locked);

				this.markDirty();

				if (!(worldIn.isRemote)) {
					if (locked) {
						TextComponentString comp = new TextComponentString(TextUtil.RED + TextUtil.BOLD + "Pocket locked.");
						playerIn.sendMessage(comp);
					} else {
						TextComponentString comp = new TextComponentString(TextUtil.GREEN + TextUtil.BOLD + "Pocket unlocked.");
						playerIn.sendMessage(comp);
					}
				}
				return false;
			} else {
				if (!(worldIn.isRemote)) {
					TextComponentString comp = new TextComponentString(TextUtil.RED + TextUtil.BOLD + "You cannot access the lock feature of this pocket.");
					playerIn.sendMessage(comp);
				}
			}
		}
		return true;
	}

	public ItemStack generateItemStackOnRemoval(BlockPos pos) {
		ItemStack itemStack = new ItemStack(BlockHandler.BLOCK_DIMENSIONAL_POCKET);

		if (!itemStack.hasTagCompound()) {
			itemStack.setTagCompound(new NBTTagCompound());
		}

		BlockPos chunkSet = this.getPocket().getChunkPos();
		NBTTagCompound tag = new NBTTagCompound();

		tag.setInteger("X", chunkSet.getX());
		tag.setInteger("Y", chunkSet.getY());
		tag.setInteger("Z", chunkSet.getZ());

		itemStack.getTagCompound().setTag("chunk_set", tag);

		String creatorLore = null;
		Pocket pocket = getPocket();
		if (pocket != null && pocket.getCreator() != null) {
			creatorLore = TextUtil.LIGHT_BLUE + TextUtil.BOLD + "Creator: [" + pocket.getCreator() + "]";
		}

		BlockPos blockSet = new BlockPos(chunkSet.getX() << 4, chunkSet.getY() << 4, chunkSet.getZ() << 4);

		itemStack = DimUtils.generateItem(itemStack, customName, false, TextUtil.LIGHT_GRAY + TextUtil.BOLD
				+ "Pocket: [" + blockSet.getX() + " | " + blockSet.getY() + " | " + blockSet.getZ() + "]", creatorLore);
		return itemStack;
	}

	public ItemStack generateItemStackWithNBT(BlockPos pos, int x, int y, int z) {
		ItemStack item_stack = new ItemStack(BlockHandler.BLOCK_DIMENSIONAL_POCKET);

		if (!item_stack.hasTagCompound()) {
			item_stack.setTagCompound(new NBTTagCompound());
		}

		NBTTagCompound tag_new = new NBTTagCompound();

		tag_new.setInteger("X", x);
		tag_new.setInteger("Y", y);
		tag_new.setInteger("Z", z);

		item_stack.getTagCompound().setTag("chunk_set", tag_new);

		String creatorLore = null;
		Pocket pocket = getPocket();
		if (pocket != null && pocket.getCreator() != null) {
			creatorLore = "Creator: [" + pocket.getCreator() + "]";
		}

		item_stack = DimUtils.generateItem(item_stack, customName, false,
				"Pocket: [" + (x << 4) + "," + (y << 4) + "," + (z << 4) + "]", creatorLore);

		return item_stack;
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

		compound.setBoolean("locked", this.locked);

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

		this.locked = tag.getBoolean("locked");

		if (world != null && world.isRemote) {
			pocket = Pocket.readFromNBT(tag);
		}

		String tempString = tag.getString(TAG_CUSTOM_DP_NAME);
		if (!tempString.isEmpty()) {
			customName = tempString;
		}
	}

	private void shiftIntoPocket(EntityPlayer player, BlockPos pos_) {
		//if (!world.isRemote) {
			this.getPocket().addBlockPosToArray(getPos());

			this.getPocket().shiftTo(player);
		//}
	}

	public boolean getState() {
		return this.locked;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	
}