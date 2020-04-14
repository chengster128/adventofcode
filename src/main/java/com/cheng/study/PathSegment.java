package com.cheng.study;

import java.awt.Point;
import java.util.Objects;

public class PathSegment {
    private final Point start;
    private final Point end;
    private final int distanceFromOriginAtStart;
    private final boolean isHorizontal;
    private final static String ERROR_INVALID_VECTOR = "Invalid vector value";

    public PathSegment(Point start, Point end) {
        this.start = start;
        this.end = end;
        this.distanceFromOriginAtStart = 0;
        this.isHorizontal = start.getY() == end.getY();
    }

    public PathSegment(Point start, Point end, int distanceFromOriginAtStart) {
        this.start = start;
        this.end = end;
        this.distanceFromOriginAtStart = distanceFromOriginAtStart;
        this.isHorizontal = start.getY() == end.getY();
    }

    public Point getStart() {
        return this.start;
    }

    public Point getEnd() {
        return this.end;
    }

    public int getDistanceFromOriginAtStart() {
        return distanceFromOriginAtStart;
    }

    public boolean getIsHorizontal() {
        return isHorizontal;
    }

    public static PathSegment fromVector(Point start, String vector) throws Exception {
        char direction = getDirectionFromVector(vector);
        Integer distance = Integer.parseInt(vector.substring(1));
        int endY = calculateY(start, direction, distance);
        int endX = calculateX(start, direction, distance);
        return new PathSegment(start, new Point(endX, endY));
    }

    public static PathSegment fromVector(Point start, String vector, int previousDistanceFromOrigin) throws Exception {
        char direction = getDirectionFromVector(vector);
        Integer distance = Integer.parseInt(vector.substring(1));
        int endY = calculateY(start, direction, distance);
        int endX = calculateX(start, direction, distance);
        Point endPoint = new Point(endX, endY);
        return new PathSegment(start, 
            endPoint, 
            previousDistanceFromOrigin);
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

    public final int calculateDistance() {
        return (int)(Math.abs(start.getX() - end.getX()) + Math.abs(start.getY() - end.getY()));
    }

    public int getDistanceFromOriginAtEnd() {
        return distanceFromOriginAtStart + calculateDistance();
    }

    // boolean isHorizontal() {
    //     return start.getY() == end.getY();
    // }

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