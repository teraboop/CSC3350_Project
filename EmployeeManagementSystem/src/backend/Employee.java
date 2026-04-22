package backend;


public class Employee {
    private enum Classify {
        Employee(false), 
        Admin(true);
        private final boolean isAdmin;
        Classify(boolean isAdmin) {
            this.isAdmin = isAdmin;
        }

        /**
         * Returns whether this classification represents an admin role.
         *
         * @return {@code true} if Admin, {@code false} if Employee
         */
        public boolean isAdmin() {
            return isAdmin;
        }

        /**
         * Returns the name of this classification constant.
         *
         * @return the classification name as a string
         */
        @Override
        public String toString() {
            return this.name();
        }

    };


    private final Classify classify;
    private String first_name;
    private String last_name;
    private int emp_ID;
    private String employmentDate;
    private String address;
    private int address_ID;
    private double salary;
    private String phone_number;
    private String email;
    private String ssn;
    private String dob;
    private String emergencyContactName;
    private String emergencyContactPhone;

    /**
     * Constructs an Employee with the specified role, ID, and name.
     *
     * @param roleID     0 for a standard employee, 1 for an admin
     * @param ID         the unique employee ID
     * @param first_name the employee's first name
     * @param last_name  the employee's last name
     */
    public Employee(int roleID, int ID, String first_name, String last_name){
        this.classify = (roleID == 0) ? Classify.Employee : Classify.Admin;
        emp_ID = ID;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    //getters

    /**
     * Returns the employee's unique ID.
     *
     * @return the employee ID
     */
    public int getEmpID(){
        return emp_ID;
    }

    /**
     * Returns the employee's full name as "First Last".
     *
     * @return the full name
     */
    public String getFullName(){
        return first_name + " " + last_name;
    }

    /**
     * Returns the employee's first name.
     *
     * @return the first name
     */
    public String getFirstName(){
        return first_name;
    }

    /**
     * Returns the employee's last name.
     *
     * @return the last name
     */
    public String getLastName(){
        return last_name;
    }

    /**
     * Returns the employee's email address.
     *
     * @return the email address
     */
    public String getEmail(){
        return email;
    }

    /**
     * Returns the employee's salary.
     *
     * @return the salary
     */
    public double getSalary(){
        return salary;
    }

    /**
     * Returns the employee's employment (hire) date.
     *
     * @return the employment date as a string
     */
    public String getEmploymentDate(){
        return employmentDate;
    }

    /**
     * Returns the employee's address.
     *
     * @return the address as a string
     */
    public String getAddress(){
        return address;
    }

    /**
     * Returns the ID of the employee's address record.
     *
     * @return the address ID
     */
    public int getAddressID(){
        return address_ID;
    }

    /**
     * Returns whether the employee has admin classification.
     *
     * @return {@code true} if the employee is an admin, {@code false} otherwise
     */
    public Boolean getClassify(){
        return this.classify.isAdmin();
    }

    /**
     * Returns the employee's phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber(){
        return phone_number;
    }

    /**
     * Returns the employee's Social Security Number.
     *
     * @return the SSN
     */
    public String getSSN(){
        return ssn;
    }

    /**
     * Returns the employee's date of birth.
     *
     * @return the date of birth as a string
     */
    public String getDob(){
        return dob;
    }

    /**
     * Returns the name of the employee's emergency contact.
     *
     * @return the emergency contact name
     */
    public String getEmergencyContactName(){
        return emergencyContactName;
    }

    /**
     * Returns the phone number of the employee's emergency contact.
     *
     * @return the emergency contact phone number
     */
    public String getEmergencyContactPhone(){
        return emergencyContactPhone;
    }


    //setters

    /**
     * Sets the employee's unique ID.
     *
     * @param emp_ID the new employee ID
     */
    public void setEmpID(int emp_ID){
        this.emp_ID = emp_ID;
    }

    /**
     * Sets the employee's first name.
     *
     * @param first_name the new first name
     */
    public void setFirstName(String first_name){
        this.first_name = first_name;
    }

    /**
     * Sets the employee's last name.
     *
     * @param last_name the new last name
     */
    public void setLastName(String last_name){
        this.last_name = last_name;
    }

    /**
     * Sets the employee's email address.
     *
     * @param email the new email address
     */
    public void setEmail(String email){
        this.email = email;
    }

    /**
     * Sets the employee's salary.
     *
     * @param salary the new salary value
     */
    public void setSalary(double salary){
        this.salary = salary;
    }

    /**
     * Sets the employee's employment (hire) date.
     *
     * @param employmentDate the new employment date as a string
     */
    public void setEmploymentDate(String employmentDate){
        this.employmentDate = employmentDate;
    }

    /**
     * Sets the employee's address.
     *
     * @param address the new address
     */
    public void setAddress(String address){
        this.address = address;
    }

    /**
     * Sets the ID of the employee's address record.
     *
     * @param address_ID the new address ID
     */
    public void setAddressID(int address_ID){
        this.address_ID = address_ID;
    }

    /**
     * Sets the employee's phone number.
     *
     * @param phone_number the new phone number
     */
    public void setPhoneNumber(String phone_number){
        this.phone_number = phone_number;
    }

    /**
     * Sets the employee's Social Security Number.
     *
     * @param ssn the new SSN
     */
    public void setSSN(String ssn){
        this.ssn = ssn;
    }

    /**
     * Sets the employee's date of birth.
     *
     * @param dob the new date of birth as a string
     */
    public void setDob(String dob){
        this.dob = dob;
    }

    /**
     * Sets the name of the employee's emergency contact.
     *
     * @param emergencyContactName the new emergency contact name
     */
    public void setEmergencyContactName(String emergencyContactName){
        this.emergencyContactName = emergencyContactName;
    }

    /**
     * Sets the phone number of the employee's emergency contact.
     *
     * @param emergencyContactPhone the new emergency contact phone number
     */
    public void setEmergencyContactPhone(String emergencyContactPhone){
        this.emergencyContactPhone = emergencyContactPhone;
    }

}
