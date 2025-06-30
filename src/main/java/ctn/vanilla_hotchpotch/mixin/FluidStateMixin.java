package ctn.vanilla_hotchpotch.mixin;

import com.mojang.serialization.MapCodec;
import ctn.vanilla_hotchpotch.capability.IPotionFluid;
import ctn.vanilla_hotchpotch.capability_provider.PotionFluidHandler;
import ctn.vanilla_hotchpotch.common.fluid.PotionFluid;
import ctn.vanilla_hotchpotch.mixin_extend.IModFluidState;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.extensions.IFluidStateExtension;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FluidState.class)
@Implements(@Interface(iface = IModFluidState.class, prefix = "vanillaHotchpotchInt$"))
public abstract class FluidStateMixin extends StateHolder<Fluid, FluidState> implements IFluidStateExtension, IModFluidState {
	@Unique
	@Nullable
	private PotionFluidHandler vanillaHotchpotch$potionFluidBlockHandler;
	
	protected FluidStateMixin(Fluid owner, Reference2ObjectArrayMap<Property<?>, Comparable<?>> values, MapCodec<FluidState> propertiesCodec) {
		super(owner, values, propertiesCodec);
	}
	
	@Inject(method = "<init>", at = @At("RETURN"))
	public void vanillaHotchpotch$FluidStateMixin(Fluid owner, Reference2ObjectArrayMap<Property<?>, Comparable<?>> values, MapCodec<FluidState> propertiesCodec, CallbackInfo ci) {
		if (owner instanceof PotionFluid) {
			vanillaHotchpotch$potionFluidBlockHandler = new PotionFluidHandler();
		}
	}
	
	@Unique
	@Nullable
	public IPotionFluid vanillaHotchpotchInt$getPotionFluidBlock() {
		return vanillaHotchpotch$potionFluidBlockHandler;
	}
}
