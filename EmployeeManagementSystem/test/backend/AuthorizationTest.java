package backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.sql.*;

public class AuthorizationTest {

    private IDataSource mockConnector;
    private Connection mockConn;
    private PreparedStatement mockStmt;
    private ResultSet mockRs;
    private Authorization auth;

    @BeforeEach
    public void setUp() throws Exception {
        mockConnector = mock(IDataSource.class);
        mockConn = mock(Connection.class);
        mockStmt = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);

        when(mockConnector.getConnection()).thenReturn(mockConn);
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        auth = new Authorization(mockConnector);
    }

    @Test
    public void testLogin_invalidUsername() throws Exception {
        when(mockRs.next()).thenReturn(false); // username not found
        Employee result = auth.login("nobody", "password");
        assertNull(result);
    }

    @Test
    public void testLogin_wrongPassword() throws Exception {
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getString("password_hash")).thenReturn("wronghash");
        when(mockRs.getInt("emp_ID")).thenReturn(1);
        Employee result = auth.login("user", "wrongpassword");
        assertNull(result);
    }

    @Test
    public void testLogout_clearsCurrentUser() {
        auth.logout();
        assertNull(auth.getCurrentUser());
    }

    @Test
    public void testGetCurrentUser_initiallyNull() {
        assertNull(auth.getCurrentUser());
    }

    /**
     * Pass: a valid HR admin is authenticated successfully and the returned
     * object is an HRAdmin (classify == true).
     */
    @Test
    public void testLogin_validAdminCredentials_returnsAdmin() throws Exception {
        String plainPassword = "adminpass";
        String hashedPassword = hashSHA256(plainPassword);

        // credentials query
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getString("password_hash")).thenReturn(hashedPassword);
        when(mockRs.getInt("emp_ID")).thenReturn(1);

        // fetchEmployee query (second getConnection call)
        Connection mockConn2 = mock(Connection.class);
        PreparedStatement mockStmt2 = mock(PreparedStatement.class);
        ResultSet mockRs2 = mock(ResultSet.class);

        when(mockConnector.getConnection()).thenReturn(mockConn, mockConn2);
        when(mockConn2.prepareStatement(anyString())).thenReturn(mockStmt2);
        when(mockStmt2.executeQuery()).thenReturn(mockRs2);

        when(mockRs2.next()).thenReturn(true, false);
        when(mockRs2.getString("classification")).thenReturn("Admin");
        when(mockRs2.getString("first_name")).thenReturn("Bob");
        when(mockRs2.getString("last_name")).thenReturn("Admin");
        when(mockRs2.getString("email")).thenReturn("bob@company.com");
        when(mockRs2.getString("hire_date")).thenReturn("2020-01-01");
        when(mockRs2.getDouble("salary")).thenReturn(90000.0);
        when(mockRs2.getString("ssn")).thenReturn("000000001");
        when(mockRs2.getInt("address_id")).thenReturn(1);
        when(mockRs2.getString("dob")).thenReturn("1980-05-15");
        when(mockRs2.getString("phone")).thenReturn("555-0001");
        when(mockRs2.getString("emergency_contact_name")).thenReturn("Jane");
        when(mockRs2.getString("emergency_contact_phone")).thenReturn("555-0002");

        Employee result = auth.login("admin_user", plainPassword);

        assertNotNull(result, "Valid admin credentials should return a non-null user");
        assertTrue(result.getClassify(), "Authenticated admin should have classify == true");
        assertInstanceOf(HRAdmin.class, result, "Authenticated admin should be an HRAdmin instance");
    }

    /**
     * Pass: a valid general employee is authenticated successfully and the
     * returned object is a regular Employee (classify == false).
     */
    @Test
    public void testLogin_validEmployeeCredentials_returnsEmployee() throws Exception {
        String plainPassword = "emppass";
        String hashedPassword = hashSHA256(plainPassword);

        when(mockRs.next()).thenReturn(true);
        when(mockRs.getString("password_hash")).thenReturn(hashedPassword);
        when(mockRs.getInt("emp_ID")).thenReturn(5);

        Connection mockConn2 = mock(Connection.class);
        PreparedStatement mockStmt2 = mock(PreparedStatement.class);
        ResultSet mockRs2 = mock(ResultSet.class);

        when(mockConnector.getConnection()).thenReturn(mockConn, mockConn2);
        when(mockConn2.prepareStatement(anyString())).thenReturn(mockStmt2);
        when(mockStmt2.executeQuery()).thenReturn(mockRs2);

        when(mockRs2.next()).thenReturn(true, false);
        when(mockRs2.getString("classification")).thenReturn("Employee");
        when(mockRs2.getString("first_name")).thenReturn("Alice");
        when(mockRs2.getString("last_name")).thenReturn("Smith");
        when(mockRs2.getString("email")).thenReturn("alice@company.com");
        when(mockRs2.getString("hire_date")).thenReturn("2022-03-01");
        when(mockRs2.getDouble("salary")).thenReturn(55000.0);
        when(mockRs2.getString("ssn")).thenReturn("000000002");
        when(mockRs2.getInt("address_id")).thenReturn(2);
        when(mockRs2.getString("dob")).thenReturn("1995-08-20");
        when(mockRs2.getString("phone")).thenReturn("555-0010");
        when(mockRs2.getString("emergency_contact_name")).thenReturn("Tom");
        when(mockRs2.getString("emergency_contact_phone")).thenReturn("555-0011");

        Employee result = auth.login("alice_smith", plainPassword);

        assertNotNull(result, "Valid employee credentials should return a non-null user");
        assertFalse(result.getClassify(), "Authenticated general employee should have classify == false");
    }

    /**
     * Pass: authentication is denied and null is returned when the username
     * does not exist in the system.
     */
    @Test
    public void testLogin_nonExistentUser_returnsNull() throws Exception {
        when(mockRs.next()).thenReturn(false);

        Employee result = auth.login("nobody_here", "somepassword");

        assertNull(result, "Non-existent username should return null");
    }

    /**
     * Pass: login attempt with an empty username returns null without
     * crashing the system.
     */
    @Test
    public void testLogin_emptyUsername_returnsNull() throws Exception {
        when(mockRs.next()).thenReturn(false);

        Employee result = auth.login("", "somepassword");

        assertNull(result, "Empty username should not authenticate any user");
    }

    /**
     * Pass: login attempt with an empty password returns null because the
     * hashed empty string will not match any valid stored hash.
     */
    @Test
    public void testLogin_emptyPassword_returnsNull() throws Exception {
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getString("password_hash")).thenReturn("somehashedvalue");
        when(mockRs.getInt("emp_ID")).thenReturn(3);

        Employee result = auth.login("someuser", "");

        assertNull(result, "Empty password should fail authentication");
    }

    /**
     * Pass: currentUser is set after a successful login and cleared after logout.
     */
    @Test
    public void testLogin_setsCurrentUser_logoutClearsIt() throws Exception {
        String plainPassword = "pass123";
        String hashedPassword = hashSHA256(plainPassword);

        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getString("password_hash")).thenReturn(hashedPassword);
        when(mockRs.getInt("emp_ID")).thenReturn(7);

        Connection mockConn2 = mock(Connection.class);
        PreparedStatement mockStmt2 = mock(PreparedStatement.class);
        ResultSet mockRs2 = mock(ResultSet.class);

        when(mockConnector.getConnection()).thenReturn(mockConn, mockConn2);
        when(mockConn2.prepareStatement(anyString())).thenReturn(mockStmt2);
        when(mockStmt2.executeQuery()).thenReturn(mockRs2);

        when(mockRs2.next()).thenReturn(true, false);
        when(mockRs2.getString("classification")).thenReturn("Employee");
        when(mockRs2.getString("first_name")).thenReturn("Dave");
        when(mockRs2.getString("last_name")).thenReturn("D");
        when(mockRs2.getString("email")).thenReturn("dave@company.com");
        when(mockRs2.getString("hire_date")).thenReturn("2023-01-01");
        when(mockRs2.getDouble("salary")).thenReturn(60000.0);
        when(mockRs2.getString("ssn")).thenReturn("000000007");
        when(mockRs2.getInt("address_id")).thenReturn(3);
        when(mockRs2.getString("dob")).thenReturn("1990-01-01");
        when(mockRs2.getString("phone")).thenReturn("555-0020");
        when(mockRs2.getString("emergency_contact_name")).thenReturn("Eve");
        when(mockRs2.getString("emergency_contact_phone")).thenReturn("555-0021");

        auth.login("dave_d", plainPassword);
        assertNotNull(auth.getCurrentUser(), "getCurrentUser should not be null after successful login");

        auth.logout();
        assertNull(auth.getCurrentUser(), "getCurrentUser should be null after logout");
    }

    // Helper: replicates the SHA-256 + Base64 hashing used by Authorization.hashPassword()
    private String hashSHA256(String password) throws Exception {
        java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        return java.util.Base64.getEncoder().encodeToString(hash);
    }
}