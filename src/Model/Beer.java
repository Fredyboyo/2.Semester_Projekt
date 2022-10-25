package Model;

import java.util.ArrayList;

public class Beer {
    private final String name;
    private final ArrayList<Integer> priceList = new ArrayList<>();

    public Beer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addPrice(int price) {
        priceList.add(price);
    }

    public ArrayList<Integer> getPriceList() {
        return priceList;
    }
}
