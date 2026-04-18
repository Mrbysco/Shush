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
				float shushedBy = ShushCache.getShushedBy(level.dimension(), player.blockPosition(), abstractSoundInstance, 32);
				if (shushedBy > 0) {
					float oldVolume = abstractSoundInstance.volume;
					// Get shush percentage modifier, 10% per shush block
					float shushPercentage = Math.max(0.0F, 1.0F - shushedBy);
					// Set the volume to the shush percentage
					abstractSoundInstance.volume = oldVolume * shushPercentage;
					return abstractSoundInstance;
				}
			}
		}
		return null;
	}
}
