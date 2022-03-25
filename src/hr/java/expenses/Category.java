package hr.java.expenses;

public class Category extends Entity{
    public Category(long id,String name) {
       super(id, name);
    }

    @Override
    public String toString() {
        return getName();
    }
}
