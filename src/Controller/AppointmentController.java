package Controller;

import dao.PatientDAO;
import dao.AppointmentDAO;
import Model.Patient;
import Model.Appointment;
import web.NotificationService;

public class AppointmentController {
    private PatientDAO patientDAO;
    private AppointmentDAO appointmentDAO;

    public AppointmentController() {
        this.patientDAO = new PatientDAO();
        this.appointmentDAO = new AppointmentDAO();
    }

    public boolean addNewPatientAndAppointment(String name, String address, String contact, String email,
                                               String history, int doctorId, String date, String visitType) {
        if (name.isEmpty() || contact.isEmpty()) {
            return false;
        }

        Patient patient = new Patient(0, name, address, contact, email, history);
        int newPatientId = patientDAO.registerPatient(patient);

        if (newPatientId > 0) {
            if (appointmentDAO.isDuplicateBooking(name, doctorId, date)) {
                return false;
            }
            Appointment appointment = new Appointment(0, name, contact, doctorId, date, visitType, null, 0.00, "Scheduled");
            boolean booked = appointmentDAO.scheduleAppointment(appointment);
            if (booked && email != null && !email.trim().isEmpty()) {
                String doctorName = appointmentDAO.getDoctorNameById(doctorId);
                NotificationService.sendBookingConfirmation(email, name, doctorName, date, visitType);
            }
            return booked;
        }
        return false;
    }

    public boolean bookAppointmentForExistingPatient(String patientName, String contact, int doctorId, String date, String visitType) {
        if (patientName.isEmpty() || contact.isEmpty() || doctorId <= 0 || date.isEmpty()) {
            return false;
        }
        if (appointmentDAO.isDuplicateBooking(patientName, doctorId, date)) {
            return false;
        }
        Appointment appointment = new Appointment(0, patientName, contact, doctorId, date, visitType, null, 0.00, "Scheduled");
        boolean booked = appointmentDAO.scheduleAppointment(appointment);
        if (booked) {
            Patient existingPatient = patientDAO.getPatientByName(patientName);
            if (existingPatient != null && existingPatient.getEmail() != null && !existingPatient.getEmail().trim().isEmpty()) {
                String doctorName = appointmentDAO.getDoctorNameById(doctorId);
                NotificationService.sendBookingConfirmation(existingPatient.getEmail(), patientName, doctorName, date, visitType);
            }
        }
        return booked;
    }

    public boolean isDuplicateBooking(String patientName, int doctorId, String date) {
        return appointmentDAO.isDuplicateBooking(patientName, doctorId, date);
    }
}
