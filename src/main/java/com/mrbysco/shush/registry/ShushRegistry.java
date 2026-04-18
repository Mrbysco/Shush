package com.mrbysco.shush.registry;

import com.mrbysco.shush.ShushMod;
import com.mrbysco.shush.block.AdvancedShushBlock;
import com.mrbysco.shush.block.FilteredShushBlock;
import com.mrbysco.shush.block.ShushBlock;
import com.mrbysco.shush.block.blockentity.ShushBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class ShushRegistry {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ShushMod.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ShushMod.MOD_ID);
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ShushMod.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ShushMod.MOD_ID);

	public static final DeferredBlock<ShushBlock> SHUSH_BLOCK = BLOCKS.registerBlock("shush_block", (properties) -> new ShushBlock(
			properties.mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.GUITAR).strength(0.8F).sound(SoundType.WOOL)));
	public static final DeferredBlock<FilteredShushBlock> FILTERED_SHUSH_BLOCK = BLOCKS.registerBlock("filtered_shush_block", (properties) -> new FilteredShushBlock(
			properties.mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.GUITAR).strength(0.8F).sound(SoundType.WOOL)));
	public static final DeferredBlock<AdvancedShushBlock> ADVANCED_SHUSH_BLOCK = BLOCKS.registerBlock("advanced_shush_block", (properties) -> new AdvancedShushBlock(
			properties.mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.GUITAR).strength(0.8F).sound(SoundType.WOOL)));
	public static final DeferredItem<BlockItem> SHUSH_ITEM = ITEMS.registerSimpleBlockItem(SHUSH_BLOCK);
	public static final DeferredItem<BlockItem> FILTERED_SHUSH_ITEM = ITEMS.registerSimpleBlockItem(FILTERED_SHUSH_BLOCK);
	public static final DeferredItem<BlockItem> ADVANCED_SHUSH_ITEM = ITEMS.registerSimpleBlockItem(ADVANCED_SHUSH_BLOCK);

	public static final Supplier<BlockEntityType<ShushBlockEntity>> SHUSH_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("shush_block_entity", () -> BlockEntityType.Builder.of(
			ShushBlockEntity::new, ShushRegistry.SHUSH_BLOCK.get(), ShushRegistry.FILTERED_SHUSH_BLOCK.get(), ShushRegistry.ADVANCED_SHUSH_BLOCK.get()).build(null));

	public static final Supplier<CreativeModeTab> SHUSH_TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
			.icon(() -> new ItemStack(ShushRegistry.SHUSH_BLOCK.get()))
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.title(Component.translatable("itemGroup.shush"))
			.displayItems((features, output) -> {
				List<ItemStack> stacks = ShushRegistry.ITEMS.getEntries().stream().map(reg -> new ItemStack(reg.get())).toList();
				output.acceptAll(stacks);
			}).build());
}
