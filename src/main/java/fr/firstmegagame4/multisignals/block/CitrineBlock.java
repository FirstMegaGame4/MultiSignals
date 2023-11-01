package fr.firstmegagame4.multisignals.block;

import fr.firstmegagame4.multisignals.MultiSignals;
import fr.firstmegagame4.multisignals.init.MultiSignalsItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.registry.Registries;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CitrineBlock extends Block {

    private final Type type;

    public CitrineBlock(Type type, Settings settings) {
        super(settings);
        this.type = type;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getStackInHand(hand).isOf(MultiSignalsItems.CITRINE) && this.type.ordinal() < Type.values().length - 1) {
            ItemStack stack = player.getStackInHand(hand);
            stack.setCount(stack.getCount() - 1);
            player.setStackInHand(hand, stack);
            world.setBlockState(pos, Type.values()[this.type.ordinal() + 1].getBlock().getDefaultState());
            return ActionResult.SUCCESS;
        }
        else if (player.getStackInHand(hand).getItem() instanceof PickaxeItem && this.type.ordinal() > 0) {
            world.setBlockState(pos, Type.values()[this.type.ordinal() - 1].getBlock().getDefaultState());
            player.giveItemStack(new ItemStack(MultiSignalsItems.CITRINE));
            return ActionResult.SUCCESS;
        }
        else {
            return super.onUse(state, world, pos, player, hand, hit);
        }
    }

    @Override
    public boolean emitsCitrinePower(BlockState state) {
        return true;
    }

    @Override
    public int getCitrineSignal(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return switch (this.type) {
            case DEPLETED -> 1;
            case NORMAL -> 2;
            case CONCENTRATED -> 3;
        };
    }

    @Override
    public int getWeakCitrinePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return 15;
    }

    public enum Type {
        DEPLETED("depleted_citrine_block"),
        NORMAL("citrine_block"),
        CONCENTRATED("concentrated_citrine_block");

        private final String path;

        Type(String path) {
            this.path = path;
        }

        public Block getBlock() {
            return Registries.BLOCK.get(MultiSignals.createId(this.path));
        }
    }
}
