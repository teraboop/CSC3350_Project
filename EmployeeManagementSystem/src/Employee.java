public class Employee {
    private String classify;
    private int empID;
    private String employmentDate;
    private String address;
    public Employee(){
        classify = getClassify();
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
    public String getClassify(){
        return classify;
    }
    public void searchEmp(){

    }
    
}
