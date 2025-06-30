package ctn.vanilla_hotchpotch.init;

import ctn.vanilla_hotchpotch.common.fluid.PotionFluid;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static ctn.vanilla_hotchpotch.VhMain.VH_ID;

public class VhFluids {
	public static final DeferredRegister<Fluid> FLUID_REGISTER = DeferredRegister.create(BuiltInRegistries.FLUID, VH_ID);
	
	public static final DeferredHolder<Fluid, FlowingFluid> POTION_FLUID         = register("potion_fluid", PotionFluid.Source::new);
	public static final DeferredHolder<Fluid, FlowingFluid> FLOWING_POTION_FLUID = register("flowing_potion_fluid", PotionFluid.Flowing::new);
	
	private static DeferredHolder<Fluid, FlowingFluid> register(String name, Supplier<? extends FlowingFluid> fluidType) {
		return FLUID_REGISTER.register(name, fluidType);
	}
}
