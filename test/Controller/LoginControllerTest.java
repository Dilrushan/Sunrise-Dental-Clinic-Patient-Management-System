package Controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginControllerTest {

    @Test
    public void testAuthenticateWithNullUsername() {
        LoginController controller = new LoginController();
        assertNull(controller.authenticate(null, "password"));
    }

    @Test
    public void testAuthenticateWithEmptyUsername() {
        LoginController controller = new LoginController();
        assertNull(controller.authenticate("", "password"));
        assertNull(controller.authenticate("   ", "password"));
    }

    @Test
    public void testAuthenticateWithNullPassword() {
        LoginController controller = new LoginController();
        assertNull(controller.authenticate("admin", null));
    }

    @Test
    public void testAuthenticateWithEmptyPassword() {
        LoginController controller = new LoginController();
        assertNull(controller.authenticate("admin", ""));
        assertNull(controller.authenticate("admin", "   "));
    }
}
