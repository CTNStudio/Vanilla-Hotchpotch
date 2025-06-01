package com.ctn.vanilla_hotchpotch.common.block.saucepan;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public interface SaucepanInteraction {
	SaucepanInteraction interact(SaucepanBlockEntity blockEntity, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, FluidState fluidState);
}
