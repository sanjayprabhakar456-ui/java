package com.campusnav.service;

import com.campusnav.dao.LocationDAO;
import com.campusnav.dao.ReportDAO;
import com.campusnav.dao.UserDAO;
import com.campusnav.exception.InvalidLoginException;
import com.campusnav.exception.LocationNotFoundException;
import com.campusnav.model.IssueReport;
import com.campusnav.model.Location;
import com.campusnav.model.RouteResult;
import com.campusnav.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Core Service Layer coordinating business logic, authentication,
 * location filtering, and the Breadth-First Search (BFS) shortest path algorithm.
 *
 * Demonstrates:
 * - Collections: ArrayList, HashMap, HashSet, ArrayDeque.
 * - Java 17 Features: var, enhanced switch expressions, Records (RouteResult).
 * - Exception handling with custom exceptions.
 */
@Service
public class CampusService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LocationDAO locationDAO;

    @Autowired
    private ReportDAO reportDAO;

    // --- Authentication ---

    public User authenticate(String username, String password) throws InvalidLoginException {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new InvalidLoginException("Username and password must not be empty.");
        }
        return userDAO.authenticate(username.trim(), password.trim());
    }

    // --- Location Search & Details ---

    public List<Location> getAllLocations() {
        try {
            return locationDAO.findAll();
        } catch (Exception e) {
            System.err.println("[CampusService] Error fetching all locations: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Location getLocationById(int id) throws LocationNotFoundException {
        return locationDAO.findById(id);
    }

    /**
     * Search locations using simple loop/filter (no complex algorithms).
     * Filters by query across name, building, room number, or category.
     */
    public List<Location> searchLocations(String query, String category) {
        var all = getAllLocations();
        var results = new ArrayList<Location>();

        var cleanQuery = (query != null) ? query.trim().toLowerCase() : "";
        var cleanCat = (category != null) ? category.trim().toLowerCase() : "";

        for (Location loc : all) {
            boolean matchesQuery = cleanQuery.isEmpty() ||
                loc.getName().toLowerCase().contains(cleanQuery) ||
                loc.getBuilding().toLowerCase().contains(cleanQuery) ||
                loc.getRoomNumber().toLowerCase().contains(cleanQuery) ||
                loc.getCategory().toLowerCase().contains(cleanQuery) ||
                loc.getDescription().toLowerCase().contains(cleanQuery);

            boolean matchesCategory = cleanCat.isEmpty() || loc.getCategory().equalsIgnoreCase(cleanCat);

            if (matchesQuery && matchesCategory) {
                results.add(loc);
            }
        }
        return results;
    }

    // --- Graph & BFS Routing Algorithm ---

    /**
     * Finds the shortest path between start and destination using Breadth-First Search (BFS).
     *
     * DSA Implementation:
     * - Graph: Adjacency list stored in a HashMap<Integer, List<Integer>>.
     * - Queue: ArrayDeque<Integer> for FIFO exploration.
     * - Visited: HashSet<Integer> to avoid cycles and redundant processing.
     * - Parent Map: HashMap<Integer, Integer> to backtrack and reconstruct the exact path.
     */
    public RouteResult findShortestRoute(int startId, int destId) {
        if (startId == destId) {
            try {
                var singleLoc = locationDAO.findById(startId);
                var path = new ArrayList<Location>();
                path.add(singleLoc);
                return RouteResult.success(path, "You are already at the destination: " + singleLoc.getName());
            } catch (LocationNotFoundException e) {
                return RouteResult.notFound("Selected location was not found.");
            }
        }

        try {
            // Check that both locations exist
            var startLoc = locationDAO.findById(startId);
            var destLoc = locationDAO.findById(destId);

            // Fetch graph adjacency list from database
            Map<Integer, List<Integer>> graph = locationDAO.getGraphAdjacencyList();

            // BFS data structures required by syllabus
            ArrayDeque<Integer> queue = new ArrayDeque<>();
            Set<Integer> visited = new HashSet<>();
            Map<Integer, Integer> parentMap = new HashMap<>();

            // Initialize BFS
            queue.add(startId);
            visited.add(startId);

            boolean reached = false;

            while (!queue.isEmpty()) {
                int current = queue.poll();

                if (current == destId) {
                    reached = true;
                    break;
                }

                List<Integer> neighbors = graph.getOrDefault(current, Collections.emptyList());
                for (int neighbor : neighbors) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        parentMap.put(neighbor, current);
                        queue.add(neighbor);
                    }
                }
            }

            if (!reached) {
                return RouteResult.notFound("No accessible route found between '" 
                    + startLoc.getName() + "' and '" + destLoc.getName() + "'.");
            }

            // Reconstruct path by walking backwards from destination to start using parentMap
            var pathIds = new ArrayList<Integer>();
            int curr = destId;
            while (curr != startId) {
                pathIds.add(curr);
                curr = parentMap.get(curr);
            }
            pathIds.add(startId);
            Collections.reverse(pathIds);

            // Convert IDs to Location entities
            var pathLocations = new ArrayList<Location>();
            for (int id : pathIds) {
                pathLocations.add(locationDAO.findById(id));
            }

            return RouteResult.success(pathLocations, "Shortest route calculated (" + (pathLocations.size() - 1) + " hops).");

        } catch (LocationNotFoundException e) {
            return RouteResult.notFound("Error finding location: " + e.getMessage());
        } catch (Exception e) {
            return RouteResult.notFound("Error calculating route: " + e.getMessage());
        }
    }

    // --- Issue Reporting ---

    public void submitReport(IssueReport report) throws Exception {
        if (report.getDescription() == null || report.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty.");
        }
        reportDAO.save(report);
    }

    public List<IssueReport> getReportsForStudent(String studentUsername) {
        try {
            return reportDAO.findByStudentUsername(studentUsername);
        } catch (Exception e) {
            System.err.println("[CampusService] Error fetching student reports: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<IssueReport> getAllReports() {
        try {
            return reportDAO.findAll();
        } catch (Exception e) {
            System.err.println("[CampusService] Error fetching all reports: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void updateReportStatus(int reportId, String status) throws Exception {
        // Java 17 Enhanced Switch for status validation and normalization
        String normalizedStatus = switch (status.toUpperCase()) {
            case "PENDING" -> "PENDING";
            case "IN_PROGRESS" -> "IN_PROGRESS";
            case "RESOLVED" -> "RESOLVED";
            default -> throw new IllegalArgumentException("Invalid status: " + status);
        };
        reportDAO.updateStatus(reportId, normalizedStatus);
    }

    // --- Admin Location & Graph Management ---

    public void saveLocation(Location location) throws Exception {
        if (location.getName() == null || location.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Location name is required.");
        }
        locationDAO.save(location);
    }

    public void deleteLocation(int id) throws Exception {
        locationDAO.deleteById(id);
    }

    public void addConnection(int fromId, int toId) throws Exception {
        if (fromId == toId) {
            throw new IllegalArgumentException("Cannot connect a location to itself.");
        }
        locationDAO.addConnection(fromId, toId);
    }

    public List<int[]> getAllConnections() {
        try {
            return locationDAO.getAllConnections();
        } catch (Exception e) {
            System.err.println("[CampusService] Error fetching connections: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
