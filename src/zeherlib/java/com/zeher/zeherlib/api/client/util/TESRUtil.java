package com.zeher.zeherlib.api.client.util;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.zeher.zeherlib.api.client.tesr.EnumTESRColour;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


public class TESRUtil {

	public static final int MAX_LIGHT_X = 0xF000F0;
	public static final int MAX_LIGHT_Y = 0xF000F0;
	
	/**
	 * Massive thanks to Ellpeck from Actually Additions, for this code.
	 * @author Ellpeck
	 */
	@SuppressWarnings("resource")
	public static void renderLaser(double firstX, double firstY, double firstZ, double secondX, double secondY, double secondZ, double rotationTime, float alpha, double beamWidth, EnumTESRColour enum_colour) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder render = tessellator.getBuffer();
		World world = Minecraft.getInstance().world;
		
		float[] colour = enum_colour.getColour();
		
		float r = colour[0];
		float g = colour[1];
		float b = colour[2];
		
		Vec3d vector_one = new Vec3d(firstX, firstY, firstZ);
		Vec3d vector_two = new Vec3d(secondX, secondY, secondZ);
		Vec3d vector_combined = vector_two.subtract(vector_one);
		
		double rotation = rotationTime > 0 ? (360D*((world.getGameTime()%rotationTime) / rotationTime)) : 0;
		double pitch = Math.atan2(vector_combined.y, Math.sqrt(vector_combined.x * vector_combined.x + vector_combined.z * vector_combined.z));
		double yaw = Math.atan2(-vector_combined.z, vector_combined.x);
		
		double length = vector_combined.length();
		
		GlStateManager.pushMatrix();
		
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
		
		int func = GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC);
		float ref = GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF);
		
		GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0);
		GlStateManager.translated(firstX - TileEntityRendererDispatcher.staticPlayerX, firstY - TileEntityRendererDispatcher.staticPlayerY, firstZ - TileEntityRendererDispatcher.staticPlayerZ);
		GlStateManager.rotatef((float) (180 * yaw / Math.PI), 0, 1, 0);
		GlStateManager.rotatef((float) (180 * pitch / Math.PI), 0, 0, 1);
		GlStateManager.rotatef((float) rotation, 1, 0, 0);
		
		GlStateManager.disableTexture();
		render.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		for (double i = 0; i < 4; i++) {
			double width = beamWidth * (i / 4.0);
			
			render.pos(length, width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.pos(0, width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.pos(0, -width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.pos(length, -width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();

            render.pos(length, -width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.pos(0, -width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.pos(0, width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.pos(length, width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();

            render.pos(length, width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.pos(0, width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.pos(0, width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.pos(length, width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();

            render.pos(length, -width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.pos(0, -width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.pos(0, -width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.pos(length, -width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
		}
		
		tessellator.draw();
		
		GlStateManager.enableTexture();
		
		GlStateManager.alphaFunc(func, ref);
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}