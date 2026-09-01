/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author HP
 */
public class UserSession {
    private static UserSession instance;
    private int userId;
    private String username;
    private String fullName;
    private String role;

    private UserSession(int userId, String username, String fullName, String role) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
    }

    public static void createSession(int userId, String username, String fullName, String role) {
        instance = new UserSession(userId, username, fullName, role);
    }

    public static UserSession getSession() {
        return instance;
    }

    public static void clearSession() {
        instance = null;
    }

    public int getUserId() {
        return userId; 
    }
    public String getUsername() {
        return username; 
    }
    public String getFullName() {
        return fullName; 
    }
    public String getRole() {
        return role; 
    }
}
