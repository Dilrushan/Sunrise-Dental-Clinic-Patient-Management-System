package dao;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public User validateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password_hash = ? AND status = 'Active'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return null;
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getString("full_name"),
                    rs.getString("role"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.err.println("User validation failed: " + e.getMessage());
        }
        return null;
    }

    public boolean insertUser(String username, String password, String fullName, String role) {
        String query = "INSERT INTO users (username, password_hash, full_name, role, status) VALUES (?, ?, ?, ?, 'Active')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, fullName);
            pstmt.setString(4, role);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("User registration failed: " + e.getMessage());
            return false;
        }
    }

    public boolean isUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Username check failed: " + e.getMessage());
        }
        return false;
    }

    public List<String[]> getDoctors() {
        List<String[]> doctors = new ArrayList<>();
        String query = "SELECT user_id, full_name FROM users WHERE role = 'Doctor' AND status = 'Active'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return doctors;
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                doctors.add(new String[]{
                    String.valueOf(rs.getInt("user_id")),
                    rs.getString("full_name")
                });
            }
        } catch (SQLException e) {
            System.err.println("Get doctors failed: " + e.getMessage());
        }
        return doctors;
    }

    public String getUserRole(String username) {
        String query = "SELECT role FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return null;
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            System.err.println("Get user role failed: " + e.getMessage());
        }
        return null;
    }

    public static class User {
        private final int userId;
        private final String username;
        private final String passwordHash;
        private final String fullName;
        private final String role;
        private final String status;

        public User(int userId, String username, String passwordHash, String fullName, String role, String status) {
            this.userId = userId;
            this.username = username;
            this.passwordHash = passwordHash;
            this.fullName = fullName;
            this.role = role;
            this.status = status;
        }

        public int getUserId() { return userId; }
        public String getUsername() { return username; }
        public String getPasswordHash() { return passwordHash; }
        public String getFullName() { return fullName; }
        public String getRole() { return role; }
        public String getStatus() { return status; }
    }
}
