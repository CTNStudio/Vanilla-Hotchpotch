package com.ctn.vanilla_hotchpotch.api;

import com.google.common.collect.Lists;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class NonEmptyItemList extends NonNullList<ItemStack> {
	protected NonEmptyItemList(List<ItemStack> list, @Nullable ItemStack defaultValue) {
		super(list, defaultValue);
	}

	/** 创建标准集合 */
	public static NonEmptyItemList create() {
		return new NonEmptyItemList(Lists.newArrayList(), null);
	}

	/** 创建指定容量的集合 */
	public static NonEmptyItemList createFixedCapacity(int initialCapacity) {
		return new NonEmptyItemList(Lists.newArrayListWithCapacity(initialCapacity), null);
	}

	/** 创建指定数量的集合并初始 */
	public static NonEmptyItemList createFixedCapacity(int size, ItemStack defaultValue) {
		Objects.requireNonNull(defaultValue);
		ItemStack[] aobject = new ItemStack[size];
		Arrays.fill(aobject, defaultValue);
		return new NonEmptyItemList(Arrays.asList(aobject), defaultValue);
	}


	public static NonEmptyItemList create(ItemStack defaultValue, ItemStack... elements) {
		return new NonEmptyItemList(Arrays.asList(elements), defaultValue);
	}

	public static void loadAllItems(CompoundTag tag, NonNullList<ItemStack> items, HolderLookup.Provider levelRegistry) {
		ListTag listtag = tag.getList("Items", 10);

		for (int i = 0; i < listtag.size(); i++) {
			CompoundTag compoundtag = listtag.getCompound(i);
			if (items.isEmpty()) {
				items.add(ItemStack.EMPTY);
			}
			items.add(ItemStack.parse(levelRegistry, compoundtag).orElse(ItemStack.EMPTY));
		}
	}

	@Override
	public @NotNull ItemStack set(int index, @NotNull ItemStack value) {
		if (index < 0) {
			index = 0;
		}
		ItemStack itemStack = super.set(index, value);
		add(0, ItemStack.EMPTY);
		update();
		return itemStack;
	}

	/** 更新 */
	public void update() {
		if (!get(0).isEmpty()) {
			add(0, ItemStack.EMPTY);
		}
		for (int i = size() - 1; i >= 1; i--) {
			if (get(i).isEmpty()) {
				remove(i);
			}
		}
	}

	@Override
	public void add(int index, ItemStack value) {
		if (index < 0) {
			index = 0;
		}
		super.add(index, value);
		update();
	}
}