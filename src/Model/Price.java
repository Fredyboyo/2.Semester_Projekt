package Model;

import java.io.Serializable;

public class Price implements Serializable {
    private double price;
    private Integer clips;
    private final Arrangement arrangement;
    ProductComponent product;

    Price(Arrangement arrangement, double price, Integer clips) {
        this.arrangement = arrangement;
        arrangement.prices.add(this);
        this.price = price;
        this.clips = clips;
    }

    public void setClips(Integer clips) {
        this.clips = clips;
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

    public Integer getClips() {
        return clips;
    }
}
