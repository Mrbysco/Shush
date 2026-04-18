package com.mrbysco.shush.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.shush.block.blockentity.ShushBlockEntity;
import com.mrbysco.shush.client.ShushCache;
import com.mrbysco.shush.registry.ShushRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ShushBlock extends BaseEntityBlock {
	public static final MapCodec<ShushBlock> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
							propertiesCodec(),
							Codec.BOOL.fieldOf("advanced").forGetter(ShushBlock::getAdvanced)
					)
					.apply(instance, ShushBlock::new)
	);
	private final boolean advanced;

	public ShushBlock(Properties properties, boolean advanced) {
		super(properties);
		this.advanced = advanced;
	}

	public ShushBlock(Properties properties) {
		this(properties, false);
	}

	public boolean getAdvanced() {
		return this.advanced;
	}

	@Override
	protected MapCodec<ShushBlock> codec() {
		return CODEC;
	}

	@Override
	@Nullable
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ShushBlockEntity(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return createShushTicker(level, blockEntityType, ShushRegistry.SHUSH_BLOCK_ENTITY.get());
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createShushTicker(Level level, BlockEntityType<T> blockEntityType, BlockEntityType<? extends ShushBlockEntity> blockEntityType1) {
		return level.isClientSide ? createTickerHelper(blockEntityType, blockEntityType1, ShushBlockEntity::clientTick) : null;
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		super.onRemove(state, level, pos, newState, movedByPiston);
		if (level.isClientSide) {
			ShushCache.removeShushBlock(level, pos);
		}
	}
}
