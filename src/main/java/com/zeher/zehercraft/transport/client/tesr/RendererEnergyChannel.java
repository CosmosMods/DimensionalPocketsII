package com.zeher.zehercraft.transport.client.tesr;

import org.lwjgl.opengl.GL11;

import com.zeher.zehercraft.ZCReference;
import com.zeher.zehercraft.transport.client.tesr.model.ModelEnergyChannel;
import com.zeher.zehercraft.transport.core.tile.TileEntityEnergyChannel;
import com.zeher.zeherlib.api.azrf.EnumChannelSideState;
import com.zeher.zeherlib.api.azrf.EnumSideState;
import com.zeher.zeherlib.api.client.tesr.EnumTESRColour;
import com.zeher.zeherlib.api.client.util.TESRUtil;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RendererEnergyChannel extends TileEntitySpecialRenderer<TileEntityEnergyChannel> {

	@SideOnly(Side.CLIENT)
	private ModelEnergyChannel model;

	@SideOnly(Side.CLIENT)
	public RendererEnergyChannel() {
		this.model = new ModelEnergyChannel();
	}
	
	@Override
	public void render(TileEntityEnergyChannel tileentity, double x, double y, double z, float partialTicks, int destroyStage, float a) {
		TileEntityEnergyChannel cable = (TileEntityEnergyChannel) tileentity;
		BlockPos pos = cable.getPos();

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		
		this.bindTexture(ZCReference.RESOURCE.TRANSPORT.ENERGY_CHANNEL_LOC_TESR);
		
		GL11.glPushMatrix();
		GL11.glDisable(3008);
		
		this.model.renderBasedOnTile(cable, 0.0625F);

		GL11.glEnable(3008);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
}