package com.zeher.dimpockets.pocket.core.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zeher.dimpockets.DimensionalPockets;
import com.zeher.dimpockets.core.log.ModLogger;
import com.zeher.dimpockets.core.manager.ModConfigManager;
import com.zeher.dimpockets.pocket.core.Pocket;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;


public class ChunkLoaderManagerRoom implements LoadingCallback {

	private static class TicketWrapper {
		final List<BlockPos> loadedRooms = new ArrayList<>();
		Ticket ticket;
	}

	private static final Map<BlockPos, TicketWrapper> ticketMap = new HashMap<>();

	@SuppressWarnings("unused")
	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) {
		//DimUtils.enforceServer();
		for (Ticket ticket : tickets) {
			if (ticket == null) {
				continue;
			}

			BlockPos chunkXZSet = new BlockPos(0,0,0); //CoordSet.readFromNBT(ticket.getModData());

			if (chunkXZSet == null) {
				continue;
			}

			TicketWrapper wrapper = ticketMap.get(chunkXZSet);

			if (wrapper == null) {
				ModLogger.info("Ticket from forge contained a chunkXZSet for which no TicketWrapper exists. Ignoring ticket.", ChunkLoaderManagerRoom.class);
				continue;
			}

			if (!wrapper.loadedRooms.isEmpty()) {
				ChunkLoaderManagerRoom.revalidateTicket(chunkXZSet, ticket);
			} else {
				ModLogger.warning("A ticket was active for a chunk without loaded pocket rooms.", ChunkLoaderManagerRoom.class);
				ForgeChunkManager.releaseTicket(ticket);
				wrapper.ticket = null;
			}
		}
	}

	private static void revalidateTicket(BlockPos chunkXZSet, Ticket currentTicket) {
		TicketWrapper wrapper = ticketMap.get(chunkXZSet);

		if (wrapper == null) {
			ModLogger.severe("Can't revalidate ticket! No TicketWrapper in ticketMap for chunkSet: " + chunkXZSet.toString());
			return;
		}

		if (wrapper.ticket != null) {
			ForgeChunkManager.releaseTicket(wrapper.ticket);
		}

		wrapper.ticket = currentTicket;

		ForgeChunkManager.forceChunk(wrapper.ticket, new ChunkPos(chunkXZSet));
	}

	public static void addPocketToChunkLoader(Pocket pocket) {
		if (!ModConfigManager.KEEP_POCKETS_CHUNKLOADED)
			return;

		//DimUtils.enforceServer();
		if (pocket == null)
			return;

		BlockPos pocketSet = pocket.getChunkPos();
		BlockPos chunkXZSet = pocketSet;
		chunkXZSet = new BlockPos(chunkXZSet.getX(), 0, chunkXZSet.getZ());
		
		TicketWrapper wrapper = ticketMap.get(chunkXZSet);
		if (wrapper == null) {
			wrapper = new TicketWrapper();
			ticketMap.put(chunkXZSet, wrapper);
		}

		if (!wrapper.loadedRooms.contains(pocketSet)) {
			wrapper.loadedRooms.add(pocketSet);
			ModLogger.info("Marked the following pocket room to the be loaded: " + pocketSet.toString(), ChunkLoaderManagerRoom.class);
		} else {
			ModLogger.info("The following Pocket was already marked as loaded: " + pocketSet.toString(), ChunkLoaderManagerRoom.class);
		}

		refreshWrapperTicket(chunkXZSet);
	}
	
	private static void refreshWrapperTicket(BlockPos chunkXZSet) {
		TicketWrapper wrapper = ticketMap.get(chunkXZSet);

		if (wrapper.ticket == null) {
			World world = PocketRegistryManager.getWorldForPockets();
			wrapper.ticket = ForgeChunkManager.requestTicket(DimensionalPockets.INSTANCE, world, ForgeChunkManager.Type.NORMAL);

			if (wrapper.ticket != null) {
				NBTTagCompound tag = wrapper.ticket.getModData();
				//chunkXZSet.writeToNBT(tag);
				ForgeChunkManager.forceChunk(wrapper.ticket, new ChunkPos(chunkXZSet));
			} else {
				ModLogger.warning("No new tickets available from the ForgeChunkManager.", ChunkLoaderManagerRoom.class);
			}
		}
	}

	public static void removePocketFromChunkLoader(Pocket pocket) {
		//DimUtils.enforceServer();
		BlockPos pocketSet = pocket.getChunkPos();
		BlockPos chunkXZSet = pocketSet;
		chunkXZSet = new BlockPos(chunkXZSet.getX(), 0, chunkXZSet.getZ()); // set to 0 to get the same CoordSet for every pocket in the same chunk

		if (!ticketMap.containsKey(chunkXZSet) && ModConfigManager.KEEP_POCKETS_CHUNKLOADED) {
			ModLogger.warning("Something tried to remove a loaded pocket from a chunk that was never loaded before...");
			return;
		}

		TicketWrapper wrapper = ticketMap.get(chunkXZSet);

		if (wrapper.loadedRooms.remove(pocketSet)) {
			ModLogger.info("Removed the following pocket room from the list of loaded rooms: " + pocketSet.toString(), ChunkLoaderManagerRoom.class);
		} else {
			ModLogger.warning("The following pocket room wanted to be removed, but was not marked as loaded: " + pocketSet.toString(), ChunkLoaderManagerRoom.class);
		}

		if (wrapper.loadedRooms.isEmpty()) {
			ForgeChunkManager.releaseTicket(wrapper.ticket);
			wrapper.ticket = null;
		}
	}

	public static void clearTicketMap() {
		//DimUtils.enforceServer();
		ticketMap.clear();
	}
}
