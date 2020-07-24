package com.zeher.dimpockets.pocket.client.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.zeher.dimpockets.DimReference;
import com.zeher.dimpockets.client.gui.button.GuiDimensionalButton;
import com.zeher.dimpockets.core.manager.ModNetworkManager;
import com.zeher.dimpockets.pocket.client.container.ContainerPocket;
import com.zeher.dimpockets.pocket.core.tileentity.TilePocket;
import com.zeher.zeherlib.api.client.container.GuiContainerElements;
import com.zeher.zeherlib.api.client.gui.element.GuiListElement;
import com.zeher.zeherlib.api.client.util.TextHelper;
import com.zeher.zeherlib.mod.util.ModGuiUtil;
import com.zeher.zeherlib.mod.util.ModGuiUtil.DRAW;
import com.zeher.zeherlib.mod.util.ModGuiUtil.DRAW.HOVER;
import com.zeher.zeherlib.mod.util.ModGuiUtil.ELEMENT;
import com.zeher.zeherlib.mod.util.ModGuiUtil.ELEMENT.LIST;
import com.zeher.zeherlib.mod.util.ModGuiUtil.ELEMENT.SCROLL;
import com.zeher.zeherlib.mod.util.ModGuiUtil.FONT;
import com.zeher.zeherlib.mod.util.ModGuiUtil.IS_HOVERING;
import com.zeher.zeherlib.mod.util.ModGuiUtil.TEXT_LIST;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import java.util.Arrays;

public class GuiPocket extends GuiContainerElements {
	
	private TilePocket tile;
	
	public int current_scroll;
	private int max_scroll = 1560;
	
	private boolean info_screen = true;
	
	private int[] SCREEN = new int[] { 256 + 92, 256 };
	
	private int[] ENERGY_BAR = new int[] { 11, 101 };
	private int[] ENERGY_BAR_ = new int[] { 72, 160, 58, 18 };
	private int[] FLUID_BAR = new int[] { 37, 52, 102, 157, 18, 58 };

	private GuiDimensionalButton TCB;
	private int[] TCBI = new int[] { 195, 16, 18 };
	private GuiDimensionalButton TPB;
	private int[] TPBI = new int[] { 215, 16, 18 };
	private GuiDimensionalButton TMB;
	private int[] TMBI = new int[] { 235, 16, 18 };
	
	private GuiDimensionalButton TAEB;
	private GuiDimensionalButton TACB;
	private int[] TABI = new int[] { 61, 141, 18 };
	
	private GuiTextField TEXT_FIELD;
	private int[] TEXT_FIELDI = new int[] { 92, 17, 100, 16 };
	
	private int[] ELEMENT_LISTI = new int[] { 92, 45, 140, 0 };
	
	private GuiDimensionalButton NB;
	private int[] NBI = new int[] { 36, 41, 18 };
	private GuiDimensionalButton SB;
	private int[] SBI = new int[] { 60, 65, 18 };
	private GuiDimensionalButton UB;
	private int[] UBI = new int[] { 36, 17, 18 };
	private GuiDimensionalButton DB;
	private int[] DBI = new int[] { 36, 65, 18 };
	private GuiDimensionalButton EB;
	private int[] EBI = new int[] { 12, 41, 18 };
	private GuiDimensionalButton WB;
	private int[] WBI = new int[] { 60, 41, 18 };

	private GuiDimensionalButton LB;
	private int[] LBI = new int[] { 11, 64, 20 };
	
	private GuiDimensionalButton SIB;
	private int[] SIBI = new int[] { 59, 16, 20 };
	
	public GuiPocket(InventoryPlayer inventoryPlayer, TilePocket tileEntity) {
		super(new ContainerPocket(inventoryPlayer, tileEntity));
		this.tile = tileEntity;
				
		this.xSize = SCREEN[0];
		this.ySize = SCREEN[1];
	}
	
	@Override
	public void initGui () {
		this.initTextField();
        super.initGui();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouse_x, int mouse_y) {
		int[] screen_coords = new int[] { ((this.width - this.xSize) / 2), (this.height - this.ySize) / 2 };

		LIST.drawSmall(this, fontRenderer, ELEMENT_LISTI[0], ELEMENT_LISTI[1], ELEMENT_LISTI[2], ELEMENT_LISTI[3], DimReference.GUI.RESOURCE.GUI_DIMENSIONAL_ELEMENT, tile.getPocket().getPlayerMap());
		
		//Order is VERY important
		this.drawStrings(screen_coords);
		this.TEXT_FIELD.drawTextBox();
		this.drawButtons(screen_coords);
		super.drawGuiContainerForegroundLayer(mouse_x, mouse_y);
		this.drawHoveringText(mouse_x, mouse_y, screen_coords);
		this.renderHoveredToolTip(mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		int[] screen_coords = new int[] { ((this.width - this.xSize) / 2), (this.height - this.ySize) / 2};

		DRAW.bindTexture(this, DimReference.GUI.RESOURCE.GUI_POCKET);
		DRAW.drawStaticElement(this, screen_coords, 0, 0, 0, 0, 256, 256);
		SCROLL.drawScrollElement(this, screen_coords, 239, 45, this.current_scroll, true, 2, false);
		
		DRAW.drawPowerBar(this, screen_coords, ENERGY_BAR[0], ENERGY_BAR[1], this.tile.getEnergyScaled(ENERGY_BAR_[2]), ENERGY_BAR_, this.tile.hasEnergy());
		//ModGuiUtil.DRAW.drawFluidTank(this, screen_coords, fluid_bar[0], fluid_bar[1], this.tile.getTank(), this.tile.getFluidLevelScaled(38));
		
		DRAW.bindTexture(this, DimReference.GUI.RESOURCE.GUI_POCKET_SLOTS);
		DRAW.drawStaticElement(this, screen_coords, 256, 0, 0, 0, 92, 256);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.equals(this.TPB)) {
			this.tile.getPocket().addToPlayerMap(TEXT_FIELD.getText());
			this.ELEMENTS.clear();
			
			this.tile.markDirty();
		} 
		
		else if (button.equals(this.TMB)) {
			ArrayList<String> allowed_players = tile.getPocket().getPlayerMap();
			this.tile.getPocket().removeFromPlayerMap(allowed_players.get(this.getSelectedElementId()));
			this.ELEMENTS.clear();
			
			this.tile.markDirty();
		} 
		
		else if (button.equals(this.TCB)) {
			this.TEXT_FIELD.setText("");
		} 
		
		else if (button.equals(this.LB)) {
			boolean lock = !tile.getLockState();
			tile.setLockState(lock);
			ModNetworkManager.sendPocketLock(tile.getPos(), lock);
		}
		
		else if (button.equals(this.TACB)) {
			if (this.isShiftKeyDown()) {
				ModNetworkManager.sendPocketEmpty(tile.getPos(), 0);
			}
		} 
		
		else if (button.equals(this.SIB)) {
			boolean sides_state = !tile.getSidesState();
			tile.setSidesState(sides_state);
			ModNetworkManager.sendPocketSides(tile.getPos(), sides_state);
		}
		
		else if (button.equals(this.UB)) {
			ModNetworkManager.sendPocketCycleSide(tile.getPos(), EnumFacing.UP);
		} else if (button.equals(this.DB)) {
			ModNetworkManager.sendPocketCycleSide(tile.getPos(), EnumFacing.DOWN);
		} else if (button.equals(this.NB)) {
			ModNetworkManager.sendPocketCycleSide(tile.getPos(), EnumFacing.NORTH);
		} else if (button.equals(this.SB)) {
			ModNetworkManager.sendPocketCycleSide(tile.getPos(), EnumFacing.SOUTH);
		} else if (button.equals(this.WB)) {
			ModNetworkManager.sendPocketCycleSide(tile.getPos(), EnumFacing.WEST);
		} else if (button.equals(this.EB)) {
			ModNetworkManager.sendPocketCycleSide(tile.getPos(), EnumFacing.EAST);
		}
	}
	
	private void drawButtons(int[] screen_coords) {
		this.buttonList.clear();
		this.TCB = this.addButton(new GuiDimensionalButton(0, screen_coords[0] + TCBI[0], screen_coords[1] + TCBI[1], 18, 3, !(this.TEXT_FIELD.getText().isEmpty()), true));
		this.TPB = this.addButton(new GuiDimensionalButton(1, screen_coords[0] + TPBI[0], screen_coords[1] + TPBI[1], 18, 1, true, true));
		this.TMB = this.addButton(new GuiDimensionalButton(2, screen_coords[0] + TMBI[0], screen_coords[1] + TMBI[1], 18, 2, true, true));
		
		int[] sides = new int[] { 0, 0, 0, 0, 0, 0 };
		for(EnumFacing c : EnumFacing.VALUES) {
			sides[c.getIndex()] = (tile.getSide(c).getIndex() + 6);
		}
		
		this.DB = this.addButton(new GuiDimensionalButton(6, screen_coords[0] + DBI[0], screen_coords[1] + DBI[1], 18, sides[0], true, true));
		this.UB = this.addButton(new GuiDimensionalButton(5, screen_coords[0] + UBI[0], screen_coords[1] + UBI[1], 18, sides[1], true, true));
		this.NB = this.addButton(new GuiDimensionalButton(3, screen_coords[0] + NBI[0], screen_coords[1] + NBI[1], 18, sides[2], true, true));
		this.SB = this.addButton(new GuiDimensionalButton(4, screen_coords[0] + SBI[0], screen_coords[1] + SBI[1], 18, sides[3], true, true));
		this.WB = this.addButton(new GuiDimensionalButton(8, screen_coords[0] + WBI[0], screen_coords[1] + WBI[1], 18, sides[4], true, true));
		this.EB = this.addButton(new GuiDimensionalButton(7, screen_coords[0] + EBI[0], screen_coords[1] + EBI[1], 18, sides[5], true, true));
		
		if (tile.getLockState()) {
			this.LB = this.addButton(new GuiDimensionalButton(9, screen_coords[0] + LBI[0], screen_coords[1] + LBI[1], 20, 5, true, true));
		} else {
			this.LB = this.addButton(new GuiDimensionalButton(9, screen_coords[0] + LBI[0], screen_coords[1] + LBI[1], 20, 4, true, true));
		}
		
		if(this.tile.isFluidEmpty()){
			this.TAEB = this.addButton(new GuiDimensionalButton(0, screen_coords[0] + TABI[0], screen_coords[1] + TABI[1], 18, 18, false, true));
		} else {
			this.TACB = this.addButton(new GuiDimensionalButton(0, screen_coords[0] + TABI[0], screen_coords[1] + TABI[1], 18, 19, true, true));
		}
		
		this.SIB = this.addButton(new GuiDimensionalButton(11, screen_coords[0] + SIBI[0], screen_coords[1] + SIBI[1], 20, 20, true, true));
	}
	
	protected void drawHoveringText(int mouse_x, int mouse_y, int[] screen_coords) {
		if (IS_HOVERING.is(screen_coords, true, mouse_x, mouse_y, ENERGY_BAR[0], ENERGY_BAR[0] + ENERGY_BAR_[3], ENERGY_BAR[1], ENERGY_BAR_[1] + ENERGY_BAR_[2])) {
			this.drawHoveringText(TEXT_LIST.storedRF(this.tile.getEnergyStored(EnumFacing.DOWN), 0, this.tile.getMaxEnergyStored(EnumFacing.DOWN), false), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
		}

		HOVER.fluidCustom(this, screen_coords, FLUID_BAR[0], FLUID_BAR[2], mouse_x, mouse_y, FLUID_BAR[4], FLUID_BAR[5], this.tile.getCurrentStoredFluidName(), this.tile.getCurrentFluidAmount(), this.tile.getFluidCapacity());
		HOVER.fluidButtonEmpty(this, screen_coords, TABI[0], TABI[1], mouse_x, mouse_y, !this.tile.isFluidEmpty());
		
		/** - Lock Button - */
		String locked_string;
		if (tile.getLockState()) {
			locked_string = TextHelper.RED + "Locked.";
		} else {
			locked_string = TextHelper.GREEN + "Unlocked.";
		}
		
		String[] locked = new String[] { 
				TextHelper.PURPLE + TextHelper.BOLD + "Lock or unlock Pocket." + TextHelper.END, 
				TextHelper.LIGHT_BLUE + "Pocket is currently: " + locked_string };
		
		if (IS_HOVERING.is(screen_coords, true, mouse_x, mouse_y, LBI[0], LBI[0] + LBI[2], LBI[1], LBI[1] + LBI[2])) {
			this.drawHoveringText(Arrays.asList(locked), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
		}
		
		/** - Sides Button - */
		String sides_string;
		if (tile.getSidesState()) {
			locked_string = TextHelper.GREEN + "Shown";
		} else {
			locked_string = TextHelper.RED + "Hidden";
		}
		
		String[] sides = new String[] { 
				TextHelper.PURPLE + TextHelper.BOLD + "Show or hide side guides" + TextHelper.END, 
				TextHelper.LIGHT_BLUE + "Side guides: " + locked_string };
		
		if (IS_HOVERING.is(screen_coords, true, mouse_x, mouse_y, SIBI[0], SIBI[0] + SIBI[2], SIBI[1], SIBI[1] + SIBI[2])) {
			this.drawHoveringText(Arrays.asList(sides), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
		}
		
		/** - Text Clear Button - */
		if (this.TCB.enabled) {
			if (IS_HOVERING.is(screen_coords, true, mouse_x, mouse_y, TCBI[0], TCBI[0] + TCBI[2], TCBI[1], TCBI[1] + TCBI[2])) {
				this.drawHoveringText(TextHelper.PURPLE + TextHelper.BOLD + "Clear the text box", mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
			}
		}
		
		/** - Text Plus Button - */
		if (IS_HOVERING.is(screen_coords, true, mouse_x, mouse_y, TPBI[0], TPBI[0] + TPBI[2], TPBI[1], TPBI[1] + TPBI[2])) {
			this.drawHoveringText(TextHelper.PURPLE + TextHelper.BOLD + "Add player to list", mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
		}
		
		/** - Text Minus Button - */
		if (IS_HOVERING.is(screen_coords, true, mouse_x, mouse_y, TMBI[0], TMBI[0] + TMBI[2], TMBI[1], TMBI[1] + TMBI[2])) {
			this.drawHoveringText(TextHelper.PURPLE + TextHelper.BOLD + "Remove player from list", mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
		}
		
		/** - Pocket Tile Buttons - */
		String interfacet = "Change Pocket Interface Type "; 
		
		String[] up = new String[] {
				TextHelper.PURPLE + TextHelper.BOLD + interfacet + TextHelper.ORANGE + TextHelper.BOLD + "[UP]",
				TextHelper.LIGHT_BLUE + "Current Type: " + tile.getSide(EnumFacing.UP).getTextColour() + 
				"[" + tile.getSide(EnumFacing.UP).getDisplayName() + "]"
		};
		
		if (IS_HOVERING.is(screen_coords, true, mouse_x, mouse_y, UBI[0], UBI[0] + UBI[2], UBI[1], UBI[1] + UBI[2])) {
			this.drawHoveringText(Arrays.asList(up), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
		}
		
		String[] down = new String[] {
				TextHelper.PURPLE + TextHelper.BOLD + interfacet + TextHelper.YELLOW + TextHelper.BOLD + "[DOWN]",
				TextHelper.LIGHT_BLUE + "Current Type: " + tile.getSide(EnumFacing.DOWN).getTextColour() + 
				"[" + tile.getSide(EnumFacing.DOWN).getDisplayName() + "]"
		};
		
		if (IS_HOVERING.is(screen_coords, true, mouse_x, mouse_y, DBI[0], DBI[0] + DBI[2], DBI[1], DBI[1] + DBI[2])) {
			this.drawHoveringText(Arrays.asList(down), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
		}
		
		String[] north = new String[] {
				TextHelper.PURPLE + TextHelper.BOLD + interfacet + TextHelper.BLUE + TextHelper.BOLD + "[NORTH]",
				TextHelper.LIGHT_BLUE + "Current Type: " + tile.getSide(EnumFacing.NORTH).getTextColour() + 
				"[" + tile.getSide(EnumFacing.NORTH).getDisplayName() + "]"
		};
		
		if (IS_HOVERING.is(screen_coords, true, mouse_x, mouse_y, NBI[0], NBI[0] + NBI[2], NBI[1], NBI[1] + NBI[2])) {
			this.drawHoveringText(Arrays.asList(north), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
		}
		
		String[] south = new String[] {
				TextHelper.PURPLE + TextHelper.BOLD + interfacet + TextHelper.GREEN + TextHelper.BOLD + "[SOUTH]",
				TextHelper.LIGHT_BLUE + "Current Type: " + tile.getSide(EnumFacing.SOUTH).getTextColour() + 
				"[" + tile.getSide(EnumFacing.SOUTH).getDisplayName() + "]"
		};
		
		if (IS_HOVERING.is(screen_coords, true, mouse_x, mouse_y, SBI[0], SBI[0] + SBI[2], SBI[1], SBI[1] + SBI[2])) {
			this.drawHoveringText(Arrays.asList(south), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
		}
		
		String[] east = new String[] {
				TextHelper.PURPLE + TextHelper.BOLD + interfacet  + TextHelper.GRAY + TextHelper.BOLD + "[EAST]",
				TextHelper.LIGHT_BLUE + "Current Type: " + tile.getSide(EnumFacing.EAST).getTextColour() + 
				"[" + tile.getSide(EnumFacing.EAST).getDisplayName() + "]"
		};
		
		if (IS_HOVERING.is(screen_coords, true, mouse_x, mouse_y, EBI[0], EBI[0] + EBI[2], EBI[1], EBI[1] + EBI[2])) {
			this.drawHoveringText(Arrays.asList(east), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
		}
		
		String[] west = new String[] {
				TextHelper.PURPLE + TextHelper.BOLD + interfacet + TextHelper.WHITE + TextHelper.BOLD + "[WEST]",
				TextHelper.LIGHT_BLUE + "Current Type: " + tile.getSide(EnumFacing.WEST).getTextColour() + 
				"[" + tile.getSide(EnumFacing.WEST).getDisplayName() + "]"
		};
		
		if (IS_HOVERING.is(screen_coords, true, mouse_x, mouse_y, WBI[0], WBI[0] + WBI[2], WBI[1], WBI[1] + WBI[2])) {
			this.drawHoveringText(Arrays.asList(west), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
		}
	}
	
	public void drawStrings(int[] screen_coords) {
		FONT.drawString(fontRenderer, screen_coords, 88, 4, "gui.main.dimensional_pocket.name", true, false, ModGuiUtil.FONT_LIST);
		//ModGuiUtil.FONT.DRAW.drawString(fontRenderer, screen_coords, 119, 36, "gui.header.owner.name", true, false, ModGuiUtil.DEFAULT_COLOUR_FONT_LIST);

		FONT.drawInventoryString(fontRenderer, screen_coords, 88, 157, false, ModGuiUtil.FONT_LIST);
		
		FONT.drawString(fontRenderer, screen_coords, 93, 35, "gui.header.allowed_players.name", true, false, ModGuiUtil.FONT_LIST);
		FONT.drawString(fontRenderer, screen_coords, 8, 4, "gui.header.config.name", true, false, ModGuiUtil.FONT_LIST);
		FONT.drawString(fontRenderer, screen_coords, 8, 89, "gui.header.internal.name", true, false, ModGuiUtil.FONT_LIST);
		
		FONT.drawString(fontRenderer, screen_coords, 262, 4, "gui.header.pocket_inv.name", true, false, ModGuiUtil.FONT_LIST);
	}
	
	public void initTextField() {
        Keyboard.enableRepeatEvents(true);
        
		this.TEXT_FIELD = new GuiTextField(2, fontRenderer, this.TEXT_FIELDI[0], this.TEXT_FIELDI[1], this.TEXT_FIELDI[2], this.TEXT_FIELDI[3]);
		this.TEXT_FIELD.setMaxStringLength(20);
		this.TEXT_FIELD.setVisible(true);
        this.TEXT_FIELD.setTextColor(ModGuiUtil.FONT_LIST);
        this.TEXT_FIELD.setEnableBackgroundDrawing(true);
        this.TEXT_FIELD.setCanLoseFocus(true);
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
		this.ELEMENTS.clear();
		
		super.onResize(mc, width, height);
	}

	@Override
	protected void actionPerformed(GuiListElement element) { }
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (!this.checkHotbarKeys(keyCode)) {
			if (this.TEXT_FIELD.isFocused()) {
				if (keyCode == 1) {
					this.TEXT_FIELD.setFocused(false);
					this.TEXT_FIELD.setText("");
				} else if (keyCode == 28) {
					tile.getPocket().addToPlayerMap(TEXT_FIELD.getText());
					this.ELEMENTS.clear();
					tile.markDirty();
				} else {
					this.TEXT_FIELD.textboxKeyTyped(typedChar, keyCode);
				}
			} else {
				super.keyTyped(typedChar, keyCode);
			}
		}
	}

	@Override
	protected void mouseClicked(int mouse_x, int mouse_y, int mouse_button) throws IOException {
		int[] screen_coords = new int[] { (this.width - this.xSize) / 2, (this.height - this.ySize) / 2 };
		
		int weighted_x = screen_coords[0] + this.TEXT_FIELD.x;
		int weighted_y = screen_coords[1] + this.TEXT_FIELD.y;
		
		if (mouse_button == 0) {
			if (IS_HOVERING.pure(mouse_x, mouse_y, weighted_x, weighted_x + this.TEXT_FIELD.width, weighted_y, weighted_y + this.TEXT_FIELD.height)) {				
				this.TEXT_FIELD.setFocused(true);
			} else {
				this.TEXT_FIELD.setFocused(false);
			}
		}
		
		super.mouseClicked(mouse_x, mouse_y, mouse_button);
	}
}