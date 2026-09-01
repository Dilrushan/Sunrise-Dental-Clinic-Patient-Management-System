/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author HP
 */
public class AppointmentDAO {
    public boolean bookAppointment(int patientId, int doctorId, String appointmentDate, String treatmentType) {
        String query = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, treatment_type, status) VALUES (?, ?, ?, ?, 'Scheduled')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, patientId);
            pstmt.setInt(2, doctorId);
            pstmt.setString(3, appointmentDate);
            pstmt.setString(4, treatmentType);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
