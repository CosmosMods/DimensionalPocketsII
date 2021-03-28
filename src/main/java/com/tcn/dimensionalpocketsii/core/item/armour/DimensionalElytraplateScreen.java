package com.tcn.dimensionalpocketsii.core.item.armour;

import java.util.List;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.common.chat.CosmosChatUtil;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.cosmoslibrary.common.comp.CosmosColour;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper.Value;
import com.tcn.cosmoslibrary.common.item.CosmosElytraArmourItem;
import com.tcn.cosmoslibrary.common.math.ChunkPos;
import com.tcn.dimensionalpocketsii.core.management.CoreModBusManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityPocket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class DimensionalElytraplateScreen extends CosmosElytraArmourItem {

	public DimensionalElytraplateScreen(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builderIn, Item item, boolean damageableIn) {
		super(materialIn, slot, builderIn, item, damageableIn);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		
		if (!CosmosCompHelper.isShiftKeyDown(Minecraft.getInstance())) {
			tooltip.add(CosmosCompHelper.getTooltipInfo("dimensionalpocketsii.info.elytraplate_s_info"));
			
			if (CosmosCompHelper.displayShiftForDetail) {
				tooltip.add(CosmosCompHelper.shiftForMoreDetails());
			}
		} else {
			tooltip.add(CosmosCompHelper.getTooltipOne("dimensionalpocketsii.info.elytraplate_s_shift_one"));
			tooltip.add(CosmosCompHelper.getTooltipTwo("dimensionalpocketsii.info.elytraplate_s_shift_two").append(CosmosCompHelper.locComp(CosmosColour.GREEN, false, CoreModBusManager.SUIT_GUI.getKey().getName()).append(CosmosCompHelper.locComp(CosmosColour.GREEN, false, "dimensionalpocketsii.info.elytraplate_key"))));
			tooltip.add(CosmosCompHelper.getTooltipThree("dimensionalpocketsii.info.elytraplate_s_shift_three"));
			tooltip.add(CosmosCompHelper.getTooltipFour("dimensionalpocketsii.info.elytraplate_s_shift_four"));
			tooltip.add(CosmosCompHelper.shiftForLessDetails());
		}
		
		if (stack.hasTag()) {
			CompoundNBT tag = stack.getTag();
			if (tag.contains("nbt_data")) {
				CompoundNBT nbt_data = tag.getCompound("nbt_data");
				
				if (!CosmosCompHelper.isControlKeyDown(Minecraft.getInstance())) {
					tooltip.add(CosmosCompHelper.ctrlForMoreDetails());
				} else {
						
					if (nbt_data.contains("chunk_pos")) {
						CompoundNBT chunk_tag = nbt_data.getCompound("chunk_pos");
						
						int x = chunk_tag.getInt("x");
						int z = chunk_tag.getInt("z");
						
						tooltip.add(CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.info.shifter.pocket").append(CosmosCompHelper.locComp(Value.LIGHT_GRAY + "[ " + Value.BRIGHT_BLUE + x + Value.LIGHT_GRAY + ", " + Value.BRIGHT_BLUE + z + Value.LIGHT_GRAY + " ]")));
					}
					
					if (nbt_data.contains("dimension")) {
						CompoundNBT dimension = nbt_data.getCompound("dimension");
						String path = dimension.getString("path");
						
						tooltip.add(CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.info.shifter_source_dimension").append(CosmosCompHelper.locComp(Value.LIGHT_GRAY + "[ " + Value.WHITE + path + Value.LIGHT_GRAY + " ]")));
					}
					tooltip.add(CosmosCompHelper.ctrlForLessDetails());
				}
			}
		}
	}

	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player) {
		return true;
	}
	
	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
		PlayerEntity playerIn = context.getPlayer();
		BlockPos pos = context.getClickedPos();
		World world = context.getLevel();
		TileEntity entity = world.getBlockEntity(pos);
		
		if (entity != null) {
			if (entity instanceof TileEntityPocket) {
				Pocket pocket = ((TileEntityPocket) entity).getPocket();
				
				if (pocket != null) {
					ChunkPos chunk_pos = pocket.getChunkPos();

					int x = chunk_pos.getX();
					int z = chunk_pos.getZ();
					
					if (playerIn.isShiftKeyDown()) {
						if (!stack.hasTag()) {
							CompoundNBT stack_tag = new CompoundNBT();
							CompoundNBT nbt_data = new CompoundNBT();
							
							CompoundNBT chunk_tag = new CompoundNBT();
							chunk_tag.putInt("x", x);
							chunk_tag.putInt("z", z);
							nbt_data.put("chunk_pos", chunk_tag);
							nbt_data.putInt("colour", pocket.getDisplayColour());
							
							CompoundNBT dimension = new CompoundNBT();
							dimension.putString("namespace", world.dimension().location().getNamespace());
							dimension.putString("path", world.dimension().location().getPath());
							nbt_data.put("dimension", dimension);
							
							stack_tag.put("nbt_data", nbt_data);
	
							stack.setTag(stack_tag);
							CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.locComp(CosmosColour.PURPLE, false, "dimensionalpocketsii.item.message.elytraplate.linked").append(CosmosCompHelper.locComp(Value.LIGHT_GRAY + " {" + x + ", " + z + "}")));
							
							playerIn.swing(Hand.MAIN_HAND);
							return ActionResultType.FAIL;
						} else {
							CompoundNBT stack_tag = stack.getTag();
							
							if (stack_tag.contains("nbt_data")) {
								CompoundNBT nbt_data = stack_tag.getCompound("nbt_data");
								
								CompoundNBT chunk_tag = nbt_data.getCompound("chunk_pos");
								chunk_tag.putInt("x", x);
								chunk_tag.putInt("z", z);
								
								CompoundNBT dimension = nbt_data.getCompound("dimension");
								dimension.putString("namespace", world.dimension().location().getNamespace());
								dimension.putString("path", world.dimension().location().getPath());
								
								stack_tag.put("nbt_data", nbt_data);
								CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.locComp(CosmosColour.PURPLE, false, "dimensionalpocketsii.item.message.elytraplate.linked").append(CosmosCompHelper.locComp(Value.LIGHT_GRAY + " {" + x + ", " + z + "}")));
								
								playerIn.swing(Hand.MAIN_HAND);
								return ActionResultType.FAIL;
							} else {
								CompoundNBT nbt_data = new CompoundNBT();
								
								CompoundNBT chunk_tag = new CompoundNBT();
								chunk_tag.putInt("x", x);
								chunk_tag.putInt("z", z);
								nbt_data.put("chunk_pos", chunk_tag);
								nbt_data.putInt("colour", pocket.getDisplayColour());
								
								CompoundNBT dimension = new CompoundNBT();
								dimension.putString("namespace", world.dimension().location().getNamespace());
								dimension.putString("path", world.dimension().location().getPath());
								nbt_data.put("dimension", dimension);
								
								stack_tag.put("nbt_data", nbt_data);
		
								stack.setTag(stack_tag);
								CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.locComp(CosmosColour.PURPLE, false, "dimensionalpocketsii.item.message.elytraplate.linked").append(CosmosCompHelper.locComp(Value.LIGHT_GRAY + " {" + x + ", " + z + "}")));
								
								playerIn.swing(Hand.MAIN_HAND);
								return ActionResultType.FAIL;
							}
						}
					}
				}
			}
		}
		return ActionResultType.FAIL;
	}
	
	@Override
	public boolean isDamageable(ItemStack stack) {
		return false;
	}

}