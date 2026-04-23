package backend;

import java.sql.*;
import java.util.*;

public class EmployeeRepository {
    private IDataSource dbConnector;

    // Default constructor ensures a DatabaseConnector is available
    public EmployeeRepository() {
        this.dbConnector = new DatabaseConnector();
    }

    // Helper to map a result row to an Employee (or HRAdmin)
    private Employee mapRowToEmployee(ResultSet rs) throws SQLException {
        int emp_id = rs.getInt("emp_id");
        String first_name = rs.getString("first_name");
        String last_name = rs.getString("last_name");
        String classification = null;
        try { classification = rs.getString("classification"); } catch(Exception ignore) {}
        int roleID = (classification != null && classification.equalsIgnoreCase("Admin")) ? 1 : 0;

        Employee e;
        if (roleID == 1) e = new HRAdmin(roleID, emp_id, first_name, last_name);
        else e = new Employee(roleID, emp_id, first_name, last_name);

        // Populate additional fields if present in the ResultSet
        try { e.setEmail(rs.getString("email")); } catch(Exception ignore) {}
        try { e.setEmploymentDate(rs.getString("hire_date")); } catch(Exception ignore) {}
        try { e.setSalary(rs.getDouble("salary")); } catch(Exception ignore) {}
        try { e.setSSN(rs.getString("ssn")); } catch(Exception ignore) {}
        try { e.setAddressID(rs.getInt("address_id")); } catch(Exception ignore) {}
        try { e.setDob(rs.getString("dob")); } catch(Exception ignore) {}
        try { e.setPhoneNumber(rs.getString("phone")); } catch(Exception ignore) {}
        try { e.setEmergencyContactName(rs.getString("emergency_contact_name")); } catch(Exception ignore) {}
        try { e.setEmergencyContactPhone(rs.getString("emergency_contact_phone")); } catch(Exception ignore) {}

        return e;
    }

    //Search methods

    /**
    * Retrieves an employee from the database by their unique employee ID.
    *
    * @param emp_ID the unique identifier of the employee to find
    * @return an {@link Employee} or {@link HRAdmin} object if found, or {@code null} if no match exists
    */
    public Employee findByID(int emp_ID) {
        try (Connection conn = dbConnector.getConnection()) {
            String query = "SELECT e.*, c.classification FROM employees e LEFT JOIN credentials c ON e.emp_id = c.emp_id WHERE e.emp_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, emp_ID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToEmployee(rs);
                } else {
                    System.out.println("Employee with ID " + emp_ID + " not found.");
                    return null;
                }
            }
        } catch (Exception ex) {
            System.out.println("Error retrieving employee with ID " + emp_ID);
            ex.printStackTrace();
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
        try (Connection conn = dbConnector.getConnection()) {
            String query = "SELECT e.*, c.classification FROM employees e LEFT JOIN credentials c ON e.emp_id = c.emp_id WHERE CONCAT(e.first_name, ' ', e.last_name) = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRowToEmployee(rs);
                System.out.println("Employee with name " + name + " not found.");
                return null;
            }
        } catch (Exception ex) {
            System.out.println("Error retrieving employee with name " + name);
            ex.printStackTrace();
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
        try (Connection conn = dbConnector.getConnection()) {
            String query = "SELECT e.*, c.classification FROM employees e LEFT JOIN credentials c ON e.emp_id = c.emp_id WHERE e.dob = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, dob);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRowToEmployee(rs);
                System.out.println("Employee with date of birth " + dob + " not found.");
                return null;
            }
        } catch (Exception ex) {
            System.out.println("Error retrieving employee with date of birth " + dob);
            ex.printStackTrace();
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
        try (Connection conn = dbConnector.getConnection()) {
            String query = "SELECT e.*, c.classification FROM employees e LEFT JOIN credentials c ON e.emp_id = c.emp_id WHERE e.ssn = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, ssn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRowToEmployee(rs);
                System.out.println("Employee with SSN " + ssn + " not found.");
                return null;
            }
        } catch (Exception ex) {
            System.out.println("Error retrieving employee with SSN " + ssn);
            ex.printStackTrace();
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
        try (Connection conn = dbConnector.getConnection()) {
            String query = "SELECT e.*, c.classification FROM employees e LEFT JOIN credentials c ON e.emp_id = c.emp_id";
            Statement stmt = conn.createStatement();
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    employees.add(mapRowToEmployee(rs));
                }
            }
        } catch (Exception ex) {
            System.out.println("Error retrieving all employees");
            ex.printStackTrace();
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

    /**
     * Updates the salary of all employees whose current salary falls within
     * [{@code lowerBound}, {@code upperBound}] (inclusive) by applying a
     * percentage adjustment, then returns the list of affected employees with
     * their new salaries reflected.
     *
     * @param lowerBound      the minimum salary of the range to adjust
     * @param upperBound      the maximum salary of the range to adjust
     * @param adjustPercent   the percentage to adjust salaries by (e.g. 10.0 = +10%, -5.0 = -5%)
     * @return a {@link List} of {@link Employee} objects whose salaries were updated,
     *         or an empty list if none matched or an error occurred
     */
    public List<Employee> updateSalariesInRange(double lowerBound, double upperBound, double adjustPercent) {
        List<Employee> updated = new ArrayList<>();
        try (Connection conn = dbConnector.getConnection()) {
            // First, collect all employees in range
            String selectQuery = "SELECT e.emp_id, e.first_name, e.last_name, e.email, e.hire_date, " +
                                 "e.salary, e.ssn, e.address_id, e.dob, e.phone, " +
                                 "e.emergency_contact_name, e.emergency_contact_phone " +
                                 "FROM employees e WHERE e.salary >= ? AND e.salary <= ?";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                selectStmt.setDouble(1, lowerBound);
                selectStmt.setDouble(2, upperBound);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    while (rs.next()) {
                        Employee e = mapRowToEmployee(rs);
                        double newSalary = e.getSalary() * (1 + adjustPercent / 100.0);
                        e.setSalary(newSalary);
                        updated.add(e);
                    }
                }
            }
            // Then batch-update all their salaries
            String updateQuery = "UPDATE employees SET salary = ROUND(salary * ?, 2) " +
                                 "WHERE salary >= ? AND salary <= ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setDouble(1, 1 + adjustPercent / 100.0);
                updateStmt.setDouble(2, lowerBound);
                updateStmt.setDouble(3, upperBound);
                updateStmt.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("Error updating salaries in range.");
            e.printStackTrace();
        }
        return updated;
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
