package Model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Order {
    private final LocalDate date = LocalDate.now();
    private double collectedCost;
    private PaymentMethod paymentMethod;
    private final ArrayList<OrderLine> orderLines = new ArrayList<>();

    public Order() {

    }

    public double calculateCollectedCost() {
        /** Add Code */

        return collectedCost;
    }

    public OrderLine createOrderLine(ProductComponent product, int count) {
        OrderLine orderline = new OrderLine(product,count);
        orderLines.add(orderline);
        return orderline;
    }
}
