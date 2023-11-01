package fr.firstmegagame4.multisignals.client.init;

import fr.firstmegagame4.multisignals.init.MultiSignalsBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class MultiSignalsRenderLayers {

    public static void register() {
        BlockRenderLayerMap.INSTANCE.putBlock(MultiSignalsBlocks.CITRINE_WIRE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(MultiSignalsBlocks.SUPPORT, RenderLayer.getTranslucent());
    }
}
