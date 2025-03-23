package com.mrbysco.shush.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mrbysco.shush.client.SoundHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
	@ModifyArg(
			method = "playLocalSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/sounds/SoundManager;play(Lnet/minecraft/client/resources/sounds/SoundInstance;)V"
			),
			index = 0
	)
	private SoundInstance shush$modifyVolume(SoundInstance sound) {
		SoundInstance adjustedSound = SoundHandler.getAdjustedVolume(sound);
		if (adjustedSound != null) {
			return adjustedSound;
		}
		return sound;
	}

	@ModifyArg(method = "playSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZJ)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/sounds/SoundManager;playDelayed(Lnet/minecraft/client/resources/sounds/SoundInstance;I)V"
			),
			index = 0
	)
	private SoundInstance shush$playSound(SoundInstance sound, @Local SimpleSoundInstance soundInstance) {
		SoundInstance adjustedSound = SoundHandler.getAdjustedVolume(sound);
		if (adjustedSound != null) {
			return adjustedSound;
		}
		return sound;
	}
}
