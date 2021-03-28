package com.tcn.dimensionalpocketsii.core.command;

import javax.annotation.Nullable;

import com.mojang.brigadier.CommandDispatcher;
import com.tcn.cosmoslibrary.common.comp.CosmosColour;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.shift.Shifter;
import com.tcn.dimensionalpocketsii.pocket.core.shift.ShifterCore;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.DimensionArgument;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.Vec3Argument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class DimShiftCommand {

	public static void register(CommandDispatcher<CommandSource> dispatcherIn) {
		dispatcherIn.register(
		Commands.literal("dim").then(Commands.literal("shift")).requires( (commandSource) -> { return commandSource.hasPermission(2); } )
			.then(Commands.literal("shift").then(Commands.argument("target", EntityArgument.player()).then(Commands.argument("dimension", DimensionArgument.dimension())
				.executes((commandContext) -> { 
					return shiftToDimension(commandContext.getSource(), EntityArgument.getPlayer(commandContext, "target"), DimensionArgument.getDimension(commandContext, "dimension"), null); 
				})
			.then(Commands.argument("targetPos", Vec3Argument.vec3())
				.executes((commandContext) -> { 
					return shiftToDimension(commandContext.getSource(), EntityArgument.getPlayer(commandContext, "target"), DimensionArgument.getDimension(commandContext, "dimension"), Vec3Argument.getVec3(commandContext, "targetPos")); 
				})
		)))));
	}
	
	private static int shiftToDimension(CommandSource commandSourceIn, ServerPlayerEntity serverPlayer, ServerWorld serverWorldIn, @Nullable Vector3d locationIn) {
		if (locationIn != null) {
				BlockPos pos = new BlockPos(locationIn);
				RegistryKey<World> dimension = serverWorldIn.dimension();
				
				Shifter shifterIn = Shifter.createTeleporter(dimension, EnumShiftDirection.GENERIC, pos, serverPlayer.getRotationVector().y, serverPlayer.getRotationVector().x, true, false);
				ShifterCore.shiftPlayerToDimension(serverPlayer, shifterIn);
				
				commandSourceIn.sendSuccess(CosmosCompHelper.locComp(CosmosColour.CYAN, false, "dimensionalpocketsii.direction.generic.pre")
						.append(CosmosCompHelper.locComp(CosmosColour.ORANGE, false, "@" + serverPlayer.getDisplayName().getString()))
						.append(CosmosCompHelper.locComp("dimensionalpocketsii.direction.generic.mid"))
						.append(CosmosCompHelper.locComp(CosmosColour.GREEN, false, dimension.location().toString())), true);
		} else {
				RegistryKey<World> dimension = serverWorldIn.dimension();
				
				Shifter shifterIn = Shifter.createTeleporter(dimension, EnumShiftDirection.GENERIC, serverWorldIn.getSharedSpawnPos(), serverPlayer.getRotationVector().y, serverPlayer.getRotationVector().x, true, false);
				ShifterCore.shiftPlayerToDimension(serverPlayer, shifterIn);
				
				commandSourceIn.sendSuccess(CosmosCompHelper.locComp(CosmosColour.CYAN, false, "dimensionalpocketsii.direction.generic.pre")
						.append(CosmosCompHelper.locComp(CosmosColour.ORANGE, false, "@" + serverPlayer.getDisplayName().getString()))
						.append(CosmosCompHelper.locComp("dimensionalpocketsii.direction.generic.mid"))
						.append(CosmosCompHelper.locComp(CosmosColour.GREEN, false, dimension.location().toString())), true);
		}
		
		return 1;
	}
}
