package com.zeher.dimpockets.core.command;

import org.apache.commons.lang3.StringUtils;

import com.zeher.dimpockets.DimReference.CONSTANT;
import com.zeher.dimpockets.core.manager.ModConfigManager;
import com.zeher.dimpockets.pocket.core.Pocket;
import com.zeher.dimpockets.pocket.core.manager.PocketRegistryManager;
import com.zeher.dimpockets.pocket.core.shift.Shifter;
import com.zeher.dimpockets.pocket.core.shift.ShifterUtil;
import com.zeher.zeherlib.api.client.util.TextHelper;
import com.zeher.zeherlib.mod.util.ModUtil;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;

public class CommandSetSpawn extends CommandBase {

	@Override
	public String getName() {
		return "setspawn";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "command.setspawn.usage";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
        return 1;
    }

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 0) {
            throw new WrongUsageException("command.setspawn.usage", new Object[0]);
        }
		
		EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		int dim_id = player.dimension;
		BlockPos pos = player.getPosition();
		WorldServer world_server = server.getWorld(dim_id);
		BlockPos spawnPos = new BlockPos(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15);
		
		if (dim_id == CONSTANT.POCKET_DIMENSION_ID) {
			Pocket pocket = PocketRegistryManager.getPocket(new BlockPos(pos.getX() >> 4, pos.getY() >> 4, pos.getZ() >> 4));
			
			if (pocket != null) {
				if (player.getName().equals(pocket.getCreator())) {
					pocket.setSpawnInPocket(spawnPos, player.rotationYaw, player.rotationPitch);
					ModUtil.sendPlayerMessage(world_server, player, TextHelper.GREEN + TextHelper.BOLD + "Spawn Position set successfully.");
				} else {
					ModUtil.sendPlayerMessage(world_server, player, TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.access_set.name"));
				}
			} else {
				ModUtil.sendPlayerMessage(world_server, player, TextHelper.RED + TextHelper.ITALIC + "You are not inside a Pocket!");
			}
		} else {
			ModUtil.sendPlayerMessage(world_server, player, TextHelper.RED + TextHelper.ITALIC + "You are not in the Pocket Dimension!");
		}
	}
}