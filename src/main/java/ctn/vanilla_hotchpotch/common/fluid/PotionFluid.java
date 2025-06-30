package ctn.vanilla_hotchpotch.common.fluid;

import ctn.vanilla_hotchpotch.init.VhBlocks;
import ctn.vanilla_hotchpotch.init.VhFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.WaterFluid;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static ctn.vanilla_hotchpotch.init.VhFluidTypes.POTION_FLUID;
import static ctn.vanilla_hotchpotch.init.VhItems.POTION_BUCKET;

public abstract class PotionFluid extends WaterFluid {
	@Override
	public @NotNull Fluid getFlowing() {
		return VhFluids.FLOWING_POTION_FLUID.get();
	}
	
	@Override
	public @NotNull Fluid getSource() {
		return VhFluids.POTION_FLUID.get();
	}
	
	@Override
	public @NotNull Item getBucket() {
		return POTION_BUCKET.get();
	}
	
	//TODO
	@Nullable
	@Override
	public ParticleOptions getDripParticle() {
		return super.getDripParticle();
	}
	
	@Override
	protected boolean canConvertToSource(@NotNull Level level) {
		return false;
	}
	
	@Override
	public @NotNull BlockState createLegacyBlock(@NotNull FluidState state) {
		return VhBlocks.POTION_FLUID.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
	}
	
	@Override
	public boolean isSame(@NotNull Fluid fluid) {
		return fluid == VhFluids.POTION_FLUID.get() || fluid == VhFluids.FLOWING_POTION_FLUID.get();
	}
	
	//TODO
	@Override
	public boolean canBeReplacedWith(@NotNull FluidState fluidState, @NotNull BlockGetter blockReader, @NotNull BlockPos pos, @NotNull Fluid fluid, @NotNull Direction direction) {
		return direction == Direction.DOWN && !fluid.is(FluidTags.WATER);
	}
	
	@Override
	public @NotNull FluidType getFluidType() {
		return POTION_FLUID.get();
	}
	
	public static class Flowing extends PotionFluid {
		@Override
		protected void createFluidStateDefinition(StateDefinition.@NotNull Builder<Fluid, FluidState> builder) {
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}
		
		@Override
		public int getAmount(FluidState state) {
			return state.getValue(LEVEL);
		}
		
		@Override
		public boolean isSource(@NotNull FluidState state) {
			return false;
		}
	}
	
	public static class Source extends PotionFluid {
		@Override
		public int getAmount(@NotNull FluidState state) {
			return 8;
		}
		
		@Override
		public boolean isSource(@NotNull FluidState state) {
			return true;
		}
	}
}
