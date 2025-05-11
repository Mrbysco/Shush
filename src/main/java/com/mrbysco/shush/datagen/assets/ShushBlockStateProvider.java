package com.mrbysco.shush.datagen.assets;

import com.mrbysco.shush.ShushMod;
import com.mrbysco.shush.block.FilteredShushBlock;
import com.mrbysco.shush.registry.ShushRegistry;
import com.mrbysco.shush.util.SourceType;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ShushBlockStateProvider extends BlockStateProvider {

	public ShushBlockStateProvider(PackOutput packOutput, ExistingFileHelper helper) {
		super(packOutput, ShushMod.MOD_ID, helper);
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlock(ShushRegistry.SHUSH_BLOCK.get());
		registerFilteredShushBlock();
		simpleBlock(ShushRegistry.ADVANCED_SHUSH_BLOCK.get());
	}

	private void registerFilteredShushBlock() {
		String path = ShushRegistry.FILTERED_SHUSH_BLOCK.getId().getPath();
		getVariantBuilder(ShushRegistry.FILTERED_SHUSH_BLOCK.get())
				.forAllStates(state -> {
					SourceType sourceType = state.getValue(FilteredShushBlock.SOURCE);

					ModelFile model = models().getBuilder(path + "_" + sourceType.getSerializedName()).renderType("cutout")
							.parent(models().getExistingFile(modLoc("block/" + path)))
							.texture("type", modLoc("block/type_" + sourceType.getSerializedName()));

					return ConfiguredModel.builder()
							.modelFile(model)
							.build();
				});
	}
}