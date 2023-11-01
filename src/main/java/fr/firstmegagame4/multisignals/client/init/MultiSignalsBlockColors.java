package fr.firstmegagame4.multisignals.client.init;

import fr.firstmegagame4.multisignals.SignalColors;
import fr.firstmegagame4.multisignals.block.CitrineWireBlock;
import fr.firstmegagame4.multisignals.init.MultiSignalsBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

@Environment(EnvType.CLIENT)
public class MultiSignalsBlockColors {

    public static void register() {
        ColorProviderRegistry.BLOCK.register(
            ((state, world, pos, tintIndex) -> SignalColors.getWireColor(state.get(CitrineWireBlock.QUATERNARY), state.get(CitrineWireBlock.POWER))),
            MultiSignalsBlocks.CITRINE_WIRE
        );
    }
}
