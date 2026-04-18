package com.mrbysco.shush.datagen.assets;

import com.mrbysco.shush.ShushMod;
import com.mrbysco.shush.block.FilteredShushBlock;
import com.mrbysco.shush.registry.ShushRegistry;
import com.mrbysco.shush.util.SourceType;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

public class ShushModelProvider extends ModelProvider {
	private static final TextureSlot BASE = TextureSlot.create("base");
	private static final TextureSlot TYPE = TextureSlot.create("type");
	public static final ModelTemplate TEMPLATE_SHUSH_BLOCK = ModelTemplates.create("shush:template_shush_block", BASE, TYPE);

	public ShushModelProvider(PackOutput output) {
		super(output, ShushMod.MOD_ID);
	}

	@Override
	protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
		registerSimpleBlock(blockModels, ShushRegistry.SHUSH_BLOCK.get(), "white_wool", "master");
		registerFilteredShushBlock(blockModels);
		registerSimpleBlock(blockModels, ShushRegistry.ADVANCED_SHUSH_BLOCK.get(), "black_wool", "master");
	}

	private void registerSimpleBlock(BlockModelGenerators blockModels, Block block, String woolPath, String type) {
		TextureMapping textures = new TextureMapping()
				.put(BASE, new Material(mcLocation("block/" + woolPath)))
				.put(TYPE, new Material(modLocation("block/type_" + type)));

		Identifier model = TEMPLATE_SHUSH_BLOCK.create(block, textures, blockModels.modelOutput);
		blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(model)));
		blockModels.registerSimpleItemModel(block, model);
	}

	private void registerFilteredShushBlock(BlockModelGenerators blockModels) {
		Block block = ShushRegistry.FILTERED_SHUSH_BLOCK.get();
		PropertyDispatch<MultiVariant> dispatch = PropertyDispatch.initial(FilteredShushBlock.SOURCE)
				.generate(sourceType -> {
					String suffix = "_" + sourceType.getSerializedName();
					TextureMapping textures = new TextureMapping()
							.put(BASE, TextureMapping.getBlockTexture(block))
							.put(TYPE, new Material(modLocation("block/type_" + sourceType.getSerializedName())));
					Identifier model = TEMPLATE_SHUSH_BLOCK.createWithSuffix(block, suffix, textures, blockModels.modelOutput);
					return BlockModelGenerators.plainVariant(model);
				});

		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(dispatch));
		blockModels.registerSimpleItemModel(block, ModelLocationUtils.getModelLocation(block, "_" + SourceType.AMBIENT.getSerializedName()));
	}
}
