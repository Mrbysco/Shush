package com.mrbysco.shush.block.blockentity;

import com.mrbysco.shush.client.ShushCache;
import com.mrbysco.shush.registry.ShushRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShushBlockEntity extends BlockEntity {
	@Nullable
	protected ShushType shushType;

	public ShushBlockEntity(BlockPos pos, BlockState blockState) {
		super(ShushRegistry.SHUSH_BLOCK_ENTITY.get(), pos, blockState);
	}

	public static void clientTick(Level level, BlockPos pos, BlockState state, ShushBlockEntity blockEntity) {
		if (level != null && level.getGameTime() % level.tickRateManager().tickrate() == 0) {
			ShushCache.addShushBlock(level, pos, blockEntity);
		}
	}

	@Nullable
	public ShushType getShushType() {
		return shushType;
	}

	@Override
	protected void loadAdditional(@NotNull CompoundTag tag, @NotNull Provider registries) {
		super.loadAdditional(tag, registries);
		if (tag.contains("shushType")) {
			this.shushType = ShushType.typeFromTag(tag.getCompound("shushType"));
		}
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, @NotNull Provider registries) {
		super.saveAdditional(tag, registries);
		if (this.shushType != null) {
			this.shushType.save(tag);
		}
	}
}
