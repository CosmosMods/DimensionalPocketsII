package com.tcn.dimensionalpocketsii.client.screen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import com.ibm.icu.text.DecimalFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tcn.cosmoslibrary.CosmosReference.RESOURCE.BASE;
import com.tcn.cosmoslibrary.client.ui.lib.CosmosUISystem;
import com.tcn.cosmoslibrary.client.ui.lib.CosmosUISystem.FONT;
import com.tcn.cosmoslibrary.client.ui.lib.CosmosUISystem.IS_HOVERING;
import com.tcn.cosmoslibrary.client.ui.screen.widget.CosmosButtonUIMode;
import com.tcn.cosmoslibrary.client.ui.screen.widget.CosmosButtonWithType;
import com.tcn.cosmoslibrary.client.ui.screen.widget.CosmosButtonWithType.TYPE;
import com.tcn.cosmoslibrary.client.ui.screen.widget.CosmosListWidget;
import com.tcn.cosmoslibrary.common.enums.EnumUIMode;
import com.tcn.cosmoslibrary.common.lib.ComponentColour;
import com.tcn.cosmoslibrary.common.lib.ComponentHelper;
import com.tcn.dimensionalpocketsii.DimReference.GUI;
import com.tcn.dimensionalpocketsii.DimReference.GUI.RESOURCE;
import com.tcn.dimensionalpocketsii.client.container.ContainerElytraplate;
import com.tcn.dimensionalpocketsii.client.screen.button.DimensionalButton;
import com.tcn.dimensionalpocketsii.core.item.armour.DimensionalElytraplate;
import com.tcn.dimensionalpocketsii.core.item.armour.ElytraSettings;
import com.tcn.dimensionalpocketsii.core.item.armour.module.BaseElytraModule;
import com.tcn.dimensionalpocketsii.core.management.NetworkManager;
import com.tcn.dimensionalpocketsii.core.network.elytraplate.PacketElytraSettingsChange;
import com.tcn.dimensionalpocketsii.core.network.elytraplate.PacketElytraplateUpdateUIMode;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketNetworkManager;
import com.tcn.dimensionalpocketsii.pocket.network.packet.elytraplate.EPacketAllowedPlayer;
import com.tcn.dimensionalpocketsii.pocket.network.packet.elytraplate.EPacketEmptyTank;
import com.tcn.dimensionalpocketsii.pocket.network.packet.elytraplate.EPacketHostileSpawnState;
import com.tcn.dimensionalpocketsii.pocket.network.packet.elytraplate.EPacketLock;
import com.tcn.dimensionalpocketsii.pocket.network.packet.elytraplate.EPacketLockToAllowedPlayers;
import com.tcn.dimensionalpocketsii.pocket.network.packet.elytraplate.EPacketTrapPlayers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.capability.templates.FluidTank;

@OnlyIn(Dist.CLIENT)
public class ScreenElytraplate extends AbstractContainerScreen<ContainerElytraplate> {

	protected ResourceLocation WIDGET_TEXTURE;
	protected ArrayList<CosmosListWidget> widgetList = new ArrayList<CosmosListWidget>();
	protected ArrayList<String> fromList = new ArrayList<String>();

	private boolean scrollEnabled = false;
	private int currentScroll;
	
	private int[] listIndex;
	private int[] scrollElementIndex;
	
	private int[] energyBarData = new int[] { 15, 177 };
	private int[] fluidBarData = new int[] { 36, 52, 203, 157, 18, 58 };
	
	private int[] screenCoords;
	
	private UUID playerUUID;
	private ItemStack stack;
	private boolean screen;
	private Pocket pocket;

	protected CosmosButtonUIMode uiModeButton;
	
	private DimensionalButton elytraFlyButton;
	private DimensionalButton teleToBlockButton;
	private DimensionalButton visorButton;
	private DimensionalButton solarButton;
	private DimensionalButton chargerButton;

	private CosmosButtonWithType buttonTextClear; private int[] TCBI = new int[] { 191, 18, 18 };
	private CosmosButtonWithType buttonTextPlus;  private int[] TABI = new int[] { 212, 18, 18 };
	private CosmosButtonWithType buttonTextMinus; private int[] TMBI = new int[] { 233, 18, 18 };
	
	private CosmosButtonWithType buttonTankClear; private int[] TBCI = new int[] { 56, 222, 20 };

	private EditBox textField; private int[] textFieldI = new int[] { 98, 23, 93, 16 };
	
	private CosmosButtonWithType buttonLock; private int[] LBI = new int[] { 93, 130 };
	private CosmosButtonWithType buttonAllowedPlayers; private int[] APBI = new int[] { 116, 130 };
	private DimensionalButton buttonTrapPlayers; private int[] TPBI = new int[] { 139, 130 };
	private CosmosButtonWithType buttonHostileSpawn; private int[] HSBI = new int[] { 162, 130 };

	private CosmosButtonWithType buttonPlaceHolder0; private int[] PB0I = new int[] { 185, 130 };
	private CosmosButtonWithType buttonPlaceHolder1; private int[] PB1I = new int[] { 208, 130 };
	private CosmosButtonWithType buttonPlaceHolder2; private int[] PB2I = new int[] { 231, 130 };
	
	public ScreenElytraplate(ContainerElytraplate containerIn, Inventory inventoryIn, Component componentIn) {
		super(containerIn, inventoryIn, componentIn);
		
		this.screen = containerIn.getScreen();
		this.pocket = containerIn.getPocket();
		this.playerUUID = this.getMenu().getPlayer().getUUID();
		this.stack = this.getMenu().getStack();
		
		if (!this.screen) {
			this.setImageDims(192, 144);
			this.setTitleLabelDims(36, 5);
			this.setInventoryLabelDims(5, 55);
		} else {
			this.setImageDims(360, 256);
			this.setTitleLabelDims(88, 4);
			this.setInventoryLabelDims(88, 157);
			
			this.setListDims(94, 52, 138, 74, 14, 1);
			this.setScrollElementDims(237, 52);
		}
	}
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	protected void init() {
		this.setScreenCoords(CosmosUISystem.getScreenCoords(this, this.imageWidth, this.imageHeight));
		
		if (this.screen) {
			this.initTextField();
		}
		
		this.addButtons();
		super.init();
	}

	@Override
	public void containerTick() {
		super.containerTick();
		if (this.textField != null) {
			this.textField.tick();
		}
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);

		this.addButtons();
		
		if (this.screen) {
			if (this.pocket != null) {
				ComponentColour colour = ComponentColour.col(pocket.getDisplayColour());
				ComponentColour textColour = this.getUIMode().equals(EnumUIMode.LIGHT) ? ComponentColour.BLACK : ComponentColour.SCREEN_LIGHT;
				
				FONT.drawString(poseStack, font, this.getScreenCoords(), 93, 40, true, ComponentHelper.locComp(ComponentColour.getCompColourForScreen(colour), false, "dimensionalpocketsii.gui.header.allowed_players"));
				FONT.drawString(poseStack, font, this.getScreenCoords(), 8, 4, true, ComponentHelper.locComp(textColour, false, "dimensionalpocketsii.gui.header.config"));
				FONT.drawString(poseStack,font, this.getScreenCoords(), 262, 4, true, ComponentHelper.locComp(textColour, false, "dimensionalpocketsii.gui.header.pocket_inv"));
			}
			
			this.textField.render(poseStack, mouseX, mouseY, partialTicks);
		}

		this.renderComponents(poseStack, mouseX, mouseY, partialTicks);
		this.renderComponentHoverEffect(poseStack, Style.EMPTY, mouseX, mouseY);
		this.renderTooltip(poseStack, mouseX, mouseY);
	}
	
	public void renderComponents(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderScrollElement(poseStack);
		this.updateWidgetList();
		this.renderWidgetList(poseStack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
		DimensionalElytraplate item = (DimensionalElytraplate) this.stack.getItem();
		
		if (!this.screen) {
			CosmosUISystem.renderStaticElementWithUIMode(this, poseStack, this.getScreenCoords(), 0, 0, 0, 0, this.imageWidth, this.imageHeight, this.getUIMode(), RESOURCE.ELYTRAPLATE_SETTINGS);
			CosmosUISystem.renderStaticElementWithUIMode(this, poseStack, getScreenCoords(), 0, 0, 0, 0, this.imageWidth, this.imageHeight, this.getUIMode(), RESOURCE.ELYTRAPLATE_SLOTS);
			
			CosmosUISystem.renderEnergyDisplay(this, poseStack, ComponentColour.RED, item.getEnergy(this.stack) / 1000, item.getMaxEnergyStored(this.stack) / 1000, 116, this.getScreenCoords(), 38, 43, 116, 7, true);
		} else {
			CosmosUISystem.renderStaticElementWithUIMode(this, poseStack, this.getScreenCoords(), 0, 0, 0, 0, Mth.clamp(this.imageWidth, 0, 256), this.imageHeight, this.getUIMode(), RESOURCE.CONNECTOR);
			CosmosUISystem.renderStaticElementWithUIMode(this, poseStack, this.getScreenCoords(), 256, 0, 0, 0, 104, this.imageHeight, this.getUIMode(), RESOURCE.CONNECTOR_SIDE);
			
			if (this.menu.getPocket() != null) {
				ComponentColour colour = ComponentColour.col(this.pocket.getDisplayColour());
				float[] rgb = colour.equals(ComponentColour.POCKET_PURPLE) ? ComponentColour.rgbFloatArray(ComponentColour.POCKET_PURPLE_LIGHT) : ComponentColour.rgbFloatArray(colour);
				
				CosmosUISystem.renderStaticElementWithUIMode(this, poseStack, this.getScreenCoords(), 0, 0, 0, 0, 256, 256, new float[] { rgb[0], rgb[1], rgb[2], 1.0F }, this.getUIMode(), GUI.RESOURCE.CONNECTOR_BASE_NORMAL);
				CosmosUISystem.renderStaticElementWithUIMode(this, poseStack, this.getScreenCoords(), 256, 0, 0, 0, 104, 256, new float[] { rgb[0], rgb[1], rgb[2], 1.0F }, this.getUIMode(), GUI.RESOURCE.CONNECTOR_BASE_SIDE);

				CosmosUISystem.renderStaticElementWithUIMode(this, poseStack, this.getScreenCoords(), 0, 0, 0, 0, 256, 256, this.getUIMode(), GUI.RESOURCE.CONNECTOR_OVERLAY_NORMAL);
				CosmosUISystem.renderStaticElementWithUIMode(this, poseStack, this.getScreenCoords(), 256, 0, 0, 0, 104, 256, this.getUIMode(), GUI.RESOURCE.CONNECTOR_OVERLAY_SIDE);

				CosmosUISystem.renderFluidTank(this, poseStack, this.getScreenCoords(), this.fluidBarData[0], this.fluidBarData[2], this.pocket.getFluidTank(), this.pocket.getFluidLevelScaled(64));
				CosmosUISystem.renderEnergyDisplay(this, poseStack, ComponentColour.RED, this.pocket, this.getScreenCoords(), this.energyBarData[0], this.energyBarData[1], 16, 64, false);
			}
			this.textField.render(poseStack, mouseX + 30, mouseY, partialTicks);
		}
		
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
		this.font.draw(poseStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, this.getUIMode().equals(EnumUIMode.DARK) ? CosmosUISystem.DEFAULT_COLOUR_FONT_LIST : ComponentColour.BLACK.dec());
		
		this.font.draw(poseStack, this.playerInventoryTitle, (float) this.inventoryLabelX, (float) this.inventoryLabelY, this.getUIMode().equals(EnumUIMode.DARK) ? CosmosUISystem.DEFAULT_COLOUR_FONT_LIST : ComponentColour.BLACK.dec());
	}
	
	@Override
	public void renderComponentHoverEffect(PoseStack poseStack, Style style, int mouseX, int mouseY) {
		DimensionalElytraplate item = (DimensionalElytraplate) this.stack.getItem();
		
		if (this.uiModeButton.isMouseOver(mouseX, mouseY)) {
			Component[] comp = new Component[] { 
				ComponentHelper.locComp(ComponentColour.WHITE, false, "cosmoslibrary.gui.ui_mode.info"),
				ComponentHelper.locComp(ComponentColour.GRAY, false, "cosmoslibrary.gui.ui_mode.value").append(this.getUIMode().getColouredComp())
			};
			
			this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
		}
		
		if (!this.screen) {
			if (this.elytraFlyButton.isMouseOver(mouseX, mouseY)) {
				ElytraSettings setting = ElytraSettings.ELYTRA_FLY;
				boolean value = DimensionalElytraplate.getElytraSetting(stack, setting)[1];
				Component compV = setting.getValueComp(value);
				
				Component[] comp = new Component[] { 
					ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.elytraplate.settings.fly_info"), 
					ComponentHelper.locComp(ComponentColour.GRAY, false, "dimensionalpocketsii.gui.elytraplate.settings.fly_value").append(compV)
				};
					
				this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
			} else if (this.teleToBlockButton.isMouseOver(mouseX, mouseY)) {
				ElytraSettings setting = ElytraSettings.TELEPORT_TO_BLOCK;
				boolean value = DimensionalElytraplate.getElytraSetting(stack, setting)[1];
				Component compV = setting.getValueComp(value);
				
				Component[] comp = new Component[] { 
					ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.elytraplate.settings.tele_info"), 
					ComponentHelper.locComp(ComponentColour.GRAY, false, "dimensionalpocketsii.gui.elytraplate.settings.tele_value").append(compV)
				};
					
				this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
			} else if (this.visorButton.isMouseOver(mouseX, mouseY)) {
				ElytraSettings setting = ElytraSettings.VISOR;
				boolean value = DimensionalElytraplate.getElytraSetting(stack, setting)[1];
				Component compV = setting.getValueComp(value);
				
				Component[] comp = new Component[] { 
					ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.elytraplate.settings.visor_info"), 
					ComponentHelper.locComp(ComponentColour.GRAY, false, "dimensionalpocketsii.gui.elytraplate.settings.visor_value").append(compV),
					ComponentHelper.locComp(ComponentColour.RED, true, "dimensionalpocktesii.gui.elytraplate.settings.visor_warn")
				};
					
				this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
			} else if (this.solarButton.isMouseOver(mouseX, mouseY)) {
				ElytraSettings setting = ElytraSettings.SOLAR;
				boolean value = DimensionalElytraplate.getElytraSetting(stack, setting)[1];
				Component compV = setting.getValueComp(value);
				
				Component[] comp = new Component[] { 
					ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.elytraplate.settings.solar_info"), 
					ComponentHelper.locComp(ComponentColour.GRAY, false, "dimensionalpocketsii.gui.elytraplate.settings.solar_value").append(compV)
				};
					
				this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
			} else if (this.chargerButton.isMouseOver(mouseX, mouseY)) {
				ElytraSettings setting = ElytraSettings.CHARGER;
				boolean value = DimensionalElytraplate.getElytraSetting(stack, setting)[1];
				Component compV = setting.getValueComp(value);
				
				Component[] comp = new Component[] { 
					ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.elytraplate.settings.charger_info"), 
					ComponentHelper.locComp(ComponentColour.GRAY, false, "dimensionalpocketsii.gui.elytraplate.settings.charger_value").append(compV)
				};
					
				this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
			}

			else if (IS_HOVERING.isHovering(mouseX, mouseY, this.getScreenCoords()[0] + 38, this.getScreenCoords()[0] + 153, this.getScreenCoords()[1] + 43, this.getScreenCoords()[1] + 49)) {
				DecimalFormat formatter = new DecimalFormat("#,###,###,###");
				String amount_string = formatter.format(item.getEnergy(this.stack));
				String capacity_string = formatter.format(item.getMaxEnergyStored(this.stack));
				
				Component[] comp = new Component[] { ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.energy_bar.pre"), ComponentHelper.locComp(ComponentColour.RED, false, amount_string + " / " + capacity_string, "dimensionalpocketsii.gui.energy_bar.suff") };
				
				this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
			}
			
		} else {
			if (IS_HOVERING.isHovering(mouseX, mouseY, this.getScreenCoords()[0] + this.fluidBarData[0] - 1, this.getScreenCoords()[0] + this.fluidBarData[0] + 16, this.getScreenCoords()[1] + this.fluidBarData[2] - 27, this.getScreenCoords()[1] + this.fluidBarData[2] + 39)) {
				FluidTank tank = pocket.getFluidTank();
				
				DecimalFormat formatter = new DecimalFormat("#,###,###,###");
				String amount_string = formatter.format(tank.getFluidAmount());
				String capacity_string = formatter.format(tank.getCapacity());
				String fluid_name = tank.getFluid().getTranslationKey();
				
				Component[] comp = new Component[] { ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.fluid_bar.pre").append(ComponentHelper.locComp(ComponentColour.CYAN, true, "[ ", fluid_name, " ]")), 
						ComponentHelper.locComp(ComponentColour.ORANGE, false, amount_string + " / " + capacity_string, "dimensionalpocketsii.gui.fluid_bar.suff") };
				
				this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
			} 
			
			else if (IS_HOVERING.isHovering(mouseX, mouseY, this.getScreenCoords()[0] + this.energyBarData[0] - 1, this.getScreenCoords()[0] + this.energyBarData[0] + 16, this.getScreenCoords()[1] + this.energyBarData[1] - 1, this.getScreenCoords()[1] + this.energyBarData[1] + 64)) {
				DecimalFormat formatter = new DecimalFormat("#,###,###,###");
				String amount_string = formatter.format(pocket.getEnergyStored());
				String capacity_string = formatter.format(pocket.getMaxEnergyStored());
				
				Component[] comp = new Component[] { ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.energy_bar.pre"), ComponentHelper.locComp(ComponentColour.RED, false, amount_string + " / " + capacity_string, "dimensionalpocketsii.gui.energy_bar.suff") };
				
				this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
			}
			
			else if (this.buttonLock.isMouseOver(mouseX, mouseY)) {
				Component[] comp = new Component[] { ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.lock_info"),
						ComponentHelper.locComp(ComponentColour.GRAY, false, "dimensionalpocketsii.gui.lock_value").append(pocket.getLockState().getColouredComp()) };
				
				this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
			} else if (this.buttonTrapPlayers.isMouseOver(mouseX, mouseY)) {
				Component[] comp = new Component[] { ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.trap_players_info"), 
						ComponentHelper.locComp(ComponentColour.GRAY, false, "dimensionalpocketsii.gui.trap_players_value").append(pocket.getTrapState().getColouredComp()) };
				
				this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
			} else if (this.buttonHostileSpawn.isMouseOver(mouseX, mouseY)) {
				Component[] comp = new Component[] { ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.hostile_spawn_info"), 
						ComponentHelper.locComp(ComponentColour.GRAY, false, "dimensionalpocketsii.gui.hostile_spawn_value").append(pocket.getHostileSpawnState().getColouredComp()) };
				
				this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
			} else if (this.buttonAllowedPlayers.isMouseOver(mouseX, mouseY)) {
				Component[] comp = new Component[] { ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.allowed_player_info"), 
						ComponentHelper.locComp(ComponentColour.GRAY, false, "dimensionalpocketsii.gui.allowed_player_value").append(pocket.getAllowedPlayerState().getColouredComp()) };
				
				this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
			}
			
			else if (this.buttonTextClear.isMouseOver(mouseX, mouseY)) {
				if (this.buttonTextClear.active) {
					this.renderTooltip(poseStack, ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.button.text.clear"),  mouseX, mouseY);
				}
			} else if (this.buttonTextPlus.isMouseOver(mouseX, mouseY)) {
				if (this.buttonTextPlus.active) {
					this.renderTooltip(poseStack, ComponentHelper.locComp(ComponentColour.GREEN, false, "dimensionalpocketsii.gui.button.text.plus"), mouseX, mouseY);
				}
			} else if (this.buttonTextMinus.isMouseOver(mouseX, mouseY)) {
				if (this.buttonTextMinus.active) {
					this.renderTooltip(poseStack, ComponentHelper.locComp(ComponentColour.RED, false, "dimensionalpocketsii.gui.button.text.minus"), mouseX, mouseY);
				}
			}
			
			else if (this.buttonTankClear != null) {
				if (this.buttonTankClear.isMouseOver(mouseX, mouseY)) {
					if (this.buttonTankClear.active) {
						if (!hasShiftDown()) {
							this.renderTooltip(poseStack, ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.button.tank_clear"), mouseX, mouseY);
						} else {
							Component[] comp = new Component[] { 
									ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.button.tank_clear"),
									ComponentHelper.locComp(ComponentColour.RED, true, "dimensionalpocketsii.gui.button.tank_clear_shift") };
							
							this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
						}
					}
				}
			}
		}
	}

	protected void updateWidgetList() {
		if (this.pocket != null && this.screen) {
			this.updateFromList(this.pocket.getAllowedPlayersArray());
			this.renderSmallWidgetList();
		}
	}
	
	protected void addButtons() {
		this.clearWidgets();
		
		if (!this.screen) {
			this.uiModeButton = this.addRenderableWidget(new CosmosButtonUIMode(this.getUIMode(), this.getScreenCoords()[0] + 144, this.getScreenCoords()[1] + 5, true, true, ComponentHelper.locComp(""), (button) -> { this.changeUIMode(); } ));
			
			this.elytraFlyButton = this.addRenderableWidget(new DimensionalButton(this.getScreenCoords()[0] + 36, this.getScreenCoords()[1] + 19, 20, true, true, DimensionalElytraplate.getElytraSetting(this.stack, ElytraSettings.ELYTRA_FLY)[1] ? 16 : 17, ComponentHelper.locComp(""), (button) -> { this.pushButton(button); }));
			this.teleToBlockButton = this.addRenderableWidget(new DimensionalButton(this.getScreenCoords()[0] + 61, this.getScreenCoords()[1] + 19, 20, DimensionalElytraplate.hasModuleInstalled(this.stack, BaseElytraModule.SHIFTER), true, DimensionalElytraplate.getElytraSetting(this.stack, ElytraSettings.TELEPORT_TO_BLOCK)[1] ? 18 : 19, ComponentHelper.locComp(""), (button) -> { this.pushButton(button); }));
			this.visorButton = this.addRenderableWidget(new DimensionalButton(this.getScreenCoords()[0] + 86, this.getScreenCoords()[1] + 19, 20, DimensionalElytraplate.hasModuleInstalled(this.stack, BaseElytraModule.VISOR), true, DimensionalElytraplate.getElytraSetting(this.stack, ElytraSettings.VISOR)[1] ? 20 : 21, ComponentHelper.locComp(""), (button) -> { this.pushButton(button); }));
			
			this.solarButton = this.addRenderableWidget(new DimensionalButton(this.getScreenCoords()[0] + 111, this.getScreenCoords()[1] + 19, 20, DimensionalElytraplate.hasModuleInstalled(this.stack, BaseElytraModule.SOLAR), true, DimensionalElytraplate.getElytraSetting(this.stack, ElytraSettings.SOLAR)[1] ? 22 : 23, ComponentHelper.locComp(""), (button) -> { this.pushButton(button); }));
			this.chargerButton = this.addRenderableWidget(new DimensionalButton(this.getScreenCoords()[0] + 136, this.getScreenCoords()[1] + 19, 20, DimensionalElytraplate.hasModuleInstalled(this.stack, BaseElytraModule.BATTERY), true, DimensionalElytraplate.getElytraSetting(this.stack, ElytraSettings.CHARGER)[1] ? 24 : 25, ComponentHelper.locComp(""), (button) -> { this.pushButton(button); }));
		} else {
			this.uiModeButton = this.addRenderableWidget(new CosmosButtonUIMode(this.getUIMode(), this.getScreenCoords()[0] + 343, this.getScreenCoords()[1] + 5, true, true, ComponentHelper.locComp(""), (button) -> { this.changeUIMode(); } ));
			
			this.buttonTrapPlayers = this.addRenderableWidget(new DimensionalButton(this.getScreenCoords()[0] + TPBI[0], this.getScreenCoords()[1] + TPBI[1], 20, true, true, pocket.getTrapState().getIndex() + 14, ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonTrapPlayers); }));
			
			this.buttonTankClear = this.addRenderableWidget(new CosmosButtonWithType(TYPE.FLUID, this.getScreenCoords()[0] + TBCI[0], this.getScreenCoords()[1] + TBCI[1], 20, !pocket.getFluidTank().isEmpty(), true, pocket.getFluidTank().isEmpty() ? 15 : 16, ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonTankClear); }));
			this.buttonLock = this.addRenderableWidget(new CosmosButtonWithType(TYPE.GENERAL, this.getScreenCoords()[0] + LBI[0], this.getScreenCoords()[1] + LBI[1], 20, true, true, pocket.getLockState().getIndex() + 8, ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonLock); }));
			this.buttonAllowedPlayers = this.addRenderableWidget(new CosmosButtonWithType(TYPE.GENERAL, this.getScreenCoords()[0] + APBI[0], this.getScreenCoords()[1] + APBI[1], 20, true, true, pocket.getAllowedPlayerState().getIndex() + 10, ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonAllowedPlayers); }));
			this.buttonHostileSpawn = this.addRenderableWidget(new CosmosButtonWithType(TYPE.GENERAL, this.getScreenCoords()[0] + HSBI[0], this.getScreenCoords()[1] + HSBI[1], 20, true, true, pocket.getHostileSpawnState().getIndex() + 15, ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonHostileSpawn); }));
			
			this.buttonTextClear = this.addRenderableWidget(new CosmosButtonWithType(TYPE.GENERAL, this.getScreenCoords()[0] + TCBI[0], this.getScreenCoords()[1] + TCBI[1], 18, !(this.textField.getValue().isEmpty()), true, 14, ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonTextClear); }));
			this.buttonTextPlus = this.addRenderableWidget(new CosmosButtonWithType(TYPE.GENERAL, this.getScreenCoords()[0] + TABI[0], this.getScreenCoords()[1] + TABI[1], 18, !(this.textField.getValue().isEmpty()), true, 1, ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonTextPlus); }));
			this.buttonTextMinus = this.addRenderableWidget(new CosmosButtonWithType(TYPE.GENERAL, this.getScreenCoords()[0] + TMBI[0], this.getScreenCoords()[1] + TMBI[1], 18, true, true, 2, ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonTextMinus); }));
			
			this.buttonPlaceHolder0 = this.addRenderableWidget(new CosmosButtonWithType(TYPE.GENERAL, this.getScreenCoords()[0] + PB0I[0], this.getScreenCoords()[1] + PB0I[1], 20, true, true, 0, ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonPlaceHolder0); }));
			this.buttonPlaceHolder1 = this.addRenderableWidget(new CosmosButtonWithType(TYPE.GENERAL, this.getScreenCoords()[0] + PB1I[0], this.getScreenCoords()[1] + PB1I[1], 20, true, true, 0, ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonPlaceHolder1); }));
			this.buttonPlaceHolder2 = this.addRenderableWidget(new CosmosButtonWithType(TYPE.GENERAL, this.getScreenCoords()[0] + PB2I[0], this.getScreenCoords()[1] + PB2I[1], 20, true, true, 0, ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonPlaceHolder2); }));
		}
	}
	
	private void pushButton(Button button) {
		if (!this.screen) {
			if (button.equals(this.elytraFlyButton)) {
				ElytraSettings setting = ElytraSettings.ELYTRA_FLY;
				
				NetworkManager.sendToServer(new PacketElytraSettingsChange(this.playerUUID, 2, setting, !DimensionalElytraplate.getElytraSetting(stack, setting)[1]));
				DimensionalElytraplate.addOrUpdateElytraSetting(this.stack, setting, !DimensionalElytraplate.getElytraSetting(stack, setting)[1]);
			} else if (button.equals(this.teleToBlockButton)) {
				ElytraSettings setting = ElytraSettings.TELEPORT_TO_BLOCK;
				
				NetworkManager.sendToServer(new PacketElytraSettingsChange(this.playerUUID, 2, setting, !DimensionalElytraplate.getElytraSetting(stack, setting)[1]));
				DimensionalElytraplate.addOrUpdateElytraSetting(this.stack, setting, !DimensionalElytraplate.getElytraSetting(stack, setting)[1]);
			} else if (button.equals(this.visorButton)) {
				ElytraSettings setting = ElytraSettings.VISOR;
				
				NetworkManager.sendToServer(new PacketElytraSettingsChange(this.playerUUID, 2, setting, !DimensionalElytraplate.getElytraSetting(stack, setting)[1]));
				DimensionalElytraplate.addOrUpdateElytraSetting(this.stack, setting, !DimensionalElytraplate.getElytraSetting(stack, setting)[1]);
			} else if (button.equals(this.solarButton)) {
				ElytraSettings setting = ElytraSettings.SOLAR;
				
				NetworkManager.sendToServer(new PacketElytraSettingsChange(this.playerUUID, 2, setting, !DimensionalElytraplate.getElytraSetting(stack, setting)[1]));
				DimensionalElytraplate.addOrUpdateElytraSetting(this.stack, setting, !DimensionalElytraplate.getElytraSetting(stack, setting)[1]);
			} else if (button.equals(this.chargerButton)) {
				ElytraSettings setting = ElytraSettings.CHARGER;
				
				NetworkManager.sendToServer(new PacketElytraSettingsChange(this.playerUUID, 2, setting, !DimensionalElytraplate.getElytraSetting(stack, setting)[1]));
				DimensionalElytraplate.addOrUpdateElytraSetting(this.stack, setting, !DimensionalElytraplate.getElytraSetting(stack, setting)[1]);
			}
		} else {
			if (button.equals(this.buttonLock)) {
				PocketNetworkManager.sendToServer(new EPacketLock(this.pocket.getChunkPos(), !pocket.getLockStateValue()));
				pocket.setLockState(!pocket.getLockStateValue());
			} else if (button.equals(this.buttonTrapPlayers)) {
				PocketNetworkManager.sendToServer(new EPacketTrapPlayers(this.pocket.getChunkPos(), !pocket.getTrapStateValue()));
				pocket.setTrapState(!pocket.getTrapStateValue());
			} else if (button.equals(this.buttonHostileSpawn)) {
				PocketNetworkManager.sendToServer(new EPacketHostileSpawnState(this.pocket.getChunkPos(), !pocket.getHostileSpawnStateValue()));
				pocket.setHostileSpawnState(!pocket.getHostileSpawnStateValue());
			} else if (button.equals(this.buttonAllowedPlayers)) {
				PocketNetworkManager.sendToServer(new EPacketLockToAllowedPlayers(this.pocket.getChunkPos(), !pocket.getAllowedPlayerStateValue()));
				pocket.setAllowedPlayerState(!pocket.getAllowedPlayerStateValue());
			}
			
			if (this.textField != null) {
				String value = this.textField.getValue();
				
				if (button.equals(this.buttonTextClear)) {
					this.textField.setValue("");
				} else if (button.equals(this.buttonTextPlus)) {
					if (!value.isEmpty() && value.length() >= 3) {
						PocketNetworkManager.sendToServer(new EPacketAllowedPlayer(this.pocket.getChunkPos(), value, true));
						pocket.addAllowedPlayerNBT(value);
						
						this.clearWidgetList();
						this.textField.setValue("");
					} 
				} else if (button.equals(this.buttonTextMinus)) {
					if (!value.isEmpty()) {
						PocketNetworkManager.sendToServer(new EPacketAllowedPlayer(this.pocket.getChunkPos(), value, false));
						pocket.removeAllowedPlayerNBT(value);
	
						this.clearWidgetList();
						this.textField.setValue("");
					} else {
						int selected = this.getSelectedWidgetIndex();
						
						if (selected != 0) {
							String string = this.getWidgetList().get(selected).getDisplayString();
							PocketNetworkManager.sendToServer(new EPacketAllowedPlayer(this.pocket.getChunkPos(), string, false));
							
							pocket.removeAllowedPlayerNBT(string);
							this.clearWidgetList();
						}
					}
				}
				
				else if (button.equals(this.buttonTankClear)) {
					if (hasShiftDown()) {
						PocketNetworkManager.sendToServer(new EPacketEmptyTank(this.pocket.getChunkPos()));
						pocket.emptyFluidTank();
					}
				}
			}
		}
	}

	public void initTextField() {
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.textField = new EditBox(this.font, this.getScreenCoords()[0] + this.textFieldI[0], this.getScreenCoords()[1] + this.textFieldI[1], this.textFieldI[2], this.textFieldI[3], ComponentHelper.locComp("Allowed Player Entry"));
		this.textField.setMaxLength(16);

		if (this.screen) {
			this.textField.setVisible(true);
		}
		
		this.textField.setTextColor(CosmosUISystem.DEFAULT_COLOUR_FONT_LIST);
		this.textField.setBordered(false);
		this.textField.setCanLoseFocus(true);
		
		if (this.screen) {
			this.textField.setEditable(true);
		}
		
		this.addWidget(this.textField);
	}

	protected void setImageDims(int widthIn, int heightIn) {
		this.imageWidth = widthIn;
		this.imageHeight = heightIn;
	}
	
	protected void setTitleLabelDims(int posX, int posY) {
		this.titleLabelX = posX;
		this.titleLabelY = posY;
	}
	
	protected void setInventoryLabelDims(int posX, int posY) {
		this.inventoryLabelX = posX;
		this.inventoryLabelY = posY;
	}
	
	protected void setScreenCoords(int[] coordsIn) {
		this.screenCoords = coordsIn;
	}
	
	protected int[] getScreenCoords() {
		return this.screenCoords;
	}

	public EnumUIMode getUIMode() {
		if (this.stack != null) {
			return DimensionalElytraplate.getUIMode(this.stack);
		}
		
		return EnumUIMode.DARK;
	}

	private void changeUIMode() {
		NetworkManager.sendToServer(new PacketElytraplateUpdateUIMode(this.playerUUID, 2, this.getUIMode().getNextState()));
		DimensionalElytraplate.setUIMode(this.stack, this.getUIMode().getNextState());
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		for (int i = 0; i < widgetList.size(); i++) {
			CosmosListWidget widget = this.getListWidget(i);
			
			if (widget.isMouseOver()) {
				widget.mousePressed(this.minecraft, mouseX, mouseY);
				this.selectWidget(i);
			} else {
				if (mouseX < this.listIndex[0] && mouseX > this.listIndex[0] + this.listIndex[2]) {
					if (mouseY < this.listIndex[1] && mouseY > this.listIndex[1] + this.listIndex[3]) {
						//this.deselectWidget(i);//widget.deselect();
					}
				}
			}
		}
		
		return ((this.textField != null) && this.textField.mouseClicked(mouseX, mouseY, mouseButton)) ? true : super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double direction) {
		if (this.scrollEnabled()) {
			int maxScroll = this.widgetList.size() - this.widgetCount();
			this.currentScroll += -direction;
			
			if (this.currentScroll >= 0) {
				if (this.currentScroll <= maxScroll) {
					
					if (direction == -1.0F) {
						for (int i = 0; i < this.widgetList.size(); i++) {
							CosmosListWidget widget = this.widgetList.get(i);
							
							widget.setPositionToLastWidget(this.listIndex[5]);
						}
					} else {
						for (int i = 0; i < this.widgetList.size(); i++) {
							CosmosListWidget widget = this.widgetList.get(i);
							
							widget.setPositionToNextWidget(this.listIndex[5]);
						}
					}
					
					return true;
				} else {
					this.currentScroll = maxScroll;
					return false;
				}
			} else {
				this.currentScroll = 0;
				return false;
			}
		} 
		
		return ((this.textField != null) && this.textField.mouseScrolled(mouseX, mouseY, direction)) ? true : super.mouseScrolled(mouseX, mouseY, direction);
	}

	@Override
	public boolean keyPressed(int keyCode, int mouseX, int mouseY) {
		if (keyCode == 256) {
			this.minecraft.player.closeContainer();
		}
		
		return  ((this.textField != null) && !this.textField.keyPressed(keyCode, mouseX, mouseY) && !this.textField.canConsumeInput()) ? super.keyPressed(keyCode, mouseX, mouseY) : true;
	}

	@Override
	public boolean charTyped(char charIn, int p_98522_) {
		return  ((this.textField != null) && !this.textField.charTyped(charIn, p_98522_) && !this.textField.canConsumeInput()) ? super.charTyped(charIn, p_98522_) : true;
	}
	
	@Override
	public void resize(Minecraft mc, int width, int height) {
		this.clearWidgetList();
		super.resize(mc, width, height);
	}
	
	protected void setListDims(int x, int y, int width, int height, int widgetHeightIn, int widgetSpacingIn) {
		this.listIndex = new int[] { x, y, width, height, widgetHeightIn, widgetSpacingIn};
	}
	
	protected void setScrollElementDims(int x, int y) {
		this.scrollEnabled = true;
		this.scrollElementIndex = new int[] { x, y };
	}

	protected void setWidgetTexture(ResourceLocation textureIn) {
		this.WIDGET_TEXTURE = textureIn;
	}
	
	protected void updateFromList(ArrayList<String> fromListIn) {
		this.fromList = fromListIn;
	}
	
	public CosmosListWidget addListWidget(CosmosListWidget widget) {
		this.widgetList.add(widget);
		return widget;
	}
	
	protected CosmosListWidget getListWidget(int index) {
		return this.widgetList.get(index);
	}

	protected void setWidgetList(ArrayList<CosmosListWidget> list) {
		this.widgetList = list;
	}
	
	protected ArrayList<CosmosListWidget> getWidgetList() {
		return this.widgetList;
	}
	
	protected void selectWidget(int index) {
		for (int i = 0; i < this.widgetList.size(); i++) {
			this.widgetList.get(i).deselect();
		}
		
		this.widgetList.get(index).setSelectedState(true);
	}

	protected void deselectWidget(int index) {
		this.widgetList.get(index).setSelectedState(false);
	}
	
	public int getSelectedWidgetIndex() {
		for (int i = 0; i < this.widgetList.size(); i++) {
			if (this.getListWidget(i).getSelected()) {
				return i;
			}
		}
		
		return 0;
	}
	
	public int widgetCount() {
		return (int) Math.floor(this.listIndex[3] / (this.listIndex[4]));
	}
	
	public boolean scrollEnabled() {
		if (this.widgetList.size() > this.widgetCount()) {
			return true;
		} else {
			return false;
		}
	}

	protected void clearWidgetList() {
		this.widgetList.clear();
	}
	
	private void renderWidgetList(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		//DimensionalPockets.CONSOLE.info(this.widgetList);
		
		for (int i = 0; i < this.widgetList.size(); i++) {
			CosmosListWidget widget = this.getListWidget(i);
			
			if ((widget.getYPos() + widget.getHeight()) <= this.listIndex[2]) {
				if (this.scrollEnabled()) {
					int j = i + this.currentScroll;
					
					if (!(j > this.widgetList.size()) && j < this.widgetList.size()) {
						CosmosListWidget firstWidget = this.widgetList.get(j);

						firstWidget.renderWidget(poseStack, this.font, this.getScreenCoords(), mouseX, mouseY, j, this.listIndex[2]);
					}
				} else {
					widget.renderWidget(poseStack, this.font, this.getScreenCoords(), mouseX, mouseY, i, this.listIndex[2]);
				}
			}
		}
	}
	
	protected void renderSmallWidgetList() {
		int spacing_y = this.listIndex[4] + this.listIndex[5];
		
		ArrayList<CosmosListWidget> new_list = new ArrayList<CosmosListWidget>();
		
		if (this.fromList.isEmpty()) {
			return;
		} else {
			for (int j = 0; j < this.fromList.size(); j++) {
				String string = this.fromList.get(j);
				
				if (string != null) {
					for (int i = 0; i < this.getWidgetList().size(); i++) {
						String test_string = this.getWidgetList().get(i).getDisplayString();
						if (string.equals(test_string)) {
							return;
						}
					}
				}
			}
		}
		
		for (int i = 0; i < this.fromList.size(); i++) {
			String display_string = this.fromList.get(i);
			CosmosListWidget widget = new CosmosListWidget(this.listIndex[0], this.listIndex[1] + (spacing_y * i), this.listIndex[2], this.listIndex[4], this.WIDGET_TEXTURE, display_string, ComponentColour.WHITE);
			
			if (new_list.size() < 1) {
				new_list.add(widget);
			} else {
				if (!(new_list.contains(widget))) {
					new_list.add(widget);
				}
			}
		}
		
		this.setWidgetList(new_list);
	}
	
	protected void renderScrollElement(PoseStack poseStack) {
		int[] scrollType = new int[] { 15, 0, 15 };
		
		CosmosUISystem.setTextureWithColour(poseStack, BASE.GUI_ELEMENT_MISC_LOC, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
		
		if (this.widgetList.size() > 0) {
			int div;
			
			if (this.widgetList.size() == this.widgetCount()) {
				div = 1;
			} else {
				div = this.widgetList.size() - this.widgetCount();
			}
			
			int increment = (this.listIndex[3] - 12) / div;
			
			int posX = this.scrollElementIndex[0];
			int posY = Mth.clamp((this.scrollElementIndex[1] + (this.currentScroll * increment)), 0, (this.listIndex[1] + this.listIndex[3] - 1) - this.listIndex[4]);

			int type = this.getUIMode().equals(EnumUIMode.DARK) ? 0 : 1;
			
			CosmosUISystem.renderStaticElementToggled(this, poseStack, this.getScreenCoords(), posX, this.scrollEnabled ? posY : this.scrollElementIndex[1], scrollType[0], scrollType[type + 1], 13, 15, true);
		}
	}
}