
import java.sql.*;
import java.util.*;

public class EmployeeRepository {
    private DatabaseConnector dbConnector;

    //Search methods
    public Employee findByID(int empID) {
        //Queries the database for an employee with the given ID and returns an Employee object
        try(Connection conn = dbConnector.getConnection()){
            String query = "SELECT Fname, Lname, classification FROM employees WHERE empID = " + empID;
            PreparedStatement stmt = conn.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                String Fname = rs.getString("Fname");
                String Lname = rs.getString("Lname");
                String classification = rs.getString("classification");
                int roleID = classification.equals("Admin") ? 1 : 0;
                if (roleID == 1) {
                    return new HRAdmin(roleID, empID, Fname, Lname);
                } else {
                    return new Employee(roleID, empID, Fname, Lname);
                }
            } else {
                System.out.println("Employee with ID " + empID + " not found.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error retrieving employee with ID " + empID);
            e.printStackTrace();
            return null;
        }

    }

    public Employee findByName(String name) {
        //Queries the database for an employee with the given name and returns an Employee object
        try(Connection conn = dbConnector.getConnection()){
            String query = "SELECT empID, Fname, Lname, classification FROM employees WHERE CONCAT(Fname, ' ', Lname) = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                int empID = rs.getInt("empID");
                String Fname = rs.getString("Fname");
                String Lname = rs.getString("Lname");
                String classification = rs.getString("classification");
                int roleID = classification.equals("Admin") ? 1 : 0;
                if (roleID == 1) {
                    return new HRAdmin(roleID, empID, Fname, Lname);
                } else {
                    return new Employee(roleID, empID, Fname, Lname);
                }
            } else {
                System.out.println("Employee with name " + name + " not found.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error retrieving employee with name " + name);
            e.printStackTrace();
            return null;
        }
        
    }

    public Employee findByDateOfBirth(String dob) {
        //Queries the database for an employee with the given date of birth and returns an Employee object
        return null;
    }

    public Employee findBySSN(String ssn) {
        //Queries the database for an employee with the given SSN and returns an Employee object
        return null;
    }

    public List<Employee> findAll() {
        //Queries the database for all employees and returns a list of Employee objects
        return null;
    }




    //CRUD methods for HRAdmin
    public void save(Employee employee) {
        //Saves a new employee to the database
    }

    public void update(Employee employee) {
        //Updates an existing employee's information in the database
    }

    public void delete(int empID) {
        //Deletes an employee from the database based on their ID
    }

    //Utility methods

    public boolean exists(int empID) {
        //Checks if an employee with the given ID exists in the database
        return false;
    }
    
    public int count() {
        //Returns the total number of employees in the database
        return 0;
    }
}
