package ctn.vanilla_hotchpotch.common.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static ctn.vanilla_hotchpotch.init.VhItems.POTION_BUCKET;

public class PotionFluid extends Fluid {
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
	
	@Override
	public @NotNull Item getBucket() {
		return POTION_BUCKET.get();
	}
	
	@Override
	protected boolean canBeReplacedWith(@NotNull FluidState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Fluid fluid, @NotNull Direction direction) {
		return false;
	}
	
	@Override
	protected @NotNull Vec3 getFlow(@NotNull BlockGetter blockReader, @NotNull BlockPos pos, @NotNull FluidState fluidState) {
		return Vec3.ZERO;
	}
	
	@Override
	public int getTickDelay(@NotNull LevelReader level) {
		return 0;
	}
	
	@Override
	protected float getExplosionResistance() {
		return 0;
	}
	
	@Override
	public float getHeight(@NotNull FluidState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		return 0;
	}
	
	@Override
	public float getOwnHeight(@NotNull FluidState state) {
		return 0;
	}
	
	@Override
	protected @NotNull BlockState createLegacyBlock(@NotNull FluidState state) {
		return Blocks.AIR.defaultBlockState();
	}
	
	@Override
	public boolean isSource(@NotNull FluidState state) {
		return false;
	}
	
	@Override
	public int getAmount(@NotNull FluidState state) {
		return state.getAmount();
	}
	
	@Override
	public @NotNull VoxelShape getShape(@NotNull FluidState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		return Shapes.empty();
	}
}
