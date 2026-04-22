package backend;

import java.sql.*;
import java.util.*;

public class EmployeeRepository {
    private DatabaseConnector dbConnector;

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
