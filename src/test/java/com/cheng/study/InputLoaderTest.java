package com.cheng.study;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

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
}