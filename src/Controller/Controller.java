package Controller;

import Model.Category;
import Model.Product;
import Storage.Storage;

import java.util.ArrayList;

public class Controller {
    private static final Storage storage = new Storage();

    public static void saveTheBeers() {
        storage.saveTheBeers();
    }

    public static void createBeer(String name, Category category) {
        storage.addBeer(new Product(name, category));
    }

    public static ArrayList<Product> getBeer() {
        return storage.getBeers();
    }
}
