package fr.firstmegagame4.multisignals.mixin;

import com.google.common.collect.Sets;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import fr.firstmegagame4.multisignals.block.AbstractBasedWireBlock;
import fr.firstmegagame4.multisignals.init.MultiSignalsBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;

@Mixin(RedstoneWireBlock.class)
public abstract class RedstoneWireBlockMixin extends BlockMixin {

    @WrapOperation(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                opcode = Opcodes.GETSTATIC,
                target = "Lnet/minecraft/block/RedstoneWireBlock;POWER:Lnet/minecraft/state/property/IntProperty;"
            )
        )
    )
    private Object wrapDefaultStates(BlockState blockState, Property<Integer> vanillaPowerProperty, Comparable<Integer> vanillaPower, Operation<Object> original) {
        return this.getObject() instanceof AbstractBasedWireBlock ? blockState : original.call(blockState, vanillaPowerProperty, vanillaPower);
    }

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/property/Property;)Ljava/lang/Comparable;"))
    private Comparable<Integer> wrapConstructorPowerGetter(BlockState blockState, Property<Integer> vanillaPowerProperty, Operation<Comparable<Integer>> original) {
        return this.getObject() instanceof AbstractBasedWireBlock based ? blockState.get(based.getPowerProperty()) : original.call(blockState, vanillaPowerProperty);
    }

    @WrapOperation(method = "getPlacementState(Lnet/minecraft/world/BlockView;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/property/Property;)Ljava/lang/Comparable;", ordinal = 0))
    private Comparable<Integer> wrapGetPlacementStatePowerGetter(BlockState blockState, Property<Integer> vanillaPowerProperty, Operation<Comparable<Integer>> original) {
        return this.getObject() instanceof AbstractBasedWireBlock based ? blockState.get(based.getPowerProperty()) : original.call(blockState, vanillaPowerProperty);
    }

    @WrapOperation(method = "getPlacementState(Lnet/minecraft/world/BlockView;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;", ordinal = 0))
    private Object wrapGetPlacementStatePowerSetter(BlockState blockState, Property<Integer> vanillaPowerProperty, Comparable<Integer> vanillaPower, Operation<Object> original, @Local(ordinal = 0) BlockState state) {
        if (this.getObject() instanceof AbstractBasedWireBlock based) {
            return blockState.with(based.getPowerProperty(), state.get(based.getPowerProperty()));
        }
        else {
            return original.call(blockState, vanillaPowerProperty, vanillaPower);
        }
    }

    @WrapOperation(method = "getStateForNeighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/property/Property;)Ljava/lang/Comparable;", ordinal = 1))
    private Comparable<Integer> wrapGetStateForNeighborUpdatePowerGetter(BlockState blockState, Property<Integer> vanillaPowerProperty, Operation<Comparable<Integer>> original) {
        return this.getObject() instanceof AbstractBasedWireBlock based ? blockState.get(based.getPowerProperty()) : original.call(blockState, vanillaPowerProperty);
    }

    @WrapOperation(method = "getStateForNeighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;", ordinal = 1))
    private Object wrapGetStateForNeighborUpdatePowerSetter(BlockState blockState, Property<Integer> vanillaPowerProperty, Comparable<Integer> vanillaPower, Operation<Object> original,  @Local(ordinal = 0) BlockState state) {
        if (this.getObject() instanceof AbstractBasedWireBlock based) {
            return blockState.with(based.getPowerProperty(), state.get(based.getPowerProperty()));
        }
        else {
            return original.call(blockState, vanillaPowerProperty, vanillaPower);
        }
    }

    @ModifyExpressionValue(method = "getRenderConnectionType(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Z)Lnet/minecraft/block/enums/WireConnection;", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/RedstoneWireBlock;connectsTo(Lnet/minecraft/block/BlockState;)Z", ordinal = 0))
    private boolean firstRenderConnectionTypeModification(boolean original, BlockView world, BlockPos pos, Direction direction) {
        return this.isConnected(world.getBlockState(pos.offset(direction).up()), original);
    }

    @ModifyExpressionValue(method = "getRenderConnectionType(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Z)Lnet/minecraft/block/enums/WireConnection;", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/RedstoneWireBlock;connectsTo(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;)Z"))
    private boolean secondRenderConnectionTypeModification(boolean original, BlockView world, BlockPos pos, Direction direction) {
        return this.isConnected(world.getBlockState(pos.offset(direction)), direction, original);
    }

    @ModifyExpressionValue(method = "getRenderConnectionType(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Z)Lnet/minecraft/block/enums/WireConnection;", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/RedstoneWireBlock;connectsTo(Lnet/minecraft/block/BlockState;)Z", ordinal = 1))
    private boolean thirdRenderConnectionTypeModification(boolean original, BlockView world, BlockPos pos, Direction direction) {
        return this.isConnected(world.getBlockState(pos.offset(direction).down()), original);
    }

    @Unique
    private boolean isConnected(BlockState state, boolean otherwise) {
        return this.getObject() instanceof AbstractBasedWireBlock based ? based.connectsToState(state) : otherwise;
    }

    @Unique
    private boolean isConnected(BlockState state, Direction direction, boolean otherwise) {
        return this.getObject() instanceof AbstractBasedWireBlock based ? based.connectsToState(state, direction) : otherwise;
    }

    @Inject(method = "canRunOnTop", at = @At("HEAD"), cancellable = true)
    private void canRunOnTop(BlockView world, BlockPos pos, BlockState floor, CallbackInfoReturnable<Boolean> cir) {
        if (floor.isOf(MultiSignalsBlocks.SUPPORT)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void update(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (this.getObject() instanceof AbstractBasedWireBlock based) {
            int lastSignal;
            int lastPower;
            int i = based.getReceivedTypedSignal(world, pos);
            if ((lastSignal = state.get(based.getSignalProperty())) != i) {
                if (world.getBlockState(pos) == state) {
                    world.setBlockState(pos, state.with(based.getSignalProperty(), i), Block.NOTIFY_LISTENERS);
                }
            }
            int j = based.getReceivedTypedPower(world, pos);
            if ((lastPower = state.get(based.getPowerProperty())) != j) {
                if (world.getBlockState(pos) == state) {
                    world.setBlockState(pos, state.with(based.getPowerProperty(), j), Block.NOTIFY_LISTENERS);
                }
            }
            if (lastSignal != i || lastPower != j) {
                HashSet<BlockPos> set = Sets.newHashSet();
                set.add(pos);
                for (Direction direction : Direction.values()) {
                    set.add(pos.offset(direction));
                }
                for (BlockPos blockPos : set) {
                    world.updateNeighborsAlways(blockPos, this.getObject());
                }
            }
            ci.cancel();
        }
    }

    @Inject(method = "increasePower", at = @At("HEAD"), cancellable = true)
    private void increasePower(BlockState state, CallbackInfoReturnable<Integer> cir) {
        if (this.getObject() instanceof AbstractBasedWireBlock based) {
            cir.setReturnValue(state.isOf(this.getObject()) ? state.get(based.getPowerProperty()) : 0);
        }
    }

    @WrapOperation(method = "getWeakRedstonePower", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/property/Property;)Ljava/lang/Comparable;", ordinal = 0))
    private Comparable<Integer> wrapGetWeakPowerGetter(BlockState blockState, Property<Integer> vanillaPowerProperty, Operation<Comparable<Integer>> original) {
        return this.getObject() instanceof AbstractBasedWireBlock based ? blockState.get(based.getPowerProperty()) : original.call(blockState, vanillaPowerProperty);
    }

    @Unique
    private RedstoneWireBlock getObject() {
        return (RedstoneWireBlock) (Object) this;
    }
}
