package com.tcn.dimensionalpocketsii.pocket.client.screen;

import java.util.Arrays;

import com.ibm.icu.text.DecimalFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tcn.cosmoslibrary.client.ui.lib.CosmosUISystem;
import com.tcn.cosmoslibrary.client.ui.lib.CosmosUISystem.FONT;
import com.tcn.cosmoslibrary.client.ui.lib.CosmosUISystem.IS_HOVERING;
import com.tcn.cosmoslibrary.client.ui.screen.CosmosScreenUIModeList;
import com.tcn.cosmoslibrary.client.ui.screen.widget.CosmosButtonWithType;
import com.tcn.cosmoslibrary.client.ui.screen.widget.CosmosButtonWithType.TYPE;
import com.tcn.cosmoslibrary.common.enums.EnumUIMode;
import com.tcn.cosmoslibrary.common.lib.ComponentColour;
import com.tcn.cosmoslibrary.common.lib.ComponentHelper;
import com.tcn.dimensionalpocketsii.DimReference.GUI;
import com.tcn.dimensionalpocketsii.DimReference.GUI.RESOURCE;
import com.tcn.dimensionalpocketsii.client.screen.button.DimensionalButton;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerPocket;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityPocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketNetworkManager;
import com.tcn.dimensionalpocketsii.pocket.network.packet.block.PacketSideGuide;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketAllowedPlayer;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketBlockSideState;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketEmptyTank;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketHostileSpawnState;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketLock;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketLockToAllowedPlayers;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketTrapPlayers;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.capability.templates.FluidTank;

@OnlyIn(Dist.CLIENT)
public class ScreenPocket extends CosmosScreenUIModeList<ContainerPocket> {
	
	private int[] energyBarData = new int[] { 15, 177 };
	private int[] fluidBarData = new int[] { 36, 52, 203, 157, 18, 58 };
	
	private CosmosButtonWithType buttonTextClear; private int[] TCBI = new int[] { 191, 18, 18 };
	private CosmosButtonWithType buttonTextPlus; private int[] TABI = new int[] { 212, 18, 18 };
	private CosmosButtonWithType buttonTextMinus; private int[] TMBI = new int[] { 233, 18, 18 };
	
	private CosmosButtonWithType buttonTankClear; private int[] TBCI = new int[] { 56, 222, 20 };

	private EditBox textField; private int[] textFieldI = new int[] { 98, 23, 93, 16 };

	private DimensionalButton buttonNorth; private int[] NBI = new int[] { 56, 70, 18 };
	private DimensionalButton buttonSouth; private int[] SBI = new int[] { 56, 97, 18 };
	private DimensionalButton buttonUp; private int[] UBI = new int[] { 56, 20, 18 };
	private DimensionalButton buttonDown; private int[] DBI = new int[] { 56, 45, 18 };
	private DimensionalButton buttonEast; private int[] EBI = new int[] { 56, 147, 18 };
	private DimensionalButton buttonWest; private int[] WBI = new int[] { 56, 122, 18 };

	private CosmosButtonWithType buttonLock; private int[] LBI = new int[] { 93, 130 };
	private CosmosButtonWithType buttonAllowedPlayers; private int[] APBI = new int[] { 116, 130 };
	
	private DimensionalButton buttonSideGuide; private int[] SIBI = new int[] { 162, 130 };
	private DimensionalButton buttonTrapPlayers; private int[] TPBI = new int[] { 139, 130 };
	
	private CosmosButtonWithType buttonHostileSpawn; private int[] HSBI = new int[] { 185, 130 };
	private CosmosButtonWithType buttonPlaceHolder0; private int[] PB0I = new int[] { 208, 130 };
	private CosmosButtonWithType buttonPlaceHolder1; private int[] PB1I = new int[] { 231, 130 };

	public ScreenPocket(ContainerPocket containerIn, Inventory playerInventoryIn, Component title) {
		super(containerIn, playerInventoryIn, title);

		this.setImageDims(360, 256);
		
		this.setLight(RESOURCE.POCKET[0]);
		this.setDark(RESOURCE.POCKET[1]);
		
		this.setDualScreen();
		this.setDualScreenIndex(256, 0, 104, 256);
		
		this.setDualLight(RESOURCE.POCKET_SIDE[0]);
		this.setDualDark(RESOURCE.POCKET_SIDE[1]);

		this.setUIModeButtonIndex(343, 5);
		this.setUIHelpButtonIndex(343, 19);
		this.setUIHelpTitleOffset(5);
		this.setUIHelpElementDeadzone(0, 0, 360, 256);
		
		this.setTitleLabelDims(23, 4);
		this.setInventoryLabelDims(88, 157);

		this.setScrollElementDims(237, 52);
		this.setListDims(94, 52, 138, 74, 14, 1);
	}

	@Override
	protected void init() {
		this.setScreenCoords(CosmosUISystem.getScreenCoords(this, this.imageWidth, this.imageHeight));
		this.initTextField();
		super.init();
	}

	@Override
	public void containerTick() {
		super.containerTick();
		this.textField.tick();
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		super.render(poseStack, mouseX, mouseY, partialTicks);
		
		BlockEntity entity = this.getBlockEntity();
		
		if (entity instanceof BlockEntityPocket) {
			BlockEntityPocket blockEntity = (BlockEntityPocket) entity;
			
			if (blockEntity.getPocket() != null) {
				Pocket pocket = blockEntity.getPocket();

				ComponentColour colour = ComponentColour.col(pocket.getDisplayColour());
				ComponentColour textColour = blockEntity.getUIMode().equals(EnumUIMode.LIGHT) ? ComponentColour.BLACK : ComponentColour.SCREEN_LIGHT;
				
				FONT.drawString(poseStack, font, this.getScreenCoords(), 93, 40, true, ComponentHelper.locComp(ComponentColour.getCompColourForScreen(colour), false, "dimensionalpocketsii.gui.header.allowed_players"));

				FONT.drawString(poseStack, font, this.getScreenCoords(), 88, 4, true, ComponentHelper.locComp(textColour, false, "dimensionalpocketsii.gui.pocket.header"));
				FONT.drawString(poseStack, font, this.getScreenCoords(), 8, 4, true, ComponentHelper.locComp(textColour, false, "dimensionalpocketsii.gui.header.config"));
				FONT.drawString(poseStack,font, this.getScreenCoords(), 262, 4, true, ComponentHelper.locComp(textColour, false, "dimensionalpocketsii.gui.header.pocket_inv"));
			}
		}
		
		this.textField.render(poseStack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
		super.renderBg(poseStack, partialTicks, mouseX, mouseY);

		BlockEntity entity = this.getBlockEntity();
		
		if (entity instanceof BlockEntityPocket) {
			BlockEntityPocket blockEntity = (BlockEntityPocket) entity;
	
			if (blockEntity.getPocket() != null) {
				Pocket pocket = blockEntity.getPocket();

				ComponentColour colour = ComponentColour.col(pocket.getDisplayColour());
				float[] rgb = colour.equals(ComponentColour.POCKET_PURPLE) ? ComponentColour.rgbFloatArray(ComponentColour.POCKET_PURPLE_LIGHT) : ComponentColour.rgbFloatArray(colour);
				
				CosmosUISystem.renderStaticElementWithUIMode(this, poseStack, this.getScreenCoords(), 0, 0, 0, 0, 256, 256, new float[] { rgb[0], rgb[1], rgb[2], 1.0F }, blockEntity, GUI.RESOURCE.POCKET_BASE_NORMAL);
				CosmosUISystem.renderStaticElementWithUIMode(this, poseStack, this.getScreenCoords(), 256, 0, 0, 0, 92, 256, new float[] { rgb[0], rgb[1], rgb[2], 1.0F }, blockEntity, GUI.RESOURCE.POCKET_BASE_SIDE);
				
				CosmosUISystem.renderStaticElementWithUIMode(this, poseStack, this.getScreenCoords(), 0, 0, 0, 0, 256, 256, blockEntity, GUI.RESOURCE.POCKET_OVERLAY_NORMAL);
				CosmosUISystem.renderStaticElementWithUIMode(this, poseStack, this.getScreenCoords(), 256, 0, 0, 0, 92, 256, blockEntity, GUI.RESOURCE.POCKET_OVERLAY_SIDE);
				
				CosmosUISystem.renderFluidTank(this, poseStack, this.getScreenCoords(), this.fluidBarData[0], this.fluidBarData[2], pocket.getFluidTank(), pocket.getFluidLevelScaled(64));

				CosmosUISystem.renderEnergyDisplay(this, poseStack, ComponentColour.RED, pocket, this.getScreenCoords(), this.energyBarData[0], this.energyBarData[1], 16, 64, false);
			}
		}

		this.textField.render(poseStack, mouseX + 30, mouseY, partialTicks);
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
		BlockEntity entity = this.getBlockEntity();
		
		if (entity instanceof BlockEntityPocket) {
			BlockEntityPocket blockEntity = (BlockEntityPocket) entity;
			
			this.font.draw(poseStack, this.playerInventoryTitle, (float) this.inventoryLabelX, (float) this.inventoryLabelY, blockEntity.getUIMode().equals(EnumUIMode.DARK) ? CosmosUISystem.DEFAULT_COLOUR_FONT_LIST : ComponentColour.BLACK.dec());
		}
	}
	
	@Override
	protected void updateWidgetList() {
		BlockEntity entity = this.getBlockEntity();
		
		if (entity instanceof BlockEntityPocket) {
			BlockEntityPocket blockEntity = (BlockEntityPocket) entity;
			
			if (blockEntity.getPocket() != null) {
				Pocket pocket = blockEntity.getPocket();
				this.updateFromList(pocket.getAllowedPlayersArray());
				super.updateWidgetList();
			}
		}
	}
	
	@Override
	protected void addButtons() {
		super.addButtons();
		
		BlockEntity entity = this.getBlockEntity();
		
		if (entity instanceof BlockEntityPocket) {
			BlockEntityPocket blockEntity = (BlockEntityPocket) entity;
			
			if (blockEntity.getPocket() != null) {
				Pocket pocket = blockEntity.getPocket();
		
				int[] sides = new int[] { 0, 0, 0, 0, 0, 0 };
				for(Direction c : Direction.values()) {
					sides[c.get3DDataValue()] = (pocket.getSide(c).getIndex());
				}
				
				this.buttonDown  = this.addRenderableWidget(new DimensionalButton(this.getScreenCoords()[0] + DBI[0], this.getScreenCoords()[1] + DBI[1], 18, true, true, sides[0], ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonDown);  }));
				this.buttonUp    = this.addRenderableWidget(new DimensionalButton(this.getScreenCoords()[0] + UBI[0], this.getScreenCoords()[1] + UBI[1], 18, true, true, sides[1], ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonUp);    }));
				this.buttonNorth = this.addRenderableWidget(new DimensionalButton(this.getScreenCoords()[0] + NBI[0], this.getScreenCoords()[1] + NBI[1], 18, true, true, sides[2], ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonNorth); }));
				this.buttonSouth = this.addRenderableWidget(new DimensionalButton(this.getScreenCoords()[0] + SBI[0], this.getScreenCoords()[1] + SBI[1], 18, true, true, sides[3], ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonSouth); }));
				this.buttonWest  = this.addRenderableWidget(new DimensionalButton(this.getScreenCoords()[0] + WBI[0], this.getScreenCoords()[1] + WBI[1], 18, true, true, sides[4], ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonWest);  }));
				this.buttonEast  = this.addRenderableWidget(new DimensionalButton(this.getScreenCoords()[0] + EBI[0], this.getScreenCoords()[1] + EBI[1], 18, true, true, sides[5], ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonEast);  }));
				
				this.buttonSideGuide = this.addRenderableWidget(new DimensionalButton(this.getScreenCoords()[0] + SIBI[0], this.getScreenCoords()[1] + SIBI[1], 20, true, true, blockEntity.getSideGuide().getIndex() + 12,  ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonSideGuide); }));
				this.buttonTrapPlayers = this.addRenderableWidget(new DimensionalButton(this.getScreenCoords()[0] + TPBI[0], this.getScreenCoords()[1] + TPBI[1], 20, true, true, pocket.getTrapState().getIndex() + 14,  ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonTrapPlayers); }));
				
				this.buttonTankClear = this.addRenderableWidget(new CosmosButtonWithType(TYPE.FLUID, this.getScreenCoords()[0] + TBCI[0], this.getScreenCoords()[1] + TBCI[1], 20, !pocket.getFluidTank().isEmpty(), true, pocket.getFluidTank().isEmpty() ? 15 : 16,  ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonTankClear); }));
				this.buttonLock = this.addRenderableWidget(new CosmosButtonWithType(TYPE.GENERAL, this.getScreenCoords()[0] + LBI[0], this.getScreenCoords()[1] + LBI[1], 20, true, true, pocket.getLockState().getIndex() + 8,  ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonLock); }));
				this.buttonAllowedPlayers = this.addRenderableWidget(new CosmosButtonWithType(TYPE.GENERAL, this.getScreenCoords()[0] + APBI[0], this.getScreenCoords()[1] + APBI[1], 20, true, true, pocket.getAllowedPlayerState().getIndex() + 10,  ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonAllowedPlayers); }));
				this.buttonHostileSpawn = this.addRenderableWidget(new CosmosButtonWithType(TYPE.GENERAL, this.getScreenCoords()[0] + HSBI[0], this.getScreenCoords()[1] + HSBI[1], 20, true, true, pocket.getHostileSpawnState().getIndex() + 15,  ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonHostileSpawn); }));
				
				this.buttonTextClear = this.addRenderableWidget(new CosmosButtonWithType(TYPE.GENERAL, this.getScreenCoords()[0] + TCBI[0], this.getScreenCoords()[1] + TCBI[1], 18, !(this.textField.getValue().isEmpty()), true, 14,  ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonTextClear); }));
				this.buttonTextPlus = this.addRenderableWidget(new CosmosButtonWithType(TYPE.GENERAL, this.getScreenCoords()[0] + TABI[0], this.getScreenCoords()[1] + TABI[1], 18, !(this.textField.getValue().isEmpty()), true, 1,  ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonTextPlus); }));
				this.buttonTextMinus = this.addRenderableWidget(new CosmosButtonWithType(TYPE.GENERAL, this.getScreenCoords()[0] + TMBI[0], this.getScreenCoords()[1] + TMBI[1], 18, true, true, 2,  ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonTextMinus); }));

				this.buttonPlaceHolder0 = this.addRenderableWidget(new CosmosButtonWithType(TYPE.GENERAL, this.getScreenCoords()[0] + PB0I[0], this.getScreenCoords()[1] + PB0I[1], 20, true, true, 0, ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonPlaceHolder0); }));
				this.buttonPlaceHolder1 = this.addRenderableWidget(new CosmosButtonWithType(TYPE.GENERAL, this.getScreenCoords()[0] + PB1I[0], this.getScreenCoords()[1] + PB1I[1], 20, true, true, 0, ComponentHelper.locComp(""), (button) -> { this.pushButton(this.buttonPlaceHolder1); }));
			}
		}
	}
	
	@Override
	public void renderStandardHoverEffect(PoseStack poseStack, Style style, int mouseX, int mouseY) {
		BlockEntity entity = this.getBlockEntity();
		
		if (entity instanceof BlockEntityPocket) {
			BlockEntityPocket blockEntity = (BlockEntityPocket) entity;
			
			if (blockEntity.getPocket() != null) {
				Pocket pocket = blockEntity.getPocket();
				
				if (this.buttonLock.isMouseOver(mouseX, mouseY)) {
					Component[] comp = new Component[] { ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.lock_info"), 
							ComponentHelper.locComp(ComponentColour.GRAY, false, "dimensionalpocketsii.gui.lock_value").append(pocket.getLockState().getColouredComp()) };
					
					this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
				} else if (this.buttonSideGuide.isMouseOver(mouseX, mouseY)) {
					Component[] comp = new Component[] { ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.pocket.side_guide_info"), 
							ComponentHelper.locComp(ComponentColour.GRAY, false, "dimensionalpocketsii.gui.pocket.side_guide_value").append(blockEntity.getSideGuide().getColouredComp()) };
					
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
				
				else if (this.buttonDown.isMouseOver(mouseX, mouseY)) {
					Component[] comp = new Component[] {
							ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.button.direction.prefix").append(ComponentHelper.locComp(ComponentColour.YELLOW, true, " [", "dimensionalpocketsii.gui.button.direction.down", "]")), 
							ComponentHelper.locComp(ComponentColour.GRAY, false, "dimensionalpocketsii.gui.button.direction.suffix").append(pocket.getSide(Direction.DOWN).getColouredComp()) };
					
					this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
				} else if (this.buttonUp.isMouseOver(mouseX, mouseY)) {
					Component[] comp = new Component[] {
							ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.button.direction.prefix").append(ComponentHelper.locComp(ComponentColour.ORANGE, true, " [", "dimensionalpocketsii.gui.button.direction.up", "]")),
							ComponentHelper.locComp(ComponentColour.GRAY, false, "dimensionalpocketsii.gui.button.direction.suffix").append(pocket.getSide(Direction.UP).getColouredComp()) };
					
					this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
				} else if (this.buttonNorth.isMouseOver(mouseX, mouseY)) {
					Component[] comp = new Component[] {
							ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.button.direction.prefix").append(ComponentHelper.locComp(ComponentColour.BLUE, true, " [", "dimensionalpocketsii.gui.button.direction.north", "]")),
							ComponentHelper.locComp(ComponentColour.GRAY, false, "dimensionalpocketsii.gui.button.direction.suffix").append(pocket.getSide(Direction.NORTH).getColouredComp()) };
					
					this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
				} else if (this.buttonSouth.isMouseOver(mouseX, mouseY)) {
					Component[] comp = new Component[] { 
							ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.button.direction.prefix").append(ComponentHelper.locComp(ComponentColour.LIME, true, " [", "dimensionalpocketsii.gui.button.direction.south", "]")),
							ComponentHelper.locComp(ComponentColour.GRAY, false, "dimensionalpocketsii.gui.button.direction.suffix").append(pocket.getSide(Direction.SOUTH).getColouredComp()) };
					
					this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
				} else if (this.buttonWest.isMouseOver(mouseX, mouseY)) {
					Component[] comp = new Component[] { 
							ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.button.direction.prefix").append(ComponentHelper.locComp(ComponentColour.WHITE, true, " [", "dimensionalpocketsii.gui.button.direction.west", "]")),
							ComponentHelper.locComp(ComponentColour.GRAY, false, "dimensionalpocketsii.gui.button.direction.suffix").append(pocket.getSide(Direction.WEST).getColouredComp()) };
					
					this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
				} else if (this.buttonEast.isMouseOver(mouseX, mouseY)) {
					Component[] comp = new Component[] { 
							ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.button.direction.prefix").append(ComponentHelper.locComp(ComponentColour.GRAY, true, " [", "dimensionalpocketsii.gui.button.direction.east", "]")),
							ComponentHelper.locComp(ComponentColour.GRAY, false, "dimensionalpocketsii.gui.button.direction.suffix").append(pocket.getSide(Direction.EAST).getColouredComp()) };
					
					this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
				}
				
				else if (IS_HOVERING.isHovering(mouseX, mouseY, this.getScreenCoords()[0] + this.fluidBarData[0] - 1, this.getScreenCoords()[0] + this.fluidBarData[0] + 16, this.getScreenCoords()[1] + this.fluidBarData[2] - 27, this.getScreenCoords()[1] + this.fluidBarData[2] + 39)) {
					FluidTank tank = pocket.getFluidTank();
					
					DecimalFormat formatter = new DecimalFormat("#,###,###,###");
					String amount_string = formatter.format(tank.getFluidAmount());
					String capacity_string = formatter.format(tank.getCapacity());
					String fluid_name = tank.getFluid().getTranslationKey();
					
					Component[] comp = new Component[] { 
							ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.fluid_bar.pre").append(ComponentHelper.locComp(ComponentColour.CYAN, true, "[ ", fluid_name, " ]")), 
							ComponentHelper.locComp(ComponentColour.ORANGE, false, amount_string + " / " + capacity_string, "dimensionalpocketsii.gui.fluid_bar.suff") };
					
					this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
				} else if (IS_HOVERING.isHovering(mouseX, mouseY, this.getScreenCoords()[0] + this.energyBarData[0] - 1, this.getScreenCoords()[0] + this.energyBarData[0] + 16, this.getScreenCoords()[1] + this.energyBarData[1] - 1, this.getScreenCoords()[1] + this.energyBarData[1] + 64)) {
					DecimalFormat formatter = new DecimalFormat("#,###,###,###");
					String amount_string = formatter.format(pocket.getEnergyStored());
					String capacity_string = formatter.format(pocket.getMaxEnergyStored());
					
					Component[] comp = new Component[] { 
							ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.gui.energy_bar.pre"),
							ComponentHelper.locComp(ComponentColour.RED, false, amount_string + " / " + capacity_string, "dimensionalpocketsii.gui.energy_bar.suff") };
					
					this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
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
		
		super.renderStandardHoverEffect(poseStack, style, mouseX, mouseY);
	}
	
	public void pushButton(Button button) {
		super.pushButton(button);
		BlockEntity entity = this.getBlockEntity();
		String value = this.textField.getValue();
		
		if (entity instanceof BlockEntityPocket) {
			BlockEntityPocket blockEntity = (BlockEntityPocket) entity;
			
			if (blockEntity.getPocket() != null) {
				Pocket pocket = blockEntity.getPocket();

				if (button.equals(this.buttonLock)) {
					PocketNetworkManager.sendToServer(new PacketLock(this.menu.getBlockPos(), this.menu.getLevel().dimension(), !pocket.getLockStateValue()));
					blockEntity.setLockState(!pocket.getLockStateValue(), true);
					blockEntity.setChanged();
				} else if (button.equals(this.buttonSideGuide)) {
					PocketNetworkManager.sendToServer(new PacketSideGuide(this.menu.getBlockPos(), this.menu.getLevel().dimension()));
					blockEntity.toggleSideGuide();
					blockEntity.setChanged();
				} else if (button.equals(this.buttonTrapPlayers)) {
					PocketNetworkManager.sendToServer(new PacketTrapPlayers(this.menu.getBlockPos(), this.menu.getLevel().dimension(), !pocket.getTrapStateValue()));
					pocket.setTrapState(!pocket.getTrapStateValue());
					blockEntity.setChanged();
				} else if (button.equals(this.buttonHostileSpawn)) {
					PocketNetworkManager.sendToServer(new PacketHostileSpawnState(this.menu.getBlockPos(), this.menu.getLevel().dimension(), !pocket.getHostileSpawnStateValue()));
					pocket.setHostileSpawnState(!pocket.getHostileSpawnStateValue());
					blockEntity.setChanged();
				} else if (button.equals(this.buttonAllowedPlayers)) {
					PocketNetworkManager.sendToServer(new PacketLockToAllowedPlayers(this.menu.getBlockPos(), this.menu.getLevel().dimension(), !pocket.getAllowedPlayerStateValue()));
					pocket.setAllowedPlayerState(!pocket.getAllowedPlayerStateValue());
					blockEntity.setChanged();
				}
				
				else if (button.equals(this.buttonTextClear)) {
					this.textField.setValue("");
				} else if (button.equals(this.buttonTextPlus)) {
					if (!value.isEmpty() && value.length() >= 3) {
						PocketNetworkManager.sendToServer(new PacketAllowedPlayer(this.menu.getBlockPos(), value, true, this.menu.getLevel().dimension()));
						pocket.addAllowedPlayerNBT(value);

						this.clearWidgetList();
						this.textField.setValue("");
					} 
				} else if (button.equals(this.buttonTextMinus)) {
					if (!value.isEmpty()) {
						PocketNetworkManager.sendToServer(new PacketAllowedPlayer(this.menu.getBlockPos(), value, false, this.menu.getLevel().dimension()));
						pocket.removeAllowedPlayerNBT(value);

						this.clearWidgetList();
						this.textField.setValue("");
					} else {
						int selected = this.getSelectedWidgetIndex();
						
						if (selected != 0) {
							String string = this.getWidgetList().get(selected).getDisplayString();
							PocketNetworkManager.sendToServer(new PacketAllowedPlayer(this.menu.getBlockPos(), string, false, this.menu.getLevel().dimension()));
							
							pocket.removeAllowedPlayerNBT(string);
							this.clearWidgetList();
						}
					}
				}

				else if (button.equals(this.buttonTankClear)) {
					if (hasShiftDown()) {
						PocketNetworkManager.sendToServer(new PacketEmptyTank(this.menu.getBlockPos(), this.menu.getLevel().dimension()));
						pocket.emptyFluidTank();
					}
				}
				
				else if (button.equals(this.buttonDown)) {
					PocketNetworkManager.sendToServer(new PacketBlockSideState(this.menu.getBlockPos(), Direction.DOWN, this.menu.getLevel().dimension()));
				} else if (button.equals(this.buttonUp)) {
					PocketNetworkManager.sendToServer(new PacketBlockSideState(this.menu.getBlockPos(), Direction.UP, this.menu.getLevel().dimension()));
				} else if (button.equals(this.buttonNorth)) {
					PocketNetworkManager.sendToServer(new PacketBlockSideState(this.menu.getBlockPos(), Direction.NORTH, this.menu.getLevel().dimension()));
				} else if (button.equals(this.buttonSouth)) {
					PocketNetworkManager.sendToServer(new PacketBlockSideState(this.menu.getBlockPos(), Direction.SOUTH, this.menu.getLevel().dimension()));
				} else if (button.equals(this.buttonWest)) {
					PocketNetworkManager.sendToServer(new PacketBlockSideState(this.menu.getBlockPos(), Direction.WEST, this.menu.getLevel().dimension()));
				} else if (button.equals(this.buttonEast)) {
					PocketNetworkManager.sendToServer(new PacketBlockSideState(this.menu.getBlockPos(), Direction.EAST, this.menu.getLevel().dimension()));
				}
			}
		}
	}

	@Override
	protected void addUIHelpElements() {
		super.addUIHelpElements();

		this.addRenderableUIHelpElement(this.getScreenCoords(), 13, 175, 20, 68, ComponentHelper.locComp(ComponentColour.RED, false, "dimensionalpocketsii.gui.help.power_display"),
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.power_display_one"),
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.power_display_two")
		);

		this.addRenderableUIHelpElement(this.getScreenCoords(), 34, 175, 20, 68, ComponentHelper.locComp(ComponentColour.LIGHT_BLUE, false, "dimensionalpocketsii.gui.help.fluid_display"), 
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.fluid_display_one"),
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.fluid_display_two")
		);

		this.addRenderableUIHelpElement(this.getScreenCoords(), 55, 175, 22, 22, ComponentHelper.locComp(ComponentColour.LIGHT_BLUE, false, "Bucket Input Slot"), ComponentHelper.locComp("DESC"));
		this.addRenderableUIHelpElement(this.getScreenCoords(), 55, 198, 22, 22, ComponentHelper.locComp(ComponentColour.GREEN, false, "Bucket Output Slot"), ComponentHelper.locComp("DESC"));

		this.addRenderableUIHelpElement(this.getScreenCoords(), 55, 221, 22, 22, ComponentHelper.locComp(ComponentColour.RED, false, "dimensionalpocketsii.gui.help.fluid_clear_button"), 
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.fluid_clear_button_one"),
			ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.help.fluid_clear_button_two")
		);

		this.addRenderableUIHelpElement(this.getScreenCoords(), 12, 16, 42, 153, ComponentHelper.locComp(ComponentColour.POCKET_PURPLE_LIGHT, false, "Pocket Surrounding Blocks"), ComponentHelper.locComp("DESC"));
		this.addRenderableUIHelpElement(this.getScreenCoords(), 54, 16, 23, 153, ComponentHelper.locComp(ComponentColour.PURPLE, false, "Pocket Side State Configuration"), ComponentHelper.locComp("DESC"));

		this.addRenderableUIHelpElement(this.getScreenCoords(), 92, 17, 97, 20, ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "Allowed Player Entry"), ComponentHelper.locComp("DESC"));
		this.addRenderableUIHelpElement(this.getScreenCoords(), 190, 17, 20, 20, ComponentHelper.locComp(ComponentColour.RED, false, "Text Clear Button"), ComponentHelper.locComp("DESC"));
		this.addRenderableUIHelpElement(this.getScreenCoords(), 211, 17, 20, 20, ComponentHelper.locComp(ComponentColour.GREEN, false, "Add Allowed Player Button"), ComponentHelper.locComp("DESC"));
		this.addRenderableUIHelpElement(this.getScreenCoords(), 232, 17, 20, 20, ComponentHelper.locComp(ComponentColour.RED, false, "Remove Allowed Player Button"), ComponentHelper.locComp("DESC"));
		
		this.addRenderableUIHelpElement(this.getScreenCoords(), 92, 50, 142, 78, ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "Allowed Player List"), ComponentHelper.locComp("DESC"));
		this.addRenderableUIHelpElement(this.getScreenCoords(), 235, 50, 17, 78, ComponentHelper.locComp(ComponentColour.LIGHT_GRAY, false, "Scroll Bar"), ComponentHelper.locComp("DESC"));
		
		this.addRenderableUIHelpElement(this.getScreenCoords(), 92, 129, 22, 22, ComponentHelper.locComp(ComponentColour.RED, false, "Lock Button"), ComponentHelper.locComp("DESC"));
		this.addRenderableUIHelpElement(this.getScreenCoords(), 115, 129, 22, 22, ComponentHelper.locComp(ComponentColour.YELLOW, false, "Allowed Player Lock Button"), ComponentHelper.locComp("DESC"));
		this.addRenderableUIHelpElement(this.getScreenCoords(), 138, 129, 22, 22, ComponentHelper.locComp(ComponentColour.LIGHT_RED, false, "Trap Players Button"), ComponentHelper.locComp("DESC"));
		this.addRenderableUIHelpElement(this.getScreenCoords(), 161, 129, 22, 22, ComponentHelper.locComp(ComponentColour.TURQUOISE, false, "Connector Mode Button"), ComponentHelper.locComp("DESC"));
		this.addRenderableUIHelpElement(this.getScreenCoords(), 184, 129, 22, 22, ComponentHelper.locComp(ComponentColour.WHITE, false, "Side State Button"), ComponentHelper.locComp("DESC"));
		this.addRenderableUIHelpElement(this.getScreenCoords(), 207, 129, 22, 22, ComponentHelper.locComp(ComponentColour.GREEN, false, "Mob Spawning Button"), ComponentHelper.locComp("DESC"));
		this.addRenderableUIHelpElement(this.getScreenCoords(), 230, 129, 22, 22, ComponentHelper.locComp(ComponentColour.GRAY, false, "Future Setting Button"), ComponentHelper.locComp("DESC"));
		
		this.addRenderableUIHelpElement(this.getScreenCoords(), 264, 15, 74, 182, ComponentHelper.locComp(ComponentColour.GRAY, false, "Pocket Items"), ComponentHelper.locComp("DESC"));
		this.addRenderableUIHelpElement(this.getScreenCoords(), 264, 208, 74, 38, ComponentHelper.locComp(ComponentColour.CYAN, false, "Pocket Buffer Items"), ComponentHelper.locComp("DESC"));
	}
	
	public void initTextField() {
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.textField = new EditBox(this.font, this.getScreenCoords()[0] + this.textFieldI[0], this.getScreenCoords()[1] + this.textFieldI[1], this.textFieldI[2], this.textFieldI[3], ComponentHelper.locComp("Allowed Player Entry"));
		this.textField.setMaxLength(16);
		this.textField.setVisible(true);
		this.textField.setTextColor(CosmosUISystem.DEFAULT_COLOUR_FONT_LIST);
		this.textField.setBordered(false);
		this.textField.setCanLoseFocus(true);
		this.textField.setEditable(true);
		
		this.addWidget(this.textField);
	}

	@Override
	public boolean mouseClicked(double p_97663_, double p_97664_, int p_97665_) {
		return this.textField.mouseClicked(p_97663_, p_97664_, p_97665_) ? true : super.mouseClicked(p_97663_, p_97664_, p_97665_);
	}

	@Override
	public boolean mouseScrolled(double p_97659_, double p_97660_, double p_97661_) {
		return this.textField.mouseScrolled(p_97659_, p_97660_, p_97661_) ? true : super.mouseScrolled(p_97659_, p_97660_, p_97661_);
	}

	@Override
	public boolean keyPressed(int keyCode, int p_97879_, int p_97880_) {
		if (keyCode == 256) {
			this.minecraft.player.closeContainer();
		}
		
		return !this.textField.keyPressed(keyCode, p_97879_, p_97880_) && !this.textField.canConsumeInput() ? super.keyPressed(keyCode, p_97879_, p_97880_) : true;
	}

	@Override
	public boolean charTyped(char charIn, int p_98522_) {
		return !this.textField.charTyped(charIn, p_98522_) && !this.textField.canConsumeInput() ? super.charTyped(charIn, p_98522_) : true;
	}

}