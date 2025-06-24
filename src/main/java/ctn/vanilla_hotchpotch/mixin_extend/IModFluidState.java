package ctn.vanilla_hotchpotch.mixin_extend;


import ctn.vanilla_hotchpotch.capability.IPotionFluid;
import org.jetbrains.annotations.Nullable;

public interface IModFluidState {
	@Nullable
	IPotionFluid getPotionFluidBlock();
}
