package Controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StaffControllerTest {

    @Test
    public void testUsernameTooShort() {
        StaffController controller = new StaffController();
        String result = controller.registerStaff("usr", "password123", "password123", "Jane Doe", "Receptionist");
        assertEquals("Username must be at least 5 characters long.", result);
    }

    @Test
    public void testUsernameNull() {
        StaffController controller = new StaffController();
        String result = controller.registerStaff(null, "password123", "password123", "Jane Doe", "Receptionist");
        assertEquals("Username must be at least 5 characters long.", result);
    }

    @Test
    public void testPasswordTooShort() {
        StaffController controller = new StaffController();
        String result = controller.registerStaff("validUser", "short", "short", "Jane Doe", "Receptionist");
        assertEquals("Password must be at least 8 characters long.", result);
    }

    @Test
    public void testPasswordNull() {
        StaffController controller = new StaffController();
        String result = controller.registerStaff("validUser", null, "password123", "Jane Doe", "Receptionist");
        assertEquals("Password must be at least 8 characters long.", result);
    }

    @Test
    public void testPasswordsDoNotMatch() {
        StaffController controller = new StaffController();
        String result = controller.registerStaff("validUser", "password123", "different123", "Jane Doe", "Receptionist");
        assertEquals("Passwords do not match!", result);
    }

    @Test
    public void testFullNameEmpty() {
        StaffController controller = new StaffController();
        String result = controller.registerStaff("validUser", "password123", "password123", "", "Receptionist");
        assertEquals("Full name is required.", result);
    }

    @Test
    public void testFullNameNull() {
        StaffController controller = new StaffController();
        String result = controller.registerStaff("validUser", "password123", "password123", null, "Receptionist");
        assertEquals("Full name is required.", result);
    }
}
