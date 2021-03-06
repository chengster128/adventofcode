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

        assertTrue(!program.findClosestIntersectionForPathSegment(1, program.processPathSegment(1, "R8")).isPresent());
        assertTrue(!program.findClosestIntersectionForPathSegment(2, program.processPathSegment(2, "D2")).isPresent());
        assertTrue(!program.findClosestIntersectionForPathSegment(2, program.processPathSegment(2, "R2")).isPresent());
        Optional<Point> intersection = program.findClosestIntersectionForPathSegment(2, program.processPathSegment(2, "U3"));

        assertTrue(intersection.isPresent());
        assertEquals(new Point(2,0), intersection.get());
    }

    @Test
    public void testProcessPathSegmentExcludeOrigin() throws Exception {
        CrossWireEvaluator program = new CrossWireEvaluator();
        
        assertTrue(!program.findClosestIntersectionForPathSegment(1, program.processPathSegment(1, "R8")).isPresent());
        assertTrue(!program.findClosestIntersectionForPathSegment(2, program.processPathSegment(2, "D2")).isPresent());
        assertTrue(!program.findClosestIntersectionForPathSegment(2, program.processPathSegment(2, "L2")).isPresent());
        assertTrue(!program.findClosestIntersectionForPathSegment(2, program.processPathSegment(2, "U3")).isPresent());
        assertTrue(!program.findClosestIntersectionForPathSegment(2, program.processPathSegment(2, "R2")).isPresent());
        assertTrue(!program.findClosestIntersectionForPathSegment(2, program.processPathSegment(2, "D2")).isPresent());
    }
    
    @ParameterizedTest(name = "{index} => x1={0}, y1={1}, vector1={2}, x2={3}, y2={4}, vector2={5}, ix={6}, iy={7}")
    @CsvSource({
        "0, 0, R4, 2, 2, D4, 2, 0",
        "0, 0, U4, -2, 2, R4, 0, 2"
    })
    public void testCalculateIntersectionPoint(int x1, int y1, String vector1,
                                                int x2, int y2, String vector2,
                                                int ix, int iy) throws Exception {
        PathSegment path1 = PathSegment.fromVector(new Point(x1,y1), vector1);
        PathSegment path2 = PathSegment.fromVector(new Point(x2,y2), vector2);
        Point expected = new Point(ix,iy);
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

    @DisplayName("Should find the closest manhattan intersection distance")
    @ParameterizedTest(name = "{index} => wire1Str={0}, wire2Str={1}, distance={2}")
    @CsvSource({
        "R75|D30|R83|U83|L12|D49|R71|U7|L72, U62|R66|U55|R34|D71|R55|D58|R83, 159",
        "R98|U47|R26|D63|R33|U87|L62|D20|R33|U53|R51, U98|R91|D20|R16|D67|R40|U7|R15|U6|R7, 135"
    })
    public void testGetClosestIntersectionOfPaths(String wire1Str, String wire2Str, int distance) throws Exception {
        List<List<String>> wires = new ArrayList<>();

        String[] wire1 = wire1Str.split("\\|");
        List<String> wires1List = new ArrayList<String>();
        Collections.addAll(wires1List, wire1);
        wires.add(wires1List);

        String[] wire2 = wire2Str.split("\\|");
        List<String> wires2List = new ArrayList<String>();
        Collections.addAll(wires2List, wire2);
        wires.add(wires2List);

        CrossWireEvaluator program = new CrossWireEvaluator();
        // Optional<Point> intersection = program.getClosestIntersectionOfPaths(wires);
        program.loadPaths(wires);
        Optional<Integer> closestManhattanOptional = program.getClosestManhattanIntersection();
        assertTrue(closestManhattanOptional.isPresent());
        assertEquals(distance, closestManhattanOptional.get());
    }

    @ParameterizedTest(name = "{index} => aX={0}, aY={1}, bX={2}, bY={3}, distance={4}")
    @CsvSource({ 
        "2, 2, 4, 4, 4",
        "4, 4, 2, 2, 4",
        "-2, -2, -4, -4, 4",
        "-2, 2, 2, 2, 4"
    })
    public void testGetDistanceBetweenPoints(int aX, int aY, int bX, int bY, int distance) {
        Point a = new Point(2,2);
        Point b = new Point(4,4);
        assertEquals(4, CrossWireEvaluator.getDistanceBetweenPoints(a, b));
    }
    
    @Test
    public void testGetTotalPathDistanceAtIntersection() throws Exception {
        PathSegment path1 = new PathSegment(new Point(2, 2), new Point(2,4), 4);
        PathSegment path2 = new PathSegment(new Point(0, 3), new Point(4,3), 3);
        assertEquals(10, CrossWireEvaluator.getTotalPathDistanceAtIntersection(path1, path2));
    }

    @DisplayName("Should find the shortest intersection distance by path length")
    @ParameterizedTest(name = "{index} => wire1Str={0}, wire2Str={1}, distance={2}")
    @CsvSource({
        "R75|D30|R83|U83|L12|D49|R71|U7|L72, U62|R66|U55|R34|D71|R55|D58|R83, 610",
        "R98|U47|R26|D63|R33|U87|L62|D20|R33|U53|R51, U98|R91|D20|R16|D67|R40|U7|R15|U6|R7, 410"
    })
    public void testGetShortestDistanceIntersectionOfPaths(String wire1Str, String wire2Str, int distance) throws Exception {
        List<List<String>> wires = new ArrayList<>();

        String[] wire1 = wire1Str.split("\\|");
        List<String> wires1List = new ArrayList<String>();
        Collections.addAll(wires1List, wire1);
        wires.add(wires1List);

        String[] wire2 = wire2Str.split("\\|");
        List<String> wires2List = new ArrayList<String>();
        Collections.addAll(wires2List, wire2);
        wires.add(wires2List);

        CrossWireEvaluator program = new CrossWireEvaluator();
        // Optional<Integer> shortestDistance = program.getShortestDistanceIntersectionOfPaths(wires);
        program.loadPaths(wires);
        Optional<Integer> shortestDistanceOptional = program.getShortestDistanceIntersection();
        assertTrue(shortestDistanceOptional.isPresent());
        assertEquals(distance, shortestDistanceOptional.get());
    }
}