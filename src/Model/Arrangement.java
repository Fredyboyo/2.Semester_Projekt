package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Arrangement implements Serializable {
    private final String name;
    ArrayList<Price> prices = new ArrayList<>();

    public Arrangement(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Price> getPrices() {
        return new ArrayList<>(prices);
    }

    @Override
    public String toString() {
        return name;
    }
}
