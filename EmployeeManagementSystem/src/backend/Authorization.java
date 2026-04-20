package backend;
import java.security.MessageDigest;
import java.sql.*;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class Authorization {
    private DatabaseConnector dbConnector;
    private Employee currentUser;
    
    public Authorization(DatabaseConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            System.out.println("Error hashing password.");
            e.printStackTrace();
            return null;
        }
    }

    private boolean verifyPassword(String inputPassword, String storedHash) {
        String inputHash = hashPassword(inputPassword);
        return inputHash != null && inputHash.equals(storedHash);
    }

    public Employee login(String username, String password) {
        try (Connection conn = dbConnector.getConnection()){
            String query = "SELECT emp_ID, password_hash FROM credentials WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if(!rs.next()) {
                System.out.println("Username not found.");
                return null;
            }

            String storedHash = rs.getString("password_hash");
            int emp_ID = rs.getInt("emp_ID");

            if (!verifyPassword(password, storedHash)) {
                System.out.println("Incorrect password.");
                return null;
            }

            Employee user = fetchEmployee(emp_ID);
            this.currentUser = user;
            
            System.out.println("Login successful. Welcome, " + username + "!");
            return user;
        } catch (Exception e) {
            System.out.println("Login failed due to an error.");
            e.printStackTrace();
            return null;
        }
    }

    private Employee fetchEmployee(int emp_ID) throws SQLException {
        try(Connection conn = dbConnector.getConnection()){
            String query = "SELECT e.emp_ID, e.first_name, e.last_name, c.classification FROM employees e " +
                        "JOIN credentials c ON e.emp_ID = c.emp_ID WHERE e.emp_ID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, emp_ID);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String classification = rs.getString("classification");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                // Convert ENUM string to roleID (Admin = 1, Employee = 0)
                int roleID = classification.equals("Admin") ? 1 : 0;
                if (roleID == 1) {
                    return new HRAdmin(roleID, emp_ID, first_name, last_name);
                } else {
                    return new Employee(roleID, emp_ID, first_name, last_name);
                }
            }
        }
        catch(Exception e){
            return null;
        }
        return null; 
    }

    public Employee logout() {

        if (currentUser != null) {
            System.out.println("User with emp_ID " + currentUser.getEmpID() + " logged out.");
            currentUser = null;
        } else {
            System.out.println("No user is currently logged in.");
        }
        return currentUser;
    }

    public Employee getCurrentUser() {
        return currentUser;
    }
    
}
