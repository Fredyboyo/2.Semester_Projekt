package Model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Order {
    private final LocalDate date = LocalDate.now();
    private double collectedCost;
    private PaymentMethod paymentMethod;
    private final ArrayList<OrderLine> orderLines = new ArrayList<>();
    private Arrangement arrangement;

    public Order(PaymentMethod paymentMethod, Arrangement arrangement) {
        this.paymentMethod = paymentMethod;
        this.arrangement = arrangement;
    }

    public double calculateCollectedCost() {
        collectedCost = 0;
        for (OrderLine orderLine : orderLines) {
            collectedCost += orderLine.calculateCost();
        }
        return collectedCost;
    }

    public OrderLine createOrderLine(ProductComponent product, int count) {
        OrderLine orderline = new OrderLine(product, count, arrangement);
        orderLines.add(orderline);
        return orderline;
    }
}
