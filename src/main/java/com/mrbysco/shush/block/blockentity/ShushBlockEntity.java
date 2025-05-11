package com.mrbysco.shush.block.blockentity;

import com.mrbysco.shush.client.ShushCache;
import com.mrbysco.shush.registry.ShushRegistry;
import com.mrbysco.shush.util.ShushData;
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
	protected ShushData shushData;

	public ShushBlockEntity(BlockPos pos, BlockState state) {
		super(ShushRegistry.SHUSH_BLOCK_ENTITY.get(), pos, state);
		if (state.is(ShushRegistry.ADVANCED_SHUSH_BLOCK)) {
			this.shushData = ShushData.EMPTY;
		}
	}

	public static void clientTick(Level level, BlockPos pos, BlockState state, ShushBlockEntity blockEntity) {
		if (level != null && level.getGameTime() % level.tickRateManager().tickrate() == 0) {
			ShushCache.addShushBlock(level, pos, blockEntity);
		}
	}

	@Nullable
	public ShushData getShushType() {
		return shushData;
	}

	public void setShushType(@Nullable ShushData shushData) {
		this.shushData = shushData;
	}

	@Override
	protected void loadAdditional(@NotNull CompoundTag tag, @NotNull Provider registries) {
		super.loadAdditional(tag, registries);
		if (tag.contains("shushType")) {
			this.shushData = ShushData.dataFromTag(tag.getCompound("shushType"));
		}
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, @NotNull Provider registries) {
		super.saveAdditional(tag, registries);
		if (this.shushData != null) {
			this.shushData.save(tag);
		}
	}

	/**
	 * Update the client whenever the Particle Emitter is modified
	 */
	public void refreshClient() {
		setChanged();
		BlockState state = level.getBlockState(worldPosition);
		level.sendBlockUpdated(worldPosition, state, state, 2);
	}
}
