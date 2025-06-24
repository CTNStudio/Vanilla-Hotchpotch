package ctn.vanilla_hotchpotch.common.block.saucepan;

import ctn.vanilla_hotchpotch.api.NonEmptyItemList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import static ctn.vanilla_hotchpotch.init.VhBlockEntityTypes.SAUCEPAN_BLOCK_ENTITY_TYPE;

/**
 * 炖锅方块实体类
 * 用于处理炖锅中的物品存储、烹饪状态以及数据同步
 */
public class SaucepanBlockEntity extends RandomizableContainerBlockEntity {
	private final ItemStackHandler itemHandler;
	// 存储炖锅中的物品列表
	private final NonEmptyItemList items;
	// 液体容器
	private final FluidTank        fluidTankHandler;
	// 烹饪进行的刻数
	private       int              cookingTick = 0;
	// 烹饪的总刻数
	private       int              totalTick   = 0;

	/**
	 * 构造函数
	 * 初始化炖锅方块实体
	 *
	 * @param pos        方块的位置
	 * @param blockState 方块的状态
	 */
	public SaucepanBlockEntity(BlockPos pos, BlockState blockState, int capacity) {
		super(SAUCEPAN_BLOCK_ENTITY_TYPE.get(), pos, blockState);
		fluidTankHandler = new FluidTank(capacity);
		items = NonEmptyItemList.create();
		items.add(ItemStack.EMPTY);
		itemHandler = new ItemStackHandler(items);
	}

	public SaucepanBlockEntity(BlockPos pos, BlockState blockState) {
		this(pos, blockState, 1000);
	}

	/**
	 * 执行炖锅的刻数更新
	 *
	 * @param level       世界
	 * @param pos         位置
	 * @param state       方块状态
	 * @param blockEntity 方块实体
	 */
	public static void tick(Level level, BlockPos pos, BlockState state, SaucepanBlockEntity blockEntity) {}

	public static SaucepanBlockEntity getBlockEntity(BlockGetter level, BlockPos pos) {
		return level.getBlockEntity(pos, SAUCEPAN_BLOCK_ENTITY_TYPE.get()).orElse(null);
	}
//
//	@Override
//	public @NotNull BlockState getBlockState() {
//		return super.getBlockState().setValue();
//	}

	/**
	 * 从NBT标签中加载额外数据
	 */
	@Override
	public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider registries) {
		super.loadAdditional(nbt, registries);
		fluidTankHandler.readFromNBT(registries, nbt);
		cookingTick = nbt.getInt("cookingTick");
		totalTick   = nbt.getInt("totalTick");
		if (!this.tryLoadLootTable(nbt)) {
			NonEmptyItemList.loadAllItems(nbt, items, registries);
		}
	}

	/**
	 * 将额外数据保存到NBT标签中
	 *
	 * @param nbt        NBT标签
	 * @param registries 标签提供者
	 */
	@Override
	public void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider registries) {
		super.saveAdditional(nbt, registries);
		fluidTankHandler.writeToNBT(registries, nbt);
		nbt.putInt("cookingTick", getCookingTick());
		nbt.putInt("totalTick", getTotalTick());
		if (!this.trySaveLootTable(nbt)) {
			ContainerHelper.saveAllItems(nbt, items, registries);
		}
	}

	@Override
	protected @NotNull Component getDefaultName() {
		return null;
	}

	/**
	 * 获取更新数据包
	 *
	 * @return 更新数据包
	 */
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	/**
	 * 处理接收到的数据包
	 *
	 * @param connection 连接
	 * @param packet     数据包
	 * @param registries 标签提供者
	 */
	@Override
	public void onDataPacket(@NotNull Connection connection, @NotNull ClientboundBlockEntityDataPacket packet, HolderLookup.@NotNull Provider registries) {
		super.onDataPacket(connection, packet, registries);
	}

	/**
	 * 清空炖锅中的内容物
	 */
	@Override
	public void clearContent() {
		super.clearContent();
		setChanged();
	}

	@Override
	public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player) {
		return null;
	}

	/**
	 * 创建容器菜单
	 */
	@Override
	protected @NotNull AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory) {
		return null;
	}

	/**
	 * 获取更新标签
	 *
	 * @param registries 标签提供者
	 * @return 更新标签
	 */
	@Override
	public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
		return saveWithoutMetadata(registries);
	}

	/**
	 * 处理更新标签
	 *
	 * @param nbt        更新标签
	 * @param registries 标签提供者
	 */
	@Override
	public void handleUpdateTag(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider registries) {
		super.handleUpdateTag(nbt, registries);
		loadAdditional(nbt, registries);
	}

	/**
	 * 获取物品列表
	 *
	 * @return 物品列表
	 */
	@Override
	public @NotNull NonNullList<ItemStack> getItems() {
		return this.items;
	}

	/**
	 * 设置物品列表
	 *
	 * @param stacks 新的物品列表
	 */
	@Override
	protected void setItems(NonNullList<ItemStack> stacks) {
		this.items.clear();
		for (ItemStack itemStack : stacks) {
			if (itemStack.isEmpty()) {
				continue;
			}
			addItem(itemStack);
		}
		setChanged();
	}

	@Override
	public void setItem(int index, @NotNull ItemStack stack) {
		if (items.isEmpty()) {
			addItem(stack);
			return;
		}
		super.setItem(index, stack);
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	public int getCookingTick() {
		return this.cookingTick;
	}

	public int getTotalTick() {
		return this.totalTick;
	}

	public ItemStack addItem(ItemStack itemStack) {
		this.items.add(itemStack.copyWithCount(1));
		itemStack.shrink(1);
		setChanged();
		return itemStack;
	}

	@Override
	public int getContainerSize() {
		return items.size();
	}

	@Override
	public @NotNull ItemStack removeItem(int index, int count) {
		if (items.size() - 1 < index) {
			return ItemStack.EMPTY;
		}
		return super.removeItem(index, count);
	}

	@Override
	public void setChanged() {
		super.setChanged();
		items.update();
	}

	public void updateFluid() {
	}

	public IItemHandler getItemHandler() {
		return itemHandler;
	}

	public IFluidHandler getFluidTankHandler() {
		return fluidTankHandler;
	}
}
