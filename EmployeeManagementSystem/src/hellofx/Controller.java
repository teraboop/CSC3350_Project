package hellofx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import backend.Reports;
import backend.Employee;
public class Controller {

    @FXML
    private Button generatePaymentReportButton;  // Reference to the button (optional, for future use)

    @FXML
    private TextArea paymentReportTextArea;  // Reference to the TextArea for displaying the report

    // Assume this is set during login or initialization (replace with your logic)
    private Employee currentEmployee;  // The logged-in employee

    public void initialize() {
        // Existing initialization code...
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        // (Keep your existing label logic if present)
    }

    @FXML
    private void handleGeneratePaymentReport(ActionEvent event) {
        if (currentEmployee == null) {
            paymentReportTextArea.setText("Error: No employee logged in.");
            return;
        }

        Reports reports = new Reports();
        String report = reports.getPaymentInfo(currentEmployee);
        if (report != null) {
            paymentReportTextArea.setText(report);
        } else {
            paymentReportTextArea.setText("No payment records found.");
        }
    }

    // Existing method (keep as-is)
    @FXML
    private void handleButtonClick(ActionEvent event) {
        // Calling the method in the separate class
        // (Assuming 'service' is defined elsewhere; keep your existing logic)
    }

    // Method to set the current employee (call this after login)
    public void setCurrentEmployee(Employee employee) {
        this.currentEmployee = employee;
    }
}