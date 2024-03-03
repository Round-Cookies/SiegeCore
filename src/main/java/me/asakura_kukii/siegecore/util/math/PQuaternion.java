package me.asakura_kukii.siegecore.util.math;

import org.joml.Quaternionf;

public class PQuaternion extends Quaternionf {

    public PQuaternion() {super();}

    public PQuaternion(float x, float y, float z, float w) {
        super(x, y, z, w);
    }

    @Override
    public PQuaternion clone() {
        try {
            return (PQuaternion) super.clone();
        } catch (Exception ignored) {
            return null;
        }
    }
}
