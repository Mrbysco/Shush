package com.mrbysco.shush.network.message;

import com.mrbysco.shush.ShushMod;
import com.mrbysco.shush.util.ShushData;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SetShushPayload(GlobalPos globalPos,
                              ShushData shushData) implements CustomPacketPayload {
	public static final StreamCodec<RegistryFriendlyByteBuf, SetShushPayload> CODEC = StreamCodec.composite(
			GlobalPos.STREAM_CODEC, SetShushPayload::globalPos,
			ShushData.STREAM_CODEC, SetShushPayload::shushData,
			SetShushPayload::new
	);
	public static final Type<SetShushPayload> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(ShushMod.MOD_ID, "set_shush_data"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
