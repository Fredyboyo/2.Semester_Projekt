package Model;

import java.util.ArrayList;

public class Category {

    private final String name;
    private ArrayList<Product> products = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
