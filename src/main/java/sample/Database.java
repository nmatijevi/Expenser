package main.java.sample;

import hr.java.expenses.Category;
import hr.java.expenses.Company;
import hr.java.expenses.Expense;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Database {

    public static Connection connection;
    public static String userName;
    public static void connectToDatabase(){
            try{
                Properties svojstva = new Properties();
                svojstva.load(new FileReader("src/main/resources/database.properties"));

                String urlBazePodataka = svojstva.getProperty("bazaPodatakaUrl");
                userName = svojstva.getProperty("korisnickoIme");
                String password = svojstva.getProperty("lozinka");

                connection = DriverManager.getConnection(
                        urlBazePodataka, userName, password);
            } catch (SQLException | FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private static void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }

    public static void addExpense(Expense expense) throws SQLException{
        connectToDatabase();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO Expenses(name, cost,company_id, category_id, date) VALUES(?,?,?,?,?)");
        preparedStatement.setString(1, expense.getName());
        preparedStatement.setBigDecimal(2, expense.getCost());
        preparedStatement.setLong(3, expense.getCompany().getId());
        preparedStatement.setLong(4, expense.getCategory().getId());
        preparedStatement.setDate(5, expense.getDate());

        preparedStatement.executeUpdate();
        closeConnection(connection);
    }

    public static List<Expense> fetchExpenses(List<Expense> expenseList) throws SQLException{
        connectToDatabase();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM EXPENSES");
        while(rs.next()){
            int id = rs.getInt("id");
            String name = rs.getString("name");
            BigDecimal cost = rs.getBigDecimal("cost");
            Date date = rs.getDate("date");
            Long companyId = rs.getLong("company_id");
            Long categoryId = rs.getLong("category_id");
            Boolean cash = rs.getBoolean("cash");
            List<Company> companyList = new ArrayList<>();
            fetchCompanies(companyList);

            Company company = null;
            for(Company c: companyList){
                if (c.getId().equals(companyId)){
                    company = c;
                }
            }

            List<Category> categoryList = new ArrayList<>();
            fetchCategories(categoryList);
            Category category = null;
            for(Category c: categoryList){
                if (c.getId().equals(categoryId)){
                    category = c;
                }
            }

            Expense expense = new Expense(id,name, cost, date, company, category,cash);
            expenseList.add(expense);
        }
        closeConnection(connection);
        return expenseList;
    }
    public static void addCategory(Category category) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO Category(name) VALUES(?)");

        preparedStatement.setString(1, category.getName());
        preparedStatement.executeUpdate();

    }

    public static List<Category> fetchCategories(List<Category> categoryList) throws SQLException{
        connectToDatabase();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM CATEGORY");
        while(rs.next()){
            int id = rs.getInt("id");
            String name = rs.getString("name");
            Category category = new Category(id, name);
            categoryList.add(category);
        }
        closeConnection(connection);
        return categoryList;

    }

    public static List<Company> fetchCompanies(List<Company> companyList) throws SQLException{
        connectToDatabase();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM COMPANY");
        while(rs.next()){
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String password = rs.getString("password");
            String salt = rs.getString("salt");

            Company company = new Company(id, name, password,salt);
            companyList.add(company);
        }
        return companyList;
    }


    public static void registerCompany(Company company) throws SQLException {
        connectToDatabase();

        PreparedStatement upit = connection.prepareStatement(
                "INSERT INTO COMPANY(name, password,salt) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
        upit.setString(1, company.getName());
        upit.setString(2, (company.getPassword()));
        upit.setString(3,(company.getSalt()));

        upit.executeUpdate();

    }
}
