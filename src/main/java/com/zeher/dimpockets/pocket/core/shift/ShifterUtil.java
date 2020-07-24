package com.zeher.dimpockets.pocket.core.shift;

import java.util.Collection;

import com.zeher.dimpockets.DimensionalPockets;
import com.zeher.dimpockets.core.log.ModLogger;
import com.zeher.dimpockets.pocket.network.packet.PacketConnector;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;

public class ShifterUtil {
	
	public static Shifter createTeleporter(int dim_id, BlockPos coordSet, float spawnYaw, float spawnPitch) {
		return new Shifter(FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getWorld(dim_id), coordSet, spawnYaw, spawnPitch);
	}
	
	public static void shiftPlayerToDimension(EntityPlayerMP player, int dimensionIn, Shifter teleporter) {
        int i = player.dimension;
        WorldServer worldserver = player.mcServer.getWorld(player.dimension);
        player.dimension = dimensionIn;
        WorldServer worldserver1 = player.mcServer.getWorld(player.dimension);
        
        PlayerList list = worldserver.getMinecraftServer().getPlayerList();
        
        player.connection.sendPacket(new SPacketRespawn(player.dimension, worldserver1.getDifficulty(), worldserver1.getWorldInfo().getTerrainType(), player.interactionManager.getGameType())); // Forge: Use new dimensions information
        
        list.updatePermissionLevel(player);
        worldserver.removeEntityDangerously(player);
        
        player.isDead = false;
        transferEntityToWorld(player, i, worldserver, worldserver1, teleporter);
        list.preparePlayer(player, worldserver);
        
        player.connection.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        player.interactionManager.setWorld(worldserver1);
        player.connection.sendPacket(new SPacketPlayerAbilities(player.capabilities));
        list.updateTimeAndWeatherForPlayer(player, worldserver1);
        list.syncPlayerInventory(player);

        for (PotionEffect potioneffect : player.getActivePotionEffects()) {
            player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), potioneffect));
        }
        
        // Fix MC-88179: on non-death SPacketRespawn, also resend attributes
        AttributeMap attributemap = (AttributeMap) player.getAttributeMap();
        Collection<IAttributeInstance> watchedAttribs = attributemap.getWatchedAttributes();
        if (!watchedAttribs.isEmpty()) player.connection.sendPacket(new SPacketEntityProperties(player.getEntityId(), watchedAttribs));
        FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, i, dimensionIn);
    }

	public static void transferEntityToWorld(Entity entityIn, int lastDimension, WorldServer oldWorldIn, WorldServer toWorldIn, Shifter teleporter) {
		double moveFactor = oldWorldIn.provider.getMovementFactor() / toWorldIn.provider.getMovementFactor();
		double d0 = MathHelper.clamp(entityIn.posX * moveFactor, toWorldIn.getWorldBorder().minX() + 16.0D, toWorldIn.getWorldBorder().maxX() - 16.0D);
		double d1 = MathHelper.clamp(entityIn.posZ * moveFactor, toWorldIn.getWorldBorder().minZ() + 16.0D, toWorldIn.getWorldBorder().maxZ() - 16.0D);
		double d2 = 8.0D;
		float f = entityIn.rotationYaw;
		oldWorldIn.profiler.startSection("moving");

		if (entityIn.dimension == 1 && teleporter.isVanilla()) {
			BlockPos blockpos;

			
			if (lastDimension == 1) {
				blockpos = toWorldIn.getSpawnPoint();
			} else {
				blockpos = toWorldIn.getSpawnCoordinate();
			}

			d0 = (double) blockpos.getX();
			entityIn.posY = (double) blockpos.getY();
			d1 = (double) blockpos.getZ();
			entityIn.setLocationAndAngles(d0, entityIn.posY, d1, 90.0F, 0.0F);

			if (entityIn.isEntityAlive()) {
				oldWorldIn.updateEntityWithOptionalForce(entityIn, false);
			}
		}
		oldWorldIn.profiler.endSection();

		if (lastDimension != 1 || !teleporter.isVanilla()) {
			oldWorldIn.profiler.startSection("placing");
			d0 = (double) MathHelper.clamp((int) d0, -29999872, 29999872);
			d1 = (double) MathHelper.clamp((int) d1, -29999872, 29999872);

			if (entityIn.isEntityAlive()) {
				entityIn.setLocationAndAngles(d0, entityIn.posY, d1, entityIn.rotationYaw, entityIn.rotationPitch);
				oldWorldIn.updateEntityWithOptionalForce(entityIn, false);
				teleporter.placeEntity(toWorldIn, entityIn, f);
				toWorldIn.spawnEntity(entityIn);
				toWorldIn.updateEntityWithOptionalForce(entityIn, false);
			}
			oldWorldIn.profiler.endSection();
		}
		entityIn.setWorld(toWorldIn);
		
		//Problem with Digimobs -- Sorted with this code until Digimobs gets fixed.
		if (Loader.isModLoaded("digimobs")) {
			ModLogger.warning("[EXTERNAL MOD <digimobs>] Digimobs is installed! Correcting mod issue...", ShifterUtil.class);
			entityIn.setPosition(teleporter.getTargetPos().getX() + 0.5F, teleporter.getTargetPos().getY() + 1.0F, teleporter.getTargetPos().getZ() + 0.5F);
		}
	}
}