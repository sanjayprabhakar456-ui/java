package com.campusnav.dao;

import com.campusnav.exception.LocationNotFoundException;
import com.campusnav.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object for Campus Locations and Graph Connections.
 * Uses plain JDBC (Connection, PreparedStatement, ResultSet).
 * Demonstrates:
 * - Try/catch/finally blocks.
 * - Throwing LocationNotFoundException.
 * - Adjacency graph extraction for BFS route finding.
 */
@Repository
public class LocationDAO implements com.campusnav.dao.Repository<Location> {

    @Autowired
    private DatabaseManager databaseManager;

    @Override
    public Location findById(int id) throws LocationNotFoundException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = databaseManager.getConnection();
            String sql = "SELECT id, name, building, floor, room_number, category, description FROM locations WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return new Location(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("building"),
                    rs.getString("floor"),
                    rs.getString("room_number"),
                    rs.getString("category"),
                    rs.getString("description")
                );
            } else {
                throw new LocationNotFoundException("Location with ID " + id + " was not found.");
            }
        } catch (SQLException e) {
            throw new LocationNotFoundException("Database error finding location: " + e.getMessage());
        } finally {
            // Demonstrating proper cleanup in finally block
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.err.println("[LocationDAO] Error closing JDBC resources: " + ex.getMessage());
            }
        }
    }

    @Override
    public List<Location> findAll() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Location> locations = new ArrayList<>();

        try {
            conn = databaseManager.getConnection();
            String sql = "SELECT id, name, building, floor, room_number, category, description FROM locations ORDER BY name ASC";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                locations.add(new Location(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("building"),
                    rs.getString("floor"),
                    rs.getString("room_number"),
                    rs.getString("category"),
                    rs.getString("description")
                ));
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ignored) {}
        }
        return locations;
    }

    @Override
    public void save(Location location) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = databaseManager.getConnection();
            if (location.getId() > 0) {
                // Update
                String sql = "UPDATE locations SET name = ?, building = ?, floor = ?, room_number = ?, category = ?, description = ? WHERE id = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, location.getName());
                ps.setString(2, location.getBuilding());
                ps.setString(3, location.getFloor());
                ps.setString(4, location.getRoomNumber());
                ps.setString(5, location.getCategory());
                ps.setString(6, location.getDescription());
                ps.setInt(7, location.getId());
                ps.executeUpdate();
            } else {
                // Insert
                String sql = "INSERT INTO locations (name, building, floor, room_number, category, description) VALUES (?, ?, ?, ?, ?, ?)";
                ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, location.getName());
                ps.setString(2, location.getBuilding());
                ps.setString(3, location.getFloor());
                ps.setString(4, location.getRoomNumber());
                ps.setString(5, location.getCategory());
                ps.setString(6, location.getDescription());
                ps.executeUpdate();

                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    location.setId(generatedKeys.getInt(1));
                }
                generatedKeys.close();
            }
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ignored) {}
        }
    }

    @Override
    public void deleteById(int id) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = databaseManager.getConnection();
            String sql = "DELETE FROM locations WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ignored) {}
        }
    }

    /**
     * Adds a bidirectional connection between two campus locations.
     */
    public void addConnection(int fromId, int toId) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = databaseManager.getConnection();
            // Using MERGE / INSERT IGNORE logic for H2
            String sql = "MERGE INTO location_connections (from_id, to_id) VALUES (?, ?)";
            ps = conn.prepareStatement(sql);

            // Forward
            ps.setInt(1, fromId);
            ps.setInt(2, toId);
            ps.executeUpdate();

            // Reverse (undirected path)
            ps.setInt(1, toId);
            ps.setInt(2, fromId);
            ps.executeUpdate();
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ignored) {}
        }
    }

    /**
     * Loads the campus graph adjacency list: Map<Integer, List<Integer>>.
     * Demonstrates Collections: HashMap and ArrayList.
     */
    public Map<Integer, List<Integer>> getGraphAdjacencyList() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<Integer, List<Integer>> graph = new HashMap<>();

        try {
            conn = databaseManager.getConnection();
            String sql = "SELECT from_id, to_id FROM location_connections";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int from = rs.getInt("from_id");
                int to = rs.getInt("to_id");

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ignored) {}
        }
        return graph;
    }

    /**
     * Retrieves all connection pairs for administrative display.
     */
    public List<int[]> getAllConnections() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<int[]> connections = new ArrayList<>();

        try {
            conn = databaseManager.getConnection();
            // Select where from_id < to_id so we display each unique undirected path once
            String sql = "SELECT from_id, to_id FROM location_connections WHERE from_id < to_id";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                connections.add(new int[]{rs.getInt("from_id"), rs.getInt("to_id")});
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ignored) {}
        }
        return connections;
    }
}
