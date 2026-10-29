package com.campusnav.dao;

import com.campusnav.exception.InvalidLoginException;
import com.campusnav.model.Admin;
import com.campusnav.model.Student;
import com.campusnav.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Users using plain JDBC.
 * Implements Repository<User> interface.
 * Demonstrates:
 * - Try/catch/finally blocks for robust resource management.
 * - Polymorphic instantiation of Student and Admin.
 * - Throwing custom InvalidLoginException.
 */
@Repository
public class UserDAO implements com.campusnav.dao.Repository<User> {

    @Autowired
    private DatabaseManager databaseManager;

    /**
     * Authenticates a user with username and password.
     * Throws InvalidLoginException if invalid.
     */
    public User authenticate(String username, String password) throws InvalidLoginException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = databaseManager.getConnection();
            String sql = "SELECT id, username, password, full_name, role, extra_info FROM users WHERE username = ? AND password = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String uname = rs.getString("username");
                String pass = rs.getString("password");
                String fullName = rs.getString("full_name");
                String role = rs.getString("role");
                String extraInfo = rs.getString("extra_info");

                // Polymorphism: instantiate Student or Admin based on role
                if ("STUDENT".equalsIgnoreCase(role)) {
                    return new Student(id, uname, pass, fullName, extraInfo);
                } else if ("ADMIN".equalsIgnoreCase(role)) {
                    return new Admin(id, uname, pass, fullName, extraInfo);
                } else {
                    throw new InvalidLoginException("Unknown role assigned to user: " + role);
                }
            } else {
                throw new InvalidLoginException("Invalid username or password.");
            }
        } catch (SQLException e) {
            throw new InvalidLoginException("Database error during authentication: " + e.getMessage());
        } finally {
            // Demonstrating explicit try/catch/finally resource cleanup required for viva
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("[UserDAO] Error closing JDBC resources: " + e.getMessage());
            }
        }
    }

    @Override
    public User findById(int id) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = databaseManager.getConnection();
            String sql = "SELECT id, username, password, full_name, role, extra_info FROM users WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                String uname = rs.getString("username");
                String pass = rs.getString("password");
                String fullName = rs.getString("full_name");
                String role = rs.getString("role");
                String extraInfo = rs.getString("extra_info");

                if ("STUDENT".equalsIgnoreCase(role)) {
                    return new Student(id, uname, pass, fullName, extraInfo);
                } else {
                    return new Admin(id, uname, pass, fullName, extraInfo);
                }
            }
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ignored) {}
        }
    }

    @Override
    public List<User> findAll() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<User> list = new ArrayList<>();

        try {
            conn = databaseManager.getConnection();
            String sql = "SELECT id, username, password, full_name, role, extra_info FROM users";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String uname = rs.getString("username");
                String pass = rs.getString("password");
                String fullName = rs.getString("full_name");
                String role = rs.getString("role");
                String extraInfo = rs.getString("extra_info");

                if ("STUDENT".equalsIgnoreCase(role)) {
                    list.add(new Student(id, uname, pass, fullName, extraInfo));
                } else {
                    list.add(new Admin(id, uname, pass, fullName, extraInfo));
                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ignored) {}
        }
        return list;
    }

    @Override
    public void save(User entity) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = databaseManager.getConnection();
            String sql = "INSERT INTO users (username, password, full_name, role, extra_info) VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getPassword());
            ps.setString(3, entity.getFullName());
            ps.setString(4, entity.getRole());
            ps.setString(5, entity instanceof Student s ? s.getDepartment() : (entity instanceof Admin a ? a.getOfficePhone() : ""));
            ps.executeUpdate();
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
            String sql = "DELETE FROM users WHERE id = ?";
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
}
