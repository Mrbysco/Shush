package com.mrbysco.shush.client;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(Dist.CLIENT)
public class ClientHandler {
	/**
	 * Prune the shush map every few seconds
	 *
	 * @param event The level tick event
	 */
	@SubscribeEvent
	public static void onLevelTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		Level level = player.level();
		if (!level.isClientSide()) return;

		if (level.getGameTime() % level.tickRateManager().tickrate() == 0) {
			ShushCache.pruneMap(level, player.blockPosition(), 64);
		}
	}

	@SubscribeEvent
	public static void onLevelUnload(LevelEvent.Unload event) {
		ShushCache.clearCache();
	}
}
