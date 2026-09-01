/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import dao.AppointmentDAO;
import dao.PatientDAO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 *
 * @author HP
 */
public class ReceptionistController {
    private final PatientDAO patientDAO;
    private final AppointmentDAO appointmentDAO;

    public ReceptionistController() {
        this.patientDAO = new PatientDAO();
        this.appointmentDAO = new AppointmentDAO();
    }

    public void registerPatient(String name, String address, String contact, String email, JFrame frame) {
        if (name.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Name and Contact Number are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = patientDAO.addPatient(name, address, contact, email);
        if (success) {
            JOptionPane.showMessageDialog(frame, "Patient registered successfully.");
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to register patient.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void bookAppointment(int patientId, int doctorId, String date, String treatment, JFrame frame) {
        boolean success = appointmentDAO.bookAppointment(patientId, doctorId, date, treatment);
        if (success) {
            JOptionPane.showMessageDialog(frame, "Appointment booked successfully!");
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to book appointment.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
