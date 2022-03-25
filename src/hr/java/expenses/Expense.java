package hr.java.expenses;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

public class Expense extends Entity implements Serializable {

    BigDecimal cost;
    Date date;
    Company company;
    Category category;
    Boolean cash;


    public Expense(long id, String name, BigDecimal cost,Date date ,Company company, Category category, Boolean cash) {
       super(id, name);
        this.cost = cost;
        this.date = date;
        this.company = company;
        this.category = category;
        this.cash = cash;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Boolean getCash() {
        return cash;
    }

    public void setCash(Boolean cash) {
        this.cash = cash;
    }
}
