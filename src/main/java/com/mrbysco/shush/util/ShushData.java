package com.mrbysco.shush.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;

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
}
