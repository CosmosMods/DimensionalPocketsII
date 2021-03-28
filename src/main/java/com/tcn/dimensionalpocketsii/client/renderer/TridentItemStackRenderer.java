package com.tcn.dimensionalpocketsii.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tcn.dimensionalpocketsii.client.renderer.model.DimensionalTridentModel;
import com.tcn.dimensionalpocketsii.core.management.CoreModBusManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TridentItemStackRenderer extends ItemStackTileEntityRenderer {
	private final DimensionalTridentModel tridentModel = new DimensionalTridentModel();

	@Override
	public void renderByItem(ItemStack stackIn, ItemCameraTransforms.TransformType transformIn, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int combinedLight, int combinedOverlay) {
		Item item = stackIn.getItem();
		ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
		IBakedModel model = null;

		if (item == CoreModBusManager.DIMENSIONAL_TRIDENT) {
			boolean flag = transformIn == ItemCameraTransforms.TransformType.GUI || transformIn == ItemCameraTransforms.TransformType.GROUND || transformIn == ItemCameraTransforms.TransformType.FIXED;
			if (flag) {
				model = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation("dimensionalpocketsii:dimensional_trident#inventory")).getBakedModel();
				
				matrixStack.pushPose();
				renderer.render(stackIn, transformIn, true, matrixStack, typeBuffer, combinedLight, combinedOverlay, model);
				matrixStack.popPose();
				
			} else {
				matrixStack.pushPose();
				matrixStack.scale(1.0F, -1.0F, -1.0F);
				IVertexBuilder ivertexbuilder1 = ItemRenderer.getFoilBufferDirect(typeBuffer, this.tridentModel.renderType(DimensionalTridentModel.TEXTURE), false, stackIn.hasFoil());
				this.tridentModel.renderToBuffer(matrixStack, ivertexbuilder1, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
				matrixStack.popPose();
			}
		}
	}
}
