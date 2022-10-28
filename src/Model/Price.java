package Model;

public class Price {

    private double price;
    private Arrangement arrangement;
    ProductComponent product;

    public Price(double price, Arrangement arrangement, ProductComponent product) {
        this.price = price;
        this.arrangement = arrangement;
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




}
