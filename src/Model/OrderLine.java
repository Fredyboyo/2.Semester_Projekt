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

    private void calculateCost() {
        for (Price price : product.getPrices()) {
           if (price.getArrangement() == arrangement) {
               cost = price.getPrice() * amount;
           }
        }
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

    public double getCost() {
        return cost;
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
