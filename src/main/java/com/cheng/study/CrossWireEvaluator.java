package com.cheng.study;

import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.awt.Point;

public class CrossWireEvaluator {
    private Map<Integer, Set<PathSegment>> paths;
    private Map<Integer, Point> pathsLastPoint;
    private Map<Point, Integer> intersectionPathsDistance;

    public CrossWireEvaluator() {
        paths = new HashMap<Integer, Set<PathSegment>>();
        pathsLastPoint = new HashMap<Integer, Point>();
        intersectionPathsDistance = new HashMap<Point, Integer>();
    }

    public void loadPaths(List<List<String>> paths) throws Exception {
        for (int pathNum = 0; pathNum < paths.size(); pathNum++) {
            int totalWireLength = 0;
            for (int segment = 0; segment < paths.get(pathNum).size(); segment++) {
                PathSegment pathSegment = processPathSegment(pathNum, paths.get(pathNum).get(segment), totalWireLength);
                totalWireLength = pathSegment.getDistanceFromOriginAtEnd();

                List<PathSegment> intersectionPaths = findIntersectingPathsWithOtherPaths(pathNum, pathSegment);
                if (!intersectionPaths.isEmpty()) {
                    Map<Point, Integer> d = getIntersectionPointAndDistance(pathSegment, intersectionPaths);
                    d.entrySet().forEach(e -> intersectionPathsDistance.putIfAbsent(e.getKey(), e.getValue()));
                }
            }
        }
    }

    public Optional<Integer> getClosestManhattanIntersection() {
        return intersectionPathsDistance.keySet()
            .stream()
            .map(p -> getManhattanDistanceFromOrigin(p))
            .min(Integer::compareTo);
    }
    
    public Optional<Integer> getShortestDistanceIntersection() {
        return intersectionPathsDistance.values()
            .stream()
            .min(Integer::compareTo);
    }

    PathSegment processPathSegment(int pathNum, String segment) throws Exception {
        return processPathSegment(pathNum, segment, 0);
    }

    PathSegment processPathSegment(int pathNum, String segment, int currentPathLength) throws Exception {
        if (!pathsLastPoint.containsKey(pathNum)) {
            pathsLastPoint.put(pathNum, new Point(0, 0));
        }
        Point currentPoint = pathsLastPoint.get(pathNum);
        PathSegment pathSegment = PathSegment.fromVector(currentPoint, segment, currentPathLength);

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
        List<Point> intersectingPoints = getPathSegmentsOfOrientationExcludingPathNum(pathNum,
                pathSegment.getIsHorizontal()).map(p -> calculateIntersectionPoint(pathSegment, p))
                        .filter(p -> p.isPresent()).map(p -> p.get()).collect(Collectors.toList());

        return intersectingPoints;
    }

    List<PathSegment> findIntersectingPathsWithOtherPaths(int pathNum, PathSegment pathSegment) {
        List<PathSegment> intersectingPoints = getPathSegmentsOfOrientationExcludingPathNum(pathNum,
                pathSegment.getIsHorizontal()).filter(s -> pathsIntersect(pathSegment, s)).collect(Collectors.toList());

        return intersectingPoints;
    }

    Stream<PathSegment> getPathSegmentsOfOrientationExcludingPathNum(int excludePathNum, boolean isHorizontal) {
        return paths.entrySet().stream().filter(map -> map.getKey().intValue() != excludePathNum)
                .flatMap(map -> map.getValue().stream()).filter(p -> p.getIsHorizontal() != isHorizontal);
    }

    Map<Point, Integer> getIntersectionPointAndDistance(PathSegment path, List<PathSegment> intersectionPaths) {
        return intersectionPaths.stream()
                .collect(Collectors.toMap(p -> calculateIntersectionPoint(path, p).get(), p -> {
                    try {
                        return getTotalPathDistanceAtIntersection(path, p);
                    } catch (Exception e) {
                        return 0;
                    }
                }));
    }

    static Optional<Point> calculateIntersectionPoint(PathSegment path1, PathSegment path2) {
        final Point intersection;

        if (pathsIntersect(path1, path2)) {
            intersection = path1.getIsHorizontal() ? 
            new Point((int) path2.getStart().getX(), (int) path1.getStart().getY()) :
            new Point((int) path1.getStart().getX(), (int) path2.getStart().getY());
            ;
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

    static boolean pathsIntersect(PathSegment path1, PathSegment path2) {
        return (
            isBetween(path1.getStart().getX(), path2.getStart().getX(), path2.getEnd().getX())
            && isBetween(path2.getStart().getY(), path1.getStart().getY(), path1.getEnd().getY())
        ) || (
            isBetween(path2.getStart().getX(), path1.getStart().getX(), path1.getEnd().getX())
            && isBetween(path1.getStart().getY(), path2.getStart().getY(), path2.getEnd().getY())
        );
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
        return getDistanceBetweenPoints(new Point(0,0), point);
    }

    public static int getTotalPathDistanceAtIntersection(PathSegment path1, PathSegment path2) throws Exception {
        Optional<Point> intersection = calculateIntersectionPoint(path1, path2);
        if (!intersection.isPresent()) {
            throw new Exception("Paths do not intersect.");
        }

        return path1.getDistanceFromOriginAtStart() + 
            path2.getDistanceFromOriginAtStart() + 
            getDistanceBetweenPoints(path1.getStart(), intersection.get()) + 
            getDistanceBetweenPoints(path2.getStart(), intersection.get());
    }

    public static int getDistanceBetweenPoints(Point a, Point b) {
        return (int)Math.abs(a.getX() - b.getX()) + 
            (int)Math.abs(a.getY() - b.getY());
    }
}