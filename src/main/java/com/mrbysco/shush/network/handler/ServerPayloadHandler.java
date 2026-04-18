package com.mrbysco.shush.network.handler;

import com.mrbysco.shush.block.blockentity.ShushBlockEntity;
import com.mrbysco.shush.network.message.SetShushPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {
	public static final ServerPayloadHandler INSTANCE = new ServerPayloadHandler();

	public static ServerPayloadHandler getInstance() {
		return INSTANCE;
	}

	public void handleShushData(final SetShushPayload payload, final IPayloadContext context) {
		// Do something with the data, on the main thread
		context.enqueueWork(() -> {
					Player player = context.player();
					if (player instanceof ServerPlayer serverPlayer) {
						ServerLevel level = serverPlayer.level();
						GlobalPos globalPos = payload.globalPos();
						BlockPos pos = globalPos.pos();
						if (level.dimension().equals(globalPos.dimension()) && level.isLoaded(pos)) {
							BlockEntity blockEntity = level.getBlockEntity(pos);
							if (blockEntity instanceof ShushBlockEntity shushBlockEntity) {
								shushBlockEntity.setShushType(payload.shushData());
								shushBlockEntity.refreshClient();
							}
						} else {
							context.disconnect(Component.translatable("shush.networking.set_shush.failed"));
						}
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("shush.networking.set_shush.failed", e.getMessage()));
					return null;
				});
	}
}
