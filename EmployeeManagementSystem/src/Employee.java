

public class Employee {
    private enum Classify {
        Employee(false), 
        Admin(true);
        private final boolean isAdmin;
        Classify(boolean isAdmin) {
            this.isAdmin = isAdmin;
        }
        public boolean isAdmin() {
            return isAdmin;
        }
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

    public Employee(int roleID, int ID, String first_name, String last_name){
        this.classify = (roleID == 0) ? Classify.Employee : Classify.Admin;
        emp_ID = ID;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    //getters
    public int getEmpID(){
        return emp_ID;
    }
    public String getFullName(){
        return first_name + " " + last_name;
    }

    public String getFirstName(){
        return first_name;
    }

    public String getLastName(){
        return last_name;
    }

    public String getEmail(){
        return email;
    }

    public double getSalary(){
        return salary;
    }

    public String getEmploymentDate(){
        return employmentDate;
    }

    public String getAddress(){
        return address;
    }

    public int getAddressID(){
        return address_ID;
    }

    public Boolean getClassify(){
        return this.classify.isAdmin();
    }

    public String getPhoneNumber(){
        return phone_number;
    }

    public String getSSN(){
        return ssn;
    }

    public String getDob(){
        return dob;
    }

    public String getEmergencyContactName(){
        return emergencyContactName;
    }

    public String getEmergencyContactPhone(){
        return emergencyContactPhone;
    }


    //setters
    public void setEmpID(int emp_ID){
        this.emp_ID = emp_ID;
    }

    public void setFirstName(String first_name){
        this.first_name = first_name;
    }

    public void setLastName(String last_name){
        this.last_name = last_name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setSalary(double salary){
        this.salary = salary;
    }

    public void setEmploymentDate(String employmentDate){
        this.employmentDate = employmentDate;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setAddressID(int address_ID){
        this.address_ID = address_ID;
    }

    public void setPhoneNumber(String phone_number){
        this.phone_number = phone_number;
    }

    public void setSSN(String ssn){
        this.ssn = ssn;
    }

    public void setDob(String dob){
        this.dob = dob;
    }

    public void setEmergencyContactName(String emergencyContactName){
        this.emergencyContactName = emergencyContactName;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone){
        this.emergencyContactPhone = emergencyContactPhone;
    }

}
