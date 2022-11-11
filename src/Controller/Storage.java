package Controller;

import Model.*;

import java.util.ArrayList;

public interface Storage {
    void storeOrder(Order order);
    void storePrice(Price price);
    void storeCategory(Category category);
    void storeProduct(ProductComponent product);
    void removeProduct(ProductComponent product);
    void storeArrangement(Arrangement arrangement);
    void storePaymentMethod(PaymentMethod paymentMethod);
    void removePaymentMethod(PaymentMethod paymentMethod);
    void storeDiscount(Discount discount);
    void deleteDiscount(Discount discount);

    ArrayList<Order> getOrders();
    ArrayList<Price> getPrices();
    ArrayList<Category> getCategories();
    ArrayList<Arrangement> getArrangements();
    ArrayList<ProductComponent> getProducts();
    ArrayList<PaymentMethod> getPaymentMethods();
    ArrayList<Discount> getDiscounts();
}
