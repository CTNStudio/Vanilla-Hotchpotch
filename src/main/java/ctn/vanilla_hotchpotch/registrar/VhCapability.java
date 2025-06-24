package ctn.vanilla_hotchpotch.registrar;

import ctn.vanilla_hotchpotch.init.VhBlockEntityTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import static ctn.vanilla_hotchpotch.VhMain.VH_ID;

@EventBusSubscriber(modid = VH_ID, bus = EventBusSubscriber.Bus.MOD)
public class VhCapability {
//	public static final Capability<IPotionFluidBlock> POTION_FLUID_BLOCK = CapabilityManage.get(new CapabilityToken<>() {});
	
	@SubscribeEvent
	public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(
				Capabilities.FluidHandler.BLOCK,
				VhBlockEntityTypes.SAUCEPAN_BLOCK_ENTITY_TYPE.get(),
				(be, context) -> be.getFluidTankHandler());
		event.registerBlockEntity(
				Capabilities.ItemHandler.BLOCK,
				VhBlockEntityTypes.SAUCEPAN_BLOCK_ENTITY_TYPE.get(),
				(be, context) -> be.getItemHandler());
	}
}