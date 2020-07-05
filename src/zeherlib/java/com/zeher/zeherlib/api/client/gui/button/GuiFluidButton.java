package com.zeher.zeherlib.api.client.gui.button;

import com.zeher.zeherlib.ZeherLib;
import com.zeher.zeherlib.ZLReference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiFluidButton extends GuiButton {
	
	public int width;
	public int height;
	public int xPosition;
	public int yPosition;
	public int size;
	
	public int identifier;
	public int id;
	
	public boolean enabled;
	public boolean visible;
	protected boolean hovered;
	
	public GuiFluidButton(int buttonId, int x, int y, int identifier, boolean enabled) {
		this(buttonId, x, y, 20, identifier, enabled);
	}

	public GuiFluidButton(int buttonId, int x, int y, int size, int identifier, boolean enabled) {
		super(buttonId, x, y, size, size, "");
		this.width = size;
		this.height = size;
		this.size = size;
		this.enabled = enabled;
		this.visible = true;
		this.id = buttonId;
		this.xPosition = x;
		this.yPosition = y;
		this.identifier = identifier;
	}
	
	protected int getHoverState(boolean mouseOver) {
		int i = 0;

		if (!this.enabled) {
			i = 2;
		} else if (mouseOver) {
			i = 1;
		}
		return i;
	}
	
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float ticks) {
		if (this.visible) {
			FontRenderer fontrenderer = mc.fontRenderer;
			mc.getTextureManager().bindTexture(ZLReference.RESOURCE.BASE.GUI_FLUID_BUTTON_LOC);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
			int i = this.getHoverState(this.hovered);
			
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			
			if (this.identifier <= 5) {
				if (this.width == 20 && this.height == 20) {
					this.drawTexturedModalRect(this.xPosition, this.yPosition, ZLReference.RESOURCE.INFO.BUTTON_STATE_X[identifier], ZLReference.RESOURCE.INFO.BUTTON_STATE_Y[i], this.width, this.height);
				} else if (this.width == 18 && this.height == 18) {
					this.drawTexturedModalRect(this.xPosition, this.yPosition, ZLReference.RESOURCE.INFO.BUTTON_STATE_X_SMALL[identifier], ZLReference.RESOURCE.INFO.BUTTON_STATE_Y_SMALL[i], this.width, this.height);
				}
			} else if (this.identifier > 5 && this.identifier <= 11) {
				if (this.width == 20 && this.height == 20) {
					this.drawTexturedModalRect(this.xPosition, this.yPosition, ZLReference.RESOURCE.INFO.BUTTON_STATE_X[identifier - 6], ZLReference.RESOURCE.INFO.BUTTON_STATE_Y[i + 3], this.width, this.height);
				} else if (this.width == 18 && this.height == 18) {
					this.drawTexturedModalRect(this.xPosition, this.yPosition, ZLReference.RESOURCE.INFO.BUTTON_STATE_X_SMALL[identifier - 6], ZLReference.RESOURCE.INFO.BUTTON_STATE_Y_SMALL[i + 3], this.width, this.height);
				}
			} else if (this.identifier > 11 && this.identifier <= 17) {
				if (this.width == 20 && this.height == 20) {
					this.drawTexturedModalRect(this.xPosition, this.yPosition, ZLReference.RESOURCE.INFO.BUTTON_STATE_X[identifier - 12], ZLReference.RESOURCE.INFO.BUTTON_STATE_Y[i + 6], this.width, this.height);
				} else if (this.width == 18 && this.height == 18) {
					this.drawTexturedModalRect(this.xPosition, this.yPosition, ZLReference.RESOURCE.INFO.BUTTON_STATE_X_SMALL[identifier - 12], ZLReference.RESOURCE.INFO.BUTTON_STATE_Y_SMALL[i + 6], this.width, this.height);
				}
			}  else if (this.identifier > 17 && this.identifier <= 23) {
				if (this.width == 20 && this.height == 20) {
					this.drawTexturedModalRect(this.xPosition, this.yPosition, ZLReference.RESOURCE.INFO.BUTTON_STATE_X[identifier - 18], ZLReference.RESOURCE.INFO.BUTTON_STATE_Y[i + 9], this.width, this.height);
				} else if (this.width == 18 && this.height == 18) {
					this.drawTexturedModalRect(this.xPosition, this.yPosition, ZLReference.RESOURCE.INFO.BUTTON_STATE_X_SMALL[identifier - 18], ZLReference.RESOURCE.INFO.BUTTON_STATE_Y_SMALL[i + 9], this.width, this.height);
				}
			}
			
			/**
			if(identifier < 5){
				if(this.width == 20 && this.height == 20){
					this.drawTexturedModalRect(this.xPosition, this.yPosition, this.button_state_x[identifier * 2], this.button_state_y[i], this.width, this.height);
				}
				if(this.width == 18 && this.height == 18){
					this.drawTexturedModalRect(this.xPosition, this.yPosition, this.button_state_x[(identifier * 2) + 1], this.button_state_y[i], this.width, this.height);
				}
			}
			if(identifier == 5){
				if(this.width == 20 && this.height == 20){
					this.drawTexturedModalRect(this.xPosition, this.yPosition, this.button_state_x[identifier - 5], this.button_state_y[i + 3], this.width, this.height);
				}
				if(this.width == 18 && this.height == 18){
					this.drawTexturedModalRect(this.xPosition, this.yPosition, this.button_state_x[(identifier - 5) + 1], this.button_state_y[i + 3], this.width, this.height);
				}
			}
			
			if(identifier == 6){
				if(this.width == 20 && this.height == 20){
					this.drawTexturedModalRect(this.xPosition, this.yPosition, this.button_state_x[identifier - 4], this.button_state_y[i + 3], this.width, this.height);
				}
				if(this.width == 18 && this.height == 18){
					this.drawTexturedModalRect(this.xPosition, this.yPosition, this.button_state_x[(identifier - 4) + 1], this.button_state_y[i + 3], this.width, this.height);
				}
			}
			
			if(identifier == 7){
				if(this.width == 20 && this.height == 20){
					this.drawTexturedModalRect(this.xPosition, this.yPosition, this.button_state_x[identifier - 3], this.button_state_y[i + 3], this.width, this.height);
				}
				if(this.width == 18 && this.height == 18){
					this.drawTexturedModalRect(this.xPosition, this.yPosition, this.button_state_x[(identifier - 3) + 1], this.button_state_y[i + 3], this.width, this.height);
				}
			}
			
			if(identifier == 8){
				if(this.width == 20 && this.height == 20){
					this.drawTexturedModalRect(this.xPosition, this.yPosition, this.button_state_x[identifier - 2], this.button_state_y[i + 3], this.width, this.height);
				}
				if(this.width == 18 && this.height == 18){
					this.drawTexturedModalRect(this.xPosition, this.yPosition, this.button_state_x[(identifier - 2) + 1], this.button_state_y[i + 3], this.width, this.height);
				}
			}
			
			if(identifier == 9){
				if(this.width == 20 && this.height == 20){
					this.drawTexturedModalRect(this.xPosition, this.yPosition, this.button_state_x[identifier - 1], this.button_state_y[i + 3], this.width, this.height);
				}
				if(this.width == 18 && this.height == 18){
					this.drawTexturedModalRect(this.xPosition, this.yPosition, this.button_state_x[(identifier - 1) + 1], this.button_state_y[i + 3], this.width, this.height);
				}
			}
			
			/*if(identifier >= 10 && identifier < 15){
				if(this.width == 20 && this.height == 20){
					this.drawTexturedModalRect(this.xPosition, this.yPosition, this.button_state_x[identifier - identifier], this.button_state_y[i + 6], this.width, this.height);
				}
				if(this.width == 18 && this.height == 18){
					this.drawTexturedModalRect(this.xPosition, this.yPosition, this.button_state_x[(identifier - identifier) + 1], this.button_state_y[i + 6], this.width, this.height);
				}
			}*/
			
			this.mouseDragged(mc, mouseX, mouseY);
		}
	}

	protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) { }
	
	public void mouseReleased(int mouseX, int mouseY) { }
	
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition
				&& mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
	}
	
	public boolean isMouseOver() {
		return this.hovered;
	}

	public void drawButtonForegroundLayer(int mouseX, int mouseY) { }

	public void playPressSound(SoundHandler soundHandlerIn) {
		soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}

	public int getButtonWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
}