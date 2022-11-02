package Model;

import java.util.ArrayList;

public class Category {
    private final String name;
    private final ArrayList<ProductComponent> products = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ProductComponent> getProducts() {
        return products;
    }

    public void addProduct(ProductComponent product) {
        products.add(product);
    }

    @Override
    public String toString() {
        return name + "";
    }
}
