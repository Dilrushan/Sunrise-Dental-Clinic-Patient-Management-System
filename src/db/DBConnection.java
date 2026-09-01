/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;
import java.sql.Connection;
import java.sql.DriverManager;
/**
 *
 * @author HP
 */
public class DBConnection {
    public static Connection getConnection(){
         Connection con = null;
         try {

            con =  DriverManager.getConnection(
            "jdbc:mysql://localhost/sunrise_dental_clinic_db",
            "root", "");

            System.out.println("Database Connected");

        }
        catch(Exception e){

            System.out.println(e);

        }
        return con;
    }
}
