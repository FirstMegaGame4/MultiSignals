package fr.firstmegagame4.multisignals.client;

import fr.firstmegagame4.multisignals.client.init.MultiSignalsBlockColors;
import fr.firstmegagame4.multisignals.client.init.MultiSignalsRenderLayers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class MultiSignalsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MultiSignalsRenderLayers.register();
        MultiSignalsBlockColors.register();
    }
}
