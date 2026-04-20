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
import javafx.scene.control.DatePicker;
import javafx.scene.control.ChoiceBox;
import java.time.LocalDate;
import javafx.scene.text.Text;
import backend.Employee;
import backend.Reports;
import backend.Authorization;
import backend.HRAdmin;
import backend.DatabaseConnector;
public class Controller {

    //PAY HISTORY CONNECTORS
    @FXML private Button generatePaymentReportButton;  // Reference to the button (optional, for future use)
    @FXML private TextArea paymentReportTextArea;  // Reference to the TextArea for displaying the report

    //PAYROLL REPORTS CONNECTORS
    @FXML private TextArea payrollReportTextArea; 
    @FXML private ChoiceBox<Integer> monthChoice;
    @FXML private ChoiceBox<Integer> yearChoice;
    @FXML private ChoiceBox<String> divisionChoiceBox;  // Division selector
    @FXML private ChoiceBox<String> titleChoiceBox;

    //NEW HIRES REPORTS CONNECTORS
    @FXML private DatePicker hireDateStart;
    @FXML private DatePicker hireDateEnd;
    @FXML private TextArea hireTextArea;

    @FXML private TextField usernameField;  // Reference to the username input field
    @FXML private PasswordField passwordField;  // Reference to the password input field
    @FXML private Text loginErrorText;

    private Employee currentEmployee;  // The logged-in employee

    public void initialize() {
    // Existing initialization code...
    String javaVersion = System.getProperty("java.version");
    String javafxVersion = System.getProperty("javafx.version");
    }

    //PAYMENT HISTORY HANDLER
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
    //PAYROLL BY DIVISION HANDLER
    @FXML
    private void handleGeneratePayrollDiv(ActionEvent event) {
        if (currentEmployee == null) {
            payrollReportTextArea.setText("Error: No employee logged in.");
            return;
        }

        // Get values from UI components
        if (divisionChoiceBox.getValue() == null || monthChoice.getValue() == null || yearChoice.getValue()==null) {
            payrollReportTextArea.setText("Error: Please select a division and a month + year.");
            return;
        }
        enum Division {
            TECHNOLOGY_ENGINEERING(1, "Technology Engineering"),
            MARKETING(2, "Marketing"),
            HR(3, "Human Resources"),
            HQ(4, "Headquarters");

            private final int id;
            private final String name;

            Division(int id, String name) {
                this.id = id;
                this.name = name;
            }

            public int getId() {
                return this.id;
            }

            @Override
            public String toString() {
                return this.name;
            }
            public static int getIdFromName(String text) {
                for (Division d : Division.values()) {
                    // Compare the input to the 'name' field with spaces
                    if (d.name.equalsIgnoreCase(text)) {
                        return d.id;
                    }
                }
                // Return a default value or throw an error if no match is found
                throw new IllegalArgumentException("No division found for: " + text);
            }
        }

        int divisionID = Division.getIdFromName(divisionChoiceBox.getValue());
        int month = monthChoice.getValue();
        int year = yearChoice.getValue();

        Reports reports = new Reports();
        String report = reports.getMonthlyPayDiv(divisionID, month, year, currentEmployee);
        if (report != null) {
            payrollReportTextArea.setText(report);
        } else {
            payrollReportTextArea.setText("No payment records found.");
        }
    }
    //PAYROLL BY TITLE HANDLER
    @FXML
    private void handleGeneratePayrollTitle(ActionEvent event) {
        if (currentEmployee == null) {
            payrollReportTextArea.setText("Error: No employee logged in.");
            return;
        }

        // Get values from UI components
        if (titleChoiceBox.getValue() == null || monthChoice.getValue() == null || yearChoice.getValue()==null) {
            payrollReportTextArea.setText("Error: Please select a Title and a month + year.");
            return;
        }
        enum Title {
            SOFTWARE_MANAGER(1, "Software Manager"),
            SOFTWARE_ENGINEER(3, "Software Engineer"),
            SOFTWARE_DEVELOPER(4, "Software Developer"),
            SOFTWARE_ARCHITECT(2, "Software Architect"),
            MARKETING_MANAGER(5, "Marketing Manager"),
            MARKETING_ASSOCIATE(6, "Marketing Associate"),
            MARKETING_ASSISTANT(7, "Marketing Assistant"),
            CHIEF_EXECUTIVE_OFFICER(8, "Chief Executive Officer"),
            CHIEF_FINANCIAL_OFFICER(9, "Chief Financial Officer"),
            CHIEF_INFORMATION_OFFICER(10, "Chief Information Officer");
            private final int id;
            private final String name;

            Title(int id, String name) {
                this.id = id;
                this.name = name;
            }

            public int getId() {
                return this.id;
            }

            @Override
            public String toString() {
                return this.name;
            }
            public static int getIdFromName(String text) {
                for (Title d : Title.values()) {
                    // Compare the input to the 'name' field with spaces
                    if (d.name.equalsIgnoreCase(text)) {
                        return d.id;
                    }
                }
                // Return a default value or throw an error if no match is found
                throw new IllegalArgumentException("No title found for: " + text);
            }
        }

        int titleID = Title.getIdFromName(titleChoiceBox.getValue());
        int month = monthChoice.getValue();
        int year = yearChoice.getValue();

        Reports reports = new Reports();
        String report = reports.getMonthlyPayTitle(titleID, month, year, currentEmployee);
        if (report != null) {
            payrollReportTextArea.setText(report);
        } else {
            payrollReportTextArea.setText("No payment records found.");
        }
    }

    @FXML
    public void handleGenerateNewHires(ActionEvent event){
        if (currentEmployee == null){
            hireTextArea.setText("Error: No employee logged in.");
            return;
        }
        if (hireDateStart.getValue() == null || hireDateEnd.getValue() == null) {
            hireTextArea.setText("Error: Please select a start and end date.");
            return;
        }

        LocalDate startDate = hireDateStart.getValue();
        LocalDate endDate = hireDateEnd.getValue();
        int startMonth = startDate.getMonthValue();
        int startYear = startDate.getYear();
        int endMonth = endDate.getMonthValue();
        int endYear = endDate.getYear();

        Reports reports = new Reports();
        String report = reports.newHires(startMonth, startYear, endMonth, endYear, currentEmployee);
        if (report != null){
            hireTextArea.setText(report);
        } else{
            hireTextArea.setText("No new hires found.");
        }
    }

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