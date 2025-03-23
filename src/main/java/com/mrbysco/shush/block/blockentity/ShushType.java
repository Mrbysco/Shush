package com.mrbysco.shush.block.blockentity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ShushType(@Nullable SoundSource soundSource, @Nullable SoundEvent soundType) {
	public static final ShushType EMPTY = new ShushType(null, null);

	@Nullable
	public static ShushType typeFromTag(CompoundTag tag) {
		if (tag.contains("shushType")) {
			CompoundTag shushTag = tag.getCompound("shushType");
			SoundSource soundSource = null;
			if (shushTag.contains("soundSource")) {
				SoundSource source = sourceFromName(tag.getString("soundSource"));
				if (source != null)
					soundSource = source;
			}
			SoundEvent sound = null;
			if (shushTag.contains("sound")) {
				ResourceLocation location = ResourceLocation.tryParse(tag.getString("sound"));
				if (location != null)
					sound = BuiltInRegistries.SOUND_EVENT.get(location);
			}
			return new ShushType(soundSource, sound);
		}
		return null;
	}

	public void save(CompoundTag tag) {
		CompoundTag shushTag = new CompoundTag();
		if (this.soundSource != null)
			shushTag.putString("soundSource", this.soundSource.getName());
		if (this.soundType != null)
			shushTag.putString("sound", this.soundType.getLocation().toString());
		tag.put("shushType", shushTag);
	}

	/**
	 * Get the SoundSource from the name
	 *
	 * @param name The name of the SoundSource
	 * @return The SoundSource or null if not found
	 */
	@Nullable
	private static SoundSource sourceFromName(@NotNull String name) {
		for (SoundSource source : SoundSource.values()) {
			if (source.getName().equals(name))
				return source;
		}
		return null;
	}
}
