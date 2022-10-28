package Model;

public class Price {
    private double kr;
    private Arrangement arrangement;
    ProductComponent product;

    public Price(double kr, Arrangement arrangement, ProductComponent product) {
        this.kr = kr;
        this.arrangement = arrangement;
        this.product = product;
    }
}
