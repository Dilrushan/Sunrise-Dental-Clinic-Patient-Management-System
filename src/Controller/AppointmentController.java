/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import dao.PatientDAO;
import dao.AppointmentDAO;
import Model.Patient;
import Model.Appointment;
/**
 *
 * @author HP
 */
public class AppointmentController {
    private PatientDAO patientDAO;
    private AppointmentDAO appointmentDAO;

    public AppointmentController() {
        this.patientDAO = new PatientDAO();
        this.appointmentDAO = new AppointmentDAO();
    }

    public boolean addNewPatientAndAppointment(String name, String address, String contact, String email, 
                                               String history, int patientId, int doctorId, 
                                               String date, String treatmentType) {
        if (name.isEmpty() || contact.isEmpty()) {
            return false;
        }
        
        Patient patient = new Patient(0, name, address, contact, email, history);
        boolean patientSaved = patientDAO.registerPatient(patient);
        
        if (patientSaved) {
            Appointment appointment = new Appointment(0, patientId, doctorId, date, treatmentType, "Scheduled");
            return appointmentDAO.scheduleAppointment(appointment);
        }
        return false;
    }
}
