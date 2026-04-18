package com.mrbysco.shush.datagen.assets;

import com.mrbysco.shush.ShushMod;
import com.mrbysco.shush.block.FilteredShushBlock;
import com.mrbysco.shush.block.ShushBlock;
import com.mrbysco.shush.registry.ShushRegistry;
import com.mrbysco.shush.util.SourceType;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ShushBlockStateProvider extends BlockStateProvider {
	private final ResourceLocation TEMPLATE_PATH = modLoc("template_shush_block");
	public ShushBlockStateProvider(PackOutput packOutput, ExistingFileHelper helper) {
		super(packOutput, ShushMod.MOD_ID, helper);
	}

	@Override
	protected void registerStatesAndModels() {
		registerSimpleBlock(ShushRegistry.SHUSH_BLOCK, "white_wool");
		registerFilteredShushBlock();
		registerSimpleBlock(ShushRegistry.ADVANCED_SHUSH_BLOCK, "black_wool");
	}

	private void registerSimpleBlock(DeferredBlock<? extends Block> deferredBlock, String woolPath) {
		getVariantBuilder(deferredBlock.get())
				.forAllStates(state -> {
					ModelFile model = models().getBuilder(deferredBlock.getId().getPath()).renderType("cutout")
							.parent(models().getExistingFile(TEMPLATE_PATH))
							.texture("base", mcLoc("block/" + woolPath));

					return ConfiguredModel.builder()
							.modelFile(model)
							.build();
				});
	}

	private void registerFilteredShushBlock() {
		String path = ShushRegistry.FILTERED_SHUSH_BLOCK.getId().getPath();
		getVariantBuilder(ShushRegistry.FILTERED_SHUSH_BLOCK.get())
				.forAllStates(state -> {
					SourceType sourceType = state.getValue(FilteredShushBlock.SOURCE);

					ModelFile model = models().getBuilder(path + "_" + sourceType.getSerializedName()).renderType("cutout")
							.parent(models().getExistingFile(TEMPLATE_PATH))
							.texture("type", modLoc("block/type_" + sourceType.getSerializedName()));

					return ConfiguredModel.builder()
							.modelFile(model)
							.build();
				});
	}
}