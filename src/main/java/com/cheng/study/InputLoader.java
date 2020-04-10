package com.cheng.study;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

public class InputLoader {

    public static int[] getIntCodesFromFile() throws IOException {
        String text = "";
        File f = new File("src/main/resources/intcodes.txt");
        if (f.canRead()) {
			try {
                text = FileUtils.readFileToString(f, "UTF-8");
			} catch (IOException e) {
                throw e;
			}
        }
        return Stream.of(text.split(","))
            .mapToInt(Integer::parseInt)
            .toArray();
    }

    public static List<Integer> getMassesFromFile() throws IOException {
        List<String> lines = null;
        File f = new File("src/main/resources/masses.txt");
        if (f.canRead()) {
			try {
                lines = FileUtils.readLines(f, "UTF-8");
			} catch (IOException e) {
                throw e;
			}
        }
        return lines.stream()
            .map(s->Integer.parseInt(s))
            .collect(Collectors.toList());
    }
}