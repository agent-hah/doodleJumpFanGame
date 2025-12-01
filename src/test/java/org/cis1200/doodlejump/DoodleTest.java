package org.cis1200.doodlejump;

import org.junit.jupiter.api.*;
import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class DoodleTest {

    @Test
    public void testSaveReader() {
        String test = "45,-12,13,5";
        LinkedList<Integer> expected = new LinkedList<>(Arrays.asList(45, -12, 13, 5));
        LinkedList<Integer> actual = SaveReader.parse(test);

        assertEquals(expected, actual);
    }

    // TODO: write test cases
}
