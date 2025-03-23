package com.mrbysco.shush.datagen.assets;

import com.mrbysco.shush.ShushMod;
import com.mrbysco.shush.registry.ShushRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ShushLanguageProvider extends LanguageProvider {
	public ShushLanguageProvider(PackOutput packOutput) {
		super(packOutput, ShushMod.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		//Creative Tab
		add("itemGroup.shush", "Shush");

		addBlock(ShushRegistry.SHUSH_BLOCK, "Shush Block");
		addBlock(ShushRegistry.FILTERED_SHUSH_BLOCK, "Filtered Shush Block");
		addBlock(ShushRegistry.ADVANCED_SHUSH_BLOCK, "Advanced Shush Block");
	}
}
