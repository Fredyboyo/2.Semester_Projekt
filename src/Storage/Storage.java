package Storage;

import Model.Arrangement;
import Model.Product;
import Model.Order;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final ArrayList<Product> beerlist = new ArrayList<>();
    private final ArrayList<Order> orderLists = new ArrayList<>();
    private final ArrayList<Arrangement> arrangements = new ArrayList<>();

    public Storage() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("Beers"))){
            beerlist.addAll(List.of((Product[]) in.readObject()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println();
        } catch (ClassCastException e) {
            System.out.println("Exception 3");
        }
    }

    public ArrayList<Product> getBeers() {
        return beerlist;
    }

    public ArrayList<Order> getOrderLists() {
        return orderLists;
    }

    public void addBeer(Product beer) {
        beerlist.add(beer);
    }

    public void removeBeer(Product beer) {
        beerlist.remove(beer);
    }

    public void saveTheBeers() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Beers"))){
            out.writeObject(beerlist.toArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
