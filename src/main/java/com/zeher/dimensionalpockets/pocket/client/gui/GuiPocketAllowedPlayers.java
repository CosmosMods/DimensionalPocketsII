package com.zeher.dimensionalpockets.pocket.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.zeher.dimensionalpockets.core.handler.NetworkHandler;
import com.zeher.dimensionalpockets.core.reference.DimReference;
import com.zeher.dimensionalpockets.core.util.DimLogger;
import com.zeher.dimensionalpockets.pocket.client.container.ContainerDimensionalPocket;
import com.zeher.dimensionalpockets.pocket.client.tileentity.TileEntityDimensionalPocket;
import com.zeher.zeherlib.Reference;
import com.zeher.zeherlib.api.connection.EnumSide;
import com.zeher.zeherlib.api.util.GuiColours;
import com.zeher.zeherlib.client.container.GuiContainerElements;
import com.zeher.zeherlib.client.gui.button.GuiEnergyButtonCustom;
import com.zeher.zeherlib.client.gui.button.GuiFluidButton;
import com.zeher.zeherlib.client.gui.button.GuiIconButton;
import com.zeher.zeherlib.client.gui.element.GuiListElement;
import com.zeher.zeherlib.client.gui.util.ModGuiUtil;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiPocketAllowedPlayers extends GuiContainerElements {
	
	private GuiIconButton plus_button;
	private GuiIconButton minus_button;
	private GuiIconButton text_clear_button;
	
	private TileEntityDimensionalPocket tile;
	
	private GuiTextField text_field;
	
	public int current_scroll;
	private int max_scroll = 1660;

	private ArrayList<GuiListElement> element_list = new ArrayList<GuiListElement>();
	
	public GuiPocketAllowedPlayers(InventoryPlayer inventoryPlayer, TileEntityDimensionalPocket tileEntity) {
		super(new ContainerDimensionalPocket(inventoryPlayer, tileEntity));
		this.tile = tileEntity;
		
		this.xSize = 200;
		this.ySize = 256;
	}
	
	@Override
	public void initGui () {
		int[] screen_coords = new int[] { (this.width - this.xSize) / 2, (this.height - this.ySize) / 2 };

        Keyboard.enableRepeatEvents(true);
        
		this.text_field = new GuiTextField(2, fontRenderer, screen_coords[0] + 16, screen_coords[1] + 21, 116, 17);
		this.text_field.setMaxStringLength(20);
		this.text_field.setVisible(true);
        this.text_field.setTextColor(ModGuiUtil.DEFAULT_COLOUR_FONT_LIST);
        this.text_field.setEnableBackgroundDrawing(false);
        this.text_field.setCanLoseFocus(true);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		int[] screen_coords = new int[] { (this.width - this.xSize) / 2, (this.height - this.ySize) / 2 };

		ModGuiUtil.FONT.DRAW.drawStringFormatted(fontRenderer, screen_coords, 12, 4, "gui.allowed_players.name");
		ModGuiUtil.FONT.DRAW.drawStringUnformatted(fontRenderer, screen_coords, 16, 44, "Pocket Owner: [" + tile.getPocket().getCreator() + "]", ModGuiUtil.DEFAULT_COLOUR_FONT_LIST);

		this.buttonList.clear();
		this.text_clear_button = this.addButton(new GuiIconButton(0, screen_coords[0] + 131, screen_coords[1] + 16, 18, 3, !(this.text_field.getText().isEmpty())));
		this.plus_button = this.addButton(new GuiIconButton(1, screen_coords[0] + 151, screen_coords[1] + 16, 18, 1, true));
		this.minus_button = this.addButton(new GuiIconButton(2, screen_coords[0] + 171, screen_coords[1] + 16, 18, 2, true));
		
		this.text_field.drawTextBox();
		
		/**
		ArrayList<String> test = new ArrayList<String>();
		test.add("TESTING");
		test.add("LOL");
		test.add("This is a test.");
		*/
		//this.element_list.clear();
		//ModGuiUtil.ELEMENT.LIST.DRAW.drawListWithElementsSmall(this, fontRenderer, screen_coords, 12, 63, 157, 0, test);
		//System.out.println(this.element_list.size());
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		int[] screen_coords = new int[] { (this.width - this.xSize) / 2, (this.height - this.ySize) / 2};
		
		ModGuiUtil.DRAW.drawBackground(this, screen_coords, DimReference.GUI.RESOURCE.GUI_ALLOWED_PLAYERS);
		ModGuiUtil.ELEMENT.SCROLL.DRAW.drawScrollElement(this, screen_coords, 175, 63, this.current_scroll, true, 1);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.equals(this.plus_button)) {
			tile.getPocket().addAllowedPlayer(text_field.getText());
			NetworkHandler.sendPocketPlayer(text_field.getText(), tile.getPos(), true);
		} else if (button.equals(this.minus_button)) {
			tile.getPocket().removeAllowedPlayer(text_field.getText());
			NetworkHandler.sendPocketPlayer(text_field.getText(), tile.getPos(), false);
			this.element_list.clear();
			
		} else if (button.equals(this.text_clear_button)) {
			this.text_field.setText("");
		}
	}
	
	@Override
	protected void actionPerformed(GuiListElement element) { }
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.renderHoveredToolTip(mouseX, mouseY);
		for (int i = 0; i < this.element_list.size(); ++i) {
            ((GuiListElement)this.element_list.get(i)).drawElement(this.mc, mouseX, mouseY, partialTicks);
        }
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (!this.checkHotbarKeys(keyCode)) {
			if (this.text_field.isFocused()) {
				this.text_field.textboxKeyTyped(typedChar, keyCode);
			} else {
				super.keyTyped(typedChar, keyCode);
			}
		}
	}

	@Override
	protected void mouseClicked(int mouse_x, int mouse_y, int mouse_button) throws IOException {
		int[] screen_coords = new int[] { (this.width - this.xSize) / 2, (this.height - this.ySize) / 2 };

		if (mouse_button == 0) {
			if ((mouse_x > this.text_field.x && mouse_x < (this.text_field.x + this.text_field.width)) && (mouse_y > this.text_field.y && mouse_y < (this.text_field.y + this.text_field.height))) {
				this.text_field.setFocused(true);
			} else {
				this.text_field.setFocused(false);
			}
		}
		
		for (int i = 0; i < element_list.size(); i++) {
			GuiListElement element = element_list.get(i);
			if (element.isMouseOver()) {
				element.mousePressed(mc, mouse_x, mouse_y);
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

	public <T extends GuiListElement> T addListElement(T element) {
		this.element_list.add(element);
		return element;
	}
	
	public ArrayList<GuiListElement> getElementList() {
		return this.element_list;
	}
	
	public void setElementList(ArrayList<GuiListElement> list) {
		this.element_list = list;
	}
}