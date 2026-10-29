package com.campusnav.model;

import java.util.List;

/**
 * Java 17 Feature: Record for immutable route results.
 */
public record RouteResult(
    boolean found,
    List<Location> path,
    int stepCount,
    String message
) {
    public static RouteResult notFound(String message) {
        return new RouteResult(false, List.of(), 0, message);
    }

    public static RouteResult success(List<Location> path, String message) {
        return new RouteResult(true, path, path.size(), message);
    }
}
