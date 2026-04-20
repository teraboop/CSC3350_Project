package hellofx;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import backend.Employee;
import backend.Reports;

public class MainController {

    @FXML private ChoiceBox<String> titleChoiceBox;

    // Called automatically after FXML loads
    public void initialize() {
        // Populate choice boxes from DB here if needed
        titleChoiceBox.getItems().addAll("Engineer", "Manager", ...);
    }

    @FXML
    private void handlePaymentHistory() {
        Employee emp = /* get current logged-in employee */;
        Reports reports = new Reports();
        String result = reports.getPaymentInfo(emp);
        // Display result in a TextArea or TableView in the right panel
        outputArea.setText(result);
    }

    @FXML
    private void handlePayrollByTitle() {
        String selected = titleChoiceBox.getValue();
        // map selected string to titleID, call reports.getMonthlyPayTitle(...)
    }
}