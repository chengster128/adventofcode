package com.cheng.study;

import java.awt.Point;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        answerDay1();
        answerDay2();
        answerDay2Part2();
        answerDay3();
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

    private static void answerDay2Part2() {
        try {
            final int[] codes = InputLoader.getIntCodes();

            IntCodeProgram program = new IntCodeProgram(codes);
            try {
                int[] answer = program.getInputPairForOutput(19690720);
                if (answer.length > 0) {
                    System.out.println(String.format("Day 2 Part 2 Answer: noun=%d, verb=%d", answer[0], answer[1]));
                } else {
                    System.out.println("Day 2 Part 2 Answer: Not Found!");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("ERROR: Day 2 - Could not load from file");
        }
    }

    private static void answerDay3() {
        try {
            final List<List<String>> wires = InputLoader.getWires();
            Instant start = Instant.now();
            CrossWireEvaluator program = new CrossWireEvaluator();
            try {
                Optional<Point> point = program.getClosestIntersectionOfPaths(wires);
                if (point.isPresent()) {
                    Instant finish = Instant.now();
                    long timeElapsed = Duration.between(start, finish).toMillis(); 
                    System.out.println("Day 3 Part 1. Dur:" + timeElapsed + "ms Answer: " + CrossWireEvaluator.getManhattanDistanceFromOrigin(point.get()));
                } else {
                    System.out.println("Day 3 Part 1 Answer: Not Found!");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("ERROR: Day 3 - Could not load from file");
        
        }
    }

}
