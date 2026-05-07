package com.mrbysco.shush.datagen.data;

import com.mrbysco.shush.registry.ShushRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ShushLootProvider extends LootTableProvider {

	public ShushLootProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, Set.of(), List.of(
				new SubProviderEntry(BoomboxBlockLoot::new, LootContextParamSets.BLOCK)
		), lookupProvider);
	}

	private static class BoomboxBlockLoot extends BlockLootSubProvider {

		protected BoomboxBlockLoot(HolderLookup.Provider provider) {
			super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
		}

		@Override
		protected void generate() {
			this.dropSelf(ShushRegistry.SHUSH_BLOCK.get());
			this.dropSelf(ShushRegistry.FILTERED_SHUSH_BLOCK.get());
			this.dropSelf(ShushRegistry.ADVANCED_SHUSH_BLOCK.get());
		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			return ShushRegistry.BLOCKS.getEntries().stream().map(holder -> (Block)holder.value())::iterator;
		}
	}
}
