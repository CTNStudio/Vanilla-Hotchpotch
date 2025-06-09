package com.ctn.vanilla_hotchpotch.common.block.saucepan;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

import static com.ctn.vanilla_hotchpotch.common.block.saucepan.SaucepanBlockEntity.getBlockEntity;
import static com.ctn.vanilla_hotchpotch.init.VhBlockEntityTypes.SAUCEPAN_BLOCK_ENTITY_TYPE;
import static net.minecraft.world.Containers.dropContentsOnDestroy;
import static net.minecraft.world.ItemInteractionResult.*;

/**
 * 炖锅方块类，继承自BaseEntityBlock并实现EntityBlock接口。
 * 表示一个可以包含方块实体的方块。
 */
public abstract class AbstractSaucepanBlock<E extends SaucepanBlockEntity> extends BaseEntityBlock
		implements EntityBlock {
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
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.NORTH));
	}

	private static void updateSelf(@NotNull final BlockEntity tile) {
		Objects.requireNonNull(tile);

		final BlockState state = tile.getBlockState();
		final BlockPos pos = tile.getBlockPos();
		final Level world = tile.getLevel();

		Objects.requireNonNull(world);

		world.setBlockAndUpdate(pos, state);
	}

	// 液体交互
	private static boolean liquidInteractionOfItems(@NotNull ItemStack stack, @NotNull Player player, @NotNull InteractionHand hand, @NotNull SaucepanBlockEntity blockEntity) {
		// 获取方块的流体处理
		IFluidHandler blockHandler = blockEntity.getFluidTankHandler();
		// 复制一份以给创造模式使用
		IFluidHandlerItem copyItemHandler = stack.copyWithCount(1).getCapability(Capabilities.FluidHandler.ITEM);
		if (copyItemHandler != null) {
			IFluidHandlerItem itemHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);

			// 只取第一个液体
			FluidStack blockFluidStack = blockHandler.getFluidInTank(0);
			FluidStack copyItemFluidStack = copyItemHandler.getFluidInTank(0);

			// 方块容器液体为空时 || 方块容器液体 等于 物品容器液体时
			if (blockFluidStack.isEmpty() && !copyItemFluidStack.isEmpty() || blockFluidStack.is(copyItemFluidStack.getFluidHolder())) {
				// 需要导入的液体堆栈
				FluidStack resource = new FluidStack(copyItemFluidStack.getFluid(), copyItemFluidStack.getAmount());

				// 如果导入成功
				if (blockHandler.fill(copyItemHandler.drain(resource, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) > 0) {
					if (!player.isCreative()) {
						blockHandler.fill(itemHandler.drain(resource, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
						player.setItemInHand(hand, itemHandler.getContainer());
					} else {
						blockHandler.fill(copyItemHandler.drain(resource, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
					}

					return true;
				}
			} else if (copyItemFluidStack.isEmpty() && !blockFluidStack.isEmpty()) { // 物品容器液体为空时 && 方块容器液体不为空时
				// 需要导入的液体堆栈
				FluidStack resource = new FluidStack(blockFluidStack.getFluid(), blockFluidStack.getAmount());

				// 如果导入成功
				if (copyItemHandler.fill(blockHandler.drain(resource, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) > 0) {
					if (!player.isCreative()) {
						itemHandler.fill(blockHandler.drain(resource, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
						player.setItemInHand(hand, itemHandler.getContainer());
					} else {
						blockHandler.drain(resource, IFluidHandler.FluidAction.EXECUTE);
					}

					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean hasDynamicLightEmission(@NotNull BlockState state) {
		return super.hasDynamicLightEmission(state);
	}

	// TODO
	@Override
	public int getLightEmission(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		SaucepanBlockEntity blockEntity;
		if ((blockEntity = getBlockEntity(level, pos)) == null) {
			return super.getLightEmission(state, level, pos);
		}
		var i = blockEntity.getFluidTankHandler().getFluidInTank(0).getFluidType().getLightLevel();
		return i;
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
	 * 添加方向属性和流体记录属性到方块状态中。
	 */
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	/**
	 * 与此方块交互时调用。
	 */
	@Override
	protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level,
			@NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
		if (stack.isEmpty()) {
			return PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}

		if (player.isShiftKeyDown()) {
			return PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}

		// 获取方块实体
		SaucepanBlockEntity blockEntity;
		if ((blockEntity = getBlockEntity(level, pos)) == null) {
			return FAIL;
		}

		if (!stack.isEmpty() && liquidInteractionOfItems(stack, player, hand, blockEntity)) {
			updateSelf(blockEntity);
			return SUCCESS;
		}

		player.getInventory().setChanged();
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

		// 获取物品
		ItemStack itemStack = blockEntity.removeItem(items.size() - 1, 1);

		if (player.addItem(itemStack)) {
			player.getInventory().setChanged();
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.SUCCESS_NO_ITEM_USED;

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
	protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
		if (getBlockEntity(level, pos) == null) {
			return;
		}
		dropContentsOnDestroy(state, newState, level, pos);
		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	public Supplier<BlockEntityType<? extends E>> getBlockEntityType() {
		return blockEntityType;
	}
}