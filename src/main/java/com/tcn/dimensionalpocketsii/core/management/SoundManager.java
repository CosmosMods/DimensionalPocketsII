package com.tcn.dimensionalpocketsii.core.management;

import com.tcn.dimensionalpocketsii.DimensionalPockets;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundManager {
	
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DimensionalPockets.MOD_ID);

	public static final RegistryObject<SoundEvent> PORTAL_IN = SOUND_EVENTS.register("sound_portal_in", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(DimensionalPockets.MOD_ID + ":" + "sound_portal_in")));
	public static final RegistryObject<SoundEvent> PORTAL_OUT = SOUND_EVENTS.register("sound_portal_out", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(DimensionalPockets.MOD_ID + ":" + "sound_portal_in")));
	
	public static final RegistryObject<SoundEvent> WOOSH = SOUND_EVENTS.register("sound_woosh", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(DimensionalPockets.MOD_ID + ":" + "sound_woosh")));

	public static final RegistryObject<SoundEvent> TINK = SOUND_EVENTS.register("sound_tink", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(DimensionalPockets.MOD_ID + ":" + "sound_tink")));
	
	public static void register(IEventBus bus) {
		SOUND_EVENTS.register(bus);
	}
}