package fr.firstmegagame4.multisignals.optimization;

import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.state.property.IntProperty;
import net.minecraft.world.BlockView;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

/** Modified class from the <a href="https://github.com/CaffeineMC/lithium-fabric">Lithium Source Code</a>
 * Since Lithium optimizes only the vanilla RedstoneWire system, we have to apply it to our derived systems too
 */
public interface OptimizedWireSystem {

    int MIN_POWER_VALUE = 0;
    int MAX_POWER_VALUE = 15;
    int MAX_POWER_VALUE_FROM_WIRE = MAX_POWER_VALUE - 1;

    RedstoneWireBlock getObject();

    int getMinSignalValue();
    int getMaxSignalValue();

    default int getMaxSignalValueFromWire() {
        return this.getMaxSignalValue();
    }

    IntProperty getSignalProperty();

    IntProperty getPowerProperty();

    boolean emitsTypedPowerOfState(BlockState state);

    int getTypedSignalOfState(BlockState state, BlockView world, BlockPos pos, Direction direction);
    int getWeakTypedPowerOfState(BlockState state, BlockView world, BlockPos pos, Direction direction);
    int getStrongTypedPowerOfState(BlockState state, BlockView world, BlockPos pos, Direction direction);

    /**
     * Calculate the typed signal a wire at the given location receives from the
     * surrounding blocks.
     */
    default int getReceivedTypedSignal(World world, BlockPos pos) {
        WorldChunk chunk = world.getWorldChunk(pos);
        int signal = this.getMinSignalValue();

        for (Direction dir : Direction.Type.VERTICAL) {
            BlockPos side = pos.offset(dir);
            BlockState neighbor = chunk.getBlockState(side);

            // Wires do not accept signal from other wires directly above or below them,
            // so those can be ignored. Similarly, if there is air directly above or
            // below a wire, it does not receive any signal from that direction.
            if (!neighbor.isAir() && !neighbor.isOf(this.getObject())) {
                signal = Math.max(signal, this.getTypedSignalFromVertical(world, side, neighbor, dir));

                if (signal >= this.getMaxSignalValue()) {
                    return this.getMaxSignalValue();
                }
            }
        }

        // In vanilla this check is done up to 4 times.
        BlockPos up = pos.up();
        boolean checkWiresAbove = !chunk.getBlockState(up).isSolidBlock(world, up);

        for (Direction dir : Direction.Type.HORIZONTAL) {
            signal = Math.max(signal, this.getTypedSignalFromSide(world, pos.offset(dir), dir, checkWiresAbove));

            if (signal >= this.getMaxSignalValue()) {
                return this.getMaxSignalValue();
            }
        }

        return signal;
    }

    /**
     * Calculate the typed signal an abstract based wire receives from a block above or below it.
     * We do these positions separately because there are no wire connections
     * vertically. This simplifies the calculations a little.
     */
    default int getTypedSignalFromVertical(World world, BlockPos pos, BlockState state, Direction toDir) {
        int signal = this.getTypedSignalOfState(state, world, pos, toDir);

        if (signal >= this.getMaxSignalValue()) {
            return this.getMaxSignalValue();
        }

        if (state.isSolidBlock(world, pos)) {
            return Math.max(signal, this.getStrongTypedSignalTo(world, pos, toDir.getOpposite()));
        }

        return signal;
    }

    /**
     * Calculate the typed signal an abstract based wire receives from blocks next to it.
     */
    default int getTypedSignalFromSide(World world, BlockPos pos, Direction toDir, boolean checkWiresAbove) {
        WorldChunk chunk = world.getWorldChunk(pos);
        BlockState state = chunk.getBlockState(pos);

        if (state.isOf(this.getObject())) {
            return state.get(this.getSignalProperty());
        }

        int signal = this.getTypedSignalOfState(state, world, pos, toDir);

        if (signal >= this.getMaxSignalValue()) {
            return this.getMaxSignalValue();
        }

        if (state.isSolidBlock(world, pos)) {
            signal = Math.max(signal, this.getStrongTypedSignalTo(world, pos, toDir.getOpposite()));

            if (signal >= this.getMaxSignalValue()) {
                return this.getMaxSignalValue();
            }

            if (checkWiresAbove && signal < this.getMaxSignalValueFromWire()) {
                BlockPos up = pos.up();
                BlockState aboveState = chunk.getBlockState(up);

                if (aboveState.isOf(this.getObject())) {
                    signal = Math.max(signal, aboveState.get(this.getSignalProperty()));
                }
            }
        } else if (signal < this.getMaxSignalValueFromWire()) {
            BlockPos down = pos.down();
            BlockState belowState = chunk.getBlockState(down);

            if (belowState.isOf(this.getObject())) {
                signal = Math.max(signal, belowState.get(this.getSignalProperty()));
            }
        }

        return signal;
    }

    /**
     * Calculate the typed signal a block receives from the surrounding blocks.
     */
    default int getStrongTypedSignalTo(World world, BlockPos pos, Direction ignore) {
        int signal = this.getMinSignalValue();

        for (Direction dir : Direction.values()) {
            if (dir != ignore) {
                BlockPos side = pos.offset(dir);
                BlockState neighbor = world.getBlockState(side);

                if (!neighbor.isAir() && !neighbor.isOf(this.getObject())) {
                    signal = Math.max(signal, this.getTypedSignalOfState(neighbor, world, side, dir));

                    if (signal >= this.getMaxSignalValue()) {
                        return this.getMaxSignalValue();
                    }
                }
            }
        }

        return signal;
    }

    /**
     * Calculate the styled power a wire at the given location receives from the
     * surrounding blocks.
     */
    default int getReceivedTypedPower(World world, BlockPos pos) {
        WorldChunk chunk = world.getWorldChunk(pos);
        int power = MIN_POWER_VALUE;

        for (Direction dir : Direction.Type.VERTICAL) {
            BlockPos side = pos.offset(dir);
            BlockState neighbor = chunk.getBlockState(side);

            // Wires do not accept power from other wires directly above or below them,
            // so those can be ignored. Similarly, if there is air directly above or
            // below a wire, it does not receive any power from that direction.
            if (!neighbor.isAir() && !neighbor.isOf(this.getObject())) {
                power = Math.max(power, this.getTypedPowerFromVertical(world, side, neighbor, dir));

                if (power >= MAX_POWER_VALUE) {
                    return MAX_POWER_VALUE;
                }
            }
        }

        // In vanilla this check is done up to 4 times.
        BlockPos up = pos.up();
        boolean checkWiresAbove = !chunk.getBlockState(up).isSolidBlock(world, up);

        for (Direction dir : Direction.Type.HORIZONTAL) {
            power = Math.max(power, this.getTypedPowerFromSide(world, pos.offset(dir), dir, checkWiresAbove));

            if (power >= MAX_POWER_VALUE) {
                return MAX_POWER_VALUE;
            }
        }

        return power;
    }

    /**
     * Calculate the styled power a wire receives from a block above or below it.
     * We do these positions separately because there are no wire connections
     * vertically. This simplifies the calculations a little.
     */
    default int getTypedPowerFromVertical(World world, BlockPos pos, BlockState state, Direction toDir) {
        int power = this.getWeakTypedPowerOfState(state, world, pos, toDir);

        if (power >= MAX_POWER_VALUE) {
            return MAX_POWER_VALUE;
        }

        if (state.isSolidBlock(world, pos)) {
            return Math.max(power, this.getStrongTypedPowerTo(world, pos, toDir.getOpposite()));
        }

        return power;
    }

    /**
     * Calculate the styled power a wire receives from blocks next to it.
     */
    default int getTypedPowerFromSide(World world, BlockPos pos, Direction toDir, boolean checkWiresAbove) {
        WorldChunk chunk = world.getWorldChunk(pos);
        BlockState state = chunk.getBlockState(pos);

        if (state.isOf(this.getObject())) {
            return state.get(this.getPowerProperty()) - 1;
        }

        int power = this.getWeakTypedPowerOfState(state, world, pos, toDir);

        if (power >= MAX_POWER_VALUE) {
            return MAX_POWER_VALUE;
        }

        if (state.isSolidBlock(world, pos)) {
            power = Math.max(power, this.getStrongTypedPowerTo(world, pos, toDir.getOpposite()));

            if (power >= MAX_POWER_VALUE) {
                return MAX_POWER_VALUE;
            }

            if (checkWiresAbove && power < MAX_POWER_VALUE_FROM_WIRE) {
                BlockPos up = pos.up();
                BlockState aboveState = chunk.getBlockState(up);

                if (aboveState.isOf(this.getObject())) {
                    power = Math.max(power, aboveState.get(this.getPowerProperty()) - 1);
                }
            }
        } else if (power < MAX_POWER_VALUE_FROM_WIRE) {
            BlockPos down = pos.down();
            BlockState belowState = chunk.getBlockState(down);

            if (belowState.isOf(this.getObject())) {
                power = Math.max(power, belowState.get(this.getPowerProperty()) - 1);
            }
        }

        return power;
    }

    /**
     * Calculate the strong power a block receives from the surrounding blocks.
     */
    default int getStrongTypedPowerTo(World world, BlockPos pos, Direction ignore) {
        int power = MIN_POWER_VALUE;

        for (Direction dir : Direction.values()) {
            if (dir != ignore) {
                BlockPos side = pos.offset(dir);
                BlockState neighbor = world.getBlockState(side);

                if (!neighbor.isAir() && !neighbor.isOf(this.getObject())) {
                    power = Math.max(power, this.getStrongTypedPowerOfState(neighbor, world, side, dir));

                    if (power >= MAX_POWER_VALUE) {
                        return MAX_POWER_VALUE;
                    }
                }
            }
        }

        return power;
    }
}
