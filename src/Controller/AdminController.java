/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import dao.UserDAO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 *
 * @author HP
 */
public class AdminController {
    private final UserDAO userDAO;

    public AdminController() {
        this.userDAO = new UserDAO();
    }

    public void registerNewStaff(String username, String password, String fullName, String role, JFrame parentFrame) {
        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "All fields must be filled out.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = userDAO.registerStaff(username, password, fullName, role);
        if (success) {
            JOptionPane.showMessageDialog(parentFrame, "Staff member successfully registered!");
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Error registering staff. Username may already exist.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
