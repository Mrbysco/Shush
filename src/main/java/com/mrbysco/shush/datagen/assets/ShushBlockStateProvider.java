package com.mrbysco.shush.datagen.assets;

import com.mrbysco.shush.ShushMod;
import com.mrbysco.shush.registry.ShushRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ShushBlockStateProvider extends BlockStateProvider {

	public ShushBlockStateProvider(PackOutput packOutput, ExistingFileHelper helper) {
		super(packOutput, ShushMod.MOD_ID, helper);
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlock(ShushRegistry.SHUSH_BLOCK.get());
		simpleBlock(ShushRegistry.FILTERED_SHUSH_BLOCK.get());
		simpleBlock(ShushRegistry.ADVANCED_SHUSH_BLOCK.get());
	}
}