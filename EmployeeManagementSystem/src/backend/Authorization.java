package backend;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import java.util.Base64;

public class Authorization {
    private IDataSource dbConnector;
    private Employee currentUser;
    
    public Authorization(IDataSource dbConnector) {
        this.dbConnector = dbConnector;
    }
    /**
     * Hashes a plaintext password using SHA-256 and encodes the result in Base64.
     *
     * @param password the plaintext password to hash
     * @return a Base64-encoded SHA-256 hash of the password,
     *         or {@code null} if hashing fails
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            System.out.println("Error hashing password.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Verifies a plaintext password against a stored hash by hashing the input
     * and comparing it to the stored value.
     *
     * @param inputPassword the plaintext password provided by the user
     * @param storedHash    the Base64-encoded SHA-256 hash stored in the database
     * @return {@code true} if the hashed input matches the stored hash, {@code false} otherwise
     */
    private boolean verifyPassword(String inputPassword, String storedHash) {
        String inputHash = hashPassword(inputPassword);
        return inputHash != null && inputHash.equals(storedHash);
    }

    /**
     * Authenticates a user with the given username and password.
     * If authentication succeeds, the user is set as the current session user.
     *
     * @param username the username of the employee attempting to log in
     * @param password the plaintext password to authenticate
     * @return the authenticated {@link Employee} or {@link HRAdmin} object on success,
     *         or {@code null} if the username is not found, the password is incorrect,
     *         or an error occurs
     */
    public Employee login(String username, String password) {
        try (Connection conn = dbConnector.getConnection()){
            String query = "SELECT emp_ID, password_hash FROM credentials WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if(!rs.next()) {
                System.out.println("Username not found.");
                return null;
            }

            String storedHash = rs.getString("password_hash");
            int emp_ID = rs.getInt("emp_ID");

            if (!verifyPassword(password, storedHash)) {
                System.out.println("Incorrect password.");
                return null;
            }

            Employee user = fetchEmployee(emp_ID);
            if (user == null) {
                System.out.println("Login failed: user record could not be fetched for emp_ID " + emp_ID);
                return null;
            }
            this.currentUser = user;
            System.out.println("Login successful. Welcome, " + username + "!");
            return user;
        } catch (Exception e) {
            System.out.println("Login failed due to an error.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fetches an employee record from the database by their employee ID and
     * returns the appropriate object type based on their classification.
     *
     * @param emp_ID the unique identifier of the employee to fetch
     * @return an {@link HRAdmin} if the employee has Admin classification,
     *         an {@link Employee} otherwise, or {@code null} if not found or an error occurs
     * @throws SQLException if a database access error occurs
     */
    private Employee fetchEmployee(int emp_ID) {
        String mainQuery = "SELECT e.*, c.classification FROM employees e JOIN credentials c ON e.emp_ID = c.emp_ID WHERE e.emp_ID = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(mainQuery)) {

            stmt.setInt(1, emp_ID);
            // small timeout to avoid very long waits (driver may ignore)
            try { stmt.setQueryTimeout(5); } catch (Exception ignore) {}

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return null;

                String classification = rs.getString("classification");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String email = rs.getString("email");
                String hire_date = rs.getString("hire_date");
                Double salary = rs.getDouble("salary");
                String ssn = rs.getString("ssn");
                int address_id = rs.getInt("address_id");
                String dob = rs.getString("dob");
                String phone = rs.getString("phone");
                String emergency_contact_name = rs.getString("emergency_contact_name");
                String emergency_contact_phone = rs.getString("emergency_contact_phone");

                // Resolve address components safely using separate statements/resultsets
                String street = "", zip = "", country = "", city = "", state = "";
                int city_id = -1, state_id = -1;

                String addrQuery = "SELECT street, zip, country, city_id FROM addresses WHERE address_id = ?";
                try (PreparedStatement addrStmt = conn.prepareStatement(addrQuery)) {
                    addrStmt.setInt(1, address_id);
                    try { addrStmt.setQueryTimeout(5); } catch (Exception ignore) {}
                    try (ResultSet rsAddr = addrStmt.executeQuery()) {
                        if (rsAddr.next()) {
                            street = rsAddr.getString("street");
                            zip = rsAddr.getString("zip");
                            country = rsAddr.getString("country");
                            city_id = rsAddr.getInt("city_id");
                        }
                    }
                }

                if (city_id > 0) {
                    String cityQuery = "SELECT city_name, state_id FROM cities WHERE city_id = ?";
                    try (PreparedStatement cityStmt = conn.prepareStatement(cityQuery)) {
                        cityStmt.setInt(1, city_id);
                        try { cityStmt.setQueryTimeout(5); } catch (Exception ignore) {}
                        try (ResultSet rsCity = cityStmt.executeQuery()) {
                            if (rsCity.next()){ 
                                city = rsCity.getString("city_name");
                                state_id = rsCity.getInt("state_id");
                        }
                        }
                    }
                }

                if (state_id > 0) {
                    String stateQuery = "SELECT state_abbr FROM states WHERE state_id = ?";
                    try (PreparedStatement stateStmt = conn.prepareStatement(stateQuery)) {
                        stateStmt.setInt(1, state_id);
                        try { stateStmt.setQueryTimeout(5); } catch (Exception ignore) {}
                        try (ResultSet rsState = stateStmt.executeQuery()) {
                            if (rsState.next()) state = rsState.getString("state_abbr");
                        }
                    }
                }

                StringBuilder address = new StringBuilder();
                if (!street.isEmpty()) address.append(street).append(" ");
                if (!city.isEmpty()) address.append(city).append(", ");
                if (!state.isEmpty()) address.append(state).append(" ");
                if (!zip.isEmpty()) address.append(zip).append(" ");
                if (!country.isEmpty()) address.append(country);

                int roleID = "Admin".equalsIgnoreCase(classification) ? 1 : 0;
                if (roleID == 1) {
                    HRAdmin temp = new HRAdmin(roleID, emp_ID, first_name, last_name);
                    temp.setEmail(email);
                    temp.setEmploymentDate(hire_date);
                    temp.setSalary(salary);
                    temp.setSSN(ssn);
                    temp.setAddressID(address_id);
                    temp.setDob(dob);
                    temp.setPhoneNumber(phone);
                    temp.setEmergencyContactName(emergency_contact_name);
                    temp.setEmergencyContactPhone(emergency_contact_phone);
                    temp.setAddress(address.toString());
                    return temp;
                } else {
                    Employee temp = new Employee(roleID, emp_ID, first_name, last_name);
                    temp.setEmail(email);
                    temp.setEmploymentDate(hire_date);
                    temp.setSalary(salary);
                    temp.setSSN(ssn);
                    temp.setAddressID(address_id);
                    temp.setDob(dob);
                    temp.setPhoneNumber(phone);
                    temp.setEmergencyContactName(emergency_contact_name);
                    temp.setEmergencyContactPhone(emergency_contact_phone);
                    temp.setAddress(address.toString());
                    return temp;
                }
            }
        } catch (Exception e) {
            System.out.println("Error fetching employee for emp_ID " + emp_ID + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Logs out the currently authenticated user by clearing the session.
     *
     * @return {@code null} after the current user has been cleared
     */
    public Employee logout() {

        if (currentUser != null) {
            System.out.println("User with emp_ID " + currentUser.getEmpID() + " logged out.");
            currentUser = null;
        } else {
            System.out.println("No user is currently logged in.");
        }
        return currentUser;
    }

    /**
     * Returns the currently authenticated user for this session.
     *
     * @return the current {@link Employee} or {@link HRAdmin} object,
     *         or {@code null} if no user is logged in
     */
    public Employee getCurrentUser() {
        return currentUser;
    }
    
}
