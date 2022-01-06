package com.tcn.dimensionalpocketsii.core.management;

import java.util.ArrayList;

import com.tcn.cosmoslibrary.common.chat.CosmosChatUtil;
import com.tcn.cosmoslibrary.common.lib.ComponentHelper;
import com.tcn.cosmoslibrary.common.lib.CosmosChunkPos;
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
import com.tcn.dimensionalpocketsii.core.network.elytraplate.PacketElytraplateOpenUI;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockFocus;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityFocus;
import com.tcn.dimensionalpocketsii.pocket.core.management.FocusJumpHandler;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketNetworkManager;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.util.PocketUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@SuppressWarnings({ "unused" })
@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
public class ForgeEventManager {

	private static final ArrayList<ConfiguredFeature<?, ?>> overworldOres = new ArrayList<ConfiguredFeature<?, ?>>();
	private static final ArrayList<ConfiguredFeature<?, ?>> netherOres = new ArrayList<ConfiguredFeature<?, ?>>();
	private static final ArrayList<ConfiguredFeature<?, ?>> endOres = new ArrayList<ConfiguredFeature<?, ?>>();

	@OnlyIn(Dist.CLIENT)
	private static ScreenElytraplateVisor screenSettings = new ScreenElytraplateVisor();
	private static final String GIVEN_INFO_BOOK = "givenInfoBook";

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onRenderGameOverlayEvent(RenderGameOverlayEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		ElementType type = event.getType();
		Player player = mc.player;
		Inventory playerInventory = player.getInventory();
		
		if (type.equals(ElementType.LAYER)) {
			if (playerInventory.getArmor(2).getItem().equals(ModBusManager.DIMENSIONAL_ELYTRAPLATE)) {
				
				if (screenSettings != null) {
					screenSettings.renderOverlay(event.getMatrixStack());
				}
			}
		}
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onKeyPressEvent(KeyInputEvent event) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer playerIn = mc.player;
		
		if (playerIn != null) {
			Level world = playerIn.level;
			if (ModBusManager.SUIT_SCREEN.isDown()) {
				if (playerIn.getInventory().getArmor(2).getItem() != null) {
					Item armour = playerIn.getInventory().getArmor(2).getItem();
					
					if (armour instanceof DimensionalElytraplate) {
						NetworkManager.sendToServer(new PacketElytraplateOpenUI(playerIn.getUUID(), 2, true));
					}
				}
			} 
		
			else if (ModBusManager.SUIT_SHIFT.isDown()) {
				if (playerIn.getInventory().getArmor(2).getItem() != null) {
					ItemStack stack = playerIn.getInventory().getArmor(2);
					Item armour = playerIn.getInventory().getArmor(2).getItem();
					BlockPos player_pos_actual = playerIn.blockPosition();
					
					if (armour instanceof DimensionalElytraplate) {
						DimensionalElytraplate elytraplate = (DimensionalElytraplate) armour;
						
						if (DimensionalElytraplate.hasModuleInstalled(stack, BaseElytraModule.SHIFTER)) {
							if (stack.hasTag()) {
								CompoundTag stack_nbt = stack.getTag();
								
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
											
											boolean tele_to_block = DimensionalElytraplate.getElytraSetting(stack, ElytraSettings.TELEPORT_TO_BLOCK)[1];
											
											CompoundTag dim = nbt_data.getCompound("dimension");
											String namespace = dim.getString("namespace");
											String path = dim.getString("path");
											ResourceLocation loc = new ResourceLocation(namespace, path);
											
											ResourceKey<Level> source_dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, loc);
											
											BlockPos teleport_pos = new BlockPos(player_x, player_y, player_z);
											
											int x = chunk_pos.getInt("x");
											int z = chunk_pos.getInt("z");
											
											CosmosChunkPos chunk = new CosmosChunkPos(x, z);
											
											if (elytraplate.hasEnergy(stack)) {
												if (PocketUtil.isDimensionEqual(world, DimensionManager.POCKET_WORLD)) {
													if (tele_to_block) {
														NetworkManager.sendToServer(new PacketElytraUseEnergy(playerIn.getUUID(), 2, elytraplate.getMaxUse(stack)));
														NetworkManager.sendToServer(new PacketElytraShift(playerIn.getUUID(), world.dimension(), chunk));
													} else {
														NetworkManager.sendToServer(new PacketElytraUseEnergy(playerIn.getUUID(), 2, elytraplate.getMaxUse(stack)));
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
													NetworkManager.sendToServer(new PacketElytraUseEnergy(playerIn.getUUID(), 2, elytraplate.getMaxUse(stack)));
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
			} else if (ModBusManager.SUIT_SETTINGS.isDown()) {
				if (playerIn.getInventory().getArmor(2).getItem() != null) {
					Item armour = playerIn.getInventory().getArmor(2).getItem();
					
					if (armour instanceof DimensionalElytraplate) {
						NetworkManager.sendToServer(new PacketElytraplateOpenUI(playerIn.getUUID(), 2, false));
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onBiomeLoadingEvent(final BiomeLoadingEvent event) {
		BiomeGenerationSettingsBuilder generation = event.getGeneration();

		if (event.getCategory().equals(Biome.BiomeCategory.NETHER)) {
			for (ConfiguredFeature<?, ?> ore : netherOres) {
				if (ore != null)
					generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ore);
			}
		}
		if (event.getCategory().equals(Biome.BiomeCategory.THEEND)) {
			for (ConfiguredFeature<?, ?> ore : endOres) {
				if (ore != null)
					generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ore);
			}
		}
		for (ConfiguredFeature<?, ?> ore : overworldOres) {
			if (ore != null)
				generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ore);
		}
	}
	
	@SubscribeEvent
	public static void onServerSaveEvent(final WorldEvent.Save event) {
		LevelAccessor world = event.getWorld();
		DimensionType type = world.dimensionType();
		
		//Do this ridiculousness to detect Overworld dimension
		if (!type.ultraWarm() && type.natural() && !type.piglinSafe() && !type.respawnAnchorWorks() && 
				type.bedWorks() && type.hasRaids() && type.hasSkyLight() && !type.hasCeiling() &&
				type.coordinateScale() == 1 && type.logicalHeight() == 256) {
			PocketRegistryManager.saveData();
		}
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedOutEvent(final PlayerEvent.PlayerLoggedOutEvent event) {
		//PocketRegistryManager.saveData();
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedInEvent(final PlayerEvent.PlayerLoggedInEvent event) {
		Entity entity = event.getEntity();
		Level world = entity.level;
		
		if (entity instanceof ServerPlayer) {
			ServerPlayer player = (ServerPlayer) entity;

			if (!hasHadBook(player)) {
				CosmosChatUtil.sendServerPlayerMessage(player, DimReference.MESSAGES.WELCOME);
			}
			
			if (!world.isClientSide) {
				checkIfSpawnWithBook(player);
			}
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public static void clearMap() {
		PocketRegistryManager.clearPocketMap();
	}
	
	public static void registerOresForGeneration() {
		overworldOres.add(register("dimensional_ore", Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, ModBusManager.BLOCK_DIMENSIONAL_ORE.defaultBlockState(), 4))
			.squared().count(24).rangeUniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(48))));

		netherOres.add(register("dimensional_ore_nether", Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NETHERRACK, ModBusManager.BLOCK_DIMENSIONAL_ORE_NETHER.defaultBlockState(), 4))
			.squared().count(30).rangeUniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(48))));

		endOres.add(register("dimensional_ore_end", Feature.ORE.configured(new OreConfiguration(new BlockMatchTest(Blocks.END_STONE), ModBusManager.BLOCK_DIMENSIONAL_ORE_END.defaultBlockState(), 4))
			.squared().count(36).rangeUniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(128))));
	}
	
	private static boolean hasHadBook(Player player) {
		CompoundTag pocketTag = PocketUtil.getPlayerPersistTag(player);
		return pocketTag.getBoolean(GIVEN_INFO_BOOK);
	}

	private static void checkIfSpawnWithBook(Player player) {
		CompoundTag pocketTag = PocketUtil.getPlayerPersistTag(player);
		boolean shouldGiveManual = ConfigurationManager.getInstance().getSpawnWithTome() && !pocketTag.getBoolean(GIVEN_INFO_BOOK);
		
		if (shouldGiveManual) {
			ItemStack infoBook = new ItemStack(ModBusManager.DIMENSIONAL_TOME);
			if (!player.getInventory().add(infoBook)) {
				Level playerWorld = player.level;
				ItemEntity entityItem = new ItemEntity(playerWorld, player.getX(), player.getY(), player.getZ(), infoBook);
				entityItem.setPickUpDelay(0);

				playerWorld.addFreshEntity(entityItem);
				pocketTag.putBoolean(GIVEN_INFO_BOOK, true);
			}
			
			pocketTag.putBoolean(GIVEN_INFO_BOOK, true);
			
			player.getPersistentData().put(DimensionalPockets.MOD_ID, pocketTag);
		}
	}
	
	private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
		return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, DimensionalPockets.MOD_ID + ":" + name, configuredFeature);
	}
}