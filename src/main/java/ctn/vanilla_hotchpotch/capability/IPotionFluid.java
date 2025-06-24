package ctn.vanilla_hotchpotch.capability;

import net.minecraft.core.NonNullList;
import net.minecraft.world.effect.MobEffectInstance;

public interface IPotionFluid {
	NonNullList<MobEffectInstance> getEffects();
	void addEffect(MobEffectInstance effect);
	void addEffect(int index, MobEffectInstance effect);
	MobEffectInstance removeEffect(int index);
	MobEffectInstance setEffects(int index, MobEffectInstance effect);
}
