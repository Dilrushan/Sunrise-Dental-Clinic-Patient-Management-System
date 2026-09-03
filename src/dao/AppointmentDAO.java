package dao;

import Model.Appointment;
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public boolean scheduleAppointment(Appointment appointment) {
        String query = "INSERT INTO appointments (patient_name, contact_no, doctor_id, appointment_date, visit_type, fee, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            pstmt.setString(1, appointment.getPatientName());
            pstmt.setString(2, appointment.getContactNo());
            pstmt.setInt(3, appointment.getDoctorId());
            pstmt.setString(4, appointment.getAppointmentDate());
            pstmt.setString(5, appointment.getVisitType());
            pstmt.setDouble(6, appointment.getFee());
            pstmt.setString(7, appointment.getStatus());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Schedule appointment failed: " + e.getMessage());
            return false;
        }
    }

    public boolean isDuplicateBooking(String patientName, int doctorId, String appointmentDate) {
        String query = "SELECT COUNT(*) FROM appointments WHERE patient_name = ? AND doctor_id = ? AND appointment_date = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            pstmt.setString(1, patientName);
            pstmt.setInt(2, doctorId);
            pstmt.setString(3, appointmentDate);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Duplicate check failed: " + e.getMessage());
        }
        return false;
    }

    public Appointment getAppointmentById(int appointmentId) {
        String query = "SELECT * FROM appointments WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return null;
            pstmt.setInt(1, appointmentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Appointment(
                    rs.getInt("appointment_id"),
                    rs.getString("patient_name"),
                    rs.getString("contact_no"),
                    rs.getInt("doctor_id"),
                    rs.getString("appointment_date"),
                    rs.getString("visit_type"),
                    rs.getString("treatment_prescribed"),
                    rs.getDouble("fee"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.err.println("Get appointment failed: " + e.getMessage());
        }
        return null;
    }

    public boolean updatePrescription(int appointmentId, String treatmentPrescribed) {
        String query = "UPDATE appointments SET treatment_prescribed = ? WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            pstmt.setString(1, treatmentPrescribed);
            pstmt.setInt(2, appointmentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update prescription failed: " + e.getMessage());
            return false;
        }
    }

    public boolean updateFee(int appointmentId, double fee) {
        String query = "UPDATE appointments SET fee = ? WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            pstmt.setDouble(1, fee);
            pstmt.setInt(2, appointmentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update fee failed: " + e.getMessage());
            return false;
        }
    }

    public double getTreatmentFee(String treatmentName) {
        String query = "SELECT fee FROM treatments WHERE treatment_name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return -1;
            pstmt.setString(1, treatmentName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("fee");
            }
        } catch (SQLException e) {
            System.err.println("Get treatment fee failed: " + e.getMessage());
        }
        return -1;
    }

    public List<Appointment> getAppointmentsByDoctorId(int doctorId) {
        List<Appointment> list = new ArrayList<>();
        String query = "SELECT * FROM appointments WHERE doctor_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return list;
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Appointment(
                    rs.getInt("appointment_id"),
                    rs.getString("patient_name"),
                    rs.getString("contact_no"),
                    rs.getInt("doctor_id"),
                    rs.getString("appointment_date"),
                    rs.getString("visit_type"),
                    rs.getString("treatment_prescribed"),
                    rs.getDouble("fee"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Get appointments by doctor failed: " + e.getMessage());
        }
        return list;
    }

    public String getDoctorNameById(int doctorId) {
        String query = "SELECT full_name FROM users WHERE user_id = ? AND role = 'Doctor'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return "Unknown Doctor";
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("full_name");
            }
        } catch (SQLException e) {
            System.err.println("Get doctor name by ID failed: " + e.getMessage());
        }
        return "Unknown Doctor";
    }

    public List<String[]> getAllDoctors() {
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
            System.err.println("Get all doctors failed: " + e.getMessage());
        }
        return doctors;
    }

    public List<String[]> getAllPatients() {
        List<String[]> patients = new ArrayList<>();
        String query = "SELECT full_name, contact_number FROM patients";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return patients;
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                patients.add(new String[]{
                    rs.getString("full_name"),
                    rs.getString("contact_number")
                });
            }
        } catch (SQLException e) {
            System.err.println("Get all patients failed: " + e.getMessage());
        }
        return patients;
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String query = "SELECT * FROM appointments";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return list;
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Appointment(
                    rs.getInt("appointment_id"),
                    rs.getString("patient_name"),
                    rs.getString("contact_no"),
                    rs.getInt("doctor_id"),
                    rs.getString("appointment_date"),
                    rs.getString("visit_type"),
                    rs.getString("treatment_prescribed"),
                    rs.getDouble("fee"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Get all appointments failed: " + e.getMessage());
        }
        return list;
    }

    public List<Appointment> searchAppointments(String keyword) {
        List<Appointment> list = new ArrayList<>();
        String query = "SELECT * FROM appointments WHERE patient_name LIKE ? OR contact_no LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return list;
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Appointment(
                    rs.getInt("appointment_id"),
                    rs.getString("patient_name"),
                    rs.getString("contact_no"),
                    rs.getInt("doctor_id"),
                    rs.getString("appointment_date"),
                    rs.getString("visit_type"),
                    rs.getString("treatment_prescribed"),
                    rs.getDouble("fee"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Search appointments failed: " + e.getMessage());
        }
        return list;
    }

    public boolean updateAppointmentDate(int appointmentId, String date) {
        String query = "UPDATE appointments SET appointment_date = ? WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            pstmt.setString(1, date);
            pstmt.setInt(2, appointmentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update appointment date failed: " + e.getMessage());
            return false;
        }
    }

    public boolean updateVisitType(int appointmentId, String visitType) {
        String query = "UPDATE appointments SET visit_type = ? WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            pstmt.setString(1, visitType);
            pstmt.setInt(2, appointmentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update visit type failed: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAppointment(int appointmentId) {
        String query = "DELETE FROM appointments WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            pstmt.setInt(1, appointmentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Delete appointment failed: " + e.getMessage());
            return false;
        }
    }

    public List<String[]> getTreatments() {
        List<String[]> treatments = new ArrayList<>();
        String query = "SELECT treatment_name, fee FROM treatments";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return treatments;
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                treatments.add(new String[]{
                    rs.getString("treatment_name"),
                    String.valueOf(rs.getDouble("fee"))
                });
            }
        } catch (SQLException e) {
            System.err.println("Get treatments failed: " + e.getMessage());
        }
        return treatments;
    }

    public int getDoctorIdByUsername(String username) {
        String query = "SELECT user_id FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn == null) return -1;
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            System.err.println("Get doctor ID failed: " + e.getMessage());
        }
        return -1;
    }
}
