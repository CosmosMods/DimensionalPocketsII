package com.zeher.zeherlib.api.client.container;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import com.zeher.zeherlib.api.client.gui.element.GuiListElementSound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.MathHelper;

public class GuiContainerElementsSound extends GuiContainer {
	
	public int current_scroll;
	private int max_scroll;
	
	protected ArrayList<GuiListElementSound> elementlist_array = new ArrayList<GuiListElementSound>();
	
	public GuiContainerElementsSound(Container container) {
		super(container);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		for (int i = 0; i < this.elementlist_array.size(); ++i) {
            ((GuiListElementSound)this.elementlist_array.get(i)).drawElement(this.mc, mouseX, mouseY, partialTicks);
        }
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	protected void mouseClicked(int mouse_x, int mouse_y, int mouse_button) throws IOException {
		for (int i = 0; i < elementlist_array.size(); i++) {
			GuiListElementSound element = elementlist_array.get(i);
			
			if (element.isMouseOver()) {
				element.mousePressed(mc, mouse_x, mouse_y);
				this.selected(i);
			}
		}
		super.mouseClicked(mouse_x, mouse_y, mouse_button);
	}
	
	protected void actionPerformed(GuiListElementSound button) throws IOException { }
	
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        
    	this.current_scroll = (this.current_scroll - i);
    	this.current_scroll = MathHelper.clamp(this.current_scroll, 0, this.max_scroll);
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		
	}

	public <T extends GuiListElementSound> T addListElement(T element) {
		this.elementlist_array.add(element);
		return element;
	}
	
	public ArrayList<GuiListElementSound> getElementList() {
		return this.elementlist_array;
	}
	
	public void setElementList(ArrayList<GuiListElementSound> list) {
		this.elementlist_array = list;
	}
	
	public void selected(int index) {
		if (index == -1) {
			for (int j = 0; j < this.elementlist_array.size(); j++) {
				GuiListElementSound element = this.elementlist_array.get(j);
				
				if (element.getSelected()) {
					element.switchSelected();
				}
			}
		} else {
			for (int i = 0; i < this.elementlist_array.size(); i++) {
				if (i == index) {
					return;
				} else {
					GuiListElementSound element = this.elementlist_array.get(i);
					
					if (element.getSelected()) {
						element.switchSelected();	
					}
				}
			}
			
			for (int i = (this.elementlist_array.size() - 1); i >= 0; i--) {
				if (i == index) {
					return;
				} else {
					GuiListElementSound element = this.elementlist_array.get(i);
					
					if (element.getSelected()) {
						element.switchSelected();	
					}
				}
			}
		}
	}
	
	public int getCurrentlySelected() {
		for (int i = 0; i < this.elementlist_array.size(); i++) {
			GuiListElementSound element = this.elementlist_array.get(i);
			
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