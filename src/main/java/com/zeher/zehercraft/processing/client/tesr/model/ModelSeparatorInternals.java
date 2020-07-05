package com.zeher.zehercraft.processing.client.tesr.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSeparatorInternals extends ModelBase {
	
	ModelRenderer arm1;
	ModelRenderer arm2;
	ModelRenderer arm3;
	ModelRenderer arm4;
	ModelRenderer arm5;
	ModelRenderer arm6;
	ModelRenderer arm7;
	ModelRenderer arm8;

	public ModelSeparatorInternals() {
		textureWidth = 32;
		textureHeight = 32;

		arm1 = new ModelRenderer(this, 0, 0);
		arm1.addBox(4F, 15F, -1F, 1, 4, 2);
		arm1.setRotationPoint(0F, 0F, 0F);
		arm1.setTextureSize(128, 64);
		arm1.mirror = true;
		setRotation(arm1, 0F, 0F, 0F);
		arm2 = new ModelRenderer(this, 0, 0);
		arm2.addBox(-5F, 15F, -1F, 1, 4, 2);
		arm2.setRotationPoint(0F, 0F, 0F);
		arm2.setTextureSize(128, 64);
		arm2.mirror = true;
		setRotation(arm2, 0F, 0F, 0F);
		arm3 = new ModelRenderer(this, 0, 8);
		arm3.addBox(-4F, 15F, -1F, 1, 1, 2);
		arm3.setRotationPoint(0F, 0F, 0F);
		arm3.setTextureSize(128, 64);
		arm3.mirror = true;
		setRotation(arm3, 0F, 0F, 0F);
		arm4 = new ModelRenderer(this, 0, 8);
		arm4.addBox(3F, 15F, -1F, 1, 1, 2);
		arm4.setRotationPoint(0F, 0F, 0F);
		arm4.setTextureSize(128, 64);
		arm4.mirror = true;
		setRotation(arm4, 0F, 0F, 0F);
		arm5 = new ModelRenderer(this, 8, 0);
		arm5.addBox(-1F, 15F, -5F, 2, 4, 1);
		arm5.setRotationPoint(0F, 0F, 0F);
		arm5.setTextureSize(128, 64);
		arm5.mirror = true;
		setRotation(arm5, 0F, 0F, 0F);
		arm6 = new ModelRenderer(this, 8, 0);
		arm6.addBox(-1F, 15F, 4F, 2, 4, 1);
		arm6.setRotationPoint(0F, 0F, 0F);
		arm6.setTextureSize(128, 64);
		arm6.mirror = true;
		setRotation(arm6, 0F, 0F, 0F);
		arm7 = new ModelRenderer(this, 8, 7);
		arm7.addBox(-1F, 15F, -4F, 2, 1, 1);
		arm7.setRotationPoint(0F, 0F, 0F);
		arm7.setTextureSize(128, 64);
		arm7.mirror = true;
		setRotation(arm7, 0F, 0F, 0F);
		arm8 = new ModelRenderer(this, 8, 7);
		arm8.addBox(-1F, 15F, 3F, 2, 1, 1);
		arm8.setRotationPoint(0F, 0F, 0F);
		arm8.setTextureSize(128, 64);
		arm8.mirror = true;
		setRotation(arm8, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		arm1.render(f5);
		arm2.render(f5);
		arm3.render(f5);
		arm4.render(f5);
		arm5.render(f5);
		arm6.render(f5);
		arm7.render(f5);
		arm8.render(f5);
	}
	
	public void renderArms(float f5) {
		arm1.render(f5);
		arm2.render(f5);
		arm3.render(f5);
		arm4.render(f5);
		arm5.render(f5);
		arm6.render(f5);
		arm7.render(f5);
		arm8.render(f5);
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