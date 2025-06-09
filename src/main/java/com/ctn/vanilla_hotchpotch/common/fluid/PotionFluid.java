package com.ctn.vanilla_hotchpotch.common.fluid;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.List;

public abstract class PotionFluid extends Fluid {
	private final List<MobEffectInstance> effects = new ArrayList<>();

	public int getSize() {
		return effects.size();
	}

	public MobEffectInstance getEffect(int index) {
		return effects.get(index);
	}

	public boolean addEffect(MobEffectInstance effect) {
		for (int i = 0, effectsSize = effects.size(); i < effectsSize; i++) {
			if (effects.get(i).is(effect.getEffect())) {
				effects.set(i, effect);
			}
		}
		return effects.add(effect);
	}

	public MobEffectInstance setEffects(MobEffectInstance effect, int index) {
		return effects.set(index, effect);
	}

	public MobEffectInstance removeEffect(int index) {
		return effects.remove(index);
	}

	// 输出的不是原集合，请用别的方法操作集合
	public List<MobEffectInstance> getEffects(){
		return new ArrayList<>(effects);
	}
}
