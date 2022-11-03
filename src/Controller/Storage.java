package Controller;

import Model.*;

import java.util.ArrayList;

public interface Storage {

    public void storeProduct(ProductComponent product);
    public void removeProduct(ProductComponent product);
    public void storeOrder(Order order);
    public void storeArrangement(Arrangement arrangement);
    public void storeCategory(Category category);
    public void storePaymentMethod(PaymentMethod paymentMethod);
    public void removePaymentMethod(PaymentMethod paymentMethod);
    public void storePrice(Price price);


    public ArrayList<Order> getOrders();
    public ArrayList<ProductComponent> getProducts();
    public ArrayList<Arrangement> getArrangements();
    public ArrayList<Category> getCategories();
    public ArrayList<PaymentMethod> getPaymentMethods();
    public ArrayList<Price> getPrices();
}
