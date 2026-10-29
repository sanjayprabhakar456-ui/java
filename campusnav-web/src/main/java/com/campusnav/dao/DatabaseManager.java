package com.campusnav.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages raw JDBC Connections and schema initialization for H2 File Database.
 * Plain JDBC is used (Connection, PreparedStatement, ResultSet, Statement).
 */
@Component
public class DatabaseManager {

    @Value("${campusnav.db.url:jdbc:h2:file:./data/campusnav;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE}")
    private String dbUrl;

    @Value("${campusnav.db.user:sa}")
    private String dbUser;

    @Value("${campusnav.db.password:}")
    private String dbPassword;

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @PostConstruct
    public void initializeDatabase() {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();

            // 1. Create Users Table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(50) NOT NULL,
                    full_name VARCHAR(100) NOT NULL,
                    role VARCHAR(20) NOT NULL,
                    extra_info VARCHAR(100)
                );
            """);

            // 2. Create Locations Table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS locations (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    building VARCHAR(100) NOT NULL,
                    floor VARCHAR(20) NOT NULL,
                    room_number VARCHAR(50) NOT NULL,
                    category VARCHAR(50) NOT NULL,
                    description VARCHAR(500) NOT NULL
                );
            """);

            // 3. Create Connections Table (Adjacency List Graph Edges)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS location_connections (
                    from_id INT NOT NULL,
                    to_id INT NOT NULL,
                    PRIMARY KEY (from_id, to_id),
                    FOREIGN KEY (from_id) REFERENCES locations(id) ON DELETE CASCADE,
                    FOREIGN KEY (to_id) REFERENCES locations(id) ON DELETE CASCADE
                );
            """);

            // 4. Create Issue Reports Table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS issue_reports (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    student_username VARCHAR(50) NOT NULL,
                    location_id INT NOT NULL,
                    category VARCHAR(50) NOT NULL,
                    description VARCHAR(500) NOT NULL,
                    status VARCHAR(20) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE CASCADE
                );
            """);

            // Seed initial data if tables are empty
            seedInitialData(conn);

        } catch (SQLException e) {
            System.err.println("[DatabaseManager] Database initialization error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Demonstrating proper resource cleanup in finally block
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.err.println("[DatabaseManager] Error closing resources: " + ex.getMessage());
            }
        }
    }

    private void seedInitialData(Connection conn) throws SQLException {
        // Check if users already exist
        Statement checkStmt = conn.createStatement();
        ResultSet rs = checkStmt.executeQuery("SELECT COUNT(*) FROM users");
        int userCount = 0;
        if (rs.next()) {
            userCount = rs.getInt(1);
        }
        rs.close();
        checkStmt.close();

        if (userCount == 0) {
            // Seed two demo users
            var userSql = "INSERT INTO users (username, password, full_name, role, extra_info) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(userSql)) {
                // student1 / student123
                ps.setString(1, "student1");
                ps.setString(2, "student123");
                ps.setString(3, "Alex Rivera");
                ps.setString(4, "STUDENT");
                ps.setString(5, "Computer Science");
                ps.executeUpdate();

                // admin / admin123
                ps.setString(1, "admin");
                ps.setString(2, "admin123");
                ps.setString(3, "Dr. Robert Smith");
                ps.setString(4, "ADMIN");
                ps.setString(5, "Admin Office, Ext 401");
                ps.executeUpdate();
            }
        }

        // Check if locations exist
        Statement locCheckStmt = conn.createStatement();
        ResultSet locRs = locCheckStmt.executeQuery("SELECT COUNT(*) FROM locations");
        int locCount = 0;
        if (locRs.next()) {
            locCount = locRs.getInt(1);
        }
        locRs.close();
        locCheckStmt.close();

        if (locCount == 0) {
            // Seed 15 sample locations
            String[][] locations = {
                {"Main Gate", "Entrance", "Ground", "Gate-01", "Gate", "Campus main security entrance, visitor badge post and bus terminal."},
                {"Administrative Block", "Admin Tower", "1st Floor", "AD-101", "Administrative", "Offices of the Dean, Registrar, Admissions, and Accounts."},
                {"Central Library", "Library Complex", "1st Floor", "LIB-Main", "Library", "3-story reading hall, quiet study rooms, and digital periodicals."},
                {"Computer Science Wing", "Tech Complex", "2nd Floor", "CS-201", "Academic", "Department of Computer Science & Engineering lecture rooms."},
                {"Software Engineering Lab", "Tech Complex", "2nd Floor", "LAB-204", "Laboratory", "High-performance programming lab with Linux development rigs."},
                {"Electronics & IoT Lab", "Engineering Block", "1st Floor", "EE-105", "Laboratory", "Microcontroller benches, sensors, soldering kits, and oscilloscopes."},
                {"Mechanical Workshop", "Workshop Shed", "Ground", "ME-G02", "Laboratory", "Heavy tooling, CNC machines, precision lathe, and welding bay."},
                {"Main Auditorium", "Cultural Center", "Ground", "AUD-1", "Cultural", "1,200 seat grand auditorium for convocations and cultural fests."},
                {"Campus Cafeteria", "Student Center", "Ground", "SC-Cafe", "Cafeteria", "Multi-cuisine food court, coffee bar, and snack counters."},
                {"Student Activity Center", "Student Center", "1st Floor", "SC-102", "Student Life", "Club headquarters, robotics team room, and student council desk."},
                {"Indoor Sports Complex", "Sports Arena", "Ground", "GYM-01", "Sports", "Badminton courts, fitness gym, table tennis, and locker rooms."},
                {"Science Lecture Hall A", "Basic Sciences", "1st Floor", "SC-110", "Academic", "Stepped tiered amphitheater lecture room with digital smartboard."},
                {"Health & Medical Center", "Amenities Block", "Ground", "HC-01", "Healthcare", "24/7 student clinic, first-aid unit, and visiting physician."},
                {"Innovation & Incubation Hub", "Tech Complex", "3rd Floor", "INN-301", "Research", "Student startup workspace, 3D printing bay, and mentoring cell."},
                {"East Campus Gate", "East Boundary", "Ground", "Gate-02", "Gate", "Perimeter pedestrian gate connecting to local transit station."}
            };

            var locSql = "INSERT INTO locations (id, name, building, floor, room_number, category, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(locSql)) {
                for (int i = 0; i < locations.length; i++) {
                    ps.setInt(1, i + 1);
                    ps.setString(2, locations[i][0]);
                    ps.setString(3, locations[i][1]);
                    ps.setString(4, locations[i][2]);
                    ps.setString(5, locations[i][3]);
                    ps.setString(6, locations[i][4]);
                    ps.setString(7, locations[i][5]);
                    ps.executeUpdate();
                }
            }

            // Seed graph connections (bidirectional edges)
            int[][] connections = {
                {1, 2},  // Main Gate <-> Admin Block
                {1, 3},  // Main Gate <-> Central Library
                {2, 3},  // Admin Block <-> Central Library
                {2, 8},  // Admin Block <-> Main Auditorium
                {3, 4},  // Central Library <-> CS Wing
                {3, 12}, // Central Library <-> Science Hall A
                {4, 5},  // CS Wing <-> Software Eng Lab
                {4, 14}, // CS Wing <-> Innovation Hub
                {5, 6},  // Software Eng Lab <-> Electronics Lab
                {6, 7},  // Electronics Lab <-> Mechanical Workshop
                {3, 9},  // Central Library <-> Campus Cafeteria
                {9, 10}, // Cafeteria <-> Student Activity Center
                {10, 11},// Student Activity Center <-> Sports Complex
                {8, 9},  // Main Auditorium <-> Cafeteria
                {12, 13},// Science Hall A <-> Health Center
                {13, 15},// Health Center <-> East Campus Gate
                {11, 15} // Sports Complex <-> East Campus Gate
            };

            var connSql = "INSERT INTO location_connections (from_id, to_id) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(connSql)) {
                for (int[] edge : connections) {
                    // Forward edge
                    ps.setInt(1, edge[0]);
                    ps.setInt(2, edge[1]);
                    ps.executeUpdate();
                    // Backward edge for undirected graph
                    ps.setInt(1, edge[1]);
                    ps.setInt(2, edge[0]);
                    ps.executeUpdate();
                }
            }

            // Seed 5 sample reports
            String[][] reports = {
                {"student1", "5", "Equipment", "Workstation #14 LCD display flickers intermittently during compilation.", "PENDING"},
                {"student1", "3", "Lighting", "Reading cubicle 4B overhead lamp bulb has burned out.", "IN_PROGRESS"},
                {"student1", "9", "Cleanliness", "Water cooler tray in the cafeteria dining area is overflowing.", "RESOLVED"},
                {"student1", "4", "WiFi", "Campus Wi-Fi connectivity drops frequently outside classroom CS-201.", "PENDING"},
                {"student1", "11", "Maintenance", "Net cord on badminton court 2 is frayed and needs re-tensioning.", "IN_PROGRESS"}
            };

            var repSql = "INSERT INTO issue_reports (student_username, location_id, category, description, status) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(repSql)) {
                for (String[] rep : reports) {
                    ps.setString(1, rep[0]);
                    ps.setInt(2, Integer.parseInt(rep[1]));
                    ps.setString(3, rep[2]);
                    ps.setString(4, rep[3]);
                    ps.setString(5, rep[4]);
                    ps.executeUpdate();
                }
            }
        }
    }
}
