package com.tcn.dimensionalpocketsii.pocket.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tcn.cosmoslibrary.client.util.ScreenUtil.DRAW;
import com.tcn.cosmoslibrary.common.comp.CosmosColour;
import com.tcn.dimensionalpocketsii.DimReference.GUI;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerCrafter;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityCrafter;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class ScreenCrafter extends ContainerScreen<ContainerCrafter> {

	public ScreenCrafter(ContainerCrafter containerIn, PlayerInventory playerInventoryIn, ITextComponent titleIn) {
		super(containerIn, playerInventoryIn, titleIn);
		
		this.imageWidth = 184;
		this.imageHeight = 177;
	}

	@Override
	protected void init() {
		super.init();
		this.titleLabelX = 29;
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int posX, int posY) {
		int[] screen_coords = new int[] { ((this.width - this.imageWidth) / 2), (this.height - this.imageHeight) / 2};
		
		ContainerCrafter container = this.menu;
		World world = container.getWorld();
		BlockPos pos = container.getBlockPos();
		TileEntity tile = world.getBlockEntity(pos);
		
		DRAW.drawStaticElement(this, matrixStack, screen_coords, 0, 0, 0, 0, this.imageWidth, this.imageHeight, GUI.RESOURCE.CRAFTER_BACKGROUND);
		
		if (tile instanceof TileEntityCrafter) {
			TileEntityCrafter tile_crafter = (TileEntityCrafter) tile;
			
			if (tile_crafter.getPocket() != null) {
				Pocket pocket = tile_crafter.getPocket();
				
				int decimal = pocket.getDisplayColour();
				CosmosColour colour = CosmosColour.col(decimal);
				float[] rgb = null;
				
				if (colour.equals(CosmosColour.POCKET_PURPLE)) {
					rgb = CosmosColour.rgbFloatArray(CosmosColour.POCKET_PURPLE_LIGHT.dec());
				} else {
					rgb = CosmosColour.rgbFloatArray(decimal);
				}
				
				RenderSystem.color4f(rgb[0], rgb[1], rgb[2], 1.0F);
				DRAW.drawStaticElement(this, matrixStack, screen_coords, 0, 0, 0, 0, this.imageWidth, this.imageHeight, GUI.RESOURCE.CRAFTER_BASE);

				
			}
		}

		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		DRAW.drawStaticElement(this, matrixStack, screen_coords, 0, 0, 0, 0, this.imageWidth, this.imageHeight, GUI.RESOURCE.CRAFTER_OVERLAY);
	}

	@Override
	protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
		this.font.draw(matrixStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY - 2, 4210752);
		this.font.draw(matrixStack, this.inventory.getDisplayName(), (float) this.inventoryLabelX, (float) this.inventoryLabelY + 2, 4210752);
	}

	@Override
	protected boolean isHovering(int positionX, int positionY, int width, int height, double mouseX, double mouseY) {
		return super.isHovering(positionX, positionY, width, height, mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void slotClicked(Slot slotIn, int mouseX, int mouseY, ClickType clickTypeIn) {
		super.slotClicked(slotIn, mouseX, mouseY, clickTypeIn);
	}
}