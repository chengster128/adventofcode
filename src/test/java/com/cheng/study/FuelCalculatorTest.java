package com.cheng.study;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class FuelCalculatorTest {
    
    @Test
    public void testCalculateFueldNeeded() {
        assertEquals(1103828, FuelCalculator.calculateFuelNeeded(3311492));
    }

    @Test
    public void testCalculateFuelNeededInclusive() {
        assertEquals(50346, FuelCalculator.calculateFuelNeededInclusive(100756));
    }
}