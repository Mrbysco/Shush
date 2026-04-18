package com.mrbysco.shush.network.handler;

import com.mrbysco.shush.client.ShushCache;
import com.mrbysco.shush.network.message.RemoveShushPayload;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
	private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

	public static ClientPayloadHandler getInstance() {
		return INSTANCE;
	}

	public void handleRemoval(final RemoveShushPayload data, final IPayloadContext context) {
		context.enqueueWork(() -> {
					ShushCache.removeShushBlock(data.globalPos());
				})
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("shush.networking.remove_shush.failed", e.getMessage()));
					return null;
				});
	}
}
