package fr.firstmegagame4.multisignals;

import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class SignalColors {

    public static final Vec3d[][] COLORS = Util.make(
        new Vec3d[16][], signals -> {
            for (int i = 0; i < 16; i++) {
                int finalI = i;
                signals[i] = Util.make(
                    new Vec3d[16], powers -> {
                        for (int j = 0; j < 16; j++) {
                            float h = 225f / 15 * finalI;
                            float s = 100;
                            float v = 30 + MathHelper.floor((80 - 30) / 15f * j);
                            powers[j] = new Vec3d(h, s, v);
                        }
                    }
                );
            }
        }
    );

    public static int getWireColor(int signal, int power) {
        Vec3d vec3d = SignalColors.COLORS[signal][power];
        return MathHelper.hsvToRgb((float) vec3d.getX() / 360f, (float) vec3d.getY() / 100f, (float) vec3d.getZ() / 100f);
    }

    public static Vec3d getWireColorRgb(int signal, int power) {
        int rgb = SignalColors.getWireColor(signal, power);
        return new Vec3d(ColorHelper.Argb.getRed(rgb), ColorHelper.Argb.getGreen(rgb), ColorHelper.Argb.getBlue(rgb));
    }
}
