package SymulatorEpidemii;

import java.util.Random;

public class Rng {
    private static Random random = new Random();
    public static void setSeed(long seed) {
        random.setSeed(seed);
    }
    public static double nextDouble() {
        return random.nextDouble();
    }
    public static int nextInt(int n) {
        return random.nextInt(n);
    }
}
