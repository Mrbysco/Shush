package com.mrbysco.shush.datagen.data;

import com.mrbysco.shush.registry.ShushRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ShushRecipeProvider extends RecipeProvider {
	public ShushRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void buildRecipes(RecipeOutput output) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ShushRegistry.SHUSH_BLOCK.get(), 10)
				.pattern("WWW")
				.pattern("WNW")
				.pattern("WWW")
				.define('W', ItemTags.WOOL)
				.define('N', Items.NOTE_BLOCK)
				.unlockedBy("has_note_block", has(Items.NOTE_BLOCK))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ShushRegistry.FILTERED_SHUSH_BLOCK.get(), 8)
				.pattern("WWW")
				.pattern("WNW")
				.pattern("WWW")
				.define('W', ShushRegistry.SHUSH_BLOCK.get())
				.define('N', Items.PAPER)
				.unlockedBy("has_shush_block", has(ShushRegistry.SHUSH_BLOCK.get()))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ShushRegistry.ADVANCED_SHUSH_BLOCK.get())
				.pattern("FFF")
				.pattern("FFF")
				.pattern("FFF")
				.define('F', ShushRegistry.FILTERED_SHUSH_BLOCK.get())
				.unlockedBy("has_filtered_shush_block", has(ShushRegistry.FILTERED_SHUSH_BLOCK.get()))
				.save(output);

	}
}
