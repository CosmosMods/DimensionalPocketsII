package com.zeher.zehercraft.utility;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.registry.RegistrySimple;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class BakedUtil {

	private static final float x[] = { 0.32f, 0.32f, 0.68f, 0.68f };
	private static final float z[] = { 0.32f, 0.68f, 0.68f, 0.32f };

	private static final float xy[] = { 0, 0, 1, 1 };
	private static final float zy[] = { 0, 1, 1, 0 };

	private static final float xMain[] = { 0, 0.32f, 0.68f, 1, -0.18f };
	private static final float zMain[] = { 0, 0.32f, 0.68f, 1, -0.18f };
	private static final float yMain[] = { 0, 0.32f, 0.68f, 1, -0.18f };
	
	private static void putVertex(UnpackedBakedQuad.Builder builder, EnumFacing side, float x, float y, float z,
			float u, float v, VertexFormat format, Fluid fluid) {
		for (int e = 0; e < format.getElementCount(); e++) {
			switch (format.getElement(e).getUsage()) {
			case POSITION:
				float[] data = new float[] { x, y, z };
				builder.put(e, data);
				break;

			case COLOR:
				builder.put(e, ((fluid.getColor() >> 16) & 0xFF) / 255f, ((fluid.getColor() >> 8) & 0xFF) / 255f,
						(fluid.getColor() & 0xFF) / 255f, ((fluid.getColor() >> 24) & 0xFF) / 255f);
				break;

			case UV:
				if (format.getElement(e).getIndex() == 0) {
					builder.put(e, u, v);
					break;
				}

			case NORMAL:
				builder.put(e, (float) side.getFrontOffsetX(), (float) side.getFrontOffsetY(),
						(float) side.getFrontOffsetZ(), 0f);
				break;

			default:
				builder.put(e);
				break;
			}
		}
	}

	public static void renderBase(Fluid fluid, UnpackedBakedQuad.Builder quad_builder, TextureAtlasSprite texture,
			VertexFormat format, EnumMap<EnumFacing, List<BakedQuad>> faceQuads) {
		// BASE
		EnumFacing side_base = EnumFacing.UP;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		float X_base, Z_base;

		for (int i = 0; i < 4; i++) {
			X_base = Math.min(Math.max(x[i], 0), 1f);
			Z_base = Math.min(Math.max(z[i], 0), 1f);

			putVertex(quad_builder, side_base, X_base, yMain[2], Z_base, texture.getInterpolatedU(x[i] * 16),
					texture.getInterpolatedV(z[i] * 16), format, fluid);
		}
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.DOWN;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		for (int i = 0; i < 4; i++) {
			X_base = Math.min(Math.max(x[i], 0.32f), 0.68f);
			Z_base = Math.min(Math.max(z[i], 0.32f), 0.68f);

			putVertex(quad_builder, side_base, Z_base, yMain[1], X_base, texture.getInterpolatedU(z[i] * 16),
					texture.getInterpolatedV(x[i] * 16), format, fluid);
		}

		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.EAST;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[2], yMain[2], zMain[2], texture.getInterpolatedU(0), texture.getInterpolatedV(16 - 6), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[1], zMain[2], texture.getInterpolatedU(0), texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[1], zMain[1], texture.getInterpolatedU(16), texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[2], zMain[1], texture.getInterpolatedU(16), texture.getInterpolatedV(16 - 6), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.WEST;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[1], yMain[2], zMain[1], texture.getInterpolatedU(0), texture.getInterpolatedV(16 - 6), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[1], zMain[1], texture.getInterpolatedU(0), texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[1], zMain[2], texture.getInterpolatedU(16), texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[2], zMain[2], texture.getInterpolatedU(16), texture.getInterpolatedV(16 - 6), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.SOUTH;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[1], yMain[2], zMain[2], texture.getInterpolatedU(0), texture.getInterpolatedV(16 - 6), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[1], zMain[2], texture.getInterpolatedU(0), texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[1], zMain[2], texture.getInterpolatedU(16), texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[2], zMain[2], texture.getInterpolatedU(16), texture.getInterpolatedV(16 - 6), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.NORTH;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[2], yMain[2], zMain[1], texture.getInterpolatedU(0), texture.getInterpolatedV(16 - 6), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[1], zMain[1], texture.getInterpolatedU(0), texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[1], zMain[1], texture.getInterpolatedU(16), texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[2], zMain[1], texture.getInterpolatedU(16), texture.getInterpolatedV(16 - 6), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));
	}

	public static void renderDown(Fluid fluid, UnpackedBakedQuad.Builder quad_builder, TextureAtlasSprite texture,
			VertexFormat format, EnumMap<EnumFacing, List<BakedQuad>> faceQuads) {
		EnumFacing side_base = EnumFacing.UP;
		float X_base, Z_base;

		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		for (int i = 0; i < 4; i++) {
			X_base = Math.min(Math.max(x[i], 0), 1f);
			Z_base = Math.min(Math.max(z[i], 0), 1f);

			putVertex(quad_builder, side_base, X_base, yMain[2], Z_base, texture.getInterpolatedU(x[i] * 16),
					texture.getInterpolatedV(z[i] * 16), format, fluid);
		}
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		/*
		 * side_base = EnumFacing.DOWN; quad_builder = new
		 * UnpackedBakedQuad.Builder(format);
		 * quad_builder.setQuadOrientation(side_base); quad_builder.setTexture(texture);
		 * for(int i = 0; i < 4; i++) { X_base = Math.min(Math.max(x[i], 0.32f), 0.68f
		 * ); Z_base = Math.min(Math.max(z[i], 0.32f), 0.68f );
		 * 
		 * putVertex( quad_builder, side_base, Z_base, yMain[0], X_base,
		 * texture.getInterpolatedU(z[i] * 16), texture.getInterpolatedV(x[i] * 16),
		 * format, fluid); }
		 * 
		 * faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));
		 */

		side_base = EnumFacing.EAST;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[2], yMain[2], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(8), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[0], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[0], zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[2], zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(8), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.WEST;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[1], yMain[2], zMain[1], texture.getInterpolatedU(0),
				texture.getInterpolatedV(8), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[0], zMain[1], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[0], zMain[2], texture.getInterpolatedU(6),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[2], zMain[2], texture.getInterpolatedU(6),
				texture.getInterpolatedV(8), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.SOUTH;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[1], yMain[2], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(8), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[0], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[0], zMain[2], texture.getInterpolatedU(6),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[2], zMain[2], texture.getInterpolatedU(6),
				texture.getInterpolatedV(8), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.NORTH;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[2], yMain[2], zMain[1], texture.getInterpolatedU(0),
				texture.getInterpolatedV(8), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[0], zMain[1], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[0], zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[2], zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(8), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));
	}

	public static void renderUp(Fluid fluid, UnpackedBakedQuad.Builder quad_builder, TextureAtlasSprite texture,
			VertexFormat format, EnumMap<EnumFacing, List<BakedQuad>> faceQuads) {
		EnumFacing side_base = null;
		float X_base, Z_base;

		/*
		 * quad_builder = new UnpackedBakedQuad.Builder(format);
		 * quad_builder.setQuadOrientation(side_base); quad_builder.setTexture(texture);
		 * for (int i = 0; i < 4; i++) { X_base = Math.min(Math.max(x[i], 0), 1f );
		 * Z_base = Math.min(Math.max(z[i], 0), 1f );
		 * 
		 * putVertex( quad_builder, side_base, X_base, yMain[3], Z_base,
		 * texture.getInterpolatedU(x[i] * 16), texture.getInterpolatedV(z[i] * 16),
		 * format, fluid); } faceQuads.put(side_base,
		 * ImmutableList.<BakedQuad>of(quad_builder.build()));
		 */

		side_base = EnumFacing.DOWN;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		for (int i = 0; i < 4; i++) {
			X_base = Math.min(Math.max(x[i], 0.32f), 0.68f);
			Z_base = Math.min(Math.max(z[i], 0.32f), 0.68f);

			putVertex(quad_builder, side_base, Z_base, yMain[1], X_base, texture.getInterpolatedU(z[i] * 16),
					texture.getInterpolatedV(x[i] * 16), format, fluid);
		}

		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.EAST;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[2], yMain[3], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(8), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[1], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[1], zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[3], zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(8), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.WEST;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[1], yMain[3], zMain[1], texture.getInterpolatedU(0),
				texture.getInterpolatedV(8), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[1], zMain[1], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[1], zMain[2], texture.getInterpolatedU(6),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[3], zMain[2], texture.getInterpolatedU(6),
				texture.getInterpolatedV(8), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.SOUTH;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[1], yMain[3], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(8), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[1], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[1], zMain[2], texture.getInterpolatedU(6),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[3], zMain[2], texture.getInterpolatedU(6),
				texture.getInterpolatedV(8), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.NORTH;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[2], yMain[3], zMain[1], texture.getInterpolatedU(0),
				texture.getInterpolatedV(8), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[1], zMain[1], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[1], zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[3], zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(8), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));
	}

	public static void renderUpDown(Fluid fluid, UnpackedBakedQuad.Builder quad_builder, TextureAtlasSprite texture,
			VertexFormat format, EnumMap<EnumFacing, List<BakedQuad>> faceQuads) {
		EnumFacing side_base = null;
		float X_base, Z_base;

		/*
		 * quad_builder = new UnpackedBakedQuad.Builder(format);
		 * quad_builder.setQuadOrientation(side_base); quad_builder.setTexture(texture);
		 * for (int i = 0; i < 4; i++) { X_base = Math.min(Math.max(x[i], 0), 1f );
		 * Z_base = Math.min(Math.max(z[i], 0), 1f );
		 * 
		 * putVertex( quad_builder, side_base, X_base, yMain[3], Z_base,
		 * texture.getInterpolatedU(x[i] * 16), texture.getInterpolatedV(z[i] * 16),
		 * format, fluid); } faceQuads.put(side_base,
		 * ImmutableList.<BakedQuad>of(quad_builder.build()));
		 */

		/*
		 * side_base = EnumFacing.DOWN; quad_builder = new
		 * UnpackedBakedQuad.Builder(format);
		 * quad_builder.setQuadOrientation(side_base); quad_builder.setTexture(texture);
		 * for(int i = 0; i < 4; i++) { X_base = Math.min(Math.max(x[i], 0.32f), 0.68f
		 * ); Z_base = Math.min(Math.max(z[i], 0.32f), 0.68f );
		 * 
		 * putVertex( quad_builder, side_base, Z_base, yMain[0], X_base,
		 * texture.getInterpolatedU(z[i] * 16), texture.getInterpolatedV(x[i] * 16),
		 * format, fluid); }
		 */

		// faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.EAST;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);

		putVertex(quad_builder, side_base, xMain[2], yMain[3], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(8), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[0], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[0], zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[3], zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(8), format, fluid);

		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.WEST;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[1], yMain[3], zMain[1], texture.getInterpolatedU(0),
				texture.getInterpolatedV(8), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[0], zMain[1], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[0], zMain[2], texture.getInterpolatedU(6),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[3], zMain[2], texture.getInterpolatedU(6),
				texture.getInterpolatedV(8), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.SOUTH;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[1], yMain[3], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(8), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[0], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[0], zMain[2], texture.getInterpolatedU(6),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[3], zMain[2], texture.getInterpolatedU(6),
				texture.getInterpolatedV(8), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.NORTH;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[2], yMain[3], zMain[1], texture.getInterpolatedU(0),
				texture.getInterpolatedV(8), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[0], zMain[1], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[0], zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[3], zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(8), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));
	}

	public static void renderNorth(Fluid fluid, UnpackedBakedQuad.Builder quad_builder, TextureAtlasSprite texture,
			VertexFormat format, EnumMap<EnumFacing, List<BakedQuad>> faceQuads) {
		// BASE
		EnumFacing side_base = EnumFacing.UP;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		float X_base, Z_base;

		for (int i = 0; i < 4; i++) {
			X_base = Math.min(Math.max(xy[i], 0.32f), 0.68f);
			Z_base = Math.min(Math.max(zy[i], 0f), 0.68f);

			putVertex(quad_builder, side_base, X_base, yMain[2], Z_base, texture.getInterpolatedU(xy[i] * 16),
					texture.getInterpolatedV(zy[i] * 16), format, fluid);
		}
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.DOWN;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		for (int i = 0; i < 4; i++) {
			X_base = Math.min(Math.max(xy[i], 0f), 0.68f);
			Z_base = Math.min(Math.max(zy[i], 0.32f), 0.68f);

			putVertex(quad_builder, side_base, Z_base, yMain[1], X_base, texture.getInterpolatedU(z[i] * 16),
					texture.getInterpolatedV(x[i] * 16), format, fluid);
		}

		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.EAST;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[2], yMain[2], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16 - 6), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[1], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[1], zMain[0], texture.getInterpolatedU(16),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[2], zMain[0], texture.getInterpolatedU(16),
				texture.getInterpolatedV(16 - 6), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.WEST;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[1], yMain[2], zMain[0], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16 - 6), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[1], zMain[0], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[1], zMain[2], texture.getInterpolatedU(16),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[2], zMain[2], texture.getInterpolatedU(16),
				texture.getInterpolatedV(16 - 6), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.SOUTH;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[1], yMain[2], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16 - 6), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[1], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[1], zMain[2], texture.getInterpolatedU(16),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[2], zMain[2], texture.getInterpolatedU(16),
				texture.getInterpolatedV(16 - 6), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		/*
		 * side_base = EnumFacing.NORTH; quad_builder = new
		 * UnpackedBakedQuad.Builder(format);
		 * quad_builder.setQuadOrientation(side_base); quad_builder.setTexture(texture);
		 * putVertex( quad_builder, side_base, xMain[2], yMain[2], zMain[0],
		 * texture.getInterpolatedU(0), texture.getInterpolatedV(16 - 6), format,
		 * fluid); putVertex( quad_builder, side_base, xMain[2], yMain[1] , zMain[0],
		 * texture.getInterpolatedU(0), texture.getInterpolatedV(16), format, fluid);
		 * putVertex( quad_builder, side_base, xMain[1], yMain[1] , zMain[0],
		 * texture.getInterpolatedU(16), texture.getInterpolatedV(16), format, fluid);
		 * putVertex( quad_builder, side_base, xMain[1], yMain[2], zMain[0],
		 * texture.getInterpolatedU(16), texture.getInterpolatedV(16 - 6), format,
		 * fluid); faceQuads.put(side_base,
		 * ImmutableList.<BakedQuad>of(quad_builder.build()));
		 */
	}

	public static void renderNorthDown(Fluid fluid, UnpackedBakedQuad.Builder quad_builder,
			UnpackedBakedQuad.Builder quad_builder_two, TextureAtlasSprite texture, VertexFormat format,
			EnumMap<EnumFacing, List<BakedQuad>> faceQuads) {
		// BASE
		EnumFacing side_base = EnumFacing.UP;
		float X_base, Z_base;

		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);

		for (int i = 0; i < 4; i++) {
			X_base = Math.min(Math.max(xy[i], 0.32f), 0.68f);
			Z_base = Math.min(Math.max(zy[i], 0f), 0.68f);

			putVertex(quad_builder, side_base, X_base, yMain[2], Z_base, texture.getInterpolatedU(xy[i] * 16),
					texture.getInterpolatedV(zy[i] * 16), format, fluid);
		}
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.DOWN;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		for (int i = 0; i < 4; i++) {
			X_base = Math.min(Math.max(xy[i], 0f), 0.32f);
			Z_base = Math.min(Math.max(zy[i], 0.32f), 0.68f);

			putVertex(quad_builder, side_base, Z_base, yMain[1], X_base, texture.getInterpolatedU(z[i] * 16),
					texture.getInterpolatedV(x[i] * 16), format, fluid);
		}

		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.EAST;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);

		quad_builder_two = new UnpackedBakedQuad.Builder(format);
		quad_builder_two.setQuadOrientation(side_base);
		quad_builder_two.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[2], yMain[2], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16 - 6), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[1], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[1], zMain[0], texture.getInterpolatedU(16),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[2], zMain[0], texture.getInterpolatedU(16),
				texture.getInterpolatedV(16 - 6), format, fluid);
		putVertex(quad_builder_two, side_base, xMain[2], yMain[1], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(10), format, fluid);
		putVertex(quad_builder_two, side_base, xMain[2], yMain[0], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder_two, side_base, xMain[2], yMain[0], zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder_two, side_base, xMain[2], yMain[1], zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(10), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build(), quad_builder_two.build()));

		side_base = EnumFacing.WEST;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);

		quad_builder_two = new UnpackedBakedQuad.Builder(format);
		quad_builder_two.setQuadOrientation(side_base);
		quad_builder_two.setTexture(texture);

		putVertex(quad_builder, side_base, xMain[1], yMain[2], zMain[0], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16 - 6), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[1], zMain[0], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[1], zMain[2], texture.getInterpolatedU(16),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[2], zMain[2], texture.getInterpolatedU(16),
				texture.getInterpolatedV(16 - 6), format, fluid);
		putVertex(quad_builder_two, side_base, xMain[1], yMain[1], zMain[1], texture.getInterpolatedU(0),
				texture.getInterpolatedV(10), format, fluid);
		putVertex(quad_builder_two, side_base, xMain[1], yMain[0], zMain[1], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder_two, side_base, xMain[1], yMain[0], zMain[2], texture.getInterpolatedU(6),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder_two, side_base, xMain[1], yMain[1], zMain[2], texture.getInterpolatedU(6),
				texture.getInterpolatedV(10), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build(), quad_builder_two.build()));

		side_base = EnumFacing.SOUTH;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		quad_builder_two = new UnpackedBakedQuad.Builder(format);
		quad_builder_two.setQuadOrientation(side_base);
		quad_builder_two.setTexture(texture);

		putVertex(quad_builder, side_base, xMain[1], yMain[2], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16 - 6), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[1], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[1], zMain[2], texture.getInterpolatedU(16),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[2], zMain[2], texture.getInterpolatedU(16),
				texture.getInterpolatedV(16 - 6), format, fluid);
		putVertex(quad_builder_two, side_base, xMain[1], yMain[1], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(10), format, fluid);
		putVertex(quad_builder_two, side_base, xMain[1], yMain[0], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder_two, side_base, xMain[2], yMain[0], zMain[2], texture.getInterpolatedU(6),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder_two, side_base, xMain[2], yMain[1], zMain[2], texture.getInterpolatedU(6),
				texture.getInterpolatedV(10), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build(), quad_builder_two.build()));

		side_base = EnumFacing.NORTH;
		quad_builder_two = new UnpackedBakedQuad.Builder(format);
		quad_builder_two.setQuadOrientation(side_base);
		quad_builder_two.setTexture(texture);
		putVertex(quad_builder_two, side_base, xMain[2], yMain[1], zMain[1], texture.getInterpolatedU(0),
				texture.getInterpolatedV(10), format, fluid);
		putVertex(quad_builder_two, side_base, xMain[2], yMain[0], zMain[1], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder_two, side_base, xMain[1], yMain[0], zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder_two, side_base, xMain[1], yMain[1], zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(10), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder_two.build()));

		/*
		 * side_base = EnumFacing.NORTH; quad_builder = new
		 * UnpackedBakedQuad.Builder(format);
		 * quad_builder.setQuadOrientation(side_base); quad_builder.setTexture(texture);
		 * putVertex( quad_builder, side_base, xMain[2], yMain[2], zMain[0],
		 * texture.getInterpolatedU(0), texture.getInterpolatedV(16 - 6), format,
		 * fluid); putVertex( quad_builder, side_base, xMain[2], yMain[1] , zMain[0],
		 * texture.getInterpolatedU(0), texture.getInterpolatedV(16), format, fluid);
		 * putVertex( quad_builder, side_base, xMain[1], yMain[1] , zMain[0],
		 * texture.getInterpolatedU(16), texture.getInterpolatedV(16), format, fluid);
		 * putVertex( quad_builder, side_base, xMain[1], yMain[2], zMain[0],
		 * texture.getInterpolatedU(16), texture.getInterpolatedV(16 - 6), format,
		 * fluid); faceQuads.put(side_base,
		 * ImmutableList.<BakedQuad>of(quad_builder.build()));
		 */
	}

	public static void exampleMultipleFaces(Fluid fluid, UnpackedBakedQuad.Builder quad_builder,
			UnpackedBakedQuad.Builder quad_builder_two, TextureAtlasSprite texture, VertexFormat format,
			EnumMap<EnumFacing, List<BakedQuad>> faceQuads) {
		EnumFacing side_base = null;
		float X_base, Z_base;

		/*
		 * quad_builder = new UnpackedBakedQuad.Builder(format);
		 * quad_builder.setQuadOrientation(side_base); quad_builder.setTexture(texture);
		 * for (int i = 0; i < 4; i++) { X_base = Math.min(Math.max(x[i], 0), 1f );
		 * Z_base = Math.min(Math.max(z[i], 0), 1f );
		 * 
		 * putVertex( quad_builder, side_base, X_base, yMain[3], Z_base,
		 * texture.getInterpolatedU(x[i] * 16), texture.getInterpolatedV(z[i] * 16),
		 * format, fluid); } faceQuads.put(side_base,
		 * ImmutableList.<BakedQuad>of(quad_builder.build()));
		 */

		/*
		 * side_base = EnumFacing.DOWN; quad_builder = new
		 * UnpackedBakedQuad.Builder(format);
		 * quad_builder.setQuadOrientation(side_base); quad_builder.setTexture(texture);
		 * for(int i = 0; i < 4; i++) { X_base = Math.min(Math.max(x[i], 0.32f), 0.68f
		 * ); Z_base = Math.min(Math.max(z[i], 0.32f), 0.68f );
		 * 
		 * putVertex( quad_builder, side_base, Z_base, yMain[0], X_base,
		 * texture.getInterpolatedU(z[i] * 16), texture.getInterpolatedV(x[i] * 16),
		 * format, fluid); }
		 */

		// faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.EAST;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);

		quad_builder_two = new UnpackedBakedQuad.Builder(format);
		quad_builder_two.setQuadOrientation(side_base);
		quad_builder_two.setTexture(texture);

		putVertex(quad_builder, side_base, xMain[2], yMain[3], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(8), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[0], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[0], zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[3], zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(8), format, fluid);

		putVertex(quad_builder_two, side_base, xMain[2], 2, zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(8), format, fluid);
		putVertex(quad_builder_two, side_base, xMain[2], yMain[0], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder_two, side_base, xMain[2], yMain[0], zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder_two, side_base, xMain[2], 2, zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(8), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build(), quad_builder_two.build()));

		side_base = EnumFacing.WEST;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[1], yMain[3], zMain[1], texture.getInterpolatedU(0),
				texture.getInterpolatedV(8), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[0], zMain[1], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[0], zMain[2], texture.getInterpolatedU(6),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[3], zMain[2], texture.getInterpolatedU(6),
				texture.getInterpolatedV(8), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.SOUTH;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[1], yMain[3], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(8), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[0], zMain[2], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[0], zMain[2], texture.getInterpolatedU(6),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[3], zMain[2], texture.getInterpolatedU(6),
				texture.getInterpolatedV(8), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));

		side_base = EnumFacing.NORTH;
		quad_builder = new UnpackedBakedQuad.Builder(format);
		quad_builder.setQuadOrientation(side_base);
		quad_builder.setTexture(texture);
		putVertex(quad_builder, side_base, xMain[2], yMain[3], zMain[1], texture.getInterpolatedU(0),
				texture.getInterpolatedV(8), format, fluid);
		putVertex(quad_builder, side_base, xMain[2], yMain[0], zMain[1], texture.getInterpolatedU(0),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[0], zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(16), format, fluid);
		putVertex(quad_builder, side_base, xMain[1], yMain[3], zMain[1], texture.getInterpolatedU(6),
				texture.getInterpolatedV(8), format, fluid);
		faceQuads.put(side_base, ImmutableList.<BakedQuad>of(quad_builder.build()));
	}
}
