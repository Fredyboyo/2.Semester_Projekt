package Model;

import Model.DiscountStrategy.AmountDiscountStrategy;
import Model.DiscountStrategy.NoDiscountStrategy;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Order implements Serializable {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final ArrayList<OrderLine> orderLines = new ArrayList<>();
    private final Arrangement arrangement;
    private double collectedCost;
    private int collectedClips;
    private PaymentMethod paymentMethod;
    private Discount discountStrategy = new NoDiscountStrategy();
    private boolean isFinished = false;

    public Order(Arrangement arrangement) {
        this.arrangement = arrangement;
    }

    public double getUpdatedPrice() {
        updateCollectedPrices();
        return collectedCost;
    }

    public OrderLine createOrderLine(ProductComponent product, int count) {
        OrderLine orderline = new OrderLine(product, count, arrangement);
        orderLines.add(orderline);
        getUpdatedPrice();
        return orderline;
    }

    public TourOrderLine createTourOrderLine(ProductComponent product, int amount, LocalDate localDate, Customer customer) {
        TourOrderLine tourOrderLine = new TourOrderLine(product, amount, arrangement, localDate, customer);
        orderLines.add(tourOrderLine);
        getUpdatedPrice();
        return tourOrderLine;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void finishOrder() {
        this.isFinished = true;
    }

    public void updateCollectedPrices(){
        double collectedCost = 0;
        int collectedClips = 0;

        for (OrderLine orderLine : orderLines) {
            orderLine.updatePrice();
            collectedCost += orderLine.getCost();
            collectedClips += orderLine.getClips();
        }
        this.collectedCost = discountStrategy.discount(collectedCost);
        if (discountStrategy.getClass() == AmountDiscountStrategy.class) {
            this.collectedClips = (int) (collectedClips * (this.collectedCost / collectedCost));
        } else {
            this.collectedClips = (int) discountStrategy.discount(collectedClips);
        }
    }

    public HashMap<ProductComponent, Integer> countSoldProduct(Category category, Arrangement arrangement){
        HashMap<ProductComponent, Integer> map = new HashMap<>();
        for (OrderLine ol : orderLines){
            if (ol.getArrangement() == arrangement && ol.getProduct().getCategory() == category){
                if (map.containsKey(ol.getProduct())){
                    map.put(ol.getProduct(), map.get(ol.getProduct()) + ol.getAmount());
                }
                else map.put(ol.getProduct(), ol.getAmount());
            }
        }
        return map;
    }

    public Discount getDiscountStrategy() {
        return discountStrategy;
    }

    public void setDiscountStrategy(Discount discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public void removeOrderLine(OrderLine orderLine) {
        orderLines.remove(orderLine);
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getCollectedClips() {
        return collectedClips;
    }

    public double getCollectedCost() {
        return collectedCost;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
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
        return arrangement + " - " + collectedCost + " - " + paymentMethod;
    }

}
