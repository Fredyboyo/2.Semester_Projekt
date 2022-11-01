package Model;

public class OrderLine {
    private final ProductComponent product;
    private int amount;
    private double cost;
    private final Arrangement arrangement;

    OrderLine(ProductComponent product, int amount, Arrangement arrangement) {
        this.product = product;
        this.amount = amount;
        this.arrangement = arrangement;
        calculateCost();
    }

    public double calculateCost() {
        for (Price price : product.getPrices()) {
           if (price.getArrangement() == arrangement) {
               cost = price.getPrice() * amount;
           }
        }
        return cost;
    }

    @Override
    public String toString() {
        return product + " (" + amount + ")  " + cost + " kr";
    }

    public void append() {
        amount++;
        calculateCost();
    }

    public void deduct() {
        if (amount <= 1)
            return;
        amount--;
        calculateCost();
    }
}
