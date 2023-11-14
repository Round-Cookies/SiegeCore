package me.asakura_kukii.siegecore.util.math;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Random;

public class PMath {
    private static final float[] sinArray = new float[65536];

    private static final float[] tanArray = new float[65536];

    public static final float pi = 3.14159265F;

    public static float sin(float f) {
        return sinArray[(int) (f * 10430.378F) & '\uffff'];
    }

    public static float cos(float f) {
        return sinArray[(int) (f * 10430.378F + 16384.0F) & '\uffff'];
    }

    public static float tan(float f) {
        return tanArray[(int) (f * 10430.378F) & '\uffff'];
    }

    public static Random r = new Random();

    static {
        for (int i = 0; i < 65536; ++i) {
            sinArray[i] = (float) Math.sin((double) i * 3.141592653589793D * 2.0D / 65536.0D);
            tanArray[i] = (float) Math.tan((double) i * 3.141592653589793D * 2.0D / 65536.0D);
        }
    }

    public static float ran() { return r.nextFloat();}

    public static int ranIndex(int size) {
        int index = (int) Math.floor(ran() * size);
        if (index == size) index = size - 1;
        return index;
    }
}
