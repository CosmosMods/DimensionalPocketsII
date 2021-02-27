package com.zhr.dimensionalpocketsii.client.screen;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.zhr.cosmoslibrary.CosmosReference;
import com.zhr.dimensionalpocketsii.DimReference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("unused")
public class DimensionalWidget extends Widget {
	protected int width;
	protected int height;
	public int x;
	public int y;
	private ITextComponent message;
	private boolean wasHovered;
	protected boolean isHovered;
	public boolean active = true;
	public boolean visible = true;
	protected float alpha = 1.0F;
	protected long nextNarration = Long.MAX_VALUE;
	private boolean focused;
	private int identifier;
	
	public DimensionalWidget(int x, int y, int width, int height, ITextComponent title, boolean active, boolean visible, int identifier) {
		super(x, y, width, height, title);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.message = title;
		
		this.active = active;
		this.visible = visible;
		this.identifier = identifier;
	}

	public DimensionalWidget(int x, int y, ITextComponent title, boolean enabled, boolean visible, int identifier) {
		this(x, y, 20, 20, title, enabled, visible, identifier);
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
	
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float ticks) {
		if (this.visible) {
			//FontRenderer fontrenderer = mc.fontRenderer;
			Minecraft minecraft = Minecraft.getInstance();
			minecraft.getTextureManager().bindTexture(DimReference.GUI.RESOURCE.GUI_DIMENSIONAL_BUTTON);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int i = this.getHoverState(this.isHovered);
			
			GlStateManager.enableBlend();
			GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param);
			
			if (this.identifier <= 5) {
				if (this.width == 20 && this.height == 20) {
					this.blit(matrixStack, this.x, this.y, CosmosReference.RESOURCE.INFO.BUTTON_STATE_X[identifier], CosmosReference.RESOURCE.INFO.BUTTON_STATE_Y[i], this.width, this.height);
				} else if (this.width == 18 && this.height == 18) {
					this.blit(matrixStack, this.x, this.y, CosmosReference.RESOURCE.INFO.BUTTON_STATE_X_SMALL[identifier], CosmosReference.RESOURCE.INFO.BUTTON_STATE_Y_SMALL[i], this.width, this.height);
				}
			} else if (this.identifier > 5 && this.identifier <= 11) {
				if (this.width == 20 && this.height == 20) {
					this.blit(matrixStack, this.x, this.y, CosmosReference.RESOURCE.INFO.BUTTON_STATE_X[identifier - 6], CosmosReference.RESOURCE.INFO.BUTTON_STATE_Y[i + 3], this.width, this.height);
				} else if (this.width == 18 && this.height == 18) {
					this.blit(matrixStack, this.x, this.y, CosmosReference.RESOURCE.INFO.BUTTON_STATE_X_SMALL[identifier - 6], CosmosReference.RESOURCE.INFO.BUTTON_STATE_Y_SMALL[i + 3], this.width, this.height);
				}
			} else if (this.identifier > 11 && this.identifier <= 17) {
				if (this.width == 20 && this.height == 20) {
					this.blit(matrixStack, this.x, this.y, CosmosReference.RESOURCE.INFO.BUTTON_STATE_X[identifier - 12], CosmosReference.RESOURCE.INFO.BUTTON_STATE_Y[i + 6], this.width, this.height);
				} else if (this.width == 18 && this.height == 18) {
					this.blit(matrixStack, this.x, this.y, CosmosReference.RESOURCE.INFO.BUTTON_STATE_X_SMALL[identifier - 12], CosmosReference.RESOURCE.INFO.BUTTON_STATE_Y_SMALL[i + 6], this.width, this.height);
				}
			}  else if (this.identifier > 17 && this.identifier <= 23) {
				if (this.width == 20 && this.height == 20) {
					this.blit(matrixStack, this.x, this.y, CosmosReference.RESOURCE.INFO.BUTTON_STATE_X[identifier - 18], CosmosReference.RESOURCE.INFO.BUTTON_STATE_Y[i + 9], this.width, this.height);
				} else if (this.width == 18 && this.height == 18) {
					this.blit(matrixStack, this.x, this.y, CosmosReference.RESOURCE.INFO.BUTTON_STATE_X_SMALL[identifier - 18], CosmosReference.RESOURCE.INFO.BUTTON_STATE_Y_SMALL[i + 9], this.width, this.height);
				}
			}
			
			this.mouseDragged(minecraft, mouseX, mouseY);
		}
	}

	protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) { }
	
	public void mouseReleased(int mouseX, int mouseY) { }
	
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		return this.active && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
	}
	
	public boolean isMouseOver() {
		return this.isHovered;
	}

	public void drawButtonForegroundLayer(int mouseX, int mouseY) { }

	public void playPressSound(SoundHandler soundHandlerIn) {
		//soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}

}