package Model.DiscountStrategy;

import Model.Discount;

public class StudentDiscountStrategy implements Discount {

    @Override
    public double discount(double collectedCost) {
        return collectedCost * 0.9;
    }

    @Override
    public void setValue(double var) {

    }
}
