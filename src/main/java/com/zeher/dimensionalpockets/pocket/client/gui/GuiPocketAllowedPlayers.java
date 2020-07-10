package com.zeher.dimensionalpockets.pocket.client.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.zeher.dimensionalpockets.core.handler.NetworkHandler;
import com.zeher.dimensionalpockets.core.reference.DimReference;
import com.zeher.dimensionalpockets.pocket.client.container.ContainerDimensionalPocket;
import com.zeher.dimensionalpockets.pocket.tileentity.TileEntityDimensionalPocket;
import com.zeher.zeherlib.ZLReference;
import com.zeher.zeherlib.api.client.container.GuiContainerElements;
import com.zeher.zeherlib.api.client.gui.button.GuiFluidButton;
import com.zeher.zeherlib.api.client.gui.button.GuiIconButton;
import com.zeher.zeherlib.api.client.gui.element.GuiListElement;
import com.zeher.zeherlib.api.client.util.TextHelper;
import com.zeher.zeherlib.mod.util.ModGuiUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import scala.actors.threadpool.Arrays;

public class GuiPocketAllowedPlayers extends GuiContainerElements {
	
	private GuiIconButton plus_button;
	private GuiIconButton minus_button;
	private GuiIconButton text_button;
	
	private GuiFluidButton tank_button_empty;
	private GuiFluidButton tank_button_clear;
	
	private GuiTextField text_field;
	
	private GuiDimensionalButton north_button;
	private GuiDimensionalButton south_button;
	private GuiDimensionalButton updir_button;
	private GuiDimensionalButton down_button;
	private GuiDimensionalButton east_button;
	private GuiDimensionalButton west_button;
	
	private GuiDimensionalButton lock_button;
	
	private TileEntityDimensionalPocket tile;
	
	public int current_scroll;
	private int max_scroll = 1560;
	
	private boolean info_screen = true;
	
	private int[] SCREEN = new int[] { 256 + 95, 256 };

	private int[] power_bar = new int[] { 88, 18 };
	private int[] fluid_bar = new int[] { 88, 105, 89, 150 };
	
	private int[] NORTH = new int[] { 35, 40 };
	private int[] SOUTH = new int[] { 59, 64 };
	private int[] UPDIR = new int[] { 35, 16 };
	private int[] DOWN = new int[] { 35, 64 };
	private int[] EAST = new int[] { 11, 40 };
	private int[] WEST = new int[] { 59, 40 };
	
	private int[] LOCK = new int[] { 11, 64 };
	private int[] FLUID = new int[] { 61, 148 , 18, 18 };
	
	public GuiPocketAllowedPlayers(InventoryPlayer inventoryPlayer, TileEntityDimensionalPocket tileEntity) {
		super(new ContainerDimensionalPocket(inventoryPlayer, tileEntity));
		this.tile = tileEntity;
				
		this.xSize = SCREEN[0];
		this.ySize = SCREEN[1];
	}
	
	@Override
	public void initGui () {
		int[] screen_coords = new int[] { ((this.width - this.xSize) / 2), (this.height - this.ySize) / 2 };

        Keyboard.enableRepeatEvents(true);
        
		this.text_field = new GuiTextField(2, fontRenderer, 116, 17, 116, 16);
		this.text_field.setMaxStringLength(20);
		this.text_field.setVisible(true);
        this.text_field.setTextColor(ModGuiUtil.DEFAULT_COLOUR_FONT_LIST);
        this.text_field.setEnableBackgroundDrawing(true);
        this.text_field.setCanLoseFocus(true);
        
        super.initGui();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouse_x, int mouse_y) {
		int[] screen_coords = new int[] { ((this.width - this.xSize) / 2), (this.height - this.ySize) / 2 };

		ModGuiUtil.ELEMENT.LIST.DRAW.drawListWithElementsSmall(this, fontRenderer, 116, 73, 116, 0, DimReference.GUI.RESOURCE.GUI_DIMENSIONAL_ELEMENT, tile.getPocket().getAllowedPlayers());
		
		this.drawStrings(screen_coords);
		this.text_field.drawTextBox();
		this.drawButtons(screen_coords);
		super.drawGuiContainerForegroundLayer(mouse_x, mouse_y);
		this.drawHoveringText(mouse_x, mouse_y, screen_coords);
		this.renderHoveredToolTip(mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		int[] screen_coords = new int[] { ((this.width - this.xSize) / 2), (this.height - this.ySize) / 2};

		ModGuiUtil.DRAW.bindTexture(this, DimReference.GUI.RESOURCE.GUI_ALLOWED_PLAYERS);
		ModGuiUtil.DRAW.drawStaticElement(this, screen_coords, 0, 0, 0, 0, 256, 256);
		ModGuiUtil.ELEMENT.SCROLL.DRAW.drawScrollElement(this, screen_coords, 239, 73, this.current_scroll, true, 1, false);
		
		ModGuiUtil.DRAW.drawPowerBar(this, screen_coords, power_bar[0], power_bar[1], this.tile.getEnergyScaled(ZLReference.RESOURCE.INFO.ENERGY_BAR[2]), ZLReference.RESOURCE.INFO.ENERGY_BAR, this.tile.hasEnergy());
		//ModGuiUtil.DRAW.drawFluidTank(this, screen_coords, fluid_bar[0], fluid_bar[1], this.tile.getTank(), this.tile.getFluidLevelScaled(38));
		
		ModGuiUtil.DRAW.bindTexture(this, DimReference.GUI.RESOURCE.GUI_ALLOWED_PLAYERS_INFO);
		ModGuiUtil.DRAW.drawStaticElement(this, screen_coords, 256, 0, 0, 0, 95, 256);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.equals(this.plus_button)) {
			this.tile.getPocket().addAllowedPlayer(text_field.getText());
			this.elementlist_array.clear();
			
			this.tile.markDirty();
		} else if (button.equals(this.minus_button)) {
			ArrayList<String> allowed_players = tile.getPocket().getAllowedPlayers();
			this.tile.getPocket().removeAllowedPlayer(allowed_players.get(this.getCurrentlySelectedInt()));
			this.elementlist_array.clear();
			
			this.tile.markDirty();
		} else if (button.equals(this.text_button)) {
			this.text_field.setText("");
		} 
		
		else if (button.equals(this.lock_button)) {
			boolean lock = !tile.getLockState();
			tile.setLockState(lock);
			NetworkHandler.sendLockPacket(tile.getPos(), lock);
			tile.sendUpdates();
		}
		
		else if (button.equals(this.tank_button_clear)) {
			if (this.isShiftKeyDown()) {
				//NetworkHandler.sendPoweredFluidCrafterPacket(this.tile.getPos(), "one", "empty", this.tile);
			}
		} 
	}
	
	@Override
	protected void actionPerformed(GuiListElement element) { }
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		//this.renderHoveredToolTip(mouseX, mouseY);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (!this.checkHotbarKeys(keyCode)) {
			if (this.text_field.isFocused()) {
				if (keyCode == 1) {
					this.text_field.setFocused(false);
					this.text_field.setText("");
				} else if (keyCode == 28) {
					tile.getPocket().addAllowedPlayer(text_field.getText());
					this.elementlist_array.clear();
					tile.markDirty();
				} else {
					this.text_field.textboxKeyTyped(typedChar, keyCode);
				}
			} else {
				super.keyTyped(typedChar, keyCode);
			}
		}
	}

	@Override
	protected void mouseClicked(int mouse_x, int mouse_y, int mouse_button) throws IOException {
		int[] screen_coords = new int[] { (this.width - this.xSize) / 2, (this.height - this.ySize) / 2 };
		
		int weighted_x = screen_coords[0] + this.text_field.x;
		int weighted_y = screen_coords[1] + this.text_field.y;
		
		if (mouse_button == 0) {
			if (ModGuiUtil.IS_HOVERING.isHovering(mouse_x, mouse_y, weighted_x, weighted_x + this.text_field.width, weighted_y, weighted_y + this.text_field.height)) {				
				this.text_field.setFocused(true);
			} else {
				this.text_field.setFocused(false);
			}
		}
		
		super.mouseClicked(mouse_x, mouse_y, mouse_button);
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        
    	this.current_scroll = (this.current_scroll - i);
    	this.current_scroll = MathHelper.clamp(this.current_scroll, 0, this.max_scroll);
    }
	
	@Override
	public void onResize(Minecraft mc, int width, int height) {
		this.elementlist_array.clear();
		
		super.onResize(mc, width, height);
	}
	
	private void drawButtons(int[] screen_coords) {
		this.buttonList.clear();
		this.text_button = this.addButton(new GuiIconButton(0, screen_coords[0] + 235, screen_coords[1] + 16, 18, 3, !(this.text_field.getText().isEmpty())));
		this.plus_button = this.addButton(new GuiIconButton(1, screen_coords[0] + 215, screen_coords[1] + 44, 18, 1, true));
		this.minus_button = this.addButton(new GuiIconButton(2, screen_coords[0] + 235, screen_coords[1] + 44, 18, 2, true));
		
		int[] sides = new int[] { 0, 0, 0, 0, 0, 0 };
		
		for(EnumFacing c : EnumFacing.VALUES) {
			sides[c.getIndex()] = tile.getSide(c).getIndex();
		}
		
		this.down_button = this.addButton(new GuiDimensionalButton(6, screen_coords[0] + DOWN[0], screen_coords[1] + DOWN[1], 20, sides[0], true, true));
		this.updir_button = this.addButton(new GuiDimensionalButton(5, screen_coords[0] + UPDIR[0], screen_coords[1] + UPDIR[1], 20, sides[1], true, true));
		this.north_button = this.addButton(new GuiDimensionalButton(3, screen_coords[0] + NORTH[0], screen_coords[1] + NORTH[1], 20, sides[2], true, true));
		this.south_button = this.addButton(new GuiDimensionalButton(4, screen_coords[0] + SOUTH[0], screen_coords[1] + SOUTH[1], 20, sides[3], true, true));
		this.west_button = this.addButton(new GuiDimensionalButton(8, screen_coords[0] + WEST[0], screen_coords[1] + WEST[1], 20, sides[4], true, true));
		this.east_button = this.addButton(new GuiDimensionalButton(7, screen_coords[0] + EAST[0], screen_coords[1] + EAST[1], 20, sides[5], true, true));
		
		if (tile.getLockState()) {
			this.lock_button = this.addButton(new GuiDimensionalButton(9, screen_coords[0] + LOCK[0], screen_coords[1] + LOCK[1], 20, 5, true, true));
		} else {
			this.lock_button = this.addButton(new GuiDimensionalButton(9, screen_coords[0] + LOCK[0], screen_coords[1] + LOCK[1], 20, 6, true, true));
		}
		
	}
	
	protected void drawHoveringText(int mouse_x, int mouse_y, int[] screen_coords) {
		if (ModGuiUtil.IS_HOVERING.isHoveringPower(mouse_x, mouse_y, screen_coords[0] + power_bar[0], screen_coords[1] + power_bar[1])) {
			this.drawHoveringText(ModGuiUtil.TEXT_LIST.storedTextNo(this.tile.getEnergyStored(EnumFacing.DOWN)), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
		}
		
		String locked_string;
		
		if (tile.getLockState()) {
			locked_string = TextHelper.RED + "Locked.";
		} else {
			locked_string = TextHelper.GREEN + "Unlocked.";
		}
		
		String[] locked = new String[] { TextHelper.PURPLE + "Pocket is currently:", locked_string};
		
		if (ModGuiUtil.IS_HOVERING.isHovering(mouse_x, mouse_y, screen_coords[0] + LOCK[0], screen_coords[0] + LOCK[0] + 20, screen_coords[1] + LOCK[1], screen_coords[1] + LOCK[1] + 20)) {
			this.drawHoveringText(Arrays.asList(locked), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
		}
		
		/**
		else if (ModGuiUtil.IS_HOVERING.isHovering(mouse_x, mouse_y, screen_coords[0] + fluid_bar[0], screen_coords[0] + fluid_bar[2], screen_coords[1] + power_bar[1], screen_coords[1] + fluid_bar[3])) {
			if (this.tile.getTank().getFluidAmount() > 0) {
				this.drawHoveringText(ModGuiUtil.TEXT_LIST.fluidText(this.tile.getTank().getFluid().getLocalizedName(), this.tile.getTank().getFluidAmount()), mouse_x  - screen_coords[0], mouse_y - screen_coords[1]);
			} else {
				this.drawHoveringText(ModGuiUtil.TEXT_LIST.fluidTextEmpty(), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
			}
		} 
		
		else if (ModGuiUtil.IS_HOVERING.isHovering(mouse_x, mouse_y, screen_coords[0] + FLUID[0], screen_coords[0] + FLUID[2], screen_coords[1] + FLUID[1], screen_coords[1] + FLUID[3])) {
			if (!this.tile.isFluidEmpty()) {
				if (this.isShiftKeyDown()) {
					this.drawHoveringText(ModGuiUtil.TEXT_LIST.emptyFluidTankDo(), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
				} else {
					this.drawHoveringText(ModGuiUtil.TEXT_LIST.emptyFluidTank(), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
				}
			}
		}
		*/
	}
	
	public void drawStrings(int[] screen_coords) {
		ModGuiUtil.FONT.DRAW.drawString(fontRenderer, screen_coords, 112, 4, "gui.main.dimensional_pocket.name", true, false);
		ModGuiUtil.FONT.DRAW.drawString(fontRenderer, screen_coords, 119, 36, "gui.header.owner.name", true, false, ModGuiUtil.DEFAULT_COLOUR_FONT_LIST);
		
		if (tile.getPocket() != null) {
			if (tile.getPocket().getCreator() != null) {
				ModGuiUtil.FONT.DRAW.drawString(fontRenderer, screen_coords, 119, 49, this.tile.getPocket().getCreator(), false, false, ModGuiUtil.DEFAULT_COLOUR_FONT_LIST);
			} else {
				ModGuiUtil.FONT.DRAW.drawString(fontRenderer, screen_coords, 119, 36, "gui.comp.null_creator.name", true, false, ModGuiUtil.DEFAULT_COLOUR_FONT_LIST);
			}
		}
		
		ModGuiUtil.FONT.DRAW.drawInventoryString(fontRenderer, screen_coords, 88, 157, false);
		
		ModGuiUtil.FONT.DRAW.drawString(fontRenderer, screen_coords, 119, 64, "gui.header.allowed_players.name", true, false, ModGuiUtil.DEFAULT_COLOUR_FONT_LIST);
		ModGuiUtil.FONT.DRAW.drawString(fontRenderer, screen_coords, 8, 4, "gui.header.config.name", true, false);
		ModGuiUtil.FONT.DRAW.drawString(fontRenderer, screen_coords, 8, 88, "gui.header.stacks.name", true, false);
		
		if (this.info_screen) {
			ModGuiUtil.FONT.DRAW.drawString(fontRenderer, screen_coords, 261, 4, "gui.header.layout.name", true, false);
			
			ModGuiUtil.FONT.DRAW.drawString(fontRenderer, screen_coords, 261, 106, "gui.header.legend.name", true, false);
			ModGuiUtil.FONT.DRAW.drawString(fontRenderer, screen_coords, 294, 124, "gui.info.normal.name", true, false, ModGuiUtil.DEFAULT_COLOUR_FONT_LIST);
			ModGuiUtil.FONT.DRAW.drawString(fontRenderer, screen_coords, 294, 146, "gui.info.input.name", true, false, ModGuiUtil.DEFAULT_COLOUR_FONT_LIST);
			ModGuiUtil.FONT.DRAW.drawString(fontRenderer, screen_coords, 294, 168, "gui.info.output.name", true, false, ModGuiUtil.DEFAULT_COLOUR_FONT_LIST);
			ModGuiUtil.FONT.DRAW.drawString(fontRenderer, screen_coords, 294, 190, "gui.info.disabled.name", true, false, ModGuiUtil.DEFAULT_COLOUR_FONT_LIST);
		}
	}
}