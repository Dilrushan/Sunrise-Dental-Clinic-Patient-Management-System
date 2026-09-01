/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import Model.Patient;
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP
 */
public class PatientDAO {
    public boolean addPatient(String fullName, String address, String contactNumber, String email) {
        String query = "INSERT INTO patients (full_name, address, contact_number, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, fullName);
            pstmt.setString(2, address);
            pstmt.setString(3, contactNumber);
            pstmt.setString(4, email);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM patients";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery(query)) {
            
            while (rs.next()) {
                patients.add(new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("full_name"),
                    rs.getString("address"),
                    rs.getString("contact_number"),
                    rs.getString("email"),
                    rs.getString("treatment_history")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }
}
