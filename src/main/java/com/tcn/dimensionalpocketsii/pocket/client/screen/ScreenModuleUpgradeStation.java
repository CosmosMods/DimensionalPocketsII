package com.tcn.dimensionalpocketsii.pocket.client.screen;

import java.util.Arrays;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tcn.cosmoslibrary.client.ui.lib.CosmosUISystem;
import com.tcn.cosmoslibrary.client.ui.screen.CosmosScreenUIMode;
import com.tcn.cosmoslibrary.client.ui.screen.widget.CosmosButtonWithType;
import com.tcn.cosmoslibrary.client.ui.screen.widget.CosmosButtonWithType.TYPE;
import com.tcn.cosmoslibrary.common.lib.ComponentColour;
import com.tcn.cosmoslibrary.common.lib.ComponentHelper;
import com.tcn.dimensionalpocketsii.DimReference.GUI;
import com.tcn.dimensionalpocketsii.DimReference.GUI.RESOURCE;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleUpgradeStation;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleUpgradeStation;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketNetworkManager;
import com.tcn.dimensionalpocketsii.pocket.network.packet.misc.PacketUpgradeStationCraft;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenModuleUpgradeStation extends CosmosScreenUIMode<ContainerModuleUpgradeStation> {

	private CosmosButtonWithType craftButton;  private int[] ACI = new int[] { 118, 62, 18 };
	
	public ScreenModuleUpgradeStation(ContainerModuleUpgradeStation containerIn, Inventory playerInventoryIn, Component titleIn) {
		super(containerIn, playerInventoryIn, titleIn);
		
		this.setImageDims(184, 189);
		
		this.setLight(RESOURCE.UPGRADE_STATION[0]);
		this.setDark(RESOURCE.UPGRADE_STATION[1]);

		this.setUIModeButtonIndex(167, 5);
		this.setUIHelpButtonIndex(167, 19);
		this.setUIHelpElementDeadzone(23, 13, 160, 86);
		
		this.setTitleLabelDims(34, 4);
		this.setInventoryLabelDims(8, 89);
	}

	@Override
	protected void init() {
		super.init();
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		super.render(poseStack, mouseX, mouseY, partialTicks);
	}

	@Override
	protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
		super.renderBg(poseStack, partialTicks, mouseX, mouseY);
		
		BlockEntity entity = this.getBlockEntity();
		
		if (entity instanceof BlockEntityModuleUpgradeStation) {
			BlockEntityModuleUpgradeStation blockEntity = (BlockEntityModuleUpgradeStation) entity;
			
			if (blockEntity.getPocket() != null) {
				Pocket pocket = blockEntity.getPocket();
				
				int decimal = pocket.getDisplayColour();
				ComponentColour colour = ComponentColour.col(decimal);
				float[] rgb = null;
				
				if (colour.equals(ComponentColour.POCKET_PURPLE)) {
					rgb = ComponentColour.rgbFloatArray(ComponentColour.POCKET_PURPLE_LIGHT.dec());
				} else {
					rgb = ComponentColour.rgbFloatArray(decimal);
				}
				
				CosmosUISystem.renderStaticElementWithUIMode(this, poseStack, this.getScreenCoords(), 0, 0, 0, 0, this.imageWidth, this.imageHeight, new float[] { rgb[0], rgb[1], rgb[2], 1.0F }, blockEntity, GUI.RESOURCE.UPGRADE_STATION_BASE);
			}
			
			CosmosUISystem.renderStaticElementWithUIMode(this, poseStack, this.getScreenCoords(), 0, 0, 0, 0, this.imageWidth, this.imageHeight, blockEntity, GUI.RESOURCE.UPGRADE_STATION_OVERLAY);
		}
	}

	@Override
	public void renderStandardHoverEffect(PoseStack poseStack, Style style, int mouseX, int mouseY) {
		super.renderStandardHoverEffect(poseStack, style, mouseX, mouseY);
		
		if (this.craftButton.isMouseOver(mouseX, mouseY)) {
			BaseComponent[] comp = new BaseComponent[] { ComponentHelper.locComp(ComponentColour.GREEN, false, "dimensionalpocketsii.gui.upgrade_station.craft_button") };
			
			this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
		} 
	}
	
	@Override
	protected void addButtons() {
		super.addButtons();
		
		BlockEntity entity = this.getBlockEntity();
		
		if (entity instanceof BlockEntityModuleUpgradeStation) {
			BlockEntityModuleUpgradeStation blockEntity = (BlockEntityModuleUpgradeStation) entity;
			
			this.craftButton = this.addRenderableWidget(new CosmosButtonWithType(TYPE.GENERAL, this.getScreenCoords()[0] + ACI[0], this.getScreenCoords()[1] + ACI[1], ACI[2], blockEntity.canCraft(), true, 1, ComponentHelper.locComp(""), (button) -> { this.pushButton(this.craftButton); }));
		}
	}
		
	
	@Override
	public void pushButton(Button button) {
		super.pushButton(button);
		
		if (button.equals(this.craftButton)) {
			PocketNetworkManager.sendToServer(new PacketUpgradeStationCraft(this.menu.getBlockPos()));
		}
	}

	protected void addUIHelpElements() {
		super.addUIHelpElements();
		
		this.addRenderableUIHelpElement(this.getScreenCoords(), 117, 61, 20, 20, ComponentHelper.locComp(ComponentColour.GREEN, false, "dimensionalpocketsii.gui.help.upgrade_station.craft_button"), 
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.upgrade_station.craft_button_one"), 
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.upgrade_station.craft_button_two"),
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.upgrade_station.craft_button_three"),
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.upgrade_station.craft_button_four"),
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.upgrade_station.craft_button_five")
		);

		this.addRenderableUIHelpElement(this.getScreenCoords(), 117, 40, 20, 20, ComponentHelper.locComp(ComponentColour.CYAN, false, "dimensionalpocketsii.gui.help.upgrade_station.result_slot"), 
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.upgrade_station.result_slot_one"), 
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.upgrade_station.result_slot_two"),
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.upgrade_station.result_slot_three")
		);
		this.addRenderableUIHelpElement(this.getScreenCoords(), 117, 19, 20, 20, ComponentHelper.locComp(ComponentColour.GREEN, false, "dimensionalpocketsii.gui.help.upgrade_station.preview_slot"), 
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.upgrade_station.preview_slot_one"), 
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.upgrade_station.preview_slot_two")
		);

		this.addRenderableUIHelpElement(this.getScreenCoords(), 47, 19, 62, 20, ComponentHelper.locComp(ComponentColour.LIGHT_BLUE, false, "dimensionalpocketsii.gui.help.upgrade_station.input_slots"), 
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.upgrade_station.input_slots_one"), 
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.upgrade_station.input_slots_two")
		);
		this.addRenderableUIHelpElement(this.getScreenCoords(), 68, 40, 20, 20, ComponentHelper.locComp(ComponentColour.PURPLE, false, "dimensionalpocketsii.gui.help.upgrade_station.focused_slot"), 
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.upgrade_station.focused_slot_one"), 
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.upgrade_station.focused_slot_two")
		);
		this.addRenderableUIHelpElement(this.getScreenCoords(), 47, 61, 62, 20, ComponentHelper.locComp(ComponentColour.LIGHT_BLUE, false, "dimensionalpocketsii.gui.help.upgrade_station.input_slots"), 
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.upgrade_station.input_slots_one"), 
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.upgrade_station.input_slots_two")
		);
	}
	
	@Override
	protected boolean isHovering(int positionX, int positionY, int width, int height, double mouseX, double mouseY) {
		return super.isHovering(positionX, positionY, width, height, mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void slotClicked(Slot slotIn, int mouseX, int mouseY, ClickType clickTypeIn) {
		super.slotClicked(slotIn, mouseX, mouseY, clickTypeIn);
	}
}