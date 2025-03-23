package com.mrbysco.shush.client;

import com.mrbysco.shush.block.blockentity.ShushBlockEntity;
import com.mrbysco.shush.block.blockentity.ShushType;
import com.mrbysco.shush.registry.ShushRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShushCache {
	private static final List<GlobalPos> SHUSH_LIST = new ArrayList<>();
	private static final Map<GlobalPos, ShushType> ADVANCED_SHUSH_MAP = new HashMap<>();

	public static void addShushBlock(Level level, BlockPos pos, ShushBlockEntity shushBlockEntity) {
		GlobalPos globalPos = GlobalPos.of(level.dimension(), pos);
		if (shushBlockEntity.getBlockState().is(ShushRegistry.SHUSH_BLOCK)) {
			if (!ShushCache.SHUSH_LIST.contains(globalPos))
				ShushCache.SHUSH_LIST.add(globalPos);
		} else {
			if (!ShushCache.ADVANCED_SHUSH_MAP.containsKey(globalPos))
				ShushCache.ADVANCED_SHUSH_MAP.put(globalPos, shushBlockEntity.getShushType());
			// NOOP
		}
	}

	public static int shushCount(ResourceKey<Level> dimension, BlockPos position, int maxRange) {
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
		ADVANCED_SHUSH_MAP.remove(globalPos);
	}

	public static void clearCache() {
		SHUSH_LIST.clear();
		ADVANCED_SHUSH_MAP.clear();
	}
}
