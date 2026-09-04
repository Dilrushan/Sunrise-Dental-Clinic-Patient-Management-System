package Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppointmentTest {

    @Test
    public void testConstructor() {
        Appointment a = new Appointment(10, "John Doe", "0712345678", 3, "2026-09-10",
                "Consultation", "General Checkup", 2000.00, "Scheduled");
        assertEquals(10, a.getAppointmentId());
        assertEquals("John Doe", a.getPatientName());
        assertEquals("0712345678", a.getContactNo());
        assertEquals(3, a.getDoctorId());
        assertEquals("2026-09-10", a.getAppointmentDate());
        assertEquals("Consultation", a.getVisitType());
        assertEquals("General Checkup", a.getTreatmentPrescribed());
        assertEquals(2000.00, a.getFee());
        assertEquals("Scheduled", a.getStatus());
    }

    @Test
    public void testSetters() {
        Appointment a = new Appointment(0, null, null, 0, null, null, null, 0.0, null);
        a.setAppointmentId(7);
        a.setPatientName("Jane Doe");
        a.setContactNo("0771234567");
        a.setDoctorId(4);
        a.setAppointmentDate("2026-09-11");
        a.setVisitType("Treatment");
        a.setTreatmentPrescribed("Root Canal");
        a.setFee(1500.00);
        a.setStatus("Completed");
        assertEquals(7, a.getAppointmentId());
        assertEquals("Jane Doe", a.getPatientName());
        assertEquals("0771234567", a.getContactNo());
        assertEquals(4, a.getDoctorId());
        assertEquals("2026-09-11", a.getAppointmentDate());
        assertEquals("Treatment", a.getVisitType());
        assertEquals("Root Canal", a.getTreatmentPrescribed());
        assertEquals(1500.00, a.getFee());
        assertEquals("Completed", a.getStatus());
    }
}
