package Controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppointmentControllerTest {

    @Test
    public void testAddNewPatientWithEmptyName() {
        AppointmentController controller = new AppointmentController();
        boolean result = controller.addNewPatientAndAppointment("", "addr", "0712345678", "e@mail.com",
                "history", 1, "2026-09-10", "Consultation");
        assertFalse(result);
    }

    @Test
    public void testAddNewPatientWithEmptyContact() {
        AppointmentController controller = new AppointmentController();
        boolean result = controller.addNewPatientAndAppointment("John Doe", "addr", "", "e@mail.com",
                "history", 1, "2026-09-10", "Consultation");
        assertFalse(result);
    }

    @Test
    public void testBookExistingPatientWithEmptyName() {
        AppointmentController controller = new AppointmentController();
        boolean result = controller.bookAppointmentForExistingPatient("", "0712345678", 1, "2026-09-10", "Consultation");
        assertFalse(result);
    }

    @Test
    public void testBookExistingPatientWithInvalidDoctorId() {
        AppointmentController controller = new AppointmentController();
        boolean result = controller.bookAppointmentForExistingPatient("John Doe", "0712345678", 0, "2026-09-10", "Consultation");
        assertFalse(result);
    }

    @Test
    public void testBookExistingPatientWithEmptyDate() {
        AppointmentController controller = new AppointmentController();
        boolean result = controller.bookAppointmentForExistingPatient("John Doe", "0712345678", 1, "", "Consultation");
        assertFalse(result);
    }
}
