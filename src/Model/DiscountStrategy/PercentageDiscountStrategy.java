package Model.DiscountStrategy;

import Model.Discount;

public class PercentageDiscountStrategy implements Discount {

    private double percentage;

    public PercentageDiscountStrategy(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public double discount(double collectedCost) {
        return collectedCost * (1 - percentage);
    }
}
