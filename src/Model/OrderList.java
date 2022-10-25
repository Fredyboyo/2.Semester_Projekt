package Model;

import Controller.Controller;

import java.time.LocalDateTime;
import java.util.*;

public class OrderList {
    private final LocalDateTime date = LocalDateTime.now();
    private final Hashtable<Beer,Integer> beerTable = new Hashtable<>();
    private PaymentMethod paymentMethod = null;

    public OrderList() {
        for (Beer beer : Controller.getBeer()) {
            beerTable.put(beer,0);
        }
    }

    public void order(Beer beer, int count) {
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
