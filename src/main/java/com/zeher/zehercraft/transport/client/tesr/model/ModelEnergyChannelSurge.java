package com.zeher.zehercraft.transport.client.tesr.model;

import com.zeher.zeherlib.api.azrf.EnumChannelSideState;
import com.zeher.zeherlib.api.azrf.EnumChannelSideState;
import com.zeher.zeherlib.api.azrf.IChannel;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;

public class ModelEnergyChannelSurge extends ModelBase {
	
	/**
	 * --- Center Cube ONLY ---
	 */
	private ModelRenderer BASE;
	private ModelRenderer EXTEND_HORIZONTAL;
	private ModelRenderer EXTEND_HORIZONTAL_NS;
	private ModelRenderer EXTEND_VERTICAL;
	
	
	/**
	 * --- Center Cube Connections ---
	 */
	private ModelRenderer SINGLE;
	private ModelRenderer INTERFACE_BASE;
	private ModelRenderer INTERFACE_NORMAL;
	private ModelRenderer INTERFACE_OUTPUT;
	private ModelRenderer INTERFACE_INPUT;
	private ModelRenderer DISABLED;
	
	/**
	 * ---Outer Shell ONLY ---
	 */
	private ModelRenderer SHELL;
	private ModelRenderer SHELL_HORIZONTAL;
	private ModelRenderer SHELL_HORIZONTAL_NS;
	private ModelRenderer SHELL_VERTICAL;
	
	/**
	 * ---Outer Shell Connections ---
	 */
	private ModelRenderer SHELL_SINGLE;
	private ModelRenderer SHELL_INTERFACE;


	public ModelEnergyChannelSurge() {
		this.textureWidth = 128;
		this.textureHeight = 48;

		/** 
		 * BASE
		 * [ 0, 0 ]
		 */
		this.BASE = new ModelRenderer(this, 0, 0);
		this.BASE.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6);
		this.BASE.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.BASE.setTextureSize(128, 48);
		this.BASE.mirror = true;
		this.setRotation(this.BASE, 0.0F, 0.0F, 0.0F);
		
		/**
		 * EXTEND-HORIZONTAL
		 */
		this.EXTEND_HORIZONTAL = new ModelRenderer(this, 0, 12);
		this.EXTEND_HORIZONTAL.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6);
		this.EXTEND_HORIZONTAL.setRotationPoint(0F, 0F, 0F);
		this.EXTEND_HORIZONTAL.setTextureSize(128, 48);
		this.EXTEND_HORIZONTAL.mirror = true;
		this.setRotation(this.EXTEND_HORIZONTAL, 0F, 0F, 0F);
		
		/**
		 * EXTEND-HORIZONTAL-NS
		 */
		this.EXTEND_HORIZONTAL_NS = new ModelRenderer(this, 0, 24);
		this.EXTEND_HORIZONTAL_NS.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6);
		this.EXTEND_HORIZONTAL_NS.setRotationPoint(0F, 0F, 0F);
		this.EXTEND_HORIZONTAL_NS.setTextureSize(128, 48);
		this.EXTEND_HORIZONTAL_NS.mirror = true;
		this.setRotation(this.EXTEND_HORIZONTAL_NS, 0F, 0F, 0F);
		
		/**
		 * EXTEND-VERTCIAL
		 */
		this.EXTEND_VERTICAL = new ModelRenderer(this, 0, 36);
		this.EXTEND_VERTICAL.addBox(-3F, -3F, -3F, 6, 6, 6);
		this.EXTEND_VERTICAL.setRotationPoint(0F, 0F, 0F);
		this.EXTEND_VERTICAL.setTextureSize(128, 48);
		this.EXTEND_VERTICAL.mirror = true;
		this.setRotation(this.EXTEND_VERTICAL, 0F, 0F, 0F);
		
		/**
		 * SINGLE
		 */
		this.SINGLE = new ModelRenderer(this, 24, 0);
		this.SINGLE.addBox(3F, -3F, -3F, 5, 6, 6);
		this.SINGLE.setRotationPoint(0F, 0F, 0F);
		this.SINGLE.setTextureSize(128, 48);
		this.SINGLE.mirror = true;
		this.setRotation(this.SINGLE, 0F, 0F, 0F);
		
		/**
		 * INTERFACE-BASE
		 */
		this.INTERFACE_BASE = new ModelRenderer(this, 24, 12);
		this.INTERFACE_BASE.addBox(3F, -3F, -3F, 2, 6, 6);
		this.INTERFACE_BASE.setRotationPoint(0F, 0F, 0F);
		this.INTERFACE_BASE.setTextureSize(128, 48);
		this.INTERFACE_BASE.mirror = true;
		this.setRotation(this.INTERFACE_BASE, 0F, 0F, 0F);
		
		/**
		 * INTERFACE-INPUT
		 */
		this.INTERFACE_INPUT = new ModelRenderer(this, 46, 16);
		this.INTERFACE_INPUT.addBox(5.0F, -4F, -4F, 3, 8, 8);
		this.INTERFACE_INPUT.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.INTERFACE_INPUT.setTextureOffset(128, 48);
		this.INTERFACE_INPUT.mirror = true;
		this.setRotation(this.INTERFACE_INPUT, 0.0F, 0.0F, 0.0F);
		
		/**
		 * INTERFACE-OUTPUT
		 */
		this.INTERFACE_OUTPUT = new ModelRenderer(this, 46, 32);
		this.INTERFACE_OUTPUT.addBox(5.0F, -4F, -4F, 3, 8, 8);
		this.INTERFACE_OUTPUT.setRotationPoint(0F, 0F, 0F);
		this.INTERFACE_OUTPUT.setTextureOffset(128, 48);
		this.INTERFACE_OUTPUT.mirror = true;
		this.setRotation(this.INTERFACE_OUTPUT, 0F, 0F, 0F);
		
		/**
		 * INTERFACE-NORMAL
		 */
		this.INTERFACE_NORMAL = new ModelRenderer(this, 46, 0);
		this.INTERFACE_NORMAL.addBox(5.0F, -4F, -4F, 3, 8, 8);
		this.INTERFACE_NORMAL.setRotationPoint(0F, 0F, 0F);
		this.INTERFACE_NORMAL.setTextureOffset(128, 48);
		this.INTERFACE_NORMAL.mirror = true;
		this.setRotation(this.INTERFACE_NORMAL, 0F, 0F, 0F);
		
		/**
		 * DISABLED
		 */
		this.DISABLED = new ModelRenderer(this, 24, 24);
		this.DISABLED.addBox(3.5F, -3F, -3F, 1, 6, 6);
		this.DISABLED.setRotationPoint(0F, 0F, 0F);
		this.DISABLED.setTextureSize(128, 48);
		this.DISABLED.mirror = true;
		this.setRotation(this.DISABLED, 0F, 0F, 0F);
		
		/** --------------------------------------------------------------------------------------- */
		
		/**
		 * --- SURGE BEGIN ---
		 */
		this.SHELL = new ModelRenderer(this, 68, 0);
		this.SHELL.addBox(-3.5F, -3.5F, -3.5F, 7, 7, 7);
		this.SHELL.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.SHELL.setTextureSize(128, 48);
		this.SHELL.mirror = true;
		this.setRotation(this.SHELL, 0.0F, 0.0F, 0.0F);
		
		/**
		 * EXTEND-HORIZONTAL
		 */
		this.SHELL_HORIZONTAL = new ModelRenderer(this, 68, 14);
		this.SHELL_HORIZONTAL.addBox(-3.5F, -3.5F, -3.5F, 7, 7, 7);
		this.SHELL_HORIZONTAL.setRotationPoint(0F, 0F, 0F);
		this.SHELL_HORIZONTAL.setTextureSize(128, 48);
		this.SHELL_HORIZONTAL.mirror = true;
		this.setRotation(this.SHELL_HORIZONTAL, 0F, 0F, 0F);
		
		/**
		 * SHELL-HORIZONTAL-NS
		 */
		this.SHELL_HORIZONTAL_NS = new ModelRenderer(this, 68, 28);
		this.SHELL_HORIZONTAL_NS.addBox(-3.5F, -3.5F, -3.5F, 7, 7, 7);
		this.SHELL_HORIZONTAL_NS.setRotationPoint(0F, 0F, 0F);
		this.SHELL_HORIZONTAL_NS.setTextureSize(128, 48);
		this.SHELL_HORIZONTAL_NS.mirror = true;
		this.setRotation(this.SHELL_HORIZONTAL_NS, 0F, 0F, 0F);
		
		/**
		 * SHELL-VERTCIAL
		 */
		this.SHELL_VERTICAL = new ModelRenderer(this, 96, 0);
		this.SHELL_VERTICAL.addBox(-3.5F, -3.5F, -3.5F, 7, 7, 7);
		this.SHELL_VERTICAL.setRotationPoint(0F, 0F, 0F);
		this.SHELL_VERTICAL.setTextureSize(128, 48);
		this.SHELL_VERTICAL.mirror = true;
		this.setRotation(this.SHELL_VERTICAL, 0F, 0F, 0F);
		
		/**
		 * SINGLE
		 */
		this.SHELL_SINGLE = new ModelRenderer(this, 96, 14);
		this.SHELL_SINGLE.addBox(3.5F, -3.5F, -3.5F, 5, 7, 7);
		this.SHELL_SINGLE.setRotationPoint(0F, 0F, 0F);
		this.SHELL_SINGLE.setTextureSize(128, 48);
		this.SHELL_SINGLE.mirror = true;
		this.setRotation(this.SHELL_SINGLE, 0F, 0F, 0F);
		
		/**
		 * INTERFACE-BASE
		 */
		this.SHELL_INTERFACE = new ModelRenderer(this, 96, 28);
		this.SHELL_INTERFACE.addBox(3.5F, -3.5F, -3.5F, 2, 7, 7);
		this.SHELL_INTERFACE.setRotationPoint(0F, 0F, 0F);
		this.SHELL_INTERFACE.setTextureSize(128, 48);
		this.SHELL_INTERFACE.mirror = true;
		this.setRotation(this.SHELL_INTERFACE, 0F, 0F, 0F);
	}

	public void Render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.BASE.render(f5);
		this.SHELL.render(f5);
	}
	
	public void renderBasedOnTile(IChannel.Energy tile, float scale) {
		//EnumChannelState[] array = tile.getSideArray();
		
		if (tile.getStateForConnection(EnumFacing.UP).equals(EnumChannelSideState.NO_CONN) && tile.getStateForConnection(EnumFacing.DOWN).equals(EnumChannelSideState.NO_CONN)
				&& tile.getStateForConnection(EnumFacing.NORTH).equals(EnumChannelSideState.NO_CONN) && tile.getStateForConnection(EnumFacing.SOUTH).equals(EnumChannelSideState.NO_CONN)
				&& tile.getStateForConnection(EnumFacing.EAST).equals(EnumChannelSideState.NO_CONN) && tile.getStateForConnection(EnumFacing.WEST).equals(EnumChannelSideState.NO_CONN)) {
			this.renderBase(scale);
		} else if (tile.getStateForConnection(EnumFacing.UP).equals(EnumChannelSideState.INTERFACE_NORMAL) || tile.getStateForConnection(EnumFacing.DOWN).equals(EnumChannelSideState.INTERFACE_NORMAL)
				|| tile.getStateForConnection(EnumFacing.NORTH).equals(EnumChannelSideState.INTERFACE_NORMAL) || tile.getStateForConnection(EnumFacing.SOUTH).equals(EnumChannelSideState.INTERFACE_NORMAL)
				|| tile.getStateForConnection(EnumFacing.EAST).equals(EnumChannelSideState.INTERFACE_NORMAL) || tile.getStateForConnection(EnumFacing.WEST).equals(EnumChannelSideState.INTERFACE_NORMAL)) {
			this.renderBase(scale);
		} else if (tile.getStateForConnection(EnumFacing.UP).equals(EnumChannelSideState.INTERFACE_INPUT) || tile.getStateForConnection(EnumFacing.DOWN).equals(EnumChannelSideState.INTERFACE_INPUT)
				|| tile.getStateForConnection(EnumFacing.NORTH).equals(EnumChannelSideState.INTERFACE_INPUT) || tile.getStateForConnection(EnumFacing.SOUTH).equals(EnumChannelSideState.INTERFACE_INPUT)
				|| tile.getStateForConnection(EnumFacing.EAST).equals(EnumChannelSideState.INTERFACE_INPUT) || tile.getStateForConnection(EnumFacing.WEST).equals(EnumChannelSideState.INTERFACE_INPUT)) {
			this.renderBase(scale);
		} else if (tile.getStateForConnection(EnumFacing.UP).equals(EnumChannelSideState.INTERFACE_OUTPUT) || tile.getStateForConnection(EnumFacing.DOWN).equals(EnumChannelSideState.INTERFACE_OUTPUT)
				|| tile.getStateForConnection(EnumFacing.NORTH).equals(EnumChannelSideState.INTERFACE_OUTPUT) || tile.getStateForConnection(EnumFacing.SOUTH).equals(EnumChannelSideState.INTERFACE_OUTPUT)
				|| tile.getStateForConnection(EnumFacing.EAST).equals(EnumChannelSideState.INTERFACE_OUTPUT) || tile.getStateForConnection(EnumFacing.WEST).equals(EnumChannelSideState.INTERFACE_OUTPUT)) {
			this.renderBase(scale);
		} else if (tile.getStateForConnection(EnumFacing.UP).equals(EnumChannelSideState.CABLE) && tile.getStateForConnection(EnumFacing.DOWN).equals(EnumChannelSideState.CABLE)
				&& !tile.getStateForConnection(EnumFacing.NORTH).equals(EnumChannelSideState.CABLE) && !tile.getStateForConnection(EnumFacing.SOUTH).equals(EnumChannelSideState.CABLE)
				&& !tile.getStateForConnection(EnumFacing.EAST).equals(EnumChannelSideState.CABLE) && !tile.getStateForConnection(EnumFacing.WEST).equals(EnumChannelSideState.CABLE)) {
			this.renderVertical(scale);
		} else if (tile.getStateForConnection(EnumFacing.EAST).equals(EnumChannelSideState.CABLE) && tile.getStateForConnection(EnumFacing.WEST).equals(EnumChannelSideState.CABLE)
				&& !tile.getStateForConnection(EnumFacing.NORTH).equals(EnumChannelSideState.CABLE) && !tile.getStateForConnection(EnumFacing.SOUTH).equals(EnumChannelSideState.CABLE)
				&& !tile.getStateForConnection(EnumFacing.UP).equals(EnumChannelSideState.CABLE) && !tile.getStateForConnection(EnumFacing.DOWN).equals(EnumChannelSideState.CABLE)) {
			this.renderHorizontal(scale);
		} else if (tile.getStateForConnection(EnumFacing.NORTH).equals(EnumChannelSideState.CABLE) && tile.getStateForConnection(EnumFacing.SOUTH).equals(EnumChannelSideState.CABLE)
				&& !tile.getStateForConnection(EnumFacing.EAST).equals(EnumChannelSideState.CABLE) && !tile.getStateForConnection(EnumFacing.WEST).equals(EnumChannelSideState.CABLE)
				&& !tile.getStateForConnection(EnumFacing.UP).equals(EnumChannelSideState.CABLE) && !tile.getStateForConnection(EnumFacing.DOWN).equals(EnumChannelSideState.CABLE)) {
			this.renderHorizontalNS(scale);
		} else {
			this.renderBase(scale);
		}
		
		this.renderSide(tile.getStateForConnection(EnumFacing.SOUTH), -1.5707964F, 0F, scale);
		this.renderSide(tile.getStateForConnection(EnumFacing.NORTH), 1.5707964F, 0F, scale);
		
		this.renderSide(tile.getStateForConnection(EnumFacing.EAST), 0F, 0F, scale);
		this.renderSide(tile.getStateForConnection(EnumFacing.WEST), 3.1415927F, 0F, scale);
		
		this.renderSide(tile.getStateForConnection(EnumFacing.UP), 0.0F, 1.5707964F, scale);
		this.renderSide(tile.getStateForConnection(EnumFacing.DOWN), 0.0F, -1.5707964F, scale);
	}

	private void renderSide(EnumChannelSideState state, float Y, float Z, float scale) {
		this.renderSideState(state, Y, Z, scale);
	}
	
	private void renderBase(float scale) {
		this.BASE.render(scale);
		this.SHELL.render(scale);
	}
	
	private void renderHorizontal(float scale) {
		this.EXTEND_HORIZONTAL.render(scale);
		this.SHELL_HORIZONTAL.render(scale);
	}
	
	private void renderHorizontalNS(float scale) {
		this.EXTEND_HORIZONTAL_NS.render(scale);
		this.SHELL_HORIZONTAL_NS.render(scale);
	}
	
	private void renderVertical(float scale) {
		this.EXTEND_VERTICAL.render(scale);
		this.SHELL_VERTICAL.render(scale);
	}
	
	private void renderSideState(EnumChannelSideState state, float Y, float Z, float scale) {
		if (state.equals(EnumChannelSideState.CABLE)) {
			this.SINGLE.rotateAngleY = Y;
			this.SINGLE.rotateAngleZ = Z;
			this.SINGLE.render(scale);
			
			this.SHELL_SINGLE.rotateAngleY = Y;
			this.SHELL_SINGLE.rotateAngleZ = Z;
			this.SHELL_SINGLE.render(scale);
		}
		
		if (state.equals(EnumChannelSideState.CABLE_OTHER)) {
			this.SINGLE.rotateAngleY = Y;
			this.SINGLE.rotateAngleZ = Z;
			this.SINGLE.render(scale);
		}
		
		if (state.equals(EnumChannelSideState.INTERFACE_NORMAL)) {
			this.INTERFACE_BASE.rotateAngleY = Y;
			this.INTERFACE_BASE.rotateAngleZ = Z;
			this.INTERFACE_BASE.render(scale);
			
			this.SHELL_INTERFACE.rotateAngleY = Y;
			this.SHELL_INTERFACE.rotateAngleZ = Z;
			this.SHELL_INTERFACE.render(scale);
			
			this.INTERFACE_NORMAL.rotateAngleY = Y;
			this.INTERFACE_NORMAL.rotateAngleZ = Z;
			this.INTERFACE_NORMAL.render(scale);
		}
		
		if (state.equals(EnumChannelSideState.INTERFACE_INPUT)) {
			this.INTERFACE_BASE.rotateAngleY = Y;
			this.INTERFACE_BASE.rotateAngleZ = Z;
			this.INTERFACE_BASE.render(scale);

			this.SHELL_INTERFACE.rotateAngleY = Y;
			this.SHELL_INTERFACE.rotateAngleZ = Z;
			this.SHELL_INTERFACE.render(scale);
			
			this.INTERFACE_INPUT.rotateAngleY = Y;
			this.INTERFACE_INPUT.rotateAngleZ = Z;
			this.INTERFACE_INPUT.render(scale);
		}
		
		if (state.equals(EnumChannelSideState.INTERFACE_OUTPUT)) {
			this.INTERFACE_BASE.rotateAngleY = Y;
			this.INTERFACE_BASE.rotateAngleZ = Z;
			this.INTERFACE_BASE.render(scale);

			this.SHELL_INTERFACE.rotateAngleY = Y;
			this.SHELL_INTERFACE.rotateAngleZ = Z;
			this.SHELL_INTERFACE.render(scale);
			
			this.INTERFACE_OUTPUT.rotateAngleY = Y;
			this.INTERFACE_OUTPUT.rotateAngleZ = Z;
			this.INTERFACE_OUTPUT.render(scale);
		}
		
		if (state.equals(EnumChannelSideState.DISABLED)) {
			this.DISABLED.rotateAngleY = Y;
			this.DISABLED.rotateAngleZ = Z;
			this.DISABLED.render(scale);
			
		}
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotataionAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}
}