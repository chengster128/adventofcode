package com.cheng.study;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Point;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PathSegmentTest {
    @Test
    public void testPointFromVector() throws Exception {
        Point start = new Point(0,0);
        String vector1 = "R10";
        PathSegment expected1 = new PathSegment(start, new Point(0,10));

        PathSegment path1 = PathSegment.fromVector(start, vector1);
        assertEquals(expected1, path1);
    }

    @Test
    public void testPointFromInvalidVector() throws Exception {
        Point start = new Point(0,0);
        String vector1 = "T10";
        Assertions.assertThrows(Exception.class, () -> { 
            PathSegment.fromVector(start, vector1); });
    }

    @ParameterizedTest(name = "{index} => pointAx={0}, pointAy={1}, pointBx={2}, pointBy={3}, distance={4}")
    @CsvSource({
        "0, 0, 5, 5, 10",
        "0, 0, -5, 5, 10",
        "-2, -2, -4, -4, 4",
        "-2, 2, 2, 2, 4"
    })
    public void testCalculateDistanceBetweenPoints(int pointAx, int pointAy, int pointBx, int pointBy, int distance) {
        Point pointA = new Point(pointAx, pointAy);
        Point pointB = new Point(pointBx, pointBy);
        PathSegment segment = new PathSegment(pointA, pointB);
        assertEquals(distance, segment.calculateDistance());
    }
}