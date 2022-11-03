package Model;

public class Price {

    private double price;
    private String priceType;
    private final Arrangement arrangement;
    private final ProductComponent product;

    Price(Arrangement arrangement, double price, String priceType, ProductComponent product) {
        this.arrangement = arrangement;
        arrangement.prices.add(this);
        this.price = price;
        this.priceType = priceType;
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

    public ProductComponent getProduct() {
        return product;
    }

    @Override
    public String toString() {
        return price + " kr i " + arrangement;
    }
}
