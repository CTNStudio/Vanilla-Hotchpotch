package com.ctn.vanilla_hotchpotch.registrar;

import com.ctn.vanilla_hotchpotch.init.VhBlockEntitys;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import static com.ctn.vanilla_hotchpotch.VhMain.VH_ID;

@EventBusSubscriber(modid = VH_ID, bus = EventBusSubscriber.Bus.MOD)
public class CapabilityRegistrationHandler {
	@SubscribeEvent
	public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(
				Capabilities.FluidHandler.BLOCK,
				VhBlockEntitys.SAUCEPAN_BLOCK_ENTITY_TYPE.get(),
				(saucepanBlockEntity, direction) -> saucepanBlockEntity);
	}
}