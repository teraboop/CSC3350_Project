import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;

import javafx.fxml.FXML;

public class Reports {
    @FXML
    public String getPaymentInfo(Employee emp, ActionEvent event) {
        StringBuilder output = new StringBuilder();
        String header = String.format("%-15s %-10s %-12s %-10s %-15s %-10s %-10s %-10s%n",
        "PAYMENT DATE", "EARNINGS", "FEDERAL TAX", "MEDICARE", "SOCIAL SECURITY", "STATE TAX", "401K", "HEALTHCARE");
        output.append(header);
        DatabaseConnector dbConn = new DatabaseConnector();
        int ID = emp.getEmpID();

        // The connection will close automatically after the catch block
        try (Connection conn = dbConn.getConnection()) {
            String query = "SELECT pay_date, earnings, fed_tax, fed_med, fed_SS, state_tax, " + 
                        "retirement_401k, health_care FROM payroll WHERE emp_id = ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, ID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("No records found in DB for ID: " + ID);
                        return null;
                    }
                    
                    do { 
                        output.append(String.format("%-15s %-10.2f %-12.2f %-10.2f %-15.2f %-10.2f %-10.2f %-10.2f%n",
                            rs.getDate(1),  // %-15s (Date)
                            rs.getFloat(2),   // %-10d (Earnings)
                            rs.getFloat(3),   // %-12d (Fed Tax)
                            rs.getFloat(4),   // %-10d (Fed Med)
                            rs.getFloat(5),   // %-15d (Fed SS)
                            rs.getFloat(6),   // %-10d (State Tax)
                            rs.getFloat(7),   // %-10d (401k)
                            rs.getFloat(8)    // %-10d (Health)
                        ));
                    } while (rs.next());
                }
            }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        return output.toString();
    }

    public String getMonthlyPayTitle(int titleID, int month, int year, Employee emp){
        if(emp.getClassify() == false){
            return null;
        }
        StringBuilder output = new StringBuilder();
        String header = String.format("%-15s %-10s %-12s %-10s %-15s %-10s %-10s %-10s%n",
        "PAYMENT DATE", "EARNINGS", "FEDERAL TAX", "MEDICARE", "SOCIAL SECURITY", "STATE TAX", "401K", "HEALTHCARE");
        output.append(header);

        DatabaseConnector dbConn = new DatabaseConnector();
        String getEmp = "SELECT emp_id from employee_job_titles WHERE job_title_id = ?";

        ArrayList<Integer> empList = new ArrayList<>();
        try(Connection conn = dbConn.getConnection()){
            try(PreparedStatement stmt = conn.prepareStatement(getEmp)){
                stmt.setInt(1, titleID);
                try(ResultSet rs = stmt.executeQuery()){
                    if(!rs.next()){
                        return null;
                    }
                    do { 
                        empList.add(rs.getInt(1));
                    } while (rs.next());
                }
            }
            String placeholders = String.join(",", Collections.nCopies(empList.size(), "?"));
            YearMonth ym = YearMonth.of(year, month);
            Date start = Date.valueOf(ym.atDay(1));
            Date end = Date.valueOf(ym.plusMonths(1).atDay(1));
            String query = "SELECT pay_date, earnings, fed_tax, fed_med, fed_SS, state_tax, " +
                            "retirement_401k, health_care FROM payroll WHERE emp_id IN (" + placeholders + ") AND pay_date >= ? AND pay_date < ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                for (int i = 0; i < empList.size(); i++) {
                    stmt.setInt(i + 1, empList.get(i));
                }
                stmt.setDate(empList.size()+1,start);
                stmt.setDate(empList.size()+2,end);
                try(ResultSet rs = stmt.executeQuery()){
                    if(!rs.next()){
                        return null;
                    }
                    do { 
                    output.append(String.format("%-15s %-10.2f %-12.2f %-10.2f %-15.2f %-10.2f %-10.2f %-10.2f%n",
                        rs.getDate(1),  // %-15s (Date)
                        rs.getFloat(2),   // %-10d (Earnings)
                        rs.getFloat(3),   // %-12d (Fed Tax)
                        rs.getFloat(4),   // %-10d (Fed Med)
                        rs.getFloat(5),   // %-15d (Fed SS)
                        rs.getFloat(6),   // %-10d (State Tax)
                        rs.getFloat(7),   // %-10d (401k)
                        rs.getFloat(8)    // %-10d (Health)
                    ));
                } while (rs.next());
                }

            }

        } catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return output.toString();
    }

    public String getMonthlyPayDiv(int divID, int month, int year, Employee emp){
        if(emp.getClassify() == false){
            return null;
        }
        StringBuilder output = new StringBuilder();
        String header = String.format("%-15s %-10s %-12s %-10s %-15s %-10s %-10s %-10s%n",
        "PAYMENT DATE", "EARNINGS", "FEDERAL TAX", "MEDICARE", "SOCIAL SECURITY", "STATE TAX", "401K", "HEALTHCARE");
        output.append(header);

        DatabaseConnector dbConn = new DatabaseConnector();
        String getEmp = "SELECT emp_id from employee_division WHERE div_id = ?";

        ArrayList<Integer> empList = new ArrayList<>();
        try(Connection conn = dbConn.getConnection()){
            try(PreparedStatement stmt = conn.prepareStatement(getEmp)){
                stmt.setInt(1, divID);
                try(ResultSet rs = stmt.executeQuery()){
                    if(!rs.next()){
                        return null;
                    }
                    do { 
                        empList.add(rs.getInt(1));
                    } while (rs.next());
                }
            }
            String placeholders = String.join(",", Collections.nCopies(empList.size(), "?"));
            YearMonth ym = YearMonth.of(year, month);
            Date start = Date.valueOf(ym.atDay(1));
            Date end = Date.valueOf(ym.plusMonths(1).atDay(1));
            String query = "SELECT pay_date, earnings, fed_tax, fed_med, fed_SS, state_tax, " +
                            "retirement_401k, health_care FROM payroll WHERE emp_id IN (" + placeholders + ") AND pay_date >= ? AND pay_date < ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                for (int i = 0; i < empList.size(); i++) {
                    stmt.setInt(i + 1, empList.get(i));
                }
                stmt.setDate(empList.size()+1,start);
                stmt.setDate(empList.size()+2,end);
                try(ResultSet rs = stmt.executeQuery()){
                    if(!rs.next()){
                        return null;
                    }
                    do { 
                    output.append(String.format("%-15s %-10.2f %-12.2f %-10.2f %-15.2f %-10.2f %-10.2f %-10.2f%n",
                        rs.getDate(1),  // %-15s (Date)
                        rs.getFloat(2),   // %-10d (Earnings)
                        rs.getFloat(3),   // %-12d (Fed Tax)
                        rs.getFloat(4),   // %-10d (Fed Med)
                        rs.getFloat(5),   // %-15d (Fed SS)
                        rs.getFloat(6),   // %-10d (State Tax)
                        rs.getFloat(7),   // %-10d (401k)
                        rs.getFloat(8)    // %-10d (Health)
                    ));
                } while (rs.next());
                }

            }

        } catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return output.toString();
    }

    public String newHires(int startMonth, int startYear, int endMonth, int endYear, Employee emp){
        if(emp.getClassify() == false){
            return null;
        }
        //gets new hires within (month, year) and (month, year) INCLUSIVE
        StringBuilder output = new StringBuilder();
        String header = String.format("%-12s %-20s %-20s %-30s %-15s %-10s %-12s %-10s%n",
        "EMPLOYEE ID", "FIRST NAME", "LAST NAME", "EMAIL", "HIRE DATE", "SALARY", "SSN", "ADDRESS ID");
        output.append(header);

        DatabaseConnector dbConn = new DatabaseConnector();
        String query = "SELECT * FROM employees WHERE hire_date >= ? AND hire_date < ?";

        YearMonth first = YearMonth.of(startYear, startMonth);
        YearMonth last = YearMonth.of(endYear, endMonth);
        Date start = Date.valueOf(first.atDay(1));
        Date end = Date.valueOf(last.plusMonths(1).atDay(1));
        try(Connection conn = dbConn.getConnection()){
            try(PreparedStatement stmt = conn.prepareStatement(query)){
                stmt.setDate(1, start);
                stmt.setDate(2, end);
                try(ResultSet rs = stmt.executeQuery()){
                    if(!rs.next()){
                        return null;
                    }
                    do { 
                        output.append(String.format("%-12d %-20s %-20s %-30s %-15s %-10d %-12s %-10d%n",
                        rs.getInt(1),       // %-12d (Emp ID)
                        rs.getString(2),    // %-20s (FName)
                        rs.getString(3),    // %-20s (LName)
                        rs.getString(4),    // %-30s (Email)
                        rs.getDate(5),      // %-15s (Hire Date)
                        rs.getInt(6),       // %-10d (Salary)
                        rs.getString(7),    // %-12s (SSN)
                        rs.getInt(8)        // %-10d (Address ID)
                        ));
                    } while (rs.next());
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return output.toString();
    }
}
