package com.ctn.vanilla_hotchpotch.init;

import com.ctn.vanilla_hotchpotch.common.block.saucepan.SaucepanBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.ctn.vanilla_hotchpotch.VhMain.VH_ID;
import static com.ctn.vanilla_hotchpotch.init.VhBlocks.SAUCEPAN;


public class VhBlockEntitys {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE_REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, VH_ID);

	private static <B extends BlockEntity> Supplier<BlockEntityType<B>> register(final String name, BlockEntityType.BlockEntitySupplier<B> blockEntity, Supplier<Block> blocks) {
		return registerBlockEntity(name, () -> BlockEntityType.Builder.of(blockEntity, blocks.get()).build(null));
	}

	private static <I extends BlockEntityType<?>> DeferredHolder<BlockEntityType<?>, I> registerBlockEntity(String name, final Supplier<? extends I> sup) {
		return BLOCK_ENTITY_TYPE_REGISTER.register(name, sup);
	}

	public static final Supplier<BlockEntityType<SaucepanBlockEntity>> SAUCEPAN_BLOCK_ENTITY_TYPE = register("saucepan_block_entity", SaucepanBlockEntity::new, SAUCEPAN::get);


}
