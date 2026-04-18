package com.mrbysco.shush.datagen.assets;

import com.mrbysco.shush.ShushMod;
import com.mrbysco.shush.registry.ShushRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ShushItemModelProvider extends ItemModelProvider {
	public ShushItemModelProvider(PackOutput packOutput, ExistingFileHelper helper) {
		super(packOutput, ShushMod.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		withExistingParent(ShushRegistry.SHUSH_ITEM.getId().getPath(), modLoc(BLOCK_FOLDER + "/" + ShushRegistry.SHUSH_ITEM.getId().getPath()));
		withExistingParent(ShushRegistry.FILTERED_SHUSH_ITEM.getId().getPath(), modLoc(BLOCK_FOLDER + "/" + ShushRegistry.FILTERED_SHUSH_ITEM.getId().getPath() + "_master"));
		withExistingParent(ShushRegistry.ADVANCED_SHUSH_ITEM.getId().getPath(), modLoc(BLOCK_FOLDER + "/" + ShushRegistry.ADVANCED_SHUSH_ITEM.getId().getPath()));
	}
}