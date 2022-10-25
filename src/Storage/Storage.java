package Storage;

import Model.Beer;
import Model.OrderList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final ArrayList<Beer> beerlist = new ArrayList<>();
    private final ArrayList<OrderList> orderLists = new ArrayList<>();

    private final String file = "Beers";

    public Storage() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))){
            beerlist.addAll(List.of((Beer[]) in.readObject()));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Beer> getBeers() {
        return beerlist;
    }

    public ArrayList<OrderList> getOrderLists() {
        return orderLists;
    }

    public void addBeer(Beer beer) {
        beerlist.add(beer);
    }

    public void removeBeer(Beer beer) {
        beerlist.remove(beer);
    }

    public void saveTheBeers() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))){
            out.writeObject(beerlist.toArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
