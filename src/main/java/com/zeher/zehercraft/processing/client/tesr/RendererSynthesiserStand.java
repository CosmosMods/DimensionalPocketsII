package com.zeher.zehercraft.processing.client.tesr;

import com.zeher.zehercraft.processing.core.tile.TileEntitySynthesiserStand;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RendererSynthesiserStand extends TileEntitySpecialRenderer<TileEntitySynthesiserStand> {
	
	@Override
	public void render(TileEntitySynthesiserStand tileentity, double x, double y, double z, float partialTicks, int destroyStage, float a) {
		if(tileentity == null) {
			return;
		}
		
		int slot = 0;
		
		World world = Minecraft.getMinecraft().world;
		BlockPos pos = tileentity.getPos();
		IBlockState state = world.getBlockState(pos.offset(EnumFacing.UP));
		
		if (!tileentity.getStackInSlot(0).isEmpty() && !(state.getBlock().isOpaqueCube(state))) {
			GlStateManager.pushMatrix();
			GlStateManager.translate((float) x + 0.5F, (float) y + 1.2F, (float) z + 0.5F);
			GlStateManager.rotate((float) Minecraft.getSystemTime() / 720.0F * 31.830988F, 0.0F, 1.0F, 0.0F);
			
			if ((tileentity.getStackInSlot(0).getItem() instanceof ItemBlock)) {
				GlStateManager.scale(0.8F, 0.8F, 0.8F);
			} else {
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
			}
			
			GlStateManager.pushAttrib();
	
			RenderHelper.disableStandardItemLighting();
			Minecraft.getMinecraft().getRenderItem().renderItem(tileentity.getStackInSlot(slot), ItemCameraTransforms.TransformType.FIXED);
			RenderHelper.enableStandardItemLighting();
	
			GlStateManager.popAttrib();
			GlStateManager.popMatrix();
		}
	}
}