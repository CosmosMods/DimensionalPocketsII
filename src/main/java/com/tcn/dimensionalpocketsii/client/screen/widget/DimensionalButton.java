package com.tcn.dimensionalpocketsii.client.screen.widget;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.tcn.cosmoslibrary.CosmosReference;
import com.tcn.dimensionalpocketsii.DimReference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("unused")
public class DimensionalButton extends Button {
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

	public DimensionalButton(int x, int y, int size, boolean enabled, boolean visible, int identifier, ITextComponent title, Button.IPressable pressedAction) {
		super(x, y, size, size, title, pressedAction);
		this.x = x;
		this.y = y;
		this.width = size;
		this.height = size;
		this.message = title;

		this.active = enabled;
		this.visible = visible;
		this.identifier = identifier;
	}

	public DimensionalButton(int x, int y, boolean enabled, boolean visible, int identifier, ITextComponent title, Button.IPressable pressedAction) {
		this(x, y, 20, enabled, visible, identifier, title, pressedAction);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float ticks) {
		if (this.visible) {
			Minecraft.getInstance().getTextureManager().bind(DimReference.GUI.RESOURCE.GUI_DIMENSIONAL_BUTTON);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int i = this.getHoverState(this.isHovered);

			GlStateManager._enableBlend();
			GlStateManager._blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value, GlStateManager.SourceFactor.ONE.value, GlStateManager.DestFactor.ZERO.value);
			GlStateManager._blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value);

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
			} else if (this.identifier > 17 && this.identifier <= 23) {
				if (this.width == 20 && this.height == 20) {
					this.blit(matrixStack, this.x, this.y, CosmosReference.RESOURCE.INFO.BUTTON_STATE_X[identifier - 18], CosmosReference.RESOURCE.INFO.BUTTON_STATE_Y[i + 9], this.width, this.height);
				} else if (this.width == 18 && this.height == 18) {
					this.blit(matrixStack, this.x, this.y, CosmosReference.RESOURCE.INFO.BUTTON_STATE_X_SMALL[identifier - 18], CosmosReference.RESOURCE.INFO.BUTTON_STATE_Y_SMALL[i + 9], this.width, this.height);
				}
			} else if (this.identifier > 23) {
				Minecraft.getInstance().getTextureManager().bind(DimReference.GUI.RESOURCE.GUI_DIMENSIONAL_BUTTON_0);
				
				if (this.identifier <= 28) {
					if (this.width == 20 && this.height == 20) {
						this.blit(matrixStack, this.x, this.y, CosmosReference.RESOURCE.INFO.BUTTON_STATE_X[identifier - 24], CosmosReference.RESOURCE.INFO.BUTTON_STATE_Y[i], this.width, this.height);
					} else if (this.width == 18 && this.height == 18) {
						this.blit(matrixStack, this.x, this.y, CosmosReference.RESOURCE.INFO.BUTTON_STATE_X_SMALL[identifier - 24], CosmosReference.RESOURCE.INFO.BUTTON_STATE_Y_SMALL[i], this.width, this.height);
					}
				} else if (this.identifier > 28 && this.identifier <= 34) {
					if (this.width == 20 && this.height == 20) {
						this.blit(matrixStack, this.x, this.y, CosmosReference.RESOURCE.INFO.BUTTON_STATE_X[identifier - 30], CosmosReference.RESOURCE.INFO.BUTTON_STATE_Y[i + 3], this.width, this.height);
					} else if (this.width == 18 && this.height == 18) {
						this.blit(matrixStack, this.x, this.y, CosmosReference.RESOURCE.INFO.BUTTON_STATE_X_SMALL[identifier - 30], CosmosReference.RESOURCE.INFO.BUTTON_STATE_Y_SMALL[i + 3], this.width, this.height);
					}
				} else if (this.identifier > 34 && this.identifier <= 40) {
					if (this.width == 20 && this.height == 20) {
						this.blit(matrixStack, this.x, this.y, CosmosReference.RESOURCE.INFO.BUTTON_STATE_X[identifier - 35], CosmosReference.RESOURCE.INFO.BUTTON_STATE_Y[i + 6], this.width, this.height);
					} else if (this.width == 18 && this.height == 18) {
						this.blit(matrixStack, this.x, this.y, CosmosReference.RESOURCE.INFO.BUTTON_STATE_X_SMALL[identifier - 35], CosmosReference.RESOURCE.INFO.BUTTON_STATE_Y_SMALL[i + 6], this.width, this.height);
					}
				} else if (this.identifier > 40 && this.identifier <= 46) {
					if (this.width == 20 && this.height == 20) {
						this.blit(matrixStack, this.x, this.y, CosmosReference.RESOURCE.INFO.BUTTON_STATE_X[identifier - 41], CosmosReference.RESOURCE.INFO.BUTTON_STATE_Y[i + 9], this.width, this.height);
					} else if (this.width == 18 && this.height == 18) {
						this.blit(matrixStack, this.x, this.y, CosmosReference.RESOURCE.INFO.BUTTON_STATE_X_SMALL[identifier - 41], CosmosReference.RESOURCE.INFO.BUTTON_STATE_Y_SMALL[i + 9], this.width, this.height);
					}
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