package com.cheng.study;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Point;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
}