package ctn.vanilla_hotchpotch.init;

import ctn.vanilla_hotchpotch.common.fluid.PotionFluid;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static ctn.vanilla_hotchpotch.VhMain.VH_ID;

public class VhFluids {
	public static final DeferredRegister<Fluid> FLUID_REGISTER = DeferredRegister.create(BuiltInRegistries.FLUID, VH_ID);
	
	public static final DeferredHolder<Fluid, PotionFluid> POTION_FLUID = FLUID_REGISTER.register("potion_fluid", PotionFluid::new);
}
