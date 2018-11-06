package sim.util;

/**
 *
 * @author Admin
 */
import java.util.Random;

public class Util {

    private Util() {
    }
    static private Random _RANDOM = new Random();

    /**
     * doubles that differ by less than EPSILON should be considered equals
     */
    static public final double EPSILON = 1e-9;

    static public boolean isEquals(double x, double y) {
        return Math.abs(x - y) <= EPSILON;
    }

    static public boolean isLessOrEquals(double x, double y) {
        return (x - y) <= EPSILON;
    }

    static public boolean isLess(double x, double y) {
        return (x - y) < -EPSILON;
    }

    static public void setRandomSeed(long seed) {
        _RANDOM.setSeed(seed);
    }

    static public double nextRandom(double min, double max) {
        if (Util.isLess(max, min)) {
            throw new IllegalArgumentException(max + " is smaller than " + min);
        }
        double value = min + ((_RANDOM.nextDouble()) * (max - min));
        return (int)Math.ceil(value);//(double) Math.round(value * 100) / 100;
    }
}
