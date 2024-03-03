package me.asakura_kukii.siegecore.util.math;

import org.apache.commons.lang3.tuple.Pair;
import org.joml.Math;

import java.util.List;
import java.util.Random;

public class PMath extends Math {
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

    public static float sign(float f) {
        if (f >= 0) return 1F;
        return -1F;
    }

    public static void seed(long seed) { r = new Random(seed);}

    public static float ran() { return r.nextFloat();}

    public static float ranFloat(float low, float high) {
        return low + (high - low) * ran();
    }

    public static float gaussian(float std) {
        return (float) r.nextGaussian() * std;
    }

    public static int ranIndex(int size) {
        int index = (int) Math.floor(ran() * size);
        if (index == size) index = size - 1;
        return index;
    }

    public static int ranInt(int low, int high) {
        if (high < low) high = low;
        return ranIndex(high + 1 - low) + low;
    }

    public static boolean ranBoolean(float f) {
        return r.nextFloat() <= f;
    }

    public static int floatToIndex(float f, int size) {
        int index = (int) floor(f * size);
        if (index == size) index = size - 1;
        return index;
    }

    public static Integer ranIndexWeighted(List<Float> floatList) {
        if (floatList == null || floatList.size() == 0) return null;
        int index = 0;
        float sum = 0;
        for (Float f : floatList) {
            if (f == null || f == 0) continue;
            sum = sum + f;
        }
        float threshold = ran() * sum;
        float probe = 0;
        for (int i = 0; i < floatList.size(); i++) {
            probe = probe + floatList.get(i);
            if (threshold < probe) {
                index = i;
                break;
            }
        }
        return index;
    }
}
