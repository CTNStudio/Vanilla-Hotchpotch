package com.ctn.vanilla_hotchpotch.linkage.jade;

import com.ctn.vanilla_hotchpotch.common.block.saucepan.SaucepanBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.*;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.FluidView;

import java.util.List;

public enum SaucepanComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
	INSTANCE;

	public static void renderingItems(ITooltip tooltip, NonNullList<ItemStack> items) {
		if (!items.isEmpty()) {
			int i = 0;
			for (ItemStack itemStack : items) {
				IElementHelper elements = IElementHelper.get();
				IElement icon = elements.item(itemStack, .5f).translate(new Vec2(0, -1));
				tooltip.add(icon);
				if (i == 20) {
					tooltip.append(Component.literal(""));
					i = 0;
				}
				i++;
			}
		}
	}

	/**
	 * 渲染流体信息
	 */
	public static void renderingFluid(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config, CompoundTag nbt) {
		FluidView fluidView = FluidView.readDefault(nbt.getCompound("fluidStorage"));

		if (fluidView == null) {
			return;
		}
		List<ClientViewGroup<FluidView>> groups = List.of(new ClientViewGroup<>(List.of(fluidView)));
		IElementHelper helper = IElementHelper.get();
		boolean renderGroup = groups.getFirst().shouldRenderGroup();
		ClientViewGroup.tooltip(
				tooltip, groups, renderGroup, (theTooltip, group) -> {
					if (renderGroup) {
						group.renderHeader(theTooltip);
					}
					for (var view : group.views) {
						Component text;
						IWailaConfig.HandlerDisplayStyle style = config.getEnum(JadeIds.UNIVERSAL_FLUID_STORAGE_STYLE);
						if (view.overrideText != null) {
							text = view.overrideText;
						} else if (view.fluidName == null) {
							text = IThemeHelper.get().info(view.current);
						} else {
							Component fluidName = IThemeHelper.get().info(IDisplayHelper.get().stripColor(view.fluidName));
							if (accessor.showDetails() || style != IWailaConfig.HandlerDisplayStyle.PROGRESS_BAR) {
								text = Component.translatable("jade.fluid.with_capacity", IThemeHelper.get().info(view.current), view.max);
							} else {
								text = IThemeHelper.get().info(view.current);
							}
							String key = style == IWailaConfig.HandlerDisplayStyle.PLAIN_TEXT ? "jade.fluid.text" : "jade.fluid";
							text = Component.translatable(key, fluidName, text);
						}
						switch (style) {
							case PLAIN_TEXT -> theTooltip.add(text);
							case ICON -> {
								theTooltip.add(helper.smallItem(new ItemStack(Items.BUCKET)));
								theTooltip.append(text);
							}
							case PROGRESS_BAR -> {
								ProgressStyle progressStyle = helper.progressStyle().overlay(view.overlay);
								theTooltip.add(helper.progress(view.ratio, text, progressStyle, BoxStyle.getNestedBox(), true));
							}
						}
						if (group.extraData != null && group.extraData.contains("+")) {
							int extra = group.extraData.getInt("+");
							if (extra > 0) {
								theTooltip.add(Component.translatable("jade.fluid.more_tanks", extra));
							}
						}
					}
				});
	}

	@Override
	public @Nullable IElement getIcon(BlockAccessor accessor, IPluginConfig config, IElement currentIcon) {
		return IBlockComponentProvider.super.getIcon(accessor, config, currentIcon);
	}

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		CompoundTag nbt = accessor.getServerData();
//		NonNullList<ItemStack> items = NonNullList.create();
//		ContainerHelper.loadAllItems(nbt, items, accessor.getLevel().registryAccess());

//		renderingItems(tooltip, items);
//		renderingFluid(tooltip, accessor, config, nbt);

		tooltip.add(Component.literal(String.valueOf(nbt.getInt("cookingTick"))));
		tooltip.add(Component.literal(String.valueOf(nbt.getInt("totalTick"))));
	}

	@Override
	public ResourceLocation getUid() {
		return VhPlugin.SAUCEPAN;
	}

	@Override
	public void appendServerData(CompoundTag nbt, BlockAccessor accessor) {
		SaucepanBlockEntity furnace = (SaucepanBlockEntity) accessor.getBlockEntity();
//		RegistryAccess registry = accessor.getLevel().registryAccess();
//		ContainerHelper.saveAllItems(nbt, furnace.getItems(), true, registry);
//		FluidStorageProvider.putData(accessor);
//		FluidStack fluidStack = furnace.getFluidStack();
//		nbt.put("fluidStorage", FluidView.writeDefault(
//				JadeFluidObject.of(
//						fluidStack.getFluid(),
//						fluidStack.getAmount(),
//						fluidStack.getComponentsPatch()),
//				furnace.getCapacity()));

		nbt.putInt("cookingTick", furnace.getTotalTick());
		nbt.putInt("totalTick", furnace.getCookingTick());
	}
}
