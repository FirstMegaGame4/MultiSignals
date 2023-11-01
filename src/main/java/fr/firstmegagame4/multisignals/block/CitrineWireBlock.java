package fr.firstmegagame4.multisignals.block;

import fr.firstmegagame4.multisignals.init.MultiSignalsBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CitrineWireBlock extends AbstractBasedWireBlock {

    public static final IntProperty QUATERNARY = AbstractBasedWireBlock.createSignalProperty("quaternary", 3);
    public static final IntProperty POWER = AbstractBasedWireBlock.createPowerProperty("quat_power");

    public CitrineWireBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public int getMinSignalValue() {
        return 0;
    }

    @Override
    public int getMaxSignalValue() {
        return 3;
    }

    @Override
    public IntProperty getSignalProperty() {
        return CitrineWireBlock.QUATERNARY;
    }

    @Override
    public IntProperty getPowerProperty() {
        return CitrineWireBlock.POWER;
    }

    @Override
    public boolean emitsTypedPowerOfState(BlockState state) {
        return state.emitsCitrinePower();
    }

    @Override
    public int getTypedSignalOfState(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.getCitrineSignal(world, pos, direction);
    }

    @Override
    public int getWeakTypedPowerOfState(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.getWeakCitrinePower(world, pos, direction);
    }

    @Override
    public int getStrongTypedPowerOfState(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.getStrongCitrinePower(world, pos, direction);
    }

    @Override
    public boolean connectsToState(BlockState state, Direction direction) {
        if (state.isOf(MultiSignalsBlocks.CITRINE_WIRE)) {
            return true;
        }
        return state.emitsCitrinePower() && direction != null;
    }

    @Override
    public int getReceivedTypedSignalOfWorld(World world, BlockPos pos) {
        return world.getReceivedCitrineSignal(pos);
    }

    @Override
    public int getReceivedTypedPowerOfWorld(World world, BlockPos pos) {
        return world.getReceivedCitrinePower(pos);
    }

    @Override
    public boolean emitsCitrinePower(BlockState state) {
        return this.emitsTypedPower(state);
    }

    @Override
    public int getCitrineSignal(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(CitrineWireBlock.QUATERNARY);
    }

    @Override
    public int getWeakCitrinePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return this.getWeakTypedPower(state, world, pos, direction);
    }

    @Override
    public int getStrongCitrinePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return this.getStrongTypedPower(state, world, pos, direction);
    }
}
