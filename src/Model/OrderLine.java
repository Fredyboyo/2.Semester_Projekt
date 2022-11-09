package Model;

import Model.DiscountStrategy.AmountDiscountStrategy;
import Model.DiscountStrategy.NoDiscountStrategy;

import java.io.Serializable;

public class OrderLine implements Serializable {
    private final ProductComponent product;
    private final Arrangement arrangement;
    private int amount;
    private double cost;
    private int clips;
    private Discount discountStrategy = new NoDiscountStrategy();

    OrderLine(ProductComponent product, int amount, Arrangement arrangement) {
        this.product = product;
        this.amount = amount;
        this.arrangement = arrangement;
        updatePrice();
    }

    public double getUpdatedPrice(){
        updatePrice();
        return cost;
    }

    public void updatePrice() {
        double cost = 0;
        for (Price price : product.getPrices()) {
           if (price.getArrangement() != arrangement) continue;
           cost = price.getPrice() * amount;
           if (price.getClips() != null) {
               clips = price.getClips() * amount;
           }
        }
        this.cost = discountStrategy.discount(cost);
        if (discountStrategy.getClass() == AmountDiscountStrategy.class) {
            clips = (int) (clips * (this.cost / cost));
        } else {
            clips = (int) discountStrategy.discount(clips);
        }
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

    public int getClips() {
        return clips;
    }

    public void append() {
        amount++;
        updatePrice();
    }

    public void deduct() {
        if (amount <= 1)
            return;
        amount--;
        updatePrice();
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
