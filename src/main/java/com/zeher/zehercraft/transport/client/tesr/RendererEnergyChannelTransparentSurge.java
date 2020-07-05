package com.zeher.zehercraft.transport.client.tesr;

import org.lwjgl.opengl.GL11;

import com.zeher.zehercraft.ZCReference;
import com.zeher.zehercraft.transport.client.tesr.model.ModelEnergyChannelSurge;
import com.zeher.zehercraft.transport.core.tile.TileEntityEnergyChannelTransparentSurge;
import com.zeher.zeherlib.api.azrf.EnumChannelSideState;
import com.zeher.zeherlib.api.client.tesr.EnumTESRColour;
import com.zeher.zeherlib.api.client.util.TESRUtil;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RendererEnergyChannelTransparentSurge extends TileEntitySpecialRenderer<TileEntityEnergyChannelTransparentSurge> {

	@SideOnly(Side.CLIENT)
	private ModelEnergyChannelSurge model;

	@SideOnly(Side.CLIENT)
	public RendererEnergyChannelTransparentSurge() {
		this.model = new ModelEnergyChannelSurge();
	}
	
	@Override
	public void render(TileEntityEnergyChannelTransparentSurge tileentity, double x, double y, double z, float partialTicks, int destroyStage, float a) {
		TileEntityEnergyChannelTransparentSurge cable = (TileEntityEnergyChannelTransparentSurge) tileentity;
		BlockPos pos = cable.getPos();

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		
		this.bindTexture(ZCReference.RESOURCE.TRANSPORT.ENERGY_CHANNEL_TRANSPARENT_SURGE_LOC_TESR);
		
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
		
		TESRUtil.renderLaser(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ(), 0F, 1F, 0.2F, EnumTESRColour.PURPLE);
		
		/**
		// UP
		if (cable.getStateForConnection(EnumFacing.UP) != ChannelState.DISABLED && cable.getStateForConnection(EnumFacing.UP) != ChannelState.NO_CONN) {
			TESRUtil.renderLaser(pos.getX() + 0.5, pos.getY() + 0.6F, pos.getZ() + 0.5F, pos.offset(EnumFacing.UP).getX() + 0.5F, pos.offset(EnumFacing.UP).getY(), pos.offset(EnumFacing.UP).getZ() + 0.5F, 0F, 1F, 0.1F, EnumTESRColour.PURPLE.colour);
		}
		
		// DOWN
		if (cable.getStateForConnection(EnumFacing.DOWN) != ChannelState.DISABLED && cable.getStateForConnection(EnumFacing.DOWN) != ChannelState.NO_CONN) {
			TESRUtil.renderLaser(pos.getX() + 0.5, pos.getY() + 0.4F, pos.getZ() + 0.5F, pos.offset(EnumFacing.DOWN).getX() + 0.5F, pos.getY(), pos.offset(EnumFacing.DOWN).getZ() + 0.5F, 0F, 1F, 0.1F, EnumTESRColour.PURPLE.colour);
		}
		
		// NORTH
		if (cable.getStateForConnection(EnumFacing.NORTH) != ChannelState.DISABLED && cable.getStateForConnection(EnumFacing.NORTH) != ChannelState.NO_CONN) {
			TESRUtil.renderLaser(pos.getX() + 0.5, pos.getY() + 0.5F, pos.getZ() + 0.5F, pos.offset(EnumFacing.NORTH).getX() + 0.5F, pos.getY() + 0.5F, pos.getZ(), 0F, 1F, 0.1F, EnumTESRColour.PURPLE.colour);
		}
		
		// SOUTH
		if (cable.getStateForConnection(EnumFacing.SOUTH) != ChannelState.DISABLED && cable.getStateForConnection(EnumFacing.SOUTH) != ChannelState.NO_CONN) {
			TESRUtil.renderLaser(pos.getX() + 0.5, pos.getY() + 0.5F, pos.getZ() + 0.5F, pos.offset(EnumFacing.SOUTH).getX() + 0.5F, pos.getY() + 0.5F, pos.offset(EnumFacing.SOUTH).getZ(), 0F, 1F, 0.1F, EnumTESRColour.PURPLE.colour);
		} */
		
		for	(EnumFacing c : EnumFacing.VALUES) {
			if (!cable.getStateForConnection(c).equals(EnumChannelSideState.DISABLED) && !cable.getStateForConnection(c).equals(EnumChannelSideState.NO_CONN)) {
				if (cable.getLastRFRate() > 0) {
				
				}
				//TESRUtil.renderLaser(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, pos.offset(c).getX() + 0.5F, pos.offset(c).getY() + 0.5F, pos.offset(c).getZ() + 0.5f, 1, 1F, 0.1F, EnumTESRColour.PURPLE.colour);		
			}
		}
	}
}