package db;
import java.sql.Connection;
import java.sql.DriverManager;
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
