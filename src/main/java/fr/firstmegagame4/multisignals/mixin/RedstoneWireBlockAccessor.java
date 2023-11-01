package fr.firstmegagame4.multisignals.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(RedstoneWireBlock.class)
public interface RedstoneWireBlockAccessor {

    @Accessor("SHAPES")
    static Map<BlockState, VoxelShape> getShapes() {
        throw new AssertionError();
    }

    @Accessor
    BlockState getDotState();

    @Invoker("getPlacementState")
    BlockState invokeGetPlacementState(BlockView world, BlockState state, BlockPos pos);

    @Invoker("isFullyConnected")
    static boolean invokeIsFullyConnected(BlockState state) {
        throw new AssertionError();
    }

    @Invoker("isNotConnected")
    static boolean invokeIsNotConnected(BlockState state) {
        throw new AssertionError();
    }

    @Invoker("addPoweredParticles")
    void invokeAddPoweredParticles(World world, Random random, BlockPos pos, Vec3d color, Direction direction, Direction direction2, float f, float g);

    @Invoker("updateForNewState")
    void invokeUpdateForNewState(World world, BlockPos pos, BlockState oldState, BlockState newState);
}
