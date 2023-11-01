package fr.firstmegagame4.multisignals.injected.world;

import fr.firstmegagame4.multisignals.block.CitrineBlock;
import fr.firstmegagame4.multisignals.block.CitrineWireBlock;
import fr.firstmegagame4.multisignals.init.MultiSignalsBlocks;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public interface CitrineView extends BlockView {

    default int getReceivedCitrineSignal(BlockPos pos) {
        int i = 0;
        for (Direction direction : Direction.values()) {
            int j = this.getBlockState(pos.offset(direction)).getCitrineSignal(this, pos.offset(direction), direction);
            if (j >= 3) {
                return 3;
            }
            if (j <= i) continue;
            i = j;
        }
        return i;
    }

    default int getStrongCitrinePower(BlockPos pos, Direction direction) {
        return this.getBlockState(pos).getStrongCitrinePower(this, pos, direction);
    }

    default int getReceivedStrongCitrinePower(BlockPos pos) {
        int i = 0;
        if ((i = Math.max(i, this.getStrongCitrinePower(pos.down(), Direction.DOWN))) >= 15) {
            return i;
        }
        if ((i = Math.max(i, this.getStrongCitrinePower(pos.up(), Direction.UP))) >= 15) {
            return i;
        }
        if ((i = Math.max(i, this.getStrongCitrinePower(pos.north(), Direction.NORTH))) >= 15) {
            return i;
        }
        if ((i = Math.max(i, this.getStrongCitrinePower(pos.south(), Direction.SOUTH))) >= 15) {
            return i;
        }
        if ((i = Math.max(i, this.getStrongCitrinePower(pos.west(), Direction.WEST))) >= 15) {
            return i;
        }
        i = Math.max(i, this.getStrongCitrinePower(pos.east(), Direction.EAST));
        return i;
    }

    default int getEmittedCitrinePower(BlockPos pos, Direction direction, boolean onlyFromGate) {
        BlockState blockState = this.getBlockState(pos);
        if (onlyFromGate) {
            return AbstractRedstoneGateBlock.isRedstoneGate(blockState) ? this.getStrongCitrinePower(pos, direction) : 0;
        }
        if (blockState.getBlock() instanceof CitrineBlock) {
            return 15;
        }
        if (blockState.isOf(MultiSignalsBlocks.CITRINE_WIRE)) {
            return blockState.get(CitrineWireBlock.POWER);
        }
        if (blockState.emitsCitrinePower()) {
            return this.getStrongCitrinePower(pos, direction);
        }
        return 0;
    }

    default boolean isEmittingCitrinePower(BlockPos pos, Direction direction) {
        return this.getEmittedCitrinePower(pos, direction) > 0;
    }

    default int getEmittedCitrinePower(BlockPos pos, Direction direction) {
        BlockState blockState = this.getBlockState(pos);
        int i = blockState.getWeakCitrinePower(this, pos, direction);
        if (blockState.isSolidBlock(this, pos)) {
            return Math.max(i, this.getReceivedStrongCitrinePower(pos));
        }
        return i;
    }

    default boolean isReceivingCitrinePower(BlockPos pos) {
        if (this.getEmittedCitrinePower(pos.down(), Direction.DOWN) > 0) {
            return true;
        }
        if (this.getEmittedCitrinePower(pos.up(), Direction.UP) > 0) {
            return true;
        }
        if (this.getEmittedCitrinePower(pos.north(), Direction.NORTH) > 0) {
            return true;
        }
        if (this.getEmittedCitrinePower(pos.south(), Direction.SOUTH) > 0) {
            return true;
        }
        if (this.getEmittedCitrinePower(pos.west(), Direction.WEST) > 0) {
            return true;
        }
        return this.getEmittedCitrinePower(pos.east(), Direction.EAST) > 0;
    }

    default int getReceivedCitrinePower(BlockPos pos) {
        int i = 0;
        for (Direction direction : Direction.values()) {
            int j = this.getEmittedCitrinePower(pos.offset(direction), direction);
            if (j >= 15) {
                return 15;
            }
            if (j <= i) continue;
            i = j;
        }
        return i;
    }
}
