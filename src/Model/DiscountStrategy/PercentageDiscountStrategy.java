package Model.DiscountStrategy;

import Model.Discount;

public class PercentageDiscountStrategy implements Discount {
    private double percentage;

    @Override
    public double discount(double collectedCost) {
        if (collectedCost != 0)
            return collectedCost * (1 - (percentage / 100));
        else
            return 0;
    }

    @Override
    public void setValue(double var) {
        percentage = var;
    }
}
