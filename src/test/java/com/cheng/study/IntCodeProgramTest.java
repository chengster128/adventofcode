package com.cheng.study;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

public class IntCodeProgramTest {

    @Test
    public void testComputeProgramCodes() throws Exception {
        int[] codes1Input = new int[] {1,0,0,0,99};
        int[] codes1Output = new int[] {2,0,0,0,99};
        assertArrayEquals(codes1Output, IntCodeProgram.computeProgramCodes(codes1Input));

        int[] codes2Input = new int[] {1,1,1,4,99,5,6,0,99};
        int[] codes2Output = new int[] {30,1,1,4,2,5,6,0,99};
        assertArrayEquals(codes2Output, IntCodeProgram.computeProgramCodes(codes2Input));

    }

    @Test
    public void testGetInputPairForOutput() throws Exception {
        int[] codesInput = InputLoader.getIntCodes();
        int[] expectedPair = new int[] { 12, 2 };
        int desiredOutput = 3790645;
        IntCodeProgram program = new IntCodeProgram(codesInput);
        int[] pair = program.getInputPairForOutput(desiredOutput);
        assertArrayEquals(expectedPair, pair);
    }
}