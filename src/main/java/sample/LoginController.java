package main.java.sample;

import hr.java.expenses.Company;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button addUserButton;

    private List<Company> companyList = new ArrayList<>();
    public static Company currentCompany;

    @FXML
    public void showAddExpensesScreen(javafx.event.ActionEvent event) throws IOException{
        String usernameText = usernameTextField.getText();
        String passwordText = passwordField.getText();

        Boolean companyFound = false;

        if(usernameText.isEmpty() || passwordText.isEmpty()){
            if(usernameText.isEmpty()){
                usernameTextField.setPromptText("Please fill up this field");
            }
            else{
                usernameTextField.setStyle("-fx-text-box-border: black;");
            }
            if(passwordText.isEmpty()){
                passwordField.setPromptText("please fill up this field");
            }
            else{
                passwordField.setStyle("-fx-text-box-border: black;");
            }
        }
        else{
            for(Company company: companyList){
                if(company.getName().equals(usernameText) &&
                        PasswordEncoder.veryfyUserPassword(passwordText, company.getPassword(), company.getSalt())){
                    currentCompany = company;
                    Parent showFrame =
                            FXMLLoader.load(getClass().getClassLoader().getResource("showExpenses.fxml"));
                    Scene expensesScene = new Scene(showFrame, 1100, 850);

                    Main.getMainStage().setScene(expensesScene);

                    companyFound = true;
                }
            }
        }
        if(!companyFound){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("User not found");
            alert.setHeaderText("User not found");
            alert.setContentText("User with that username or password does not exist");
            alert.showAndWait();

            usernameTextField.setText("");
            passwordField.setText("");
        }
    }

    @FXML
    public void showAddUserScreen(javafx.event.ActionEvent event) throws IOException{
        Parent showFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("registerUser.fxml"));
        Scene expensesScene = new Scene(showFrame, 800, 550);
        Main.getMainStage().setScene(expensesScene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Database.fetchCompanies(companyList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
