package com.zeher.dimensionalpockets.core.handlers;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import com.zeher.dimensionalpockets.DimensionalPockets;

public class GuiHandler implements IGuiHandler {

	public GuiHandler() {
		NetworkRegistry.INSTANCE.registerGuiHandler(DimensionalPockets.instance, this);
	}

	public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tile = world.getTileEntity(pos);
		if (!tile.equals(null)) {
			switch (ID) {
			case 0:
				//return new ContainerPoweredCapacitorDirection(player.inventory, (TileEntityPoweredCapacitor) tile);
			
			}
		}
		return null;
	}

	public Gui getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tile = world.getTileEntity(pos);
		switch (ID) {

		case 0:
			//return new GuiPoweredCapacitorDirection(player.inventory, (TileEntityPoweredCapacitor) tile, world);
		
		}
		return null;
	}

}
