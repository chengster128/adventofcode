package com.cheng.study;

public class FuelCalculator {
    public static int calculateFuelNeeded(int mass) {
        return Math.max(0, mass / 3 - 2);
    }
    public static int calculateFuelNeededInclusive(int mass) {
        Integer totalFuelNeeded = calculateFuelNeeded(mass);
        Integer fuelForFuelNeeded = totalFuelNeeded;
        while ((fuelForFuelNeeded = FuelCalculator.calculateFuelNeeded(fuelForFuelNeeded)) > 0) {
            totalFuelNeeded += fuelForFuelNeeded;
        }
        return totalFuelNeeded;
    }
}