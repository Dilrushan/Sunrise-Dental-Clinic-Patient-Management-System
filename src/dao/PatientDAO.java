package dao;

import Model.Patient;
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PatientDAO {

    public int registerPatient(Patient patient) {
        String query = "INSERT INTO patients (full_name, address, contact_number, email, treatment_history) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            if (conn == null) return -1;
            pstmt.setString(1, patient.getFullName());
            pstmt.setString(2, patient.getAddress());
            pstmt.setString(3, patient.getContactNumber());
            pstmt.setString(4, patient.getEmail());
            pstmt.setString(5, patient.getTreatmentHistory());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Patient registration failed: " + e.getMessage());
        }
        return -1;
    }

    public Patient getPatientByName(String name) {
        String query = "SELECT patient_id, full_name, address, contact_number, email, treatment_history FROM patients WHERE full_name = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return null;
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("full_name"),
                    rs.getString("address"),
                    rs.getString("contact_number"),
                    rs.getString("email"),
                    rs.getString("treatment_history")
                );
            }
        } catch (SQLException e) {
            System.err.println("Get patient by name failed: " + e.getMessage());
        }
        return null;
    }

    public boolean isEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM patients WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Email check failed: " + e.getMessage());
        }
        return false;
    }
}
