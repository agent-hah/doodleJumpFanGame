package org.cis1200.doodlejump;
import java.util.Random;


// Random number generator to get x coordinates for each layer
public class RandomNumberGenerator {
    private final Random r;

    public RandomNumberGenerator() {
        r = new Random();
    }

    public int next(int bound) {
        return r.nextInt(bound);
    }
}
