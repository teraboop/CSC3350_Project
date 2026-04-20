package hellofx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import backend.Authorization;
import backend.DatabaseConnector;
import backend.Employee;


public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    private Employee currentEmployee;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        DatabaseConnector dbConn = new DatabaseConnector();
        Authorization auth = new Authorization(dbConn);
        Employee user = auth.login(username, password);

        if (user == null) {
            // Show an error — e.g. shake the form or display a label
            passwordField.clear();
            return;
        }

        // Switch to main screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainscreen.fxml"));
            Parent root = loader.load();

            // Pass the logged-in user to the main screen controller
            MainController mainCtrl = loader.getController();
            mainCtrl.setCurrentEmployee(user);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Employee Management System");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCurrentEmployee(Employee emp) {
        this.currentEmployee = emp;
    }
}