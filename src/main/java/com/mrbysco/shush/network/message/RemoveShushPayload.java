package com.mrbysco.shush.network.message;

import com.mrbysco.shush.ShushMod;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record RemoveShushPayload(GlobalPos globalPos) implements CustomPacketPayload {
	public static final StreamCodec<RegistryFriendlyByteBuf, RemoveShushPayload> CODEC = StreamCodec.composite(
			GlobalPos.STREAM_CODEC, RemoveShushPayload::globalPos,
			RemoveShushPayload::new
	);
	public static final Type<RemoveShushPayload> ID = new Type<>(ShushMod.modLoc("remove_shush"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
