package Model;

import java.io.Serializable;

public class Price implements Serializable {
    private double price;
    private final Arrangement arrangement;
    ProductComponent product;

    Price(Arrangement arrangement, double price) {
        this.arrangement = arrangement;
        arrangement.prices.add(this);
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public Arrangement getArrangement() {
        return arrangement;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductComponent getProduct() {
        return product;
    }

    @Override
    public String toString() {
        return price + " kr i " + arrangement;
    }
}
