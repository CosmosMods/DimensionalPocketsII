package com.zeher.dimensionalpockets.core.command;


import com.zeher.zeherlib.api.client.util.TextHelper;

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
			TextComponentString header = new TextComponentString(TextHelper.GREEN + "--- Showing help for Dimensional Pockets ---");
			
			TextComponentString command_one = new TextComponentString(TextHelper.LIGHT_GRAY + "/dimshift" + TextHelper.LIGHT_RED + " <destination dimension>(As a number)");
			TextComponentString command_one_desc = new TextComponentString(TextHelper.TEAL + " -- Teleports you into the selected dimension using its ID.");
			TextComponentString command_two = new TextComponentString(TextHelper.LIGHT_GRAY + "/recoverpocket" + TextHelper.LIGHT_RED + "<pocket number> (Specifies the pocket to recover) / [length] (This will show how many pockets exist in the registry)");
			TextComponentString command_two_desc = new TextComponentString(TextHelper.TEAL + "When in creative mode, recovers a pocket whose block has been destroyed.");
			
			player.sendMessage(header);
			player.sendMessage(command_one);
			player.sendMessage(command_one_desc);
			player.sendMessage(command_two);
			player.sendMessage(command_two_desc);
		}
	}
}