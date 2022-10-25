package Model;

import java.util.ArrayList;

public class Product {
    private final String name;
    private final ArrayList<Price> priceList = new ArrayList<>();

    public Product(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addPrice(Price price) {
        priceList.add(price);
    }

    public ArrayList<Price> getPriceList() {
        return priceList;
    }
}
