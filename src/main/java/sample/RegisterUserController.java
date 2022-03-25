package main.java.sample;

import hr.java.expenses.Company;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class RegisterUserController implements Initializable {

    private static Random rand = new Random((new Date()).getTime());
    public static  String encryptedPassword;
   // public static String saltValue;
    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordFieldSecond;

    @FXML
    private Button button;

    List<Company> companyList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Database.fetchCompanies(companyList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    public void showLoginScreen(javafx.event.ActionEvent event) throws IOException{
        Parent showFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        Scene expensesScene = new Scene(showFrame, 800, 550);
        Main.getMainStage().setScene(expensesScene);

    }

    public void registerAccount() throws SQLException, IOException {
        String nameText = nameField.getText();
        String passwordText = passwordField.getText();
        String passwordTextSecond = passwordFieldSecond.getText();

        String saltValue = PasswordEncoder.getSaltvalue(30);
        encryptedPassword = PasswordEncoder.generateSecurePassword(passwordText, saltValue);

        if (passwordText.equals(passwordTextSecond)) {

            Boolean status = PasswordEncoder.veryfyUserPassword(passwordText, encryptedPassword, saltValue);
            if (status == true) {
                System.out.println("matched");
            } else {
                System.out.println("mismatched");
            }


            System.out.println(encryptedPassword);
            Boolean companyExist = false;
            Boolean registrationFailed = false;

            if (nameText.isEmpty() || passwordText.isEmpty() || passwordTextSecond.isEmpty()) {
                registrationFailed = true;

                if (nameText.isEmpty()) {
                    nameField.setPromptText("Please fill up this field");
                    nameField.setStyle("-fx-text-box-border: red;");
                } else {
                    nameField.setStyle("-fx-text-box-border: black;");
                }

                if (passwordTextSecond.isEmpty()) {
                    passwordFieldSecond.setPromptText("Please fill up this field");
                    passwordFieldSecond.setStyle("-fx-text-box-border: red;");
                } else {
                    passwordFieldSecond.setStyle("-fx-text-box-border: black;");
                }

                if (passwordText.isEmpty()) {
                    passwordField.setPromptText("Please fill up this field");
                    passwordField.setStyle("-fx-text-box-border: red;");
                } else {
                    passwordField.setStyle("-fx-text-box-border: black;");
                }
            } else {
                for (Company company : companyList) {
                    if (company.getName().equals(nameText)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("User already registered");
                        alert.setHeaderText("Username taken");
                        alert.setContentText("User with that username is already registered. Please pick another username");
                        alert.showAndWait();
                        companyExist = true;
                        registrationFailed = true;
                        break;
                    }
                }
            }
            if (!companyExist && !registrationFailed) {
                Company company = new Company(1, nameText, encryptedPassword, saltValue);
                Database.registerCompany(company);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("User created");
                alert.setHeaderText("User successfully created");
                alert.setContentText("User with username " + company.getName() + " successfully created, you may now login");
                alert.showAndWait();

                Parent loginFrame =
                        FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
                Scene loginScene = new Scene(loginFrame, 800, 550);
                Main.getMainStage().setScene(loginScene);
            }

        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Zaporke nisu jednake");
            alert.setHeaderText("Greška");
            alert.setContentText("Unesene zaporke nisu jednake, molimo ponovite unos");
            alert.showAndWait();
        }
    }
}
