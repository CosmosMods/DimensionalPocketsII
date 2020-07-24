package com.zeher.dimpockets.core.manager;

import java.util.ArrayList;

import com.zeher.dimpockets.DimensionalPockets;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID)
public class ModSoundManager {
	
	@EventBusSubscriber(modid = DimensionalPockets.MOD_ID)
	public static class GENERIC {

		public static final SoundEvent PORTAL_IN = new SoundEvent(new ResourceLocation(DimensionalPockets.MOD_ID + ":" + "portal_in")).setRegistryName("portal_in");
		public static final SoundEvent PORTAL_OUT = new SoundEvent(new ResourceLocation(DimensionalPockets.MOD_ID + ":" + "portal_out")).setRegistryName("portal_out");
		
		@SubscribeEvent
		public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
			event.getRegistry().registerAll(PORTAL_IN, PORTAL_OUT);
			
		}
	}	
}