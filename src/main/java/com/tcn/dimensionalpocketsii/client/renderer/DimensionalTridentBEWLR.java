package com.tcn.dimensionalpocketsii.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tcn.dimensionalpocketsii.client.renderer.model.DimensionalTridentModel;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;

@OnlyIn(Dist.CLIENT)
public class DimensionalTridentBEWLR extends BlockEntityWithoutLevelRenderer {
	
	public final static BlockEntityWithoutLevelRenderer INSTANCE = new DimensionalTridentBEWLR();

	private final DimensionalTridentModel tridentModel = new DimensionalTridentModel();

	public DimensionalTridentBEWLR() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderByItem(ItemStack stackIn, ItemTransforms.TransformType transformIn, PoseStack matrixStack, MultiBufferSource typeBuffer, int combinedLight, int combinedOverlay) {
		Item item = stackIn.getItem();
		BakedModel model = null;
		Minecraft mc = Minecraft.getInstance();
		ItemRenderer renderer = mc.getItemRenderer();

		if (item == ModBusManager.DIMENSIONAL_TRIDENT) {
			
			boolean flag = transformIn == ItemTransforms.TransformType.GUI || transformIn == ItemTransforms.TransformType.GROUND || transformIn == ItemTransforms.TransformType.FIXED;
			
			if (flag) {
				model = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation("dimensionalpocketsii:dimensional_trident#inventory"));

				matrixStack.pushPose();
				renderer.render(stackIn, transformIn, true, matrixStack, typeBuffer, combinedLight, combinedOverlay, model);
				matrixStack.popPose();
			} else {
				matrixStack.pushPose();
				matrixStack.scale(1.0F, -1.0F, -1.0F);
				VertexConsumer ivertexbuilder1 = ItemRenderer.getFoilBufferDirect(typeBuffer, this.tridentModel.renderType(DimensionalTridentModel.TEXTURE), false, stackIn.hasFoil());
				this.tridentModel.renderToBuffer(matrixStack, ivertexbuilder1, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
				matrixStack.popPose();
			}
		} else if (item == ModBusManager.DIMENSIONAL_TRIDENT_ENHANCED) {
			
			boolean flag = transformIn == ItemTransforms.TransformType.GUI || transformIn == ItemTransforms.TransformType.GROUND || transformIn == ItemTransforms.TransformType.FIXED;
			
			if (flag) {
				model = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation("dimensionalpocketsii:dimensional_trident_enhanced#inventory"));

				matrixStack.pushPose();
				renderer.render(stackIn, transformIn, true, matrixStack, typeBuffer, combinedLight, combinedOverlay, model);
				matrixStack.popPose();
			} else {
				matrixStack.pushPose();
				matrixStack.scale(1.0F, -1.0F, -1.0F);
				VertexConsumer ivertexbuilder1 = ItemRenderer.getFoilBufferDirect(typeBuffer, this.tridentModel.renderType(DimensionalTridentModel.TEXTURE_ENHANCED), false, stackIn.hasFoil());
				this.tridentModel.renderToBuffer(matrixStack, ivertexbuilder1, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
				matrixStack.popPose();
			}
		}
	}
	

	@OnlyIn(Dist.CLIENT)
	public void render(ItemRenderer renderer, ItemStack stackIn, ItemTransforms.TransformType transformIn, boolean p_229111_3_, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn, BakedModel modelIn, boolean renderFoil) {
		if (!stackIn.isEmpty()) {
			matrixStackIn.pushPose();
			boolean flag = transformIn == ItemTransforms.TransformType.GUI || transformIn == ItemTransforms.TransformType.GROUND || transformIn == ItemTransforms.TransformType.FIXED;
			
			modelIn = ForgeHooksClient.handleCameraTransforms(matrixStackIn, modelIn, transformIn, p_229111_3_);
			matrixStackIn.translate(-0.5D, -0.5D, -0.5D);
			
			if ((stackIn.getItem() != Items.TRIDENT || flag)) {
				boolean flag1;
				if (transformIn != ItemTransforms.TransformType.GUI && !transformIn.firstPerson() && stackIn.getItem() instanceof BlockItem) {
					Block block = ((BlockItem) stackIn.getItem()).getBlock();
					flag1 = !(block instanceof HalfTransparentBlock) && !(block instanceof StainedGlassPaneBlock);
				} else {
					flag1 = true;
				}
				
				if (modelIn.isLayered()) {
					ForgeHooksClient.drawItemLayered(renderer, modelIn, stackIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, flag1);
				} else {
					RenderType rendertype = ItemBlockRenderTypes.getRenderType(stackIn, flag1);
					VertexConsumer ivertexbuilder;
					
					if (flag1) {
						if (renderFoil) {
							ivertexbuilder = ItemRenderer.getFoilBufferDirect(bufferIn, rendertype, true, stackIn.hasFoil());
						} else {
							ivertexbuilder = ItemRenderer.getFoilBufferDirect(bufferIn, rendertype, true, false);
						}
					} else {
						if (renderFoil) {
							ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, rendertype, true, stackIn.hasFoil());
						} else {
							ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, rendertype, true, false);
						}
					}
					
					
					renderer.renderModelLists(modelIn, stackIn, combinedLightIn, combinedOverlayIn, matrixStackIn, ivertexbuilder);
				}
			}

			matrixStackIn.popPose();
		}
	}
}