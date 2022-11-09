package Model.DiscountStrategy;

import Model.Discount;


public class AmountDiscountStrategy implements Discount {

    private double amount;

    public AmountDiscountStrategy(int amount) {
        this.amount = amount;
    }

    @Override
    public double discount(double collectedCost) {
        if (collectedCost != 0 || collectedCost > amount)
            return collectedCost - amount;
        else
            return 0;
    }

    @Override
    public void setValue(double var) {
        amount = var;
    }
}
