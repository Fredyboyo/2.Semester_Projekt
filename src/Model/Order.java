package Model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Order {
    private final LocalDateTime date = LocalDateTime.now();
    private double collectedCost;
    private PaymentMethod paymentMethod;
    private final ArrayList<OrderLine> orderLines = new ArrayList<>();
    private final Arrangement arrangement;

    public Order(Arrangement arrangement) {
        this.arrangement = arrangement;
    }

    public double calculateCollectedCost() {
        collectedCost = 0;
        for (OrderLine orderLine : orderLines) {
            collectedCost += orderLine.getCost();
        }
        return collectedCost;
    }

    public OrderLine createOrderLine(ProductComponent product, int count) {
        OrderLine orderline = new OrderLine(product, count, arrangement);
        orderLines.add(orderline);
        calculateCollectedCost();
        return orderline;
    }

    public void removeOrderLine(OrderLine orderLine) {
        orderLines.remove(orderLine);
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getCollectedCost() {
        return collectedCost;
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
        return date + " - " + arrangement + " - " + collectedCost;
    }

}
