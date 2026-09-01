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
    private UserDAO userDAO;

    public LoginController() {
        this.userDAO = new UserDAO();
    }

    public boolean authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }
        
        boolean isValid = userDAO.validateUser(username, password);
        if (isValid) {
            UserSession.setSession(username, "ActiveRole");
        }
        return isValid;
    }
}
