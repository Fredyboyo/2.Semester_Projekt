package Controller;

import Model.Product;
import Storage.Storage;

import java.util.ArrayList;

public class Controller {
    private static final Storage storage = new Storage();

    public static void saveTheBeers() {
        storage.saveTheBeers();
    }

    public static void createBeer(String name) {
        storage.addBeer(new Product(name));
    }

    public static ArrayList<Product> getBeer() {
        return storage.getBeers();
    }
}
