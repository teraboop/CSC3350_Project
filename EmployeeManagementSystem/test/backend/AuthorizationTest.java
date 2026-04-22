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
}