package com.zeher.dimpockets.core.manager;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.zeher.dimpockets.DimensionalPockets;
import com.zeher.dimpockets.core.log.ModLogger;
import com.zeher.dimpockets.pocket.client.baked.ModelFluidDisplay;
import com.zeher.dimpockets.pocket.client.baked.ModelFluidDisplayFluid;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.registry.RegistrySimple;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID)
public class ModBakedManager {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onModelBakeEvent(ModelBakeEvent event) {
		if (ModConfigManager.DIMENSIONAL_POCKETS_SYSTEM_MESSAGE) {
			ModLogger.info("<Client/Baked>: ModelBakeEvent begin...");
		}
		
		if (ModConfigManager.DIMENSIONAL_POCKETS_SYSTEM_MESSAGE) {
			ModLogger.info("<Client/Baked>: Registering fluids for render...");
			for (Entry<String, Fluid> entry : FluidRegistry.getRegisteredFluids().entrySet()) {
				Fluid fluid_ = entry.getValue();
				String fluid_name = fluid_.getName();
				System.out.println("[OreCraft/Client]: Fluid: " + fluid_name + " has been registered.");
			}
		}
		
		bakeFluidDisplayModel(event);
	}
	
	public static void bakeFluidDisplayModel(ModelBakeEvent event) {
		Fluid fluid;
		IBakedModel[] bakedFluidModels;

		for (Entry<String, Fluid> entry : FluidRegistry.getRegisteredFluids().entrySet()) {
			fluid = entry.getValue();

			bakedFluidModels = new IBakedModel[ModelFluidDisplay.FLUID_LEVELS];

			for (int x = 0; x < ModelFluidDisplay.FLUID_LEVELS; x++) {
				bakedFluidModels[x] = new ModelFluidDisplayFluid(fluid, x + 1);
			}

			ModelFluidDisplay.FLUID_MODELS.put(entry.getKey(), bakedFluidModels);
		}

		RegistrySimple<ModelResourceLocation, IBakedModel> registry = (RegistrySimple) event.getModelRegistry();
		ArrayList<ModelResourceLocation> modelLocations = Lists.newArrayList();

		String modelPath = "block_dimensional_pocket_wall_fluid_display";

		for (ModelResourceLocation modelLoc : registry.getKeys()) {
			if (modelLoc.getResourceDomain().equals(DimensionalPockets.MOD_ID) && modelLoc.getResourcePath().equals(modelPath) && !modelLoc.getVariant().equals("inventory")) {
				modelLocations.add(modelLoc);
			}
		}

		IBakedModel registeredModel;
		IBakedModel replacementModel;

		for (ModelResourceLocation loc : modelLocations) {
			registeredModel = event.getModelRegistry().getObject(loc);
			replacementModel = new ModelFluidDisplay(registeredModel);
			event.getModelRegistry().putObject(loc, replacementModel);
		}
	}

}