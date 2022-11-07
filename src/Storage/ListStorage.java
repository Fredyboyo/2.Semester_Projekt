package Storage;

import Controller.Storage;
import Model.*;

import java.io.*;
import java.util.ArrayList;

public class ListStorage implements Storage, Serializable {
    private final ArrayList<ProductComponent> products = new ArrayList<>();
    private final ArrayList<Order> orders = new ArrayList<>();
    private final ArrayList<Arrangement> arrangements = new ArrayList<>();
    private final ArrayList<Category> categories = new ArrayList<>();
    private final ArrayList<PaymentMethod> paymentMethods = new ArrayList<>();
    private final ArrayList<Price> prices = new ArrayList<>();
    private final ArrayList<Discount> discounts = new ArrayList<>();

    // -------------------------------------------------------------------------

    public static ListStorage loadStorage() {
        try (ObjectInputStream out = new ObjectInputStream(new FileInputStream("StorageFile"))) {
            return (ListStorage) out.readObject();
        } catch (IOException e) {
            System.out.println("Failed");
        } catch (ClassNotFoundException e) {
            System.out.println("Class Not Found");
        } catch (ClassCastException e) {
            System.out.println("Class Cast Failed");
        }
        return null;
    }

    public static void saveStorage(Storage storage) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("StorageFile"))) {
            out.writeObject(storage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------

    @Override
    public void storeProduct(ProductComponent product) {
        products.add(product);
    }

    @Override
    public void removeProduct(ProductComponent product) {
        products.remove(product);
    }
    @Override
    public void storeOrder(Order order) {
        orders.add(order);
    }
    @Override
    public void storeCategory(Category category) {
        categories.add(category);
    }
    @Override
    public void storeArrangement(Arrangement arrangement) {
        arrangements.add(arrangement);
    }
    @Override
    public void storePrice(Price price) {
        prices.add(price);
    }
    @Override
    public void storePaymentMethod(PaymentMethod paymentMethod) {
        paymentMethods.add(paymentMethod);
    }
    @Override
    public void removePaymentMethod(PaymentMethod paymentMethod){
        paymentMethods.remove(paymentMethod);
    }

    @Override
    public ArrayList<ProductComponent> getProducts() {
        return products;
    }
    @Override
    public ArrayList<Order> getOrders() {
        return orders;
    }
    @Override
    public ArrayList<Arrangement> getArrangements() {
        return arrangements;
    }
    @Override
    public ArrayList<Category> getCategories() {
        return categories;
    }
    @Override
    public ArrayList<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }
    @Override
    public ArrayList<Price> getPrices() {
        return prices;
    }

    public void storeDiscount(Discount discount){
        discounts.add(discount);
    }

    public void deleteDiscount(Discount discount){
        discounts.remove(discount);
    }

    public ArrayList<Discount> getDiscounts() {
        return new ArrayList<>(discounts);
    }

}
