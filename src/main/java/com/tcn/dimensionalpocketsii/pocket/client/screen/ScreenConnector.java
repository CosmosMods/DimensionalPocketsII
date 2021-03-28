package com.tcn.dimensionalpocketsii.pocket.client.screen;

import java.util.Arrays;
import java.util.Objects;

import com.ibm.icu.text.DecimalFormat;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tcn.cosmoslibrary.client.screen.ContainerScreenWithList;
import com.tcn.cosmoslibrary.client.util.ScreenUtil;
import com.tcn.cosmoslibrary.client.util.ScreenUtil.DRAW;
import com.tcn.cosmoslibrary.client.util.ScreenUtil.FONT;
import com.tcn.cosmoslibrary.client.util.ScreenUtil.IS_HOVERING;
import com.tcn.cosmoslibrary.common.comp.CosmosColour;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.dimensionalpocketsii.DimReference.GUI;
import com.tcn.dimensionalpocketsii.DimReference.GUI.RESOURCE;
import com.tcn.dimensionalpocketsii.client.screen.widget.DimensionalButton;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerConnector;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketNetworkManager;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityConnector;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketAllowedPlayer;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketBlockSideState;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketEmptyTank;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketHostileSpawnState;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketLock;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketLockToAllowedPlayers;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketTrapPlayers;
import com.tcn.dimensionalpocketsii.pocket.network.packet.connector.PacketConnectionType;
import com.tcn.dimensionalpocketsii.pocket.network.packet.connector.PacketSideState;

import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.capability.templates.FluidTank;

@SuppressWarnings( { "unused", "deprecation" } )
@OnlyIn(Dist.CLIENT)
public class ScreenConnector extends ContainerScreenWithList<ContainerConnector> {
	
	private int[] SCREEN = new int[] { 256 + 92, + 256 };
	
	private int[] ENERGY_BAR = new int[] { 15, 177 };
	private int[] ENERGY_BAR_ = new int[] { 18, 160, 58, 18 };
	private int[] FLUID_BAR = new int[] { 36, 52, 203, 157, 18, 58 };

	private DimensionalButton buttonTextClear; private int[] TCBI = new int[] { 191, 18, 18 };
	private DimensionalButton buttonTextPlus; private int[] TABI = new int[] { 212, 18, 18 };
	private DimensionalButton buttonTextMinus; private int[] TMBI = new int[] { 233, 18, 18 };
	
	private DimensionalButton buttonTankClear; private int[] TBCI = new int[] { 56, 222, 20 };
	
	private TextFieldWidget TEXT_FIELD; private int[] TEXT_FIELDI = new int[] { 94, 19, 93, 16 };
	
	private int[] ELEMENT_LISTI = new int[] { 94, 52, 138, 0, 72 };
	
	private DimensionalButton buttonNorth; private int[] NBI = new int[] { 56, 70, 18 };
	private DimensionalButton buttonSouth; private int[] SBI = new int[] { 56, 97, 18 };
	private DimensionalButton buttonUp; private int[] UBI = new int[] { 56, 20, 18 };
	private DimensionalButton buttonDown; private int[] DBI = new int[] { 56, 45, 18 };
	private DimensionalButton buttonEast; private int[] EBI = new int[] { 56, 147, 18 };
	private DimensionalButton buttonWest; private int[] WBI = new int[] { 56, 122, 18 };

	private DimensionalButton buttonLock; private int[] LBI = new int[] { 93, 130 };
	private DimensionalButton buttonAllowedPlayers; private int[] APBI = new int[] { 116, 130 };
	private DimensionalButton buttonTrapPlayers; private int[] TPBI = new int[] { 139, 130 };
	private DimensionalButton buttonType; private int[] TYBI = new int[] { 162, 130 };
	private DimensionalButton buttonSideState; private int[] SIBI = new int[] { 185, 130 };
	private DimensionalButton buttonHostileSpawn; private int[] HSBI = new int[] { 208, 130 };
	
	private boolean ignoreTextInput;

	public ScreenConnector(ContainerConnector containerIn, PlayerInventory playerInventoryIn, ITextComponent titleIn) {
		super(containerIn, playerInventoryIn, titleIn, new int[] { 72, 128, 125, 46, 2 }, RESOURCE.GUI_DIMENSIONAL_ELEMENT, 14, 1);

		this.imageWidth = SCREEN[0];
		this.imageHeight = SCREEN[1];
	}

	@Override
	protected void init() {
		this.leftPos = (this.width - this.SCREEN[0]) / 2;
		this.topPos = (this.height - this.SCREEN[1]) / 2;
		
		int[] screen_coords = new int[] { ((this.width - this.imageWidth) / 2), (this.height - this.imageHeight) / 2};
		
		this.initTextField();
		this.drawButtons(screen_coords);
		super.init();
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks){
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		
		int[] screen_coords = new int[] { ((this.width - this.imageWidth) / 2), (this.height - this.imageHeight) / 2};
		this.drawButtons(screen_coords);
		
		ContainerConnector container = this.menu;
		World world = container.getWorld();
		BlockPos pos = container.getBlockPos();
		TileEntity tile = world.getBlockEntity(pos);
		
		if (tile instanceof TileEntityConnector) {
			TileEntityConnector tile_entity_pocket = (TileEntityConnector) tile;
			
			if (tile_entity_pocket.getPocket() != null) {
				Pocket pocket = tile_entity_pocket.getPocket();

				int decimal = pocket.getDisplayColour();
				CosmosColour colour = CosmosColour.col(decimal);
				
				if (colour.isDark()) {
					FONT.drawString(matrixStack, font, screen_coords, 93, 40, "dimensionalpocketsii.gui.header.allowed_players", true, true, ScreenUtil.DEFAULT_COLOUR_FONT_LIST);
				} else {
					FONT.drawString(matrixStack, font, screen_coords, 93, 40, "dimensionalpocketsii.gui.header.allowed_players", true, true, ScreenUtil.DEFAULT_COLOUR_BACKGROUND);
				}
				
				FONT.drawString(matrixStack, font, screen_coords, 88, 4, "dimensionalpocketsii.gui.connector.header", true, true);
				FONT.drawString(matrixStack, font, screen_coords, 8, 4, "dimensionalpocketsii.gui.header.surrounding", true, true, ScreenUtil.DEFAULT_COLOUR_BACKGROUND);
				//FONT.drawString(matrixStack, font, screen_coords, 88, 116, "dimensionalpocketsii.gui.header.config", true, true, CosmosGuiUtil.DEFAULT_COLOUR_BACKGROUND);
				
				FONT.drawString(matrixStack,font, screen_coords, 262, 4, "dimensionalpocketsii.gui.header.pocket_inv", true, true, ScreenUtil.DEFAULT_COLOUR_BACKGROUND);
				FONT.drawInventoryString(matrixStack, font, screen_coords, 88, 157, true);
				
				this.renderComponentHoverEffect(matrixStack, Style.EMPTY, mouseX, mouseY);
				this.renderTooltip(matrixStack, mouseX, mouseY);
			}
		}
	}
	
	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int posX, int posY) {
		int[] screen_coords = new int[] { ((this.width - this.imageWidth) / 2), (this.height - this.imageHeight) / 2};
		
		ContainerConnector container = this.menu;
		World world = container.getWorld();
		BlockPos pos = container.getBlockPos();
		TileEntity tile = world.getBlockEntity(pos);

		DRAW.drawStaticElement(this, matrixStack, screen_coords, 0, 0, 0, 0, 256, 256, GUI.RESOURCE.CONNECTOR_BACKGROUND_NORMAL);
		DRAW.drawStaticElement(this, matrixStack, screen_coords, 256, 0, 0, 0, 92, 256, GUI.RESOURCE.CONNECTOR_BACKGROUND_SIDE);
		
		if (tile instanceof TileEntityConnector) {
			TileEntityConnector connector = (TileEntityConnector) tile;
			
			if (connector.getPocket() != null) {
				Pocket pocket = connector.getPocket();
				
				int decimal = pocket.getDisplayColour();
				CosmosColour colour = CosmosColour.col(decimal);
				float[] rgb = null;
				
				if (colour.equals(CosmosColour.POCKET_PURPLE)) {
					rgb = CosmosColour.rgbFloatArray(CosmosColour.POCKET_PURPLE_LIGHT.dec());
				} else {
					rgb = CosmosColour.rgbFloatArray(decimal);
				}
				
				RenderSystem.color4f(rgb[0], rgb[1], rgb[2], 1.0F);
				DRAW.drawStaticElement(this, matrixStack, screen_coords, 0, 0, 0, 0, 256, 256, GUI.RESOURCE.CONNECTOR_BASE_NORMAL);
				DRAW.drawStaticElement(this, matrixStack, screen_coords, 256, 0, 0, 0, 92, 256, GUI.RESOURCE.CONNECTOR_BASE_SIDE);
				
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				DRAW.drawStaticElement(this, matrixStack, screen_coords, 0, 0, 0, 0, 256, 256, GUI.RESOURCE.CONNECTOR_OVERLAY_NORMAL);
				DRAW.drawStaticElement(this, matrixStack, screen_coords, 256, 0, 0, 0, 92, 256, GUI.RESOURCE.CONNECTOR_OVERLAY_SIDE);

				DRAW.drawFluidTank(this, matrixStack, screen_coords, this.FLUID_BAR[0], this.FLUID_BAR[2], pocket.getFluidTank(), pocket.getFluidLevelScaled(64));
				
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				DRAW.drawPowerBar(this, matrixStack, screen_coords, this.ENERGY_BAR[0], this.ENERGY_BAR[1], pocket.getStoredLevelScaled(64), new int[] { 16, 255, 64, 16 }, pocket.hasStored());
			}
		}
		
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.TEXT_FIELD.render(matrixStack, posX + 30, posY, partialTicks);
		this.drawScrollElement(matrixStack, screen_coords, 237, 52, true, 2, this.scrollEnabled());
	}
	
	@Override
	protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) { }
	
	private void drawButtons(int[] screen_coords) {
		this.buttons.clear();
		
		ContainerConnector container = this.menu;
		World world = container.getWorld();
		BlockPos pos = container.getBlockPos();
		TileEntity tile = world.getBlockEntity(pos);

		if (tile instanceof TileEntityConnector) {
			TileEntityConnector connector = (TileEntityConnector) tile;
			if (connector.getPocket() != null) {
				Pocket pocket = connector.getPocket();
				
				int[] sides = new int[] { 0, 0, 0, 0, 0, 0 };
				for(Direction c : Direction.values()) {
					int i = c.get3DDataValue();
					sides[i] = (pocket.getSideArray()[i].getIndex() + 6);
				}
				
				this.buttonDown  = this.addButton(new DimensionalButton(screen_coords[0] + DBI[0], screen_coords[1] + DBI[1], 18, true, true, sides[0], CosmosCompHelper.locComp(""), (button) -> { this.pushButton(this.buttonDown);  }));
				this.buttonUp    = this.addButton(new DimensionalButton(screen_coords[0] + UBI[0], screen_coords[1] + UBI[1], 18, true, true, sides[1], CosmosCompHelper.locComp(""), (button) -> { this.pushButton(this.buttonUp);    }));
				this.buttonNorth = this.addButton(new DimensionalButton(screen_coords[0] + NBI[0], screen_coords[1] + NBI[1], 18, true, true, sides[2], CosmosCompHelper.locComp(""), (button) -> { this.pushButton(this.buttonNorth); }));
				this.buttonSouth = this.addButton(new DimensionalButton(screen_coords[0] + SBI[0], screen_coords[1] + SBI[1], 18, true, true, sides[3], CosmosCompHelper.locComp(""), (button) -> { this.pushButton(this.buttonSouth); }));
				this.buttonWest  = this.addButton(new DimensionalButton(screen_coords[0] + WBI[0], screen_coords[1] + WBI[1], 18, true, true, sides[4], CosmosCompHelper.locComp(""), (button) -> { this.pushButton(this.buttonWest);  }));
				this.buttonEast  = this.addButton(new DimensionalButton(screen_coords[0] + EBI[0], screen_coords[1] + EBI[1], 18, true, true, sides[5], CosmosCompHelper.locComp(""), (button) -> { this.pushButton(this.buttonEast);  }));

				this.buttonTankClear = this.addButton(new DimensionalButton(screen_coords[0] + TBCI[0], screen_coords[1] + TBCI[1], 20, !pocket.getFluidTank().isEmpty(), true, pocket.getFluidTank().isEmpty() ? 18 : 19, CosmosCompHelper.locComp(""), (button) -> { this.pushButton(this.buttonTankClear); }));
				this.buttonLock = this.addButton(new DimensionalButton(screen_coords[0] + LBI[0], screen_coords[1] + LBI[1], 20, true, true, pocket.getLockState().getIndex() + 4, CosmosCompHelper.locComp(""), (button) -> { this.pushButton(this.buttonLock); }));
				this.buttonAllowedPlayers = this.addButton(new DimensionalButton(screen_coords[0] + APBI[0], screen_coords[1] + APBI[1], 20, true, true, pocket.getAllowedPlayerState().getIndex() + 27, CosmosCompHelper.locComp(""), (button) -> { this.pushButton(this.buttonAllowedPlayers); }));
				this.buttonSideState = this.addButton(new DimensionalButton(screen_coords[0] + SIBI[0], screen_coords[1] + SIBI[1], 20, true, true, (connector.getSide(Direction.UP).getIndex() + 14), CosmosCompHelper.locComp(""), (button) -> { this.pushButton(this.buttonSideState); }));
				this.buttonType = this.addButton(new DimensionalButton(screen_coords[0] + TYBI[0], screen_coords[1] + TYBI[1], 20, true, true, (connector.getConnectionType().getIndex() + 10), CosmosCompHelper.locComp(""), (button) -> { this.pushButton(this.buttonType); }));
				
				this.buttonTrapPlayers = this.addButton(new DimensionalButton(screen_coords[0] + TPBI[0], screen_coords[1] + TPBI[1], 20, true, true, pocket.getTrapState().getIndex() + 23, CosmosCompHelper.locComp(""), (button) -> { this.pushButton(this.buttonTrapPlayers); }));
				this.buttonHostileSpawn = this.addButton(new DimensionalButton(screen_coords[0] + HSBI[0], screen_coords[1] + HSBI[1], 20, true, true, pocket.getHostileSpawnState().getIndex() + 25, CosmosCompHelper.locComp(""), (button) -> { this.pushButton(this.buttonHostileSpawn); }));
				this.buttonTextClear = this.addButton(new DimensionalButton(screen_coords[0] + TCBI[0], screen_coords[1] + TCBI[1], 18, !(this.TEXT_FIELD.getValue().isEmpty()), true, 3, CosmosCompHelper.locComp(""), (button) -> { this.pushButton(this.buttonTextClear); }));
				this.buttonTextPlus = this.addButton(new DimensionalButton(screen_coords[0] + TABI[0], screen_coords[1] + TABI[1], 18, !(this.TEXT_FIELD.getValue().isEmpty()), true, 1, CosmosCompHelper.locComp(""), (button) -> { this.pushButton(this.buttonTextPlus); }));
				this.buttonTextMinus = this.addButton(new DimensionalButton(screen_coords[0] + TMBI[0], screen_coords[1] + TMBI[1], 18, true, true, 2, CosmosCompHelper.locComp(""), (button) -> { this.pushButton(this.buttonTextMinus); }));
				
				this.drawListWithElementsSmall(font, ELEMENT_LISTI[0], ELEMENT_LISTI[1], ELEMENT_LISTI[2], pocket.getAllowedPlayersArray());
			}
		}
	}
	
	@Override
	public void renderComponentHoverEffect(MatrixStack matrixStack, Style style, int mouseX, int mouseY) {
		int[] screen_coords = new int[] { ((this.width - this.SCREEN[0]) / 2), (this.height - this.SCREEN[1]) / 2};
		ContainerConnector container = this.menu;
		World world = container.getWorld();
		BlockPos pos = container.getBlockPos();
		TileEntity tile = world.getBlockEntity(pos);

		if (tile instanceof TileEntityConnector) {
			TileEntityConnector connector = (TileEntityConnector) tile;
			if (connector.getPocket() != null) {
				Pocket pocket = connector.getPocket();
				if (pocket != null) {
					if (this.buttonLock.isMouseOver(mouseX, mouseY)) {
						IFormattableTextComponent[] comp = new IFormattableTextComponent[] { CosmosCompHelper.locComp(CosmosColour.WHITE, false, "dimensionalpocketsii.gui.lock_info"), 
								CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.gui.lock_value").append(pocket.getLockState().getColouredComp()) };
						
						this.renderComponentTooltip(matrixStack, Arrays.asList(comp), mouseX, mouseY);
					} else if (this.buttonType.isMouseOver(mouseX, mouseY)) {
						IFormattableTextComponent[] comp = new IFormattableTextComponent[] { CosmosCompHelper.locComp(CosmosColour.WHITE, false, "dimensionalpocketsii.gui.connector.connection_info"), 
								CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.gui.connector.connection_value").append(connector.getConnectionType().getColouredComp()) };
						
						this.renderComponentTooltip(matrixStack, Arrays.asList(comp), mouseX, mouseY);
					} else if (this.buttonSideState.isMouseOver(mouseX, mouseY)) {
						IFormattableTextComponent[] comp = new IFormattableTextComponent[] { CosmosCompHelper.locComp(CosmosColour.WHITE, false, "dimensionalpocketsii.gui.connector.side_state_info"), 
								CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.gui.connector.side_state_value").append(connector.getSide(Direction.UP).getColouredComp()) };
						
						this.renderComponentTooltip(matrixStack, Arrays.asList(comp), mouseX, mouseY);
					} else if (this.buttonTrapPlayers.isMouseOver(mouseX, mouseY)) {
						IFormattableTextComponent[] comp = new IFormattableTextComponent[] { CosmosCompHelper.locComp(CosmosColour.WHITE, false, "dimensionalpocketsii.gui.trap_players_info"), 
								CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.gui.trap_players_value").append(pocket.getTrapState().getColouredComp()) };
						
						this.renderComponentTooltip(matrixStack, Arrays.asList(comp), mouseX, mouseY);
					} else if (this.buttonHostileSpawn.isMouseOver(mouseX, mouseY)) {
						IFormattableTextComponent[] comp = new IFormattableTextComponent[] { CosmosCompHelper.locComp(CosmosColour.WHITE, false, "dimensionalpocketsii.gui.hostile_spawn_info"), 
								CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.gui.hostile_spawn_value").append(pocket.getHostileSpawnState().getColouredComp()) };
						
						this.renderComponentTooltip(matrixStack, Arrays.asList(comp), mouseX, mouseY);
					} else if (this.buttonAllowedPlayers.isMouseOver(mouseX, mouseY)) {
						IFormattableTextComponent[] comp = new IFormattableTextComponent[] { CosmosCompHelper.locComp(CosmosColour.WHITE, false, "dimensionalpocketsii.gui.allowed_player_info"), 
								CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.gui.allowed_player_value").append(pocket.getAllowedPlayerState().getColouredComp()) };
						
						this.renderComponentTooltip(matrixStack, Arrays.asList(comp), mouseX, mouseY);
					}
					
					else if (this.buttonTextClear.isMouseOver(mouseX, mouseY)) {
						if (this.buttonTextClear.active) {
							this.renderTooltip(matrixStack, CosmosCompHelper.locComp(CosmosColour.LIGHT_GRAY, false, "dimensionalpocketsii.gui.button.text.clear"),  mouseX, mouseY);
						}
					} else if (this.buttonTextPlus.isMouseOver(mouseX, mouseY)) {
						if (this.buttonTextPlus.active) {
							this.renderTooltip(matrixStack, CosmosCompHelper.locComp(CosmosColour.GREEN, false, "dimensionalpocketsii.gui.button.text.plus"), mouseX, mouseY);
						}
					} else if (this.buttonTextMinus.isMouseOver(mouseX, mouseY)) {
						if (this.buttonTextMinus.active) {
							this.renderTooltip(matrixStack, CosmosCompHelper.locComp(CosmosColour.RED, false, "dimensionalpocketsii.gui.button.text.minus"), mouseX, mouseY);
						}
					} 
					
					else if (this.buttonDown.isMouseOver(mouseX, mouseY)) {
						IFormattableTextComponent[] comp = new IFormattableTextComponent[] { 
								CosmosCompHelper.locComp(CosmosColour.WHITE, false, "dimensionalpocketsii.gui.button.direction.prefix").append(CosmosCompHelper.locComp(CosmosColour.YELLOW, true, " [", "dimensionalpocketsii.gui.button.direction.down", "]")), 
								CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.gui.button.direction.suffix").append(pocket.getSide(Direction.DOWN).getColouredComp()) };
						
						this.renderComponentTooltip(matrixStack, Arrays.asList(comp), mouseX, mouseY);
					} else if (this.buttonUp.isMouseOver(mouseX, mouseY)) {
						IFormattableTextComponent[] comp = new IFormattableTextComponent[] { 
								CosmosCompHelper.locComp(CosmosColour.WHITE, false, "dimensionalpocketsii.gui.button.direction.prefix").append(CosmosCompHelper.locComp(CosmosColour.ORANGE, true, " [", "dimensionalpocketsii.gui.button.direction.up", "]")),
								CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.gui.button.direction.suffix").append(pocket.getSide(Direction.UP).getColouredComp()) };
						
						this.renderComponentTooltip(matrixStack, Arrays.asList(comp), mouseX, mouseY);
					} else if (this.buttonNorth.isMouseOver(mouseX, mouseY)) {
						IFormattableTextComponent[] comp = new IFormattableTextComponent[] {
								CosmosCompHelper.locComp(CosmosColour.WHITE, false, "dimensionalpocketsii.gui.button.direction.prefix").append(CosmosCompHelper.locComp(CosmosColour.BLUE, true, " [", "dimensionalpocketsii.gui.button.direction.north", "]")),
								CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.gui.button.direction.suffix").append(pocket.getSide(Direction.NORTH).getColouredComp()) };
						
						this.renderComponentTooltip(matrixStack, Arrays.asList(comp), mouseX, mouseY);
					} else if (this.buttonSouth.isMouseOver(mouseX, mouseY)) {
						IFormattableTextComponent[] comp = new IFormattableTextComponent[] { 
								CosmosCompHelper.locComp(CosmosColour.WHITE, false, "dimensionalpocketsii.gui.button.direction.prefix").append(CosmosCompHelper.locComp(CosmosColour.LIME, true, " [", "dimensionalpocketsii.gui.button.direction.south", "]")),
								CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.gui.button.direction.suffix").append(pocket.getSide(Direction.SOUTH).getColouredComp()) };
						
						this.renderComponentTooltip(matrixStack, Arrays.asList(comp), mouseX, mouseY);
					} else if (this.buttonWest.isMouseOver(mouseX, mouseY)) {
						IFormattableTextComponent[] comp = new IFormattableTextComponent[] { 
								CosmosCompHelper.locComp(CosmosColour.WHITE, false, "dimensionalpocketsii.gui.button.direction.prefix").append(CosmosCompHelper.locComp(CosmosColour.WHITE, true, " [", "dimensionalpocketsii.gui.button.direction.west", "]")),
								CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.gui.button.direction.suffix").append(pocket.getSide(Direction.WEST).getColouredComp()) };
						
						this.renderComponentTooltip(matrixStack, Arrays.asList(comp), mouseX, mouseY);
					} else if (this.buttonEast.isMouseOver(mouseX, mouseY)) {
						IFormattableTextComponent[] comp = new IFormattableTextComponent[] { 
								CosmosCompHelper.locComp(CosmosColour.WHITE, false, "dimensionalpocketsii.gui.button.direction.prefix").append(CosmosCompHelper.locComp(CosmosColour.GRAY, true, " [", "dimensionalpocketsii.gui.button.direction.east", "]")),
								CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.gui.button.direction.suffix").append(pocket.getSide(Direction.EAST).getColouredComp()) };
						
						this.renderComponentTooltip(matrixStack, Arrays.asList(comp), mouseX, mouseY);
					}
					
					else if (IS_HOVERING.isHovering(mouseX, mouseY, screen_coords[0] + this.FLUID_BAR[0] - 1, screen_coords[0] + this.FLUID_BAR[0] + 16, screen_coords[1] + this.FLUID_BAR[2] - 27, screen_coords[1] + this.FLUID_BAR[2] + 39)) {
						FluidTank tank = pocket.getFluidTank();
						
						DecimalFormat formatter = new DecimalFormat("#,###,###,###");
						String amount_string = formatter.format(tank.getFluidAmount());
						String capacity_string = formatter.format(tank.getCapacity());
						String fluid_name = tank.getFluid().getTranslationKey();
						
						IFormattableTextComponent[] comp = new IFormattableTextComponent[] { 
								CosmosCompHelper.locComp(CosmosColour.WHITE, false, "dimensionalpocketsii.gui.fluid_bar.pre").append(CosmosCompHelper.locComp(CosmosColour.CYAN, true, "[ ", fluid_name, " ]")), 
								CosmosCompHelper.locComp(CosmosColour.ORANGE, false, amount_string + " / " + capacity_string, "dimensionalpocketsii.gui.fluid_bar.suff") };
						
						this.renderComponentTooltip(matrixStack, Arrays.asList(comp), mouseX, mouseY);
					} else if (IS_HOVERING.isHovering(mouseX, mouseY, screen_coords[0] + this.ENERGY_BAR[0] - 1, screen_coords[0] + this.ENERGY_BAR[0] + 16, screen_coords[1] + this.ENERGY_BAR[1] - 1, screen_coords[1] + this.ENERGY_BAR[1] + 64)) {
						FluidTank tank = pocket.getFluidTank();
						
						DecimalFormat formatter = new DecimalFormat("#,###,###,###");
						String amount_string = formatter.format(pocket.getEnergyStored());
						String capacity_string = formatter.format(pocket.getMaxEnergyStored());
						
						IFormattableTextComponent[] comp = new IFormattableTextComponent[] { 
								CosmosCompHelper.locComp(CosmosColour.WHITE, false, "dimensionalpocketsii.gui.energy_bar.pre"),
								CosmosCompHelper.locComp(CosmosColour.RED, false, amount_string + " / " + capacity_string, "dimensionalpocketsii.gui.energy_bar.suff") };
						
						this.renderComponentTooltip(matrixStack, Arrays.asList(comp), mouseX, mouseY);
					}
					
					else if (this.buttonTankClear != null) {
						if (this.buttonTankClear.isMouseOver(mouseX, mouseY)) {
							if (this.buttonTankClear.active) {
								if (!hasShiftDown()) {
									this.renderTooltip(matrixStack, CosmosCompHelper.locComp(CosmosColour.WHITE, false, "dimensionalpocketsii.gui.button.tank_clear"), mouseX, mouseY);
								} else {
									IFormattableTextComponent[] comp = new IFormattableTextComponent[] { 
											CosmosCompHelper.locComp(CosmosColour.WHITE, false, "dimensionalpocketsii.gui.button.tank_clear"),
											CosmosCompHelper.locComp(CosmosColour.RED, true, "dimensionalpocketsii.gui.button.tank_clear_shift") };
									
									this.renderComponentTooltip(matrixStack, Arrays.asList(comp), mouseX, mouseY);
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void pushButton(Button button) {
		ContainerConnector container = this.menu;
		World world = container.getWorld();
		BlockPos pos = container.getBlockPos();
		TileEntity tile = world.getBlockEntity(pos);
		
		String value = this.TEXT_FIELD.getValue();
		
		if (tile instanceof TileEntityConnector) {
			TileEntityConnector connector = (TileEntityConnector) tile;
			if (connector.getPocket() != null) {
				Pocket pocket = connector.getPocket();

				if (button.equals(this.buttonType)) {
					PocketNetworkManager.sendToServer(new PacketConnectionType(pos));
					connector.cycleConnectionType(true);
					connector.setChanged();;
				} else if (button.equals(this.buttonLock)) {
					PocketNetworkManager.sendToServer(new PacketLock(pos, world.dimension(), !pocket.getLockStateValue()));
					connector.setLockState(!pocket.getLockStateValue(), true);
					connector.setChanged();
				} else if (button.equals(this.buttonSideState)) {
					PocketNetworkManager.sendToServer(new PacketSideState(pos));
					connector.cycleSide(Direction.UP, true);
					connector.setChanged();
				} else if (button.equals(this.buttonTrapPlayers)) {
					PocketNetworkManager.sendToServer(new PacketTrapPlayers(pos, world.dimension(), !pocket.getTrapStateValue()));
					pocket.setTrapState(!pocket.getTrapStateValue());
					connector.setChanged();
				} else if (button.equals(this.buttonHostileSpawn)) {
					PocketNetworkManager.sendToServer(new PacketHostileSpawnState(pos, world.dimension(), !pocket.getHostileSpawnStateValue()));
					pocket.setHostileSpawnState(!pocket.getHostileSpawnStateValue());
					connector.setChanged();
				} else if (button.equals(this.buttonAllowedPlayers)) {
					PocketNetworkManager.sendToServer(new PacketLockToAllowedPlayers(pos, world.dimension(), !pocket.getAllowedPlayerStateValue()));
					pocket.setAllowedPlayerState(!pocket.getAllowedPlayerStateValue());
					connector.setChanged();
					
					//this.drawListWithElementsSmall(font, ELEMENT_LISTI[0], ELEMENT_LISTI[1], ELEMENT_LISTI[2], pocket.getAllowedPlayersArray());
				}
				
				else if (button.equals(this.buttonTextClear)) {
					this.TEXT_FIELD.setValue("");
				} else if (button.equals(this.buttonTextPlus)) {
					if (!value.isEmpty()) {
						PocketNetworkManager.sendToServer(new PacketAllowedPlayer(pos, value, true, world.dimension()));
						pocket.addAllowedPlayerNBT(value);
						
						this.widget_list_array.clear();
						this.TEXT_FIELD.setValue("");
					} 
				} else if (button.equals(this.buttonTextMinus)) {
					if (!value.isEmpty()) {
						PocketNetworkManager.sendToServer(new PacketAllowedPlayer(pos, value, false, world.dimension()));
						pocket.removeAllowedPlayerNBT(value);
						
						this.widget_list_array.clear();
						this.TEXT_FIELD.setValue("");
					} else {
						int selected = this.getCurrentlySelectedInt();
						
						if (selected != 0) {
							String string = this.widget_list_array.get(selected).displayString;
							PocketNetworkManager.sendToServer(new PacketAllowedPlayer(pos, string, false, world.dimension()));
							
							pocket.removeAllowedPlayerNBT(string);
							this.widget_list_array.clear();
						}
					}
				}
				
				else if (button.equals(this.buttonTankClear)) {
					if (hasShiftDown()) {
						PocketNetworkManager.sendToServer(new PacketEmptyTank(pos, world.dimension()));
						pocket.emptyFluidTank();
					}
				}
				
				else if (button.equals(this.buttonDown)) {
					PocketNetworkManager.sendToServer(new PacketBlockSideState(pos, Direction.DOWN, world.dimension()));
					pocket.cycleSide(Direction.DOWN, true);
				} else if (button.equals(this.buttonUp)) {
					PocketNetworkManager.sendToServer(new PacketBlockSideState(pos, Direction.UP, world.dimension()));
					pocket.cycleSide(Direction.UP, true);
				} else if (button.equals(this.buttonNorth)) {
					PocketNetworkManager.sendToServer(new PacketBlockSideState(pos, Direction.NORTH, world.dimension()));
					pocket.cycleSide(Direction.NORTH, true);
				} else if (button.equals(this.buttonSouth)) {
					PocketNetworkManager.sendToServer(new PacketBlockSideState(pos, Direction.SOUTH, world.dimension()));
					pocket.cycleSide(Direction.SOUTH, true);
				} else if (button.equals(this.buttonWest)) {
					PocketNetworkManager.sendToServer(new PacketBlockSideState(pos, Direction.WEST, world.dimension()));
					pocket.cycleSide(Direction.WEST, true);
				} else if (button.equals(this.buttonEast)) {
					PocketNetworkManager.sendToServer(new PacketBlockSideState(pos, Direction.EAST, world.dimension()));
					pocket.cycleSide(Direction.EAST, true);
				}
			}
		}
	}
	
	public void initTextField() {
		int[] screen_coords = new int[] { ((this.width - this.SCREEN[0]) / 2), (this.height - this.SCREEN[1]) / 2};
		
        this.TEXT_FIELD = new TextFieldWidget(this.font, screen_coords[0] + this.TEXT_FIELDI[0], screen_coords[1] + this.TEXT_FIELDI[1], this.TEXT_FIELDI[2], this.TEXT_FIELDI[3], CosmosCompHelper.locComp("Allowed Player Entry"));

		this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
		this.TEXT_FIELD.setMaxLength(20);
		this.TEXT_FIELD.setVisible(true);
        this.TEXT_FIELD.setTextColor(ScreenUtil.DEFAULT_COLOUR_FONT_LIST);
        this.TEXT_FIELD.setBordered(true);
        this.TEXT_FIELD.setCanLoseFocus(true);
        
        this.children.add(this.TEXT_FIELD);
	}

	public boolean charTyped(char charIn, int p_231042_2_) {
		if (this.ignoreTextInput) {
			return false;
		} else {
			String s = this.TEXT_FIELD.getValue();
			if (this.TEXT_FIELD.charTyped(charIn, p_231042_2_)) {
				if (!Objects.equals(s, this.TEXT_FIELD.getValue())) {
					System.out.println(charIn);
				}

				return true;
			} else {
				return false;
			}
		}
	}

	public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
		this.ignoreTextInput = false;
		
		boolean flag1 = InputMappings.getKey(p_231046_1_, p_231046_2_).getNumericKeyValue().isPresent();
		if (flag1 && this.checkHotbarKeyPressed(p_231046_1_, p_231046_2_)) {
			this.ignoreTextInput = true;
			return true;
		} else {
			String s = this.TEXT_FIELD.getValue();
			if (this.TEXT_FIELD.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_)) {
				if (!Objects.equals(s, this.TEXT_FIELD.getValue())) {
					//this.refreshSearchResults();
				}

				return true;
			} else {
				return this.TEXT_FIELD.isFocused() && this.TEXT_FIELD.isVisible() && p_231046_1_ != 256 ? true : super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
			}
		}
	}

	public boolean mouseScrolled(double mouseX, double mouseY, double direction) {
		return super.mouseScrolled(mouseX, mouseY, direction);
	}
}