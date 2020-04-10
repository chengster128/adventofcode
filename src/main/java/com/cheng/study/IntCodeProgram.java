package com.cheng.study;

public class IntCodeProgram {
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