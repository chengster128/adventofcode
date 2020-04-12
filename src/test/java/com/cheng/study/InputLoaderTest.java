package com.cheng.study;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

public class InputLoaderTest {
    @Test
    public void testGetMassesFromFile() throws IOException {
        List<Integer> masses = InputLoader.getMasses();
        assertFalse(masses.isEmpty());
    }

    @Test
    public void testGetIntCodesFromFile() throws IOException {
        int[] codes = InputLoader.getIntCodes();
        assertTrue(codes.length > 0);
    }

    @Test
    public void testGetWiresFromFile() throws IOException {
        List<List<String>> wires = InputLoader.getWires();
        assertEquals(2, wires.size());
        assertEquals("R1008", wires.get(0).get(0));
        assertEquals("R131", wires.get(0).get(wires.get(0).size()-1));
        assertEquals("L61", wires.get(1).get(wires.get(0).size()-1));
    }
}