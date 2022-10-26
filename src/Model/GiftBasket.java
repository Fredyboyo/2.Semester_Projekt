package Model;

import java.util.ArrayList;

public class GiftBasket {
    ArrayList<Product> products;

    public GiftBasket(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }
}
