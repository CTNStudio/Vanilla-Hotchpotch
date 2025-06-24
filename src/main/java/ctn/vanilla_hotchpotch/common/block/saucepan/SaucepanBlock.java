package ctn.vanilla_hotchpotch.common.block.saucepan;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

import static ctn.vanilla_hotchpotch.init.VhBlockEntityTypes.SAUCEPAN_BLOCK_ENTITY_TYPE;

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
}