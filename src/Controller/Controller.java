package Controller;

import Model.Beer;
import Storage.Storage;

import java.util.ArrayList;

public class Controller {
    private static final Storage storage = new Storage();

    public static void saveTheBeers() {
        storage.saveTheBeers();
    }

    public static void createBeer(String name) {
        storage.addBeer(new Beer(name));
    }

    public static ArrayList<Beer> getBeer() {
        return storage.getBeers();
    }
}
