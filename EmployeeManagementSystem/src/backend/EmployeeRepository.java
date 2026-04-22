package backend;

import java.sql.*;
import java.util.*;

public class EmployeeRepository {
    private IDataSource dbConnector;

    //Search methods

    /**
    * Retrieves an employee from the database by their unique employee ID.
    *
    * @param emp_ID the unique identifier of the employee to find
    * @return an {@link Employee} or {@link HRAdmin} object if found, or {@code null} if no match exists
    */
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

    /**
    * Retrieves an employee from the database by their full name.
    *
    * @param name the full name of the employee to find
    * @return an {@link Employee} or {@link HRAdmin} object if found, or {@code null} if no match exists
    */
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

    /**
    * Retrieves an employee from the database by their date of birth.
    *
    * @param dob the date of birth of the employee to find
    * @return an {@link Employee} or {@link HRAdmin} object if found, or {@code null} if no match exists
    */
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

    /**
    * Retrieves an employee from the database by their Social Security Number (SSN).
    *
    * @param ssn the SSN of the employee to find
    * @return an {@link Employee} or {@link HRAdmin} object if found, or {@code null} if no match exists
    */

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

    /**
    * Retrieves all employees from the database.
    *
    * @return a list of {@link Employee} and {@link HRAdmin} objects
    */
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
    /**
    * Saves a new employee to the database.
    *
    * @param employee the {@link Employee} or {@link HRAdmin} object to save
    */
    public void save(Employee employee) {
        try(Connection conn = dbConnector.getConnection()){
            String query = "INSERT INTO employees (first_name, last_name, email, hire_date, salary, ssn, address_id, dob, phone, emergency_contact_name, emergency_contact_phone) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getEmail());
            stmt.setString(4, employee.getEmploymentDate());
            stmt.setDouble(5, employee.getSalary());
            stmt.setString(6, employee.getSSN());
            stmt.setInt(7, employee.getAddressID());
            stmt.setString(8, employee.getDob());
            stmt.setString(9, employee.getPhoneNumber());
            stmt.setString(10, employee.getEmergencyContactName());
            stmt.setString(11, employee.getEmergencyContactPhone());
        
            stmt.executeUpdate();
            System.out.println("Employee saved successfully");
        } catch (Exception e) {
            System.out.println("Error saving employee");
            e.printStackTrace();
        }
    }
    
    
    /**
    * Updates an existing employee in the database.
    *
    * @param employee the {@link Employee} or {@link HRAdmin} object to update
    */

    public void update(Employee employee) {
        try(Connection conn = dbConnector.getConnection()){
            String query = "UPDATE employees SET first_name = ?, last_name = ?, email = ?, hire_date = ?, salary = ?, ssn = ?, address_id = ?, dob = ?, phone = ?, emergency_contact_name = ?, emergency_contact_phone = ? " +
                           "WHERE emp_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getEmail());
            stmt.setString(4, employee.getEmploymentDate());
            stmt.setDouble(5, employee.getSalary());
            stmt.setString(6, employee.getSSN());
            stmt.setInt(7, employee.getAddressID());
            stmt.setString(8, employee.getDob());
            stmt.setString(9, employee.getPhoneNumber());
            stmt.setString(10, employee.getEmergencyContactName());
            stmt.setString(11, employee.getEmergencyContactPhone());
            stmt.setInt(12, employee.getEmpID());
        
            stmt.executeUpdate();
            System.out.println("Employee updated successfully");
        } catch (Exception e) {
            System.out.println("Error updating employee with ID " + employee.getEmpID());
            e.printStackTrace();
        }
    }

    /**
    * Deletes an employee record from the database by their unique employee ID.
    *
    * @param emp_ID the unique identifier of the employee to delete
    */
    public void delete(int emp_ID) {
        try(Connection conn = dbConnector.getConnection()){
            String query = "DELETE FROM employees WHERE emp_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, emp_ID);
            stmt.executeUpdate();
            System.out.println("Employee with ID " + emp_ID + " deleted successfully");
        } catch (Exception e) {
            System.out.println("Error deleting employee with ID " + emp_ID);
            e.printStackTrace();
        }
    }

    //Utility methods

    /**
    * Checks whether an employee with the given ID exists in the database.
    *
    * @param emp_ID the unique identifier of the employee to check
    * @return {@code true} if an employee with the given ID exists, {@code false} otherwise
    */
    public boolean exists(int emp_ID) {
        try(Connection conn = dbConnector.getConnection()){
            String query = "SELECT 1 FROM employees WHERE emp_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, emp_ID);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("Error checking if employee exists");
            e.printStackTrace();
            return false;
        }
    }

    /**
    * Returns the total number of employee records in the database.
    *
    * @return the count of employees, or {@code 0} if no records exist or an error occurs
    */
    public int count() {
        try(Connection conn = dbConnector.getConnection()){
            String query = "SELECT COUNT(*) FROM employees";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Error counting employees");
            e.printStackTrace();
        }
        return 0;
    }
}
