package Storage;

import Model.*;

import javax.crypto.interfaces.PBEKey;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final ArrayList<ProductComponent> products = new ArrayList<>();
    private final ArrayList<Order> orders = new ArrayList<>();
    private final ArrayList<Arrangement> arrangements = new ArrayList<>();
    private final ArrayList<Category> categories = new ArrayList<>();
    private final ArrayList<PaymentMethod> paymentMethods = new ArrayList<>();
    private final ArrayList<Price> prices = new ArrayList<>();

    public void storeProduct(ProductComponent product) {
        products.add(product);
    }

    public void removeProduct(ProductComponent product) {
        products.remove(product);
    }

    public ArrayList<ProductComponent> getProducts() {
        return products;
    }

    public void storeOrder(Order order) {
        orders.add(order);
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void storeArrangement(Arrangement arrangement) {
        arrangements.add(arrangement);
    }

    public ArrayList<Arrangement> getArrangements() {
        return arrangements;
    }

    public void storeCategory(Category category) {
        categories.add(category);
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void storePaymentMethod(PaymentMethod paymentMethod) {
        paymentMethods.add(paymentMethod);
    }

    public void removePaymentMethod(PaymentMethod paymentMethod){
        paymentMethods.remove(paymentMethod);
    }

    public ArrayList<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void storePrice(Price price) {
        prices.add(price);
    }

    public ArrayList<Price> getPrices() {
        return prices;
    }
}
