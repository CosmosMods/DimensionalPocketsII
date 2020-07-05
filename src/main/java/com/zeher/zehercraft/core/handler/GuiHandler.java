package com.zeher.zehercraft.core.handler;

import com.zeher.zehercraft.ZCReference;
import com.zeher.zehercraft.ZeherCraft;
import com.zeher.zehercraft.processing.client.container.ContainerCharger;
import com.zeher.zehercraft.processing.client.container.ContainerCompactor;
import com.zeher.zehercraft.processing.client.container.ContainerGrinder;
import com.zeher.zehercraft.processing.client.container.ContainerKiln;
import com.zeher.zehercraft.processing.client.container.ContainerSynthesiser;
import com.zeher.zehercraft.processing.client.gui.GuiCharger;
import com.zeher.zehercraft.processing.client.gui.GuiCompactor;
import com.zeher.zehercraft.processing.client.gui.GuiGrinder;
import com.zeher.zehercraft.processing.client.gui.GuiKiln;
import com.zeher.zehercraft.processing.client.gui.GuiSynthesiser;
import com.zeher.zehercraft.processing.core.tile.TileEntityCharger;
import com.zeher.zehercraft.processing.core.tile.TileEntityCompactor;
import com.zeher.zehercraft.processing.core.tile.TileEntityGrinder;
import com.zeher.zehercraft.processing.core.tile.TileEntityKiln;
import com.zeher.zehercraft.processing.core.tile.TileEntitySynthesiser;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class GuiHandler implements IGuiHandler {
	
	public GuiHandler() {
		NetworkRegistry.INSTANCE.registerGuiHandler(ZeherCraft.INSTANCE, this);
	}

	/**
	 * Specifies the {@link GuiContainer} of the GUI.
	 */
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tile = world.getTileEntity(pos);
		
		if (tile != null) {
			switch (ID) {
				case ZCReference.RESOURCE.PROCESSING.KILN_INDEX:
					return new ContainerKiln(player.inventory, (TileEntityKiln) tile);
				case ZCReference.RESOURCE.PROCESSING.GRINDER_INDEX:
					return new ContainerGrinder(player.inventory, (TileEntityGrinder) tile);
				case ZCReference.RESOURCE.PROCESSING.COMPACTOR_INDEX:
					return new ContainerCompactor(player.inventory, (TileEntityCompactor) tile);
				case ZCReference.RESOURCE.PROCESSING.CHARGER_INDEX:
					return new ContainerCharger(player.inventory, (TileEntityCharger) tile);
				case ZCReference.RESOURCE.PROCESSING.SYNTHESISER_INDEX:
					return new ContainerSynthesiser(player.inventory, (TileEntitySynthesiser) tile);
				default:
					throw new IllegalStateException("No Container exists for ID [" + ID + "]");
			}
		}
		return null;
	}
	
	/**
	 * Specifies the {@link Gui} of the GUI.
	 */
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tile = world.getTileEntity(pos);
		
		if (tile != null) {
			switch (ID) {
				case ZCReference.RESOURCE.PROCESSING.KILN_INDEX:
					return new GuiKiln(player.inventory, (TileEntityKiln) tile);
				case ZCReference.RESOURCE.PROCESSING.GRINDER_INDEX:
					return new GuiGrinder(player.inventory, (TileEntityGrinder) tile);
				case ZCReference.RESOURCE.PROCESSING.COMPACTOR_INDEX:
					return new GuiCompactor(player.inventory, (TileEntityCompactor) tile);
				case ZCReference.RESOURCE.PROCESSING.CHARGER_INDEX:
					return new GuiCharger(player.inventory, (TileEntityCharger) tile);
				case ZCReference.RESOURCE.PROCESSING.SYNTHESISER_INDEX:
					return new GuiSynthesiser(player.inventory, (TileEntitySynthesiser) tile);
				default:
					throw new IllegalStateException("No Gui exists for ID [" + ID + "]");
			}
		}
		return null;
	}
	
	public static void openGui(EntityPlayer player, int index, World world, BlockPos pos) {
		FMLNetworkHandler.openGui(player, ZeherCraft.INSTANCE, index, world, pos.getX(), pos.getY(), pos.getZ());
	}
}