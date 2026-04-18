package com.mrbysco.shush.block;

import com.mrbysco.shush.block.blockentity.ShushBlockEntity;
import com.mrbysco.shush.util.SourceType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

public class FilteredShushBlock extends ShushBlock {
	public static final EnumProperty<SourceType> SOURCE = EnumProperty.create("source", SourceType.class);

	public FilteredShushBlock(Properties properties) {
		super(properties, true);
		this.registerDefaultState(this.defaultBlockState().setValue(SOURCE, SourceType.MASTER));
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		} else {
			state = state.cycle(SOURCE);
			level.setBlock(pos, state, 3);
			if (level.getBlockEntity(pos) instanceof ShushBlockEntity shushBlockEntity)
				shushBlockEntity.setChanged();
			return InteractionResult.CONSUME;
		}
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(SOURCE);
	}
}
