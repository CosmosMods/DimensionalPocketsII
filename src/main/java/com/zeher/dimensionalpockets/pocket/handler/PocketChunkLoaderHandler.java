package com.zeher.dimensionalpockets.pocket.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.util.DimLogger;
import com.zeher.dimensionalpockets.core.util.DimUtils;
import com.zeher.dimensionalpockets.pocket.Pocket;
import com.zeher.dimensionalpockets.pocket.PocketRegistry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;


public class PocketChunkLoaderHandler implements LoadingCallback {

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
				DimLogger.info("Ticket from forge contained a chunkXZSet for which no TicketWrapper exists. Ignoring ticket.", PocketChunkLoaderHandler.class);
				continue;
			}

			if (!wrapper.loadedRooms.isEmpty()) {
				PocketChunkLoaderHandler.revalidateTicket(chunkXZSet, ticket);
			} else {
				DimLogger.warning("A ticket was active for a chunk without loaded pocket rooms.", PocketChunkLoaderHandler.class);
				ForgeChunkManager.releaseTicket(ticket);
				wrapper.ticket = null;
			}
		}
	}

	private static void revalidateTicket(BlockPos chunkXZSet, Ticket currentTicket) {
		TicketWrapper wrapper = ticketMap.get(chunkXZSet);

		if (wrapper == null) {
			DimLogger.severe("Can't revalidate ticket! No TicketWrapper in ticketMap for chunkSet: " + chunkXZSet.toString());
			return;
		}

		if (wrapper.ticket != null) {
			ForgeChunkManager.releaseTicket(wrapper.ticket);
		}

		wrapper.ticket = currentTicket;

		ForgeChunkManager.forceChunk(wrapper.ticket, new ChunkPos(chunkXZSet));
	}

	public static void addPocketToChunkLoader(Pocket pocket) {
		if (!DimensionalPockets.KEEP_POCKETS_CHUNKLOADED)
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
			DimLogger.info("Marked the following pocket room to the be loaded: " + pocketSet.toString(), PocketChunkLoaderHandler.class);
		} else {
			DimLogger.info("The following Pocket was already marked as loaded: " + pocketSet.toString(), PocketChunkLoaderHandler.class);
		}

		refreshWrapperTicket(chunkXZSet);
	}

	private static void refreshWrapperTicket(BlockPos chunkXZSet) {
		TicketWrapper wrapper = ticketMap.get(chunkXZSet);

		if (wrapper.ticket == null) {
			World world = PocketRegistry.getWorldForPockets();
			wrapper.ticket = ForgeChunkManager.requestTicket(DimensionalPockets.INSTANCE, world, ForgeChunkManager.Type.NORMAL);

			if (wrapper.ticket != null) {
				NBTTagCompound tag = wrapper.ticket.getModData();
				//chunkXZSet.writeToNBT(tag);
				ForgeChunkManager.forceChunk(wrapper.ticket, new ChunkPos(chunkXZSet));
			} else {
				DimLogger.warning("No new tickets available from the ForgeChunkManager.", PocketChunkLoaderHandler.class);
			}
		}
	}

	public static void removePocketFromChunkLoader(Pocket pocket) {
		//DimUtils.enforceServer();
		BlockPos pocketSet = pocket.getChunkPos();
		BlockPos chunkXZSet = pocketSet;
		chunkXZSet = new BlockPos(chunkXZSet.getX(), 0, chunkXZSet.getZ()); // set to 0 to get the same CoordSet for every pocket in the same chunk

		if (!ticketMap.containsKey(chunkXZSet) && DimensionalPockets.KEEP_POCKETS_CHUNKLOADED) {
			DimLogger.warning("Something tried to remove a loaded pocket from a chunk that was never loaded before...");
			return;
		}

		TicketWrapper wrapper = ticketMap.get(chunkXZSet);

		if (wrapper.loadedRooms.remove(pocketSet)) {
			DimLogger.info("Removed the following pocket room from the list of loaded rooms: " + pocketSet.toString(), PocketChunkLoaderHandler.class);
		} else {
			DimLogger.warning("The following pocket room wanted to be removed, but was not marked as loaded: " + pocketSet.toString(), PocketChunkLoaderHandler.class);
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
