package com.cheng.study;

import java.util.Arrays;

public class IntCodeProgram {

    private final int[] codesOriginal;

    public IntCodeProgram(int[] codes) {
        this.codesOriginal = codes;
    }

    public int[] getInputPairForOutput(int desiredOutput) throws Exception {
        //try inputs
        for (int noun = 0; noun < 100; noun++) {
            for (int verb = 0; verb < 100; verb++) {
                int[] codesCopy = Arrays.copyOf(codesOriginal, codesOriginal.length);
                codesCopy[1] = noun;
                codesCopy[2] = verb;
                codesCopy = computeProgramCodes(codesCopy);
                if (codesCopy[0] == desiredOutput) {
                    return new int[] { noun, verb };
                }
            }
        }
        return new int[0];
    }

    public static int[] computeProgramCodes(int[] codes) throws Exception {
        int opCodeIndex = 0;
        while (opCodeIndex < codes.length - 1) {
            int opCode = codes[opCodeIndex];
            if (opCode == 99) {
                return codes;
            } else {
                int value1 = codes[codes[opCodeIndex+1]];
                int value2 = codes[codes[opCodeIndex+2]];
                if (opCode == 1) {
                    codes[codes[opCodeIndex+3]] = value1 + value2;
                } else if (opCode == 2) {
                    codes[codes[opCodeIndex+3]] = value1 * value2;
                } else {
                    throw new Exception(String.format("opCode {} not recognized.",opCode));
                }
                opCodeIndex+=4;
            }
        }
        return codes;
    }
}