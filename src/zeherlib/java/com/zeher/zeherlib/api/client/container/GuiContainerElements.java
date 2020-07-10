package com.zeher.zeherlib.api.client.container;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import com.zeher.zeherlib.api.client.gui.element.GuiListElement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.MathHelper;

public class GuiContainerElements extends GuiContainer {
	
	public int current_scroll;
	private int max_scroll;
	
	protected ArrayList<GuiListElement> elementlist_array = new ArrayList<GuiListElement>();
	
	public GuiContainerElements(Container container) {
		super(container);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouse_x, int mouse_y) {
		int[] screen_coords = new int[] { ((this.width - this.xSize) / 2), (this.height - this.ySize) / 2 };

		for (int i = 0; i < this.elementlist_array.size(); ++i) {
            this.elementlist_array.get(i).drawElement(this.mc, this.fontRenderer, screen_coords, mouse_x, mouse_y);
        }
		
		//super.drawGuiContainerForegroundLayer(mouse_x, mouse_y);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	protected void mouseClicked(int mouse_x, int mouse_y, int mouse_button) throws IOException {
		for (int i = 0; i < elementlist_array.size(); i++) {
			GuiListElement element = elementlist_array.get(i);
			
			if (element.isMouseOver()) {
				element.mousePressed(mc, mouse_x, mouse_y);
				this.selected(i);
			}
		}
		super.mouseClicked(mouse_x, mouse_y, mouse_button);
	}
	
	protected void actionPerformed(GuiListElement button) throws IOException { }
	
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        
    	this.current_scroll = (this.current_scroll - i);
    	this.current_scroll = MathHelper.clamp(this.current_scroll, 0, this.max_scroll);
    }

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
	public void onResize(Minecraft mc, int width, int height) {
		this.elementlist_array.clear();
		
		super.onResize(mc, width, height);
	}
}