package fr.firstmegagame4.multisignals.injected;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public interface CitrineImpl {

    default boolean emitsCitrinePower(BlockState state) {
        throw new AssertionError();
    }

    default int getCitrineSignal(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        throw new AssertionError();
    }

    default int getWeakCitrinePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        throw new AssertionError();
    }

    default int getStrongCitrinePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        throw new AssertionError();
    }

    interface State {

        default boolean emitsCitrinePower() {
            throw new AssertionError();
        }

        default int getCitrineSignal(BlockView world, BlockPos pos, Direction direction) {
            throw new AssertionError();
        }

        default int getWeakCitrinePower(BlockView world, BlockPos pos, Direction direction) {
            throw new AssertionError();
        }

        default int getStrongCitrinePower(BlockView world, BlockPos pos, Direction direction) {
            throw new AssertionError();
        }
    }
}
