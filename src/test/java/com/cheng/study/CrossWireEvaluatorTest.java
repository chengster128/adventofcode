package com.cheng.study;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CrossWireEvaluatorTest {

    @Test
    public void testProcessPathSegment() throws Exception {
        CrossWireEvaluator program = new CrossWireEvaluator();
        
        assertTrue(!program.processPathSegment(1, "R8").isPresent());
        assertTrue(!program.processPathSegment(2, "D2").isPresent());
        assertTrue(!program.processPathSegment(2, "R2").isPresent());
        Optional<Point> intersection = program.processPathSegment(2, "U3");

        assertTrue(intersection.isPresent());
        assertEquals(new Point(2,0), intersection.get());
    }

    @Test
    public void testProcessPathSegmentExcludeOrigin() throws Exception {
        CrossWireEvaluator program = new CrossWireEvaluator();
        
        assertTrue(!program.processPathSegment(1, "R8").isPresent());
        assertTrue(!program.processPathSegment(2, "D2").isPresent());
        assertTrue(!program.processPathSegment(2, "L2").isPresent());
        assertTrue(!program.processPathSegment(2, "U3").isPresent());
        assertTrue(!program.processPathSegment(2, "R2").isPresent());
        assertTrue(!program.processPathSegment(2, "D2").isPresent());
    }
    
    @Test
    public void testPathsIntersect() throws Exception {
        PathSegment path1 = PathSegment.fromVector(new Point(0,0), "R4");
        PathSegment path2 = PathSegment.fromVector(new Point(2,2), "D4");
        assertTrue(CrossWireEvaluator.pathsIntersect(path1, path2));
        assertTrue(CrossWireEvaluator.pathsIntersect(path2, path1));
    }

    @Test
    public void testCalculateIntersectionPoint() throws Exception {
        PathSegment path1 = PathSegment.fromVector(new Point(0,0), "R4");
        PathSegment path2 = PathSegment.fromVector(new Point(2,2), "D4");
        Point expected = new Point(2,0);
        Optional<Point> intersection = CrossWireEvaluator.calculateIntersectionPoint(path1, path2);
        
        assertEquals(expected, intersection.get());
    }

    @DisplayName("Should calculate the correct distance")
    @ParameterizedTest(name = "{index} => x={0}, y={1}, distance={2}")
    @CsvSource({
            "3, 3, 6",
            "-2, -2, 4",
            "2, -2, 4"
    })
    public void testGetManhattanDistance(int x, int y, int distance) {
        Point point = new Point(x,y);
        assertEquals(distance, CrossWireEvaluator.getManhattanDistanceFromOrigin(point));
    }

    @Test
    public void testGetClosestIntersectionOfPaths() throws Exception {
        List<List<String>> wires = new ArrayList<>();

        String[] wire1 = new String[] { "R75","D30","R83","U83","L12","D49","R71","U7","L72" };
        List<String> wires1List = new ArrayList<String>();
        Collections.addAll(wires1List, wire1);
        wires.add(wires1List);

        String[] wire2 = new String[] { "U62","R66","U55","R34","D71","R55","D58","R83" };
        List<String> wires2List = new ArrayList<String>();
        Collections.addAll(wires2List, wire2);
        wires.add(wires2List);

        CrossWireEvaluator program = new CrossWireEvaluator();
        Optional<Point> intersection = program.getClosestIntersectionOfPaths(wires);
        assertTrue(intersection.isPresent());
        assertEquals(159, CrossWireEvaluator.getManhattanDistanceFromOrigin(intersection.get()));
    }

}