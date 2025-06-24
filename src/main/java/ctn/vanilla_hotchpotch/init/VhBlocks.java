package ctn.vanilla_hotchpotch.init;

import ctn.vanilla_hotchpotch.common.block.saucepan.SaucepanBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

import static ctn.vanilla_hotchpotch.VhMain.VH_ID;

public class VhBlocks {
	public static final DeferredRegister.Blocks BLOCK_REGISTER = DeferredRegister.createBlocks(VH_ID);

	public static final DeferredBlock<SaucepanBlock> SAUCEPAN = registerEntityBlock(
			"saucepan", SaucepanBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.STONE)
					.requiresCorrectToolForDrops()
					.strength(2.0F)
					.noOcclusion()
					.forceSolidOn());

	public static <B extends Block & EntityBlock> DeferredBlock<B> registerEntityBlock(String name, final Function<BlockBehaviour.Properties, B> sup) {
		return BLOCK_REGISTER.registerBlock(name, sup);
	}

	public static <B extends Block & EntityBlock> DeferredBlock<B> registerEntityBlock(String name, Function<BlockBehaviour.Properties, ? extends B> func, BlockBehaviour.Properties props) {
		return BLOCK_REGISTER.registerBlock(name, func, props);
	}

	public static <B extends Block> DeferredBlock<B> registerBlock(String name, final Supplier<? extends B> sup) {
		return BLOCK_REGISTER.register(name, sup);
	}

	public static DeferredBlock<Block> registerBlock(String name, BlockBehaviour.Properties props) {
		return BLOCK_REGISTER.registerSimpleBlock(name, props);
	}
}
