package Model.DiscountStrategy;

import Model.Discount;

public class RegCustomerDiscountStrategy implements Discount {

    @Override
    public double discount(double collectedCost) {
        return collectedCost * 0.85;
    }
}
