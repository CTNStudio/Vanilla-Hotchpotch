package ctn.vanilla_hotchpotch.datagen;

import ctn.vanilla_hotchpotch.init.VhBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static ctn.vanilla_hotchpotch.VhMain.VH_ID;

public class VhTags {
	public static class Blocks extends BlockTagsProvider {
		public static final TagKey<Block> SAUCEPAN = createTag("saucepan");
		
		public Blocks(PackOutput output,
				CompletableFuture<HolderLookup.Provider> lookupProvider,
				@Nullable ExistingFileHelper existingFileHelper) {
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
	
	public static class Items extends ItemTagsProvider {
		public Items(PackOutput output,
				CompletableFuture<HolderLookup.Provider> lookupProvider,
				CompletableFuture<TagLookup<Block>> blockTags,
				@Nullable ExistingFileHelper existingFileHelper) {
			super(output, lookupProvider, blockTags, VH_ID, existingFileHelper);
		}
		
		protected static TagKey<Item> createTag(String name) {
			return ItemTags.create(ResourceLocation.fromNamespaceAndPath(VH_ID, name));
		}
		
		@Override
		protected void addTags(HolderLookup.@NotNull Provider provider) {
		
		}
	}
	
	public static class Fluids extends FluidTagsProvider {
		public Fluids(PackOutput output,
				CompletableFuture<HolderLookup.Provider> provider,
				@Nullable ExistingFileHelper existingFileHelper) {
			super(output, provider, VH_ID, existingFileHelper);
		}
		
		protected static TagKey<Fluid> createTag(String name) {
			return FluidTags.create(ResourceLocation.fromNamespaceAndPath(VH_ID, name));
		}
		
		@Override
		protected void addTags(HolderLookup.@NotNull Provider provider) {
		
		}
	}
}
