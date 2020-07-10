package com.zeher.dimensionalpockets.core.command;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.zeher.dimensionalpockets.core.handler.BlockHandler;
import com.zeher.dimensionalpockets.core.util.DimUtils;
import com.zeher.dimensionalpockets.pocket.Pocket;
import com.zeher.dimensionalpockets.pocket.PocketRegistry;
import com.zeher.zeherlib.api.client.util.TextHelper;
import com.zeher.zeherlib.api.compat.core.interfaces.ISidedTile;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandRecoverPocket extends CommandBase {

	@Override
	public String getName() {
		return "recoverpocket";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "command.recoverpocket.usage";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
        return 4;
    }

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
            throw new WrongUsageException("commands.recoverpocket.usage", new Object[0]);
        }
		
		EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		
		Map<BlockPos, Pocket> map = PocketRegistry.getMap();
		Object[] keys = map.keySet().toArray();
		int length = keys.length - 1;
		
		if(args[0] != null || args[0].length() > 0) {
			if(StringUtils.isNumeric(args[0])) {		
				int map_number = Integer.parseInt(args[0]);
				
				if (map_number <= length) {
					Pocket pocket = map.get(keys[map_number]);
					
					if (player.getName().equals(pocket.getCreator())) {
						if (player.isCreative()) {
							player.inventory.addItemStackToInventory(this.generateItemStackOnRemoval(pocket));
						} else {
							throw new WrongUsageException("commands.recoverpocket.creative", new Object[0]);
						}
					} else {
						throw new WrongUsageException("commands.recoverpocket.wrong_name", new Object[0]);
					}
				} else {
					throw new WrongUsageException("commands.recoverpocket.too.usage", new Object[0]);
				}
			} else if (args[0].equals("length")){
				TextComponentString comp = new TextComponentString(TextHelper.LIGHT_GRAY + "The current amount of registered pockets is: " + TextHelper.PURPLE + TextHelper.BOLD + length);
				player.sendMessage(comp);
			} else {
				throw new WrongUsageException("commands.recoverpocket.usage", new Object[0]);
			}
		} else {
			throw new WrongUsageException("commands.recoverpocket.usage", new Object[0]);
		}
	}
	
	/**
	 * Custom variant of generateItemStackOnRemoval from {@link Pocket}
	 */
	public ItemStack generateItemStackOnRemoval(Pocket pocket) {
		ItemStack itemStack = new ItemStack(BlockHandler.BLOCK_DIMENSIONAL_POCKET);

		if (!itemStack.hasTagCompound()) {
			itemStack.setTagCompound(new NBTTagCompound());
		}

		BlockPos chunkSet = pocket.getChunkPos();
		
		NBTTagCompound compound = new NBTTagCompound();
		
		//Saves the chunk data to NBT
		NBTTagCompound chunk_tag = new NBTTagCompound();
		chunk_tag.setInteger("X", chunkSet.getX());
		chunk_tag.setInteger("Y", chunkSet.getY());
		chunk_tag.setInteger("Z", chunkSet.getZ());

		compound.setTag("chunk_set", chunk_tag);

		String creatorLore = null;
		
		if (pocket != null && pocket.getCreator() != null) {
			creatorLore = TextHelper.LIGHT_BLUE + TextHelper.BOLD + I18n.format("pocket.desc.creator.name") + TextHelper.PURPLE + TextHelper.BOLD + " [" + pocket.getCreator() + "]";
		}
		
		itemStack.getTagCompound().setTag("nbt_data", compound);
		
		BlockPos blockSet = new BlockPos(chunkSet.getX() << 4, chunkSet.getY() << 4, chunkSet.getZ() << 4);

		itemStack = DimUtils.generateItem(itemStack, null, false, TextHelper.LIGHT_GRAY + TextHelper.BOLD
				+ "Pocket: [" + blockSet.getX() + " | " + blockSet.getY() + " | " + blockSet.getZ() + "]", creatorLore);
		return itemStack;
	}
}