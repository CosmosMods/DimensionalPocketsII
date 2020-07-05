package com.zeher.zehercraft.processing.client.tesr;

import java.util.ArrayList;

import com.zeher.zehercraft.processing.core.tile.TileEntitySynthesiser;
import com.zeher.zehercraft.processing.core.tile.TileEntitySynthesiserStand;
import com.zeher.zeherlib.api.client.util.TESRUtil;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RendererSynthesiser extends TileEntitySpecialRenderer<TileEntitySynthesiser> {

	@SideOnly(Side.CLIENT)
	public RendererSynthesiser() { }
	
	@Override
	public void render(TileEntitySynthesiser tileentity, double x, double y, double z, float partialTicks, int destroyStage, float a) {
		if (tileentity == null) {
			return;
		}
		
		World world = Minecraft.getMinecraft().world;
		BlockPos pos = tileentity.getPos();
		IBlockState state = world.getBlockState(pos.offset(EnumFacing.UP));
		
		if (!tileentity.getStackInSlot(0).isEmpty() && !(state.getBlock().isOpaqueCube(state))) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5F, y + 1.3F, z + 0.5F);
			GlStateManager.rotate(Minecraft.getSystemTime() / 720.0F * 31.830988F, 0.0F, 1.0F, 0.0F);
			
			if ((tileentity.getStackInSlot(0).getItem() instanceof ItemBlock)) {
				GlStateManager.scale(0.8F, 0.8F, 0.8F);
			} else {
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
			}
			
			GlStateManager.pushAttrib();
	
			RenderHelper.disableStandardItemLighting();
			Minecraft.getMinecraft().getRenderItem().renderItem(tileentity.getStackInSlot(0), ItemCameraTransforms.TransformType.FIXED);
			RenderHelper.enableStandardItemLighting();
	
			GlStateManager.popAttrib();
			GlStateManager.popMatrix();
		}
		
		if (tileentity.canProcessItemFour()) {
			ArrayList<TileEntity> tiles = tileentity.getTilesFour();
			
			for (int i = 0; i < tiles.size(); i++) {
				if (tiles.get(i) instanceof TileEntitySynthesiserStand) {
					if (! (((TileEntitySynthesiserStand) tiles.get(i)).getStackInSlot(0).isEmpty())) {
						TESRUtil.renderLaser(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, tiles.get(i).getPos().getX() + 0.5, tiles.get(i).getPos().getY() + 0.5,  tiles.get(i).getPos().getZ() + 0.5, 0, 1F, 0.1F, tileentity.getColour());
					}
				}
			}
		} else if (tileentity.canProcessItemEight()) {
			ArrayList<TileEntity> tiles = tileentity.getTilesEight();
			
			for (int i = 0; i < tiles.size(); i++) {
				if (tiles.get(i) instanceof TileEntitySynthesiserStand) {
					if (! (((TileEntitySynthesiserStand) tiles.get(i)).getStackInSlot(0).isEmpty())) {
						TESRUtil.renderLaser(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, tiles.get(i).getPos().getX() + 0.5, tiles.get(i).getPos().getY() + 0.5,  tiles.get(i).getPos().getZ() + 0.5, 0, 1F, 0.1F, tileentity.getColour());
					}
				}
			}
		}
	}
}