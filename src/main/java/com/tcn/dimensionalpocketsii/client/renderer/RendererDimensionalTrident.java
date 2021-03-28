package com.tcn.dimensionalpocketsii.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.client.renderer.model.DimensionalTridentModel;
import com.tcn.dimensionalpocketsii.core.entity.DimensionalTridentEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RendererDimensionalTrident extends EntityRenderer<DimensionalTridentEntity> {
	public static final ResourceLocation TRIDENT_LOCATION = new ResourceLocation(DimensionalPockets.MOD_ID, "textures/entity/dimensional_trident.png");
	private final DimensionalTridentModel model = new DimensionalTridentModel();

	public RendererDimensionalTrident(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
	}

	@Override
	public void render(DimensionalTridentEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int packedLightIn) {
		matrixStack.pushPose();
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.yRotO, entityIn.yRot) - 90.0F));
		matrixStack.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.xRotO, entityIn.xRot) + 90.0F));
		IVertexBuilder ivertexbuilder = ItemRenderer.getFoilBufferDirect(typeBuffer, this.model.renderType(this.getTextureLocation(entityIn)), false, entityIn.isFoil());
		this.model.renderToBuffer(matrixStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStack.popPose();
		super.render(entityIn, entityYaw, partialTicks, matrixStack, typeBuffer, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(DimensionalTridentEntity entityIn) {
		return TRIDENT_LOCATION;
	}
}
