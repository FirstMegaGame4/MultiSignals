package fr.firstmegagame4.multisignals.init;

import fr.firstmegagame4.multisignals.MultiSignals;
import fr.firstmegagame4.multisignals.block.CitrineBlock;
import fr.firstmegagame4.multisignals.block.CitrineWireBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class MultiSignalsBlocks {

    public static final CitrineBlock DEPLETED_CITRINE_BLOCK = new CitrineBlock(CitrineBlock.Type.DEPLETED, FabricBlockSettings.copyOf(Blocks.REDSTONE_BLOCK));
    public static final CitrineBlock CITRINE_BLOCK = new CitrineBlock(CitrineBlock.Type.NORMAL, FabricBlockSettings.copyOf(Blocks.REDSTONE_BLOCK));
    public static final CitrineBlock CONCENTRATED_CITRINE_BLOCK = new CitrineBlock(CitrineBlock.Type.CONCENTRATED, FabricBlockSettings.copyOf(Blocks.REDSTONE_BLOCK));

    public static final CitrineWireBlock CITRINE_WIRE = new CitrineWireBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_WIRE));

    public static final Block CARBONATED_TUFF_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.TUFF));

    public static final Block SUPPORT = new Block(FabricBlockSettings.create().noCollision());

    public static void register() {
        Registry.register(Registries.BLOCK, MultiSignals.createId("depleted_citrine_block"), DEPLETED_CITRINE_BLOCK);
        Registry.register(Registries.BLOCK, MultiSignals.createId("citrine_block"), CITRINE_BLOCK);
        Registry.register(Registries.BLOCK, MultiSignals.createId("concentrated_citrine_block"), CONCENTRATED_CITRINE_BLOCK);
        Registry.register(Registries.BLOCK, MultiSignals.createId("citrine_wire"), CITRINE_WIRE);
        Registry.register(Registries.BLOCK, MultiSignals.createId("carbonated_tuff_block"), CARBONATED_TUFF_BLOCK);
        Registry.register(Registries.BLOCK, MultiSignals.createId("support"), SUPPORT);
    }
}
