package com.mrbysco.shush.datagen.data;

import com.mrbysco.shush.registry.ShushRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ShushRecipeProvider extends RecipeProvider {
	public ShushRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
		super(provider, recipeOutput);
	}

	@Override
	protected void buildRecipes() {
		shaped(RecipeCategory.MISC, ShushRegistry.SHUSH_BLOCK.get(), 10)
				.pattern("WWW")
				.pattern("WNW")
				.pattern("WWW")
				.define('W', ItemTags.WOOL)
				.define('N', Items.NOTE_BLOCK)
				.unlockedBy("has_note_block", has(Items.NOTE_BLOCK))
				.save(output);

		shaped(RecipeCategory.MISC, ShushRegistry.FILTERED_SHUSH_BLOCK.get(), 8)
				.pattern("WWW")
				.pattern("WNW")
				.pattern("WWW")
				.define('W', ShushRegistry.SHUSH_BLOCK.get())
				.define('N', Items.PAPER)
				.unlockedBy("has_shush_block", has(ShushRegistry.SHUSH_BLOCK.get()))
				.save(output);

		shaped(RecipeCategory.MISC, ShushRegistry.ADVANCED_SHUSH_BLOCK.get())
				.pattern("FFF")
				.pattern("FFF")
				.pattern("FFF")
				.define('F', ShushRegistry.FILTERED_SHUSH_BLOCK.get())
				.unlockedBy("has_filtered_shush_block", has(ShushRegistry.FILTERED_SHUSH_BLOCK.get()))
				.save(output);

	}

	public static class Runner extends RecipeProvider.Runner {
		public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
			super(output, completableFuture);
		}

		@Override
		protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
			return new ShushRecipeProvider(provider, recipeOutput);
		}

		@Override
		public String getName() {
			return "Shush Recipes";
		}
	}
}
