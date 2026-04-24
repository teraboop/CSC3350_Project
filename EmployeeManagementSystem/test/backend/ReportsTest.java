package backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.*;

public class ReportsTest {

    private IDataSource mockConnector;
    private Connection mockConn;
    private PreparedStatement mockStmt;
    private ResultSet mockRs;
    private Reports reports;

    private Employee generalEmployee;
    private Employee adminEmployee;

    @BeforeEach
    public void setUp() throws Exception {
        mockConnector = mock(IDataSource.class);
        mockConn = mock(Connection.class);
        mockStmt = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);

        when(mockConnector.getConnection()).thenReturn(mockConn);
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        reports = new Reports(mockConnector);

        // General employee (classify = false)
        generalEmployee = new Employee(0, 1, "Alice", "Smith");

        // Admin employee (classify = true)
        adminEmployee = new HRAdmin(1, 2, "Bob", "Admin");
    }

    /** Pass: payroll records are retrieved and displayed for a valid employee. */
    @Test
    public void testGetPaymentInfo_validEmployee_returnsReport() throws Exception {
        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getDate(1)).thenReturn(Date.valueOf("2025-12-31"));
        when(mockRs.getFloat(2)).thenReturn(4000.0f);   // earnings
        when(mockRs.getFloat(3)).thenReturn(480.0f);    // fed_tax
        when(mockRs.getFloat(4)).thenReturn(58.0f);     // fed_med
        when(mockRs.getFloat(5)).thenReturn(248.0f);    // fed_SS
        when(mockRs.getFloat(6)).thenReturn(200.0f);    // state_tax
        when(mockRs.getFloat(7)).thenReturn(160.0f);    // 401k
        when(mockRs.getFloat(8)).thenReturn(100.0f);    // health_care

        String result = reports.getPaymentInfo(generalEmployee);

        assertNotNull(result, "Report should not be null for a valid employee with payroll records");
        assertTrue(result.contains("EARNINGS"), "Report header should contain EARNINGS column");
        assertTrue(result.contains("4000"), "Report should contain the employee's earnings");
    }

    /** Pass: system displays a message indicating no records when payroll history is empty. */
    @Test
    public void testGetPaymentInfo_noRecords_returnsNull() throws Exception {
        when(mockRs.next()).thenReturn(false);

        String result = reports.getPaymentInfo(generalEmployee);

        assertNull(result, "getPaymentInfo should return null when no payroll records exist");
    }

    /** Pass: report is generated with correct data when HR admin requests by job title. */
    @Test
    public void testGetMonthlyPayTitle_adminAccess_returnsReport() throws Exception {
        // First query: get emp_ids for titleID
        PreparedStatement mockEmpStmt = mock(PreparedStatement.class);
        PreparedStatement mockPayStmt = mock(PreparedStatement.class);
        ResultSet mockEmpRs = mock(ResultSet.class);
        ResultSet mockPayRs = mock(ResultSet.class);

        // Route the two prepareStatement calls by SQL content
        when(mockConn.prepareStatement(contains("employee_job_titles"))).thenReturn(mockEmpStmt);
        when(mockConn.prepareStatement(contains("payroll"))).thenReturn(mockPayStmt);
        when(mockEmpStmt.executeQuery()).thenReturn(mockEmpRs);
        when(mockPayStmt.executeQuery()).thenReturn(mockPayRs);

        // Employee IDs for title 1
        when(mockEmpRs.next()).thenReturn(true, false);
        when(mockEmpRs.getInt(1)).thenReturn(1);

        // Payroll row
        when(mockPayRs.next()).thenReturn(true, false);
        when(mockPayRs.getDate(1)).thenReturn(Date.valueOf("2025-11-30"));
        when(mockPayRs.getFloat(2)).thenReturn(3500.0f);
        when(mockPayRs.getFloat(3)).thenReturn(420.0f);
        when(mockPayRs.getFloat(4)).thenReturn(50.75f);
        when(mockPayRs.getFloat(5)).thenReturn(217.0f);
        when(mockPayRs.getFloat(6)).thenReturn(175.0f);
        when(mockPayRs.getFloat(7)).thenReturn(140.0f);
        when(mockPayRs.getFloat(8)).thenReturn(100.0f);

        String result = reports.getMonthlyPayTitle(1, 11, 2025, adminEmployee);

        assertNotNull(result, "Admin should receive a report for a valid job title and month");
        assertTrue(result.contains("3500"), "Report should contain the earnings value");
    }

    /** Pass: getMonthlyPayTitle returns null when the requester is not an HR admin. */
    @Test
    public void testGetMonthlyPayTitle_nonAdminAccess_returnsNull() {
        String result = reports.getMonthlyPayTitle(1, 11, 2025, generalEmployee);

        assertNull(result, "Non-admin should not receive a title-based payroll report");
    }

    /** Pass: report is generated with correct data when HR admin requests by division. */
    @Test
    public void testGetMonthlyPayDiv_adminAccess_returnsReport() throws Exception {
        PreparedStatement mockEmpStmt = mock(PreparedStatement.class);
        PreparedStatement mockPayStmt = mock(PreparedStatement.class);
        ResultSet mockEmpRs = mock(ResultSet.class);
        ResultSet mockPayRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(contains("employee_division"))).thenReturn(mockEmpStmt);
        when(mockConn.prepareStatement(contains("payroll"))).thenReturn(mockPayStmt);
        when(mockEmpStmt.executeQuery()).thenReturn(mockEmpRs);
        when(mockPayStmt.executeQuery()).thenReturn(mockPayRs);

        when(mockEmpRs.next()).thenReturn(true, false);
        when(mockEmpRs.getInt(1)).thenReturn(1);

        when(mockPayRs.next()).thenReturn(true, false);
        when(mockPayRs.getDate(1)).thenReturn(Date.valueOf("2025-10-31"));
        when(mockPayRs.getFloat(2)).thenReturn(5000.0f);
        when(mockPayRs.getFloat(3)).thenReturn(600.0f);
        when(mockPayRs.getFloat(4)).thenReturn(72.5f);
        when(mockPayRs.getFloat(5)).thenReturn(310.0f);
        when(mockPayRs.getFloat(6)).thenReturn(250.0f);
        when(mockPayRs.getFloat(7)).thenReturn(200.0f);
        when(mockPayRs.getFloat(8)).thenReturn(100.0f);

        String result = reports.getMonthlyPayDiv(1, 10, 2025, adminEmployee);

        assertNotNull(result, "Admin should receive a report for a valid division and month");
        assertTrue(result.contains("5000"), "Report should contain the earnings value");
    }

    /** Pass: getMonthlyPayDiv returns null when the requester is not an HR admin. */
    @Test
    public void testGetMonthlyPayDiv_nonAdminAccess_returnsNull() {
        String result = reports.getMonthlyPayDiv(1, 10, 2025, generalEmployee);

        assertNull(result, "Non-admin should not receive a division-based payroll report");
    }

    /**
     * Pass: getPaymentInfo only queries for the requesting employee's own ID,
     * enforcing payroll access restriction.
     */
    @Test
    public void testGetPaymentInfo_payrollAccessRestriction_queriesOwnID() throws Exception {
        when(mockRs.next()).thenReturn(false);

        // Employee with ID 99 requesting their own payroll
        Employee emp = new Employee(0, 99, "Carol", "C");
        reports.getPaymentInfo(emp);

        // Verify the query was parameterised with the employee's own ID
        verify(mockStmt).setInt(1, 99);
    }

    /** Pass: getMonthlyPayTitle returns null when no payroll records exist for the given title/month. */
    @Test
    public void testGetMonthlyPayTitle_noRecordsFound_returnsNull() throws Exception {
        PreparedStatement mockEmpStmt = mock(PreparedStatement.class);
        ResultSet mockEmpRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(contains("employee_job_titles"))).thenReturn(mockEmpStmt);
        when(mockEmpStmt.executeQuery()).thenReturn(mockEmpRs);
        when(mockEmpRs.next()).thenReturn(false); // no employees for that title

        String result = reports.getMonthlyPayTitle(999, 1, 2025, adminEmployee);

        assertNull(result, "Should return null when no employees exist for the given title");
    }
}
