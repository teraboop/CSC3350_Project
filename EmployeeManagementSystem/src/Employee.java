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
    public Employee(int roleID, int ID, String first_name, String last_name){
        this.classify = (roleID == 0) ? Classify.Employee : Classify.Admin;
        emp_ID = ID;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public int getEmpID(){
        return emp_ID;
    }

    public void setEmpID(int emp_ID){
        this.emp_ID = emp_ID;
    }
    public String getEmploymentDate(){
        return employmentDate;
    }
    public String getAddress(){
        return address;
    }
    public Boolean getClassify(){
        return this.classify.isAdmin();
    }
    public void searchEmp(){

    }
    
}
