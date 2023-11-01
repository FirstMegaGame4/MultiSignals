package fr.firstmegagame4.multisignals.mixin;

import fr.firstmegagame4.multisignals.block.AbstractBasedWireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(State.class)
public class StateMixin<O, S> {

    @Inject(method = "with", at = @At("TAIL"))
    private <T extends Comparable<T>, V extends T> void with(Property<T> property, V value, CallbackInfoReturnable<S> cir) {
        if (this.getObject() instanceof BlockState state) {
            if (state.getBlock() instanceof AbstractBasedWireBlock based) {
                if (property.equals(based.getPowerProperty())) {
                    state.with(based.getSignalProperty(), 0);
                }
            }
        }
    }

    @Unique
    @SuppressWarnings("unchecked")
    public State<O, S> getObject() {
        return (State<O, S>) (Object) this;
    }
}
