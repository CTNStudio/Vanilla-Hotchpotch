package com.ctn.vanilla_hotchpotch.common.block.saucepan;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

import static com.ctn.vanilla_hotchpotch.common.block.saucepan.SaucepanBlockEntity.getBlockEntity;
import static com.ctn.vanilla_hotchpotch.init.VhBlockEntityTypes.SAUCEPAN_BLOCK_ENTITY_TYPE;

/**
 * 炖锅方块类，继承自BaseEntityBlock并实现EntityBlock接口。
 * 表示一个可以包含方块实体的方块。
 */
public class SaucepanBlock extends AbstractSaucepanBlock<SaucepanBlockEntity> {
	private static final VoxelShape              INSIDE = Block.box(3, 1, 3, 13, 8, 13);
	private static final VoxelShape              SHAPE  = Shapes.join(
			Block.box(2, 0, 2, 14, 1, 14), Stream.of(
					Block.box(2, 1, 2, 3, 9, 14),
					Block.box(3, 1, 2, 13, 9, 3),
					Block.box(3, 1, 13, 13, 9, 14),
					Block.box(13, 1, 2, 14, 9, 14)
			).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), BooleanOp.OR);
	/**
	 * 方块编码器。
	 * 用于序列化和反序列化该方块。
	 */
	private static final MapCodec<SaucepanBlock> CODEC  = simpleCodec(SaucepanBlock::new);

	/**
	 * 构造函数。
	 * 初始化方块的基本属性。
	 */
	public SaucepanBlock(Properties properties, int capacity) {
		super(properties, capacity, SAUCEPAN_BLOCK_ENTITY_TYPE::get);
	}

	/**
	 * 构造函数
	 */
	public SaucepanBlock(Properties properties) {
		this(properties, 1000);
	}

	private static void extractFluid(SaucepanBlockEntity blockEntity, FluidStack fluid, int amount) {
		blockEntity.getFluidTankHandler().drain(new FluidStack(fluid.getFluidHolder(), amount), IFluidHandler.FluidAction.EXECUTE);
	}

	/**
	 * 获取编码器。
	 */
	@Override
	protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	/**
	 * 创建新的方块实体。
	 * 在给定位置创建一个新的方块实体。
	 */
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new SaucepanBlockEntity(pos, state, getCapacity());
	}

	@Override
	protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected @NotNull VoxelShape getInteractionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		return INSIDE;
	}

	@Override
	public @NotNull ItemStack pickupBlock(@Nullable Player player, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state) {
		SaucepanBlockEntity blockEntity;
		if ((blockEntity = getBlockEntity(level, pos)) == null) {
			return ItemStack.EMPTY;
		}
		if (player == null) {
			return ItemStack.EMPTY;
		}

		ItemStack stack = player.getMainHandItem();
		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		}

		FluidStack fluid = blockEntity.getFluidTankHandler().getFluidInTank(0);
		if (fluid.getFluid().isSame(Fluids.EMPTY)) {
			return ItemStack.EMPTY;
		}

		Fluid fluidType = fluid.getFluid();
		int amount = fluid.getAmount();

		if (fluidType.isSame(Fluids.WATER)) {
			if (stack.is(Items.BUCKET) && amount >= 1000) {
				extractFluid(blockEntity, fluid, 1000);
				return Items.WATER_BUCKET.getDefaultInstance();
			}
			if (stack.is(Items.GLASS_BOTTLE) && amount >= 250) {
				extractFluid(blockEntity, fluid, 250);
				return Items.POTION.getDefaultInstance();
			}
		} else if (fluidType.isSame(Fluids.LAVA)) {
			if (stack.is(Items.BUCKET) && amount >= 1000) {
				extractFluid(blockEntity, fluid, 1000);
				return Items.LAVA_BUCKET.getDefaultInstance();
			}
		}

		return ItemStack.EMPTY;
	}

	/**
	 * 判断是否可以放置液体。
	 */
	@Override
	public boolean canPlaceLiquid(@Nullable Player player, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Fluid fluid) {
		SaucepanBlockEntity blockEntity;
		if ((blockEntity = getBlockEntity(level, pos)) == null) {
			return false;
		}
		if (fluid.isSame(Fluids.EMPTY)) {
			return false;
		}
		if (!blockEntity.getFluidTankHandler().getFluidInTank(0).isEmpty()) {
			if (!blockEntity.getFluidTankHandler().getFluidInTank(0).is(fluid.getFluidType())) {
				return false;
			}
		}
		if (player != null) {
			ItemStack stack = player.getMainHandItem();
			if (stack.is(Tags.Items.BUCKETS)) {
				blockEntity.getFluidTankHandler().fill(new FluidStack(fluid, 1000), IFluidHandler.FluidAction.EXECUTE);
				return true;
			}
			if (stack.is(Tags.Items.DRINKS)) {
				blockEntity.getFluidTankHandler().fill(new FluidStack(fluid, 250), IFluidHandler.FluidAction.EXECUTE);
				return true;
			}
		}

		return blockEntity.getFluidTankHandler().getFluidInTank(0).getAmount() != blockEntity.getFluidTankHandler().getTankCapacity(0);
	}

	/**
	 * 放置液体。
	 */
	@Override
	public boolean placeLiquid(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull FluidState fluidState) {
		return getBlockEntity(level, pos) != null;
	}
}