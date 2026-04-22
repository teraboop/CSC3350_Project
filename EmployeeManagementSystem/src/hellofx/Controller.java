package hellofx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import java.time.LocalDate;
import javafx.scene.text.Text;
import backend.Employee;
import backend.Reports;
import backend.Authorization;
import backend.HRAdmin;
import backend.DatabaseConnector;
import backend.EmployeeRepository;
import javafx.scene.control.TableRow;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback; // If using a custom result converter
import java.util.Optional;    // For handling the showAndWait() result
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory; // You'll need this to link columns to data
import javafx.beans.property.ReadOnlyObjectWrapper;


public class Controller {
    @FXML private TabPane mainTabPane;
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

    //SEARCH CONNECTORS
    // --- Search Power Bar ---
    @FXML private ComboBox<String> searchTypeCombo;
    @FXML private TextField searchInputField;
    @FXML private DatePicker searchDatePicker;
    @FXML private Button searchButton;

    // --- Table & Results ---
    @FXML private TableView<Employee> employeeTable;
    @FXML private TableColumn<Employee, Integer> colEmpID;          // SQL index 1
    @FXML private TableColumn<Employee, String> colFirstName;      // SQL index 2
    @FXML private TableColumn<Employee, String> colLastName;       // SQL index 3
    @FXML private TableColumn<Employee, String> colEmail;          // SQL index 4
    @FXML private TableColumn<Employee, String> colEmploymentDate; // SQL index 5
    @FXML private TableColumn<Employee, Double> colSalary;         // SQL index 6
    @FXML private TableColumn<Employee, String> colSSN;            // SQL index 7
    @FXML private TableColumn<Employee, Integer> colAddressID;     // SQL index 8
    @FXML private TableColumn<Employee, String> colDOB;            // SQL index 9
    @FXML private TableColumn<Employee, String> colPhone;          // SQL index 10
    @FXML private TableColumn<Employee, String> colEmergencyName;  // SQL index 11
    @FXML private TableColumn<Employee, String> colEmergencyPhone; // SQL index 12
    @FXML private Tab searchTab;

    @FXML private TextField usernameField;  // Reference to the username input field
    @FXML private PasswordField passwordField;  // Reference to the password input field
    @FXML private Text loginErrorText;

    private Employee currentEmployee;  // The logged-in employee

    public void initialize() {
    // Existing initialization code...
    String javaVersion = System.getProperty("java.version");
    String javafxVersion = System.getProperty("javafx.version");
    if (paymentReportTextArea != null) {
        paymentReportTextArea.setStyle("-fx-font-family: 'monospace'; -fx-font-size: 14;");
    }
    if (payrollReportTextArea != null) {
        payrollReportTextArea.setStyle("-fx-font-family: 'monospace'; -fx-font-size: 14;");
    }
    if (hireTextArea != null) {
        hireTextArea.setStyle("-fx-font-family: 'monospace'; -fx-font-size: 14;");
    }
    if (mainTabPane != null) {
        mainTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == searchTab) {
                refreshSearchTab();
            }
        });
    }

        // 3. Setup Power Bar Dropdown
        if (searchTypeCombo != null) {
            searchTypeCombo.getItems().addAll("All", "Name", "EmpID", "SSN", "DOB");
            searchTypeCombo.setValue("All");
            // Toggle visibility between text input and date picker for DOB searches
            searchTypeCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null && newVal.equalsIgnoreCase("DOB")) {
                    if (searchDatePicker != null) searchDatePicker.setVisible(true);
                    if (searchInputField != null) searchInputField.setVisible(false);
                } else {
                    if (searchDatePicker != null) searchDatePicker.setVisible(false);
                    if (searchInputField != null) searchInputField.setVisible(true);
                }
            });
        }
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
    //NEW HIRES HANDLER
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
    
    private void refreshSearchTab() {
        if (employeeTable != null && colFirstName != null) {
            setupColumns();
            setupTableSelection();
            // Optional: Trigger a "Search All" automatically when they open the tab
            handleSearch(); 
        }
    }
    private void setupColumns() {
        // Use lambda-based factories to avoid reflection/property name issues
        colEmpID.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getEmpID()));
        colFirstName.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getFirstName()));
        colLastName.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getLastName()));
        colEmail.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getEmail()));
        colEmploymentDate.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getEmploymentDate()));
        colSalary.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getSalary()));
        colSSN.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getSSN()));
        colAddressID.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getAddressID()));
        colDOB.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getDob()));
        colPhone.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getPhoneNumber()));
        colEmergencyName.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getEmergencyContactName()));
        colEmergencyPhone.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getEmergencyContactPhone()));
    }

    private void showEditDialog(Employee employee) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("HR Admin: Edit Employee");
        dialog.setHeaderText("Updating: " + employee.getEmpID());

        // 1. Buttons
        ButtonType saveButtonType = new ButtonType("Save Changes", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // 2. Layout & Separate Fields
        GridPane grid = new GridPane();
        grid.setHgap(10); 
        grid.setVgap(10);

        // Splitting names (assumes your Employee object has separate getters or you split the string)
        TextField firstNameField = new TextField(employee.getFirstName()); 
        TextField lastNameField = new TextField(employee.getLastName());
        TextField salaryField = new TextField(String.valueOf(employee.getSalary()));
        TextField raisePercentField = new TextField();
        raisePercentField.setPromptText("Enter % (optional)");

        grid.add(new Label("First Name:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Base Salary:"), 0, 2);
        grid.add(salaryField, 1, 2);
        grid.add(new Label("Apply Raise %:"), 0, 3);
        grid.add(raisePercentField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // 3. Logic for Save
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                // Update Name
                employee.setFirstName(firstNameField.getText());
                employee.setLastName(lastNameField.getText());

                // Handle Requirement #5 (Percentage Raise logic)
                if (!raisePercentField.getText().isEmpty()) {
                    double percent = Double.parseDouble(raisePercentField.getText());
                    // Call your backend method here:
                    // backend.applyPercentageRaise(employee, percent);
                }
                
                // Call your backend SQL update here
                // backend.updateEmployeeInDB(employee);
                
                return dialogButton;
            }
            return null;
        });

        dialog.showAndWait();
        employeeTable.refresh(); // Visual update for the table
    }

private void setupTableSelection() {
    employeeTable.setRowFactory(tv -> {
        TableRow<Employee> row = new TableRow<>();
        
        row.setOnMouseClicked(event -> {
            // Requirement: Double-click + Row is not empty
            if (event.getClickCount() == 2 && !row.isEmpty()) {
                
                // Get the specific employee object from the clicked row
                Employee clickedEmployee = row.getItem();
                
                // Check if user is HR Admin (Requirement #1a)
                if (currentEmployee != null && currentEmployee.getClassify()) {
                    showEditDialog(clickedEmployee);
                } else {
                    // Optional: Show "View Only" alert or different read-only dialog
                    System.out.println("Access Denied: View-only for general employees.");
                }
            }
        });
        return row;
    });
}

    @FXML
    private void handleSearch() {
        if (employeeTable == null) return;

        EmployeeRepository repo = new EmployeeRepository();
        String type = (searchTypeCombo != null && searchTypeCombo.getValue() != null) ? searchTypeCombo.getValue() : "All";

        try {
            // Clear current results
            employeeTable.getItems().clear();

            switch (type) {
                case "All":
                    java.util.List<Employee> all = repo.findAll();
                    if (all != null && !all.isEmpty()) {
                        employeeTable.getItems().addAll(all);
                    }
                    break;
                case "Name":
                    String name = (searchInputField != null) ? searchInputField.getText().trim() : "";
                    if (!name.isEmpty()) {
                        Employee e = repo.findByName(name);
                        if (e != null) employeeTable.getItems().add(e);
                    }
                    break;
                case "EmpID":
                    String idText = (searchInputField != null) ? searchInputField.getText().trim() : "";
                    try {
                        int id = Integer.parseInt(idText);
                        Employee e = repo.findByID(id);
                        if (e != null) employeeTable.getItems().add(e);
                    } catch (NumberFormatException nfe) {
                        System.out.println("Invalid EmpID: " + idText);
                    }
                    break;
                case "SSN":
                    String ssn = (searchInputField != null) ? searchInputField.getText().trim() : "";
                    if (!ssn.isEmpty()) {
                        Employee e = repo.findBySSN(ssn);
                        if (e != null) employeeTable.getItems().add(e);
                    }
                    break;
                case "DOB":
                    String dob = null;
                    if (searchDatePicker != null && searchDatePicker.getValue() != null) {
                        dob = searchDatePicker.getValue().toString();
                    } else if (searchInputField != null) {
                        dob = searchInputField.getText().trim();
                    }
                    if (dob != null && !dob.isEmpty()) {
                        Employee e = repo.findByDateOfBirth(dob);
                        if (e != null) employeeTable.getItems().add(e);
                    }
                    break;
                default:
                    java.util.List<Employee> fallback = repo.findAll();
                    if (fallback != null && !fallback.isEmpty()) employeeTable.getItems().addAll(fallback);
            }
        } catch (Exception e) {
            e.printStackTrace();
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