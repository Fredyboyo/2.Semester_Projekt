package Storage;

import Model.Arrangement;
import Model.Category;
import Model.Product;
import Model.Order;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final ArrayList<Product> products = new ArrayList<>();
    private final ArrayList<Order> orders = new ArrayList<>();
    private final ArrayList<Arrangement> arrangements = new ArrayList<>();
    private final ArrayList<Category> categories = new ArrayList<>();

    public Storage() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("Beers"))){
            products.addAll(List.of((Product[]) in.readObject()));
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
        return products;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void addBeer(Product beer) {
        products.add(beer);
    }

    public void removeBeer(Product beer) {
        products.remove(beer);
    }

    public void saveTheProducts() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Beers"))){
            out.writeObject(products.toArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
