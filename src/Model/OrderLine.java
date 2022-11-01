package Model;

public class OrderLine {
    private final ProductComponent product;
    private final int amount;
    private double cost;
    private Arrangement arrangement;

    OrderLine(ProductComponent product, int count, Arrangement arrangement) {
        this.product = product;
        this.amount = count;
        this.arrangement = arrangement;
    }

    public double calculateCost() {
        for (Price price : product.getPrices()) {
           if (price.getArrangement() == arrangement) {
               cost = price.getPrice() * amount;
           }
        }
        return cost;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return product + " (" + amount + ")  " + cost + " kr";
    }
}
