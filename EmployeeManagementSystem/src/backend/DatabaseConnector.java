package backend;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnector {
    /**
     * Creates and returns a new database connection using credentials loaded
     * from {@code config.properties}.
     *
     * @return a {@link Connection} to the configured database,
     *         or {@code null} if the connection cannot be established
     */
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
