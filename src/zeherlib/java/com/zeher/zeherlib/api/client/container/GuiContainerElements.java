package com.zeher.zeherlib.api.client.container;

import java.io.IOException;
import java.util.ArrayList;

import com.zeher.zeherlib.api.client.gui.element.GuiListElement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;

public class GuiContainerElements extends ContainerScreen<Container> {
	
	public int current_scroll;
	@SuppressWarnings("unused")
	private int max_scroll;
	
	protected ArrayList<GuiListElement> elementlist_array = new ArrayList<GuiListElement>();
	
	public GuiContainerElements(Container container, PlayerInventory player_inventory, ITextComponent title) {
		super(container, player_inventory, title);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouse_x, int mouse_y) {
		int[] screen_coords = new int[] { ((this.width - this.xSize) / 2), (this.height - this.ySize) / 2 };

		for (int i = 0; i < this.elementlist_array.size(); ++i) {
            this.elementlist_array.get(i).drawElement(this.minecraft, this.font, screen_coords, mouse_x, mouse_y, i);
        }
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean mouseClicked(double mouse_x, double mouse_y, int mouse_button) {
		for (int i = 0; i < elementlist_array.size(); i++) {
			@SuppressWarnings("unused")
			GuiListElement element = elementlist_array.get(i);
			
			/**
			if (element.isMouseOver()) {
				element.mousePressed(mc, mouse_x, mouse_y);
				this.selected(i);
			}
			*/
		}
		super.mouseClicked(mouse_x, mouse_y, mouse_button);
		
		return false;
	}
	
	protected void actionPerformed(GuiListElement button) throws IOException { }
	
	/**
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        
    	this.current_scroll = (this.current_scroll - i);
    	this.current_scroll = MathHelper.clamp(this.current_scroll, 0, this.max_scroll);
    }
	*/
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) { }

	public <T extends GuiListElement> T addListElement(T element) {
		this.elementlist_array.add(element);
		return element;
	}
	
	public ArrayList<GuiListElement> getElementList() {
		return this.elementlist_array;
	}
	
	public void setElementList(ArrayList<GuiListElement> list) {
		this.elementlist_array = list;
	}
	
	public void selected(int index) {
		for (int i = 0; i < this.elementlist_array.size(); i++) {
			GuiListElement element = this.elementlist_array.get(i);
			element.deselect();
		}
		
		GuiListElement element_focused = this.elementlist_array.get(index);
		element_focused.setSelectedState(true);
	}
	
	public int getCurrentlySelectedInt() {
		for (int i = 0; i < this.elementlist_array.size(); i++) {
			GuiListElement element = this.elementlist_array.get(i);
			
			if (element.getSelected()) {
				return i;
			}
		}
		return 0;
	}
	
	@Override
	public void resize(Minecraft mc, int width, int height) {
		this.elementlist_array.clear();
		
		super.resize(mc, width, height);
	}
}