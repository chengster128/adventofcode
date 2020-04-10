package com.cheng.study;

import java.io.IOException;
import java.util.List;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        answerDay1();
        answerDay2();
    }

    private static void answerDay1() {
        try {
            List<Integer> masses = InputLoader.getMasses();
            Integer totalMass = masses.stream().reduce(0, Integer::sum);
            System.out.println("Day 1 Total Mass: " + totalMass);
            
            Integer totalFuelNeeded = FuelCalculator.calculateFuelNeeded(totalMass);
            System.out.println("Day 1 Part 1 Answer: " + totalFuelNeeded);

            totalFuelNeeded = masses.stream()
                .mapToInt(FuelCalculator::calculateFuelNeededInclusive)
                .sum();
            System.out.println("Day 1 Part 2 Answer: " + totalFuelNeeded);
        } catch(IOException e) {
            System.out.println("ERROR: Day 1 - Could not load from file");
        }
    }

    private static void answerDay2() {
        try {
            int[] codes = InputLoader.getIntCodes();
            //restore to "1202 program alarm" state
            codes[1] = 12;
            codes[2] = 2;
            codes = IntCodeProgram.computeProgramCodes(codes);
            System.out.println("Day 2 Answer: " + codes[0]);
        } catch (Exception e) {
            System.out.println("ERROR: Day 2 - Could not load from file");
        }
    }

}
