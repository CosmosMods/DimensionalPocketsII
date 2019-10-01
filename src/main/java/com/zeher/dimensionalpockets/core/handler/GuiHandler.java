package com.zeher.dimensionalpockets.core.handler;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.pocket.client.container.*;
import com.zeher.dimensionalpockets.pocket.client.gui.*;
import com.zeher.dimensionalpockets.pocket.client.tileentity.*;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiHandler implements IGuiHandler {
	
	public GuiHandler() {
		NetworkRegistry.INSTANCE.registerGuiHandler(DimensionalPockets.INSTANCE, this);
	}
	
	public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tile = world.getTileEntity(pos);
		
		if (!tile.equals(null)) {
			switch (ID) {
				case 0:
					return new ContainerDimensionalPocket(player.inventory, (TileEntityDimensionalPocket) tile);
			}
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	public Gui getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tile = world.getTileEntity(pos);
		
		switch (ID) {
			case 0:
				return new GuiPocketAllowedPlayers(player.inventory, (TileEntityDimensionalPocket) tile);
		}
		return null;
	}

}
