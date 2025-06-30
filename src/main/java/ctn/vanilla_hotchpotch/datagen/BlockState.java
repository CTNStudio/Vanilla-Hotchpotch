package ctn.vanilla_hotchpotch.datagen;

import ctn.vanilla_hotchpotch.init.VhBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import static ctn.vanilla_hotchpotch.VhMain.VH_ID;

public class BlockState extends BlockStateProvider {
	public BlockState(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, VH_ID, exFileHelper);
	}
	
	@Override
	protected void registerStatesAndModels() {
		horizontalBlock(VhBlocks.SAUCEPAN.get(), getModelFile("block/saucepan"));
	}
	
	private static ModelFile.@NotNull UncheckedModelFile getModelFile(String name) {
		return new ModelFile.UncheckedModelFile(getLocation(name));
	}
	
	private static ResourceLocation getLocation(String name) {
		return ResourceLocation.fromNamespaceAndPath(VH_ID, name);
	}
}