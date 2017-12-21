package com.zeher.dimensionalpockets.core.command;

import com.zeher.trzlib.api.TRZTextUtil;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandDimHelp extends CommandBase {

	@Override
	public String getName() {
		return "dimhelp";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "command.dimhelp.usage";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
        return 2;
    }

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		
		if (args.length < 1) {
			TextComponentString header = new TextComponentString(TRZTextUtil.GREEN + "--- Showing help for Dimensional Pockets ---");
			
			TextComponentString command_one = new TextComponentString(TRZTextUtil.LIGHT_GRAY + "/dimshift" + TRZTextUtil.LIGHT_RED + " <destination dimension>");
			TextComponentString command_one_desc = new TextComponentString(TRZTextUtil.TEAL + " -- Teleports you into the selected dimension using its ID.");
			
			player.sendMessage(header);
			player.sendMessage(command_one);
			player.sendMessage(command_one_desc);
		
		}
	}

}
