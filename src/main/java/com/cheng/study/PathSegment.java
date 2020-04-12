package com.cheng.study;

import java.awt.Point;
import java.util.Objects;

public class PathSegment {
    private final Point start;
    private final Point end;
    private final static String ERROR_INVALID_VECTOR = "Invalid vector value";

    public PathSegment(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public static PathSegment fromVector(Point start, String vector) throws Exception {
        char direction = getDirectionFromVector(vector);
        Integer distance = Integer.parseInt(vector.substring(1));
        int endY = calculateY(start, direction, distance);
        int endX = calculateX(start, direction, distance);
        return new PathSegment(start, new Point(endX, endY));
    }

    private static char getDirectionFromVector(String vector) throws Exception {
        if (vector.length() < 2) {
            throw new Exception(ERROR_INVALID_VECTOR);
        }
        char direction = vector.charAt(0);
        if (direction != 'U' && 
            direction != 'D' && 
            direction != 'L' && 
            direction != 'R') {
            throw new Exception(ERROR_INVALID_VECTOR);
        }
        return direction;
    }

    private static int calculateY(Point start, char direction, int distance) {
        if (direction == 'L' || direction == 'R') {
            return (int) start.getY();
        }
        if (direction == 'U') {
            return (int) (start.getY() + distance);
        } 
        return (int) (start.getY() - distance);
    }

    private static int calculateX(Point start, char direction, int distance) {
        if (direction == 'U' || direction == 'D') {
            return (int) start.getX();
        }
        if (direction == 'R') {
            return (int) (start.getX() + distance);
        } 
        return (int) (start.getX() - distance);
    }

    public Point getStart() {
        return this.start;
    }

    public Point getEnd() {
        return this.end;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof PathSegment)) {
            return false;
        }

        PathSegment pathSegment = (PathSegment) other;
        return start.equals(pathSegment.getStart()) &&
            start.equals(pathSegment.getStart());
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}