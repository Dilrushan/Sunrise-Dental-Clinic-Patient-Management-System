package Controller;

import dao.UserDAO;

public class StaffController {
    private final UserDAO userDAO;

    public StaffController() {
        this.userDAO = new UserDAO();
    }

    public String registerStaff(String username, String password, String confirmPassword, String fullName, String role) {
        if (username == null || username.trim().length() < 5) {
            return "Username must be at least 5 characters long.";
        }
        if (password == null || password.length() < 8) {
            return "Password must be at least 8 characters long.";
        }
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match!";
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            return "Full name is required.";
        }
        if (userDAO.isUsernameExists(username.trim())) {
            return "Username already exists. Please choose another.";
        }
        boolean success = userDAO.insertUser(username.trim(), password, fullName.trim(), role);
        return success ? "SUCCESS" : "Failed to register staff. Please check database connection.";
    }
}
