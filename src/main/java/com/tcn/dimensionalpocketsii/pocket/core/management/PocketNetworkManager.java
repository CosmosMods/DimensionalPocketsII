package com.tcn.dimensionalpocketsii.pocket.core.management;

import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.pocket.network.packet.block.PacketSideGuide;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketAllowedPlayer;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketBlockSideState;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketEmptyTank;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketHostileSpawnState;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketLock;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketLockToAllowedPlayers;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketRegistryData;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketTrapPlayers;
import com.tcn.dimensionalpocketsii.pocket.network.packet.connector.PacketConnectionType;
import com.tcn.dimensionalpocketsii.pocket.network.packet.connector.PacketSideState;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PocketNetworkManager {

	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(DimensionalPockets.MOD_ID, "pocket_net"), 
			() -> PROTOCOL_VERSION, 
			PROTOCOL_VERSION::equals, 
			PROTOCOL_VERSION::equals);
	
	public static void register() {
		INSTANCE.registerMessage(0, PacketRegistryData.class, PacketRegistryData::encode, PacketRegistryData::new, PacketRegistryData::handle);

		INSTANCE.registerMessage(1, PacketLock.class, PacketLock::encode, PacketLock::new, PacketLock::handle);
		INSTANCE.registerMessage(2, PacketAllowedPlayer.class, PacketAllowedPlayer::encode, PacketAllowedPlayer::new, PacketAllowedPlayer::handle);
		INSTANCE.registerMessage(3, PacketBlockSideState.class, PacketBlockSideState::encode, PacketBlockSideState::new, PacketBlockSideState::handle);
		INSTANCE.registerMessage(4, PacketEmptyTank.class, PacketEmptyTank::encode, PacketEmptyTank::new, PacketEmptyTank::handle);
		INSTANCE.registerMessage(5, PacketTrapPlayers.class, PacketTrapPlayers::encode, PacketTrapPlayers::new, PacketTrapPlayers::handle);
		INSTANCE.registerMessage(6, PacketHostileSpawnState.class, PacketHostileSpawnState::encode, PacketHostileSpawnState::new, PacketHostileSpawnState::handle);
		INSTANCE.registerMessage(7, PacketLockToAllowedPlayers.class, PacketLockToAllowedPlayers::encode, PacketLockToAllowedPlayers::new, PacketLockToAllowedPlayers::handle);
		
		INSTANCE.registerMessage(8, PacketConnectionType.class, PacketConnectionType::encode, PacketConnectionType::new, PacketConnectionType::handle);
		INSTANCE.registerMessage(9, PacketSideState.class, PacketSideState::encode, PacketSideState::new, PacketSideState::handle);
		
		INSTANCE.registerMessage(10, PacketSideGuide.class, PacketSideGuide::encode, PacketSideGuide::new, PacketSideGuide::handle);
		
		DimensionalPockets.LOGGER.info("Packets Registered");
	}

	public static void sendToServer(Object message) {
        INSTANCE.sendToServer(message);
    }
}
