/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import Model.User;
import dao.UserDAO;
import Model.UserSession;
import View.AdminDashboard;
import View.DoctorDashboard;
import View.LoginForm;
import View.ReceptionistDashboard;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class LoginController {
    private final UserDAO userDAO;

    public LoginController() {
        this.userDAO = new UserDAO();
    }

    public void authenticateUser(String username, String password, JFrame currentFrame) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(currentFrame, "Please enter both username and password.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = userDAO.login(username, password);

        if (user != null) {
            // Create global session
            UserSession.createSession(user.getUserId(), user.getUsername(), user.getFullName(), user.getRole());
            
            JOptionPane.showMessageDialog(currentFrame, "Login Successful! Welcome, " + user.getFullName());
            currentFrame.dispose(); // Close login window

            // Open respective dashboard based on role
            switch (user.getRole()) {
                case "Admin":
                    new AdminDashboard().setVisible(true);
                    break;
                case "Receptionist":
                    new ReceptionistDashboard().setVisible(true);
                    break;
                case "Doctor":
                    new DoctorDashboard().setVisible(true);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Unknown user role assigned.");
            }
        } else {
            JOptionPane.showMessageDialog(currentFrame, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
