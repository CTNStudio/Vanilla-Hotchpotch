package ctn.vanilla_hotchpotch.init;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

import static ctn.vanilla_hotchpotch.VhMain.VH_ID;
import static net.minecraft.world.item.Items.BUCKET;

public class VhItems {
	public static final DeferredRegister.Items ITEM_REGISTER = DeferredRegister.createItems(VH_ID);
	
	public static final DeferredItem<BlockItem> SAUCEPAN = createBlockItem("saucepan", VhBlocks.SAUCEPAN, new Item.Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
	
	public static final DeferredItem<Item> POTION_BUCKET = createBucketItem("potion_bucket",
			VhFluids.POTION_FLUID, new Item.Properties()
					.craftRemainder(BUCKET)
					.stacksTo(1));
	
	private static DeferredItem<BlockItem> createBlockItem(String name, Supplier<? extends Block> block, Item.Properties properties) {
		return ITEM_REGISTER.registerSimpleBlockItem(name, block, properties);
	}
	
	private static DeferredItem<Item> createItem(String name, Function<Item.Properties, ? extends Item> item, Item.Properties properties) {
		return ITEM_REGISTER.registerItem(name, item, properties);
	}
	
	private static DeferredItem<Item> createBucketItem(String name, Supplier<? extends Fluid> fluid, Item.Properties properties) {
		return ITEM_REGISTER.registerItem(name, (p) -> new BucketItem(fluid.get(), properties));
	}
}
