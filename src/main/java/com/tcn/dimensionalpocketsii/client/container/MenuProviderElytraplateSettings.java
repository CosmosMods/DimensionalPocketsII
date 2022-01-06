package com.tcn.dimensionalpocketsii.client.container;

import com.tcn.cosmoslibrary.common.lib.ComponentHelper;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class MenuProviderElytraplateSettings implements MenuProvider {

	@Override
	public AbstractContainerMenu createMenu(int indexIn, Inventory playerInventoryIn, Player playerIn) {
		return ContainerElytraplate.createContainerServerSide(indexIn, playerInventoryIn, playerInventoryIn.getArmor(2), false);
	}

	@Override
	public Component getDisplayName() {
		return ComponentHelper.locComp("dimensionalpocketsii.gui.elytraplate.settings.title");
	}
}
