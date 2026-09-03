package Controller;

import Model.UserSession;
import dao.UserDAO;
import dao.UserDAO.User;

public class LoginController {
    private final UserDAO userDAO;

    public LoginController() {
        this.userDAO = new UserDAO();
    }

    public User authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }
        User user = userDAO.validateUser(username.trim(), password.trim());
        if (user != null) {
            UserSession.setSession(user.getUsername(), user.getRole());
        }
        return user;
    }
}
