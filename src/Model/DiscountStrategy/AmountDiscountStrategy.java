package Model.DiscountStrategy;

import Model.Discount;


public class AmountDiscountStrategy implements Discount {

    private int amount;

    public AmountDiscountStrategy(int amount) {
        this.amount = amount;
    }

    @Override
    public double discount(double collectedCost) {
        return collectedCost - amount;
    }
}
