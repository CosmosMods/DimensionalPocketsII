package com.zeher.dimensionalpockets.core.handler;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.pocket.client.container.ContainerDimensionalPocket;
import com.zeher.dimensionalpockets.pocket.client.container.ContainerDimensionalPocketWallConnector;
import com.zeher.dimensionalpockets.pocket.client.gui.GuiPocketAllowedPlayers;
import com.zeher.dimensionalpockets.pocket.client.gui.GuiPocketAllowedPlayersWallConnector;
import com.zeher.dimensionalpockets.pocket.tileentity.TileEntityDimensionalPocket;
import com.zeher.dimensionalpockets.pocket.tileentity.TileEntityDimensionalPocketWallConnector;

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
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		
		if (!tile.equals(null)) {
			switch (ID) {
				case 0:
					return new ContainerDimensionalPocket(player.inventory, (TileEntityDimensionalPocket) tile);
				case 1:
					return new ContainerDimensionalPocketWallConnector(player.inventory, (TileEntityDimensionalPocketWallConnector) tile);
			}
		}
		
		return null;
	}

	@SideOnly(Side.CLIENT)
	public Gui getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		
		switch (ID) {
			case 0:
				return new GuiPocketAllowedPlayers(player.inventory, (TileEntityDimensionalPocket) tile);
			case 1:
				return new GuiPocketAllowedPlayersWallConnector(player.inventory, (TileEntityDimensionalPocketWallConnector) tile);
		}
		
		return null;
	}
}