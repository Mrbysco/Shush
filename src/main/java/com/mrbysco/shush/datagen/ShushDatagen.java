package com.mrbysco.shush.datagen;

import com.mrbysco.shush.datagen.assets.ShushLanguageProvider;
import com.mrbysco.shush.datagen.assets.ShushModelProvider;
import com.mrbysco.shush.datagen.data.ShushRecipeProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class ShushDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<Provider> lookupProvider = event.getLookupProvider();

		generator.addProvider(true, new ShushRecipeProvider.Runner(packOutput, lookupProvider));

		generator.addProvider(true, new ShushLanguageProvider(packOutput));
		generator.addProvider(true, new ShushModelProvider(packOutput));
//		generator.addProvider(true, new ShushBlockStateProvider(packOutput));
//		generator.addProvider(true, new ShushItemModelProvider(packOutput));

	}
}
