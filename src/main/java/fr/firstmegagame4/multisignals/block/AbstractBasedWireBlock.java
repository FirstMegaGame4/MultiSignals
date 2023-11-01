package fr.firstmegagame4.multisignals.block;

import fr.firstmegagame4.multisignals.SignalColors;
import fr.firstmegagame4.multisignals.mixin.RedstoneWireBlockAccessor;
import fr.firstmegagame4.multisignals.optimization.OptimizedWireSystem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.WireConnection;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Objects;

public abstract class AbstractBasedWireBlock extends RedstoneWireBlock implements OptimizedWireSystem {

    public static IntProperty createSignalProperty(String name, int limit) {
        return IntProperty.of(name, 0, limit);
    }

    public static IntProperty createPowerProperty(String name) {
        return IntProperty.of(name, 0, 15);
    }

    public AbstractBasedWireBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(this.getSignalProperty(), 0).with(this.getPowerProperty(), 0));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return RedstoneWireBlockAccessor.getShapes().get(state.with(this.getPowerProperty(), 0));
    }

    public boolean connectsToState(BlockState state) {
        return this.connectsToState(state, null);
    }

    public abstract boolean connectsToState(BlockState state, Direction direction);

    protected boolean emitsTypedPower(BlockState state) {
        return super.emitsRedstonePower(state);
    }

    protected int getWeakTypedPower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return super.getWeakRedstonePower(state, world, pos, direction);
    }

    protected int getStrongTypedPower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return super.getStrongRedstonePower(state, world, pos, direction);
    }

    abstract public int getReceivedTypedSignalOfWorld(World world, BlockPos pos);

    abstract public int getReceivedTypedPowerOfWorld(World world, BlockPos pos);

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return Objects.requireNonNull(super.getPlacementState(ctx)).with(this.getSignalProperty(), ((RedstoneWireBlockAccessor) this).getDotState().get(this.getSignalProperty()));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos).with(this.getSignalProperty(), state.get(this.getSignalProperty()));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WIRE_CONNECTION_NORTH);
        builder.add(WIRE_CONNECTION_EAST);
        builder.add(WIRE_CONNECTION_SOUTH);
        builder.add(WIRE_CONNECTION_WEST);
        builder.add(this.getSignalProperty());
        builder.add(this.getPowerProperty());
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return false;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return 0;
    }

    @Override
    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return 0;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        int signal = state.get(this.getSignalProperty());
        int power = state.get(this.getPowerProperty());
        if (signal == 0 || power == 0) {
            return;
        }
        for (Direction direction : Direction.Type.HORIZONTAL) {
            WireConnection wireConnection = state.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction));
            switch (wireConnection) {
                case UP: {
                    ((RedstoneWireBlockAccessor) this).invokeAddPoweredParticles(world, random, pos, SignalColors.getWireColorRgb(signal, power), direction, Direction.UP, -0.5f, 0.5f);
                }
                case SIDE: {
                    ((RedstoneWireBlockAccessor) this).invokeAddPoweredParticles(world, random, pos, SignalColors.getWireColorRgb(signal, power), Direction.DOWN, direction, 0.0f, 0.5f);
                    continue;
                }
            }
            ((RedstoneWireBlockAccessor) this).invokeAddPoweredParticles(world, random, pos, SignalColors.getWireColorRgb(signal, power), Direction.DOWN, direction, 0.0f, 0.3f);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!player.getAbilities().allowModifyWorld) {
            return ActionResult.PASS;
        }
        if (RedstoneWireBlockAccessor.invokeIsFullyConnected(state) || RedstoneWireBlockAccessor.invokeIsNotConnected(state)) {
            BlockState placeholderState = RedstoneWireBlockAccessor.invokeIsFullyConnected(state) ? this.getDefaultState() : ((RedstoneWireBlockAccessor) this).getDotState();
            BlockState blockState = ((RedstoneWireBlockAccessor) this).invokeGetPlacementState(world, placeholderState.with(this.getPowerProperty(), state.get(this.getPowerProperty())), pos);
            if (blockState != state) {
                world.setBlockState(pos, blockState, Block.NOTIFY_ALL);
                ((RedstoneWireBlockAccessor) this).invokeUpdateForNewState(world, pos, state, blockState);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public RedstoneWireBlock getObject() {
        return this;
    }
}
