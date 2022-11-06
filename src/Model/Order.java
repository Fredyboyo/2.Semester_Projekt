package Model;

import Model.DiscountStrategy.NoDiscountStrategy;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Order implements Serializable {
    private final LocalDateTime date = LocalDateTime.now();
    private final ArrayList<OrderLine> orderLines = new ArrayList<>();
    private final Arrangement arrangement;
    private double collectedCost;
    private PaymentMethod paymentMethod;
    private Discount discountStrategy = new NoDiscountStrategy();

    public Order(Arrangement arrangement) {
        this.arrangement = arrangement;
    }

    public double getUpdatedPrice() {
        updateCollectedCost();
        return collectedCost;
    }

    public OrderLine createOrderLine(ProductComponent product, int count) {
        OrderLine orderline = new OrderLine(product, count, arrangement);
        orderLines.add(orderline);
        getUpdatedPrice();
        return orderline;
    }

    public double getPrice(){
        return collectedCost;
    }

    public void updateCollectedCost(){
        collectedCost = 0;
        for (OrderLine orderLine : orderLines) {
            collectedCost += orderLine.getCost();
        }
        collectedCost = discountStrategy.discount(collectedCost);
    }

    public void removeOrderLine(OrderLine orderLine) {
        orderLines.remove(orderLine);
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Arrangement getArrangement() {
        return arrangement;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }


    public ArrayList<OrderLine> getOrderLines() {
        return orderLines;
    }

    @Override
    public String toString() {
        return date.toLocalDate() + " - " + arrangement + " - " + collectedCost;
    }

}
