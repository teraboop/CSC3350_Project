import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Reports {
public String getPaymentInfo(Employee emp) {
    StringBuilder output = new StringBuilder();
    String header = String.format("%-15s %-10s %-12s %-10s %-15s %-10s %-10s %-10s%n",
    "PAYMENT DATE", "EARNINGS", "FEDERAL TAX", "MEDICARE", "SOCIAL SECURITY", "STATE TAX", "401K", "HEALTHCARE");
    output.append(header);
    DatabaseConnector dbConn = new DatabaseConnector();
    int ID = emp.getEmpID();

    // The connection will close automatically after the catch block
    try (Connection conn = dbConn.getConnection()) {
        String query = "SELECT pay_date, earnings, fed_tax, fed_med, fed_SS, state_tax, " + 
                       "retire_401k, health_care FROM payroll WHERE empid = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("No records found in DB for ID: " + ID);
                    return null;
                }
                
                do { 
                    output.append(String.format("%-15s %-10d %-12d %-10d %-15d %-10d %-10d %-10d%n",
                        rs.getDate(1),  // %-15s (Date)
                        rs.getInt(2),   // %-10d (Earnings)
                        rs.getInt(3),   // %-10d (Fed Tax)
                        rs.getInt(4),   // %-10d (Fed Med)
                        rs.getInt(5),   // %-10d (Fed SS)
                        rs.getInt(6),   // %-10d (State Tax)
                        rs.getInt(7),   // %-10d (401k)
                        rs.getInt(8)    // %-10d (Health)
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
    public String getMonthlyPay(String type){
        return "";
    }
    public String newHires(){
        return "";
    }
}
