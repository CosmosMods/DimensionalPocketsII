package com.tcn.dimensionalpocketsii.core.item;

import java.util.List;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.common.chat.CosmosChatUtil;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.cosmoslibrary.common.comp.CosmosColour;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper.Value;
import com.tcn.cosmoslibrary.common.math.ChunkPos;
import com.tcn.dimensionalpocketsii.core.management.CoreDimensionManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.shift.Shifter;
import com.tcn.dimensionalpocketsii.pocket.core.shift.ShifterCore;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityCharger;
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
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class DimensionalShifter extends Item {

	public DimensionalShifter(Item.Properties properties) {
		super(properties.stacksTo(1));
	}
	
	@Override
	public boolean canAttackBlock(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
		return false;
	}
	
	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player) {
		return true;
	}
	
	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
		PlayerEntity playerIn = context.getPlayer();
		
		BlockPos pos = context.getClickedPos();
		BlockPos player_pos = playerIn.blockPosition();
		World world = context.getLevel();
		TileEntity entity = world.getBlockEntity(pos);
		
		if (entity != null) {
			if (entity instanceof TileEntityPocket) {
				Pocket pocket = ((TileEntityPocket) entity).getPocket();
				
				if (pocket != null) {
					ChunkPos pocket_get_pos = pocket.getChunkPos();
					
					CompoundNBT stack_tag = new CompoundNBT();
					CompoundNBT use_data = new CompoundNBT();
					CompoundNBT nbt_data = new CompoundNBT();
					CompoundNBT chunk_tag = new CompoundNBT();
					
					if (playerIn.isShiftKeyDown()) {
						int x = pocket_get_pos.getX();
						int z = pocket_get_pos.getZ();
						
						chunk_tag.putInt("x", x);
						chunk_tag.putInt("z", z);
						
						nbt_data.put("chunk_pos", chunk_tag);
						nbt_data.putInt("colour", pocket.getDisplayColour());
						stack_tag.put("nbt_data", nbt_data);
						
						//if (!(PocketUtil.isDimensionEqual(world, CoreDimensionManager.POCKET_WORLD))) {
							CompoundNBT pos_tag = new CompoundNBT();
							pos_tag.putInt("x", player_pos.getX());
							pos_tag.putInt("y", player_pos.getY());
							pos_tag.putInt("z", player_pos.getZ());
							pos_tag.putFloat("yaw", playerIn.getRotationVector().y);
							pos_tag.putFloat("pitch", playerIn.getRotationVector().x);
							pos_tag.putBoolean("tele_to_block", true);
							pos_tag.putString("dimension", world.dimension().location().getPath());
							
							stack_tag.put("player_pos", pos_tag);
						//}
						
						if (stack.hasTag()) {
							CompoundNBT stack_tag_get = stack.getTag();
							
							if (stack_tag_get.contains("use_data")) {
								CompoundNBT use_data_get = stack_tag_get.getCompound("use_data");
								
								int uses_get = use_data_get.getInt("uses");
								
								use_data.putInt("uses", uses_get);
								stack_tag.put("use_data", use_data);
							}
						} else {
							use_data.putInt("uses", 30);
							stack_tag.put("use_data", use_data);
						}
						
						stack.setTag(stack_tag);
						CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.locComp(CosmosColour.PURPLE, false, "dimensionalpocketsii.item.message.shifter.linked").append(CosmosCompHelper.locComp(Value.LIGHT_GRAY + " {" + x + ", " + z + "}")));
						
						playerIn.swing(Hand.MAIN_HAND);
						return ActionResultType.FAIL;
					}
				}
			} else if (entity instanceof TileEntityCharger) {
				return ActionResultType.PASS;
			}
		}
		return ActionResultType.FAIL;
	}

	@SuppressWarnings("unused")
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		BlockPos player_pos_actual = playerIn.blockPosition();
		BlockPos pos = playerIn.blockPosition();
		
		if (stack.hasTag()) {
			CompoundNBT stack_tag = stack.getTag();
			
			if (playerIn.isShiftKeyDown()) {
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
							ChunkPos chunk_pos_actual = ChunkPos.scaleToChunkPos(playerIn.blockPosition());
							
							Pocket pocket = PocketRegistryManager.getPocketFromChunkPosition(chunk_pos);
							
							if (stack_tag.contains("use_data")) {
								CompoundNBT use_data = stack_tag.getCompound("use_data");
								int uses = use_data.getInt("uses");
								
								if (uses > 0) {
									use_data.putInt("uses", uses - 1);
									stack_tag.put("use_data", use_data);
									
									if (pocket.exists()) {
										if (PocketUtil.isDimensionEqual(worldIn, CoreDimensionManager.POCKET_WORLD)) {
											if (chunk_pos.equals(chunk_pos_actual)) {
												if (tele_to_block) {
													pocket.shift(playerIn, EnumShiftDirection.LEAVE, null, stack);
												} else {
													Shifter shifter = Shifter.createTeleporter(pocket.getSourceBlockDimension(), EnumShiftDirection.LEAVE, teleport_pos, player_yaw, player_pitch, false, true);
													ShifterCore.shiftPlayerToDimension(playerIn, shifter);
													
													return ActionResult.pass(playerIn.getItemInHand(handIn));
												}
											} else {
												pocket.shift(playerIn, EnumShiftDirection.ENTER, null, stack);
												
												return ActionResult.pass(playerIn.getItemInHand(handIn));
											}
										} else {
											CompoundNBT pos_tag = new CompoundNBT();
											pos_tag.putInt("x", player_pos_actual.getX());
											pos_tag.putInt("y", player_pos_actual.getY());
											pos_tag.putInt("z", player_pos_actual.getZ());
											pos_tag.putFloat("yaw", playerIn.getRotationVector().y);
											pos_tag.putFloat("pitch", playerIn.getRotationVector().x);
											pos_tag.putBoolean("tele_to_block", tele_to_block);
											pos_tag.putString("dimension", worldIn.dimension().location().getPath());
											
											stack_tag.put("player_pos", pos_tag);
											stack.setTag(stack_tag);
											
											pocket.shift(playerIn, EnumShiftDirection.ENTER, null, stack);
											
											return ActionResult.pass(playerIn.getItemInHand(handIn));
										}
									}
								} else {
									CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.item.message.shifter.out_of_charges"));
								}
							} else {
								CompoundNBT use_data = new CompoundNBT();
								use_data.putInt("uses", 30);
								stack_tag.put("use_data", use_data);
								
								if (pocket != null) {
									if (worldIn.dimension().equals(CoreDimensionManager.POCKET_WORLD)) {
										pocket.shift(playerIn, EnumShiftDirection.LEAVE, null, stack);
										
										return ActionResult.pass(playerIn.getItemInHand(handIn));
									} else {
										pocket.shift(playerIn, EnumShiftDirection.ENTER, null, stack);
										
										return ActionResult.pass(playerIn.getItemInHand(handIn));
									}
								}
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
					
					if (!change) {
						CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.locComp(CosmosColour.PURPLE, false, "dimensionalpocketsii.item.message.shifter.mode_change")
								.append(CosmosCompHelper.locComp(CosmosColour.LIGHT_GRAY, false, "dimensionalpocketsii.item.message.shifter.mode_change_prefix")
										.append(CosmosCompHelper.locComp(CosmosColour.GREEN, false, "dimensionalpocketsii.item.message.shifter.mode_change_true").append(CosmosCompHelper.locComp(Value.LIGHT_GRAY + " ]")))));
					} else {
						CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.locComp(CosmosColour.PURPLE, false, "dimensionalpocketsii.item.message.shifter.mode_change")
								.append(CosmosCompHelper.locComp(CosmosColour.LIGHT_GRAY, false, "dimensionalpocketsii.item.message.shifter.mode_change_prefix")
										.append(CosmosCompHelper.locComp(CosmosColour.RED, false, "dimensionalpocketsii.item.message.shifter.mode_change_false").append(CosmosCompHelper.locComp(Value.LIGHT_GRAY + " ]")))));
					}
				}
			}
		} else {
			CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.item.message.shifter.not_linked"));
		}
		
		return ActionResult.pass(playerIn.getItemInHand(handIn));
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		
		if (!CosmosCompHelper.isShiftKeyDown(Minecraft.getInstance())) {
			tooltip.add(CosmosCompHelper.getTooltipInfo("dimensionalpocketsii.info.shifter_info"));
			
			if (CosmosCompHelper.displayShiftForDetail) {
				tooltip.add(CosmosCompHelper.shiftForMoreDetails());
			} 
			
		} else {
			tooltip.add(CosmosCompHelper.getTooltipOne("dimensionalpocketsii.info.shifter_shift_one"));
			tooltip.add(CosmosCompHelper.getTooltipTwo("dimensionalpocketsii.info.shifter_shift_two"));
			tooltip.add(CosmosCompHelper.getTooltipThree("dimensionalpocketsii.info.shifter_shift_three"));
			tooltip.add(CosmosCompHelper.getTooltipLimit("dimensionalpocketsii.info.shifter_limitation"));
			
			tooltip.add(CosmosCompHelper.shiftForLessDetails());
		}
		
		if (stack.hasTag()) {
			if (!CosmosCompHelper.isControlKeyDown(Minecraft.getInstance())) {
				tooltip.add(CosmosCompHelper.ctrlForMoreDetails());
			} else {
				CompoundNBT tag = stack.getTag();
				
				if (tag.contains("use_data")) {
					CompoundNBT use_data = tag.getCompound("use_data");
					
					int uses = use_data.getInt("uses");
					
					tooltip.add(CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.info.shifter.charges").append(CosmosCompHelper.locComp(Value.LIGHT_GRAY + "[ " + uses + " ]")));
				}
				
				if (tag.contains("nbt_data")) {
					CompoundNBT compound_tag = tag.getCompound("nbt_data");
					
					if (compound_tag.contains("chunk_pos")) {
						CompoundNBT pos_tag = compound_tag.getCompound("chunk_pos");
						
						int x = pos_tag.getInt("x");
						int z = pos_tag.getInt("z");
						tooltip.add(CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.info.shifter.pocket").append(CosmosCompHelper.locComp(Value.LIGHT_GRAY + "[ " + Value.BRIGHT_BLUE + x + Value.LIGHT_GRAY + ", " + Value.BRIGHT_BLUE + z + Value.LIGHT_GRAY + " ]")));
					}
				}
				
				if (tag.contains("player_pos")) {
					CompoundNBT player_pos = tag.getCompound("player_pos");
					
					int x = player_pos.getInt("x");
					int y = player_pos.getInt("y");
					int z = player_pos.getInt("z");
					boolean tele_to_block = player_pos.getBoolean("tele_to_block");
					String dimension = player_pos.getString("dimension");
					
					if (tele_to_block) {
						tooltip.add(CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.item.message.shifter.mode_change_prefix").append(CosmosCompHelper.locComp(Value.LIGHT_GRAY + "[ ")
								.append(CosmosCompHelper.locComp(CosmosColour.GREEN, false, "dimensionalpocketsii.item.message.shifter.mode_change_true")).append(CosmosCompHelper.locComp(Value.LIGHT_GRAY + " ]"))));
					} else {
						tooltip.add(CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.item.message.shifter.mode_change_prefix").append(CosmosCompHelper.locComp(Value.LIGHT_GRAY + "[ ")
								.append(CosmosCompHelper.locComp(CosmosColour.RED, false, "dimensionalpocketsii.item.message.shifter.mode_change_false")).append(CosmosCompHelper.locComp(Value.LIGHT_GRAY + " ]"))));
					}
					
					tooltip.add(CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.info.shifter_player_pos").append(CosmosCompHelper.locComp(Value.LIGHT_GRAY + "[ " + Value.CYAN + x + Value.LIGHT_GRAY + ", " + Value.CYAN + y + Value.LIGHT_GRAY + ", " + Value.CYAN + z + Value.LIGHT_GRAY + " ]")));
					tooltip.add(CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.info.shifter_source_dimension").append(CosmosCompHelper.locComp(Value.LIGHT_GRAY + "[ " + Value.WHITE + dimension + Value.LIGHT_GRAY + " ]")));
								
				}
				tooltip.add(CosmosCompHelper.ctrlForLessDetails());
			}
		}
	}
	
	@Override
	public boolean isDamageable(ItemStack stack) {
		return false;
	}
}