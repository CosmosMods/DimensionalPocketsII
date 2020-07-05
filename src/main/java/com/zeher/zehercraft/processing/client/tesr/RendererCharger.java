package com.zeher.zehercraft.processing.client.tesr;

import com.zeher.zehercraft.processing.core.block.BlockCharger;
import com.zeher.zehercraft.processing.core.tile.TileEntityCharger;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class RendererCharger extends TileEntitySpecialRenderer<TileEntityCharger> {
	
	public RendererCharger() { }
	
	@Override
	public void render(TileEntityCharger tileentity, double x, double y, double z, float partialTicks, int destroyStage, float a) {
		if (tileentity == null) {
			return;
		}
		
		IBlockState state = tileentity.getWorld().getBlockState(tileentity.getPos());
		BlockCharger block = (BlockCharger) state.getBlock();
		EnumFacing facing = state.getValue(block.FACING);
		
		float offset = 0.25F;
		float zero__ = 0.0F;
		
		float[] array_x = new float[] { offset, zero__, -offset, offset, zero__, -offset, offset, zero__, -offset };
		float[] array_z = new float[] { offset, offset, offset, zero__, zero__, zero__, -offset, -offset, -offset };
		
		if (!tileentity.isEmpty()) {
			for (int i = 0; i < tileentity.getSizeInventory(); i++) {
				if (i < 9) {
					ItemStack stack = tileentity.getStackInSlot(i);
					
					GlStateManager.pushMatrix();
					GlStateManager.translate(x + 0.5D, y + 1.1D, z + 0.5D);
					
					GlStateManager.translate(array_x[i], -0.1F, array_z[i]);
					
					//GlStateManager.rotate(Minecraft.getSystemTime() / 720.0F * 31.830988F, 0.0F, 1.0F, 0.0F);
					
					if (stack.getItem() instanceof ItemBlock) {
						GlStateManager.scale(0.4F, 0.4F, 0.4F);
					} else {
						GlStateManager.scale(0.2F, 0.2F, 0.2F);
					}
					
					System.out.println(facing);
					
					if (facing.equals(EnumFacing.SOUTH)) {
						//GlStateManager.rotate(180, 0.0F, 1.0F, 0.0F);
					} else if (facing.equals(EnumFacing.WEST)) {
						//GlStateManager.rotate(90, 0.0F, 1.0F, 0.0F);
					} else if (facing.equals(EnumFacing.EAST)) {
						//GlStateManager.rotate(270, 0.0F, 1.0F, 0.0F);
					}
					
					GlStateManager.pushAttrib();
					
					RenderHelper.disableStandardItemLighting();
					Minecraft.getMinecraft().getRenderItem().renderItem(tileentity.getStackInSlot(i), ItemCameraTransforms.TransformType.FIXED);
					RenderHelper.enableStandardItemLighting();
			
					GlStateManager.popAttrib();
					GlStateManager.popMatrix();
				}
			}
		}
	}
}