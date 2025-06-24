package ctn.vanilla_hotchpotch.linkage.jade;

import ctn.vanilla_hotchpotch.common.block.saucepan.SaucepanBlock;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

import static ctn.vanilla_hotchpotch.VhMain.VH_ID;

@WailaPlugin
public class VhPlugin implements IWailaPlugin {
	public static final ResourceLocation SAUCEPAN = getResourceLocation("saucepan");

	private static @NotNull ResourceLocation getResourceLocation(String name) {
		return ResourceLocation.fromNamespaceAndPath(VH_ID, name);
	}

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.registerBlockDataProvider(SaucepanComponentProvider.INSTANCE, SaucepanBlock.class);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerBlockComponent(SaucepanComponentProvider.INSTANCE, SaucepanBlock.class);
	}
}