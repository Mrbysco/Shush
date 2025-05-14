package com.mrbysco.shush.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record ShushData(float shushAmount, List<SoundEvent> filteredSounds) {
	public static final MapCodec<ShushData> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
					Codec.FLOAT.fieldOf("shushAmount").forGetter(ShushData::shushAmount),
					SoundEvent.DIRECT_CODEC.listOf().fieldOf("filteredSounds").forGetter(ShushData::filteredSounds)
			).apply(instance, ShushData::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, ShushData> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.FLOAT, ShushData::shushAmount,
			SoundEvent.DIRECT_STREAM_CODEC.apply(ByteBufCodecs.list()), ShushData::filteredSounds,
			ShushData::new
	);

	public static final ShushData EMPTY = new ShushData(0.1F, List.of());

	@Nullable
	public static ShushData dataFromTag(CompoundTag tag) {
		if (tag.contains("shushType")) {
			CompoundTag shushTag = tag.getCompound("shushType");
			float shushAmount = shushTag.getFloat("shushAmount");

			List<SoundEvent> sounds = new ArrayList<>();
			ListTag soundList = shushTag.getList("filteredSounds", Tag.TAG_COMPOUND);
			if (!soundList.isEmpty()) {
				soundList.forEach(soundTag -> {
					if (soundTag instanceof CompoundTag soundCompound) {
						ResourceLocation location = ResourceLocation.tryParse(soundCompound.getString("sound"));
						if (location != null) {
							BuiltInRegistries.SOUND_EVENT.getOptional(location).ifPresent(sounds::add);
						}
					}
				});
			}
			return new ShushData(shushAmount, sounds);
		}
		return null;
	}

	public void save(CompoundTag tag) {
		CompoundTag shushTag = new CompoundTag();
		if (this.shushAmount > 0)
			shushTag.putFloat("shushAmount", this.shushAmount);
		if (this.filteredSounds != null) {
			ListTag filteredSoundsTag = new ListTag();
			for (SoundEvent sound : this.filteredSounds) {
				CompoundTag soundTag = new CompoundTag();
				soundTag.putString("sound", sound.getLocation().toString());
				filteredSoundsTag.add(soundTag);
			}
			shushTag.put("filteredSounds", filteredSoundsTag);
		}
		tag.put("shushType", shushTag);
	}
}
