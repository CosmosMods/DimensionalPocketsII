package com.tcn.dimensionalpocketsii.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tcn.dimensionalpocketsii.core.item.armour.DimensionalElytraplate;
import com.tcn.dimensionalpocketsii.core.item.armour.module.BaseElytraModule;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
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
public class ElytraplateBEWLR extends BlockEntityWithoutLevelRenderer {
	
	public final static BlockEntityWithoutLevelRenderer INSTANCE = new ElytraplateBEWLR();

	public ElytraplateBEWLR() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderByItem(ItemStack stackIn, ItemTransforms.TransformType transformIn, PoseStack poseStack, MultiBufferSource typeBuffer, int combinedLight, int combinedOverlay) {
		Item item = stackIn.getItem();
		ResourceLocation itemLocation = item.getRegistryName();
		Minecraft mc = Minecraft.getInstance();
		ItemRenderer renderer = mc.getItemRenderer();
		ModelManager manager = mc.getModelManager();
		
		boolean flag = transformIn == ItemTransforms.TransformType.GUI;
		
		if (flag) {
			if (item.equals(ModBusManager.DIMENSIONAL_ELYTRAPLATE)) {
				poseStack.pushPose();
				ResourceLocation resBase = new ResourceLocation(itemLocation.getNamespace(), "item/" + itemLocation.getPath() + "_base");
				BakedModel itemModelBase = manager.getModel(resBase);
				
				boolean foil = true;
				
				poseStack.translate(0.5F, 0.5F, 0.0F);
				if (DimensionalElytraplate.hasModuleInstalled(stackIn, BaseElytraModule.SCREEN)) {
					BakedModel itemModel = manager.getModel(new ResourceLocation(itemLocation.getNamespace(), "item/" + itemLocation.getPath() + "_connect"));
					
					poseStack.pushPose();
					poseStack.translate(0.0F, 0.0F, 0.001F);
					this.render(renderer, stackIn, transformIn, false, poseStack, typeBuffer, combinedLight, combinedOverlay, itemModel, foil);
					poseStack.popPose();
					
					foil = false;
				}

				if (DimensionalElytraplate.hasModuleInstalled(stackIn, BaseElytraModule.SHIFTER)) {
					BakedModel itemModel = manager.getModel(new ResourceLocation(itemLocation.getNamespace(), "item/" + itemLocation.getPath() + "_shifter"));
					
					poseStack.pushPose();
					poseStack.translate(0.0F, 0.0F, 0.001F);
					this.render(renderer, stackIn, transformIn, false, poseStack, typeBuffer, combinedLight, combinedOverlay, itemModel, foil);
					poseStack.popPose();
					
					foil = false;
				}

				if (DimensionalElytraplate.hasModuleInstalled(stackIn, BaseElytraModule.VISOR)) {
					BakedModel itemModel = manager.getModel(new ResourceLocation(itemLocation.getNamespace(), "item/" + itemLocation.getPath() + "_visor"));
					
					poseStack.pushPose();
					poseStack.translate(0.0F, 0.0F, 0.001F);
					this.render(renderer, stackIn, transformIn, false, poseStack, typeBuffer, combinedLight, combinedOverlay, itemModel, foil);
					poseStack.popPose();
					
					foil = false;
				}
				
				if (DimensionalElytraplate.hasModuleInstalled(stackIn, BaseElytraModule.SOLAR)) {
					BakedModel itemModel = manager.getModel(new ResourceLocation(itemLocation.getNamespace(), "item/" + itemLocation.getPath() + "_solar"));
					
					poseStack.pushPose();
					poseStack.translate(0.0F, 0.0F, 0.001F);
					this.render(renderer, stackIn, transformIn, false, poseStack, typeBuffer, combinedLight, combinedOverlay, itemModel, foil);
					poseStack.popPose();
					
					foil = false;
				}
				
				if (DimensionalElytraplate.hasModuleInstalled(stackIn, BaseElytraModule.BATTERY)) {
					BakedModel itemModel = manager.getModel(new ResourceLocation(itemLocation.getNamespace(), "item/" + itemLocation.getPath() + "_battery"));
					
					poseStack.pushPose();
					poseStack.translate(0.0F, 0.0F, 0.001F);
					this.render(renderer, stackIn, transformIn, false, poseStack, typeBuffer, combinedLight, combinedOverlay, itemModel, foil);
					poseStack.popPose();
					
					foil = false;
				}
				
				poseStack.pushPose();
				this.render(renderer, stackIn, transformIn, false, poseStack, typeBuffer, combinedLight, combinedOverlay, itemModelBase, true);
				poseStack.popPose();
				
				poseStack.popPose();
			}
		} else {
			poseStack.pushPose();
			BakedModel model = mc.getModelManager().getModel(new ResourceLocation(itemLocation.getNamespace(), "item/" + itemLocation.getPath() + "_base"));
			
			boolean flag1 = true;

			if (model.isLayered()) {

			} else {
				RenderType rendertype = ItemBlockRenderTypes.getRenderType(stackIn, flag1);
				VertexConsumer ivertexbuilder;
				
				boolean foil = true;
				ivertexbuilder = ItemRenderer.getFoilBufferDirect(typeBuffer, rendertype, true, stackIn.hasFoil());
				
				if (DimensionalElytraplate.hasModuleInstalled(stackIn, BaseElytraModule.SCREEN)) {
					BakedModel itemModel = manager.getModel(new ResourceLocation(itemLocation.getNamespace(), "item/" + itemLocation.getPath() + "_connect"));

					VertexConsumer consumer = transformIn.firstPerson() ? ItemRenderer.getFoilBuffer(typeBuffer, rendertype, true, foil ? stackIn.hasFoil() : false) : ItemRenderer.getFoilBufferDirect(typeBuffer, rendertype, true, foil ? stackIn.hasFoil() : false);
					
					poseStack.pushPose();
					poseStack.translate(0.0F, 0.0F, 0.001F);
					renderer.renderModelLists(itemModel, stackIn, combinedLight, combinedOverlay, poseStack, consumer);
					poseStack.popPose();

					poseStack.pushPose();
					poseStack.translate(0.0F, 0.0F, -0.001F);
					renderer.renderModelLists(itemModel, stackIn, combinedLight, combinedOverlay, poseStack, consumer);
					poseStack.popPose();

					foil = false;
				}

				if (DimensionalElytraplate.hasModuleInstalled(stackIn, BaseElytraModule.SHIFTER)) {
					BakedModel itemModel = manager.getModel(new ResourceLocation(itemLocation.getNamespace(), "item/" + itemLocation.getPath() + "_shifter"));

					VertexConsumer consumer = transformIn.firstPerson() ? ItemRenderer.getFoilBuffer(typeBuffer, rendertype, true, foil ? stackIn.hasFoil() : false) : ItemRenderer.getFoilBufferDirect(typeBuffer, rendertype, true, foil ? stackIn.hasFoil() : false);
					
					poseStack.pushPose();
					poseStack.translate(0.0F, 0.0F, 0.001F);
					renderer.renderModelLists(itemModel, stackIn, combinedLight, combinedOverlay, poseStack, consumer);
					poseStack.popPose();

					poseStack.pushPose();
					poseStack.translate(0.0F, 0.0F, -0.001F);
					renderer.renderModelLists(itemModel, stackIn, combinedLight, combinedOverlay, poseStack, consumer);
					poseStack.popPose();

					foil = false;
				}

				if (DimensionalElytraplate.hasModuleInstalled(stackIn, BaseElytraModule.VISOR)) {
					BakedModel itemModel = manager.getModel(new ResourceLocation(itemLocation.getNamespace(), "item/" + itemLocation.getPath() + "_visor"));

					VertexConsumer consumer = transformIn.firstPerson() ? ItemRenderer.getFoilBuffer(typeBuffer, rendertype, true, foil ? stackIn.hasFoil() : false) : ItemRenderer.getFoilBufferDirect(typeBuffer, rendertype, true, foil ? stackIn.hasFoil() : false);
					
					poseStack.pushPose();
					poseStack.translate(0.0F, 0.0F, 0.001F);
					renderer.renderModelLists(itemModel, stackIn, combinedLight, combinedOverlay, poseStack, consumer);
					poseStack.popPose();

					poseStack.pushPose();
					poseStack.translate(0.0F, 0.0F, -0.001F);
					renderer.renderModelLists(itemModel, stackIn, combinedLight, combinedOverlay, poseStack, consumer);
					poseStack.popPose();

					foil = false;
				}
				
				if (DimensionalElytraplate.hasModuleInstalled(stackIn, BaseElytraModule.SOLAR)) {
					BakedModel itemModel = manager.getModel(new ResourceLocation(itemLocation.getNamespace(), "item/" + itemLocation.getPath() + "_solar"));

					VertexConsumer consumer = transformIn.firstPerson() ? ItemRenderer.getFoilBuffer(typeBuffer, rendertype, true, foil ? stackIn.hasFoil() : false) : ItemRenderer.getFoilBufferDirect(typeBuffer, rendertype, true, foil ? stackIn.hasFoil() : false);
					
					poseStack.pushPose();
					poseStack.translate(0.0F, 0.0F, 0.001F);
					renderer.renderModelLists(itemModel, stackIn, combinedLight, combinedOverlay, poseStack, consumer);
					poseStack.popPose();

					poseStack.pushPose();
					poseStack.translate(0.0F, 0.0F, -0.001F);
					renderer.renderModelLists(itemModel, stackIn, combinedLight, combinedOverlay, poseStack, consumer);
					poseStack.popPose();

					foil = false;
				}
				
				if (DimensionalElytraplate.hasModuleInstalled(stackIn, BaseElytraModule.BATTERY)) {
					BakedModel itemModel = manager.getModel(new ResourceLocation(itemLocation.getNamespace(), "item/" + itemLocation.getPath() + "_battery"));
					
					VertexConsumer consumer = transformIn.firstPerson() ? ItemRenderer.getFoilBuffer(typeBuffer, rendertype, true, foil ? stackIn.hasFoil() : false) : ItemRenderer.getFoilBufferDirect(typeBuffer, rendertype, true, foil ? stackIn.hasFoil() : false);
					
					poseStack.pushPose();
					poseStack.translate(0.0F, 0.0F, 0.001F);
					renderer.renderModelLists(itemModel, stackIn, combinedLight, combinedOverlay, poseStack, consumer);
					poseStack.popPose();

					poseStack.pushPose();
					poseStack.translate(0.0F, 0.0F, -0.001F);
					renderer.renderModelLists(itemModel, stackIn, combinedLight, combinedOverlay, poseStack, consumer);
					poseStack.popPose();

					foil = false;
				}
				
				renderer.renderModelLists(model, stackIn, combinedLight, combinedOverlay, poseStack, ivertexbuilder);
			}
			
			poseStack.popPose();
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void render(ItemRenderer renderer, ItemStack stackIn, ItemTransforms.TransformType transformIn, boolean p_229111_3_, PoseStack poseStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn, BakedModel modelIn, boolean renderFoil) {
		if (!stackIn.isEmpty()) {
			poseStackIn.pushPose();
			boolean flag = transformIn == ItemTransforms.TransformType.GUI || transformIn == ItemTransforms.TransformType.GROUND || transformIn == ItemTransforms.TransformType.FIXED;
			
			modelIn = ForgeHooksClient.handleCameraTransforms(poseStackIn, modelIn, transformIn, p_229111_3_);
			poseStackIn.translate(-0.5D, -0.5D, -0.5D);
			
			if ((stackIn.getItem() != Items.TRIDENT || flag)) {
				boolean flag1;
				if (transformIn != ItemTransforms.TransformType.GUI && !transformIn.firstPerson() && stackIn.getItem() instanceof BlockItem) {
					Block block = ((BlockItem) stackIn.getItem()).getBlock();
					flag1 = !(block instanceof HalfTransparentBlock) && !(block instanceof StainedGlassPaneBlock);
				} else {
					flag1 = true;
				}
				
				if (modelIn.isLayered()) {
					ForgeHooksClient.drawItemLayered(renderer, modelIn, stackIn, poseStackIn, bufferIn, combinedLightIn, combinedOverlayIn, flag1);
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
					
					
					renderer.renderModelLists(modelIn, stackIn, combinedLightIn, combinedOverlayIn, poseStackIn, ivertexbuilder);
				}
			}

			poseStackIn.popPose();
		}
	}
}
