package com.zeher.zehercraft.processing.client.tesr.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelGrinderInternals extends ModelBase {
	
	ModelRenderer Shape5;
	ModelRenderer Shape6;
	ModelRenderer Shape7;
	ModelRenderer Shape8;
	ModelRenderer Shape9;
	ModelRenderer Shape10;
	ModelRenderer Shape12;
	
	ModelRenderer Shape16;
	ModelRenderer Shape17;
	ModelRenderer Shape18;
	ModelRenderer Shape19;
	ModelRenderer Shape20;
	ModelRenderer Shape21;
	ModelRenderer Shape22;

	public ModelGrinderInternals() {
		textureWidth = 32;
		textureHeight = 32;

		Shape5 = new ModelRenderer(this, 8, 0);
		Shape5.addBox(-0.5F, -1F, -2F, 1, 2, 4);
		Shape5.setRotationPoint(0.0F, 0.0F, 0.0F);
		Shape5.setTextureSize(128, 64);
		Shape5.mirror = true;
		setRotation(Shape5, 0F, 1.570796F, 0F);
		Shape6 = new ModelRenderer(this, 0, 0);
		Shape6.addBox(-2.5F, -2F, -1F, 1, 4, 2);
		Shape6.setRotationPoint(0.0F, 0.0F, 0.0F);
		Shape6.setTextureSize(128, 64);
		Shape6.mirror = true;
		setRotation(Shape6, 0F, 1.570796F, 0F);
		Shape7 = new ModelRenderer(this, 8, 0);
		Shape7.addBox(-2.5F, -1F, -2F, 1, 2, 4);
		Shape7.setRotationPoint(0.0F, 0.0F, 0.0F);
		Shape7.setTextureSize(128, 64);
		Shape7.mirror = true;
		setRotation(Shape7, 0F, 1.570796F, 0F);
		Shape8 = new ModelRenderer(this, 0, 0);
		Shape8.addBox(1.5F, -2F, -1F, 1, 4, 2);
		Shape8.setRotationPoint(0.0F, 0.0F, 0.0F);
		Shape8.setTextureSize(128, 64);
		Shape8.mirror = true;
		setRotation(Shape8, 0F, 1.570796F, 0F);
		Shape9 = new ModelRenderer(this, 8, 0);
		Shape9.addBox(1.5F, -1F, -2F, 1, 2, 4);
		Shape9.setRotationPoint(0.0F, 0.0F, 0.0F);
		Shape9.setTextureSize(128, 64);
		Shape9.mirror = true;
		setRotation(Shape9, 0F, 1.570796F, 0F);
		Shape10 = new ModelRenderer(this, 8, 0);
		Shape10.addBox(-0.5F, -2F, -1F, 1, 4, 2);
		Shape10.setRotationPoint(0.0F, 0.0F, 0.0F);
		Shape10.setTextureSize(128, 64);
		Shape10.mirror = true;
		setRotation(Shape10, 0F, 1.570796F, 0F);
		Shape12 = new ModelRenderer(this, 6, 8);
		Shape12.addBox(-3.5F, -0.5F, -0.5F, 7, 1, 1);
		Shape12.setRotationPoint(0.0F, 0.0F, 0.0F);
		Shape12.setTextureSize(128, 64);
		Shape12.mirror = true;
		setRotation(Shape12, 0F, 1.570796F, 0F);
		
		Shape16 = new ModelRenderer(this, 6, 8);
		Shape16.addBox(-3.5F, -0.5F, -0.5F, 7, 1, 1);
		Shape16.setRotationPoint(0.0F, 0.0F, 0.0F);
		Shape16.setTextureSize(128, 64);
		Shape16.mirror = true;
		setRotation(Shape16, 0F, 1.570796F, 0F);
		Shape17 = new ModelRenderer(this, 8, 0);
		Shape17.addBox(1.5F, -1F, -2F, 1, 2, 4);
		Shape17.setRotationPoint(0F, 0F, 0F);
		Shape17.setTextureSize(128, 64);
		Shape17.mirror = true;
		setRotation(Shape17, 0F, 1.570796F, 0F);
		Shape18 = new ModelRenderer(this, 0, 0);
		Shape18.addBox(1.5F, -2F, -1F, 1, 4, 2);
		Shape18.setRotationPoint(0F, 0F, 0F);
		Shape18.setTextureSize(128, 64);
		Shape18.mirror = true;
		setRotation(Shape18, 0F, 1.570796F, 0F);
		Shape19 = new ModelRenderer(this, 8, 0);
		Shape19.addBox(-2.5F, -1F, -2F, 1, 2, 4);
		Shape19.setRotationPoint(0F, 0F, 0F);
		Shape19.setTextureSize(128, 64);
		Shape19.mirror = true;
		setRotation(Shape19, 0F, 1.570796F, 0F);
		Shape20 = new ModelRenderer(this, 8, 0);
		Shape20.addBox(-0.5F, -1F, -2F, 1, 2, 4);
		Shape20.setRotationPoint(0F, 0F, 0F);
		Shape20.setTextureSize(128, 64);
		Shape20.mirror = true;
		setRotation(Shape20, 0F, 1.570796F, 0F);
		Shape21 = new ModelRenderer(this, 0, 0);
		Shape21.addBox(-2.5F, -2F, -1F, 1, 4, 2);
		Shape21.setRotationPoint(0F, 0F, 0F);
		Shape21.setTextureSize(128, 64);
		Shape21.mirror = true;
		setRotation(Shape21, 0F, 1.570796F, 0F);
		Shape22 = new ModelRenderer(this, 0, 0);
		Shape22.addBox(-0.5F, -2F, -1F, 1, 4, 2);
		Shape22.setRotationPoint(0F, 0F, 0F);
		Shape22.setTextureSize(128, 64);
		Shape22.mirror = true;
		setRotation(Shape22, 0F, 1.570796F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Shape5.render(f5);
		Shape6.render(f5);
		Shape7.render(f5);
		Shape8.render(f5);
		Shape9.render(f5);
		Shape10.render(f5);
		Shape12.render(f5);
		
		Shape16.render(f5);
		Shape17.render(f5);
		Shape18.render(f5);
		Shape19.render(f5);
		Shape20.render(f5);
		Shape21.render(f5);
		Shape22.render(f5);
	}
	
	public void renderLeftTeeth(float f5) {
		Shape16.render(f5);
		Shape17.render(f5);
		Shape18.render(f5);
		Shape19.render(f5);
		Shape20.render(f5);
		Shape21.render(f5);
		Shape22.render(f5);
	}
	
	public void renderRightTeeth(float f5) {
		Shape5.render(f5);
		Shape6.render(f5);
		Shape7.render(f5);
		Shape8.render(f5);
		Shape9.render(f5);
		Shape10.render(f5);
		Shape12.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

}
