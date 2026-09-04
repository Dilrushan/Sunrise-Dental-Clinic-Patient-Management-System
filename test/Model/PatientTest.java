package Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PatientTest {

    @Test
    public void testParameterizedConstructor() {
        Patient p = new Patient(1, "John Doe", "Colombo", "0712345678", "john@mail.com", "Cavity filling");
        assertEquals(1, p.getPatientId());
        assertEquals("John Doe", p.getFullName());
        assertEquals("Colombo", p.getAddress());
        assertEquals("0712345678", p.getContactNumber());
        assertEquals("john@mail.com", p.getEmail());
        assertEquals("Cavity filling", p.getTreatmentHistory());
    }

    @Test
    public void testDefaultConstructor() {
        Patient p = new Patient();
        assertEquals(0, p.getPatientId());
        assertNull(p.getFullName());
        assertNull(p.getAddress());
        assertNull(p.getContactNumber());
        assertNull(p.getEmail());
        assertNull(p.getTreatmentHistory());
    }

    @Test
    public void testSetters() {
        Patient p = new Patient();
        p.setPatientId(5);
        p.setFullName("Jane Doe");
        p.setAddress("Kandy");
        p.setContactNumber("0771234567");
        p.setEmail("jane@mail.com");
        p.setTreatmentHistory("Root canal");
        assertEquals(5, p.getPatientId());
        assertEquals("Jane Doe", p.getFullName());
        assertEquals("Kandy", p.getAddress());
        assertEquals("0771234567", p.getContactNumber());
        assertEquals("jane@mail.com", p.getEmail());
        assertEquals("Root canal", p.getTreatmentHistory());
    }
}
