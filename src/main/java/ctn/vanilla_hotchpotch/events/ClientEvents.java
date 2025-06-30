package ctn.vanilla_hotchpotch.events;

import ctn.vanilla_hotchpotch.capability.IPotionFluid;
import ctn.vanilla_hotchpotch.capability_provider.PotionFluidHandler;
import ctn.vanilla_hotchpotch.init.VhBlocks;
import ctn.vanilla_hotchpotch.init.VhFluidTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import static ctn.vanilla_hotchpotch.VhMain.VH_ID;

@EventBusSubscriber(modid = VH_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
	@SubscribeEvent
	public static void blockColor(RegisterColorHandlersEvent.Block event) {
		event.register(ClientEvents::getColor, VhBlocks.POTION_FLUID.get());
		
	}
	
	private static int getColor(BlockState state, BlockAndTintGetter getter, BlockPos pos, int tintIndex) {
		getColor:
		{
			if (getter == null || pos == null) {
				break getColor;
			}
			FluidState fluidState = getter.getFluidState(pos);
			if (fluidState.getFluidType() != VhFluidTypes.POTION_FLUID.get()) {
				break getColor;
			}
			IPotionFluid handler = PotionFluidHandler.create(fluidState);
			if (handler == null) {
				break getColor;
			}
			NonNullList<MobEffectInstance> effects = handler.getEffects();
			if (effects.isEmpty()) {
				break getColor;
			}
			return effects.getFirst().getEffect().value().getColor();
		}
		return -1;
	}
}