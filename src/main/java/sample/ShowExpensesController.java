package main.java.sample;

import hr.java.expenses.Category;
import hr.java.expenses.Company;
import hr.java.expenses.Expense;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ShowExpensesController extends Database implements Initializable {

    @FXML
    private PieChart pieChart;

    @FXML
    private PieChart pieChartSecond;

    @FXML
    private LineChart<String,Number> lineChart;

    @FXML
    private NumberAxis x;

    @FXML
    private CategoryAxis y;

    @FXML
    private TableView<Expense> expenseTableView;

    @FXML
    private TableColumn<Expense, String> columnName;

    @FXML
    private TableColumn<Expense, BigDecimal> columnPrice;

    @FXML
    private TableColumn<Expense, Category> columnCategory;

    @FXML
    private TableColumn<Expense, Date> columnDate;

    @FXML
    private ComboBox<String> categoryPicker;

    @FXML
    private DatePicker dateFrom;

    @FXML
    private DatePicker dateTo;

    @FXML
    private Button addExpense;

    @FXML
    private Button backButton;


    List<Expense> listOfExpenses = new ArrayList<>();
    List<Category> listOfCategoires = new ArrayList<>();
    ObservableList<Expense> expenseObservableList = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            fetchExpenses(listOfExpenses);
            fetchCategories(listOfCategoires);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

       int p=0;
       int z = 0;
        Double sumOfEverythingSecond = 1.0;
        PieChart.Data dataSecond[] = new PieChart.Data[listOfCategoires.size()];
        Double[] zbrojSecond =new Double[listOfCategoires.size()];
        pieChart.setTitle("Troškovi Vaše tvrtke " + LoginController.currentCompany.getName());
        for(Category c: listOfCategoires) {
            zbrojSecond[z] = 1.0;
            for (Expense e : listOfExpenses) {
                if (c.getId().equals(e.getCategory().getId())) {
                    zbrojSecond[z] = zbrojSecond[z] + e.getCost().doubleValue();
                }
            }
            sumOfEverythingSecond = sumOfEverythingSecond + zbrojSecond[z] - 2.0;
            z++;
        }
        for(Category c: listOfCategoires){
            double divided;
            divided = (zbrojSecond[p]/sumOfEverythingSecond)*100;
            BigDecimal round = new BigDecimal(divided);
            BigDecimal scaled = round.setScale(2, RoundingMode.HALF_UP);
            dataSecond[p] = new PieChart.Data(c.getName() + " " + scaled + "%", zbrojSecond[p]);
            p++;
        }
        ObservableList<PieChart.Data> pieChartDataSecond
                = FXCollections.observableArrayList(dataSecond);

        pieChartSecond.setData(pieChartDataSecond);



        int i = 0;
        int m = 0;
        PieChart.Data data[] = new PieChart.Data[listOfCategoires.size()];
        Double[] zbroj = new Double[listOfCategoires.size()];

        Double sumOfEverything = 1.0;
        for(Category c: listOfCategoires) {
           zbroj[m] = 1.0;
           for (Expense e : listOfExpenses) {

               if (e.getCompany().getId().equals(LoginController.currentCompany.getId())
                       && c.getId().equals(e.getCategory().getId())) {

                   zbroj[m] =zbroj[m] + e.getCost().doubleValue();
               }
           }
           sumOfEverything = sumOfEverything + zbroj[m]-2.0;
            m++;
        }
        for(Category c: listOfCategoires){
          double divided;
          divided = (zbroj[i]/sumOfEverything)*100;
          BigDecimal round = new BigDecimal(divided);
          BigDecimal scaled = round.setScale(2, RoundingMode.HALF_UP);
          data[i] = new PieChart.Data(c.getName() + " " + scaled + "%", zbroj[i]);
          i++;
       }
            ObservableList<PieChart.Data> pieChartData
                    = FXCollections.observableArrayList(data);

         pieChart.setData(pieChartData);

        //Adding table

         columnName
                 .setCellValueFactory(new PropertyValueFactory<Expense, String>("name"));

        columnPrice
                .setCellValueFactory(new PropertyValueFactory<Expense, BigDecimal>("cost"));

        columnCategory
                .setCellValueFactory(new PropertyValueFactory<Expense, Category>("category"));

        columnDate
                .setCellValueFactory(new PropertyValueFactory<Expense, Date>("date"));

        listOfExpenses = listOfExpenses.stream()
                .filter(expense -> (expense.getCompany().getName().equals(LoginController.currentCompany.getName())))
                .collect(Collectors.toList());
        expenseObservableList = FXCollections.observableArrayList(listOfExpenses);

        expenseTableView.setItems(expenseObservableList);


        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("prvi");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("drugi");


      //LineChart lineChart = new LineChart(x, y);

       XYChart.Series<String, Number> series = new XYChart.Series();
       // series.setName("2014");
        int mm = 0;

        for(Expense e: listOfExpenses) {
            series.getData().add(new XYChart.Data<String, Number>(e.getName(), e.getCost()));

        }
        lineChart.getData().add(series);


    }

    public void showCategory() throws SQLException {
        List<Category> listOfCategories = new ArrayList<>();
        fetchCategories(listOfCategories);

        for(Category c: listOfCategories)
            categoryPicker.getItems().addAll(c.getName());
    }



    List<Category> listOfCategories = new ArrayList<>();
    public void search(){
        String kategorija = categoryPicker.getValue();
        List<Category> categoryList = listOfCategories.stream().filter(bb -> bb.getName().toLowerCase().equals(kategorija.toLowerCase())).collect(Collectors.toList());


        LocalDate dateOne = dateFrom.getValue();
        Date getDateOne = Date.valueOf(dateFrom.getValue());
        Date getDateTwo =  Date.valueOf(dateTo.getValue());


        List<Expense> filteredList = listOfExpenses.stream().filter(bb -> bb.getCategory().getName().equals(kategorija) && bb.getDate().after(getDateOne) && bb.getDate().before(getDateTwo)).collect(Collectors.toList());
        expenseObservableList.clear();
        expenseObservableList.addAll(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    public void showAddExpensesScreen(javafx.event.ActionEvent event) throws IOException{

        Parent showFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("addExpenses.fxml"));
        Scene expensesScene = new Scene(showFrame, 800, 500);
        Main.getMainStage().setScene(expensesScene);

    }

    public void logout() throws IOException {
        Parent showFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        Scene loginScene = new Scene(showFrame, 800, 550);
        Main.getMainStage().setScene(loginScene);
    }
   
}
