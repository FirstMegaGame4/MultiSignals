package fr.firstmegagame4.multisignals.init;

import fr.firstmegagame4.multisignals.MultiSignals;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class MultiSignalsItems {

    public static final BlockItem DEPLETED_CITRINE_BLOCK = new BlockItem(MultiSignalsBlocks.DEPLETED_CITRINE_BLOCK, new FabricItemSettings());
    public static final BlockItem CITRINE_BLOCK = new BlockItem(MultiSignalsBlocks.CITRINE_BLOCK, new FabricItemSettings());
    public static final BlockItem CONCENTRATED_CITRINE_BLOCK = new BlockItem(MultiSignalsBlocks.CONCENTRATED_CITRINE_BLOCK, new FabricItemSettings());

    public static final Item CITRINE = new Item(new FabricItemSettings());

    public static final AliasedBlockItem CITRINE_DUST = new AliasedBlockItem(MultiSignalsBlocks.CITRINE_WIRE, new FabricItemSettings());

    public static final Item CARBONATED_TUFF_BLOCK = new BlockItem(MultiSignalsBlocks.CARBONATED_TUFF_BLOCK, new FabricItemSettings());

    public static final Item CARBONATED_TUFF = new Item(new FabricItemSettings());

    public static final BlockItem SUPPORT = new BlockItem(MultiSignalsBlocks.SUPPORT, new FabricItemSettings());

    public static void register() {
        Registry.register(Registries.ITEM, MultiSignals.createId("depleted_citrine_block"), DEPLETED_CITRINE_BLOCK);
        Registry.register(Registries.ITEM, MultiSignals.createId("citrine_block"), CITRINE_BLOCK);
        Registry.register(Registries.ITEM, MultiSignals.createId("concentrated_citrine_block"), CONCENTRATED_CITRINE_BLOCK);
        Registry.register(Registries.ITEM, MultiSignals.createId("citrine"), CITRINE);
        Registry.register(Registries.ITEM, MultiSignals.createId("citrine_dust"), CITRINE_DUST);
        Registry.register(Registries.ITEM, MultiSignals.createId("carbonated_tuff_block"), CARBONATED_TUFF_BLOCK);
        Registry.register(Registries.ITEM, MultiSignals.createId("carbonated_tuff"), CARBONATED_TUFF);
        Registry.register(Registries.ITEM, MultiSignals.createId("support"), SUPPORT);
    }
}
