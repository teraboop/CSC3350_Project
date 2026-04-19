
import java.sql.*;
import java.util.*;

public class EmployeeRepository {
    private DatabaseConnector dbConnector;

    //Search methods
    public Employee findByID(int emp_ID) {
        //Queries the database for an employee with the given ID and returns an Employee object
        try(Connection conn = dbConnector.getConnection()){
            String query = "SELECT e.emp_id, e.first_name, e.last_name, c.classification " +
                       "FROM employees e " + "JOIN credentials c ON e.emp_id = c.emp_id " + "WHERE e.emp_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, emp_ID);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String classification = rs.getString("classification");
                int roleID = classification.equals("Admin") ? 1 : 0;
                if (roleID == 1) {
                    return new HRAdmin(roleID, emp_ID, first_name, last_name);
                } else {
                    return new Employee(roleID, emp_ID, first_name, last_name);
                }
            } else {
                System.out.println("Employee with ID " + emp_ID + " not found.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error retrieving employee with ID " + emp_ID);
            e.printStackTrace();
            return null;
        }

    }

    public Employee findByName(String name) {
        try(Connection conn = dbConnector.getConnection()){
            String query = "SELECT e.emp_id, e.first_name, e.last_name, c.classification " +
                           "FROM employees e " +
                           "JOIN credentials c ON e.emp_id = c.emp_id " +
                           "WHERE CONCAT(e.first_name, ' ', e.last_name) = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                int emp_id = rs.getInt("emp_id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String classification = rs.getString("classification");
                int roleID = classification.equals("Admin") ? 1 : 0;
                if (roleID == 1) {
                    return new HRAdmin(roleID, emp_id, first_name, last_name);
                } else {
                    return new Employee(roleID, emp_id, first_name, last_name);
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
        try(Connection conn = dbConnector.getConnection()){
            String query = "SELECT e.emp_id, e.first_name, e.last_name, c.classification " +
                           "FROM employees e " +
                           "JOIN credentials c ON e.emp_id = c.emp_id " +
                           "WHERE e.dob = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, dob);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                int emp_id = rs.getInt("emp_id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String classification = rs.getString("classification");
                int roleID = classification.equals("Admin") ? 1 : 0;
                if (roleID == 1) {
                    return new HRAdmin(roleID, emp_id, first_name, last_name);
                } else {
                    return new Employee(roleID, emp_id, first_name, last_name);
                }
            } else {
                System.out.println("Employee with date of birth " + dob + " not found.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error retrieving employee with date of birth " + dob);
            e.printStackTrace();
            return null;
        }
    }

    public Employee findBySSN(String ssn) {
        try(Connection conn = dbConnector.getConnection()){
            String query = "SELECT e.emp_id, e.first_name, e.last_name, c.classification " +
                           "FROM employees e " +
                           "JOIN credentials c ON e.emp_id = c.emp_id " +
                           "WHERE e.ssn = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, ssn);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                int emp_id = rs.getInt("emp_id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String classification = rs.getString("classification");
                int roleID = classification.equals("Admin") ? 1 : 0;
                if (roleID == 1) {
                    return new HRAdmin(roleID, emp_id, first_name, last_name);
                } else {
                    return new Employee(roleID, emp_id, first_name, last_name);
                }
            } else {
                System.out.println("Employee with SSN " + ssn + " not found.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error retrieving employee with SSN " + ssn);
            e.printStackTrace();
            return null;
        }
    }

    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        try(Connection conn = dbConnector.getConnection()){
            String query = "SELECT e.emp_id, e.first_name, e.last_name, c.classification " +
                           "FROM employees e " +
                           "JOIN credentials c ON e.emp_id = c.emp_id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
        
            while(rs.next()) {
                int emp_id = rs.getInt("emp_id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String classification = rs.getString("classification");
                int roleID = classification.equals("Admin") ? 1 : 0;
                if (roleID == 1) {
                    employees.add(new HRAdmin(roleID, emp_id, first_name, last_name));
                } else {
                    employees.add(new Employee(roleID, emp_id, first_name, last_name));
                }
            }
        } catch (Exception e) {
            System.out.println("Error retrieving all employees");
            e.printStackTrace();
        }
        return employees;
    }



    //CRUD methods for HRAdmin
    public void save(Employee employee) {
        //Saves a new employee to the database
    }

    public void update(Employee employee) {
        //Updates an existing employee's information in the database
    }

    public void delete(int emp_ID) {
        //Deletes an employee from the database based on their ID
    }

    //Utility methods

    public boolean exists(int emp_ID) {
        //Checks if an employee with the given ID exists in the database
        return false;
    }
    
    public int count() {
        //Returns the total number of employees in the database
        return 0;
    }
}
