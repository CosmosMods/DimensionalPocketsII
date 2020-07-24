package com.zeher.dimpockets.core.manager;

import com.zeher.dimpockets.DimensionalPockets;
import com.zeher.dimpockets.pocket.client.container.ContainerConnector;
import com.zeher.dimpockets.pocket.client.container.ContainerPocket;
import com.zeher.dimpockets.pocket.client.gui.GuiConnector;
import com.zeher.dimpockets.pocket.client.gui.GuiPocket;
import com.zeher.dimpockets.pocket.core.tileentity.TileConnector;
import com.zeher.dimpockets.pocket.core.tileentity.TilePocket;

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

public class ModGuiManager implements IGuiHandler {
	
	public ModGuiManager() {
		NetworkRegistry.INSTANCE.registerGuiHandler(DimensionalPockets.INSTANCE, this);
	}
	
	public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		
		if (!tile.equals(null)) {
			switch (ID) {
				case 0:
					return new ContainerPocket(player.inventory, (TilePocket) tile);
				case 1:
					return new ContainerConnector(player.inventory, (TileConnector) tile);
			}
		}
		
		return null;
	}

	@SideOnly(Side.CLIENT)
	public Gui getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		
		switch (ID) {
			case 0:
				return new GuiPocket(player.inventory, (TilePocket) tile);
			case 1:
				return new GuiConnector(player.inventory, (TileConnector) tile);
		}
		
		return null;
	}
}