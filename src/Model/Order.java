package Model;

import Model.DiscountStrategy.NoDiscountStrategy;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Order implements Serializable {
    private final LocalDateTime date = LocalDateTime.now();
    private final ArrayList<OrderLine> orderLines = new ArrayList<>();
    private final Arrangement arrangement;
    private double collectedCost;
    private PaymentMethod paymentMethod;
    private boolean isCompleted;
    private Discount discountStrategy = new NoDiscountStrategy();

    public Order(Arrangement arrangement) {
        this.arrangement = arrangement;
        this.isCompleted = false;
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


    public HashMap<ProductComponent, Integer>  countSoldProduct(Category category, Arrangement arrangement){
        HashMap<ProductComponent, Integer> map = new HashMap<>();
        for (OrderLine ol : orderLines){
            if (ol.getArrangement() == arrangement && ol.getProduct().getCategory() == category){
                if (map.containsKey(ol.getProduct())){
                    map.put(ol.getProduct(), map.get(ol.getProduct()) + ol.getAmount());
                }
                else
                    map.put(ol.getProduct(), ol.getAmount());
            }
        }
        return map;
    }



    public void removeOrderLine(OrderLine orderLine) {
        orderLines.remove(orderLine);
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setIsCompletedToTrue(){
        this.isCompleted = true;
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

    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public String toString() {
        return arrangement + " - " + collectedCost + " - " + paymentMethod;
    }

}
