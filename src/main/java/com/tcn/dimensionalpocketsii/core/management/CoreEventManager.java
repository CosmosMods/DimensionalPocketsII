package com.tcn.dimensionalpocketsii.core.management;

import java.util.ArrayList;

import com.tcn.cosmoslibrary.client.entity.layer.CosmosElytraArmourLayer;
import com.tcn.cosmoslibrary.client.entity.layer.CosmosElytraLayerStand;
import com.tcn.cosmoslibrary.common.chat.CosmosChatUtil;
import com.tcn.cosmoslibrary.common.comp.CosmosColour;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper.Value;
import com.tcn.cosmoslibrary.common.math.ChunkPos;
import com.tcn.dimensionalpocketsii.DimReference;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.network.PacketDimensionChange;
import com.tcn.dimensionalpocketsii.core.network.PacketElytraItemUpdate;
import com.tcn.dimensionalpocketsii.core.network.PacketElytraShift;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.util.PocketUtil;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.model.ArmorStandArmorModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
public class CoreEventManager {

	private static final ArrayList<ConfiguredFeature<?, ?>> overworldOres = new ArrayList<ConfiguredFeature<?, ?>>();
	private static final ArrayList<ConfiguredFeature<?, ?>> netherOres = new ArrayList<ConfiguredFeature<?, ?>>();
	private static final ArrayList<ConfiguredFeature<?, ?>> endOres = new ArrayList<ConfiguredFeature<?, ?>>();

	private static final String GIVEN_INFO_BOOK = "givenInfoBook";
	
	@SubscribeEvent
	public static void onPlayerChangedDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent event) { }
	
	@SuppressWarnings("unused")
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onKeyPressEvent(KeyInputEvent event) {
		Minecraft mc = Minecraft.getInstance();
		ClientPlayerEntity playerIn = mc.player;
		
		if (playerIn != null) {
			World world = playerIn.level;
			if (CoreModBusManager.SUIT_GUI.isDown()) {
				if (playerIn.inventory.getArmor(2).getItem() != null) {
					ItemStack stack = playerIn.inventory.getArmor(2);
					Item armour = playerIn.inventory.getArmor(2).getItem();
					
					if (armour.equals(CoreModBusManager.DIMENSIONAL_ELYTRAPLATE_SCREEN)) {
						if (stack.hasTag()) {
							CompoundNBT stack_nbt = stack.getTag();
							if (stack_nbt.contains("nbt_data")) {
								CompoundNBT nbt_data = stack_nbt.getCompound("nbt_data");
								if (nbt_data.contains("chunk_pos")) {
									CompoundNBT chunk_pos = nbt_data.getCompound("chunk_pos");
									
									int x = chunk_pos.getInt("x");
									int z = chunk_pos.getInt("z");
									ChunkPos chunk = new ChunkPos(x, z);
									
									CosmosChatUtil.sendClientPlayerMessage(playerIn, CosmosCompHelper.getErrorText("This is a WIP feature. This feature will be implemented in the future."));
								}
								
							} else {
								CosmosChatUtil.sendClientPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.item.message.elytraplate.not_linked"));
							}
						} else {
							CosmosChatUtil.sendClientPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.item.message.elytraplate.not_linked"));
						}
					}
				}
			} 
		
			else if (CoreModBusManager.SUIT_SHIFT.isDown()) {
				if (playerIn.inventory.getArmor(2).getItem() != null) {
					ItemStack stack = playerIn.inventory.getArmor(2);
					Item armour = playerIn.inventory.getArmor(2).getItem();
					BlockPos player_pos_actual = playerIn.blockPosition();
					
					if (armour.equals(CoreModBusManager.DIMENSIONAL_ELYTRAPLATE_SHIFT)) {
						if (stack.hasTag()) {
							CompoundNBT stack_nbt = stack.getTag();
							
							if (stack_nbt.contains("nbt_data")) {
								CompoundNBT nbt_data = stack_nbt.getCompound("nbt_data");
								
								if (nbt_data.contains("chunk_pos")) {
									CompoundNBT chunk_pos = nbt_data.getCompound("chunk_pos");
									
									if (nbt_data.contains("player_pos")) {
										CompoundNBT player_pos = nbt_data.getCompound("player_pos");

										int player_x = player_pos.getInt("x");
										int player_y = player_pos.getInt("y");
										int player_z = player_pos.getInt("z");
										float player_pitch = player_pos.getFloat("pitch");
										float player_yaw = player_pos.getFloat("yaw");
										boolean tele_to_block = player_pos.getBoolean("tele_to_block");
										
										CompoundNBT dim = nbt_data.getCompound("dimension");
										String namespace = dim.getString("namespace");
										String path = dim.getString("path");
										ResourceLocation loc = new ResourceLocation(namespace, path);
										
										RegistryKey<World> source_dimension = RegistryKey.create(Registry.DIMENSION_REGISTRY, loc);
										
										BlockPos teleport_pos = new BlockPos(player_x, player_y, player_z);
										
										int x = chunk_pos.getInt("x");
										int z = chunk_pos.getInt("z");
										
										ChunkPos chunk = new ChunkPos(x, z);
										//Pocket pocket = PocketRegistryManager.getPocketFromChunkPosition(chunk);
	
										//if (pocket.exists()) {
										if (PocketUtil.isDimensionEqual(world, CoreDimensionManager.POCKET_WORLD)) {
											if (tele_to_block) {
												CoreNetworkManager.sendToServer(new PacketElytraShift(playerIn.getUUID(), world.dimension(), chunk));
											} else {
												CoreNetworkManager.sendToServer(new PacketDimensionChange(playerIn.getUUID(), source_dimension, EnumShiftDirection.LEAVE, teleport_pos, player_yaw, player_pitch, false, true));
											}
										} else {
											CompoundNBT pos_tag = new CompoundNBT();
											pos_tag.putInt("x", player_pos_actual.getX());
											pos_tag.putInt("y", player_pos_actual.getY());
											pos_tag.putInt("z", player_pos_actual.getZ());
											pos_tag.putFloat("yaw", playerIn.getRotationVector().y);
											pos_tag.putFloat("pitch", playerIn.getRotationVector().x);
											pos_tag.putBoolean("tele_to_block", tele_to_block);
											
											CompoundNBT dimension = new CompoundNBT();
											dimension.putString("namespace", world.dimension().location().getNamespace());
											dimension.putString("path", world.dimension().location().getPath());
											
											nbt_data.put("player_pos", pos_tag);
											nbt_data.put("dimension", dimension);
											stack_nbt.put("nbt_data", nbt_data);
											
											CoreNetworkManager.sendToServer(new PacketElytraItemUpdate(playerIn.getUUID(), 2, stack_nbt));
											
											CoreNetworkManager.sendToServer(new PacketElytraShift(playerIn.getUUID(), world.dimension(), chunk));
										}
										//}
									}
								}
								
							} else {
								CosmosChatUtil.sendClientPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.item.message.elytraplate.not_linked"));
							}
						} else {
							CosmosChatUtil.sendClientPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.item.message.elytraplate.not_linked"));
						}
					}
				}
			} else if (CoreModBusManager.SUIT_MODE_CHANGE.isDown()) {
				if (playerIn.inventory.getArmor(2).getItem() != null) {
					ItemStack stack = playerIn.inventory.getArmor(2);
					Item armour = playerIn.inventory.getArmor(2).getItem();
					
					if (armour.equals(CoreModBusManager.DIMENSIONAL_ELYTRAPLATE_SHIFT)) {
						if (stack.hasTag()) {
							CompoundNBT stack_nbt = stack.getTag();
							
							if (stack_nbt.contains("nbt_data")) {
								CompoundNBT nbt_data = stack_nbt.getCompound("nbt_data");
								
								if (nbt_data.contains("player_pos")) {
									CompoundNBT player_pos = nbt_data.getCompound("player_pos");
									
									boolean change = player_pos.getBoolean("tele_to_block");
									
									player_pos.putBoolean("tele_to_block", !change);
									nbt_data.put("player_pos", player_pos);
									stack_nbt.put("nbt_data", nbt_data);
									
									CoreNetworkManager.sendToServer(new PacketElytraItemUpdate(playerIn.getUUID(), 2, stack_nbt));
									
									if (!change) {
										CosmosChatUtil.sendClientPlayerMessage(playerIn, CosmosCompHelper.locComp(CosmosColour.PURPLE, false, "dimensionalpocketsii.item.message.shifter.mode_change")
												.append(CosmosCompHelper.locComp(CosmosColour.LIGHT_GRAY, false, "dimensionalpocketsii.item.message.shifter.mode_change_prefix")
														.append(CosmosCompHelper.locComp(CosmosColour.GREEN, false, "dimensionalpocketsii.item.message.shifter.mode_change_true").append(CosmosCompHelper.locComp(Value.LIGHT_GRAY + " ]")))));
									} else {
										CosmosChatUtil.sendClientPlayerMessage(playerIn, CosmosCompHelper.locComp(CosmosColour.PURPLE, false, "dimensionalpocketsii.item.message.shifter.mode_change")
												.append(CosmosCompHelper.locComp(CosmosColour.LIGHT_GRAY, false, "dimensionalpocketsii.item.message.shifter.mode_change_prefix")
														.append(CosmosCompHelper.locComp(CosmosColour.RED, false, "dimensionalpocketsii.item.message.shifter.mode_change_false").append(CosmosCompHelper.locComp(Value.LIGHT_GRAY + " ]")))));
									}
								}
							} else {
								CosmosChatUtil.sendClientPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.item.message.elytraplate.not_linked"));
							}
						} else {
							CosmosChatUtil.sendClientPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.item.message.elytraplate.not_linked"));
						}
					}
				}
			}
		}
	}
	

	@SubscribeEvent
	public static void onBiomeLoadingEvent(BiomeLoadingEvent event) {
		BiomeGenerationSettingsBuilder generation = event.getGeneration();

		if (event.getCategory().equals(Biome.Category.NETHER)) {
			for (ConfiguredFeature<?, ?> ore : netherOres) {
				if (ore != null)
					generation.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ore);
			}
		}
		if (event.getCategory().equals(Biome.Category.THEEND)) {
			for (ConfiguredFeature<?, ?> ore : endOres) {
				if (ore != null)
					generation.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ore);
			}
		}
		for (ConfiguredFeature<?, ?> ore : overworldOres) {
			if (ore != null)
				generation.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ore);
		}
	}
	
	@SubscribeEvent
	public static void onServerSaveEvent(WorldEvent.Save event) {
		PocketRegistryManager.saveData();
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedOutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
		PocketRegistryManager.saveData();
		//clearMap();
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
		Entity entity = event.getEntity();
		World world = entity.level;
		
		if (entity instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) entity;

			if (!hasHadBook(player)) {
				CosmosChatUtil.sendServerPlayerMessage(player, DimReference.MESSAGES.WELCOME);
			}
			
			if (!world.isClientSide) {
				checkIfSpawnWithBook(player);
			}
		}
		PocketRegistryManager.loadData();
	}
	
	@OnlyIn(Dist.CLIENT)
	public static void clearMap() {
		PocketRegistryManager.clearPocketMap();
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onEntityConstructingEvent(EntityConstructing event) {
		Entity entity = event.getEntity();
		
		if (entity instanceof ArmorStandEntity) {
			ArmorStandEntity armorStand = (ArmorStandEntity) entity;
			EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(armorStand);
			
			if (renderer instanceof ArmorStandRenderer) {
				ArmorStandRenderer armorRenderer = (ArmorStandRenderer) renderer;
				armorRenderer.addLayer(new CosmosElytraArmourLayer<>(armorRenderer, new ArmorStandArmorModel(0.5F), new ArmorStandArmorModel(1.0F)));
				armorRenderer.addLayer(new CosmosElytraLayerStand<>(armorRenderer, new ResourceLocation(DimensionalPockets.MOD_ID, "textures/entity/dimensional_elytra.png")));
			}
		}
	}
	
	public static void registerOresForGeneration() {
		overworldOres.add(register("dimensional_ore", Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, CoreModBusManager.BLOCK_DIMENSIONAL_ORE.defaultBlockState(), 4))
			.range(48).squared().count(24)));
			//.weighted(24)));

		netherOres.add(register("dimensional_ore_nether", Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, CoreModBusManager.BLOCK_DIMENSIONAL_ORE_NETHER.defaultBlockState(), 4))
			.range(48).squared().count(30)));
			//.weighted(30)));

		endOres.add(register("dimensional_ore_end", Feature.ORE.configured(new OreFeatureConfig(new BlockMatchRuleTest(Blocks.END_STONE), CoreModBusManager.BLOCK_DIMENSIONAL_ORE_END.defaultBlockState(), 4))
			.range(128).squared().count(36)));
			//.weighted(36)));
	}
	
	private static boolean hasHadBook(PlayerEntity player) {
		CompoundNBT pocketTag = PocketUtil.getPlayerPersistTag(player);
		return pocketTag.getBoolean(GIVEN_INFO_BOOK);
	}

	private static void checkIfSpawnWithBook(PlayerEntity player) {
		CompoundNBT pocketTag = PocketUtil.getPlayerPersistTag(player);
		boolean shouldGiveManual = CoreConfigurationManager.getInstance().getShouldSpawnWithBook() && !pocketTag.getBoolean(GIVEN_INFO_BOOK);
		
		if (shouldGiveManual) {
			ItemStack infoBook = new ItemStack(CoreModBusManager.DIMENSIONAL_TOME);
			if (!player.inventory.add(infoBook)) {
				World playerWorld = player.level;
				ItemEntity entityItem = new ItemEntity(playerWorld, player.getX(), player.getY(), player.getZ(), infoBook);
				entityItem.setPickUpDelay(0);

				playerWorld.addFreshEntity(entityItem);
			}
			
			pocketTag.putBoolean(GIVEN_INFO_BOOK, true);
			
			player.getPersistentData().put(DimensionalPockets.MOD_ID, pocketTag);
		}
	}
	
	private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
		return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, DimensionalPockets.MOD_ID + ":" + name, configuredFeature);
	}
}