package Model;

import Controller.Controller;

import java.time.LocalDateTime;
import java.util.*;

public class Order {
    private final LocalDateTime date = LocalDateTime.now();
    private final HashMap<Product,Integer> beerTable = new HashMap<>();
    private PaymentMethod paymentMethod = null;

    public Order() {
        for (Product beer : Controller.getBeer()) {
            beerTable.put(beer,0);
        }
    }

    public void order(Product beer, int count) {
        beerTable.replace(beer, beerTable.get(beer) + count);
    }

    public int getCount() {
        int count = 0;
        for (Integer amount : beerTable.values()) {
            count += amount;
        }
        return count;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
}
