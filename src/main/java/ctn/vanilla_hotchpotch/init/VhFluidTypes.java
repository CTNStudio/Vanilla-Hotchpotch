package ctn.vanilla_hotchpotch.init;

import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

import static ctn.vanilla_hotchpotch.VhMain.VH_ID;

public class VhFluidTypes {
	public static final DeferredRegister<FluidType> FLUID_TYPE_REGISTER = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, VH_ID);
	
	public static final DeferredHolder<FluidType, FluidType> POTION_FLUID =
			register("potion_fluid", () -> new FluidType(
					FluidType.Properties.create()
							.descriptionId("block." + VH_ID + ".potion_fluid")
							.fallDistanceModifier(0F)
							.canExtinguish(true)
							.supportsBoating(true)
							.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
							.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
							.sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
			));
	
	private static DeferredHolder<FluidType, FluidType> register(String name, Supplier<? extends FluidType> fluidType) {
		return FLUID_TYPE_REGISTER.register(name, fluidType);
	}
}
