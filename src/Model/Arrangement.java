package Model;

import java.util.ArrayList;

public class Arrangement {
    private String name;
    ArrayList<Price> prices = new ArrayList<>();

    public Arrangement(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addPrice(Price price){
        prices.add(price);
    }

    public void removePrice(Price price){
        prices.remove(price);
    }
}
