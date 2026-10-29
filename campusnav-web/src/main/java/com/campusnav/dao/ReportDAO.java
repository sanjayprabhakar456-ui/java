package com.campusnav.dao;

import com.campusnav.model.IssueReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Issue Reports using plain JDBC.
 * Implements Repository<IssueReport>.
 * Demonstrates:
 * - Explicit try/catch/finally resource handling.
 * - JOIN queries to link issue reports to location names.
 */
@Repository
public class ReportDAO implements com.campusnav.dao.Repository<IssueReport> {

    @Autowired
    private DatabaseManager databaseManager;

    @Override
    public IssueReport findById(int id) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = databaseManager.getConnection();
            String sql = """
                SELECT r.id, r.student_username, r.location_id, l.name as loc_name, 
                       r.category, r.description, r.status, r.created_at 
                FROM issue_reports r
                JOIN locations l ON r.location_id = l.id
                WHERE r.id = ?
            """;
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToReport(rs);
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
    public List<IssueReport> findAll() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<IssueReport> reports = new ArrayList<>();

        try {
            conn = databaseManager.getConnection();
            String sql = """
                SELECT r.id, r.student_username, r.location_id, l.name as loc_name, 
                       r.category, r.description, r.status, r.created_at 
                FROM issue_reports r
                JOIN locations l ON r.location_id = l.id
                ORDER BY r.created_at DESC
            """;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                reports.add(mapResultSetToReport(rs));
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ignored) {}
        }
        return reports;
    }

    /**
     * Finds reports submitted by a specific student.
     */
    public List<IssueReport> findByStudentUsername(String studentUsername) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<IssueReport> reports = new ArrayList<>();

        try {
            conn = databaseManager.getConnection();
            String sql = """
                SELECT r.id, r.student_username, r.location_id, l.name as loc_name, 
                       r.category, r.description, r.status, r.created_at 
                FROM issue_reports r
                JOIN locations l ON r.location_id = l.id
                WHERE r.student_username = ?
                ORDER BY r.created_at DESC
            """;
            ps = conn.prepareStatement(sql);
            ps.setString(1, studentUsername);
            rs = ps.executeQuery();

            while (rs.next()) {
                reports.add(mapResultSetToReport(rs));
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ignored) {}
        }
        return reports;
    }

    @Override
    public void save(IssueReport entity) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = databaseManager.getConnection();
            String sql = "INSERT INTO issue_reports (student_username, location_id, category, description, status) VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, entity.getStudentUsername());
            ps.setInt(2, entity.getLocationId());
            ps.setString(3, entity.getCategory());
            ps.setString(4, entity.getDescription());
            ps.setString(5, entity.getStatus() != null ? entity.getStatus() : "PENDING");
            ps.executeUpdate();
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ignored) {}
        }
    }

    /**
     * Allows Admin to update report status (PENDING, IN_PROGRESS, RESOLVED).
     */
    public void updateStatus(int reportId, String status) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = databaseManager.getConnection();
            String sql = "UPDATE issue_reports SET status = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, reportId);
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
            String sql = "DELETE FROM issue_reports WHERE id = ?";
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

    private IssueReport mapResultSetToReport(ResultSet rs) throws SQLException {
        return new IssueReport(
            rs.getInt("id"),
            rs.getString("student_username"),
            rs.getInt("location_id"),
            rs.getString("loc_name"),
            rs.getString("category"),
            rs.getString("description"),
            rs.getString("status"),
            rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toString() : ""
        );
    }
}
