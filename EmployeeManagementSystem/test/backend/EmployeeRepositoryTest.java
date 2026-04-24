package backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.*;
import java.util.List;

public class EmployeeRepositoryTest {

    private IDataSource mockConnector;
    private Connection mockConn;
    private PreparedStatement mockStmt;
    private ResultSet mockRs;
    private EmployeeRepository repo;

    @BeforeEach
    public void setUp() throws Exception {
        mockConnector = mock(IDataSource.class);
        mockConn = mock(Connection.class);
        mockStmt = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);

        when(mockConnector.getConnection()).thenReturn(mockConn);
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        repo = new EmployeeRepository(mockConnector);
    }

    /** Pass: update() executes against the DB without throwing. */
    @Test
    public void testUpdateBasicInfo_success() throws Exception {
        when(mockStmt.executeUpdate()).thenReturn(1);

        Employee emp = new Employee(0, 10, "Jane", "Doe");
        emp.setEmail("jane@example.com");
        emp.setSalary(55000.0);

        assertDoesNotThrow(() -> repo.update(emp));
        verify(mockStmt).executeUpdate();
    }

    /** Pass: updateSalariesInRange applies a percentage increase to qualifying employees. */
    @Test
    public void testUpdateSalary_withinRange() throws Exception {
        // SELECT returns one employee with salary 40000
        PreparedStatement mockSelectStmt = mock(PreparedStatement.class);
        PreparedStatement mockUpdateStmt = mock(PreparedStatement.class);
        ResultSet mockSelectRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(contains("SELECT"))).thenReturn(mockSelectStmt);
        when(mockConn.prepareStatement(contains("UPDATE"))).thenReturn(mockUpdateStmt);
        when(mockSelectStmt.executeQuery()).thenReturn(mockSelectRs);
        when(mockSelectRs.next()).thenReturn(true, false);
        when(mockSelectRs.getInt("emp_id")).thenReturn(5);
        when(mockSelectRs.getString("first_name")).thenReturn("Alice");
        when(mockSelectRs.getString("last_name")).thenReturn("Smith");
        when(mockSelectRs.getDouble("salary")).thenReturn(40000.0);
        when(mockUpdateStmt.executeUpdate()).thenReturn(1);

        List<Employee> updated = repo.updateSalariesInRange(0, 60000, 10.0);

        assertFalse(updated.isEmpty(), "Expected at least one employee to be updated");
        assertEquals(44000.0, updated.get(0).getSalary(), 0.01,
                "Salary should be increased by 10%");
    }

    /** Pass: employees above the threshold are not in the updated list. */
    @Test
    public void testUpdateSalary_aboveThresholdUnchanged() throws Exception {
        PreparedStatement mockSelectStmt = mock(PreparedStatement.class);
        PreparedStatement mockUpdateStmt = mock(PreparedStatement.class);
        ResultSet mockSelectRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(contains("SELECT"))).thenReturn(mockSelectStmt);
        when(mockConn.prepareStatement(contains("UPDATE"))).thenReturn(mockUpdateStmt);
        when(mockSelectStmt.executeQuery()).thenReturn(mockSelectRs);
        // No employees fall in range 0–30000
        when(mockSelectRs.next()).thenReturn(false);
        when(mockUpdateStmt.executeUpdate()).thenReturn(0);

        List<Employee> updated = repo.updateSalariesInRange(0, 30000, 10.0);

        assertTrue(updated.isEmpty(), "No employees should be updated when none fall in range");
    }

    /** Pass: system reports no employees updated when no salary matches the condition. */
    @Test
    public void testUpdateSalary_noEmployeeMeetsCondition() throws Exception {
        PreparedStatement mockSelectStmt = mock(PreparedStatement.class);
        PreparedStatement mockUpdateStmt = mock(PreparedStatement.class);
        ResultSet mockSelectRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(contains("SELECT"))).thenReturn(mockSelectStmt);
        when(mockConn.prepareStatement(contains("UPDATE"))).thenReturn(mockUpdateStmt);
        when(mockSelectStmt.executeQuery()).thenReturn(mockSelectRs);
        when(mockSelectRs.next()).thenReturn(false);
        when(mockUpdateStmt.executeUpdate()).thenReturn(0);

        List<Employee> updated = repo.updateSalariesInRange(200000, 300000, 5.0);

        assertTrue(updated.isEmpty(), "Result list should be empty when no employees match");
    }

    /** Pass: system rejects zero percentage and does not update any salary. */
    @Test
    public void testUpdateSalary_zeroPercent_noChange() throws Exception {
        PreparedStatement mockSelectStmt = mock(PreparedStatement.class);
        PreparedStatement mockUpdateStmt = mock(PreparedStatement.class);
        ResultSet mockSelectRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(contains("SELECT"))).thenReturn(mockSelectStmt);
        when(mockConn.prepareStatement(contains("UPDATE"))).thenReturn(mockUpdateStmt);
        when(mockSelectStmt.executeQuery()).thenReturn(mockSelectRs);
        when(mockSelectRs.next()).thenReturn(true, false);
        when(mockSelectRs.getInt("emp_id")).thenReturn(7);
        when(mockSelectRs.getString("first_name")).thenReturn("Bob");
        when(mockSelectRs.getString("last_name")).thenReturn("Jones");
        when(mockSelectRs.getDouble("salary")).thenReturn(50000.0);
        when(mockUpdateStmt.executeUpdate()).thenReturn(1);

        List<Employee> updated = repo.updateSalariesInRange(0, 60000, 0.0);

        // Salary * (1 + 0/100) = unchanged
        assertFalse(updated.isEmpty());
        assertEquals(50000.0, updated.get(0).getSalary(), 0.01,
                "Salary should not change when percentage is zero");
    }

    /** Pass: update() on a nonexistent employee does not crash the system. */
    @Test
    public void testUpdate_nonexistentEmployee_noException() throws Exception {
        when(mockStmt.executeUpdate()).thenReturn(0); // 0 rows affected

        Employee emp = new Employee(0, 9999, "Ghost", "User");
        assertDoesNotThrow(() -> repo.update(emp));
    }

    /** Pass: findByID returns the correct employee when the ID exists. */
    @Test
    public void testFindByID_existingEmployee() throws Exception {
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("emp_id")).thenReturn(42);
        when(mockRs.getString("first_name")).thenReturn("Jane");
        when(mockRs.getString("last_name")).thenReturn("Doe");
        when(mockRs.getString("classification")).thenReturn("Employee");

        Employee result = repo.findByID(42);

        assertNotNull(result, "findByID should return an employee when ID exists");
        assertEquals(42, result.getEmpID());
        assertEquals("Jane", result.getFirstName());
    }

    /** Pass: findByID returns null and displays error when the ID does not exist. */
    @Test
    public void testFindByID_nonexistentEmployee_returnsNull() throws Exception {
        when(mockRs.next()).thenReturn(false);

        Employee result = repo.findByID(9999);

        assertNull(result, "findByID should return null for a nonexistent ID");
    }

    /** Pass: exists() returns false for an invalid/blank-equivalent ID (0). */
    @Test
    public void testExists_invalidID_returnsFalse() throws Exception {
        when(mockRs.next()).thenReturn(false);

        boolean result = repo.exists(0);

        assertFalse(result, "exists() should return false for ID 0");
    }

    /** Pass: exists() returns true when the employee record is present. */
    @Test
    public void testExists_validID_returnsTrue() throws Exception {
        when(mockRs.next()).thenReturn(true);

        boolean result = repo.exists(5);

        assertTrue(result, "exists() should return true when employee is found");
    }

    /** Pass: delete() executes without error for an existing employee. */
    @Test
    public void testDelete_existingEmployee_noException() throws Exception {
        when(mockStmt.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> repo.delete(5));
        verify(mockStmt).executeUpdate();
    }

    /** Pass: delete() does not crash when called with a nonexistent ID. */
    @Test
    public void testDelete_nonexistentEmployee_noException() throws Exception {
        when(mockStmt.executeUpdate()).thenReturn(0); // 0 rows affected

        assertDoesNotThrow(() -> repo.delete(9999));
    }

    /** Pass: all employees with salary below the threshold are updated by the correct percentage. */
    @Test
    public void testUpdateSalariesBelowThreshold_correctCalculation() throws Exception {
        PreparedStatement mockSelectStmt = mock(PreparedStatement.class);
        PreparedStatement mockUpdateStmt = mock(PreparedStatement.class);
        ResultSet mockSelectRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(contains("SELECT"))).thenReturn(mockSelectStmt);
        when(mockConn.prepareStatement(contains("UPDATE"))).thenReturn(mockUpdateStmt);
        when(mockSelectStmt.executeQuery()).thenReturn(mockSelectRs);
        when(mockSelectRs.next()).thenReturn(true, true, false);
        // First employee: salary 30000
        when(mockSelectRs.getInt("emp_id")).thenReturn(1, 2);
        when(mockSelectRs.getString("first_name")).thenReturn("Alice", "Bob");
        when(mockSelectRs.getString("last_name")).thenReturn("A", "B");
        when(mockSelectRs.getDouble("salary")).thenReturn(30000.0, 45000.0);
        when(mockUpdateStmt.executeUpdate()).thenReturn(2);

        // Update salaries for employees earning less than $50,000 by 15%
        List<Employee> updated = repo.updateSalariesInRange(0, 49999, 15.0);

        assertEquals(2, updated.size(), "Both employees should be in the updated list");
        assertEquals(34500.0, updated.get(0).getSalary(), 0.01, "30000 * 1.15 = 34500");
        assertEquals(51750.0, updated.get(1).getSalary(), 0.01, "45000 * 1.15 = 51750");
    }

    /** Pass: findBySalaryRange with a range above threshold returns only qualifying employees. */
    @Test
    public void testFindBySalaryRange_returnsMatchingEmployees() throws Exception {
        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getInt("emp_id")).thenReturn(3);
        when(mockRs.getString("first_name")).thenReturn("Carol");
        when(mockRs.getString("last_name")).thenReturn("C");
        when(mockRs.getDouble("salary")).thenReturn(25000.0);

        List<Employee> result = repo.findBySalaryRange(0, 49999);

        assertEquals(1, result.size());
        assertEquals(25000.0, result.get(0).getSalary(), 0.01);
    }

    /** Pass: negative percentage input does not crash the system (salary decreases). */
    @Test
    public void testUpdateSalary_negativePercent_salaryDecreases() throws Exception {
        PreparedStatement mockSelectStmt = mock(PreparedStatement.class);
        PreparedStatement mockUpdateStmt = mock(PreparedStatement.class);
        ResultSet mockSelectRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(contains("SELECT"))).thenReturn(mockSelectStmt);
        when(mockConn.prepareStatement(contains("UPDATE"))).thenReturn(mockUpdateStmt);
        when(mockSelectStmt.executeQuery()).thenReturn(mockSelectRs);
        when(mockSelectRs.next()).thenReturn(true, false);
        when(mockSelectRs.getInt("emp_id")).thenReturn(4);
        when(mockSelectRs.getString("first_name")).thenReturn("Dan");
        when(mockSelectRs.getString("last_name")).thenReturn("D");
        when(mockSelectRs.getDouble("salary")).thenReturn(60000.0);
        when(mockUpdateStmt.executeUpdate()).thenReturn(1);

        List<Employee> updated = repo.updateSalariesInRange(0, 70000, -10.0);

        assertFalse(updated.isEmpty());
        assertEquals(54000.0, updated.get(0).getSalary(), 0.01, "60000 * 0.90 = 54000");
    }
}
