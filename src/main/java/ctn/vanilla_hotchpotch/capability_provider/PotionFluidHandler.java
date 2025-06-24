package ctn.vanilla_hotchpotch.capability_provider;

import ctn.vanilla_hotchpotch.capability.IPotionFluid;
import ctn.vanilla_hotchpotch.mixin_extend.IModFluidState;
import net.minecraft.core.NonNullList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class PotionFluidHandler implements IPotionFluid {
	private static final Logger log = LogManager.getLogger(PotionFluidHandler.class);
	private final NonNullList<MobEffectInstance> effects;
	
	public PotionFluidHandler(List<MobEffectInstance> effects){
		this.effects = NonNullList.copyOf(effects);
	}
	
	public PotionFluidHandler(){
		this.effects = NonNullList.create();
	}
	
	public PotionFluidHandler(FluidState state){
		IPotionFluid invoke = null;
		(IModFluidState)state
		try {
			Method method = IModFluidState.class.getMethod("getPotionFluidBlock");
			invoke = (IPotionFluid) method.invoke(state);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			log.error("Failed to get PotionFluidBlock from FluidState");
			log.error(e);
		}
		if (invoke != null) {
			this.effects = invoke.getEffects();
		} else {
			this.effects = null;
		}
	}
	
	@Override
	public NonNullList<MobEffectInstance> getEffects() {
		return effects;
	}
	
	@Override
	public void addEffect(MobEffectInstance effect) {
		effects.add(effect);
	}
	
	@Override
	public void addEffect(int index, MobEffectInstance effect) {
		effects.add(index, effect);
	}
	
	@Override
	public MobEffectInstance removeEffect(int index) {
		return effects.remove(index);
	}
	
	@Override
	public MobEffectInstance setEffects(int index, MobEffectInstance effect) {
		return effects.set(index, effect);
	}
}
