package com.ctn.vanilla_hotchpotch;

import com.ctn.vanilla_hotchpotch.datagen.BlockState;
import com.ctn.vanilla_hotchpotch.datagen.I18ZhCn;
import com.ctn.vanilla_hotchpotch.datagen.ItemModel;
import com.ctn.vanilla_hotchpotch.datagen.VhTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

import static com.ctn.vanilla_hotchpotch.VhMain.VH_ID;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = VH_ID)
public class VhDatagenMain {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper fileHelper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		generator.addProvider(event.includeClient(), new I18ZhCn(output));
		generator.addProvider(event.includeClient(), new ItemModel(output, fileHelper));
		generator.addProvider(event.includeClient(), new BlockState(output, fileHelper));
		VhTags.Blocks blocksTags = new VhTags.Blocks(output, lookupProvider, fileHelper);
		generator.addProvider(event.includeClient(), blocksTags);
		generator.addProvider(event.includeClient(), new VhTags.Items(output, lookupProvider, blocksTags.contentsGetter(), fileHelper));

	}
}
