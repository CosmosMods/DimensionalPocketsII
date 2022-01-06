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
import com.tcn.dimensionalpocketsii.pocket.network.packet.elytraplate.EPacketAllowedPlayer;
import com.tcn.dimensionalpocketsii.pocket.network.packet.elytraplate.EPacketEmptyTank;
import com.tcn.dimensionalpocketsii.pocket.network.packet.elytraplate.EPacketHostileSpawnState;
import com.tcn.dimensionalpocketsii.pocket.network.packet.elytraplate.EPacketLock;
import com.tcn.dimensionalpocketsii.pocket.network.packet.elytraplate.EPacketLockToAllowedPlayers;
import com.tcn.dimensionalpocketsii.pocket.network.packet.elytraplate.EPacketTrapPlayers;
import com.tcn.dimensionalpocketsii.pocket.network.packet.misc.PacketArmourItem;
import com.tcn.dimensionalpocketsii.pocket.network.packet.misc.PacketChargerEnergyState;
import com.tcn.dimensionalpocketsii.pocket.network.packet.misc.PacketFocus;
import com.tcn.dimensionalpocketsii.pocket.network.packet.misc.PacketFocusTeleport;
import com.tcn.dimensionalpocketsii.pocket.network.packet.misc.PacketGeneratorEmptyTank;
import com.tcn.dimensionalpocketsii.pocket.network.packet.misc.PacketGeneratorMode;
import com.tcn.dimensionalpocketsii.pocket.network.packet.misc.PacketUpgradeStationCraft;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class PocketNetworkManager {

	private static final String PROTOCOL_VERSION = "1";
	
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
		new ResourceLocation(DimensionalPockets.MOD_ID, "pocket_net"), 
		() -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals
	);
	
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
		
		INSTANCE.registerMessage(11, PacketArmourItem.class, PacketArmourItem::encode, PacketArmourItem::new, PacketArmourItem::handle);
		INSTANCE.registerMessage(12, PacketChargerEnergyState.class, PacketChargerEnergyState::encode, PacketChargerEnergyState::new, PacketChargerEnergyState::handle);
		INSTANCE.registerMessage(13, PacketGeneratorMode.class, PacketGeneratorMode::encode, PacketGeneratorMode::new, PacketGeneratorMode::handle);
		INSTANCE.registerMessage(14, PacketUpgradeStationCraft.class, PacketUpgradeStationCraft::encode, PacketUpgradeStationCraft::new, PacketUpgradeStationCraft::handle);
		INSTANCE.registerMessage(15, PacketGeneratorEmptyTank.class, PacketGeneratorEmptyTank::encode, PacketGeneratorEmptyTank::new, PacketGeneratorEmptyTank::handle);

		INSTANCE.registerMessage(16, PacketFocus.class, PacketFocus::encode, PacketFocus::new, PacketFocus::handle);
		INSTANCE.registerMessage(17, PacketFocusTeleport.class, PacketFocusTeleport::encode, PacketFocusTeleport::new, PacketFocusTeleport::handle);
		
		INSTANCE.registerMessage(18, EPacketLock.class, EPacketLock::encode, EPacketLock::new, EPacketLock::handle);
		INSTANCE.registerMessage(19, EPacketAllowedPlayer.class, EPacketAllowedPlayer::encode, EPacketAllowedPlayer::new, EPacketAllowedPlayer::handle);
		INSTANCE.registerMessage(20, EPacketEmptyTank.class, EPacketEmptyTank::encode, EPacketEmptyTank::new, EPacketEmptyTank::handle);
		INSTANCE.registerMessage(21, EPacketTrapPlayers.class, EPacketTrapPlayers::encode, EPacketTrapPlayers::new, EPacketTrapPlayers::handle);
		INSTANCE.registerMessage(22, EPacketHostileSpawnState.class, EPacketHostileSpawnState::encode, EPacketHostileSpawnState::new, EPacketHostileSpawnState::handle);
		INSTANCE.registerMessage(23, EPacketLockToAllowedPlayers.class, EPacketLockToAllowedPlayers::encode, EPacketLockToAllowedPlayers::new, EPacketLockToAllowedPlayers::handle);
		
		
		DimensionalPockets.CONSOLE.startup("Pocket Network Setup complete.");
	}

	public static void sendToServer(Object message) {
        INSTANCE.sendToServer(message);
    }
}
