package com.tcn.dimensionalpocketsii.pocket.core.management;

import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.block.entity.AbstractBlockEntityPocket;
import com.tcn.dimensionalpocketsii.pocket.core.event.PocketBlockEvent;
import com.tcn.dimensionalpocketsii.pocket.core.event.PocketEvent;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

public class PocketEventFactory {
	
	public static boolean onPocketBlockPlaced(Level levelIn, LivingEntity playerIn, AbstractBlockEntityPocket blockEntityIn, BlockPos posIn) {
		return !(MinecraftForge.EVENT_BUS.post(new PocketBlockEvent.PlacePocketBlock(levelIn, playerIn, blockEntityIn, posIn)));
	}
	
	public static boolean onPocketBlockPickup(Level levelIn, LivingEntity playerIn, AbstractBlockEntityPocket blockEntityIn, BlockPos posIn) {
		return !(MinecraftForge.EVENT_BUS.post(new PocketBlockEvent.PickupPocketBlock(levelIn, playerIn, blockEntityIn, posIn)));
	}
	
	public static boolean onPocketGenerate(Level levelIn, Pocket pocketIn) {
		return !(MinecraftForge.EVENT_BUS.post(new PocketEvent.GeneratePocketEvent(levelIn, pocketIn)));
	}
}
