package Model;

import java.util.ArrayList;

public class Product implements ProductComponent {
    private final String name;
    private final Category category;
    private final ArrayList<Price> prices = new ArrayList<>();

    public Product(
            String name,
            Category category) {
        this.category = category;
        category.addProduct(this);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Category getCategory() {
        return category;
    }

    @Override
    public ArrayList<Price> getPrices() {
        return new ArrayList<>(prices);
    }

    @Override
    public Price createPrice(Arrangement arrangement, double kr) {
        Price price = new Price(arrangement, kr, this);
        price.product = this;
        prices.add(price);
        return price;
    }

    @Override
    public String toString() {
        return name + " " + category + " " + prices;
    }
}
