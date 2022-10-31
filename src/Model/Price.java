package Model;

public class Price {

    private double price;
    private final Arrangement arrangement;
    ProductComponent product;

    public Price(Arrangement arrangement, double price, ProductComponent product) {
        this.arrangement = arrangement;
        this.price = price;
        this.product = product;
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

    @Override
    public String toString() {
        return price + " kr i " + arrangement;
    }
}
