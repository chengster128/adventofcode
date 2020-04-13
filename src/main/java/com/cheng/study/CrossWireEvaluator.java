package com.cheng.study;

import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import java.awt.Point;

public class CrossWireEvaluator {
    private Map<Integer, Set<PathSegment>> paths;
    private Map<Integer, Point> pathsLastPoint;
    private Point closestIntersectionPoint;

    public CrossWireEvaluator() {
        paths = new HashMap<Integer, Set<PathSegment>>();
        pathsLastPoint = new HashMap<Integer, Point>();
    }

    public Optional<Point> getClosestIntersectionOfPaths(List<List<String>> paths) throws Exception {
        for (int pathNum = 0; pathNum < paths.size(); pathNum++) {
            for (int segment = 0; segment < paths.get(pathNum).size(); segment++) {
                PathSegment pathSegment = processPathSegment(pathNum, paths.get(pathNum).get(segment));
                Optional<Point> intersection = findClosestIntersectionForPathSegment(pathNum, pathSegment);
                if (intersection.isPresent()) {
                    closestIntersectionPoint = getCloserPoint(closestIntersectionPoint, intersection.get());
                }
            }
        }
        if (closestIntersectionPoint != null) {
            return Optional.of(closestIntersectionPoint);
        }
        return Optional.empty();
    }

    PathSegment processPathSegment(int pathNum, String segment) throws Exception { 
        if (!pathsLastPoint.containsKey(pathNum)) {
            pathsLastPoint.put(pathNum, new Point(0, 0));
        }
        Point currentPoint = pathsLastPoint.get(pathNum);
        PathSegment pathSegment = PathSegment.fromVector(currentPoint, segment);

        if (paths.containsKey(pathNum)) {
            paths.get(pathNum).add(pathSegment);
        } else {
            Set<PathSegment> segments = new HashSet<PathSegment>();
            segments.add(pathSegment);
            paths.put(pathNum, segments);
        }

        currentPoint = pathSegment.getEnd();
        pathsLastPoint.put(pathNum, currentPoint);

        return pathSegment;
    }

    Optional<Point> findClosestIntersectionForPathSegment(int pathNum, PathSegment pathSegment) {
        if (paths.keySet().size() <= 1) {
            return Optional.empty();
        }
        
        List<Point> intersectingPoints = findIntersectionsWithOtherPaths(pathNum, pathSegment);
        if (intersectingPoints.isEmpty()) {
            return Optional.empty();
        }
        Point closerPoint = null;
        for (Point point : intersectingPoints) {
            closerPoint = getCloserPoint(closerPoint, point);
        }
        return Optional.of(closerPoint);
    }

    List<Point> findIntersectionsWithOtherPaths(int pathNum, PathSegment pathSegment) {
        List<Point> intersectingPoints = paths.entrySet().stream()
                .filter(map -> map.getKey().intValue() != pathNum).map(map -> map.getValue())
                .flatMap(map -> map.stream())
                .map(p->calculateIntersectionPoint(pathSegment, p))
                .filter(p->p.isPresent())
                .map(p->p.get())
                .collect(Collectors.toList());
        
        return intersectingPoints;
    }

    static Optional<Point> calculateIntersectionPoint(PathSegment path1, PathSegment path2) {
        final Point intersection;

        if (isBetween(path1.getStart().getX(), path2.getStart().getX(), path2.getEnd().getX())
            && isBetween(path2.getStart().getY(), path1.getStart().getY(), path1.getEnd().getY())) {
            intersection = new Point((int) path1.getStart().getX(), (int) path2.getStart().getY());
        } else if (isBetween(path2.getStart().getX(), path1.getStart().getX(), path1.getEnd().getX())
            && isBetween(path1.getStart().getY(), path2.getStart().getY(), path2.getEnd().getY())) {
            intersection = new Point((int) path2.getStart().getX(), (int) path1.getStart().getY());
        } else {
            return Optional.empty();
        }

        //Ignore origin point as a valid intersection
        if (intersection.getX() != 0 || intersection.getY() != 0) {
            return Optional.of(intersection);
        } else {
            return Optional.empty();
        }
    }

    static boolean isBetween(double value, double x1, double x2) {
        return (x1 < value && value < x2) || (x2 < value && value < x1);
    }

    static Point getCloserPoint(Point p1, Point p2) {
        if (p1 == null) {
            return p2;
        }
        if (p2 == null) {
            return p1;
        }
        return getManhattanDistanceFromOrigin(p1) < getManhattanDistanceFromOrigin(p2) ? p1 : p2;
    }

    public static int getManhattanDistanceFromOrigin(Point point) {
        return (int) Math.abs(point.getX()) + (int) Math.abs(point.getY());
    }
}