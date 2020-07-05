package com.zeher.zehercraft.processing.client.tesr.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelKilnInternals extends ModelBase { 
	
	ModelRenderer heatercoilbottom1;
	ModelRenderer heatercoilbottom2;
	ModelRenderer heatercoilbottom3;
	ModelRenderer heatercoilbottom4;
	
	ModelRenderer heatercoilbottomon1;
	ModelRenderer heatercoilbottomon2;
	ModelRenderer heatercoilbottomon3;
	ModelRenderer heatercoilbottomon4;

	public ModelKilnInternals() {
		textureWidth = 32;
		textureHeight = 32;
		
		heatercoilbottom1 = new ModelRenderer(this, 0, 0);
		heatercoilbottom1.addBox(-5F, 0F, -5F, 10, 1, 1);
		heatercoilbottom1.setRotationPoint(0F, 17F, 0F);
		heatercoilbottom1.setTextureSize(128, 64);
		heatercoilbottom1.mirror = true;
		setRotation(heatercoilbottom1, 0F, 0F, 0F);
		heatercoilbottom2 = new ModelRenderer(this, 0, 0);
		heatercoilbottom2.addBox(-5F, 0F, 4F, 10, 1, 1);
		heatercoilbottom2.setRotationPoint(0F, 17F, 0F);
		heatercoilbottom2.setTextureSize(128, 64);
		heatercoilbottom2.mirror = true;
		setRotation(heatercoilbottom2, 0F, 0F, 0F);
		heatercoilbottom3 = new ModelRenderer(this, 0, 4);
		heatercoilbottom3.addBox(-5F, 0F, -5F, 1, 1, 10);
		heatercoilbottom3.setRotationPoint(0F, 17F, 0F);
		heatercoilbottom3.setTextureSize(128, 64);
		heatercoilbottom3.mirror = true;
		setRotation(heatercoilbottom3, 0F, 0F, 0F);
		heatercoilbottom4 = new ModelRenderer(this, 0, 4);
		heatercoilbottom4.addBox(4F, 0F, -5F, 1, 1, 10);
		heatercoilbottom4.setRotationPoint(0F, 17F, 0F);
		heatercoilbottom4.setTextureSize(128, 64);
		heatercoilbottom4.mirror = true;
		setRotation(heatercoilbottom4, 0F, 0F, 0F);
		
		heatercoilbottomon1 = new ModelRenderer(this, 0, 16);
		heatercoilbottomon1.addBox(-5F, 0F, -5F, 10, 1, 1);
		heatercoilbottomon1.setRotationPoint(0F, 17F, 0F);
		heatercoilbottomon1.setTextureSize(128, 64);
		heatercoilbottomon1.mirror = true;
		setRotation(heatercoilbottomon1, 0F, 0F, 0F);
		heatercoilbottomon2 = new ModelRenderer(this, 0, 16);
		heatercoilbottomon2.addBox(-5F, 0F, 4F, 10, 1, 1);
		heatercoilbottomon2.setRotationPoint(0F, 17F, 0F);
		heatercoilbottomon2.setTextureSize(128, 64);
		heatercoilbottomon2.mirror = true;
		setRotation(heatercoilbottomon2, 0F, 0F, 0F);
		heatercoilbottomon3 = new ModelRenderer(this, 0, 20);
		heatercoilbottomon3.addBox(-5F, 0F, -5F, 1, 1, 10);
		heatercoilbottomon3.setRotationPoint(0F, 17F, 0F);
		heatercoilbottomon3.setTextureSize(128, 64);
		heatercoilbottomon3.mirror = true;
		setRotation(heatercoilbottomon3, 0F, 0F, 0F);
		heatercoilbottomon4 = new ModelRenderer(this, 0, 20);
		heatercoilbottomon4.addBox(4F, 0F, -5F, 1, 1, 10);
		heatercoilbottomon4.setRotationPoint(0F, 17F, 0F);
		heatercoilbottomon4.setTextureSize(128, 64);
		heatercoilbottomon4.mirror = true;
		setRotation(heatercoilbottomon4, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		heatercoilbottom1.render(f5);
		heatercoilbottom2.render(f5);
		heatercoilbottom3.render(f5);
		heatercoilbottom4.render(f5);
	}
	
	public void renderCoilsOff(float f5) {
		heatercoilbottom1.render(f5);
		heatercoilbottom2.render(f5);
		heatercoilbottom3.render(f5);
		heatercoilbottom4.render(f5);
	}
	
	public void renderCoilsOn(float f5) {
		heatercoilbottomon1.render(f5);
		heatercoilbottomon2.render(f5);
		heatercoilbottomon3.render(f5);
		heatercoilbottomon4.render(f5);
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
