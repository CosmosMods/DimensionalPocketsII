package com.zeher.zehercraft.processing.client.tesr.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCompactorInternals extends ModelBase { 
	
	ModelRenderer lefthead;
	ModelRenderer leftshaft;
	ModelRenderer righthead;
	ModelRenderer rightshaft;

	public ModelCompactorInternals() {
		textureWidth = 32;
		textureHeight = 32;

		lefthead = new ModelRenderer(this, 0, 7);
		lefthead.addBox(4.5F, 12F, -2F, 1, 4, 4);
		lefthead.setRotationPoint(0F, 0F, 0F);
		lefthead.setTextureSize(128, 64);
		lefthead.mirror = true;
		setRotation(lefthead, 0F, 0F, 0F);
		leftshaft = new ModelRenderer(this, 11, 0);
		leftshaft.addBox(4.75F, 13.5F, -0.5F, 3, 1, 1);
		leftshaft.setRotationPoint(0F, 0F, 0F);
		leftshaft.setTextureSize(128, 64);
		leftshaft.mirror = true;
		setRotation(leftshaft, 0F, 0F, 0F);
		
		righthead = new ModelRenderer(this, 0, 7);
		righthead.addBox(-5.5F, 12F, -2F, 1, 4, 4);
		righthead.setRotationPoint(0F, 0F, 0F);
		righthead.setTextureSize(128, 64);
		righthead.mirror = true;
		setRotation(righthead, 0F, 0F, 0F);
		rightshaft = new ModelRenderer(this, 11, 0);
		rightshaft.addBox(-7.8F, 13.5F, -0.5F, 3, 1, 1);
		rightshaft.setRotationPoint(0F, 0F, 0F);
		rightshaft.setTextureSize(128, 64);
		rightshaft.mirror = true;
		setRotation(rightshaft, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		lefthead.render(f5);
		leftshaft.render(f5);
		righthead.render(f5);
		rightshaft.render(f5);
	}
	
	public void renderRightArm(float f5) {
		righthead.render(f5);
		rightshaft.render(f5);
	}
	
	public void renderLeftArm(float f5) {
		lefthead.render(f5);
		leftshaft.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}
}