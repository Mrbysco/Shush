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

		add("shush.advanced.screen", "Advanced Shush Block");
		add("shush.screen.selection.save", "Save selection");
		add("shush.screen.selection.select", "Select");
		add("shush.screen.selection.remove", "Un-select");
		add("shush.screen.search", "Search");
		add("shush.screen.search.a_to_z", "A-Z");
		add("shush.screen.search.z_to_a", "Z-A");

		add("shush.screen.selection.saved", "Selection saved");

		addBlock(ShushRegistry.SHUSH_BLOCK, "Shush Block");
		addBlock(ShushRegistry.FILTERED_SHUSH_BLOCK, "Filtered Shush Block");
		addBlock(ShushRegistry.ADVANCED_SHUSH_BLOCK, "Advanced Shush Block");
	}
}
