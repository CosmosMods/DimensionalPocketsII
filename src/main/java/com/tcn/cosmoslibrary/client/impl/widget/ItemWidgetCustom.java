package com.tcn.cosmoslibrary.client.impl.widget;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.tcn.cosmoslibrary.CosmosReference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;

public class ItemWidgetCustom extends Widget {
	
	public int width;
	public int height;

	public int xPosition;
	public int yPosition;

	public ITextComponent displayString;

	public boolean enabled;
	public boolean visible;

	protected boolean hovered;

	public int packedFGColour;
	
	public ItemWidgetCustom(int x, int y, int widthIn, int heightIn, ITextComponent buttonText) {
		super(x, y, widthIn, heightIn, buttonText);
		this.width = widthIn;
		this.height = heightIn;
		this.enabled = true;
		this.visible = true;
		this.xPosition = x;
		this.yPosition = y;
		this.width = widthIn;
		this.height = heightIn;
		this.displayString = buttonText;
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
	
	public void drawButton(MatrixStack stack, int mouseX, int mouseY, float ticks) {
		if (this.visible) {
			Minecraft minecraft = Minecraft.getInstance();
			FontRenderer fontrenderer = minecraft.fontRenderer;
			
			minecraft.getTextureManager().bindTexture(CosmosReference.RESOURCE.BASE.GUI_ITEM_BUTTON_CUSTOM_LOC);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
			int i = this.getHoverState(this.hovered);
			
			GlStateManager.enableBlend();
			GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param);

			this.blit(stack, this.xPosition, this.yPosition, 56, 0 + i * 20, this.width / 2, this.height);
			this.blit(stack, this.xPosition + this.width / 2, this.yPosition, 256 - this.width / 2, 0 + i * 20, this.width / 2, this.height);
			
			int j = 14737632;

			if (packedFGColour != 0) {
				j = packedFGColour;
			} else if (!this.enabled) {
				j = 10526880;
			} else if (this.hovered) {
				j = 16777120;
			}

			drawCenteredString(stack, fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
		}
	}
	
	
	@Override
	protected boolean clicked(double p_clicked_1_, double p_clicked_3_) {
		return this.active && this.visible && p_clicked_1_ >= (double) this.x && p_clicked_3_ >= (double) this.y
				&& p_clicked_1_ < (double) (this.x + this.width) && p_clicked_3_ < (double) (this.y + this.height);
	}

	@Override
	public boolean isMouseOver(double p_isMouseOver_1_, double p_isMouseOver_3_) {
		return this.active && this.visible && p_isMouseOver_1_ >= (double) this.x && p_isMouseOver_3_ >= (double) this.y
				&& p_isMouseOver_1_ < (double) (this.x + this.width)
				&& p_isMouseOver_3_ < (double) (this.y + this.height);
	}

	@Override
	public void render(MatrixStack stack, int mouseX, int mouseY, float ticks) { }

	@Override
	public void playDownSound(SoundHandler sound_handler) {
		sound_handler.play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}
}