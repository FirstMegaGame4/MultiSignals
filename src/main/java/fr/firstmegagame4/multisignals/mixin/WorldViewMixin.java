package fr.firstmegagame4.multisignals.mixin;

import fr.firstmegagame4.multisignals.injected.world.CitrineView;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldView.class)
public interface WorldViewMixin extends CitrineView {}
