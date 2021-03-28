package com.tcn.dimensionalpocketsii.client.screen.widget;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.tcn.cosmoslibrary.common.comp.CosmosColour;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TomeButton extends Button {
	private ResourceLocation TEXTURE;
	
	protected int width;
	protected int height;
	public int x;
	public int y;
	private int colour;
	protected boolean isHovered;
	protected float alpha = 1.0F;
	protected long nextNarration = Long.MAX_VALUE;
	private int identifier;

	public TomeButton(int x, int y, int size, int identifier, int colour, ResourceLocation location, Button.IPressable pressedAction) {
		super(x, y, size, size, new StringTextComponent(""), pressedAction);
		this.x = x;
		this.y = y;
		this.width = size;
		this.height = size;
		
		this.colour = colour;
		this.TEXTURE = location;
		this.identifier = identifier;
	}
	
	public TomeButton(int x, int y, int colour, ResourceLocation location, Button.IPressable pressedAction) {
		super(x, y, 15, 25, new StringTextComponent(""), pressedAction);
		this.x = x;
		this.y = y;
		this.width = 15;
		this.height = 25;
		this.colour = colour;
		this.TEXTURE = location;
		this.identifier = -1;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float ticks) {
		if (this.visible) {
			Minecraft.getInstance().getTextureManager().bind(this.TEXTURE);
			
			this.setFGColor(this.colour);
			
			float[] rgb = CosmosColour.rgbFloatArray(this.colour);
			
			GL11.glColor4f(rgb[0], rgb[1], rgb[2], 1.0F);

			this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

			GlStateManager._enableBlend();
			GlStateManager._blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value, GlStateManager.SourceFactor.ONE.value, GlStateManager.DestFactor.ZERO.value);
			GlStateManager._blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value);

			if (this.isHovered) {
				if (this.identifier == 0) {
					this.blit(matrixStack, this.x, this.y, 243, 216, this.width, this.height);
				} else if (this.identifier == 1) {
					this.blit(matrixStack, this.x, this.y, 230, 216, this.width, this.height);
				} else {
					this.blit(matrixStack, this.x, this.y, 241, 177, this.width, this.height);
				}
			} else {
				if (this.identifier == 0) {
					this.blit(matrixStack, this.x, this.y, 243, 203, this.width, this.height);
				} else if (this.identifier == 1) {
					this.blit(matrixStack, this.x, this.y, 230, 203, this.width, this.height);
				} else {
					this.blit(matrixStack, this.x, this.y, 241, 152, this.width, this.height);
				}
			}
		}
	}

	@Override
	public void onPress() {
		this.onPress.onPress(this);
	}

	@Override
	public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
	}

	protected int getHoverState(boolean mouseOver) {
		int i = 0;

		if (!this.active) {
			i = 2;
		} else if (mouseOver) {
			i = 1;
		}
		return i;
	}
}