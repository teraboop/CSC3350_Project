import java.sql.*;

public class DatabaseConnector {
    private Connection conn;
    public DatabaseConnector(){
        /* Establish a connection to the database using JDBC. Handle any exceptions that may occur during the connection process. */
        String url = "jdbc:mysql://localhost:3306/SMS2";
        String user = "root";
        String password = "password";
        try {
            this.conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database successfully!");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        } finally {
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.out.println("Failed to close the database connection.");
                e.printStackTrace();
            }
        }
    }

}
