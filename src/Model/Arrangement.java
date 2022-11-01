package Model;

import java.util.ArrayList;

public class Arrangement {
    private final String name;
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

    public ArrayList<Price> getPrices() {
        return new ArrayList<>(prices);
    }

    @Override
    public String toString() {
        return name + "";
    }
}
