package main.java.sample;

import hr.java.expenses.Category;
import hr.java.expenses.Company;
import hr.java.expenses.Expense;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddExpensesController extends Database implements Initializable {
    @FXML
    private TextField nazivTroska;
    @FXML
    private TextField iznos;
    @FXML
    private CheckBox cash;
    @FXML
    private ComboBox<String> kategorija;
    @FXML
    private DatePicker datumTroška;

    @FXML
    private Button dodajKategoriju;

    @FXML
    private Button odjava;

    List<Category> listOfCategories = new ArrayList<>();

    @FXML
    public void showAddCategoryDialog(ActionEvent event) throws SQLException {
        TextInputDialog dialog = new TextInputDialog("");

        dialog.setTitle("Nova kategorija");
        dialog.setHeaderText("Dodaj naziv kategorije:");
        dialog.setContentText("Kategorija:");

        Optional<String> result = dialog.showAndWait();
        TextField input = dialog.getEditor();

        if(input.getText() != null && input.getText().toString().length() !=0){

            System.out.println(input.getText());
            addCategory(new Category(1,input.getText()));
        }
    }


    public void pokazi() throws SQLException, ParseException{
        connectToDatabase();

        fetchCategories(listOfCategories);
        for(Category c: listOfCategories)
            kategorija.getItems().addAll(c.getName());

    }

    public void spremi() throws SQLException {
        String trosakNaziv = nazivTroska.getText();
        String iznosString = iznos.getText();
        Boolean gotovina = cash.isSelected();

        Double iznosDouble = new Double(iznosString);
        BigDecimal iznosBroj = BigDecimal.valueOf(iznosDouble);

        String kategorijaNaziv = kategorija.getValue();
        List<Category> categoryList = listOfCategories.stream().filter(bb -> bb.getName().toLowerCase().
                equals(kategorijaNaziv.toLowerCase())).collect(Collectors.toList());

        Category category = null;

        for(Category cat: categoryList){
            if(cat.getName().equals(kategorijaNaziv)){
                category = cat;
                break;
            }
        }

        LocalDate datum = datumTroška.getValue();
        java.sql.Date getDatePickerDate = java.sql.Date.valueOf(datumTroška.getValue());
        Date sqlDatum = Date.valueOf(datum);

        Expense expense = new Expense(1, trosakNaziv, iznosBroj,sqlDatum,
                LoginController.currentCompany, category,gotovina);

        addExpense(expense);

        Alert alert = new Alert (Alert.AlertType.INFORMATION);
        alert.setTitle ("Spremanje podataka");
        alert.setHeaderText("Message");
        alert.setContentText("Novi trošak je uspješno spremljen");
        alert.showAndWait();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    public void showExpensesScreen(javafx.event.ActionEvent event) throws IOException {
        Parent showFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("showExpenses.fxml"));
        Scene expensesScene = new Scene(showFrame, 1100, 850);
        Main.getMainStage().setScene(expensesScene);
    }

    @FXML
    public void showLoginScreen(javafx.event.ActionEvent event) throws IOException {
        Boolean button = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
       alert.setTitle("Želite se odjaviti?");
       alert.setContentText("OK?");
       ButtonType okButton = new ButtonType("Da", ButtonBar.ButtonData.YES);
       ButtonType noButton = new ButtonType("ne", ButtonBar.ButtonData.NO);
       alert.getButtonTypes().setAll(okButton,noButton);
       alert.showAndWait().ifPresent(type -> {
           if (type == okButton){
               try {
                   doIt();
               } catch (IOException e) {
                   e.printStackTrace();
               }

           }
       });


    }
    public void doIt() throws IOException {
        Parent showFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        Scene loginScene = new Scene(showFrame, 800, 550);
        Main.getMainStage().setScene(loginScene);
    }
}
