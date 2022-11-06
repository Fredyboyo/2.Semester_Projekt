package Model;

import Model.DiscountStrategy.NoDiscountStrategy;

import java.io.Serializable;

public class OrderLine implements Serializable {
    private final ProductComponent product;
    private final Arrangement arrangement;
    private int amount;
    private double cost;
    private Discount discountStrategy = new NoDiscountStrategy();

    OrderLine(ProductComponent product, int amount, Arrangement arrangement) {
        this.product = product;
        this.amount = amount;
        this.arrangement = arrangement;
        updateCost();
    }

    private double getUpdatedPrice(){
        updateCost();
        return cost;
    }

    private void updateCost() {
        for (Price price : product.getPrices()) {
           if (price.getArrangement() == arrangement) {
               cost = price.getPrice() * amount;
           }
        }
        cost = discountStrategy.discount(cost);
    }

    public Discount getDiscountStrategy() {
        return discountStrategy;
    }

    public Arrangement getArrangement() {
        return arrangement;
    }

    public void setDiscountStrategy(Discount discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public double getCost() {
        return cost;
    }

    public void append() {
        amount++;
        updateCost();
    }

    public void deduct() {
        if (amount <= 1)
            return;
        amount--;
        updateCost();
    }

    public int getAmount() {
        return amount;
    }

    public ProductComponent getProduct() {
        return product;
    }

    @Override
    public String toString() {
        return product + " (" + amount + ")";
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
