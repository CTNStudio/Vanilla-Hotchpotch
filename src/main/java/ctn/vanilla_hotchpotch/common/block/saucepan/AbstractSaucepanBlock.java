package ctn.vanilla_hotchpotch.common.block.saucepan;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

import static ctn.vanilla_hotchpotch.common.block.saucepan.SaucepanBlockEntity.getBlockEntity;
import static ctn.vanilla_hotchpotch.init.VhBlockEntityTypes.SAUCEPAN_BLOCK_ENTITY_TYPE;
import static net.minecraft.world.Containers.dropContentsOnDestroy;
import static net.minecraft.world.ItemInteractionResult.*;
import static net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.*;

/**
 * 炖锅方块类，继承自BaseEntityBlock并实现EntityBlock接口。
 * 表示一个可以包含方块实体的方块。
 */
public abstract class AbstractSaucepanBlock<E extends SaucepanBlockEntity> extends BaseEntityBlock
		implements EntityBlock {
	public static final  IntegerProperty                        LIGHT_LEVEL = IntegerProperty.create("light_level", 0, 15);
	/**
	 * 方向属性，表示该方块面对的方向。
	 */
	private static final DirectionProperty                      FACING      = HorizontalDirectionalBlock.FACING;
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
		this.registerDefaultState(
				this.stateDefinition.any()
						.setValue(FACING, Direction.NORTH)
						.setValue(LIGHT_LEVEL, 0)
		);
	}
	
	private void updateSelf(@NotNull final BlockEntity tile, Level level, BlockPos pos) {
		Objects.requireNonNull(tile);
		final BlockState state = tile.getBlockState();
		final BlockPos blockPos = tile.getBlockPos();
		final Level world = tile.getLevel();
		
		Objects.requireNonNull(world);
		
		world.setBlockAndUpdate(blockPos, state);
	}
	
	@Override
	public boolean hasDynamicLightEmission(@NotNull BlockState state) {
		return true;
	}
	
	@Override
	public int getLightEmission(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		return state.getValue(LIGHT_LEVEL);
	}
	
	/**
	 * 获取放置时的方块状态。
	 * 根据玩家放置时的方向设置方块的状态。
	 */
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState()
				.setValue(FACING, context.getHorizontalDirection())
				.setValue(LIGHT_LEVEL, 0);
	}
	
	/**
	 * 创建方块状态定义。
	 * 添加方向属性和流体记录属性到方块状态中。
	 */
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING).add(LIGHT_LEVEL);
	}
	
	@Override
	protected boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType pathComputationType) {
		return false;
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
			IFluidHandler fluidTankHandler = blockEntity.getFluidTankHandler();
			FluidStack fluid = fluidTankHandler.getFluidInTank(0);
			FluidType fluidType = fluid.getFluidType();
			int lightLevel = fluidType.getLightLevel();
			level.setBlock(pos, state.setValue(LIGHT_LEVEL, lightLevel), Block.UPDATE_ALL);
//			updateSelf(blockEntity, level, pos);
			return SUCCESS;
		}
		
		player.getInventory().setChanged();
		blockEntity.addItem(stack);
		return SUCCESS;
	}
	
	// 液体交互
	private static boolean liquidInteractionOfItems(@NotNull ItemStack stack, @NotNull Player player, @NotNull InteractionHand hand, @NotNull SaucepanBlockEntity blockEntity) {
		final ItemCapability<IFluidHandlerItem, @Nullable Void> fluidHandler = Capabilities.FluidHandler.ITEM;
		
		// 获取方块的流体处理
		final IFluidHandler blockHandler = blockEntity.getFluidTankHandler();
		final ItemStack itemStack = stack.copyWithCount(1);
		// 复制一份以给创造模式使用
		IFluidHandlerItem copyItemHandler = itemStack.getCapability(fluidHandler);
		if (copyItemHandler != null) {
			final IFluidHandlerItem itemHandler = stack.getCapability(fluidHandler);
			final ItemStack container;
			if (itemHandler != null) {
				container = itemHandler.getContainer();
			} else {
				container = null;
			}
			// 只取第一个液体
			final FluidStack blockFluidStack = blockHandler.getFluidInTank(0);
			final FluidStack copyItemFluidStack = copyItemHandler.getFluidInTank(0);

			final Holder<Fluid> fluidHolder = copyItemFluidStack.getFluidHolder();
			/// 方块容器液体为空时 || 方块容器液体 等于 物品容器液体时
			if (blockFluidStack.isEmpty() && !copyItemFluidStack.isEmpty() || blockFluidStack.is(fluidHolder)) {
				// 需要导入的液体堆栈
				final Fluid fluid = copyItemFluidStack.getFluid();
				final int amount = copyItemFluidStack.getAmount();
				final FluidStack resource = new FluidStack(fluid, amount);
				
				// 如果导入成功
				final FluidStack simulateDrain = copyItemHandler.drain(resource, SIMULATE);
				int fill = blockHandler.fill(simulateDrain, SIMULATE);
				if (fill > 0) {
					if (!player.isCreative() && itemHandler != null) {
						final FluidStack drain = itemHandler.drain(resource, EXECUTE);
						blockHandler.fill(drain, EXECUTE);
						player.setItemInHand(hand, container);
					} else {
						final FluidStack drain = copyItemHandler.drain(resource, EXECUTE);
						blockHandler.fill(drain, EXECUTE);
					}
					return true;
				}
			}
			/// 物品容器液体为空时 && 方块容器液体不为空时
			else if (copyItemFluidStack.isEmpty() && !blockFluidStack.isEmpty()) {
				// 需要导出的液体堆栈
				final Fluid fluid = blockFluidStack.getFluid();
				final int amount = blockFluidStack.getAmount();
				final FluidStack resource = new FluidStack(fluid, amount);
				
				// 如果导出成功
				final FluidStack simulateDrain = blockHandler.drain(resource, SIMULATE);
				int fill = copyItemHandler.fill(simulateDrain, SIMULATE);
				if (fill > 0) {
					if (!player.isCreative() && itemHandler != null) {
						final FluidStack drain = blockHandler.drain(resource, EXECUTE);
						itemHandler.fill(drain, EXECUTE);
						player.setItemInHand(hand, container);
					} else {
						blockHandler.drain(resource, EXECUTE);
					}
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	protected abstract @NotNull VoxelShape getInteractionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos);
	
	@Override
	protected abstract @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context);
	
	/**
	 * 获取编码器。
	 */
	@Override
	protected abstract @NotNull MapCodec<? extends BaseEntityBlock> codec();
	
	@Override
	protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
		return RenderShape.MODEL;
	}
	
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
	
	public Supplier<BlockEntityType<? extends E>> getBlockEntityType() {
		return blockEntityType;
	}
}