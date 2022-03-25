package hr.java.expenses;

import java.math.BigDecimal;

public class Company extends Entity{

    BigDecimal money;
    private String password;
    private String salt;

    public Company(long id,String name, String password, String salt) {
       super(id, name);

       this.password = password;
       this.salt = salt;
    }


    public String getPassword() { return password;    }

    public void setPassword(String password) {  this.password = password;    }

    public String getSalt() { return salt; }

    public void setSalt(String salt) { this.salt = salt; }
}
