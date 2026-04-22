package backend;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {

    @Test
    public void testConstructorSetsFields() {
        Employee emp = new Employee(0, 42, "Jane", "Doe");
        assertEquals(42, emp.getEmpID());
        assertEquals("Jane", emp.getFirstName());
        assertEquals("Doe", emp.getLastName());
        assertEquals("Jane Doe", emp.getFullName());
    }

    @Test
    public void testGetClassify_Employee() {
        Employee emp = new Employee(0, 1, "John", "Smith");
        assertFalse(emp.getClassify());
    }

    @Test
    public void testGetClassify_Admin() {
        Employee emp = new Employee(1, 2, "Admin", "User");
        assertTrue(emp.getClassify());
    }

    @Test
    public void testSetters() {
        Employee emp = new Employee(0, 1, "John", "Smith");
        emp.setEmail("john@example.com");
        emp.setSalary(75000.0);
        assertEquals("john@example.com", emp.getEmail());
        assertEquals(75000.0, emp.getSalary());
    }
}