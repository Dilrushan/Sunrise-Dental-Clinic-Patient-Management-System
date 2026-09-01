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
    private static String loggedInUser;
    private static String userRole;

    public static void setSession(String username, String role) {
        loggedInUser = username;
        userRole = role;
    }

    public static String getLoggedInUser() { return loggedInUser; }
    public static String getUserRole() { return userRole; }
    
    public static void clearSession() {
        loggedInUser = null;
        userRole = null;
    }
}
