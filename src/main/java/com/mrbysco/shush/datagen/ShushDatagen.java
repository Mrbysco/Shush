package com.mrbysco.shush.datagen;

import com.mrbysco.shush.datagen.assets.ShushBlockStateProvider;
import com.mrbysco.shush.datagen.assets.ShushItemModelProvider;
import com.mrbysco.shush.datagen.assets.ShushLanguageProvider;
import com.mrbysco.shush.datagen.data.ShushRecipeProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ShushDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(true, new ShushRecipeProvider(packOutput, lookupProvider));
		}
		if (event.includeClient()) {
			generator.addProvider(true, new ShushLanguageProvider(packOutput));
			generator.addProvider(true, new ShushBlockStateProvider(packOutput, existingFileHelper));
			generator.addProvider(true, new ShushItemModelProvider(packOutput, existingFileHelper));
		}
	}
}
