package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {
    private final String name;
    ArrayList<ProductComponent> products = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ProductComponent> getProducts() {
        return new ArrayList<>(products);
    }

    @Override
    public String toString() {
        return name;
    }
}
