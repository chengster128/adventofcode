package com.cheng.study;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        answerDay1();
    }

    private static void answerDay1() {
        try {
            List<Integer> masses = InputLoader.getMassesFromFile();
            Integer totalMass = masses.stream().reduce(0, Integer::sum);
            System.out.println("Day 1 Total Mass: " + totalMass);
            
            Integer totalFuelNeeded = FuelCalculator.calculateFuelNeeded(totalMass);
            System.out.println("Day 1 Part 1 Answer: " + totalFuelNeeded);

            totalFuelNeeded = masses.stream()
                .mapToInt(FuelCalculator::calculateFuelNeededInclusive)
                .sum();
            System.out.println("Day 1 Part 2 Answer: " + totalFuelNeeded);
        } catch(IOException e) {
            System.out.println("ERROR: Could not load masses from file");
        }
    }

}
