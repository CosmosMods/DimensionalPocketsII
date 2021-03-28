package com.tcn.dimensionalpocketsii.pocket.client.renderer;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityCharger;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RendererCharger extends TileEntityRenderer<TileEntityCharger> {

	public RendererCharger(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void render(TileEntityCharger tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		
		int slot = 0;
		World world = tileEntityIn.getLevel();
		BlockPos pos = tileEntityIn.getBlockPos();	
		ItemStack stack = tileEntityIn.getItem(slot);
		
		if (!(stack.isEmpty())) {
			matrixStackIn.pushPose();
			GL11.glPushMatrix();

			matrixStackIn.translate(0, 0.5F, 0);
			
			/**- EAST - */
			if (world.getBlockState(pos.offset(Direction.EAST.getNormal())).isAir()) {
				matrixStackIn.pushPose();
				matrixStackIn.translate(0.975F, 0, 0.5F);
				Quaternion east = new Quaternion(Vector3f.YN, 90, true);
				matrixStackIn.mulPose(east);
				Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.FIXED, 0, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
				matrixStackIn.popPose();
			}
			
			/** - WEST - */
			if (world.getBlockState(pos.offset(Direction.WEST.getNormal())).isAir()) {
				matrixStackIn.pushPose();
				matrixStackIn.translate(0.025F, 0, 0.5F);
				Quaternion west = new Quaternion(Vector3f.YN, 90, true);
				matrixStackIn.mulPose(west);
				Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.FIXED, 0, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
				matrixStackIn.popPose();
			}
			
			/** - SOUTH - */
			if (world.getBlockState(pos.offset(Direction.SOUTH.getNormal())).isAir()) {
				matrixStackIn.pushPose();
				matrixStackIn.translate(0.5F, 0, 0.975F);
				Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.FIXED, 0, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
				matrixStackIn.popPose();
			}
			/** - NORTH - */
			if (world.getBlockState(pos.offset(Direction.NORTH.getNormal())).isAir()) {
				matrixStackIn.pushPose();
				matrixStackIn.translate(0.5F, 0, 0.025F);
				Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.FIXED, 0, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
				matrixStackIn.popPose();
			}
			
			
			
			
			//GL11.glScalef(0.8F, 0.8F, 0.8F);
			
			//GlStateManager.pushLightingAttributes();

			for (Direction c : Direction.values()) {
				if (c != Direction.UP && c != Direction.DOWN) {
					BlockPos offset = pos.offset(c.getNormal());
					Block block = world.getBlockState(offset).getBlock();
					
					if (block.equals(Blocks.AIR)) {
						
						RenderHelper.turnOff();//.disableStandardItemLighting();
						if (c.equals(Direction.EAST)) {
							//matrixStackIn.translate(0, 0, 1.525F);
						//	Minecraft.getInstance().getItemRenderer().renderItem(stack, TransformType.FIXED, 0, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
							break;
						} else if (c.equals(Direction.WEST)) {
							//matrixStackIn.translate(0, 0, -0.525F);
							//Minecraft.getInstance().getItemRenderer().renderItem(stack, TransformType.FIXED, 0, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
						} else if (c.equals(Direction.NORTH)) {
							//matrixStackIn.translate(0, 1, 0);
							//Minecraft.getInstance().getItemRenderer().renderItem(stack, TransformType.FIXED, 0, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
						} else if (c.equals(Direction.SOUTH)) {
							//matrixStackIn.translate(0, 0, -0.525F);
							//Minecraft.getInstance().getItemRenderer().renderItem(stack, TransformType.FIXED, 0, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
						}
						RenderHelper.turnBackOn();//.enableStandardItemLighting();
					}
					
					
					Vector3i vec = c.getNormal();//.getDirectionVec();
					//System.out.println(c.getDirectionVec());

					//Quaternion q = new Quaternion(Vector3f.YN, 90, true);
					//matrixStackIn.rotate(q);
					
					
					matrixStackIn.translate(-vec.getX() - 0.5F, -vec.getY(), -vec.getZ() - 0.5F);
					
					//GL11.glRotatef(90, 1, 1, 1);
					
					
				}
			}
			
			//GlStateManager.popAttributes();
			GL11.glPopMatrix();
			matrixStackIn.popPose();
		}
	}

}
