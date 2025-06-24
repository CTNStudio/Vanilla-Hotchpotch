package ctn.vanilla_hotchpotch.datagen;

import ctn.vanilla_hotchpotch.init.VhBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static ctn.vanilla_hotchpotch.VhMain.VH_ID;

public class ItemModel extends ItemModelProvider {
	public ItemModel(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, VH_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		simpleBlockItem(VhBlocks.SAUCEPAN.get());
	}
}
