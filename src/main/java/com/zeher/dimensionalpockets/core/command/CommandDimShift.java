package com.zeher.dimensionalpockets.core.command;

import java.awt.Dimension;

import org.apache.commons.lang3.StringUtils;

import com.zeher.dimensionalpockets.core.dimshift.DimensionalShifter;
import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.dimshift.DimensionalShiftUtils;
import com.zeher.dimensionalpockets.core.util.DimUtils;
import com.zeher.trzcore.api.TRZTextUtil;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DimensionType;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;

public class CommandDimShift extends CommandBase {

	@Override
	public String getName() {
		return "dimshift";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "command.dimshift.usage";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
        return 4;
    }

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
            throw new WrongUsageException("command.dimshift.usage", new Object[0]);
        }
		
		EntityPlayerMP player;
		
		if(!args[0].equals(null) || !(args[0].length() == 0)) {
			player = getCommandSenderAsPlayer(sender);
			
			if(!player.world.equals(null)) {
				if(StringUtils.isNumeric(args[0])) {
					int dim_id = Integer.parseInt(args[0]);
					
					if(!(DimensionalPockets.can_teleport_to_dim) && dim_id == 98) {
						TextComponentString string = new TextComponentString(TRZTextUtil.RED + "You cannot teleport to that dimension.");
						player.sendMessage(string);
						return;
					}
					
					WorldServer world_server = server.worldServerForDimension(dim_id);
					WorldInfo world_info = world_server.getWorldInfo();
					
					BlockPos spawn_point = world_server.getSpawnPoint();
					BlockPos new_spawn = new BlockPos(spawn_point.getX(), world_server.getHeight(spawn_point.getX() - 1, spawn_point.getZ()), spawn_point.getZ());
					
					BlockPos dim_pos = DimensionManager.getProvider(dim_id).getSpawnPoint();
					
					//System.out.println("PRO" + dim_pos + " -- ");
					DimensionType dimension = DimensionType.getById(dim_id);
					String dim_name = dimension.getName();
					
					DimensionalShifter teleporter = DimensionalShiftUtils.createTeleporter(dim_id, new_spawn, 0, 0);
					
					if(player.dimension == dim_id) {
						TextComponentString comp = new TextComponentString(TRZTextUtil.RED + "You are already in that dimension!");
						player.sendMessage(comp);
						
						//player.setPositionAndUpdate(spawn_point.getX(), spawn_point.getY(), spawn_point.getZ());
					} else {
						TextComponentString comp = new TextComponentString(TRZTextUtil.TEAL + "Shifted: " + TRZTextUtil.GREEN + player.getName() + TRZTextUtil.TEAL + " to dimension: " + dim_id + ".");
						player.sendMessage(comp);
						
						DimensionalShiftUtils.shiftPlayerToDimension(player, dim_id, teleporter);
						System.out.println("Shifted " + player.getName() + " to dimension: " + dim_name + "(" + new_spawn.getX() + ", " + new_spawn.getY() + ", " + new_spawn.getZ() + ")");
					}
				} 
				
				else if(args[0].contains("-")){
					String args1 = args[0].substring(1);
					int dim_id = Integer.parseInt(args1);
					
					WorldServer world_server = server.worldServerForDimension(-dim_id);
					BlockPos spawn_point = DimensionManager.getWorld(-dim_id).getSpawnPoint();
					
					DimensionType dimension = DimensionType.getById(-dim_id);
					String dim_name = dimension.getName();
					DimensionalShifter teleporter = DimensionalShiftUtils.createTeleporter(-dim_id, spawn_point, 0, 0);
					
					if(player.dimension == -dim_id) {
						TextComponentString comp = new TextComponentString(TRZTextUtil.RED + "You are already in that dimension!");
						player.sendMessage(comp);
					} else {
						TextComponentString comp = new TextComponentString(TRZTextUtil.TEAL + "Shifted: " + TRZTextUtil.GREEN + player.getName() + TRZTextUtil.TEAL + " to dimension: " + dim_id + ".");
						player.sendMessage(comp);
						
						DimensionalShiftUtils.shiftPlayerToDimension(player, -dim_id, teleporter);
						System.out.println("Shifted " + player.getName() + " to dimension: " + dim_name + " (" + spawn_point.getX() + ", " + spawn_point.getY() + ", " + spawn_point.getZ() + ")");
					}
				} else {
					throw new WrongUsageException("command.dimshift.usage", new Object[0]);
				}
			}
		}
	}

}
