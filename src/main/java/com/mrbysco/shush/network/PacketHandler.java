package com.mrbysco.shush.network;

import com.mrbysco.shush.ShushMod;
import com.mrbysco.shush.network.handler.ClientPayloadHandler;
import com.mrbysco.shush.network.handler.ServerPayloadHandler;
import com.mrbysco.shush.network.message.RemoveShushPayload;
import com.mrbysco.shush.network.message.SetShushPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketHandler {

	public static void setupPackets(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(ShushMod.MOD_ID);

		registrar.playToServer(SetShushPayload.ID, SetShushPayload.CODEC, ServerPayloadHandler.getInstance()::handleShushData);
		registrar.playToClient(RemoveShushPayload.ID, RemoveShushPayload.CODEC, ClientPayloadHandler.getInstance()::handleRemoval);
	}
}
