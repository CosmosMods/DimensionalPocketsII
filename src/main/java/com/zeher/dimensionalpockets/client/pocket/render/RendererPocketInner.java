package com.zeher.dimensionalpockets.client.pocket.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.*;

import org.lwjgl.opengl.GL11;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.client.model.ModelPocketInner;
import com.zeher.dimensionalpockets.core.tileentity.TileEntityDimensionalPocket;

@SideOnly(Side.CLIENT)
public class RendererPocketInner extends TileEntitySpecialRenderer {
	
	@SideOnly(Side.CLIENT)
	private ModelPocketInner model;
	
	@SideOnly(Side.CLIENT)
	public RendererPocketInner() {
		this.model = new ModelPocketInner();
	}

	private static final ResourceLocation texture = new ResourceLocation(DimensionalPockets.mod_id + ":" + "textures/blocks/pocket/dimensional_pocket_inner.png");
	
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTicks, int destroyStage) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y, z + 0.5);
		this.bindTexture(texture);
		GlStateManager.rotate((Minecraft.getSystemTime() / 720.0F) * (100.0F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(-0.5, 0, -0.5);
		GlStateManager.pushAttrib();
		this.model.render(0.0625F);
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}

}
