package com.mrbysco.shush.client;

import com.mrbysco.shush.block.FilteredShushBlock;
import com.mrbysco.shush.block.blockentity.ShushBlockEntity;
import com.mrbysco.shush.registry.ShushRegistry;
import com.mrbysco.shush.util.ShushData;
import com.mrbysco.shush.util.SourceType;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShushCache {
	private static final List<GlobalPos> SHUSH_LIST = new ArrayList<>();
	private static final Map<GlobalPos, SourceType> FILTERED_SHUSH_MAP = new HashMap<>();
	private static final Map<GlobalPos, ShushData> ADVANCED_SHUSH_MAP = new HashMap<>();

	public static void addShushBlock(Level level, BlockPos pos, ShushBlockEntity shushBlockEntity) {
		GlobalPos globalPos = GlobalPos.of(level.dimension(), pos);
		if (shushBlockEntity.getBlockState().is(ShushRegistry.SHUSH_BLOCK)) {
			if (!ShushCache.SHUSH_LIST.contains(globalPos)) {
				ShushCache.SHUSH_LIST.add(globalPos);
			}
		} else {
			BlockState state = shushBlockEntity.getBlockState();
			if (state.is(ShushRegistry.FILTERED_SHUSH_BLOCK)) {
				if (state.hasProperty(FilteredShushBlock.SOURCE)) {
					SourceType sourceType = state.getValue(FilteredShushBlock.SOURCE);
					//Check if the position is missing from the map or if the source type is different
					if (ShushCache.FILTERED_SHUSH_MAP.containsKey(globalPos)) {
						SourceType existingType = ShushCache.FILTERED_SHUSH_MAP.get(globalPos);
						if (!existingType.equals(sourceType)) {
							ShushCache.FILTERED_SHUSH_MAP.put(globalPos, sourceType);
						}
					} else {
						//Add the new entry to the map
						ShushCache.FILTERED_SHUSH_MAP.put(globalPos, sourceType);
					}
				}
			} else {
				if (!ShushCache.ADVANCED_SHUSH_MAP.containsKey(globalPos))
					ShushCache.ADVANCED_SHUSH_MAP.put(globalPos, shushBlockEntity.getShushType());
			}
			// NOOP
		}
	}

	public static float getShushedBy(ResourceKey<Level> dimension, BlockPos position, AbstractSoundInstance instance, int maxRange) {
		float shushBy = 0.0F;
		//Check the regular shush count
		int globalShushCount = globalShushCount(dimension, position, maxRange);
		//Check the filtered shush count
		int typedShushCount = getTypedShushCount(dimension, position, instance.getSource(), maxRange);

		int totalShushCount = globalShushCount + typedShushCount;
		if (totalShushCount > 0) {
			shushBy += 0.1F * totalShushCount;
		}

		return shushBy;
	}

	public static int globalShushCount(ResourceKey<Level> dimension, BlockPos position, int maxRange) {
		int count = 0;
		for (GlobalPos mufflerPos : SHUSH_LIST) {
			BlockPos mufflerBlockPos = mufflerPos.pos();
			ResourceKey<Level> mufflerDimension = mufflerPos.dimension();
			if (mufflerDimension.equals(dimension) && mufflerBlockPos.closerThan(position, maxRange)) {
				count++;
			}
		}
		return count;
	}

	public static int getTypedShushCount(ResourceKey<Level> dimension, BlockPos position, SoundSource sourceType, int maxRange) {
		int count = 0;
		for (Map.Entry<GlobalPos, SourceType> entry : FILTERED_SHUSH_MAP.entrySet()) {
			GlobalPos mufflerPos = entry.getKey();
			BlockPos mufflerBlockPos = mufflerPos.pos();
			ResourceKey<Level> mufflerDimension = mufflerPos.dimension();
			if (mufflerDimension.equals(dimension) && mufflerBlockPos.closerThan(position, maxRange)) {
				SourceType shushData = entry.getValue();
				if (shushData != null && sourceType == entry.getValue().getSource()) {
					count++;
				}
			}
		}
		return count;
	}

	public static void pruneMap(Level level, BlockPos position, int maxRange) {
		SHUSH_LIST.removeIf(globalPos -> {
			BlockPos mufflerBlockPos = globalPos.pos();
			if (!(level.getBlockEntity(mufflerBlockPos) instanceof ShushBlockEntity))
				return true;
			ResourceKey<Level> mufflerDimension = globalPos.dimension();
			return !mufflerDimension.equals(level.dimension()) || !mufflerBlockPos.closerThan(position, maxRange); // Remove if out of range
		});

		ADVANCED_SHUSH_MAP.entrySet().removeIf(globalPos -> {
			BlockPos mufflerBlockPos = globalPos.getKey().pos();
			if (!(level.getBlockEntity(mufflerBlockPos) instanceof ShushBlockEntity))
				return true;
			ResourceKey<Level> mufflerDimension = globalPos.getKey().dimension();
			return !mufflerDimension.equals(level.dimension()) || !mufflerBlockPos.closerThan(position, maxRange); // Remove if out of range
		});
	}

	public static void removeShushBlock(Level level, BlockPos pos) {
		GlobalPos globalPos = GlobalPos.of(level.dimension(), pos);
		SHUSH_LIST.remove(globalPos);
	}

	public static void removeShushBlock(GlobalPos globalPos) {
		SHUSH_LIST.remove(globalPos);
		FILTERED_SHUSH_MAP.remove(globalPos);
		ADVANCED_SHUSH_MAP.remove(globalPos);
	}

	public static void clearCache() {
		SHUSH_LIST.clear();
		FILTERED_SHUSH_MAP.clear();
		ADVANCED_SHUSH_MAP.clear();
	}
}
