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
    };
    private final Classify classify;
    private int empID;
    private String employmentDate;
    private String address;
    public Employee(int roleID){
        this.classify = (roleID == 1) ? Classify.Employee : Classify.Admin;
    }

    public int getEmpID(){
        return empID;
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
