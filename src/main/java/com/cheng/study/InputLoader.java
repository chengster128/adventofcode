package com.cheng.study;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

public class InputLoader {
    private static final String resourcesFolderPath = "src/main/resources/";
    public static int[] getIntCodes() throws IOException {
        String text = "";
        File f = new File(resourcesFolderPath.concat("intcodes.txt"));
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

    public static List<Integer> getMasses() throws IOException {
        List<String> lines = null;
        File f = new File(resourcesFolderPath.concat("masses.txt"));
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