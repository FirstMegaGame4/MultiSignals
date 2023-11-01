package fr.firstmegagame4.multisignals.mixin;

import fr.firstmegagame4.multisignals.injected.CitrineImpl;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin implements CitrineImpl {

    @Override
    public boolean emitsCitrinePower(BlockState state) {
        return false;
    }

    @Override
    public int getCitrineSignal(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return 0;
    }

    @Override
    public int getWeakCitrinePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return 0;
    }

    @Override
    public int getStrongCitrinePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return 0;
    }

    @Mixin(AbstractBlock.AbstractBlockState.class)
    public static abstract class AbstractBlockStateMixin implements CitrineImpl.State {

        @Shadow
        public abstract Block getBlock();

        @Shadow
        protected abstract BlockState asBlockState();

        @Override
        public boolean emitsCitrinePower() {
            return this.getBlock().emitsCitrinePower(this.asBlockState());
        }

        @Override
        public int getCitrineSignal(BlockView world, BlockPos pos, Direction direction) {
            return this.getBlock().getCitrineSignal(this.asBlockState(), world, pos, direction);
        }

        @Override
        public int getWeakCitrinePower(BlockView world, BlockPos pos, Direction direction) {
            return this.getBlock().getWeakCitrinePower(this.asBlockState(), world, pos, direction);
        }

        @Override
        public int getStrongCitrinePower(BlockView world, BlockPos pos, Direction direction) {
            return this.getBlock().getStrongCitrinePower(this.asBlockState(), world, pos, direction);
        }
    }
}
