package hellofx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import backend.Employee;
import backend.Reports;
import backend.Authorization;
import backend.HRAdmin;
import backend.DatabaseConnector;
public class Controller {

    @FXML
    private Button generatePaymentReportButton;  // Reference to the button (optional, for future use)

    @FXML
    private TextArea paymentReportTextArea;  // Reference to the TextArea for displaying the report

    @FXML private TextField usernameField;  // Reference to the username input field
    @FXML private PasswordField passwordField;  // Reference to the password input field
    @FXML private Text loginErrorText;

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
        String report = reports.getPaymentInfo(currentEmployee, event);
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

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        DatabaseConnector dbConnector = new DatabaseConnector();
        Authorization auth = new Authorization(dbConnector);
        Employee employee = auth.login(username, password);

        if (employee != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("mainscreen.fxml"));
                Parent mainRoot = loader.load();

                Controller mainController = loader.getController();
                mainController.setCurrentEmployee(employee);

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(mainRoot, 800, 600));
                stage.setTitle("Employee Management System");
            } catch (Exception e) {
                e.printStackTrace();
                loginErrorText.setText("Failed to load main screen.");
            }
        } else {
            loginErrorText.setText("Invalid username or password.");
        }
    }
}