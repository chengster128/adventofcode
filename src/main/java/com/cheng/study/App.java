package com.cheng.study;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        List<Integer> masses = getMassesFromFile();
        Integer totalMass = masses.stream().reduce(0, Integer::sum);
        System.out.println("Day 1 Total Mass: " + totalMass);
        
        Integer totalFuelNeeded = FuelCalculator.calculateFuelNeeded(totalMass);
        System.out.println("Day 1 Part 1 Answer: " + totalFuelNeeded);

        totalFuelNeeded = masses.stream()
            .mapToInt(FuelCalculator::calculateFuelNeededInclusive)
            .sum();
        System.out.println("Day 1 Part 2 Answer: " + totalFuelNeeded);

    }

    private static List<Integer> getMassesFromFile() {
        File f = new File("src/main/resources/masses.txt");
        if (f.canRead()) {
            List<String> lines;
			try {
				lines = FileUtils.readLines(f, "UTF-8");
			} catch (IOException e) {
                return Collections.emptyList();
			}
            return lines.stream().map(s->Integer.parseInt(s)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

}
