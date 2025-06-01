package com.ctn.vanilla_hotchpotch.common.block.saucepan;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

import static com.ctn.vanilla_hotchpotch.common.block.saucepan.SaucepanBlockEntity.getBlockEntity;
import static com.ctn.vanilla_hotchpotch.init.VhBlockEntitys.SAUCEPAN_BLOCK_ENTITY_TYPE;
import static net.minecraft.world.Containers.dropContentsOnDestroy;
import static net.minecraft.world.ItemInteractionResult.*;

/**
 * 炖锅方块类，继承自BaseEntityBlock并实现EntityBlock接口。
 * 表示一个可以包含方块实体的方块。
 */
public abstract class AbstractSaucepanBlock<E extends SaucepanBlockEntity> extends BaseEntityBlock implements BucketPickup, EntityBlock, LiquidBlockContainer {
	/**
	 * 方向属性，表示该方块面对的方向。
	 */
	private static final DirectionProperty                      FACING = HorizontalDirectionalBlock.FACING;
	private final        int                                    capacity;
	private final        Supplier<BlockEntityType<? extends E>> blockEntityType;

	/**
	 * 构造函数。
	 * 初始化方块的基本属性。
	 */
	public AbstractSaucepanBlock(Properties properties, int capacity, Supplier<BlockEntityType<? extends E>> blockEntityType) {
		super(properties);
		this.capacity        = capacity;
		this.blockEntityType = blockEntityType;
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public boolean hasDynamicLightEmission(@NotNull BlockState state) {
		return true;
	}

	@Override
	public int getLightEmission(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		SaucepanBlockEntity blockEntity;
		if ((blockEntity = getBlockEntity(level, pos)) == null) {
			return super.getLightEmission(state, level, pos);
		}

		return blockEntity.getFluidInTank(0).getFluidType().getLightLevel();
	}

	/**
	 * 获取放置时的方块状态。
	 * 根据玩家放置时的方向设置方块的状态。
	 */
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	/**
	 * 创建方块状态定义。
	 * 添加方向属性到方块状态中。
	 */
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	/**
	 * 当使用物品与此方块交互时调用。
	 * 调用父类方法处理交互逻辑。
	 */
	@Override
	protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level,
			@NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
		if (stack.isEmpty()) {
			return PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}
		SaucepanBlockEntity blockEntity;
		if ((blockEntity = getBlockEntity(level, pos)) == null) {
			return FAIL;
		}
		InteractionResultHolder<ItemStack> resultHolder = stack.use(level, player, hand);
		switch (resultHolder.getResult()) {
			case SUCCESS -> {
			}
			case SUCCESS_NO_ITEM_USED -> {
			}
			case CONSUME -> {
			}
			case CONSUME_PARTIAL -> {
			}
			case PASS -> {
			}
			case FAIL -> {
			}
		}
		blockEntity.addItem(stack);
		return SUCCESS;
	}

	@Override
	protected boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType pathComputationType) {
		return false;
	}

	/**
	 * 取出物品
	 */
	@Override
	protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
		SaucepanBlockEntity blockEntity;
		if ((blockEntity = getBlockEntity(level, pos)) == null) {
			return InteractionResult.SUCCESS_NO_ITEM_USED;
		}
		NonNullList<ItemStack> items = blockEntity.getItems();
		if (items.isEmpty()) {
			return InteractionResult.SUCCESS_NO_ITEM_USED;
		}
		if (!player.getMainHandItem().isEmpty()) {
			return InteractionResult.SUCCESS_NO_ITEM_USED;
		}

		player.setItemSlot(EquipmentSlot.MAINHAND, blockEntity.removeItem(items.size() - 1, 1));
		return InteractionResult.SUCCESS;
	}

	@Override
	protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected abstract @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context);

	@Override
	protected abstract @NotNull VoxelShape getInteractionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos);

	/**
	 * 获取编码器。
	 */
	@Override
	protected abstract @NotNull MapCodec<? extends BaseEntityBlock> codec();

	/**
	 * 创建新的方块实体。
	 * 在给定位置创建一个新的方块实体。
	 */
	@Override
	public abstract BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state);

	/**
	 * 获取方块实体的计时器。
	 * 如果类型匹配，则返回对应的计时器。
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
		if (level.isClientSide()) {
			return null;
		}
		BlockEntityTicker<T> ticker = null;
		if (type == SAUCEPAN_BLOCK_ENTITY_TYPE.get()) {
			ticker = (l, p, s, b) -> E.tick(l, p, s, (E) b);
		}
		return ticker;
	}

	public int getCapacity() {
		return capacity;
	}

	@Override
	public abstract @NotNull ItemStack pickupBlock(@Nullable Player player, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state);

	@Override
	public @NotNull Optional<SoundEvent> getPickupSound() {
		return Optional.of(SoundEvents.BUCKET_FILL);
	}

	@Override
	protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
		if (getBlockEntity(level, pos) == null) {
			return;
		}
		dropContentsOnDestroy(state, newState, level, pos);
		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	/**
	 * 判断是否可以放置液体。
	 */
	@Override
	public abstract boolean canPlaceLiquid(@Nullable Player player, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Fluid fluid);

	/**
	 * 放置液体。
	 */
	@Override
	public abstract boolean placeLiquid(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull FluidState fluidState);

	public Supplier<BlockEntityType<? extends E>> getBlockEntityType() {
		return blockEntityType;
	}
}