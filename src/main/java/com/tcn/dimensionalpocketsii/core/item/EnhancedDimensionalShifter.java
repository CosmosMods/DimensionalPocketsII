package com.tcn.dimensionalpocketsii.core.item;

import java.util.List;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.client.impl.util.TextHelper;
import com.tcn.cosmoslibrary.impl.util.CosmosChatUtil;
import com.tcn.cosmoslibrary.math.ChunkPos;
import com.tcn.dimensionalpocketsii.core.management.CoreDimensionManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.shift.Shifter;
import com.tcn.dimensionalpocketsii.pocket.core.shift.ShifterCore;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityPocket;
import com.tcn.dimensionalpocketsii.pocket.core.util.PocketUtil;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class EnhancedDimensionalShifter extends Item {

	public String info = "Allows access to a Pocket from anywhere!";
	public String shift_desc_one = "To link: Shift-Right click this item on a Pocket Block!";
	public String shift_desc_two = "To use: Shift-Right click this item facing air!";
	public String shift_desc_three = "To change mode: Right click this item facing air!";
	public String limitation = "Unlimited Charges.";
	
	public EnhancedDimensionalShifter(Item.Properties properties) {
		super(properties.maxStackSize(1));
	}
	
	@Override
	public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
		return false;
	}
	
	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player) {
		return true;
	}
	
	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
		PlayerEntity playerIn = context.getPlayer();
		
		BlockPos pos = context.getPos();
		BlockPos player_pos = playerIn.getPosition();
		World world = context.getWorld();
		TileEntity entity = world.getTileEntity(pos);
		
		if (entity != null) {
			if (entity instanceof TileEntityPocket) {
				Pocket pocket = ((TileEntityPocket) entity).getPocket();
				
				if (pocket != null) {
					ChunkPos pocket_get_pos = pocket.getChunkPos();
			
					CompoundNBT stack_tag = new CompoundNBT();
					CompoundNBT nbt_data = new CompoundNBT();
					CompoundNBT chunk_tag = new CompoundNBT();
					
					if (playerIn.isSneaking()) {
						int x = pocket_get_pos.getX();
						int z = pocket_get_pos.getZ();
						
						chunk_tag.putInt("x", x);
						chunk_tag.putInt("z", z);
						
						nbt_data.put("chunk_pos", chunk_tag);
						stack_tag.put("nbt_data", nbt_data);

						if (!(PocketUtil.isDimensionEqual(world, CoreDimensionManager.POCKET_WORLD))) {
							CompoundNBT pos_tag = new CompoundNBT();
							pos_tag.putInt("x", player_pos.getX());
							pos_tag.putInt("y", player_pos.getY());
							pos_tag.putInt("z", player_pos.getZ());
							pos_tag.putFloat("yaw", playerIn.getPitchYaw().y);
							pos_tag.putFloat("pitch", playerIn.getPitchYaw().x);
							pos_tag.putBoolean("tele_to_block", true);
							
							stack_tag.put("player_pos", pos_tag);
						}
						
						stack.setTag(stack_tag);
						CosmosChatUtil.sendPlayerMessage(playerIn, false, TextHelper.PURPLE + "Dimensional Shifter Linked to Pocket:" + TextHelper.LIGHT_GRAY + " {" + x + ", " + z + "}");
						
						playerIn.swingArm(Hand.MAIN_HAND);
						return ActionResultType.FAIL;
					}
				}
			}
			
		}
		return ActionResultType.FAIL;
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		BlockPos player_pos_actual = playerIn.getPosition();
		
		if (stack.hasTag()) {
			CompoundNBT stack_tag = stack.getTag();
			
			if (playerIn.isSneaking()) {
				if (stack_tag.contains("nbt_data")) {
					CompoundNBT nbt_data = stack_tag.getCompound("nbt_data");
					
					if (nbt_data.contains("chunk_pos")) {
						CompoundNBT chunk_tag = nbt_data.getCompound("chunk_pos");
						if (stack_tag.contains("player_pos")) {
							CompoundNBT player_pos = stack_tag.getCompound("player_pos");
							
							int player_x = player_pos.getInt("x");
							int player_y = player_pos.getInt("y");
							int player_z = player_pos.getInt("z");
							float player_pitch = player_pos.getFloat("pitch");
							float player_yaw = player_pos.getFloat("yaw");
							boolean tele_to_block = player_pos.getBoolean("tele_to_block");
							
							BlockPos teleport_pos = new BlockPos(player_x, player_y, player_z);
							
							int x = chunk_tag.getInt("x");
							int z = chunk_tag.getInt("z");
							
							ChunkPos chunk_pos = new ChunkPos(x, z);
							ChunkPos chunk_pos_actual = PocketUtil.scaleToChunkPos(playerIn.getPosition());
							Pocket pocket = PocketRegistryManager.getPocketFromChunk(chunk_pos);
							
							if (pocket != null) {
								if (PocketUtil.isDimensionEqual(worldIn, CoreDimensionManager.POCKET_WORLD)) {
									if (chunk_pos.equals(chunk_pos_actual)) {
										if (tele_to_block) {
											pocket.shift(playerIn, EnumShiftDirection.LEAVE, null, stack);
										} else {
											Shifter shifter = Shifter.createTeleporter(pocket.getSourceBlockDimension(), EnumShiftDirection.LEAVE, teleport_pos, player_yaw, player_pitch);
											
											ShifterCore.shiftPlayerToDimension(playerIn, shifter);
											
											return ActionResult.resultPass(playerIn.getHeldItem(handIn));
										}
									} else {
										pocket.shift(playerIn, EnumShiftDirection.ENTER, null, stack);
										
										return ActionResult.resultPass(playerIn.getHeldItem(handIn));
									}
								} else {
									CompoundNBT pos_tag = new CompoundNBT();
									pos_tag.putInt("x", player_pos_actual.getX());
									pos_tag.putInt("y", player_pos_actual.getY());
									pos_tag.putInt("z", player_pos_actual.getZ());
									pos_tag.putFloat("yaw", playerIn.getPitchYaw().y);
									pos_tag.putFloat("pitch", playerIn.getPitchYaw().x);
									pos_tag.putBoolean("tele_to_block", tele_to_block);
									
									stack_tag.put("player_pos", pos_tag);
									stack.setTag(stack_tag);
									
									pocket.shift(playerIn, EnumShiftDirection.ENTER, null, stack);
									
									return ActionResult.resultPass(playerIn.getHeldItem(handIn));
								}
							} else {
								CosmosChatUtil.sendPlayerMessage(playerIn, false, TextHelper.LIGHT_RED + "Your Dimensional Shifter is out of charges!");
							}
						}
					}
				}
			} else {
				if (stack_tag.contains("player_pos")) {
					CompoundNBT player_pos_tag = stack_tag.getCompound("player_pos");
					boolean change = player_pos_tag.getBoolean("tele_to_block");
					
					player_pos_tag.putBoolean("tele_to_block", !change);
					stack_tag.put("player_pos", player_pos_tag);
					stack.setTag(stack_tag);
					
					if (change) {
						CosmosChatUtil.sendPlayerMessage(playerIn, false, TextHelper.PURPLE + "Mode changed: " + TextHelper.LIGHT_GRAY + "{Teleport to Block: " + TextHelper.RED + "False" + TextHelper.LIGHT_GRAY + "}");
					} else {
						CosmosChatUtil.sendPlayerMessage(playerIn, false, TextHelper.PURPLE + "Mode changed: " + TextHelper.LIGHT_GRAY + "{Teleport to Block: " + TextHelper.GREEN + "True" + TextHelper.LIGHT_GRAY + "}");
					}
				}
			}
		} else {
			CosmosChatUtil.sendPlayerMessage(playerIn, false, TextHelper.LIGHT_RED + "You havent linked this Shifter to a Pocket yet!");
		}
		
		return ActionResult.resultPass(playerIn.getHeldItem(handIn));
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		if (!TextHelper.isShiftKeyDown(Minecraft.getInstance())) {
			tooltip.add(new StringTextComponent(TextHelper.getInfoText(this.info)));
			
			if (TextHelper.displayShiftForDetail) {
				tooltip.add(new StringTextComponent(TextHelper.shiftForMoreDetails()));
			}
		} else {
			tooltip.add(new StringTextComponent(TextHelper.getDescOneText(shift_desc_one)));
			tooltip.add(new StringTextComponent(TextHelper.getDescTwoText(shift_desc_two)));
			tooltip.add(new StringTextComponent(TextHelper.getDescThreeText(shift_desc_three)));
			tooltip.add(new StringTextComponent(TextHelper.ORANGE + limitation + TextHelper.END));
			
			tooltip.add(new StringTextComponent(TextHelper.shiftForLessDetails()));
		}
		
		if (stack.hasTag()) {
			CompoundNBT tag = stack.getTag();
			
			if (tag.contains("nbt_data")) {
				CompoundNBT compound_tag = tag.getCompound("nbt_data");
				
				if (compound_tag.contains("chunk_pos")) {
					CompoundNBT pos_tag = compound_tag.getCompound("chunk_pos");
					
					int x = pos_tag.getInt("x");
					int z = pos_tag.getInt("z");
					
					tooltip.add(new StringTextComponent(TextHelper.GRAY + "Currently linked to Pocket: " + TextHelper.LIGHT_GRAY + "[ " + TextHelper.BRIGHT_BLUE + x + TextHelper.LIGHT_GRAY + ", " + TextHelper.BRIGHT_BLUE + z + TextHelper.LIGHT_GRAY + " ]"));
				}
				
				if (tag.contains("player_pos")) {
					CompoundNBT player_pos = tag.getCompound("player_pos");
					
					int x = player_pos.getInt("x");
					int y = player_pos.getInt("y");
					int z = player_pos.getInt("z");
					
					boolean tele_to_block = player_pos.getBoolean("tele_to_block");
					
					if (tele_to_block) {
						tooltip.add(new StringTextComponent(TextHelper.GRAY + "Teleport to Block: " + TextHelper.LIGHT_GRAY + "[ " + TextHelper.GREEN + "True" + TextHelper.LIGHT_GRAY + " ]"));
					} else {
						tooltip.add(new StringTextComponent(TextHelper.GRAY + "Teleport to Block: " + TextHelper.LIGHT_GRAY + "[ " + TextHelper.RED + "False" + TextHelper.LIGHT_GRAY + " ]"));
					}
					tooltip.add(new StringTextComponent(TextHelper.GRAY + "Saved Player Position: " + TextHelper.LIGHT_GRAY + "[ " + TextHelper.TEAL + x + TextHelper.LIGHT_GRAY + ", " + TextHelper.TEAL + y + TextHelper.LIGHT_GRAY + ", " + TextHelper.TEAL + z + TextHelper.LIGHT_GRAY + " ]"));
				}
			}
		}
	}
	
	@Override
	public boolean hasEffect(ItemStack itemStack) {
		return true;	
	}
}