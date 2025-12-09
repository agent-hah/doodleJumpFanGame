package org.cis1200.doodlejump;

import java.util.Random;

// This code is from the chatterbot homework

/**
 * Random number generator to include an element of randomness into the game.
 */
public class RandomGenerator {
    private final Random r;

    /**
     * Constructor to create the random number generator
     */
    public RandomGenerator() {
        r = new Random();
    }

    /**
     * Method for getting a random positive integer (including 0)
     * 
     * @param bound the (exclusive) top restriction on the output
     * @return the random number
     */
    public int next(int bound) {
        return r.nextInt(bound);
    }

    /**
     * Method to getting a random boolean
     * 
     * @return true or false (random)
     */
    public boolean nextBoolean() {
        return r.nextBoolean();
    }
}
