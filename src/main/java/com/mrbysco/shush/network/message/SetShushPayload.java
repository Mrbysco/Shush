package com.mrbysco.shush.network.message;

import com.mrbysco.shush.ShushMod;
import com.mrbysco.shush.util.ShushData;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.List;
import java.util.Optional;

public record SetShushPayload(GlobalPos globalPos,
                              ShushData shushData) implements CustomPacketPayload {
	public static final StreamCodec<RegistryFriendlyByteBuf, SetShushPayload> CODEC = StreamCodec.composite(
			GlobalPos.STREAM_CODEC, SetShushPayload::globalPos,
			ShushData.STREAM_CODEC, SetShushPayload::shushData,
			SetShushPayload::new
	);
	public static final Type<SetShushPayload> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(ShushMod.MOD_ID, "set_shush_data"));

	private static List<SoundEvent> getSounds(List<ResourceLocation> soundIds) {
		return soundIds.stream()
				.map(BuiltInRegistries.SOUND_EVENT::getOptional)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.toList();
	}

	public SetShushPayload(GlobalPos pos, List<ResourceLocation> soundIds) {
		this(pos, new ShushData(0.1F, getSounds(soundIds)));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
