package Storage;

import Model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final ArrayList<ProductComponent> products = new ArrayList<>();
    private final ArrayList<Order> orders = new ArrayList<>();
    private final ArrayList<Arrangement> arrangements = new ArrayList<>();
    private final ArrayList<Category> categories = new ArrayList<>();
    private final ArrayList<PaymentMethod> paymentMethods = new ArrayList<>();

    public void storeProduct(ProductComponent product) {
        products.add(product);
    }

    public ArrayList<ProductComponent> getProducts() {
        return new ArrayList<>(products);
    }

    public void storeOrder(Order order) {
        orders.add(order);
    }

    public ArrayList<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    public void storeArrangement(Arrangement arrangement) {
        arrangements.add(arrangement);
    }

    public ArrayList<Arrangement> getArrangements() {
        return new ArrayList<>(arrangements);
    }

    public void storeCategory(Category category) {
        categories.add(category);
    }

    public ArrayList<Category> getCategories() {
        return new ArrayList<>(categories);
    }

    public void storePaymentMethod(PaymentMethod paymentMethod) {
        paymentMethods.add(paymentMethod);
    }

    public ArrayList<PaymentMethod> getPaymentMethods() {
        return new ArrayList<>(paymentMethods);
    }
}
