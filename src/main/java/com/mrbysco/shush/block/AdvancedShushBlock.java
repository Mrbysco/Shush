package com.mrbysco.shush.block;

import com.mrbysco.shush.block.blockentity.ShushBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class AdvancedShushBlock extends ShushBlock {

	public AdvancedShushBlock(Properties properties) {
		super(properties, true);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.getBlockEntity(pos) instanceof ShushBlockEntity blockEntity) {
			if (level.isClientSide) {
				GlobalPos globalPos = GlobalPos.of(level.dimension(), pos);
				com.mrbysco.shush.client.screen.ShushScreen.openScreen(
						globalPos, blockEntity.getShushType()
				);
			}

			return InteractionResult.sidedSuccess(level.isClientSide);
		}

		return super.useWithoutItem(state, level, pos, player, hitResult);
	}
}
