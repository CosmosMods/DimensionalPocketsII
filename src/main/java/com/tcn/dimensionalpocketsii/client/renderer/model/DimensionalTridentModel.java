package com.tcn.dimensionalpocketsii.client.renderer.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tcn.dimensionalpocketsii.DimensionalPockets;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DimensionalTridentModel extends Model {
	public static final ResourceLocation TEXTURE = new ResourceLocation(DimensionalPockets.MOD_ID, "textures/entity/dimensional_trident.png");
	private final ModelRenderer pole = new ModelRenderer(32, 32, 0, 6);

	public DimensionalTridentModel() {
		super(RenderType::entitySolid);
		this.pole.addBox(-0.5F, 3.0F, -0.5F, 1.0F, 25.0F, 1.0F, 0.0F);
		
		ModelRenderer coreBox = new ModelRenderer(32, 32, 4, 0);
		coreBox.addBox(-1.5F, 0.0F, -0.5F, 3.0F, 3.0F, 1.0F);
		this.pole.addChild(coreBox);
		
		ModelRenderer sideProng1 = new ModelRenderer(32, 32, 4, 4);
		sideProng1.addBox(-2.5F, -3.0F, -0.5F, 1.0F, 5.0F, 1.0F);
		this.pole.addChild(sideProng1);
		
		ModelRenderer middleProng = new ModelRenderer(32, 32, 0, 0);
		middleProng.addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F);
		this.pole.addChild(middleProng);
		
		ModelRenderer sideProng2 = new ModelRenderer(32, 32, 8, 4);
		sideProng2.addBox(1.5F, -3.0F, -0.5F, 1.0F, 5.0F, 1.0F);
		this.pole.addChild(sideProng2);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float redIn, float greenIn, float blueIn, float alphaIn) {
		this.pole.render(matrixStack, vertexBuilder, packedLightIn, packedOverlayIn, redIn, greenIn, blueIn, alphaIn);
	}
}
