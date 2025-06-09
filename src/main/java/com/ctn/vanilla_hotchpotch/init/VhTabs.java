package com.ctn.vanilla_hotchpotch.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.ctn.vanilla_hotchpotch.VhMain.VH_ID;

public class VhTabs {
	public static final DeferredRegister<CreativeModeTab> MOON_TAB_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, VH_ID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BLOCK = register(
			"block", (name) ->
					createCreativeModeTab(
							name, (parameters, output) -> {
								output.accept(VhItems.SAUCEPAN.get());
							},
							VhItems.SAUCEPAN.get()::getDefaultInstance));

	private static DeferredHolder<CreativeModeTab, CreativeModeTab> register(String name,
			Function<String, CreativeModeTab.Builder> builder) {
		return MOON_TAB_REGISTER.register(name, builder.apply(name)::build);
	}

	private static CreativeModeTab.Builder createCreativeModeTab(
			String name,
			CreativeModeTab.DisplayItemsGenerator displayItemsGenerator,
			Supplier<ItemStack> icon,
			ResourceKey<CreativeModeTab> withTabsBefore) {
		return createCreativeModeTab(name, displayItemsGenerator, icon).withTabsBefore(withTabsBefore);
	}

	private static CreativeModeTab.Builder createCreativeModeTab(
			String name,
			CreativeModeTab.DisplayItemsGenerator displayItemsGenerator,
			Supplier<ItemStack> icon) {
		return createCreativeModeTab(name, displayItemsGenerator).icon(icon);
	}

	private static CreativeModeTab.Builder createCreativeModeTab(String name,
			CreativeModeTab.DisplayItemsGenerator displayItemsGenerator) {
		return CreativeModeTab.builder().title(getComponent(name)).displayItems(displayItemsGenerator);
	}

	private static @NotNull MutableComponent getComponent(String imagePath) {
		return Component.translatable("itemGroup." + VH_ID + "." + imagePath);
	}

}
