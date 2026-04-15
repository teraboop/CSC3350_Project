import java.security.MessageDigest;
import java.sql.*;
import java.util.Base64;

public class Authorization {
    private DatabaseConnector dbConnector;
    private Employee currentUser;
    
    public Authorization(DatabaseConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
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
            String query = "SELECT empid, password_hash FROM credentials WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if(!rs.next()) {
                System.out.println("Username not found.");
                return null;
            }

            String storedHash = rs.getString("password_hash");
            int empID = rs.getInt("empid");

            if (!verifyPassword(password, storedHash)) {
                System.out.println("Incorrect password.");
                return null;
            }

            Employee user = fetchEmployee(empID);
            this.currentUser = user;
            
            System.out.println("Login successful. Welcome, " + username + "!");
            return user;
        } catch (Exception e) {
            System.out.println("Login failed due to an error.");
            e.printStackTrace();
            return null;
        }
    }

    private Employee fetchEmployee(int empID) throws SQLException {
        try(Connection conn = dbConnector.getConnection()){
            String query = "SELECT e.empid, e.Fname, e.Lname, c.classification FROM employees e " +
                        "JOIN credentials c ON e.empid = c.empid WHERE e.empid = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, empID);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String classification = rs.getString("classification");
                // Convert ENUM string to roleID (Admin = 1, Employee = 0)
                int roleID = classification.equals("Admin") ? 1 : 0;
                Employee emp = new Employee(roleID,empID);
                return emp;
            }
        }
        catch(Exception e){
            return null;
        }
        return null; 
    }

    public Employee logout() {

        if (currentUser != null) {
            System.out.println("User with empID " + currentUser.getEmpID() + " logged out.");
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
