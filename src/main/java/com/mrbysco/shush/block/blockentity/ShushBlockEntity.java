package com.mrbysco.shush.block.blockentity;

import com.mrbysco.shush.ShushMod;
import com.mrbysco.shush.client.ShushCache;
import com.mrbysco.shush.network.message.RemoveShushPayload;
import com.mrbysco.shush.registry.ShushRegistry;
import com.mrbysco.shush.util.ShushData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.network.PacketDistributor;
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
	public void preRemoveSideEffects(BlockPos pos, BlockState state) {
		super.preRemoveSideEffects(pos, state);
		PacketDistributor.sendToAllPlayers(new RemoveShushPayload(GlobalPos.of(level.dimension(), pos)));
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.shushData = input.read("shushData", ShushData.CODEC.codec()).orElse(null);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		if (this.shushData != null) {
			output.store("shushData", ShushData.CODEC.codec(), this.shushData);
		}
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider lookupProvider) {
		CompoundTag tag = new CompoundTag();
		try (ProblemReporter.ScopedCollector problemreporter$scopedcollector = new ProblemReporter.ScopedCollector(ShushMod.LOGGER)) {
			TagValueOutput output = TagValueOutput.createWithContext(problemreporter$scopedcollector, lookupProvider);
			this.saveAdditional(output);
			tag.merge(output.buildResult());
		}
		return tag;
	}

	@Override
	public CompoundTag getPersistentData() {
		CompoundTag tag = new CompoundTag();
		try (ProblemReporter.ScopedCollector problemreporter$scopedcollector = new ProblemReporter.ScopedCollector(ShushMod.LOGGER)) {
			HolderLookup.Provider lookupProvider = this.level != null ? this.level.registryAccess() : VanillaRegistries.createLookup();
			TagValueOutput output = TagValueOutput.createWithContext(problemreporter$scopedcollector, lookupProvider);
			this.saveAdditional(output);
			tag.merge(output.buildResult());
		}
		return tag;
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
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
