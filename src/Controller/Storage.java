package Controller;

import Model.*;

import java.util.ArrayList;

public interface Storage {
    public void storeOrder(Order order);
    public void storePrice(Price price);
    public void storeCategory(Category category);
    public void storeProduct(ProductComponent product);
    public void removeProduct(ProductComponent product);
    public void storeArrangement(Arrangement arrangement);
    public void storePaymentMethod(PaymentMethod paymentMethod);
    public void removePaymentMethod(PaymentMethod paymentMethod);

    public ArrayList<Order> getOrders();
    public ArrayList<Price> getPrices();
    public ArrayList<Category> getCategories();
    public ArrayList<Arrangement> getArrangements();
    public ArrayList<ProductComponent> getProducts();
    public ArrayList<PaymentMethod> getPaymentMethods();
}
