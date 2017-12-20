package com.zeher.dimensionalpockets.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPocketInner extends ModelBase {

	ModelRenderer inner;
	
	public ModelPocketInner() {
		textureWidth = 12;
		textureHeight = 12;
		
		inner = new ModelRenderer(this, 0, 0);
		inner.addBox(2, 2, 2, 12, 12, 12);
		inner.setRotationPoint(0, 0, 0);
		inner.setTextureSize(12, 12);
		inner.mirror = true;
		setRotation(inner, 0F, 0F, 0F);
	}
	
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		
		inner.render(f);
	}
	
	public void render(float f) {
		inner.render(f);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
	}
}
