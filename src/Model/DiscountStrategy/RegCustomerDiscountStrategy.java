package Model.DiscountStrategy;

import Model.Discount;

public class RegCustomerDiscountStrategy implements Discount {

    @Override
    public double discount(double collectedCost) {
        if (collectedCost != 0)
            return collectedCost * 0.85;
        else
            return 0;
    }

    @Override
    public void setValue(double var) {

    }
}
