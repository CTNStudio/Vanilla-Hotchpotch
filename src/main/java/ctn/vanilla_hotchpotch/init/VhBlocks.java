package ctn.vanilla_hotchpotch.init;

import com.google.common.util.concurrent.ClosingFuture;
import ctn.vanilla_hotchpotch.common.block.PotionFluidBlock;
import ctn.vanilla_hotchpotch.common.block.saucepan.SaucepanBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static ctn.vanilla_hotchpotch.VhMain.VH_ID;

public class VhBlocks {
	public static final DeferredRegister.Blocks BLOCK_REGISTER = DeferredRegister.createBlocks(VH_ID);
	
	public static final DeferredBlock<SaucepanBlock> SAUCEPAN = registerEntityBlock("saucepan",
			SaucepanBlock::new, BlockBehaviour.Properties.of()
			.mapColor(MapColor.STONE)
			.requiresCorrectToolForDrops()
			.strength(2.0F)
			.noOcclusion()
			.forceSolidOn());
	
	public static final DeferredBlock<Block> POTION_FLUID = registerFluidBlock("potion_fluid",
			PotionFluidBlock::new, VhFluids.POTION_FLUID, BlockBehaviour.Properties.of()
			.mapColor(MapColor.WATER)
			.replaceable()
			.noCollission()
			.strength(100.0F)
			.pushReaction(PushReaction.DESTROY)
			.noLootTable()
			.liquid()
			.sound(SoundType.EMPTY));
	
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
	
	public static DeferredBlock<Block> registerBlock(String name, Block block) {
		return BLOCK_REGISTER.register(name, () -> block);
	}
	
	public static DeferredBlock<Block> registerFluidBlock(String name, BiFunction<FlowingFluid, BlockBehaviour.Properties, LiquidBlock> block, Supplier<FlowingFluid> fluid, BlockBehaviour.Properties props) {
		return BLOCK_REGISTER.registerBlock(name, (properties) -> block.apply(fluid.get(), props), props);
	}
}
