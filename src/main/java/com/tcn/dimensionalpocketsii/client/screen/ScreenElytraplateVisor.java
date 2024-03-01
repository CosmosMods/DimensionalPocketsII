package com.tcn.dimensionalpocketsii.client.screen;

import org.apache.commons.lang3.text.WordUtils;

import com.tcn.cosmoslibrary.client.ui.lib.CosmosUISystem;
import com.tcn.cosmoslibrary.common.lib.ComponentColour;
import com.tcn.cosmoslibrary.common.lib.ComponentHelper;
import com.tcn.cosmoslibrary.energy.interfaces.ICosmosEnergyItem;
import com.tcn.dimensionalpocketsii.DimReference;
import com.tcn.dimensionalpocketsii.core.item.armour.DimensionalElytraplate;
import com.tcn.dimensionalpocketsii.core.item.armour.ElytraSettings;
import com.tcn.dimensionalpocketsii.core.item.armour.module.BaseElytraModule;
import com.tcn.dimensionalpocketsii.core.management.ObjectManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@SuppressWarnings({ "deprecation", "unused" })
@OnlyIn(Dist.CLIENT)
public class ScreenElytraplateVisor implements IGuiOverlay {

	@Override
	public void render(ForgeGui gui, GuiGraphics graphicsIn, float partialTick, int screenWidth, int screenHeight) {
		Minecraft minecraft = gui.getMinecraft();
		LocalPlayer player = minecraft.player;
		Inventory playerInventory = player.getInventory();
		
		if (playerInventory.getArmor(2).getItem().equals(ObjectManager.dimensional_elytraplate)) {
			//gui.screenWidth = gui.getMinecraft().getWindow().getGuiScaledWidth();
			//gui.screenHeight = gui.getMinecraft().getWindow().getGuiScaledHeight();
	
			int powerOffset = 26;
			int powerWidth = 116;
			
			float scaleAll = Mth.clamp(((screenWidth / 1008.0F) + (screenHeight / 576.0F)) / 2, 0.0F, 1.0F);
	
			int scaledWidth = (int) (screenWidth / Mth.clamp(scaleAll, 0.0F, 1.0F));
			int scaledHeight = (int) (screenHeight / Mth.clamp(scaleAll, 0.0F, 1.0F));
	
			graphicsIn.pose().pushPose();
			graphicsIn.pose().scale(scaleAll, scaleAll, 1.0F);
			
			ItemStack chestStack = player.getInventory().getArmor(2);
			DimensionalElytraplate plate = (DimensionalElytraplate) chestStack.getItem();
			
			if (DimensionalElytraplate.hasModuleInstalled(chestStack, BaseElytraModule.VISOR) && DimensionalElytraplate.getElytraSetting(chestStack, ElytraSettings.VISOR)[1]) {
				CosmosUISystem.renderStaticElement(gui, graphicsIn, new int[] { 0, 0 }, 1, scaledHeight - 43, 0, 0, 242, 42, DimReference.GUI.RESOURCE.ELYTRAPLATE_VISOR);
				CosmosUISystem.renderEnergyDisplay(gui, graphicsIn, ComponentColour.RED, this.getSuitEnergy(gui.getMinecraft())[0] / 1000, this.getSuitEnergy(gui.getMinecraft())[1] / 1000, powerWidth, new int[] { 0, 0 }, 125, scaledHeight - powerOffset + 9, powerWidth, 7, true);
				CosmosUISystem.renderEnergyDisplay(gui, graphicsIn, ComponentColour.GREEN, plate.getEnergy(chestStack) / 1000, plate.getMaxEnergyStored(chestStack) / 1000, powerWidth, new int[] { 0, 0 }, 125, scaledHeight - powerOffset + 19, powerWidth, 4, true);
				
				if (DimensionalElytraplate.getInstalledModules(chestStack).size() == 1) {
					graphicsIn.drawString(gui.getFont(), ComponentHelper.style(ComponentColour.RED, "dimensionalpocketsii.gui.elytraplate.visor.empty"), 80, scaledHeight - 36, ComponentColour.WHITE.dec());
				}
				
				boolean elytra = DimensionalElytraplate.getElytraSetting(chestStack, ElytraSettings.ELYTRA_FLY)[1];
				boolean telepo = DimensionalElytraplate.getElytraSetting(chestStack, ElytraSettings.TELEPORT_TO_BLOCK)[1];
				boolean solarp = DimensionalElytraplate.getElytraSetting(chestStack, ElytraSettings.SOLAR)[1];
				boolean charge = DimensionalElytraplate.getElytraSetting(chestStack, ElytraSettings.CHARGER)[1];
				
				//this.blit(graphicsIn, 02, scaledHeight - 42, 20, 42, 20, 20);
				
				CosmosUISystem.renderStaticElementToggled(gui, graphicsIn, DimReference.GUI.RESOURCE.ELYTRAPLATE_VISOR, new int[] { 0, 0 }, 02, scaledHeight - 42, 0, 42, 20, 20, elytra);
				CosmosUISystem.renderStaticElementToggled(gui, graphicsIn, DimReference.GUI.RESOURCE.ELYTRAPLATE_VISOR, new int[] { 0, 0 }, 22, scaledHeight - 42, 0, 42, 20, 20, telepo);
				CosmosUISystem.renderStaticElementToggled(gui, graphicsIn, DimReference.GUI.RESOURCE.ELYTRAPLATE_VISOR, new int[] { 0, 0 }, 42, scaledHeight - 42, 0, 42, 20, 20, solarp);
				CosmosUISystem.renderStaticElementToggled(gui, graphicsIn, DimReference.GUI.RESOURCE.ELYTRAPLATE_VISOR, new int[] { 0, 0 }, 02, scaledHeight - 22, 0, 42, 20, 20, charge);
				
				CosmosUISystem.renderStaticElement(gui, graphicsIn, new int[] { 0, 0 }, 04, scaledHeight - 39, 0,  90, 16, 14, DimReference.GUI.RESOURCE.ELYTRAPLATE_VISOR);
				CosmosUISystem.renderStaticElement(gui, graphicsIn, new int[] { 0, 0 }, 25, scaledHeight - 39, 16, 90, 14, 14, DimReference.GUI.RESOURCE.ELYTRAPLATE_VISOR);
				CosmosUISystem.renderStaticElement(gui, graphicsIn, new int[] { 0, 0 }, 45, scaledHeight - 39, 30, 90, 14, 14, DimReference.GUI.RESOURCE.ELYTRAPLATE_VISOR);
				CosmosUISystem.renderStaticElement(gui, graphicsIn, new int[] { 0, 0 }, 05, scaledHeight - 19, 44, 90, 14, 14, DimReference.GUI.RESOURCE.ELYTRAPLATE_VISOR);
				
				float scl1 = 0.525F;
				
				graphicsIn.pose().pushPose();
				graphicsIn.pose().scale(scl1, scl1, 0.0F);
				CosmosUISystem.renderStaticElement(gui, graphicsIn, DimReference.GUI.RESOURCE.ELYTRAPLATE_VISOR, new int[] { 0, 0 }, (int)(66 / scl1), (int) (scaledHeight / scl1) - (int) (39 / scl1), 56, 62, 28, 28);
				graphicsIn.pose().popPose();
				
				if (DimensionalElytraplate.hasModuleInstalled(chestStack, BaseElytraModule.SCREEN)) {
					graphicsIn.pose().pushPose();
					graphicsIn.pose().scale(scl1, scl1, 0.0F);
					CosmosUISystem.renderStaticElement(gui, graphicsIn, DimReference.GUI.RESOURCE.ELYTRAPLATE_VISOR, new int[] { 0, 0 }, (int)(86 / scl1), (int) (scaledHeight / scl1) - (int) (39 / scl1), 28, 62, 28, 28);
					graphicsIn.pose().popPose();
				}
	
				if (DimensionalElytraplate.hasModuleInstalled(chestStack, BaseElytraModule.SHIFTER)) {
					graphicsIn.pose().pushPose();
					graphicsIn.pose().scale(scl1, scl1, 0.0F);
					CosmosUISystem.renderStaticElement(gui, graphicsIn, DimReference.GUI.RESOURCE.ELYTRAPLATE_VISOR, new int[] { 0, 0 }, (int)(106 / scl1), (int) (scaledHeight / scl1) - (int) (39 / scl1), 0, 62, 28, 28);
					graphicsIn.pose().popPose();
				}
	
				if (DimensionalElytraplate.hasModuleInstalled(chestStack, BaseElytraModule.SOLAR)) {
					graphicsIn.pose().pushPose();
					graphicsIn.pose().scale(scl1, scl1, 0.0F);
					CosmosUISystem.renderStaticElement(gui, graphicsIn, DimReference.GUI.RESOURCE.ELYTRAPLATE_VISOR, new int[] { 0, 0 }, (int)(66 / scl1), (int) (scaledHeight / scl1) - (int) (19 / scl1), 84, 62, 28, 28);
					graphicsIn.pose().popPose();
				}
				
				if (DimensionalElytraplate.hasModuleInstalled(chestStack, BaseElytraModule.BATTERY)) {
					graphicsIn.pose().pushPose();
					graphicsIn.pose().scale(scl1, scl1, 0.0F);
					CosmosUISystem.renderStaticElement(gui, graphicsIn, DimReference.GUI.RESOURCE.ELYTRAPLATE_VISOR, new int[] { 0, 0 }, (int)(86 / scl1), (int) (scaledHeight / scl1) - (int) (19 / scl1), 112, 62, 28, 28);
					graphicsIn.pose().popPose();
				}
	
				if (DimensionalElytraplate.hasModuleInstalled(chestStack, BaseElytraModule.ENDER_CHEST)) {
					graphicsIn.pose().pushPose();
					graphicsIn.pose().scale(scl1, scl1, 0.0F);
					CosmosUISystem.renderStaticElement(gui, graphicsIn, DimReference.GUI.RESOURCE.ELYTRAPLATE_VISOR, new int[] { 0, 0 }, (int)(106 / scl1), (int) (scaledHeight / scl1) - (int) (19 / scl1), 140, 62, 28, 28);
					graphicsIn.pose().popPose();
				}
	
				if (DimensionalElytraplate.hasModuleInstalled(chestStack, BaseElytraModule.SHIFTER)) {
					if (chestStack.hasTag()) {
						CompoundTag stack_nbt = chestStack.getTag();
	
						if (stack_nbt.contains("nbt_data")) {
							CompoundTag nbt_data = stack_nbt.getCompound("nbt_data");
	
							if (nbt_data.contains("player_pos")) {
								CompoundTag player_pos = nbt_data.getCompound("player_pos");
	
								if (nbt_data.contains("dimension")) {
									CompoundTag dim = nbt_data.getCompound("dimension");
									String path = dim.getString("path");
	
									int[] pos = new int[] { player_pos.getInt("x"), player_pos.getInt("y"), player_pos.getInt("z") };
	
									float sclA = 1.0F;
									Component comp = ComponentHelper.style(ComponentColour.GREEN, "dimensionalpocketsii.gui.elytraplate.visor.dimension").append(ComponentHelper.style(ComponentColour.LIME, WordUtils.capitalizeFully(path.replace("_", " "))));
	
									if (gui.getFont().width(comp) > 114) {
										sclA = 114.0F / gui.getFont().width(comp);
									}
	
									graphicsIn.pose().pushPose();
									graphicsIn.pose().scale(sclA, sclA, sclA);
									graphicsIn.drawString(gui.getFont(), comp, Math.round(126 / sclA), Math.round((scaledHeight / sclA) - (40 / sclA)), ComponentColour.WHITE.dec());
									graphicsIn.pose().popPose();
	
									String position = "X: " + pos[0] + " | Y: " + pos[1] + " | Z: " + pos[2] + "";
	
									float sclB = 1.0F;
	
									if (gui.getFont().width(position) > 114) {
										sclB = 114.0F / gui.getFont().width(position);
									}
									
									graphicsIn.pose().pushPose();
									graphicsIn.pose().scale(sclB, sclB, sclB);
									graphicsIn.drawString(gui.getFont(), ComponentHelper.style(ComponentColour.CYAN, position), Math.round(126 / sclB), Math.round((scaledHeight / sclB) - (28 / sclB)), ComponentColour.WHITE.dec());
									graphicsIn.pose().popPose();
								}
							}
						}
					}
				}
			}
			
			graphicsIn.pose().popPose();
		}
	}

	public int[] getSuitEnergy(Minecraft minecraft) {
		Inventory playerInventory = minecraft.player.getInventory();

		int energy0 = 0;
		int maxEnergy0 = 0;

		int energy1 = 0;
		int maxEnergy1 = 0;

		int energy2 = 0;
		int maxEnergy2 = 0;

		int energy3 = 0;
		int maxEnergy3 = 0;

		if (playerInventory.getArmor(0).getItem() instanceof ICosmosEnergyItem) {
			ICosmosEnergyItem item = (ICosmosEnergyItem) playerInventory.getArmor(0).getItem();

			energy0 = item.getEnergy(playerInventory.getArmor(0));
			maxEnergy0 = item.getMaxEnergyStored(playerInventory.getArmor(0));
		}

		if (playerInventory.getArmor(1).getItem() instanceof ICosmosEnergyItem) {
			ICosmosEnergyItem item = (ICosmosEnergyItem) playerInventory.getArmor(1).getItem();

			energy1 = item.getEnergy(playerInventory.getArmor(1));
			maxEnergy1 = item.getMaxEnergyStored(playerInventory.getArmor(1));
		}

		if (playerInventory.getArmor(2).getItem() instanceof ICosmosEnergyItem) {
			ICosmosEnergyItem item = (ICosmosEnergyItem) playerInventory.getArmor(2).getItem();

			energy2 = item.getEnergy(playerInventory.getArmor(2));
			maxEnergy2 = item.getMaxEnergyStored(playerInventory.getArmor(2));
		}

		if (playerInventory.getArmor(3).getItem() instanceof ICosmosEnergyItem) {
			ICosmosEnergyItem item = (ICosmosEnergyItem) playerInventory.getArmor(3).getItem();

			energy3 = item.getEnergy(playerInventory.getArmor(3));
			maxEnergy3 = item.getMaxEnergyStored(playerInventory.getArmor(3));
		}

		return new int[] { energy0 + energy1 + energy2 + energy3, maxEnergy0 + maxEnergy1 + maxEnergy2 + maxEnergy3 };
	}
}