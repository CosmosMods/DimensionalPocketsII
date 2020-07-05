package com.zeher.zehercraft.processing.client.tesr;

import com.zeher.zehercraft.ZCReference;
import com.zeher.zehercraft.processing.client.tesr.model.ModelCompactorInternals;
import com.zeher.zehercraft.processing.core.block.BlockCompactor;
import com.zeher.zehercraft.processing.core.tile.TileEntityCompactor;
import com.zeher.zeherlib.core.recipe.CompactorRecipes;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RendererCompactorInternals extends TileEntitySpecialRenderer<TileEntityCompactor> {

	@SideOnly(Side.CLIENT)
	private ModelCompactorInternals model;
	
	public RendererCompactorInternals() {
		this.model = new ModelCompactorInternals();
	}
	
	@Override
	public void render(TileEntityCompactor tileentity, double x, double y, double z, float partialTicks, int destroyStage, float a) {
		if (tileentity == null) {
			return;
		}
		
		IBlockState state = tileentity.getWorld().getBlockState(tileentity.getPos());
		BlockCompactor block = (BlockCompactor) state.getBlock();
		EnumFacing facing = state.getValue(block.FACING);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5F, y + 1.5F, z + 0.5F);
		GlStateManager.rotate(180, 1.0F, 0.0F, 0.0F);
		
		this.bindTexture(ZCReference.RESOURCE.PROCESSING.COMPACTOR_LOC_TESR);
		
		GlStateManager.popMatrix();
		
		if (tileentity.canProcess() && tileentity.hasEnergy() && tileentity.getProcessTime(0) > 0) {
			
			GlStateManager.pushMatrix();
			double offset = Math.sin(Minecraft.getSystemTime() / 720D) / 20;
			
			if (facing.equals(EnumFacing.WEST)) {
				GlStateManager.translate(x + 0.5F, y + 1.5F, z + 0.585F + offset);
			} else if (facing.equals(EnumFacing.EAST)) {
				GlStateManager.translate(x + 0.5F, y + 1.5F, z + 0.415F + offset);
			} else {
				GlStateManager.translate(x + 0.415F + offset, y + 1.5F, z + 0.5F);
			}
					
			GlStateManager.rotate(180, 1.0F, 0.0F, 0.0F);

			if (facing.equals(EnumFacing.WEST)) {
				GlStateManager.rotate(270, 0.0F, 1.0F, 0.0F);
			} else if (facing.equals(EnumFacing.EAST)) {
				GlStateManager.rotate(90, 0.0F, 1.0F, 0.0F);
			}
			
			this.model.renderLeftArm(0.0625F);
			
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			
			if (facing.equals(EnumFacing.WEST)) {
				GlStateManager.translate(x + 0.5, y + 1.5F, z + 0.415F - offset);
			} else if (facing.equals(EnumFacing.EAST)) {
				GlStateManager.translate(x + 0.5, y + 1.5F, z + 0.585F - offset);
			} else {
				GlStateManager.translate(x + 0.585F - offset, y + 1.5F, z + 0.5F);
			}
			
			
			GlStateManager.rotate(180, 1.0F, 0.0F, 0.0F);

			if (facing.equals(EnumFacing.WEST)) {
				GlStateManager.rotate(270, 0.0F, 1.0F, 0.0F);
			} else if (facing.equals(EnumFacing.EAST)) {
				GlStateManager.rotate(90, 0.0F, 1.0F, 0.0F);
			}
			
			this.model.renderRightArm(0.0625F);
			
			GlStateManager.popMatrix();
			
		} else {
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5F, y + 1.5F, z + 0.5F);
			GlStateManager.rotate(180, 1.0F, 0.0F, 0.0F);
			
			if (facing.equals(EnumFacing.WEST)) {
				GlStateManager.rotate(270, 0.0F, 1.0F, 0.0F);
			} else if (facing.equals(EnumFacing.EAST)) {
				GlStateManager.rotate(90, 0.0F, 1.0F, 0.0F);
			}
			
			this.model.renderLeftArm(0.0625F);
			this.model.renderRightArm(0.0625F);
			
			GlStateManager.popMatrix();
		}
		
		if (tileentity.getProcessTime(0) > (tileentity.getProcessSpeed() - 10)) {
			
			GlStateManager.pushMatrix();
			
			ItemStack result = CompactorRecipes.getInstance().getCompactingResult(tileentity.getStackInSlot(0));
			
			GlStateManager.translate(x + 0.5, y + 0.65, z + 0.5);
			
			if (facing.equals(EnumFacing.SOUTH)) {
				GlStateManager.rotate(180, 0.0F, 1.0F, 0.0F);
			} else  if (facing.equals(EnumFacing.WEST)) {
				GlStateManager.rotate(90, 0.0F, 1.0F, 0.0F);
			} else if (facing.equals(EnumFacing.EAST)) {
				GlStateManager.rotate(270, 0.0F, 1.0F, 0.0F);
			} 
			
			if (result.getItem() instanceof ItemBlock) {
				GlStateManager.translate(0.0F, -0.05F, 0.0F);
				GlStateManager.scale(0.6F, 0.6F, 0.6F);
			} else {
				GlStateManager.translate(0.0F, -0.2F, 0.0F);
				GlStateManager.rotate(90, 1.0F, 0F, 0F);
				GlStateManager.scale(0.4F, 0.4F, 0.4F);
			}
			
			GlStateManager.pushAttrib();
			
			RenderHelper.disableStandardItemLighting();
			Minecraft.getMinecraft().getRenderItem().renderItem(result, ItemCameraTransforms.TransformType.FIXED);
			RenderHelper.enableStandardItemLighting();
	
			GlStateManager.popAttrib();
			GlStateManager.popMatrix();
			
		} else if (!tileentity.getStackInSlot(0).isEmpty()) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5, y + 0.65, z + 0.5);
			
			if (facing.equals(EnumFacing.SOUTH)) {
				GlStateManager.rotate(180, 0.0F, 1.0F, 0.0F);
			} else if (facing.equals(EnumFacing.WEST)) {
				GlStateManager.rotate(90, 0.0F, 1.0F, 0.0F);
			} else if (facing.equals(EnumFacing.EAST)) {
				GlStateManager.rotate(270, 0.0F, 1.0F, 0.0F);
			} 
			
			if (tileentity.getStackInSlot(0).getItem() instanceof ItemBlock) {
				GlStateManager.translate(0.0F, -0.05F, 0.0F);
				GlStateManager.scale(0.6F, 0.6F, 0.6F);
			} else {
				GlStateManager.translate(0.0F, -0.2F, 0.0F);
				GlStateManager.rotate(90, 1.0F, 0F, 0F);
				GlStateManager.scale(0.4F, 0.4F, 0.4F);
			}
			
			GlStateManager.pushAttrib();
			
			RenderHelper.disableStandardItemLighting();
			Minecraft.getMinecraft().getRenderItem().renderItem(tileentity.getStackInSlot(0), ItemCameraTransforms.TransformType.FIXED);
			RenderHelper.enableStandardItemLighting();
	
			GlStateManager.popAttrib();
			GlStateManager.popMatrix();
		}
	}
}