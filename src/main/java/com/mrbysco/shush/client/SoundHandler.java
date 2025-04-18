package com.mrbysco.shush.client;

import com.mrbysco.shush.ShushMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import org.jetbrains.annotations.Nullable;

public class SoundHandler {
	public static void onSoundEvent(PlaySoundEvent event) {
		SoundInstance sound = event.getSound();
		SoundInstance adjustedSound = getAdjustedVolume(sound);
		if (adjustedSound != null) {
			event.setSound(adjustedSound);
		}
	}

	public static SoundInstance getAdjustedVolume(@Nullable SoundInstance sound) {
		if (sound == null)
			return null;

		if (sound instanceof AbstractSoundInstance abstractSoundInstance) {
			final Minecraft mc = Minecraft.getInstance();
			final Player player = mc.player;
			final Level level = mc.level;
			if (player != null && level != null && abstractSoundInstance.volume > 0) {
				int shushCount = ShushCache.shushCount(level.dimension(), player.blockPosition(), 32);
				if (shushCount > 0) {
					float oldVolume = abstractSoundInstance.volume;
					// Get shush percentage modifier, 10% per shush block
					float shushPercentage = 1.0F - (0.1F * shushCount);
					// Set the volume to the shush percentage
					abstractSoundInstance.volume = oldVolume * shushPercentage;
//					ShushMod.LOGGER.info("{} {}", oldVolume, abstractSoundInstance.volume);
					return abstractSoundInstance;
				}
			}
		}
		return null;
	}
}
