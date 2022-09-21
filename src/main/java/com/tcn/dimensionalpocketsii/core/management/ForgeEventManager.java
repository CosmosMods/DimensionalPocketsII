package com.tcn.dimensionalpocketsii.core.management;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tcn.cosmoslibrary.common.chat.CosmosChatUtil;
import com.tcn.cosmoslibrary.common.lib.ComponentHelper;
import com.tcn.cosmoslibrary.common.lib.CosmosChunkPos;
import com.tcn.cosmoslibrary.energy.item.CosmosEnergyArmourItemColourable;
import com.tcn.dimensionalpocketsii.DimReference;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.client.screen.ScreenElytraplateVisor;
import com.tcn.dimensionalpocketsii.core.item.armour.DimensionalElytraplate;
import com.tcn.dimensionalpocketsii.core.item.armour.ElytraSettings;
import com.tcn.dimensionalpocketsii.core.item.armour.module.BaseElytraModule;
import com.tcn.dimensionalpocketsii.core.network.PacketDimensionChange;
import com.tcn.dimensionalpocketsii.core.network.elytraplate.PacketElytraItemStackTagUpdate;
import com.tcn.dimensionalpocketsii.core.network.elytraplate.PacketElytraShift;
import com.tcn.dimensionalpocketsii.core.network.elytraplate.PacketElytraUseEnergy;
import com.tcn.dimensionalpocketsii.core.network.elytraplate.PacketElytraplateOpenConnector;
import com.tcn.dimensionalpocketsii.core.network.elytraplate.PacketElytraplateOpenEnderChest;
import com.tcn.dimensionalpocketsii.core.network.elytraplate.PacketElytraplateOpenSettings;
import com.tcn.dimensionalpocketsii.pocket.core.chunkloading.ChunkTrackerBlock;
import com.tcn.dimensionalpocketsii.pocket.core.chunkloading.ChunkTrackerRoom;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.util.PocketUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent.Key;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@SuppressWarnings({ "unused" })
@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
public class ForgeEventManager {

	@SubscribeEvent
	public static void onLivingEquipmentChangeEvent(final LivingEquipmentChangeEvent event) {
		EquipmentSlot slot = event.getSlot();
		
		if (!(slot.equals(EquipmentSlot.MAINHAND)) && !(slot.equals(EquipmentSlot.OFFHAND))) {
			LivingEntity entity = event.getEntity();
	
			if (!(entity instanceof Player)) {
				ItemStack stackTo = event.getTo();
				
				if (stackTo.getItem() instanceof CosmosEnergyArmourItemColourable) {
					CosmosEnergyArmourItemColourable item = (CosmosEnergyArmourItemColourable) stackTo.getItem();
					
					item.setDamage(stackTo, 0);
					
					if (!item.hasEnergy(stackTo)) {
						item.setEnergy(stackTo, 1000);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onRenderGameOverlayEvent(RenderGuiOverlayEvent.Post event) {
		ScreenElytraplateVisor screenSettings = new ScreenElytraplateVisor();
		
		Minecraft mc = Minecraft.getInstance();
		PoseStack type = event.getPoseStack();
		Player player = mc.player;
		Inventory playerInventory = player.getInventory();
		
		if (playerInventory.getArmor(2).getItem().equals(ObjectManager.dimensional_elytraplate)) {
			if (screenSettings != null) {
				type.pushPose();
				screenSettings.renderOverlay(event.getPoseStack());
				type.popPose();
			}
		}
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onKeyPressEvent(Key event) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer playerIn = mc.player;
		
		if (playerIn != null) {
			Level world = playerIn.level;
			if (ModBusManager.SUIT_SCREEN.isDown()) {
				if (playerIn.getInventory().getArmor(2).getItem() != null) {
					ItemStack armourStack = playerIn.getInventory().getArmor(2);
					Item armour = armourStack.getItem();
					
					if (armour instanceof DimensionalElytraplate) {
						DimensionalElytraplate elytraplate = (DimensionalElytraplate) armour;
						
						if (DimensionalElytraplate.hasModuleInstalled(armourStack, BaseElytraModule.SCREEN)) {
							if (elytraplate.hasEnergy(armourStack)) {
								NetworkManager.sendToServer(new PacketElytraplateOpenConnector(playerIn.getUUID(), 2));
								NetworkManager.sendToServer(new PacketElytraUseEnergy(playerIn.getUUID(), 2, elytraplate.getMaxUse(armourStack)));
							} else {
								CosmosChatUtil.sendClientPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.item.message.elytraplate.no_energy"));
							}
						} else {
							CosmosChatUtil.sendClientPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.item.message.elytraplate.no_screen"));
						}
					}
				}
			} 
			
			else if (ModBusManager.SUIT_SCREEN_ENDER_CHEST.isDown()) {
				if (playerIn.getInventory().getArmor(2).getItem() != null) {
					ItemStack armourStack = playerIn.getInventory().getArmor(2);
					Item armour = armourStack.getItem();
					
					if (armour instanceof DimensionalElytraplate) {
						DimensionalElytraplate elytraplate = (DimensionalElytraplate) armour;
						
						if (DimensionalElytraplate.hasModuleInstalled(armourStack, BaseElytraModule.ENDER_CHEST)) {
							if (elytraplate.hasEnergy(armourStack)) {
								NetworkManager.sendToServer(new PacketElytraplateOpenEnderChest(playerIn.getUUID(), 2));
								NetworkManager.sendToServer(new PacketElytraUseEnergy(playerIn.getUUID(), 2, elytraplate.getMaxUse(armourStack)));
							} else {
								CosmosChatUtil.sendClientPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.item.message.elytraplate.no_energy"));
							}
						} else {
							CosmosChatUtil.sendClientPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.item.message.elytraplate.no_ender_chest"));
						}
					}
				}
			}  
			
			else if (ModBusManager.SUIT_SETTINGS.isDown()) {
				if (playerIn.getInventory().getArmor(2).getItem() != null) {
					ItemStack armourStack = playerIn.getInventory().getArmor(2);
					
					if (armourStack.getItem() instanceof DimensionalElytraplate) {
						DimensionalElytraplate elytraplate = (DimensionalElytraplate) armourStack.getItem();
						
						if (elytraplate.hasEnergy(armourStack)) {
							NetworkManager.sendToServer(new PacketElytraplateOpenSettings(playerIn.getUUID(), 2));
						} else {
							CosmosChatUtil.sendClientPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.item.message.elytraplate.no_energy"));
						}
					}
				}
			}
		
			else if (ModBusManager.SUIT_SHIFT.isDown()) {
				if (playerIn.getInventory().getArmor(2).getItem() != null) {
					ItemStack armourStack = playerIn.getInventory().getArmor(2);
					Item armour = armourStack.getItem();
					BlockPos player_pos_actual = playerIn.blockPosition();
					
					if (armour instanceof DimensionalElytraplate) {
						DimensionalElytraplate elytraplate = (DimensionalElytraplate) armour;
						
						if (DimensionalElytraplate.hasModuleInstalled(armourStack, BaseElytraModule.SHIFTER)) {
							if (armourStack.hasTag()) {
								CompoundTag stack_nbt = armourStack.getTag();
								
								if (stack_nbt.contains("nbt_data")) {
									CompoundTag nbt_data = stack_nbt.getCompound("nbt_data");
									
									if (nbt_data.contains("chunk_pos")) {
										CompoundTag chunk_pos = nbt_data.getCompound("chunk_pos");
										
										if (nbt_data.contains("player_pos")) {
											CompoundTag player_pos = nbt_data.getCompound("player_pos");
											
											int player_x = player_pos.getInt("x");
											int player_y = player_pos.getInt("y");
											int player_z = player_pos.getInt("z");
											float player_pitch = player_pos.getFloat("pitch");
											float player_yaw = player_pos.getFloat("yaw");
											
											boolean tele_to_block = DimensionalElytraplate.getElytraSetting(armourStack, ElytraSettings.TELEPORT_TO_BLOCK)[1];
											
											CompoundTag dim = nbt_data.getCompound("dimension");
											String namespace = dim.getString("namespace");
											String path = dim.getString("path");
											ResourceLocation loc = new ResourceLocation(namespace, path);
											
											ResourceKey<Level> source_dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, loc);
											
											BlockPos teleport_pos = new BlockPos(player_x, player_y, player_z);
											
											int x = chunk_pos.getInt("x");
											int z = chunk_pos.getInt("z");
											
											CosmosChunkPos chunk = new CosmosChunkPos(x, z);
											
											if (elytraplate.hasEnergy(armourStack)) {
												if (PocketUtil.isDimensionEqual(world, DimensionManager.POCKET_WORLD)) {
													if (tele_to_block) {
														NetworkManager.sendToServer(new PacketElytraUseEnergy(playerIn.getUUID(), 2, elytraplate.getMaxUse(armourStack)));
														NetworkManager.sendToServer(new PacketElytraShift(playerIn.getUUID(), world.dimension(), chunk));
													} else {
														NetworkManager.sendToServer(new PacketElytraUseEnergy(playerIn.getUUID(), 2, elytraplate.getMaxUse(armourStack)));
														NetworkManager.sendToServer(new PacketDimensionChange(playerIn.getUUID(), source_dimension, EnumShiftDirection.LEAVE, teleport_pos, player_yaw, player_pitch, false, true, true));
													}
												} else {
													CompoundTag pos_tag = new CompoundTag();
													pos_tag.putInt("x", player_pos_actual.getX());
													pos_tag.putInt("y", player_pos_actual.getY());
													pos_tag.putInt("z", player_pos_actual.getZ());
													pos_tag.putFloat("yaw", playerIn.getRotationVector().y);
													pos_tag.putFloat("pitch", playerIn.getRotationVector().x);
													//pos_tag.putBoolean("tele_to_block", tele_to_block);
													
													CompoundTag dimension = new CompoundTag();
													dimension.putString("namespace", world.dimension().location().getNamespace());
													dimension.putString("path", world.dimension().location().getPath());
													
													nbt_data.put("player_pos", pos_tag);
													nbt_data.put("dimension", dimension);
													stack_nbt.put("nbt_data", nbt_data);
													
													NetworkManager.sendToServer(new PacketElytraItemStackTagUpdate(playerIn.getUUID(), 2, stack_nbt));
													NetworkManager.sendToServer(new PacketElytraUseEnergy(playerIn.getUUID(), 2, elytraplate.getMaxUse(armourStack)));
													NetworkManager.sendToServer(new PacketElytraShift(playerIn.getUUID(), world.dimension(), chunk));
												}
											} else {
												CosmosChatUtil.sendClientPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.item.message.elytraplate.no_energy"));
											}
										}
									}
								} else {
									CosmosChatUtil.sendClientPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.item.message.elytraplate.not_linked"));
								}
							} else {
								CosmosChatUtil.sendClientPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.item.message.elytraplate.not_linked"));
							}
						} else {
							CosmosChatUtil.sendClientPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.item.message.elytraplate.no_shifter"));
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onServerSaveEvent(final LevelEvent.Save event) {
		LevelAccessor world = event.getLevel();
		DimensionType type = world.dimensionType();
		
		if (!type.ultraWarm() && type.natural() && !type.piglinSafe() && !type.respawnAnchorWorks() && 
				type.bedWorks() && type.hasRaids() && type.hasSkyLight() && !type.hasCeiling() &&
					type.coordinateScale() == 1 && type.logicalHeight() == 384 && type.minY() == -64 && 
					type.effectsLocation() == BuiltinDimensionTypes.OVERWORLD_EFFECTS) {
			PocketRegistryManager.saveData();
		}
	}

	@SubscribeEvent
	public static void onServerUnloadEvent(final LevelEvent.Unload event) {
		ChunkTrackerBlock.BLOCKS.clear();
		ChunkTrackerRoom.ROOMS.clear();
		//clearMap();
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedOutEvent(final PlayerEvent.PlayerLoggedOutEvent event) { }
	
	@SubscribeEvent
	public static void onPlayerLoggedInEvent(final PlayerEvent.PlayerLoggedInEvent event) {
		
	}
	
	@OnlyIn(Dist.CLIENT)
	public static void clearMap() {
		PocketRegistryManager.clearPocketMap();
	}
}