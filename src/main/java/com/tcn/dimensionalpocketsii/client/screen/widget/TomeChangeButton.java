package com.tcn.dimensionalpocketsii.client.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class TomeChangeButton extends Button {
	private final boolean isForward;
	private final boolean playTurnSound;

	private ResourceLocation texture;

	public TomeChangeButton(int x, int y, boolean isForward, Button.IPressable onPress, boolean playTurnSound, ResourceLocation location) {
		super(x, y, 25, 13, StringTextComponent.EMPTY, onPress);
		this.isForward = isForward;
		this.playTurnSound = playTurnSound;

		this.texture = location;
	}

	public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getInstance().getTextureManager().bind(texture);
		int i = 206;
		int j = 230;

		if (this.isHovered()) {
			i += 25;
		}

		if (!this.isForward) {
			j += 13;
		}

		this.blit(matrixStack, this.x, this.y, i, j, 25, 13);
	}

	public void playDownSound(SoundHandler handler) {
		if (this.playTurnSound) {
			handler.play(SimpleSound.forUI(SoundEvents.BOOK_PAGE_TURN, 5F));
		}

	}
}
