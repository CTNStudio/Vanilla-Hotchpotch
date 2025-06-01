package com.ctn.vanilla_hotchpotch.datagen;

import com.ctn.vanilla_hotchpotch.init.VhBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.ctn.vanilla_hotchpotch.VhMain.VH_ID;

public class VhTags {
	public static class Block extends BlockTagsProvider {
		public static final TagKey<net.minecraft.world.level.block.Block> SAUCEPAN = createTag("saucepan");

		public Block(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
			super(output, lookupProvider, VH_ID, existingFileHelper);
		}

		protected static TagKey<net.minecraft.world.level.block.Block> createTag(String name) {
			return BlockTags.create(ResourceLocation.fromNamespaceAndPath(VH_ID, name));
		}

		@Override
		protected void addTags(HolderLookup.@NotNull Provider provider) {
			tag(BlockTags.MINEABLE_WITH_PICKAXE).add(VhBlocks.SAUCEPAN.get());
			tag(SAUCEPAN).add(VhBlocks.SAUCEPAN.get());
		}
	}

	public static class Item extends ItemTagsProvider {
		public Item(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<net.minecraft.world.level.block.Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
			super(output, lookupProvider, blockTags, VH_ID, existingFileHelper);
		}

		protected static TagKey<net.minecraft.world.item.Item> createTag(String name) {
			return ItemTags.create(ResourceLocation.fromNamespaceAndPath(VH_ID, name));
		}

		@Override
		protected void addTags(HolderLookup.Provider provider) {

		}
	}
}
