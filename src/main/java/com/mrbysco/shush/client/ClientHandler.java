package com.mrbysco.shush.client;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class ClientHandler {
	/**
	 * Prune the shush map every few seconds
	 *
	 * @param event The level tick event
	 */
	public static void onLevelTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		Level level = player.level();
		if (!level.isClientSide()) return;

		if (level.getGameTime() % level.tickRateManager().tickrate() == 0) {
			ShushCache.pruneMap(level, player.blockPosition(), 64);
		}
	}

	public static void onLevelUnload(LevelEvent.Unload event) {
		ShushCache.clearCache();
	}
}
