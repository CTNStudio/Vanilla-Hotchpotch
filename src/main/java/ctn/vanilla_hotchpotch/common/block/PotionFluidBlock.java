package ctn.vanilla_hotchpotch.common.block;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

public class PotionFluidBlock extends LiquidBlock {
	public PotionFluidBlock(FlowingFluid fluid, Properties properties) {
		super(fluid, properties);
	}
	
	public PotionFluidBlock(Supplier<? extends
			FlowingFluid> fluid, Properties properties) {
		super(fluid.get(), properties);
	}
}
