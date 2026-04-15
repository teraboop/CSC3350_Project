import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnector {
    public Connection getConnection() {
        Properties prop = new Properties();
        
        // Load the file
        try (InputStream input = new FileInputStream("EmployeeManagementSystem/src/config.properties")) {
            prop.load(input);
            
            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.user");
            String pass = prop.getProperty("db.password");
            
            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
