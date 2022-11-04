package Model.DiscountStrategy;

import Model.Discount;

public class NoDiscountStrategy implements Discount {

    @Override
    public double discount(double collectedCost) {
        return collectedCost;
    }

    @Override
    public void setValue(double var) {

    }
}
