package com.zeher.zehercraft.transport.client.tesr;

import org.lwjgl.opengl.GL11;

import com.zeher.zehercraft.ZCReference;
import com.zeher.zehercraft.transport.client.tesr.model.ModelEnergyChannelSurge;
import com.zeher.zehercraft.transport.core.tile.TileEntityEnergyChannelSurge;
import com.zeher.zeherlib.api.azrf.EnumChannelSideState;
import com.zeher.zeherlib.api.client.tesr.EnumTESRColour;
import com.zeher.zeherlib.api.client.util.TESRUtil;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RendererEnergyChannelSurge extends TileEntitySpecialRenderer<TileEntityEnergyChannelSurge> {

	@SideOnly(Side.CLIENT)
	private ModelEnergyChannelSurge model;

	@SideOnly(Side.CLIENT)
	public RendererEnergyChannelSurge() {
		this.model = new ModelEnergyChannelSurge();
	}
	
	@Override
	public void render(TileEntityEnergyChannelSurge tileentity, double x, double y, double z, float partialTicks, int destroyStage, float a) {
		TileEntityEnergyChannelSurge cable = (TileEntityEnergyChannelSurge) tileentity;
		BlockPos pos = cable.getPos();

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		
		this.bindTexture(ZCReference.RESOURCE.TRANSPORT.ENERGY_CHANNEL_SURGE_LOC_TESR);
		
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		this.model.renderBasedOnTile(cable, 0.0625F);
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
}