package com.mrbysco.shush;

import com.mojang.logging.LogUtils;
import com.mrbysco.shush.client.ClientHandler;
import com.mrbysco.shush.client.SoundHandler;
import com.mrbysco.shush.network.PacketHandler;
import com.mrbysco.shush.registry.ShushRegistry;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(ShushMod.MOD_ID)
public class ShushMod {
	public static final String MOD_ID = "shush";
	public static final Logger LOGGER = LogUtils.getLogger();

	public ShushMod(IEventBus eventBus, Dist dist, ModContainer container) {
		ShushRegistry.BLOCKS.register(eventBus);
		ShushRegistry.BLOCK_ENTITY_TYPES.register(eventBus);
		ShushRegistry.ITEMS.register(eventBus);
		ShushRegistry.CREATIVE_MODE_TABS.register(eventBus);

		eventBus.addListener(PacketHandler::setupPackets);

		if (dist.isClient()) {
			NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, SoundHandler::onSoundEvent);
			NeoForge.EVENT_BUS.addListener(ClientHandler::onLevelTick);
			NeoForge.EVENT_BUS.addListener(ClientHandler::onLevelUnload);
		}
	}

	public static Identifier modLoc(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
