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
    private int empID;
    private String employmentDate;
    private String address;
    public Employee(int roleID, int ID){
        this.classify = (roleID == 0) ? Classify.Employee : Classify.Admin;
        empID = ID;
    }

    public int getEmpID(){
        return empID;
    }

    public void setEmpID(int empID){
        this.empID = empID;
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
