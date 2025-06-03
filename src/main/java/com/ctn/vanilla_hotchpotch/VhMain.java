package com.ctn.vanilla_hotchpotch;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

import static com.ctn.vanilla_hotchpotch.init.VhBlockEntityTypes.BLOCK_ENTITY_TYPE_REGISTER;
import static com.ctn.vanilla_hotchpotch.init.VhBlocks.BLOCK_REGISTER;
import static com.ctn.vanilla_hotchpotch.init.VhEntitys.ENTITY_TYPE_REGISTER;
import static com.ctn.vanilla_hotchpotch.init.VhItems.ITEM_REGISTER;
import static com.ctn.vanilla_hotchpotch.init.VhTabs.MOON_TAB_REGISTER;

@Mod(VhMain.VH_ID)
public class VhMain {
	public static final String VH_ID  = "vanilla_hotchpotch";
	public static final Logger LOGGER = LogUtils.getLogger();

	public VhMain(IEventBus modEventBus, ModContainer modContainer) {
		ITEM_REGISTER.register(modEventBus);
		BLOCK_REGISTER.register(modEventBus);
		ENTITY_TYPE_REGISTER.register(modEventBus);
		BLOCK_ENTITY_TYPE_REGISTER.register(modEventBus);
		MOON_TAB_REGISTER.register(modEventBus);
		NeoForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		LOGGER.info("Vanilla hotchpotch from server starting");
	}
}
