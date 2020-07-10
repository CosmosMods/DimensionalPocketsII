package com.zeher.zeherlib.api.client.gui.element;

import javax.annotation.Nullable;

import com.zeher.zeherlib.ZLReference;
import com.zeher.zeherlib.mod.util.ModGuiUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiListElement extends Gui {
	
	public int width;
	public int height;
	public int xPosition;
	public int yPosition;
	public int id;
	public int text_colour;
	public String displayString;
	
	public ResourceLocation skin;
	
	protected boolean hovered;
	protected boolean selected;
	
	public GuiListElement() { }
	
	public GuiListElement(int elementId, int draw_x, int draw_y, int widthIn, int heightIn, @Nullable ResourceLocation skin, String list_test, int text_colour) {
		this.width = widthIn;
		this.height = heightIn;
		this.id = elementId;
		this.xPosition = draw_x;
		this.yPosition = draw_y;
		this.width = widthIn;
		this.height = heightIn;
		this.displayString = list_test;
		this.text_colour = text_colour;
		
		this.skin = skin;
	}
	
	public GuiListElement(int elementId, int[] screen_coords, int draw_x, int draw_y, int widthIn, int heightIn, @Nullable ResourceLocation skin, String list_test, int text_colour) {
		this.width = widthIn;
		this.height = heightIn;
		this.id = elementId;
		this.xPosition = screen_coords[0] + draw_x;
		this.yPosition = screen_coords[1] + draw_y;
		this.width = widthIn;
		this.height = heightIn;
		this.displayString = list_test;
		this.text_colour = text_colour;
		
		this.skin = skin;
	}

	protected int getHoverState(boolean mouseOver) {
		int i = 0;

		if (mouseOver) {
			i = 1;
		}
		
		return i;
	}
	
	public void drawElement(Minecraft mc, FontRenderer font_renderer, int[] screen_coords, int mouseX, int mouseY) {
		if (this.skin != null) {
			mc.getTextureManager().bindTexture(skin);
		} else {
			mc.getTextureManager().bindTexture(ZLReference.RESOURCE.BASE.GUI_ELEMENT_MISC_LOC);
		}
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		if (ModGuiUtil.IS_HOVERING.isHovering(mouseX, mouseY, screen_coords[0] + this.xPosition, screen_coords[0] + this.xPosition + this.width, screen_coords[1] + this.yPosition, screen_coords[1] + this.yPosition + this.height)) {
			this.hovered = true;
		} else {
			this.hovered = false;
		}
		
		int i = this.getHoverState(this.hovered);
		
		//this.drawTexturedModalRect(this.xPosition + mouseX, this.yPosition + mouseY, 56, 0 + 1 * 20, this.width / 2, this.height);
		
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableLighting();
		
		if (this.height == 20) {
			if (this.selected) {
				this.drawTexturedModalRect(this.xPosition, this.yPosition, 56, 0 + 1 * 20, this.width / 2, this.height);
				this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 256 - this.width / 2, 0 + 1 * 20, this.width / 2, this.height);
			} else {
				this.drawTexturedModalRect(this.xPosition, this.yPosition, 56, 0 + i * 20, this.width / 2, this.height);
				this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 256 - this.width / 2, 0 + i * 20, this.width / 2, this.height);
			}
		} else if (this.height == 14) {
			if (this.selected) {
				this.drawTexturedModalRect(this.xPosition, this.yPosition, 56, 42 + 1 * 14, this.width / 2, this.height);
				this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 256 - this.width / 2, 42 + 1 * 14, this.width / 2, this.height);
			} else {
				this.drawTexturedModalRect(this.xPosition, this.yPosition, 56, 42 + i * 14, this.width / 2, this.height);
				this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 256 - this.width / 2, 42 + i * 14, this.width / 2, this.height);
			}
		} else {
			if (this.selected) {
				//this.drawTexturedModalRect(this.xPosition, this.yPosition, 56, 0 + 1 * 20, this.width / 2, this.height);
				//this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 256 - this.width / 2, 0 + 1 * 20, this.width / 2, this.height);
			} else {
				//this.drawTexturedModalRect(this.xPosition, this.yPosition, 56, 0 + i * 20, this.width / 2, this.height);
				//this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 256 - this.width / 2, 0 + i * 20, this.width / 2, this.height);
			}
		}
		
		this.drawCenteredString(font_renderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, this.text_colour);
		
		GlStateManager.enableLighting();
	}
	
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		if (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height) {
			this.switchSelected();
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isMouseOver() {
		return this.hovered;
	}
	
	public void switchSelected() {
		this.selected = !(this.selected);
	}
	
	public boolean getSelected() {
		return this.selected;
	}
	
	public void deselect() {
		this.selected = false;
	}
	
	public void setSelectedState(boolean set) {
		this.selected = set;
	}
}